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
public class DeliveryDetail implements java.io.Serializable, org.openxava.test.model.IDeliveryDetail {

    public DeliveryDetail() {
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
            throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Description", "DeliveryDetail", "String"));
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
            throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Description", "DeliveryDetail", "String"));
        }
    }

    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int newNumber) {
        this.number = newNumber;
    }

    private org.openxava.test.model.IDelivery delivery;

    public org.openxava.test.model.IDelivery getDelivery() {
        if (delivery != null) {
            try {
                delivery.toString();
            } catch (Exception ex) {
                return null;
            }
        }
        return delivery;
    }

    public void setDelivery(org.openxava.test.model.IDelivery newDelivery) {
        if (newDelivery != null && !(newDelivery instanceof org.openxava.test.model.Delivery)) {
            throw new IllegalArgumentException(XavaResources.getString("ejb_to_pojo_illegal"));
        }
        this.delivery = newDelivery;
    }

    private static MetaModel metaModel;

    public MetaModel getMetaModel() throws XavaException {
        if (metaModel == null) {
            metaModel = MetaComponent.get("Delivery").getMetaAggregate("DeliveryDetail");
        }
        return metaModel;
    }

    public String toString() {
        try {
            return getMetaModel().toString(this);
        } catch (XavaException ex) {
            System.err.println(XavaResources.getString("toString_warning", "DeliveryDetail"));
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
