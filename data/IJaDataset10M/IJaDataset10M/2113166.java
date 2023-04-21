package com.db4o.drs.inside;

import com.db4o.ext.Db4oUUID;

public interface ReplicationReference {

    Db4oUUID uuid();

    /**
	 * IMPORTANT
	 * <p/>
	 * Must return the latests version of the object AND OF ALL COLLECTIONS IT REFERENCES IN ITS
	 * FIELDS because collections are treated as 2nd class objects (just like arrays) for Hibernate replication
	 * compatibility purposes.
	 */
    long version();

    Object object();

    Object counterpart();

    void setCounterpart(Object obj);

    void markForReplicating();

    boolean isMarkedForReplicating();

    void markForDeleting();

    boolean isMarkedForDeleting();

    void markCounterpartAsNew();

    boolean isCounterpartNew();
}
