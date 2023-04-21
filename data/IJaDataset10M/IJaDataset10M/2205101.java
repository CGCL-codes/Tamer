package es.eucm.eadventure.editor.gui.structurepanel.structureelements;

import javax.swing.ImageIcon;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.control.controllers.assessment.AssessmentProfilesDataControl;
import es.eucm.eadventure.editor.gui.structurepanel.StructureElement;
import es.eucm.eadventure.editor.gui.structurepanel.StructureElementFactory;
import es.eucm.eadventure.editor.gui.structurepanel.StructureListElement;

public class AssessmentControllerStructureElement extends StructureListElement {

    public AssessmentControllerStructureElement(DataControl dataControl) {
        super(TC.get("AssessmentProfiles.Title"), dataControl);
        icon = new ImageIcon("img/icons/assessmentProfiles.png");
    }

    @Override
    public StructureElement getChild(int i) {
        StructureElement temp = StructureElementFactory.getStructureElement(((AssessmentProfilesDataControl) dataControl).getProfiles().get(i), this);
        temp.setName(((AssessmentProfilesDataControl) dataControl).getProfiles().get(i).getFileName());
        return temp;
    }

    @Override
    public int getChildCount() {
        return ((AssessmentProfilesDataControl) dataControl).getProfiles().size();
    }
}
