package uk.co.massycat.appreviewsfinder.reviews;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 *
 * @author ben
 */
public class ReviewsLayout implements LayoutManager {

    static final Dimension mReviewSize;

    static {
        ReviewPanel review = new ReviewPanel();
        mReviewSize = review.getPreferredSize();
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);
        return dim;
    }

    public Dimension minimumLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);
        return dim;
    }

    public void layoutContainer(Container parent) {
        int nComps = parent.getComponentCount();
        for (int i = 0; i < nComps; i++) {
            Component comp = parent.getComponent(i);
            if (comp.isVisible()) {
                if (comp instanceof ReviewPanel) {
                    ReviewPanel review = (ReviewPanel) comp;
                    comp.setBounds(0, review.mReviewNumber * mReviewSize.height, parent.getSize().width, mReviewSize.height);
                }
            }
        }
    }
}
