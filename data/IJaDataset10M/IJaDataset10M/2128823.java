package net.sf.jtreemap.swing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abtract class with the method which split the elements of a JTreeMap.
 * <p>
 * The split is done by dichotomy. We split the elements in 2 groups with a
 * defined strategy (for example : take care of the weight of the elements)
 * <p>
 * 
 * @author Laurent Dutheil
 */
public abstract class SplitStrategy implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * calculate the positions for all the elements of the root.
     * 
     * @param root
     *            the root to calculate
     */
    public void calculatePositions(final TreeMapNode root) {
        if (root == null) {
            return;
        }
        final List<TreeMapNode> v = root.getChildren();
        if (v != null && !v.isEmpty()) {
            calculatePositionsRec(root.getX(), root.getY(), root.getWidth(), root.getHeight(), this.sumWeight(v), v);
        }
    }

    /**
     * split the elements of a JTreeMap.
     * 
     * @param v
     *            List with the elements to split (arg IN)
     * @param v1
     *            first List of the split (arg OUT)
     * @param v2
     *            second List of the split (arg OUT)
     */
    public abstract void splitElements(List<TreeMapNode> v, List<TreeMapNode> v1, List<TreeMapNode> v2);

    /**
     * Sum the weight of elements. <BR>
     * You can override this method if you want to apply a coef on the weights
     * or to cancel the effect of weight on the strategy.
     * 
     * @param v
     *            List with the elements to sum
     * @return the sum of the weight of elements
     */
    public double sumWeight(final List<TreeMapNode> v) {
        double d = 0.0;
        if (v != null) {
            final int size = v.size();
            for (int i = 0; i < size; i++) {
                d += (v.get(i)).getWeight();
            }
        }
        return d;
    }

    protected void calculatePositionsRec(final int x0, final int y0, final int w0, final int h0, final double weight0, final List<TreeMapNode> v) {
        if (v.isEmpty()) {
            return;
        }
        if (w0 * h0 < 20) {
            return;
        }
        if (w0 * h0 < v.size()) {
            return;
        }
        if (v.size() == 1) {
            final TreeMapNode f = v.get(0);
            if (f.isLeaf()) {
                int w = w0 - TreeMapNode.getBorder();
                if (w < 0) {
                    w = 0;
                }
                int h = h0 - TreeMapNode.getBorder();
                if (h < 0) {
                    h = 0;
                }
                f.setDimension(x0 + TreeMapNode.getBorder(), y0 + TreeMapNode.getBorder(), w, h);
            } else {
                f.setDimension(x0, y0, w0, h0);
                int bSub;
                if (TreeMapNode.getBorder() > 1) {
                    bSub = 2;
                } else if (TreeMapNode.getBorder() == 1) {
                    bSub = 1;
                } else {
                    bSub = 0;
                }
                int w = w0 - bSub;
                if (w < 0) {
                    w = 0;
                }
                int h = h0 - bSub;
                if (h < 0) {
                    h = 0;
                }
                TreeMapNode.setBorder(TreeMapNode.getBorder() - bSub);
                calculatePositionsRec(x0 + bSub, y0 + bSub, w, h, weight0, f.getChildren());
                TreeMapNode.setBorder(TreeMapNode.getBorder() + bSub);
            }
        } else {
            final List<TreeMapNode> v1 = new ArrayList<TreeMapNode>();
            final List<TreeMapNode> v2 = new ArrayList<TreeMapNode>();
            double weight1;
            double weight2;
            this.splitElements(v, v1, v2);
            weight1 = this.sumWeight(v1);
            weight2 = this.sumWeight(v2);
            int w1;
            int w2;
            int h1;
            int h2;
            int x2;
            int y2;
            if (w0 > h0) {
                w1 = (int) (w0 * weight1 / weight0);
                w2 = w0 - w1;
                h1 = h0;
                h2 = h0;
                x2 = x0 + w1;
                y2 = y0;
            } else {
                w1 = w0;
                w2 = w0;
                h1 = (int) (h0 * weight1 / weight0);
                h2 = h0 - h1;
                x2 = x0;
                y2 = y0 + h1;
            }
            calculatePositionsRec(x0, y0, w1, h1, weight1, v1);
            calculatePositionsRec(x2, y2, w2, h2, weight2, v2);
        }
    }

    /**
     * Sort the elements by descending weight.
     * 
     * @param v
     *            List with the elements to be sorted
     */
    protected void sortList(final List<TreeMapNode> v) {
        TreeMapNode tmn;
        for (int i = 0; i < v.size(); i++) {
            for (int j = v.size() - 1; j > i; j--) {
                if ((v.get(j)).getWeight() > (v.get(j - 1)).getWeight()) {
                    tmn = (v.get(j));
                    v.set(j, v.get(j - 1));
                    v.set(j - 1, tmn);
                }
            }
        }
    }

    protected void workOutWeight(final List<TreeMapNode> v1, final List<TreeMapNode> v2, final List<TreeMapNode> vClone, final double sumWeight) {
        double memWeight = 0.0;
        double elemWeight = 0.0;
        for (final Iterator<TreeMapNode> i = vClone.iterator(); i.hasNext(); ) {
            TreeMapNode tmn = i.next();
            elemWeight = tmn.getWeight();
            if (memWeight + elemWeight >= sumWeight / 2) {
                if (((sumWeight / 2) - memWeight) > ((memWeight + elemWeight) - (sumWeight / 2))) {
                    memWeight += elemWeight;
                    v1.add(tmn);
                } else {
                    if (v1.isEmpty()) {
                        v1.add(tmn);
                    } else {
                        v2.add(tmn);
                    }
                }
                while (i.hasNext()) {
                    tmn = i.next();
                    v2.add(tmn);
                }
            } else {
                memWeight += elemWeight;
                v1.add(tmn);
            }
        }
    }
}
