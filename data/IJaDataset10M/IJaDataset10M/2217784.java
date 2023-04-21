package org.bpmn.diagram.ui.views.properties.sections.task;

import org.bpmn.diagram.ui.views.properties.sections.AbstractListPropertySection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import bpmn.BpmnPackage;
import bpmn.ManualTask;
import bpmn.UserTask;

/**
 * @author jassuncao
 *
 */
public class TaskPerformersPropertySection extends AbstractListPropertySection {

    @Override
    protected String getLabelText() {
        return "Performers";
    }

    @Override
    protected EList getListFeatureValues() {
        EList list = null;
        if (eObject instanceof UserTask) list = ((UserTask) eObject).getPerformers(); else if (eObject instanceof ManualTask) list = ((ManualTask) eObject).getPerformers(); else throw new IllegalArgumentException("Expected UserTask or ManualTask. Received " + eObject.getClass());
        return list;
    }

    @Override
    protected String[] getListFeatureValuesAsString() {
        EList list = getListFeatureValues();
        return (String[]) list.toArray(new String[list.size()]);
    }

    @Override
    protected String getSingleItemText() {
        return "Performer";
    }

    @Override
    protected EAttribute getFeature() {
        if (eObject instanceof UserTask) return BpmnPackage.eINSTANCE.getUserTask_Performers(); else if (eObject instanceof ManualTask) return BpmnPackage.eINSTANCE.getManualTask_Performers();
        throw new IllegalArgumentException("Expected UserTask or ManualTask. Received " + eObject.getClass());
    }
}
