package iconSwingStates.Event;

import iconSwingStates.Transitions.TGeneric;
import java.util.ArrayList;
import fr.lri.swingstates.events.VirtualEvent;

public class EVirtualEvent extends VirtualEvent {

    /**
	 * Values of each slots attached to the event.
	 */
    protected ArrayList<Object> values;

    protected TGeneric transition;

    public EVirtualEvent(String name) {
        super(name);
    }

    public EVirtualEvent(String name, ArrayList<Object> values) {
        super(name);
        this.values = values;
    }

    /**
     * This method returns all the integer values into an ArrayList<Integer>
     * @return an ArrayList<Integer> with all the integer values
     */
    public ArrayList<Integer> getIntValues() {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) instanceof Integer) {
                ints.add((Integer) values.get(i));
            }
        }
        return ints;
    }

    /**
     * This method returns all the double values into an ArrayList<Double>
     * @return an ArrayList<Double> with all the double values
     */
    public ArrayList<Double> getDoubleValues() {
        ArrayList<Double> doubles = new ArrayList<Double>();
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) instanceof Double) {
                doubles.add((Double) values.get(i));
            }
        }
        return doubles;
    }

    /**
     * This method returns all the boolean values into an ArrayList<Boolean>
     * @return an ArrayList<Boolean> with all the boolean values
     */
    public ArrayList<Boolean> getBooleanValues() {
        ArrayList<Boolean> booleans = new ArrayList<Boolean>();
        for (int i = 0; i < values.size(); i++) {
            System.out.println(values.get(i));
            if (values.get(i) instanceof Boolean) {
                booleans.add((Boolean) values.get(i));
            }
        }
        return booleans;
    }

    /**
     * This method returns all the string values into an ArrayList<String>
     * @return an ArrayList<String> with all the string values
     */
    public ArrayList<String> getStringValues() {
        ArrayList<String> strings = new ArrayList<String>();
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) instanceof String) {
                strings.add((String) values.get(i));
            }
        }
        return strings;
    }

    /**
     * This method returns the integer values at the index i
     * @param i The index
     * @return the integer values at i
     */
    public int getValueAsInteger(int i) {
        return (Integer) values.get(i);
    }

    /**
     * This method returns the double values at the index i
     * @param i The index
     * @return the double values at i
     */
    public double getValueAsDouble(int i) {
        return (Double) values.get(i);
    }

    /**
     * This method returns the string values at the index i
     * @param i The index
     * @return the string values at i
     */
    public String getValueAsString(int i) {
        return (String) values.get(i);
    }

    /**
     * This method returns the boolean values at the index i
     * @param i The index
     * @return the boolean values at i
     */
    public boolean getValueAsBoolean(int i) {
        return (Boolean) values.get(i);
    }

    /**
     * This method returns all the values into an ArrayList<Object>
     * @return all the values
     */
    public ArrayList<Object> getValues() {
        return values;
    }

    public TGeneric getTransition() {
        return transition;
    }
}
