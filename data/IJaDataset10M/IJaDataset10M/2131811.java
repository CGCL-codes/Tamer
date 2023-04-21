package commandz.var;

/**
 *  A Integer type specialized implementation of the Variable interface.
 *
 *  This class is applicable when a Variable for integral numbers is needed.
 */
public class VariableInteger implements Variable {

    private final String VARIABLE_NAME = "integer";

    protected int value;

    /**
     *  Initializes a new instance of the VariableInteger class
     *  with the default value 0.
     */
    public VariableInteger() {
        this.value = 0;
    }

    /**
     *  Initializes a new instance of the VariableInteger class
     *  with the given value.
     *
     *  @param value A integer value to use for initialization.
     */
    public VariableInteger(final int value) {
        this.value = value;
    }

    /**
     *  Assigns the value of this VariableInteger to the given one.
     *
     *  @param value A integer value to use.
     */
    public void setValue(final int value) {
        this.value = value;
    }

    /**
     *  Returns the value used by this VariableInteger.
     *
     *  @return A integer value.
     */
    public int getValue() {
        return this.value;
    }

    /**
     *  Returns the name of the type of this Variable.
     *
     *  @return A String containing the type-name of this Variable.
     */
    public String getTypeName() {
        return this.VARIABLE_NAME;
    }

    /**
     *  Parses the given string into the value of this Variable.
     *
     *  @param str A String holding the representation of an integral value.
     *
     *  @return true on success, false on error.
     */
    public boolean parse(final String str) {
        try {
            this.value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     *  Formats the value of this Variable into a readable String.
     *
     *  @return A textual representation of a integral value.
     */
    public String format() {
        return String.format("%d", this.value);
    }
}
