package hub.metrik.lang.eprovide.usertrace.step.diagram.navigator;

import hub.metrik.lang.eprovide.usertrace.step.diagram.part.MMUnitVisualIDRegistry;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * @generated
 */
public class MMUnitNavigatorSorter extends ViewerSorter {

    /**
	 * @generated
	 */
    private static final int GROUP_CATEGORY = 7003;

    /**
	 * @generated
	 */
    public int category(Object element) {
        if (element instanceof MMUnitNavigatorItem) {
            MMUnitNavigatorItem item = (MMUnitNavigatorItem) element;
            return MMUnitVisualIDRegistry.getVisualID(item.getView());
        }
        return GROUP_CATEGORY;
    }
}
