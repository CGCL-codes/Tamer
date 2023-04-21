package uebung09.ml.aufgabe04;

import gvs.tree.GVSBinaryTreeNode;
import gvs.tree.GVSTreeWithRoot;
import gvs.typ.node.GVSNodeTyp;

public class BinarySearchTreeGVS<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {

    protected GVSTreeWithRoot gvsTree = new GVSTreeWithRoot("BinaryTreeTestGVS");

    protected class NodeGVS extends BinarySearchTree<K, V>.Node<K, V> implements GVSBinaryTreeNode {

        protected NodeGVS(Entry<K, V> entry) {
            super(entry);
        }

        public GVSBinaryTreeNode getGVSLeftChild() {
            return (GVSBinaryTreeNode) getLeftChild();
        }

        public GVSBinaryTreeNode getGVSRightChild() {
            return (GVSBinaryTreeNode) getRightChild();
        }

        public String getNodeLabel() {
            Entry<K, V> e = getEntry();
            return e.getKey().toString() + " / " + e.getValue().toString();
        }

        public GVSNodeTyp getNodeTyp() {
            return null;
        }
    }

    @Override
    protected Node newNode(Entry<K, V> entry) {
        return new NodeGVS(entry);
    }

    @Override
    public Entry<K, V> insert(K key, V value) {
        Entry<K, V> newEntry = new Entry<K, V>(key, value);
        root = insert(root, newEntry);
        gvsTree.setRoot((GVSBinaryTreeNode) root);
        gvsTree.display();
        return newEntry;
    }

    @Override
    public Entry<K, V> remove(Entry<K, V> entry) {
        Entry<K, V> deletedEntry = super.remove(entry);
        gvsTree.display();
        return deletedEntry;
    }
}
