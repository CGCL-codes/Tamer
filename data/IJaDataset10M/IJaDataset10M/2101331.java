package org.openxava.test.ejb.xejb;

/**
 * CMP layer for ServiceType.
 */
public abstract class ServiceTypeCMP extends org.openxava.test.ejb.xejb.ServiceTypeBean implements javax.ejb.EntityBean {

    public org.openxava.test.ejb.ServiceTypeData getData() {
        org.openxava.test.ejb.ServiceTypeData dataHolder = null;
        try {
            dataHolder = new org.openxava.test.ejb.ServiceTypeData();
            dataHolder.set_Family(get_Family());
            dataHolder.set_Description(get_Description());
            dataHolder.set_Subfamily(get_Subfamily());
            dataHolder.setNumber(getNumber());
        } catch (RuntimeException e) {
            throw new javax.ejb.EJBException(e);
        }
        return dataHolder;
    }

    public void setData(org.openxava.test.ejb.ServiceTypeData dataHolder) {
        try {
            set_Family(dataHolder.get_Family());
            set_Description(dataHolder.get_Description());
            set_Subfamily(dataHolder.get_Subfamily());
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
        ServiceTypeValue = null;
    }

    public void setEntityContext(javax.ejb.EntityContext ctx) {
        super.setEntityContext(ctx);
    }

    public void unsetEntityContext() {
        super.unsetEntityContext();
    }

    public void ejbRemove() throws javax.ejb.RemoveException {
    }

    private org.openxava.test.ejb.ServiceTypeValue ServiceTypeValue = null;

    public org.openxava.test.ejb.ServiceTypeValue getServiceTypeValue() {
        ServiceTypeValue = new org.openxava.test.ejb.ServiceTypeValue();
        try {
            ServiceTypeValue.setFamily(getFamily());
            ServiceTypeValue.setDescription(getDescription());
            ServiceTypeValue.setSubfamily(getSubfamily());
            ServiceTypeValue.setNumber(getNumber());
        } catch (Exception e) {
            throw new javax.ejb.EJBException(e);
        }
        return ServiceTypeValue;
    }

    public void setServiceTypeValue(org.openxava.test.ejb.ServiceTypeValue valueHolder) {
        try {
            setFamily(valueHolder.getFamily());
            setDescription(valueHolder.getDescription());
            setSubfamily(valueHolder.getSubfamily());
        } catch (Exception e) {
            throw new javax.ejb.EJBException(e);
        }
    }

    public abstract java.lang.Integer get_Family();

    public abstract void set_Family(java.lang.Integer _Family);

    public abstract java.lang.String get_Description();

    public abstract void set_Description(java.lang.String _Description);

    public abstract java.lang.Integer get_Subfamily();

    public abstract void set_Subfamily(java.lang.Integer _Subfamily);

    public abstract int getNumber();

    public abstract void setNumber(int number);
}
