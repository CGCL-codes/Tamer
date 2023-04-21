package riafswing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import riaf.facade.IComponent;
import riaf.facade.IContainer;

/**
 * The Class AbsoluteLayout.
 */
public class AbsoluteLayout extends BaseLayout {

    /**
	 * Instantiates a new absolute layout.
	 *
	 * @param container
	 *            the container
	 */
    public AbsoluteLayout(IContainer container) {
        super(container);
    }

    @Override
    public void layoutContainer(Container parent) {
        if (sizeUnknown) {
            setSizes(parent);
        }
        int nComps = container.getChildElements().size();
        Insets zeroMargin = new Insets(0, 0, 0, 0);
        for (int i = 0; i < nComps; i++) {
            IComponent c = container.getChildElements().get(i);
            Component cImpl = (Component) c.getImpl();
            Dimension pref = c.getPreferredSize();
            Dimension min = c.getMinimumSize();
            Insets margin = c.getMargin();
            if (margin == null) margin = zeroMargin;
            if (cImpl.isVisible() && parent == cImpl.getParent()) {
                cImpl.setBounds(cImpl.getX(), cImpl.getY(), Math.max(pref.width, min.width), Math.max(pref.height, min.height));
            }
        }
    }

    /**
	 * Sets the sizes.
	 *
	 * @param parent
	 *            the new sizes
	 */
    private void setSizes(Container parent) {
        int nComps = container.getChildElements().size();
        Dimension pref = null, min = null;
        preferredWidth = 0;
        preferredHeight = 0;
        minWidth = 0;
        minHeight = 0;
        Insets zeroMargin = new Insets(0, 0, 0, 0);
        for (int i = 0; i < nComps; i++) {
            IComponent c = container.getChildElements().get(i);
            Component cImpl = (Component) c.getImpl();
            Insets margin = c.getMargin();
            if (margin == null) margin = zeroMargin;
            if (((Component) c.getImpl()).isVisible() && parent == cImpl.getParent()) {
                pref = c.getPreferredSize();
                min = c.getMinimumSize();
                preferredWidth = Math.max(preferredWidth, cImpl.getX() + pref.width + margin.left + margin.right);
                preferredHeight = Math.max(preferredHeight, cImpl.getY() + pref.height + margin.top + margin.bottom);
                minWidth = Math.max(minWidth, cImpl.getX() + min.width + margin.left + margin.right);
                minHeight = Math.max(minHeight, cImpl.getY() + min.height + margin.top + margin.bottom);
            }
        }
        sizeUnknown = false;
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);
        setSizes(parent);
        Insets insets = parent.getInsets();
        dim.width = preferredWidth + insets.left + insets.right;
        dim.height = preferredHeight + insets.top + insets.bottom;
        return dim;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);
        Insets insets = parent.getInsets();
        dim.width = minWidth + insets.left + insets.right;
        dim.height = minHeight + insets.top + insets.bottom;
        return dim;
    }
}
