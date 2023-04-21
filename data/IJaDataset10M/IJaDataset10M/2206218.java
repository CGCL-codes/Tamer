package ar.uba.fi.tonyvaliente.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LeafBPlusTreeNode<K extends Comparable<K>, V> extends BPlusTreeNode<K, V> {

    private BPlusTreeNodeReference<K, V> previousReference;

    private BPlusTreeNodeReference<K, V> nextReference;

    private Map<K, V> data;

    protected LeafBPlusTreeNode(BPlusTree<K, V> tree, BPlusTreeNodeReference<K, V> parent) {
        super(tree, parent);
        this.setData(new TreeMap<K, V>());
    }

    protected boolean isInner() {
        return false;
    }

    protected boolean isLeaf() {
        return true;
    }

    @Override
    protected AddResult add(K key, V value) throws BPlusTreeException {
        ArrayList<K> kk = new ArrayList<K>(data.keySet());
        int a = kk.size();
        a = a / 1;
        data.put(key, value);
        kk = new ArrayList<K>(data.keySet());
        a = kk.size();
        a = a / 1;
        if (data.size() > tree.elementsPerNode) {
            return split();
        } else {
            tree.addDirtyNodeReference(thisReference);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private AddResult split() throws BPlusTreeException {
        LeafBPlusTreeNode<K, V> leftNode = this;
        LeafBPlusTreeNode<K, V> rightNode = new LeafBPlusTreeNode<K, V>(this.getTree(), this.getParentReference());
        List<K> keys = new ArrayList<K>(leftNode.getData().keySet());
        Collections.sort(keys);
        int middle = keys.size() / 2;
        K middleKey = keys.get(middle);
        for (K key : keys) {
            if (key.compareTo(middleKey) >= 0) {
                rightNode.getData().put(key, leftNode.getData().get(key));
                leftNode.getData().remove(key);
            }
        }
        BPlusTreeNodeReference<K, V> leftReference = thisReference;
        BPlusTreeNodeReference<K, V> rightReference = tree.createReference();
        rightNode.setNextReference(leftNode.getNextReference());
        rightNode.setPreviousReference(leftNode.getThisReference());
        rightReference.setNode(rightNode);
        leftNode.setNextReference(rightReference);
        if (rightNode.getNextReference() != null) {
            LeafBPlusTreeNode<K, V> rightRightNode = (LeafBPlusTreeNode<K, V>) rightNode.getNextReference().getNode();
            rightRightNode.setPreviousReference(rightReference);
        }
        ArrayList<K> kk1 = new ArrayList<K>(leftNode.data.keySet());
        int a1 = kk1.size();
        a1 = a1 / 1;
        ArrayList<K> kk2 = new ArrayList<K>(rightNode.data.keySet());
        int a2 = kk2.size();
        a2 = a2 / 1;
        tree.addDirtyNodeReference(leftReference);
        tree.addDirtyNodeReference(rightReference);
        if (parentReference != null) {
            tree.addDirtyNodeReference(parentReference);
        }
        if (rightNode.getNextReference() != null) {
            tree.addDirtyNodeReference(rightNode.getNextReference());
        }
        return new AddResult(middleKey, leftReference, rightReference);
    }

    @Override
    protected V get(K key) {
        ArrayList<K> kk = new ArrayList<K>(data.keySet());
        int a = kk.size();
        a = a / 1;
        return data.get(key);
    }

    protected Map<K, V> getData() {
        return this.data;
    }

    protected void setData(Map<K, V> data) {
        this.data = data;
    }

    protected BPlusTreeNodeReference<K, V> getPreviousReference() {
        return this.previousReference;
    }

    protected void setPreviousReference(BPlusTreeNodeReference<K, V> previousReference) {
        this.previousReference = previousReference;
    }

    public BPlusTreeNodeReference<K, V> getNextReference() {
        return this.nextReference;
    }

    protected void setNextReference(BPlusTreeNodeReference<K, V> nextReference) {
        this.nextReference = nextReference;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void validate() throws BPlusTreeException {
        BPlusTreeNodeReference<K, V> parentReference = this.getParentReference();
        BPlusTreeNodeReference<K, V> previousReference = this.getPreviousReference();
        BPlusTreeNodeReference<K, V> nextReference = this.getNextReference();
        if (parentReference == null) {
            if (previousReference != null) {
                throw new BPlusTreeException("Error 3: the node cannot has a previous reference because it is a root node");
            }
            if (nextReference != null) {
                throw new BPlusTreeException("Error 4: the node cannot has a next reference because it is a root node");
            }
        } else {
            BPlusTreeNode<K, V> parentNode = parentReference.getNode();
            if (parentNode == null) {
                throw new BPlusTreeException("Error 5: cannot retrieve parent node");
            }
            if (!(parentNode instanceof InnerBPlusTreeNode)) {
                throw new BPlusTreeException("Error 6: node's parent must be a InnerNode");
            }
            Map<K, V> data = this.getData();
            List<K> keys = new ArrayList<K>(data.keySet());
            Collections.sort(keys);
            K minKey = keys.get(0);
            K maxKey = keys.get(keys.size() - 1);
            InnerBPlusTreeNode<K, V> innerNode = (InnerBPlusTreeNode<K, V>) parentNode;
            List<K> parentKeys = innerNode.getKeys();
            int referenceIndex = -1;
            for (int i = 0; i < parentKeys.size(); i++) {
                if (maxKey.compareTo(parentKeys.get(i)) < 0) {
                    referenceIndex = i;
                    break;
                }
            }
            if (referenceIndex == -1) {
                referenceIndex = parentKeys.size();
            }
            BPlusTreeNode<K, V> node = innerNode.getReferences().get(referenceIndex).getNode();
            if (node == null) {
                throw new BPlusTreeException("Error 7:");
            }
            if (!(node instanceof LeafBPlusTreeNode)) {
                throw new BPlusTreeException("Error 8:");
            }
            LeafBPlusTreeNode<K, V> other = (LeafBPlusTreeNode<K, V>) node;
            Map<K, V> otherData = other.getData();
            if (otherData.size() != data.size()) {
                throw new BPlusTreeException("Error 9:");
            }
            for (K key : otherData.keySet()) {
                if (!data.containsKey(key)) {
                    throw new BPlusTreeException("Error 10:");
                }
                if (!data.get(key).equals(otherData.get(key))) {
                    throw new BPlusTreeException("Error 11:");
                }
            }
            if (referenceIndex > 0 && previousReference == null) {
                throw new BPlusTreeException("Error 12:");
            }
            if (referenceIndex < parentKeys.size() && nextReference == null) {
                throw new BPlusTreeException("Error 13:");
            }
            if (previousReference != null) {
                node = previousReference.getNode();
                if (!(node instanceof LeafBPlusTreeNode)) {
                    throw new BPlusTreeException("Error 14:");
                }
                LeafBPlusTreeNode<K, V> previousNode = (LeafBPlusTreeNode<K, V>) node;
                Map<K, V> previousData = previousNode.getData();
                for (K key : previousData.keySet()) {
                    if (key.compareTo(minKey) >= 0) {
                        throw new BPlusTreeException("Error 15:");
                    }
                }
            }
            if (nextReference != null) {
                node = nextReference.getNode();
                if (!(node instanceof LeafBPlusTreeNode)) {
                    throw new BPlusTreeException("Error 16:");
                }
                LeafBPlusTreeNode<K, V> nextNode = (LeafBPlusTreeNode<K, V>) node;
                Map<K, V> nextData = nextNode.getData();
                for (K key : nextData.keySet()) {
                    if (key.compareTo(maxKey) <= 0) {
                        throw new BPlusTreeException("Error 17:");
                    }
                }
            }
        }
    }
}
