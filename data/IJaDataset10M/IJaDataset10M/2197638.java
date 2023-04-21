package commandz.var;

/**
 *  A Boolean type specialized implementation of the Variable interface.
 *
 *  This class is applicable when a Variable for Boolean truths is needed.
 */
public class VariableBoolean implements Variable {

    private final String VARIABLE_NAME = "logic";

    protected boolean value;

    /**
     *  Initializes a new instance of this VariableBoolean class with
     *  the value 'false';
     */
    public VariableBoolean() {
        this.value = false;
    }

    /**
     *  Initializes a new instance of this VariableBoolean class with
     *  the specified value.
     *
     *  @param value A boolean value to use for initialization.
     */
    public VariableBoolean(final boolean value) {
        this.value = value;
    }

    /**
     *  Assigns the value of this VariableBoolean to the given argument.
     *
     *  @param value A boolean value to use.
     */
    public void setValue(final boolean value) {
        this.value = value;
    }

    /**
     *  Returns the value of this VariableBoolean.
     *
     *  @return A boolean value.
     */
    public boolean getValue() {
        return this.value;
    }

    /**
     *  Returns the name of the type of this Variable.
     *
     *  @return The type-name of this Variable.
     */
    public String getTypeName() {
        return this.VARIABLE_NAME;
    }

    /**
     *  Parses the given string into the value of this VariableBoolean.
     *
     *  @param str A String representing a possible boolean truth.
     *
     *  @return false on error, true otherwise.
     */
    public boolean parse(final String str) {
        if (str.equals("true")) {
            this.value = true;
            return true;
        }
        if (str.equals("false")) {
            this.value = false;
            return true;
        }
        return false;
    }

    /**
     *  Formats the value of this Variable into a readable String.
     *
     *  @return A String containing either "true" or "false".
     */
    public String format() {
        return String.format("%b", this.value);
    }
}
