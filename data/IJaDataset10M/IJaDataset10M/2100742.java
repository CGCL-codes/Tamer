package org.eclipsetrader.core.views;

/**
 * This interface is implemented by objects that visits view trees.
 *
 * @since 1.0
 */
public interface IViewVisitor extends IViewItemVisitor {

    /**
	 * Visits the given view.
	 *
	 * @param view the view to visit.
	 * @return <code>true</code> if the view's items should be visited, <code>false</code> otherwise.
	 */
    public boolean visit(IView view);
}
