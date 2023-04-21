package com.ivis.xprocess.ui.actions.workflow;

import java.util.Collection;
import com.ivis.xprocess.ui.actions.XProcessAction;
import com.ivis.xprocess.ui.datawrappers.workflow.WorkflowPackageWrapper;
import com.ivis.xprocess.ui.factories.creation.WorkflowCreationFactory;
import com.ivis.xprocess.ui.properties.ElementMessages;

public class NewExternalSystemAction extends XProcessAction {

    @Override
    public void doAction(Collection<Object> objects) {
        if (objects.size() == 1) {
            Object object = objects.toArray()[0];
            if (object instanceof WorkflowPackageWrapper) {
                WorkflowPackageWrapper workflowPackageWrapper = (WorkflowPackageWrapper) object;
                WorkflowCreationFactory.getInstance().createExternalSystem(workflowPackageWrapper, ElementMessages.external_system);
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
