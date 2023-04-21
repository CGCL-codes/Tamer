package models.requests;

/**
 * This class represents an argument to a request. An argument is simply a name-value pair passed in with
 * a request. A collection of arguments is maintained within the {@link Request} class.
 * 
 * @author Thomas Pedley
 */
public class Argument {

    /** The name of the argument. */
    private String name;

    /** The value of the argument. */
    private String value;

    /**
	 * Constructor.
	 * 
	 * @param name The name of the argument.
	 * @param value The value of the argument.
	 */
    public Argument(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
	 * Get the name of the argument.
	 * 
	 * @return The name of the argument.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Get the value of the argument.
	 * 
	 * @return The value of the argument.
	 */
    public String getValue() {
        return value;
    }
}
