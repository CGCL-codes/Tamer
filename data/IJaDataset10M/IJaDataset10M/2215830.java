package org.equanda.persistence;

import javax.persistence.EntityManager;

/**
 * Abstract base for all mediator root classes
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public abstract class EquandaMediatorRoot<ENTITY extends EquandaEntity, BEAN extends EquandaEJB> implements ExceptionCodes {

    protected EntityManager em;

    protected ENTITY entity;

    protected BEAN object;

    public void init(ENTITY entity, BEAN object, EntityManager em) {
        this.entity = entity;
        this.object = object;
        this.em = em;
    }

    public ENTITY getEquandaEntity() {
        return entity;
    }

    public Uoid getId() {
        return entity.getId();
    }

    public String getEquandaType() {
        String type = entity.getEquandaType();
        if (type.length() > 4) type = type.substring(0, 4);
        return type;
    }

    public java.sql.Timestamp getEquandaModificationDate() {
        return entity.getEquandaModificationDate();
    }

    public java.sql.Timestamp getEquandaCreationDate() {
        return entity.getEquandaCreationDate();
    }

    public long getEquandaVersion() {
        return entity.getEquandaVersion();
    }

    public String getEquandaStatus() {
        String status = entity.getEquandaStatus();
        if (status != null && status.length() > 1) status = status.substring(0, 1);
        return status;
    }

    public void setEquandaStatus(String status) {
        entity.setEquandaStatus(status);
    }

    public void create() throws EquandaPersistenceException {
    }

    public abstract boolean isEquandaType(String type);

    public abstract boolean isEquandaParentType(String type);

    public abstract ObjectType getEquandaTypeObject();

    protected void checkImmutableIf(String field, String exclude) throws EquandaPersistenceException {
    }

    /**
     * utility routine to test equality of objects without relying on one of them being not null.
     *
     * @param obj1 one of the objects to compare
     * @param obj2 other object to compare
     * @return true when bot objects are equal
     */
    protected boolean testEquals(Object obj1, Object obj2) {
        return (obj1 == null) ? (obj2 == null) : (obj1.equals(obj2));
    }

    /**
     * utility routine to allow checking the integrity of the record
     *
     * @throws EquandaPersistenceException when constraints are violated
     */
    public void equandaCheckRules() throws EquandaPersistenceException {
    }
}
