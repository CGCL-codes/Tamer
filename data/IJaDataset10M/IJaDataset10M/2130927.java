package org.openxava.test.model.xejb;

/**
 * CMP layer for TransportCharge2.
 */
public abstract class TransportCharge2CMP extends org.openxava.test.model.xejb.TransportCharge2Bean implements javax.ejb.EntityBean {

    public org.openxava.test.model.TransportCharge2Data getData() {
        org.openxava.test.model.TransportCharge2Data dataHolder = null;
        try {
            dataHolder = new org.openxava.test.model.TransportCharge2Data();
            dataHolder.set_Amount(get_Amount());
            dataHolder.setYear(getYear());
            dataHolder.set_Delivery_number(get_Delivery_number());
            dataHolder.set_Delivery_invoice_number(get_Delivery_invoice_number());
            dataHolder.set_Delivery_type_number(get_Delivery_type_number());
        } catch (RuntimeException e) {
            throw new javax.ejb.EJBException(e);
        }
        return dataHolder;
    }

    public void setData(org.openxava.test.model.TransportCharge2Data dataHolder) {
        try {
            set_Amount(dataHolder.get_Amount());
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
        TransportCharge2Value = null;
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

    private org.openxava.test.model.TransportCharge2Value TransportCharge2Value = null;

    public org.openxava.test.model.TransportCharge2Value getTransportCharge2Value() {
        TransportCharge2Value = new org.openxava.test.model.TransportCharge2Value();
        try {
            TransportCharge2Value.setAmount(getAmount());
            TransportCharge2Value.setYear(getYear());
            TransportCharge2Value.setDelivery_number(getDelivery_number());
            TransportCharge2Value.setDelivery_invoice_year(getDelivery_invoice_year());
            TransportCharge2Value.setDelivery_invoice_number(getDelivery_invoice_number());
            TransportCharge2Value.setDelivery_type_number(getDelivery_type_number());
        } catch (Exception e) {
            throw new javax.ejb.EJBException(e);
        }
        return TransportCharge2Value;
    }

    public void setTransportCharge2Value(org.openxava.test.model.TransportCharge2Value valueHolder) {
        try {
            setAmount(valueHolder.getAmount());
            setDelivery_number(valueHolder.getDelivery_number());
            setDelivery_invoice_year(valueHolder.getDelivery_invoice_year());
            setDelivery_invoice_number(valueHolder.getDelivery_invoice_number());
            setDelivery_type_number(valueHolder.getDelivery_type_number());
        } catch (Exception e) {
            throw new javax.ejb.EJBException(e);
        }
    }

    public abstract java.math.BigDecimal get_Amount();

    public abstract void set_Amount(java.math.BigDecimal _Amount);

    public abstract int getYear();

    public abstract void setYear(int year);

    public abstract int get_Delivery_number();

    public abstract void set_Delivery_number(int _Delivery_number);

    public abstract int get_Delivery_invoice_number();

    public abstract void set_Delivery_invoice_number(int _Delivery_invoice_number);

    public abstract int get_Delivery_type_number();

    public abstract void set_Delivery_type_number(int _Delivery_type_number);
}
