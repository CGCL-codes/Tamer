package org.openxava.test.model;

import java.util.*;
import java.math.*;
import java.rmi.RemoteException;
import org.openxava.component.MetaComponent;
import org.openxava.model.meta.MetaModel;
import org.openxava.util.*;

/**
 * 
 * @author MCarmen Gimeno
 */
public class DeliveryType implements java.io.Serializable, org.openxava.test.model.IDeliveryType {

    public DeliveryType() {
        initMembers();
    }

    private void initMembers() {
        setNumber(0);
        setDescription(null);
    }

    private static org.openxava.converters.IConverter descriptionConverter;

    private org.openxava.converters.IConverter getDescriptionConverter() {
        if (descriptionConverter == null) {
            try {
                descriptionConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("description");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "description"));
            }
        }
        return descriptionConverter;
    }

    private java.lang.String description;

    private java.lang.String get_Description() {
        return description;
    }

    private void set_Description(java.lang.String newDescription) {
        this.description = newDescription;
    }

    /**
	 * 
	 * 
	 */
    public String getDescription() {
        try {
            return (String) getDescriptionConverter().toJava(get_Description());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Description", "DeliveryType", "String"));
        }
    }

    /**
	 * 
	 */
    public void setDescription(String newDescription) {
        try {
            set_Description((java.lang.String) getDescriptionConverter().toDB(newDescription));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Description", "DeliveryType", "String"));
        }
    }

    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int newNumber) {
        this.number = newNumber;
    }

    private java.util.Collection deliveries;

    public java.util.Collection getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(java.util.Collection deliveries) {
        this.deliveries = deliveries;
    }

    public static DeliveryType findByNumber(int number) throws javax.ejb.ObjectNotFoundException {
        if (XavaPreferences.getInstance().isJPAPersistence()) {
            javax.persistence.Query query = org.openxava.jpa.XPersistence.getManager().createQuery("from DeliveryType as o where o.number = :arg0");
            query.setParameter("arg0", new Integer(number));
            try {
                return (DeliveryType) query.getSingleResult();
            } catch (Exception ex) {
                if (ex.getClass().getName().equals("javax.persistence.NoResultException")) {
                    throw new javax.ejb.ObjectNotFoundException(XavaResources.getString("object_not_found", "DeliveryType"));
                } else {
                    ex.printStackTrace();
                    throw new RuntimeException(ex.getMessage());
                }
            }
        } else {
            org.hibernate.Query query = org.openxava.hibernate.XHibernate.getSession().createQuery("from DeliveryType as o where o.number = :arg0");
            query.setParameter("arg0", new Integer(number));
            DeliveryType r = (DeliveryType) query.uniqueResult();
            if (r == null) {
                throw new javax.ejb.ObjectNotFoundException(XavaResources.getString("object_not_found", "DeliveryType"));
            }
            return r;
        }
    }

    private static MetaModel metaModel;

    public MetaModel getMetaModel() throws XavaException {
        if (metaModel == null) {
            metaModel = MetaComponent.get("DeliveryType").getMetaEntity();
        }
        return metaModel;
    }

    public String toString() {
        try {
            return getMetaModel().toString(this);
        } catch (XavaException ex) {
            System.err.println(XavaResources.getString("toString_warning", "DeliveryType"));
            return super.toString();
        }
    }

    public boolean equals(Object other) {
        if (other == null) return false;
        return toString().equals(other.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
