package com.ivis.xprocess.ui.datawrappers.workflow;

import java.util.HashSet;
import java.util.Set;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.ui.UIType;
import com.ivis.xprocess.ui.actions.workflow.NewExternalEventTypeAction;
import com.ivis.xprocess.ui.datawrappers.ElementWrapper;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.datawrappers.IProvideEditor;

/**
 * ElementWrapper wrapping the Core ExternalEventType.
 *
 * Parent Wrapper is ExternalSystemWrapper.
 *
 */
public class ExternalEventTypeWrapper extends ElementWrapper implements IProvideEditor {

    public ExternalEventTypeWrapper(IElementWrapper parent, Xelement xelement) {
        super(parent, xelement);
    }

    @Override
    public boolean testAttribute(Object target, String name, String value) {
        if (name.equals("canDelete")) {
            return true;
        }
        return false;
    }

    public UIType getUIType() {
        return UIType.external_event_type;
    }

    public void openEditor(Object selectedObject) {
        NewExternalEventTypeAction newExternalEventTypeAction = new NewExternalEventTypeAction();
        Set<Object> objects = new HashSet<Object>();
        objects.add(selectedObject);
        newExternalEventTypeAction.doAction(objects);
    }
}
