package by.it.group310951.oktysyuk.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {
    private class Node {
        int key;
        String value;
        int height;
        Node left, right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            height = 1;
        }
    }

    private Node root;
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get((Integer) key) != null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getNode(root, (Integer) key);
        return node == null ? null : node.value;
    }

    private Node getNode(Node node, int key) {
        if (node == null) return null;
        if (key < node.key) return getNode(node.left, key);
        else if (key > node.key) return getNode(node.right, key);
        else return node;
    }

    @Override
    public String put(Integer key, String value) {
        Node[] result = insert(root, key, value);
        root = result[0];
        return result[1] == null ? null : result[1].value;
    }

    private Node[] insert(Node node, int key, String value) {
        if (node == null) {
            size++;
            return new Node[]{new Node(key, value), null};
        }

        Node oldNode;
        if (key < node.key) {
            Node[] res = insert(node.left, key, value);
            node.left = res[0];
            oldNode = res[1];
        } else if (key > node.key) {
            Node[] res = insert(node.right, key, value);
            node.right = res[0];
            oldNode = res[1];
        } else {
            oldNode = new Node(node.key, node.value);
            node.value = value;
            return new Node[]{node, oldNode};
        }

        updateHeight(node);
        return new Node[]{balance(node), oldNode};
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node[] result = delete(root, (Integer) key);
        root = result[0];
        return result[1] == null ? null : result[1].value;
    }

    private Node[] delete(Node node, int key) {
        if (node == null) return new Node[]{null, null};

        Node removed;
        if (key < node.key) {
            Node[] res = delete(node.left, key);
            node.left = res[0];
            removed = res[1];
        } else if (key > node.key) {
            Node[] res = delete(node.right, key);
            node.right = res[0];
            removed = res[1];
        } else {
            removed = node;
            size--;
            if (node.left == null || node.right == null) {
                return new Node[]{(node.left != null) ? node.left : node.right, removed};
            } else {
                Node minLarger = findMin(node.right);
                node.key = minLarger.key;
                node.value = minLarger.value;
                Node[] res = delete(node.right, minLarger.key);
                node.right = res[0];
            }
        }

        updateHeight(node);
        return new Node[]{balance(node), removed};
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node balance(Node node) {
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0)
                node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1) {
            if (getBalance(node.right) > 0)
                node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2); // remove trailing ", "
        sb.append("}");
        return sb.toString();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrder(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrder(node.right, sb);
    }

    // Остальные методы Map — либо не реализованы, либо выбрасывают исключения

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Not implemented");
    }
}