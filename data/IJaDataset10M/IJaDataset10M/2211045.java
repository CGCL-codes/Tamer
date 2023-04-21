package hub.sam.mof.simulator.editor.diagram.navigator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class M3ActionsNavigatorItem extends M3ActionsAbstractNavigatorItem {

    /**
	 * @generated
	 */
    static {
        final Class[] supportedTypes = new Class[] { View.class, EObject.class };
        Platform.getAdapterManager().registerAdapters(new IAdapterFactory() {

            public Object getAdapter(Object adaptableObject, Class adapterType) {
                if (adaptableObject instanceof hub.sam.mof.simulator.editor.diagram.navigator.M3ActionsNavigatorItem && (adapterType == View.class || adapterType == EObject.class)) {
                    return ((hub.sam.mof.simulator.editor.diagram.navigator.M3ActionsNavigatorItem) adaptableObject).getView();
                }
                return null;
            }

            public Class[] getAdapterList() {
                return supportedTypes;
            }
        }, hub.sam.mof.simulator.editor.diagram.navigator.M3ActionsNavigatorItem.class);
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
    public M3ActionsNavigatorItem(View view, Object parent, boolean isLeaf) {
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
        if (obj instanceof hub.sam.mof.simulator.editor.diagram.navigator.M3ActionsNavigatorItem) {
            return EcoreUtil.getURI(getView()).equals(EcoreUtil.getURI(((hub.sam.mof.simulator.editor.diagram.navigator.M3ActionsNavigatorItem) obj).getView()));
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
