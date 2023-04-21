package org.argouml.uml.ui.behavior.common_behavior;

/**
 * The properties panel for a DestroyAction.
 * <p>
 * TODO: this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelDestroyAction extends PropPanelAction {

    /**
     * The constructor.
     *
     */
    public PropPanelDestroyAction() {
        super("label.destroy-action", lookupIcon("DestroyAction"));
    }
}
