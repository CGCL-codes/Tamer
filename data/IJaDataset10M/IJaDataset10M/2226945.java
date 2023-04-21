package backend.core.index;

import java.io.Serializable;

/**
 * Multikey provides an efficiant method for using
 * multiple keys for one hashmap.
 * 
 * @author weilej
 */
public class MultiKey implements Serializable {

    /**
	 * default serialization id
	 */
    private static final long serialVersionUID = 1L;

    public String[] keys;

    public MultiKey(String[] k) {
        keys = k;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(this.getClass())) {
            MultiKey other = (MultiKey) o;
            if (keys.length != other.keys.length) return false; else {
                boolean out = true;
                for (int i = 0; i < keys.length; i++) {
                    out = out && keys[i].equals(other.keys[i]);
                }
                return out;
            }
        } else return false;
    }

    @Override
    public int hashCode() {
        String sum = "";
        for (String s : keys) {
            sum = sum + s;
        }
        return sum.hashCode();
    }

    @Override
    public String toString() {
        String sum = "";
        for (String s : keys) {
            sum = sum + s + " ";
        }
        return "MultiKey: " + sum;
    }

    /**
	 * Returns the keys as string array.
	 * @return the keys
	 */
    public String[] getKeys() {
        return keys;
    }
}
