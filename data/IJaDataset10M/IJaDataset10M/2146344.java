package edu.lmu.cs.cmsi402.celldoku.utils;

public class UndoNode {

    private int _x;

    private int _y;

    private String _value;

    public UndoNode(int x, int y, String value) {
        _x = x;
        _y = y;
        _value = value;
    }

    /**
	 * @return Returns the _value.
	 */
    public String getValue() {
        return _value;
    }

    /**
	 * @return Returns the _x.
	 */
    public int getX() {
        return _x;
    }

    /**
	 * @return Returns the _y.
	 */
    public int getY() {
        return _y;
    }
}
