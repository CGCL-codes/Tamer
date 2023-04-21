package org.nakedobjects.persistence.hibernate;

import java.io.Serializable;
import java.rmi.dgc.VMID;
import org.nakedobjects.object.Oid;
import org.nakedobjects.utility.Assert;

public final class HibernateOid implements Oid {

    private String className;

    private Serializable primaryKey;

    /** 
	 * Id to return to hibernate - may not be the same as primaryKey if the
	 * HibernateOid has not been madePersistent
	 */
    private Serializable hibernateId;

    private HibernateOid previous;

    private int cachedHashCode;

    private String cachedToString;

    /**
	 * Create a new transient id
	 */
    public HibernateOid(final Class clazz) {
        this(clazz.getName(), new VMID(), null);
    }

    public HibernateOid(final Class clazz, final Serializable primaryKey) {
        this(clazz.getName(), primaryKey, primaryKey);
    }

    public HibernateOid(final String className, final Serializable primaryKey) {
        this(className, primaryKey, primaryKey);
    }

    public HibernateOid(final String className, final Serializable primaryKey, final Serializable hibernateId) {
        this.className = className;
        this.primaryKey = primaryKey;
        this.hibernateId = hibernateId;
        cacheState();
    }

    private void cacheState() {
        cachedHashCode = 17;
        cachedHashCode = 37 * cachedHashCode + className.hashCode();
        cachedHashCode = 37 * cachedHashCode + primaryKey.hashCode();
        cachedToString = (isTransient() ? "T" : "") + "OID#" + primaryKey.toString() + "/" + className + (hibernateId == null ? "" : "(" + hibernateId + ")") + (previous == null ? "" : "+");
    }

    public void copyFrom(final Oid oid) {
        Assert.assertTrue(oid instanceof HibernateOid);
        HibernateOid from = (HibernateOid) oid;
        this.primaryKey = from.primaryKey;
        this.className = from.className;
        this.hibernateId = from.hibernateId;
        cacheState();
    }

    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof HibernateOid) {
            HibernateOid o = ((HibernateOid) obj);
            return className.equals(o.className) && primaryKey.equals(o.primaryKey);
        }
        return false;
    }

    public String getClassName() {
        return className;
    }

    public Oid getPrevious() {
        return previous;
    }

    public Serializable getPrimaryKey() {
        return primaryKey;
    }

    public int hashCode() {
        return cachedHashCode;
    }

    public boolean hasPrevious() {
        return previous != null;
    }

    public boolean isTransient() {
        return primaryKey instanceof VMID;
    }

    public Serializable getHibernateId() {
        return hibernateId;
    }

    public void makePersistent() {
        Assert.assertTrue(isTransient());
        previous = new HibernateOid(this.className, this.primaryKey, null);
        this.primaryKey = hibernateId;
        cacheState();
    }

    public void setHibernateId(final Serializable hibernateId) {
        this.hibernateId = hibernateId;
    }

    public String toString() {
        return cachedToString;
    }
}
