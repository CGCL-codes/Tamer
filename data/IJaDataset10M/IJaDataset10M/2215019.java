package skycastle.flexibleui.viewhost.layout.operations.viewaddition;

import skycastle.flexibleui.viewhost.ViewContainer;
import skycastle.flexibleui.viewhost.layout.operations.LayoutOperation;
import skycastle.flexibleui.viewhost.views.View;
import skycastle.model.Model;

/**
 * An operation that adds a view to a target view as a new tab.
 *
 * @author Hans H�ggstr�m
 */
public final class AddTabOperation implements LayoutOperation {

    private final String myViewToAdd;

    private final String myTargetView;

    /**
     * Creates a new AddTabOperation.
     *
     * @param targetView the identifier of the view to add the viewToAdd to, as a new tab.
     * @param viewToAdd  the identifier of the view to be added.
     */
    public AddTabOperation(final String targetView, final String viewToAdd) {
        myViewToAdd = viewToAdd;
        myTargetView = targetView;
    }

    public void applyOperation(final ViewContainer viewContainer, final Model model) {
        final View target = viewContainer.getView(myTargetView);
        final View viewToAdd = viewContainer.getView(myViewToAdd);
        target.addTab(viewToAdd);
    }
}
