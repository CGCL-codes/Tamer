package jtk.project4.fleet.field;

import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.control.GroupControl;

public class MainAddPMServiceField extends GroupControl {

    public MainAddPMServiceField(String bindID) {
        setBindID(bindID);
        createLayout();
    }

    private void createLayout() {
        setLayout(new FormLayout()).addField(new ServiceNameField("ServiceName")).addField(new TombolAddPMServicesField("TombolAddPMServices")).addRow().addField(new TypeField("Type")).addRow().addField(new DateTrackingField("DateTracking")).addRow().addField(new MileageTrackingField("MileageTracking"));
    }
}
