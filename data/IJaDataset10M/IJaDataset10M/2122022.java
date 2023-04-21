package org.bpmn.diagram.ui.views.properties.sections.diagram;

import org.bpmn.diagram.ui.views.properties.sections.AbstractTextPropertySection;
import org.eclipse.emf.ecore.EAttribute;
import bpmn.BpmnPackage;

public class CreationDatePropertySection extends AbstractTextPropertySection {

    @Override
    protected EAttribute getFeature() {
        return BpmnPackage.eINSTANCE.getBPMNDiagram_CreationDate();
    }

    @Override
    protected String getLabelText() {
        return "Creation Date:";
    }
}
