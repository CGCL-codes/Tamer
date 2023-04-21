package org.openxava.test.model.xejb;

import java.util.*;
import java.math.*;
import javax.ejb.*;
import javax.rmi.PortableRemoteObject;
import org.openxava.ejbx.*;
import org.openxava.util.*;
import org.openxava.component.*;
import org.openxava.model.meta.*;
import org.openxava.validators.ValidationException;
import org.openxava.test.model.*;

/**
 * @ejb:bean name="Delivery" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/Delivery" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IDelivery"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Delivery" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByInvoice(int year, int number)" query="SELECT OBJECT(o) FROM Delivery o WHERE o._Invoice_year = ?1 AND o._Invoice_number = ?2 ORDER BY o.number" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByInvoice(int year, int number)" query="SELECT OBJECT(o) FROM Delivery o WHERE o._Invoice_year = ?1 AND o._Invoice_number = ?2 ORDER BY o.number" 	
 * @ejb:finder signature="Collection findByShipment(java.lang.String type, int mode, int number)" query="SELECT OBJECT(o) FROM Delivery o WHERE o._Shipment_type = ?1 AND o._Shipment_mode = ?2 AND o._Shipment_number = ?3 ORDER BY o.number" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByShipment(java.lang.String type, int mode, int number)" query="SELECT OBJECT(o) FROM Delivery o WHERE o._Shipment_type = ?1 AND o._Shipment_mode = ?2 AND o._Shipment_number = ?3 ORDER BY o.number" 	
 * @ejb:finder signature="Collection findByCarrier(java.lang.Integer number)" query="SELECT OBJECT(o) FROM Delivery o WHERE o._Carrier_number = ?1 ORDER BY o.number" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByCarrier(java.lang.Integer number)" query="SELECT OBJECT(o) FROM Delivery o WHERE o._Carrier_number = ?1 ORDER BY o.number" 	
 * @ejb:finder signature="Collection findByType(int number)" query="SELECT OBJECT(o) FROM Delivery o WHERE o._Type_number = ?1 ORDER BY o.number" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByType(int number)" query="SELECT OBJECT(o) FROM Delivery o WHERE o._Type_number = ?1 ORDER BY o.number" 	
 * @ejb:finder signature="Collection findAll()" query="SELECT OBJECT(o) FROM Delivery o" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findAll()" query="SELECT OBJECT(o) FROM Delivery o" 
 * 
 * @jboss:table-name "XAVATEST.DELIVERY"
 *
 * @author Javier Paniza
 */
public abstract class DeliveryBean extends EJBReplicableBase implements org.openxava.test.model.IDelivery, EntityBean {

    private boolean creating = false;

    private boolean modified = false;

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.DeliveryKey ejbCreate(Map values) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        executeSets(values);
        return null;
    }

    public void ejbPostCreate(Map values) throws CreateException, ValidationException {
    }

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.DeliveryKey ejbCreate(org.openxava.test.model.DeliveryData data) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setData(data);
        set_Invoice_year(data.get_Invoice_year());
        set_Invoice_number(data.get_Invoice_number());
        set_Type_number(data.get_Type_number());
        setNumber(data.getNumber());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.model.DeliveryData data) throws CreateException, ValidationException {
    }

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.DeliveryKey ejbCreate(org.openxava.test.model.DeliveryValue value) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setDeliveryValue(value);
        setInvoice_year(value.getInvoice_year());
        setInvoice_number(value.getInvoice_number());
        setType_number(value.getType_number());
        setNumber(value.getNumber());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.model.DeliveryValue value) throws CreateException, ValidationException {
    }

    public void ejbLoad() {
        creating = false;
        modified = false;
    }

    public void ejbStore() {
        if (creating) {
            creating = false;
            return;
        }
        if (!modified) return;
        modified = false;
    }

    public void ejbRemove() throws RemoveException {
    }

    private static org.openxava.converters.IConverter incidentsConverter;

    private org.openxava.converters.IConverter getIncidentsConverter() {
        if (incidentsConverter == null) {
            try {
                incidentsConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("incidents");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "incidents"));
            }
        }
        return incidentsConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "INCIDENTS"
	 */
    public abstract java.lang.String get_Incidents();

    public abstract void set_Incidents(java.lang.String newIncidents);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.lang.String getIncidents() {
        try {
            return (java.lang.String) getIncidentsConverter().toJava(get_Incidents());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Incidents", "Delivery", "java.lang.String"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setIncidents(java.lang.String newIncidents) {
        try {
            this.modified = true;
            set_Incidents((java.lang.String) getIncidentsConverter().toDB(newIncidents));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Incidents", "Delivery", "java.lang.String"));
        }
    }

    private static org.openxava.converters.IConverter driverTypeConverter;

    private org.openxava.converters.IConverter getDriverTypeConverter() {
        if (driverTypeConverter == null) {
            try {
                driverTypeConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("driverType");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "driverType"));
            }
        }
        return driverTypeConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DRIVERTYPE"
	 */
    public abstract java.lang.String get_DriverType();

    public abstract void set_DriverType(java.lang.String newDriverType);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public String getDriverType() {
        try {
            return (String) getDriverTypeConverter().toJava(get_DriverType());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "DriverType", "Delivery", "String"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setDriverType(String newDriverType) {
        try {
            this.modified = true;
            set_DriverType((java.lang.String) getDriverTypeConverter().toDB(newDriverType));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "DriverType", "Delivery", "String"));
        }
    }

    private static org.openxava.converters.IConverter distanceConverter;

    private org.openxava.converters.IConverter getDistanceConverter() {
        if (distanceConverter == null) {
            try {
                distanceConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("distance");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "distance"));
            }
        }
        return distanceConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DISTANCE"
	 */
    public abstract java.lang.String get_Distance();

    public abstract void set_Distance(java.lang.String newDistance);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public int getDistance() {
        try {
            return ((Integer) getDistanceConverter().toJava(get_Distance())).intValue();
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Distance", "Delivery", "int"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setDistance(int newDistance) {
        try {
            this.modified = true;
            set_Distance((java.lang.String) getDistanceConverter().toDB(new Integer(newDistance)));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Distance", "Delivery", "int"));
        }
    }

    private static org.openxava.converters.IConverter descriptionConverter;

    private org.openxava.converters.IConverter getDescriptionConverter() {
        if (descriptionConverter == null) {
            try {
                descriptionConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("description");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "description"));
            }
        }
        return descriptionConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DESCRIPTION"
	 */
    public abstract java.lang.String get_Description();

    public abstract void set_Description(java.lang.String newDescription);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public String getDescription() {
        try {
            return (String) getDescriptionConverter().toJava(get_Description());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Delivery", "String"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setDescription(String newDescription) {
        try {
            this.modified = true;
            set_Description((java.lang.String) getDescriptionConverter().toDB(newDescription));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Delivery", "String"));
        }
    }

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.util.Date getDateAsLabel() {
        boolean cmtActivated = false;
        if (!org.openxava.hibernate.XHibernate.isCmt()) {
            org.openxava.hibernate.XHibernate.setCmt(true);
            cmtActivated = true;
        }
        try {
            org.openxava.calculators.ByPassCalculator dateAsLabelCalculator = (org.openxava.calculators.ByPassCalculator) getMetaModel().getMetaProperty("dateAsLabel").getMetaCalculator().createCalculator();
            dateAsLabelCalculator.setSource(getDate());
            return (java.util.Date) dateAsLabelCalculator.calculate();
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "DateAsLabel", "Delivery", ex.getLocalizedMessage()));
        } finally {
            if (cmtActivated) {
                org.openxava.hibernate.XHibernate.setCmt(false);
            }
        }
    }

    public void setDateAsLabel(java.util.Date newDateAsLabel) {
    }

    private static org.openxava.converters.IConverter vehicleConverter;

    private org.openxava.converters.IConverter getVehicleConverter() {
        if (vehicleConverter == null) {
            try {
                vehicleConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("vehicle");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "vehicle"));
            }
        }
        return vehicleConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "VEHICLE"
	 */
    public abstract java.lang.String get_Vehicle();

    public abstract void set_Vehicle(java.lang.String newVehicle);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public String getVehicle() {
        try {
            return (String) getVehicleConverter().toJava(get_Vehicle());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Vehicle", "Delivery", "String"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setVehicle(String newVehicle) {
        try {
            this.modified = true;
            set_Vehicle((java.lang.String) getVehicleConverter().toDB(newVehicle));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Vehicle", "Delivery", "String"));
        }
    }

    private static org.openxava.converters.IConverter remarksConverter;

    private org.openxava.converters.IConverter getRemarksConverter() {
        if (remarksConverter == null) {
            try {
                remarksConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("remarks");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "remarks"));
            }
        }
        return remarksConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "REMARKS"
	 */
    public abstract java.lang.String get_Remarks();

    public abstract void set_Remarks(java.lang.String newRemarks);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.lang.String getRemarks() {
        try {
            return (java.lang.String) getRemarksConverter().toJava(get_Remarks());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Remarks", "Delivery", "java.lang.String"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setRemarks(java.lang.String newRemarks) {
        try {
            this.modified = true;
            set_Remarks((java.lang.String) getRemarksConverter().toDB(newRemarks));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Remarks", "Delivery", "java.lang.String"));
        }
    }

    /**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "NUMBER"
	 */
    public abstract int getNumber();

    /**
	  * 
	  */
    public abstract void setNumber(int newNumber);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public String getTransportMode() {
        boolean cmtActivated = false;
        if (!org.openxava.hibernate.XHibernate.isCmt()) {
            org.openxava.hibernate.XHibernate.setCmt(true);
            cmtActivated = true;
        }
        try {
            org.openxava.test.calculators.DeliveryTransportModeCalculator transportModeCalculator = (org.openxava.test.calculators.DeliveryTransportModeCalculator) getMetaModel().getMetaProperty("transportMode").getMetaCalculator().createCalculator();
            transportModeCalculator.setVehicle(getVehicle());
            return (String) transportModeCalculator.calculate();
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "TransportMode", "Delivery", ex.getLocalizedMessage()));
        } finally {
            if (cmtActivated) {
                org.openxava.hibernate.XHibernate.setCmt(false);
            }
        }
    }

    public void setTransportMode(String newTransportMode) {
    }

    private static org.openxava.converters.IConverter employeeConverter;

    private org.openxava.converters.IConverter getEmployeeConverter() {
        if (employeeConverter == null) {
            try {
                employeeConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("employee");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "employee"));
            }
        }
        return employeeConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "EMPLOYEE"
	 */
    public abstract java.lang.String get_Employee();

    public abstract void set_Employee(java.lang.String newEmployee);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public String getEmployee() {
        try {
            return (String) getEmployeeConverter().toJava(get_Employee());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Employee", "Delivery", "String"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setEmployee(String newEmployee) {
        try {
            this.modified = true;
            set_Employee((java.lang.String) getEmployeeConverter().toDB(newEmployee));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Employee", "Delivery", "String"));
        }
    }

    private static org.openxava.converters.Date3Converter dateConverter;

    private org.openxava.converters.Date3Converter getDateConverter() {
        if (dateConverter == null) {
            try {
                dateConverter = (org.openxava.converters.Date3Converter) getMetaModel().getMapping().getMultipleConverter("date");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "date"));
            }
        }
        return dateConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * @jboss:column-name "DAY"
	 */
    public abstract int getDate_day();

    public abstract void setDate_day(int newValue);

    /**	 
	 * @ejb:persistent-field
	 * @jboss:column-name "MONTH"
	 */
    public abstract int getDate_month();

    public abstract void setDate_month(int newValue);

    /**	 
	 * @ejb:persistent-field
	 * @jboss:column-name "YEAR"
	 */
    public abstract int getDate_year();

    public abstract void setDate_year(int newValue);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.util.Date getDate() {
        try {
            getDateConverter().setDay(getDate_day());
            getDateConverter().setMonth(getDate_month());
            getDateConverter().setYear(getDate_year());
            return (java.util.Date) getDateConverter().toJava();
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Date", "Delivery", "java.util.Date"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setDate(java.util.Date newDate) {
        try {
            this.modified = true;
            getDateConverter().toDB(newDate);
            setDate_day(getDateConverter().getDay());
            setDate_month(getDateConverter().getMonth());
            setDate_year(getDateConverter().getYear());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_db_error", "Date", "Delivery"));
        }
    }

    private org.openxava.test.model.DeliveryDetailHome detailsHome;

    /**
	 * @ejb:interface-method
	 */
    public java.util.Collection getDetails() {
        try {
            return getDetailsHome().findByDelivery(getInvoice_year(), getInvoice_number(), getType_number(), getNumber());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_collection_elements_error", "Details", "Delivery"));
        }
    }

    private org.openxava.test.model.DeliveryDetailHome getDetailsHome() throws Exception {
        if (detailsHome == null) {
            detailsHome = (org.openxava.test.model.DeliveryDetailHome) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.model/DeliveryDetail"), org.openxava.test.model.DeliveryDetailHome.class);
        }
        return detailsHome;
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.IInvoice getInvoice() {
        try {
            return getInvoiceHome().findByPrimaryKey(getInvoiceKey());
        } catch (ObjectNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_reference_error", "Invoice", "Delivery"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.InvoiceRemote getInvoiceRemote() {
        return (org.openxava.test.model.InvoiceRemote) getInvoice();
    }

    /**
	 * 
	 */
    public void setInvoice(org.openxava.test.model.IInvoice newInvoice) {
        this.modified = true;
        try {
            if (newInvoice == null) setInvoiceKey(null); else {
                if (newInvoice instanceof org.openxava.test.model.Invoice) {
                    throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
                }
                org.openxava.test.model.InvoiceRemote remote = (org.openxava.test.model.InvoiceRemote) newInvoice;
                setInvoiceKey((org.openxava.test.model.InvoiceKey) remote.getPrimaryKey());
            }
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("set_reference_error", "Invoice", "Delivery"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.InvoiceKey getInvoiceKey() {
        org.openxava.test.model.InvoiceKey key = new org.openxava.test.model.InvoiceKey();
        key.year = getInvoice_year();
        key.number = getInvoice_number();
        return key;
    }

    /**
	 * 
	 */
    public void setInvoiceKey(org.openxava.test.model.InvoiceKey key) {
        this.modified = true;
        if (key == null) {
            key = new org.openxava.test.model.InvoiceKey();
            setInvoice_year(key.year);
            setInvoice_number(key.number);
        } else {
            setInvoice_year(key.year);
            setInvoice_number(key.number);
        }
    }

    /**		
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @jboss:column-name "INVOICE_YEAR"
	 */
    public abstract int get_Invoice_year();

    public abstract void set_Invoice_year(int newInvoice_year);

    /**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public int getInvoice_year() {
        return get_Invoice_year();
    }

    public void setInvoice_year(int newInvoice_year) {
        set_Invoice_year(newInvoice_year);
    }

    /**		
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @jboss:column-name "INVOICE_NUMBER"
	 */
    public abstract int get_Invoice_number();

    public abstract void set_Invoice_number(int newInvoice_number);

    /**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public int getInvoice_number() {
        return get_Invoice_number();
    }

    public void setInvoice_number(int newInvoice_number) {
        set_Invoice_number(newInvoice_number);
    }

    private org.openxava.test.model.InvoiceHome invoiceHome;

    private org.openxava.test.model.InvoiceHome getInvoiceHome() throws Exception {
        if (invoiceHome == null) {
            invoiceHome = (org.openxava.test.model.InvoiceHome) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.model/Invoice"), org.openxava.test.model.InvoiceHome.class);
        }
        return invoiceHome;
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.IShipment getShipment() {
        try {
            return getShipmentHome().findByPrimaryKey(getShipmentKey());
        } catch (ObjectNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_reference_error", "Shipment", "Delivery"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.ShipmentRemote getShipmentRemote() {
        return (org.openxava.test.model.ShipmentRemote) getShipment();
    }

    /**
	 * @ejb:interface-method
	 */
    public void setShipment(org.openxava.test.model.IShipment newShipment) {
        this.modified = true;
        try {
            if (newShipment == null) setShipmentKey(null); else {
                if (newShipment instanceof org.openxava.test.model.Shipment) {
                    throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
                }
                org.openxava.test.model.ShipmentRemote remote = (org.openxava.test.model.ShipmentRemote) newShipment;
                setShipmentKey((org.openxava.test.model.ShipmentKey) remote.getPrimaryKey());
            }
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("set_reference_error", "Shipment", "Delivery"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.ShipmentKey getShipmentKey() {
        org.openxava.test.model.ShipmentKey key = new org.openxava.test.model.ShipmentKey();
        key._Type = get_Shipment_type();
        key.mode = getShipment_mode();
        key.number = getShipment_number();
        return key;
    }

    /**
	 * @ejb:interface-method
	 */
    public void setShipmentKey(org.openxava.test.model.ShipmentKey key) {
        this.modified = true;
        if (key == null) {
            key = new org.openxava.test.model.ShipmentKey();
            setShipment_type(key._Type);
            setShipment_mode(key.mode);
            setShipment_number(key.number);
        } else {
            set_Shipment_type(key._Type);
            setShipment_mode(key.mode);
            setShipment_number(key.number);
        }
    }

    /**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "SHIPMENT_TYPE"
	 */
    public abstract java.lang.String get_Shipment_type();

    public abstract void set_Shipment_type(java.lang.String newShipment_type);

    /**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public java.lang.String getShipment_type() {
        return get_Shipment_type();
    }

    public void setShipment_type(java.lang.String newShipment_type) {
        set_Shipment_type(newShipment_type);
    }

    /**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "SHIPMENT_MODE"
	 */
    public abstract java.lang.Integer get_Shipment_mode();

    public abstract void set_Shipment_mode(java.lang.Integer newShipment_mode);

    /**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public int getShipment_mode() {
        try {
            return ((Integer) shipment_modeConverter.toJava(get_Shipment_mode())).intValue();
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "shipment.mode", "Delivery", "int"));
        }
    }

    public void setShipment_mode(int newShipment_mode) {
        try {
            set_Shipment_mode((java.lang.Integer) shipment_modeConverter.toDB(new Integer(newShipment_mode)));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "shipment.mode", "Delivery", "int"));
        }
    }

    /**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "SHIPMENT_NUMBER"
	 */
    public abstract java.lang.Integer get_Shipment_number();

    public abstract void set_Shipment_number(java.lang.Integer newShipment_number);

    /**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public int getShipment_number() {
        try {
            return ((Integer) shipment_numberConverter.toJava(get_Shipment_number())).intValue();
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "shipment.number", "Delivery", "int"));
        }
    }

    public void setShipment_number(int newShipment_number) {
        try {
            set_Shipment_number((java.lang.Integer) shipment_numberConverter.toDB(new Integer(newShipment_number)));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "shipment.number", "Delivery", "int"));
        }
    }

    private org.openxava.test.model.ShipmentHome shipmentHome;

    private org.openxava.test.model.ShipmentHome getShipmentHome() throws Exception {
        if (shipmentHome == null) {
            shipmentHome = (org.openxava.test.model.ShipmentHome) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.model/Shipment"), org.openxava.test.model.ShipmentHome.class);
        }
        return shipmentHome;
    }

    private static final org.openxava.converters.IntegerNumberConverter shipment_numberConverter = new org.openxava.converters.IntegerNumberConverter();

    private static final org.openxava.converters.IntegerNumberConverter shipment_modeConverter = new org.openxava.converters.IntegerNumberConverter();

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.ICarrier getCarrier() {
        try {
            return getCarrierHome().findByPrimaryKey(getCarrierKey());
        } catch (ObjectNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_reference_error", "Carrier", "Delivery"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.CarrierRemote getCarrierRemote() {
        return (org.openxava.test.model.CarrierRemote) getCarrier();
    }

    /**
	 * @ejb:interface-method
	 */
    public void setCarrier(org.openxava.test.model.ICarrier newCarrier) {
        this.modified = true;
        try {
            if (newCarrier == null) setCarrierKey(null); else {
                if (newCarrier instanceof org.openxava.test.model.Carrier) {
                    throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
                }
                org.openxava.test.model.CarrierRemote remote = (org.openxava.test.model.CarrierRemote) newCarrier;
                setCarrierKey((org.openxava.test.model.CarrierKey) remote.getPrimaryKey());
            }
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("set_reference_error", "Carrier", "Delivery"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.CarrierKey getCarrierKey() {
        org.openxava.test.model.CarrierKey key = new org.openxava.test.model.CarrierKey();
        key._Number = get_Carrier_number();
        return key;
    }

    /**
	 * @ejb:interface-method
	 */
    public void setCarrierKey(org.openxava.test.model.CarrierKey key) {
        this.modified = true;
        if (key == null) {
            key = new org.openxava.test.model.CarrierKey();
            setCarrier_number(key._Number);
        } else {
            set_Carrier_number(key._Number);
        }
    }

    /**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "CARRIER"
	 */
    public abstract java.lang.Integer get_Carrier_number();

    public abstract void set_Carrier_number(java.lang.Integer newCarrier_number);

    /**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public java.lang.Integer getCarrier_number() {
        return get_Carrier_number();
    }

    public void setCarrier_number(java.lang.Integer newCarrier_number) {
        set_Carrier_number(newCarrier_number);
    }

    private org.openxava.test.model.CarrierHome carrierHome;

    private org.openxava.test.model.CarrierHome getCarrierHome() throws Exception {
        if (carrierHome == null) {
            carrierHome = (org.openxava.test.model.CarrierHome) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.model/Carrier"), org.openxava.test.model.CarrierHome.class);
        }
        return carrierHome;
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.IDeliveryType getType() {
        try {
            return getTypeHome().findByPrimaryKey(getTypeKey());
        } catch (ObjectNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_reference_error", "Type", "Delivery"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.DeliveryTypeRemote getTypeRemote() {
        return (org.openxava.test.model.DeliveryTypeRemote) getType();
    }

    /**
	 * 
	 */
    public void setType(org.openxava.test.model.IDeliveryType newType) {
        this.modified = true;
        try {
            if (newType == null) setTypeKey(null); else {
                if (newType instanceof org.openxava.test.model.DeliveryType) {
                    throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
                }
                org.openxava.test.model.DeliveryTypeRemote remote = (org.openxava.test.model.DeliveryTypeRemote) newType;
                setTypeKey((org.openxava.test.model.DeliveryTypeKey) remote.getPrimaryKey());
            }
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("set_reference_error", "Type", "Delivery"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.DeliveryTypeKey getTypeKey() {
        org.openxava.test.model.DeliveryTypeKey key = new org.openxava.test.model.DeliveryTypeKey();
        key.number = getType_number();
        return key;
    }

    /**
	 * 
	 */
    public void setTypeKey(org.openxava.test.model.DeliveryTypeKey key) {
        this.modified = true;
        if (key == null) {
            key = new org.openxava.test.model.DeliveryTypeKey();
            setType_number(key.number);
        } else {
            setType_number(key.number);
        }
    }

    /**		
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @jboss:column-name "TYPE"
	 */
    public abstract int get_Type_number();

    public abstract void set_Type_number(int newType_number);

    /**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public int getType_number() {
        return get_Type_number();
    }

    public void setType_number(int newType_number) {
        set_Type_number(newType_number);
    }

    private org.openxava.test.model.DeliveryTypeHome typeHome;

    private org.openxava.test.model.DeliveryTypeHome getTypeHome() throws Exception {
        if (typeHome == null) {
            typeHome = (org.openxava.test.model.DeliveryTypeHome) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.model/DeliveryType"), org.openxava.test.model.DeliveryTypeHome.class);
        }
        return typeHome;
    }

    private static MetaModel metaModel;

    public MetaModel getMetaModel() throws XavaException {
        if (metaModel == null) {
            metaModel = MetaComponent.get("Delivery").getMetaEntity();
        }
        return metaModel;
    }

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.model.DeliveryData getData();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setData(org.openxava.test.model.DeliveryData data);

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.model.DeliveryValue getDeliveryValue();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setDeliveryValue(org.openxava.test.model.DeliveryValue value);

    public void setEntityContext(javax.ejb.EntityContext ctx) {
        super.setEntityContext(ctx);
    }

    public void unsetEntityContext() {
        super.unsetEntityContext();
    }

    private void initMembers() {
        setNumber(0);
        setDate(null);
        setDescription(null);
        setDistance(0);
        setVehicle(null);
        setDriverType(null);
        setEmployee(null);
        setRemarks(null);
        setIncidents(null);
        setInvoiceKey(null);
        setShipmentKey(null);
        setCarrierKey(null);
        setTypeKey(null);
    }
}
