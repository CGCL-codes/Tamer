package com.elibera.msgs.threading;

/**
 * @author meisi
 *
 */
public interface DBObject extends java.io.Serializable {

    public Object getUniqueID();

    public void setUniqueID(Object id);
}
