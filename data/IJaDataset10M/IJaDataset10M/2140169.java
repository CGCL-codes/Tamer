package com.vividsolutions.xdo;

import java.net.URI;
import com.vividsolutions.xdo.xsi.Schema;

/**
 * @author dzwiers
 */
public interface SchemaBuilder {

    /**
     * This is typically used as a look up action.
     * 
     * If the namespace is null (shouldn't happen, but might), return Null.
     * 
     * Otherwise, if you have a Schema implementation for the namespace, return it, Null otherwise.
     * 
     * @param namespace
     * @return
     */
    public Schema build(URI namespace);

    /**
     * This is a multi-funcational method. Depending on the inputs, 
     * different actions may occur.
     * 
     * The implementator should endeavor to create a valid Schema object where posible. 
     * This may include a parsing action.
     * 
     * When the location is Null and the namespace is non-null, then this method should act as if build(namespace);
     * 
     * When the namespace is Null, then the builder should attempt to parse the URI if they can. This action is not 
     * required, infact in many cases it is discouraged as there is an XML schema parser included. Users may wish 
     * to implement non-standard schema parser implementations here.
     * 
     * If neither param is Null, either of the actions listed above are valid. 
     * 
     * You should never be called with both null values ... but if this does happen, just return Null.
     * 
     * @param namespace May be Null
     * @param location May be Null
     * @return
	 */
    public Schema build(URI namespace, URI location);
}
