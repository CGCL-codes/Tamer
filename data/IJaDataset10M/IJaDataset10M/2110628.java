package org.aitools.programd.graph;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * This is an optimization of {@link Nodemapper} that avoids creating the internal
 * {@link java.util.LinkedHashMap LinkedMap} until the number of mappings exceeds three (3).
 * 
 * @author <a href="mailto:noel@aitools.org">Noel Bush</a>
 */
public class ThreeOptimalMemoryNodemapper extends AbstractNodemaster {

    protected int size = 0;

    protected String key_0;

    protected String key_1;

    protected String key_2;

    protected Object value_0;

    protected Object value_1;

    protected Object value_2;

    /**
     * @see org.aitools.programd.graph.Nodemapper#put(java.lang.String, java.lang.Object)
     */
    public Object put(String key, Object value) {
        if (this.size < 3) {
            if (this.size == 0) {
                this.key_0 = key.toUpperCase().intern();
                if (value instanceof String) {
                    this.value_0 = ((String) value).intern();
                } else {
                    this.value_0 = value;
                }
                this.size = 1;
                return this.value_0;
            } else if (this.size == 1) {
                this.key_1 = key.toUpperCase().intern();
                if (value instanceof String) {
                    this.value_1 = ((String) value).intern();
                } else {
                    this.value_1 = value;
                }
                this.size = 2;
                return this.value_1;
            }
            this.key_2 = key.toUpperCase().intern();
            if (value instanceof String) {
                this.value_2 = ((String) value).intern();
            } else {
                this.value_2 = value;
            }
            this.size = 3;
            return this.value_2;
        } else if (this.size == 3) {
            this.hidden = new LinkedHashMap<String, Object>();
            this.hidden.put(this.key_0, this.value_0);
            this.hidden.put(this.key_1, this.value_1);
            this.hidden.put(this.key_2, this.value_2);
            this.key_0 = null;
            this.key_1 = null;
            this.key_2 = null;
            this.value_0 = null;
            this.value_1 = null;
            this.value_2 = null;
            this.size = 4;
            if (value instanceof String) {
                return this.hidden.put(key.toUpperCase().intern(), ((String) value).intern());
            }
            return this.hidden.put(key.toUpperCase().intern(), value);
        } else {
            this.size++;
            if (value instanceof String) {
                return this.hidden.put(key.toUpperCase().intern(), ((String) value).intern());
            }
            return this.hidden.put(key.toUpperCase().intern(), value);
        }
    }

    /**
     * @see org.aitools.programd.graph.Nodemapper#remove(java.lang.Object)
     */
    public void remove(Object value) {
        if (this.size == 3 || this.size == 2 || this.size == 1) {
            if (value.equals(this.value_0)) {
                this.value_0 = null;
                this.key_0 = null;
            } else if (value.equals(this.value_1)) {
                this.value_1 = null;
                this.key_1 = null;
            } else if (value.equals(this.value_2)) {
                this.value_2 = null;
                this.key_2 = null;
            } else {
                Logger.getLogger("programd.graphmaster").error(String.format("Key was not found for value when trying to remove \"%s\".", value));
                return;
            }
            this.size--;
        } else if (this.size > 3) {
            Object keyToRemove = null;
            for (Map.Entry<String, Object> item : this.hidden.entrySet()) {
                if (item.getValue().equals(value)) {
                    keyToRemove = item.getKey();
                    break;
                }
            }
            if (keyToRemove == null) {
                Logger.getLogger("programd.graphmaster").error(String.format("Key was not found for value when trying to remove \"%s\".", value));
                return;
            }
            if (this.size > 4) {
                this.hidden.remove(keyToRemove);
                this.size--;
            } else {
                this.hidden.remove(keyToRemove);
                this.key_2 = this.hidden.keySet().iterator().next();
                this.value_2 = this.hidden.remove(this.key_1);
                this.key_1 = this.hidden.keySet().iterator().next();
                this.value_1 = this.hidden.remove(this.key_1);
                this.key_0 = this.hidden.keySet().iterator().next();
                this.value_0 = this.hidden.remove(this.key_0);
                this.hidden = null;
                this.size = 3;
            }
        } else if (this.size == 0) {
            Logger.getLogger("programd.graphmaster").error(String.format("No keys in Nodemapper when trying to remove \"%s\".", value));
        }
    }

    /**
     * @see org.aitools.programd.graph.Nodemapper#get(java.lang.String)
     */
    public Object get(String key) {
        if (this.size == 0) {
            return null;
        } else if (this.size == 3 || this.size == 2 || this.size == 1) {
            if (key.equalsIgnoreCase(this.key_0)) {
                return this.value_0;
            }
            if (key.equalsIgnoreCase(this.key_1)) {
                return this.value_1;
            }
            if (key.equalsIgnoreCase(this.key_2)) {
                return this.value_2;
            }
            return null;
        } else {
            return this.hidden.get(key.toUpperCase());
        }
    }

    /**
     * @see org.aitools.programd.graph.Nodemapper#keySet()
     */
    public Set<String> keySet() {
        if (this.size < 4) {
            Set<String> result = new HashSet<String>();
            if (this.key_0 != null) {
                result.add(this.key_0);
            }
            if (this.key_1 != null) {
                result.add(this.key_1);
            }
            if (this.key_2 != null) {
                result.add(this.key_2);
            }
            return result;
        }
        return this.hidden.keySet();
    }

    /**
     * @see org.aitools.programd.graph.Nodemapper#containsKey(java.lang.String)
     */
    public boolean containsKey(String key) {
        if (this.size == 0) {
            return false;
        } else if (this.size == 3 || this.size == 2 || this.size == 1) {
            return (key.equalsIgnoreCase(this.key_0) || key.equalsIgnoreCase(this.key_1) || key.equalsIgnoreCase(this.key_2));
        }
        return this.hidden.containsKey(key.toUpperCase());
    }

    /**
     * @see org.aitools.programd.graph.Nodemapper#size()
     */
    public int size() {
        return this.size;
    }

    /**
     * @see org.aitools.programd.graph.Nodemapper#getAverageSize()
     */
    public double getAverageSize() {
        double total = 0d;
        if (this.size < 4) {
            if (this.value_0 != null && this.value_0 instanceof AbstractNodemaster) {
                total += ((AbstractNodemaster) this.value_0).getAverageSize();
            }
            if (this.value_1 != null && this.value_1 instanceof AbstractNodemaster) {
                total += ((AbstractNodemaster) this.value_1).getAverageSize();
            }
            if (this.value_2 != null && this.value_2 instanceof AbstractNodemaster) {
                total += ((AbstractNodemaster) this.value_2).getAverageSize();
            }
        } else {
            for (Object object : this.hidden.values()) {
                if (object instanceof AbstractNodemaster) {
                    total += ((AbstractNodemaster) object).getAverageSize();
                }
            }
        }
        if (this._parent != null) {
            return (this.size + (total / this.size)) / 2d;
        }
        return total / this.size;
    }
}
