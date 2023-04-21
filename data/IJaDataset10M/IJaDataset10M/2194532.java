package ch.unifr.dmlib.cluster;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ilya Boyandin
 */
public class ClusterNode<T> {

    public static final int NO_ITEM_INDEX = -1;

    private final ClusterNode<T> leftChild;

    private final ClusterNode<T> rightChild;

    private final double distance;

    private final int childNesting;

    /**
     * Index of the item in the input item list (is set to
     * NO_ITEM_INDEX if this cluster node is not a leaf node)
     */
    private final int itemIndex;

    private final T item;

    /**
     * Create leaf cluster node (containing only one itemIndex)
     *
     * @param itemIndex Index of the item in the original item list.
     */
    public ClusterNode(T item, int itemIndex) {
        if (itemIndex < 0) {
            throw new IllegalArgumentException("itemIndex should be positive");
        }
        this.item = item;
        this.itemIndex = itemIndex;
        this.leftChild = null;
        this.rightChild = null;
        this.childNesting = 0;
        this.distance = Double.NaN;
    }

    /**
     * Create a non-leaf cluster node.
     *
     * @param distance Distance between the left and right child nodes
     */
    public ClusterNode(ClusterNode<T> leftChild, ClusterNode<T> rightChild, double distance) {
        this(null, leftChild, rightChild, distance);
    }

    /**
     * Create a non-leaf cluster node with an aggregated item.
     * <p>
     * Use this constructor only if you need to store the aggregated item associated with
     * the cluster in the ClusterNode. Otherwise you should prefer
     * {@link #ClusterNode(ClusterNode,ClusterNode,double)}.
     * <p>
     * The {@code itemIndex} will be set to {@code NO_ITEM_INDEX}, because the aggregated
     * item is not supposed to be in the original item list.
     *
     * @param item Aggregated item associated with the cluster.
     */
    public ClusterNode(T item, ClusterNode<T> leftChild, ClusterNode<T> rightChild, double distance) {
        this.item = item;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.childNesting = Math.max(leftChild.childNesting, rightChild.childNesting) + 1;
        this.itemIndex = NO_ITEM_INDEX;
        this.distance = distance;
    }

    public boolean isLeafNode() {
        return (leftChild == null && rightChild == null);
    }

    public T getItem() {
        return item;
    }

    public int getItemIndex() {
        if (itemIndex == NO_ITEM_INDEX) {
            throw new IllegalStateException("Cluster node doesn't have an associated item");
        }
        return itemIndex;
    }

    public ClusterNode<T> getLeftChild() {
        return leftChild;
    }

    public ClusterNode<T> getRightChild() {
        return rightChild;
    }

    public double getDistance() {
        return distance;
    }

    public List<T> listItems() {
        ArrayList<T> elems = new ArrayList<T>();
        listItems(elems);
        return elems;
    }

    private void listItems(List<T> items) {
        if (isLeafNode()) {
            items.add(item);
        } else {
            if (leftChild != null) {
                leftChild.listItems(items);
            }
            if (rightChild != null) {
                rightChild.listItems(items);
            }
        }
    }

    public int[] getItemIndices() {
        final int[] items;
        if (itemIndex != NO_ITEM_INDEX) {
            items = new int[] { itemIndex };
        } else {
            final int[] left = leftChild.getItemIndices();
            final int[] right = rightChild.getItemIndices();
            items = new int[left.length + right.length];
            System.arraycopy(left, 0, items, 0, left.length);
            System.arraycopy(right, 0, items, left.length, right.length);
        }
        return items;
    }

    public int getChildNesting() {
        return childNesting;
    }

    public String dumpToString() {
        final StringBuilder sb = new StringBuilder();
        traverseClusters(this, new ClusterVisitor<T>() {

            public void beforeChildren(ClusterNode<T> cn) {
                if (cn.isLeafNode()) {
                    sb.append("'" + cn.getItem().toString() + "'");
                } else {
                    sb.append("{");
                }
            }

            public void betweenChildren(ClusterNode<T> cn) {
            }

            public void afterChildren(ClusterNode<T> cn) {
                if (!cn.isLeafNode()) {
                    sb.append("}");
                }
            }
        });
        return sb.toString();
    }

    public String dumpToTreeString() {
        final StringBuilder sb = new StringBuilder();
        traverseClusters(this, new ClusterVisitor<T>() {

            int level = 0;

            public void beforeChildren(ClusterNode<T> cn) {
                for (int i = 0; i < level; i++) {
                    sb.append(" ");
                }
                if (cn.isLeafNode()) {
                    sb.append(cn.getItem() + "\n");
                } else {
                    sb.append("+");
                }
                level++;
            }

            public void betweenChildren(ClusterNode<T> cn) {
            }

            public void afterChildren(ClusterNode<T> cn) {
                level--;
            }
        });
        return sb.toString();
    }

    public void traverse(ClusterVisitor<T> v) {
        v.beforeChildren(this);
        if (!this.isLeafNode()) {
            traverseClusters(this.getLeftChild(), v);
            v.betweenChildren(this);
            traverseClusters(this.getRightChild(), v);
        }
        v.afterChildren(this);
    }

    public static <T> void traverseClusters(ClusterNode<T> start, ClusterVisitor<T> v) {
        start.traverse(v);
    }

    @Override
    public String toString() {
        return "ClusterNode [childNesting=" + childNesting + ", distance=" + distance + ", item=" + item + ", itemIndex=" + itemIndex + ", leftChild=" + leftChild + ", rightChild=" + rightChild + "]";
    }
}
