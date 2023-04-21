package jhomenet.commons.hw.states;

import java.io.Serializable;

/**
 * Default abstract state class.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class State implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * List of available states.
	 */
    public static final State ONSTATE = new State("ON", 1D);

    public static final State OFFSTATE = new State("OFF", 0D);

    public static final State DAYSTATE = new State("DAY", 1D);

    public static final State NIGHTSTATE = new State("NIGHT", 0D);

    public static final State NULLSTATE = new State("N/A", -1D);

    public static final State ERRORSTATE = new State("ERROR", -1D);

    /**
	 * The description of the state.
	 */
    protected String description;

    /**
	 * The value associated with the state.
	 */
    protected Double value;

    /**
	 * State ID. Used primarily for persistence storing.
	 */
    private Long id;

    /**
	 * Constructor.
	 * 
	 * @param description
	 * @param value
	 */
    public State(String description, Double value) {
        super();
        if (description == null) throw new IllegalArgumentException("State description cannot be null!");
        if (value == null) throw new IllegalArgumentException("State value cannot be null!");
        this.description = description;
        this.value = value;
    }

    /**
	 * Non-instantiable constructor (for persistence).
	 */
    private State() {
        super();
    }

    /**
	 * Set the state ID.
	 *
	 * @param id
	 */
    private void setId(Long id) {
        this.id = id;
    }

    /**
	 * Get the state ID.
	 *
	 * @return State ID
	 */
    private Long getId() {
        return id;
    }

    /**
	 * Set the state type.
	 * 
	 * @param type State type
	 */
    private void setDescription(String type) {
        this.description = type;
    }

    /**
	 * Get the state type.
	 *
	 * @return State type
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @return the value
	 */
    public Double getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    private void setValue(Double value) {
        this.value = value;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final State other = (State) obj;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return description;
    }
}
