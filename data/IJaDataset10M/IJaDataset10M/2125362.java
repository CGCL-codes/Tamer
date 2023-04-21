package ibbt.sumo.vann.matlab2java.basicObjects;

/**
 * Java-representation of a cell-array in Matlab.
 * @author Nathan Henckes
 * @param <E> the MatlabObject that represents the type of the Matlab cell-array values
 * (should be MatlabObject if more types are present).
 */
public class MatlabCellArray<E extends MatlabObject> extends MatlabCompound<E> {

    /**
	 * Constructor
	 * @param variableName: the variable name
	 */
    public MatlabCellArray(String variableName) {
        super(variableName);
    }

    /**
	 * Character used for indexing
	 */
    @Override
    public char getIndexingClosingChar() {
        return '}';
    }

    /**
	 * Character used for indexing
	 */
    @Override
    public char getIndexingOpeningChar() {
        return '{';
    }
}
