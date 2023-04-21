package com.hofstetter.diplthesis.ctb.ctb.diagram.navigator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class CtbNavigatorItem extends CtbAbstractNavigatorItem {

    /**
	 * @generated
	 */
    static {
        final Class[] supportedTypes = new Class[] { View.class, EObject.class };
        Platform.getAdapterManager().registerAdapters(new IAdapterFactory() {

            public Object getAdapter(Object adaptableObject, Class adapterType) {
                if (adaptableObject instanceof com.hofstetter.diplthesis.ctb.ctb.diagram.navigator.CtbNavigatorItem && (adapterType == View.class || adapterType == EObject.class)) {
                    return ((com.hofstetter.diplthesis.ctb.ctb.diagram.navigator.CtbNavigatorItem) adaptableObject).getView();
                }
                return null;
            }

            public Class[] getAdapterList() {
                return supportedTypes;
            }
        }, com.hofstetter.diplthesis.ctb.ctb.diagram.navigator.CtbNavigatorItem.class);
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
    public CtbNavigatorItem(View view, Object parent, boolean isLeaf) {
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
        if (obj instanceof com.hofstetter.diplthesis.ctb.ctb.diagram.navigator.CtbNavigatorItem) {
            return EcoreUtil.getURI(getView()).equals(EcoreUtil.getURI(((com.hofstetter.diplthesis.ctb.ctb.diagram.navigator.CtbNavigatorItem) obj).getView()));
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
