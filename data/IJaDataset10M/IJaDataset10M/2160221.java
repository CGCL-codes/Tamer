package org.aitools.programd.graph;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * This is an optimization of {@link NodemapperFactory} that avoids creating
 * the internal {@link java.util.LinkedHashMap LinkedMap} until the
 * number of mappings exceeds two (2).
 * 
 * @author <a href="mailto:noel@aitools.org">Noel Bush</a>
 * @version 4.6
 */
public class TwoOptimalNodemaster extends AbstractNodemaster {

    protected int size = 0;

    protected String key_0;

    protected String key_1;

    protected Object value_0;

    protected Object value_1;

    /**
     * Puts the given object into the Nodemaster, associated with the given key.
     * 
     * @param keyToUse the key to use
     * @param valueToPut the value to put
     * @return the same object that was put into the Nodemaster
     */
    public Object put(String keyToUse, Object valueToPut) {
        if (this.size < 2) {
            if (this.size == 0) {
                this.key_0 = keyToUse.toUpperCase().intern();
                if (valueToPut instanceof String) {
                    this.value_0 = ((String) valueToPut).intern();
                } else {
                    this.value_0 = valueToPut;
                }
                this.size = 1;
                return this.value_0;
            }
            this.key_1 = keyToUse.toUpperCase().intern();
            if (valueToPut instanceof String) {
                this.value_1 = ((String) valueToPut).intern();
            } else {
                this.value_1 = valueToPut;
            }
            this.size = 2;
            return this.value_1;
        } else if (this.size == 2) {
            this.hidden = new LinkedHashMap<String, Object>();
            this.hidden.put(this.key_0, this.value_0);
            this.hidden.put(this.key_1, this.value_1);
            this.key_0 = null;
            this.key_1 = null;
            this.value_0 = null;
            this.value_1 = null;
            this.size = 3;
            if (valueToPut instanceof String) {
                return this.hidden.put(keyToUse.toUpperCase().intern(), ((String) valueToPut).intern());
            }
            return this.hidden.put(keyToUse.toUpperCase().intern(), valueToPut);
        } else {
            this.size++;
            if (valueToPut instanceof String) {
                return this.hidden.put(keyToUse.toUpperCase().intern(), ((String) valueToPut).intern());
            }
            return this.hidden.put(keyToUse.toUpperCase().intern(), valueToPut);
        }
    }

    /**
     * Removes the given object from the Nodemaster.
     * 
     * @param valueToRemove the object to remove
     */
    public void remove(Object valueToRemove) {
        if (this.size == 2 || this.size == 1) {
            if (valueToRemove.equals(this.value_0)) {
                this.value_0 = null;
                this.key_0 = null;
            } else if (valueToRemove.equals(this.value_1)) {
                this.value_1 = null;
                this.key_1 = null;
            } else {
                Logger.getLogger("programd.graphmaster").error(String.format("Key was not found for value when trying to remove \"%s\".", valueToRemove));
                return;
            }
            this.size--;
        } else if (this.size > 2) {
            Object keyToRemove = null;
            for (Map.Entry<String, Object> item : this.hidden.entrySet()) {
                if (item.getValue().equals(valueToRemove)) {
                    keyToRemove = item.getKey();
                    break;
                }
            }
            if (keyToRemove == null) {
                Logger.getLogger("programd.graphmaster").error(String.format("Key was not found for value when trying to remove \"%s\".", valueToRemove));
                return;
            }
            if (this.size > 3) {
                this.hidden.remove(keyToRemove);
                this.size--;
            } else {
                this.hidden.remove(keyToRemove);
                this.key_1 = this.hidden.keySet().iterator().next();
                this.value_1 = this.hidden.remove(this.key_1);
                this.key_0 = this.hidden.keySet().iterator().next();
                this.value_0 = this.hidden.remove(this.key_0);
                this.hidden = null;
                this.size = 2;
            }
        } else if (this.size == 0) {
            Logger.getLogger("programd.graphmaster").error(String.format("No keys in Nodemapper when trying to remove \"%s\".", valueToRemove));
        }
    }

    /**
     * Gets the object associated with the specified key.
     * 
     * @param keyToGet the key to use
     * @return the object associated with the given key
     */
    public Object get(String keyToGet) {
        if (this.size == 0) {
            return null;
        } else if (this.size == 2 || this.size == 1) {
            if (keyToGet.equalsIgnoreCase(this.key_0)) {
                return this.value_0;
            }
            if (keyToGet.equalsIgnoreCase(this.key_1)) {
                return this.value_1;
            }
            return null;
        } else {
            return this.hidden.get(keyToGet.toUpperCase());
        }
    }

    /**
     * @return the keyset of the Nodemaster
     */
    public Set<String> keySet() {
        if (this.size < 3) {
            Set<String> result = new HashSet<String>();
            if (this.key_0 != null) {
                result.add(this.key_0);
            }
            if (this.key_1 != null) {
                result.add(this.key_1);
            }
            return result;
        }
        return this.hidden.keySet();
    }

    /**
     * @param keyToCheck the key to check
     * @return whether or not the Nodemaster contains the given key
     */
    public boolean containsKey(String keyToCheck) {
        if (this.size == 0) {
            return false;
        } else if (this.size == 2 || this.size == 1) {
            return (keyToCheck.equalsIgnoreCase(this.key_0) || keyToCheck.equalsIgnoreCase(this.key_1));
        }
        return this.hidden.containsKey(keyToCheck.toUpperCase());
    }

    /**
     * @return the size of the Nodemaster
     */
    public int size() {
        return this.size;
    }

    public double getAverageSize() {
        double total = 0d;
        if (this.size < 3) {
            if (this.value_0 != null && this.value_0 instanceof Nodemapper) {
                total += ((Nodemapper) this.value_0).getAverageSize();
            }
            if (this.value_1 != null && this.value_1 instanceof Nodemapper) {
                total += ((Nodemapper) this.value_1).getAverageSize();
            }
        } else {
            for (Object object : this.hidden.values()) {
                if (object instanceof Nodemapper) {
                    total += ((Nodemapper) object).getAverageSize();
                }
            }
        }
        if (this.parent != null) {
            return (this.size + (total / this.size)) / 2d;
        }
        return total / this.size;
    }
}
