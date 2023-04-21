package mapMaker;

import java.io.Serializable;

public class Entity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7956043216312159295L;

    String name;

    boolean door;

    public boolean equals(Object other) {
        if (other instanceof Entity) return ((Entity) other).name.equals(name) && ((Entity) other).door == door;
        return false;
    }

    public String toString() {
        return (door ? "Door: " : "") + name;
    }
}
