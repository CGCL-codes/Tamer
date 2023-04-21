package SensorDataWebGui.diagram.navigator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;

/**
 * @generated
 */
public abstract class SensorDataWebGuiAbstractNavigatorItem extends PlatformObject {

    /**
	 * @generated
	 */
    static {
        final Class[] supportedTypes = new Class[] { ITabbedPropertySheetPageContributor.class };
        final ITabbedPropertySheetPageContributor propertySheetPageContributor = new ITabbedPropertySheetPageContributor() {

            public String getContributorId() {
                return "SensorDataWebGuiMM.diagram";
            }
        };
        Platform.getAdapterManager().registerAdapters(new IAdapterFactory() {

            public Object getAdapter(Object adaptableObject, Class adapterType) {
                if (adaptableObject instanceof SensorDataWebGui.diagram.navigator.SensorDataWebGuiAbstractNavigatorItem && adapterType == ITabbedPropertySheetPageContributor.class) {
                    return propertySheetPageContributor;
                }
                return null;
            }

            public Class[] getAdapterList() {
                return supportedTypes;
            }
        }, SensorDataWebGui.diagram.navigator.SensorDataWebGuiAbstractNavigatorItem.class);
    }

    /**
	 * @generated
	 */
    private Object myParent;

    /**
	 * @generated
	 */
    protected SensorDataWebGuiAbstractNavigatorItem(Object parent) {
        myParent = parent;
    }

    /**
	 * @generated
	 */
    public Object getParent() {
        return myParent;
    }
}
