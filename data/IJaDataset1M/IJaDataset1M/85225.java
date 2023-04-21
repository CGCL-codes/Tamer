package org.spbu.pldoctoolkit.graph.diagram.infproduct.navigator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class DrlModelNavigatorItem extends DrlModelAbstractNavigatorItem {

    /**
	 * @generated
	 */
    static {
        final Class[] supportedTypes = new Class[] { View.class, EObject.class };
        Platform.getAdapterManager().registerAdapters(new IAdapterFactory() {

            public Object getAdapter(Object adaptableObject, Class adapterType) {
                if (adaptableObject instanceof org.spbu.pldoctoolkit.graph.diagram.infproduct.navigator.DrlModelNavigatorItem && (adapterType == View.class || adapterType == EObject.class)) {
                    return ((org.spbu.pldoctoolkit.graph.diagram.infproduct.navigator.DrlModelNavigatorItem) adaptableObject).getView();
                }
                return null;
            }

            public Class[] getAdapterList() {
                return supportedTypes;
            }
        }, org.spbu.pldoctoolkit.graph.diagram.infproduct.navigator.DrlModelNavigatorItem.class);
    }

    /**
	 * @generated
	 */
    private View myView;

    /**
	 * @generated
	 */
    private boolean myLeaf = false;

    /**
	 * @generated
	 */
    public DrlModelNavigatorItem(View view, Object parent, boolean isLeaf) {
        super(parent);
        myView = view;
        myLeaf = isLeaf;
    }

    /**
	 * @generated
	 */
    public View getView() {
        return myView;
    }

    /**
	 * @generated
	 */
    public boolean isLeaf() {
        return myLeaf;
    }

    /**
	 * @generated
	 */
    public boolean equals(Object obj) {
        if (obj instanceof org.spbu.pldoctoolkit.graph.diagram.infproduct.navigator.DrlModelNavigatorItem) {
            return EcoreUtil.getURI(getView()).equals(EcoreUtil.getURI(((org.spbu.pldoctoolkit.graph.diagram.infproduct.navigator.DrlModelNavigatorItem) obj).getView()));
        }
        return super.equals(obj);
    }

    /**
	 * @generated
	 */
    public int hashCode() {
        return EcoreUtil.getURI(getView()).hashCode();
    }
}
