package com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator;

import org.eclipse.jface.viewers.ViewerSorter;

/**
 * @generated
 */
public class ClassdiagramNavigatorSorter extends ViewerSorter {

    /**
	 * @generated
	 */
    private static final int GROUP_CATEGORY = 7009;

    /**
	 * @generated
	 */
    public int category(Object element) {
        if (element instanceof com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramNavigatorItem) {
            com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramNavigatorItem item = (com.ssd.mdaworks.classdiagram.classDiagram.diagram.navigator.ClassdiagramNavigatorItem) element;
            return com.ssd.mdaworks.classdiagram.classDiagram.diagram.part.ClassdiagramVisualIDRegistry.getVisualID(item.getView());
        }
        return GROUP_CATEGORY;
    }
}
