package icapture.hibernate;

public final class ControlLayoutWell extends Persistent {

    private String column;

    private Control control;

    private String controlLayoutWellID;

    private String layoutName;

    private String row;

    public String getControlLayoutWellID() {
        return controlLayoutWellID;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }

    public Control getControl() {
        return control;
    }

    public String getVisibleName() {
        return controlLayoutWellID;
    }

    public String getPropertyValue(String propertyName) {
        if (propertyName.equals("visibleName")) {
            return controlLayoutWellID;
        } else {
            return super.getPropertyValue(propertyName);
        }
    }

    public Object getPropertyObject(String propertyName) {
        if (propertyName.equals("visibleName")) {
            return controlLayoutWellID;
        } else {
            return super.getPropertyObject(propertyName);
        }
    }

    public String[] getValueArray(byte[] indexArray, int viewSize) {
        String[] valueArray = super.getValueArray(indexArray, viewSize);
        if (indexArray[6] > 0) {
            valueArray[indexArray[6] - 1] = controlLayoutWellID;
        }
        if (indexArray[7] > 0) {
            valueArray[indexArray[7] - 1] = layoutName;
        }
        if (indexArray[8] > 0) {
            valueArray[indexArray[8] - 1] = row;
        }
        if (indexArray[9] > 0) {
            valueArray[indexArray[9] - 1] = column;
        }
        if (indexArray[10] > 0) {
            valueArray[indexArray[10] - 1] = control.getControlID();
        }
        return valueArray;
    }

    public String[] getValueArrayReadable(byte[] indexArray, int viewSize) {
        String[] valueArray = super.getValueArray(indexArray, viewSize);
        if (indexArray[6] > 0) {
            valueArray[indexArray[6] - 1] = controlLayoutWellID;
        }
        if (indexArray[7] > 0) {
            valueArray[indexArray[7] - 1] = layoutName;
        }
        if (indexArray[8] > 0) {
            valueArray[indexArray[8] - 1] = row;
        }
        if (indexArray[9] > 0) {
            valueArray[indexArray[9] - 1] = column;
        }
        if (indexArray[10] > 0) {
            valueArray[indexArray[10] - 1] = control.getDescription();
        }
        return valueArray;
    }

    public void setControlLayoutWellID(String input) {
        controlLayoutWellID = input;
    }

    public void setLayoutName(String input) {
        layoutName = input;
    }

    public void setRow(String input) {
        row = input;
    }

    public void setColumn(String input) {
        column = input;
    }

    public void setControl(Control input) {
        control = input;
    }
}
