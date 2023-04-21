package org.simpleframework.http;

import java.util.Set;

/**
 * This is an <code>Attributes</code> object which is used to convey 
 * attributes for a given connection. This is used to describe any 
 * specific attributes that a connection may have, for example any 
 * security policy for HTTPS. The <code>Attributes</code> provide a
 * subset of the methods provided by the <code>Map</code> object.
 * 
 * @author Niall Gallagher   
 */
public interface Attributes {

    /**
    * The <code>put</code> method is used to insert a mapping to
    * the attributes that pairs the issued name with the issued
    * value. The value can be referenced in future by its name.
    *
    * @param name this is the name of the value being inserted
    * @param value this is the named value that is inserted
    */
    public void put(String name, Object value);

    /**
    * The <code>get</code> method is used to retrieve the value
    * mapped to the specified name. If a value does not exist
    * matching the given name, then this returns null.
    * 
    * @param name this is the name of the value to be retrieved
    *  
    * @return returns the value if it exists or null otherwise
    */
    public Object get(String name);

    /**
    * The <code>remove</code> method is used to remove the 
    * named value from the attributes. This method will remove
    * the value or returns silently if the name does not exits.
    *
    * @param name this is the name of the value to be removed
    */
    public void remove(String name);

    /**
    * To ascertain what mappings exist, the names of all values
    * previously put into this attributes can be retrieved with 
    * this method. This will return a <code>Set</code> that 
    * contains the names of all the mappings added to this.
    *
    * @return this returns all the keys for existing mappings
    */
    public Set keySet();

    /**
    * The <code>contains</code> method is used to determine if
    * a mapping exists for the given name. This returns true if
    * the mapping exists or false otherwise.
    *
    * @param name this is the name of the mapping to determine
    *
    * @return returns true if a mapping exists, false otherwise
    */
    public boolean contains(String name);
}
