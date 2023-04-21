package net.sf.vgap4.assistant.models;

import java.io.Serializable;
import java.util.logging.Logger;
import org.eclipse.draw2d.geometry.Point;

public class ShipInformation extends AssistantTurnInfo implements Serializable {

    private static final long serialVersionUID = 3906934058195215773L;

    private static Logger logger = Logger.getLogger("net.sf.vgap4.projecteditor.model");

    public void loadData(final String[] fieldNames, final String[] fields) {
        for (int i = 1; i < fieldNames.length; i++) {
            final String fieldName = fieldNames[i].trim();
            final String value = fields[i].trim();
            info.setProperty(fieldName, value);
        }
        idNumber = this.convert2Integer(this.getField("ID Number"));
        name = this.getField("Name");
        final int x = this.convert2Integer(this.getField("x"));
        final int y = this.convert2Integer(this.getField("y"));
        location = new Point(x, y);
    }
}
