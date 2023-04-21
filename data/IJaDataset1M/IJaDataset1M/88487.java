package bmm.diagram.navigator;

import org.eclipse.jface.viewers.ViewerSorter;
import bmm.diagram.part.BmmVisualIDRegistry;

/**
 * @generated
 */
public class BmmNavigatorSorter extends ViewerSorter {

    /**
	 * @generated
	 */
    private static final int GROUP_CATEGORY = 4013;

    /**
	 * @generated
	 */
    public int category(Object element) {
        if (element instanceof BmmNavigatorItem) {
            BmmNavigatorItem item = (BmmNavigatorItem) element;
            return BmmVisualIDRegistry.getVisualID(item.getView());
        }
        return GROUP_CATEGORY;
    }
}
