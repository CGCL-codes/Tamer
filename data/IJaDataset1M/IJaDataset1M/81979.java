package org.bpmn.diagram.ui.views.properties.sections.task;

import java.util.List;
import org.bpmn.diagram.ui.views.properties.sections.AbstractEnumerationPropertySection;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import bpmn.BpmnPackage;
import bpmn.MessageEvent;
import bpmn.ReceiveTask;
import bpmn.SendTask;
import bpmn.ServiceTask;
import bpmn.UserTask;
import bpmn.util.BpmnSwitch;

/**
 * @author jassuncao
 *
 */
public class TaskImplementationPropertySection extends AbstractEnumerationPropertySection {

    @Override
    protected String[] getEnumerationFeatureValues() {
        List values = bpmn.ImplementationType.VALUES;
        String[] ret = new String[values.size()];
        for (int i = 0; i < values.size(); i++) {
            ret[i] = ((bpmn.ImplementationType) values.get(i)).getName();
        }
        return ret;
    }

    @Override
    protected EAttribute getFeature() {
        BpmnSwitch bpmnSwitch = new BpmnSwitch() {

            @Override
            public Object caseSendTask(SendTask object) {
                return BpmnPackage.eINSTANCE.getSendTask_Implementation();
            }

            @Override
            public Object caseServiceTask(ServiceTask object) {
                return BpmnPackage.eINSTANCE.getServiceTask_Implementation();
            }

            @Override
            public Object caseReceiveTask(ReceiveTask object) {
                return BpmnPackage.eINSTANCE.getReceiveTask_Implementation();
            }

            @Override
            public Object caseUserTask(UserTask object) {
                return BpmnPackage.eINSTANCE.getUserTask_Implementation();
            }
        };
        return (EAttribute) bpmnSwitch.doSwitch(eObject);
    }

    @Override
    protected String getFeatureAsText() {
        BpmnSwitch bpmnSwitch = new BpmnSwitch() {

            @Override
            public Object caseSendTask(SendTask object) {
                return object.getImplementation().getName();
            }

            @Override
            public Object caseServiceTask(ServiceTask object) {
                return object.getImplementation().getName();
            }

            @Override
            public Object caseReceiveTask(ReceiveTask object) {
                return object.getImplementation().getName();
            }

            @Override
            public Object caseUserTask(UserTask object) {
                return object.getImplementation().getName();
            }
        };
        return (String) bpmnSwitch.doSwitch(eObject);
    }

    @Override
    protected Object getFeatureValue(int index) {
        return bpmn.ImplementationType.VALUES.get(index);
    }

    @Override
    protected String getLabelText() {
        return "Implementation:";
    }

    @Override
    protected boolean isEqual(int index) {
        BpmnSwitch bpmnSwitch = new BpmnSwitch() {

            @Override
            public Object caseSendTask(SendTask object) {
                return object.getImplementation();
            }

            @Override
            public Object caseServiceTask(ServiceTask object) {
                return object.getImplementation();
            }

            @Override
            public Object caseReceiveTask(ReceiveTask object) {
                return object.getImplementation();
            }

            @Override
            public Object caseUserTask(UserTask object) {
                return object.getImplementation();
            }
        };
        return bpmn.ImplementationType.VALUES.get(index).equals(bpmnSwitch.doSwitch(eObject));
    }
}
