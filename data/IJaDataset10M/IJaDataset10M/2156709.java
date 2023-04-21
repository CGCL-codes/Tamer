package gui.dialogs.properties;

import gui.Field;
import gui.dialogs.FieldDialog;

public class PortalsDialog extends FieldDialog {

    public PortalsDialog() {
        super(new Field[] { new Field("Portals") });
    }

    @Override
    public String getMessage() {
        return "Enter the number of portals allowed in this level";
    }

    @Override
    public String getTitle() {
        return "Portals";
    }

    @Override
    public void doOKAction() {
        level.setPortals(fields[0].getValue());
    }

    @Override
    public void updateValues() {
        fields[0].setValue(level.getPortals());
    }
}
