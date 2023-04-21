package ru.scriptum.db4o.spring;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

/**
 * Constructs a object container for a db4o database.
 */
public class Db4oFileFactoryBean extends Db4oAbstractLocalFactoryBean {

    protected Object createInstance() throws Exception {
        return Db4o.openFile(getLocation());
    }

    public Class getObjectType() {
        return ObjectContainer.class;
    }

    public void destroy() throws Exception {
        ObjectContainer container = (ObjectContainer) getSingleton();
        if (container != null) container.close();
    }
}
