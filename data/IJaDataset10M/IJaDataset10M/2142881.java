package edu.villanova.studs.poker.transport;

/**
 * @author Stephen Campbell
 * This simple class is mainly used for transporting results
 * to the GUI. It holds a name and value - mostly likely 
 * representing a label such as "One Pair" and a probability 
 * value such as 0.50.
 */
public class NameValuePair {

    private String name;

    private String value;

    /**
	 * Constructor that takes in the name and value
	 * @param name
	 * @param value
	 */
    public NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
	 * Default constructor; sets the name and value to null
	 */
    public NameValuePair() {
        this.name = null;
        this.value = null;
    }

    /**
	 * Getter method to retrieve the name
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * Setter method for the name
	 * @param name
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Getter method to retrieve the value
	 * @return
	 */
    public String getValue() {
        return value;
    }

    /**
	 * Setter method for the value
	 * @param value
	 */
    public void setValue(String value) {
        this.value = value;
    }
}
