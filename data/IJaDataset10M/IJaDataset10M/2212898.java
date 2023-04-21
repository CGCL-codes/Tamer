package edu.byu.ece.rapidSmith.device.helper;

import java.io.Serializable;
import java.util.Arrays;
import edu.byu.ece.rapidSmith.device.WireConnection;

/**
 * A helper class to help remove duplicate objects and reduce memory usage and file
 * size of the Device class. 
 * @author Chris Lavin
 */
public class WireArray implements Serializable {

    private static final long serialVersionUID = 222495247665714923L;

    /** An array of wires */
    public WireConnection[] array;

    /**
	 * Constructor
	 * @param array The Array of wires that correspond to this object.
	 */
    public WireArray(WireConnection[] array) {
        this.array = array;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(array);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        WireArray other = (WireArray) obj;
        if (!Arrays.deepEquals(array, other.array)) return false;
        return true;
    }
}
