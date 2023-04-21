package org.openxava.test.model.xejb;

/**
 * CMP layer for State.
 */
public abstract class StateCMP extends org.openxava.test.model.xejb.StateBean implements javax.ejb.EntityBean {

    public org.openxava.test.model.StateData getData() {
        org.openxava.test.model.StateData dataHolder = null;
        try {
            dataHolder = new org.openxava.test.model.StateData();
            dataHolder.setId(getId());
            dataHolder.set_Name(get_Name());
        } catch (RuntimeException e) {
            throw new javax.ejb.EJBException(e);
        }
        return dataHolder;
    }

    public void setData(org.openxava.test.model.StateData dataHolder) {
        try {
            set_Name(dataHolder.get_Name());
        } catch (Exception e) {
            throw new javax.ejb.EJBException(e);
        }
    }

    public void ejbLoad() {
        super.ejbLoad();
    }

    public void ejbStore() {
        super.ejbStore();
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
        StateValue = null;
    }

    public void setEntityContext(javax.ejb.EntityContext ctx) {
        super.setEntityContext(ctx);
    }

    public void unsetEntityContext() {
        super.unsetEntityContext();
    }

    public void ejbRemove() throws javax.ejb.RemoveException {
        super.ejbRemove();
    }

    private org.openxava.test.model.StateValue StateValue = null;

    public org.openxava.test.model.StateValue getStateValue() {
        StateValue = new org.openxava.test.model.StateValue();
        try {
            StateValue.setId(getId());
            StateValue.setName(getName());
            StateValue.setFullName(getFullName());
        } catch (Exception e) {
            throw new javax.ejb.EJBException(e);
        }
        return StateValue;
    }

    public void setStateValue(org.openxava.test.model.StateValue valueHolder) {
        try {
            setName(valueHolder.getName());
            setFullName(valueHolder.getFullName());
        } catch (Exception e) {
            throw new javax.ejb.EJBException(e);
        }
    }

    public abstract java.lang.String getId();

    public abstract void setId(java.lang.String id);

    public abstract java.lang.String get_Name();

    public abstract void set_Name(java.lang.String _Name);
}
