package self.micromagic.util.container;

import java.util.Iterator;
import java.util.Enumeration;

public interface ValueContainer {

    /**
    * Get the value at the key.
    */
    Object getValue(Object key);

    /**
    * Set the value to the key.
    */
    void setValue(Object key, Object value);

    /**
    * Remove the value at the key.
    */
    void removeValue(Object key);

    /**
    * Get the keys enumeration.
    */
    Enumeration getKeys();
}
