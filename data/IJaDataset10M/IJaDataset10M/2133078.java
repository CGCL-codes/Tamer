package pdmeditor.PDM.Operation;

import javafx.scene.paint.Color;

/**
 *
 * @author Peter
 */
public class OperationType {

    private String name;

    public String getName() {
        return name;
    }

    private Integer id;

    public Integer getID() {
        return id;
    }

    private String label;

    public String getLabel() {
        return label;
    }

    private Color typeColor;

    public Color getTypeColor() {
        return typeColor;
    }

    private Integer dropDownPosition;

    public Integer getDropDownPosition() {
        return dropDownPosition;
    }

    public OperationType(String inName, Integer inID, String inLabel, Color inColor, Integer inPosition) {
        name = inName;
        id = inID;
        label = inLabel;
        typeColor = inColor;
        dropDownPosition = inPosition;
    }

    public String toString() {
        return name;
    }
}
