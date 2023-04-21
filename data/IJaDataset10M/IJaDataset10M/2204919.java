package org.lds.wilmington.christiana.preparedness.gui.layout;

/**
 * 
 * @author Jay Askren
 *
 */
public class FormConstraints {

    public static final int RIGHT_JUSTIFICATION = 0;

    public static final int LEFT_JUSTIFICATION = 1;

    public static final int CENTER_JUSTIFICATION = 2;

    public static final int LABEL_RIGHT = 8;

    public static final int LABEL_LEFT = 9;

    public static final int TEXT_FIELD = 10;

    public static final int CHECK_BOX = 11;

    private int _numColumns;

    private int _justification;

    private int _type = TEXT_FIELD;

    public FormConstraints() {
        _type = TEXT_FIELD;
        _numColumns = 1;
        _justification = LEFT_JUSTIFICATION;
    }

    public FormConstraints(int componentType) {
        _type = componentType;
        _numColumns = 1;
        _justification = LEFT_JUSTIFICATION;
    }

    public FormConstraints(int type, int numColumns) {
        _type = type;
        _numColumns = numColumns;
        _justification = LEFT_JUSTIFICATION;
    }

    public FormConstraints(int type, int numColumns, int justification) {
        _numColumns = numColumns;
        _type = type;
        _justification = justification;
    }

    public int get_justification() {
        return _justification;
    }

    public void setjustification(int justification) {
        _justification = justification;
    }

    public int getNumColumns() {
        return _numColumns;
    }

    public void setNumColumns(int columns) {
        _numColumns = columns;
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        _type = type;
    }
}
