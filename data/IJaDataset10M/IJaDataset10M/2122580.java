package ibbt.sumo.vann.matlab2java.basicObjects;

import ibbt.sumo.vann.matlab2java.Matlab;

/**
 * Read-only variant for simple data-types in Matlab
 * @author Nathan Henckes
 * @param <E> the java version of the simple type of the variable in Matlab
 */
public class MatlabObjectReadOnly<E> extends MatlabObject {

    protected E fValue;

    /**
	 * Constructor for the MatlabObject given a variable name and an initValue.
	 * @param variableName: the variable name of the object in Matlab
	 * @param initValue: an initValue for the internal value before a load.
	 */
    public MatlabObjectReadOnly(String variableName, E initValue) {
        this.fValue = initValue;
        this.setVariableName(variableName);
    }

    /**
	 * Getter for the internal value of the MatlabObject (will only be usefull after a load()).
	 * @return the value;
	 */
    public E get() {
        return this.fValue;
    }

    /**
	 * Loads the value of the MatlabObject from the object in Matlab.
	 */
    @SuppressWarnings("unchecked")
    public void load() {
        try {
            this.fValue = (E) Matlab.getVariable(this.getWorkspace(), this.getVariableName(), this.fValue.getClass());
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Empty save since no changes can be made to the value
	 */
    public void save() {
    }
}
