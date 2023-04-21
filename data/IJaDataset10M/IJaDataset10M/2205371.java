package org.exist.util.serializer;

import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * @author Wolfgang Meier (wolfgang@exist-db.org)
 */
public class DOMSerializerObjectFactory extends BasePoolableObjectFactory {

    public DOMSerializerObjectFactory() {
        super();
    }

    public Object makeObject() throws Exception {
        return new DOMSerializer();
    }

    public void activateObject(Object obj) throws Exception {
        ((DOMSerializer) obj).reset();
    }
}
