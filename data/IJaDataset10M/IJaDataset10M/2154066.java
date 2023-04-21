package ru.adv.util.matrix;

import ru.adv.util.ErrorCodeException;

/**
 * Throws if given column index is out of bounds
 */
public class MatrixColumnIndexException extends ErrorCodeException {

    private int _index;

    private int _width;

    /**
	 * Constructs object
	 */
    public MatrixColumnIndexException(String msg, int index, int width) {
        super(msg);
        _index = index;
        _width = width;
        setAttr("index", Integer.toString(getHumanIndex()));
        setAttr("width", Integer.toString(getWidth()));
    }

    /**
	 * Returns mismatched index (zero based).
	 */
    public int getIndex() {
        return _index;
    }

    public int getWidth() {
        return _width;
    }

    /**
	 * Returns mismatched index in human convinient representaion
	 * (based on 1).
	 */
    public int getHumanIndex() {
        return _index + 1;
    }
}
