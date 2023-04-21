package org.openxava.test.ejb.xejb;

import java.util.*;
import java.math.*;
import javax.ejb.*;
import javax.rmi.PortableRemoteObject;
import org.openxava.ejbx.*;
import org.openxava.util.*;
import org.openxava.component.*;
import org.openxava.model.meta.*;
import org.openxava.validators.ValidationException;
import org.openxava.test.ejb.*;

/**
 * @ejb:bean name="Office2" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.ejb/Office2" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.ejb.IOffice2"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Office2" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByOfficeManager(int officeNumber, int number, int zoneNumber)" query="SELECT OBJECT(o) FROM Office2 o WHERE o.number = ?1 AND o.officeManager_number = ?2 AND o.zoneNumber = ?3 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByOfficeManager(int officeNumber, int number, int zoneNumber)" query="SELECT OBJECT(o) FROM Office2 o WHERE o.number = ?1 AND o.officeManager_number = ?2 AND o.zoneNumber = ?3 " 	
 * @ejb:finder signature="Collection findByDefaultCarrier(java.lang.Integer number)" query="SELECT OBJECT(o) FROM Office2 o WHERE o.defaultCarrier_number = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByDefaultCarrier(java.lang.Integer number)" query="SELECT OBJECT(o) FROM Office2 o WHERE o.defaultCarrier_number = ?1 " 	
 * @ejb:finder signature="Collection findByMainWarehouse(java.lang.Integer number, int zoneNumber)" query="SELECT OBJECT(o) FROM Office2 o WHERE o.mainWarehouse_number = ?1 AND o.zoneNumber = ?2 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByMainWarehouse(java.lang.Integer number, int zoneNumber)" query="SELECT OBJECT(o) FROM Office2 o WHERE o.mainWarehouse_number = ?1 AND o.zoneNumber = ?2 " 
 * 
 * @jboss:table-name "XAVATEST_OFFICE2"
 *
 * @author Javier Paniza
 */
public abstract class Office2Bean extends EJBReplicableBase implements org.openxava.test.ejb.IOffice2, EntityBean {

    private boolean creating = false;

    private boolean modified = false;

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.ejb.Office2Key ejbCreate(Map values) throws CreateException, ValidationException {
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
    public org.openxava.test.ejb.Office2Key ejbCreate(org.openxava.test.ejb.Office2Data data) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setData(data);
        setZoneNumber(data.getZoneNumber());
        setNumber(data.getNumber());
        setMainWarehouse_number(data.getMainWarehouse_number());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.ejb.Office2Data data) throws CreateException, ValidationException {
    }

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.ejb.Office2Key ejbCreate(org.openxava.test.ejb.Office2Value value) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setOffice2Value(value);
        setZoneNumber(value.getZoneNumber());
        setNumber(value.getNumber());
        setMainWarehouse_number(value.getMainWarehouse_number());
        setMainWarehouse_zoneNumber(value.getMainWarehouse_zoneNumber());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.ejb.Office2Value value) throws CreateException, ValidationException {
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

    private org.openxava.converters.IntegerNumberConverter receptionistConverter;

    private org.openxava.converters.IntegerNumberConverter getReceptionistConverter() {
        if (receptionistConverter == null) {
            try {
                receptionistConverter = (org.openxava.converters.IntegerNumberConverter) getMetaModel().getMapping().getConverter("receptionist");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "receptionist"));
            }
        }
        return receptionistConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "RECEPTIONIST_OID"
	 */
    public abstract java.lang.Integer get_Receptionist();

    public abstract void set_Receptionist(java.lang.Integer newReceptionist);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public int getReceptionist() {
        try {
            return ((Integer) getReceptionistConverter().toJava(get_Receptionist())).intValue();
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Receptionist", "Office2", "int"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setReceptionist(int newReceptionist) {
        try {
            this.modified = true;
            set_Receptionist((java.lang.Integer) getReceptionistConverter().toDB(new Integer(newReceptionist)));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Receptionist", "Office2", "int"));
        }
    }

    private org.openxava.converters.TrimStringConverter nameConverter;

    private org.openxava.converters.TrimStringConverter getNameConverter() {
        if (nameConverter == null) {
            try {
                nameConverter = (org.openxava.converters.TrimStringConverter) getMetaModel().getMapping().getConverter("name");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "name"));
            }
        }
        return nameConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "NAME"
	 */
    public abstract java.lang.String get_Name();

    public abstract void set_Name(java.lang.String newName);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public String getName() {
        try {
            return (String) getNameConverter().toJava(get_Name());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Name", "Office2", "String"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setName(String newName) {
        try {
            this.modified = true;
            set_Name((java.lang.String) getNameConverter().toDB(newName));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Name", "Office2", "String"));
        }
    }

    /**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "ZONE"
	 */
    public abstract int getZoneNumber();

    /**
	  * 
	  */
    public abstract void setZoneNumber(int newZoneNumber);

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
	 * @ejb:interface-method
	 */
    public org.openxava.test.ejb.Clerk getOfficeManager() {
        try {
            return getOfficeManagerHome().findByPrimaryKey(getOfficeManagerKey());
        } catch (ObjectNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_reference_error", "OfficeManager", "Office2"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setOfficeManager(org.openxava.test.ejb.Clerk newOfficeManager) {
        this.modified = true;
        try {
            if (newOfficeManager == null) setOfficeManagerKey(null); else setOfficeManagerKey((org.openxava.test.ejb.ClerkKey) newOfficeManager.getPrimaryKey());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("set_reference_error", "OfficeManager", "Office2"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.ejb.ClerkKey getOfficeManagerKey() {
        org.openxava.test.ejb.ClerkKey key = new org.openxava.test.ejb.ClerkKey();
        key.officeNumber = getOfficeManager_officeNumber();
        key.number = getOfficeManager_number();
        key.zoneNumber = getOfficeManager_zoneNumber();
        return key;
    }

    /**
	 * @ejb:interface-method
	 */
    public void setOfficeManagerKey(org.openxava.test.ejb.ClerkKey key) {
        this.modified = true;
        if (key == null) {
            key = new org.openxava.test.ejb.ClerkKey();
        }
        setOfficeManager_officeNumber(key.officeNumber);
        setOfficeManager_number(key.number);
        setOfficeManager_zoneNumber(key.zoneNumber);
    }

    /**		
	 * @ejb:interface-method
	 *
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public int getOfficeManager_zoneNumber() {
        return getZoneNumber();
    }

    public void setOfficeManager_zoneNumber(int OfficeManager_zoneNumber) {
    }

    /**		
	 * @ejb:interface-method
	 *
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public int getOfficeManager_officeNumber() {
        return getNumber();
    }

    public void setOfficeManager_officeNumber(int OfficeManager_officeNumber) {
    }

    /**		
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * 
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @jboss:column-name "MANAGER_NUMBER"
	 */
    public abstract int getOfficeManager_number();

    public abstract void setOfficeManager_number(int newOfficeManager_number);

    private org.openxava.test.ejb.ClerkHome officeManagerHome;

    private org.openxava.test.ejb.ClerkHome getOfficeManagerHome() throws Exception {
        if (officeManagerHome == null) {
            officeManagerHome = (org.openxava.test.ejb.ClerkHome) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.ejb/Clerk"), org.openxava.test.ejb.ClerkHome.class);
        }
        return officeManagerHome;
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.ejb.Carrier getDefaultCarrier() {
        try {
            return getDefaultCarrierHome().findByPrimaryKey(getDefaultCarrierKey());
        } catch (ObjectNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_reference_error", "DefaultCarrier", "Office2"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setDefaultCarrier(org.openxava.test.ejb.Carrier newDefaultCarrier) {
        this.modified = true;
        try {
            if (newDefaultCarrier == null) setDefaultCarrierKey(null); else setDefaultCarrierKey((org.openxava.test.ejb.CarrierKey) newDefaultCarrier.getPrimaryKey());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("set_reference_error", "DefaultCarrier", "Office2"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.ejb.CarrierKey getDefaultCarrierKey() {
        org.openxava.test.ejb.CarrierKey key = new org.openxava.test.ejb.CarrierKey();
        key._Number = getDefaultCarrier_number();
        return key;
    }

    /**
	 * @ejb:interface-method
	 */
    public void setDefaultCarrierKey(org.openxava.test.ejb.CarrierKey key) {
        this.modified = true;
        if (key == null) {
            key = new org.openxava.test.ejb.CarrierKey();
        }
        setDefaultCarrier_number(key._Number);
    }

    /**		
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * 
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @jboss:column-name "CARRIER_NUMBER"
	 */
    public abstract java.lang.Integer getDefaultCarrier_number();

    public abstract void setDefaultCarrier_number(java.lang.Integer newDefaultCarrier_number);

    private org.openxava.test.ejb.CarrierHome defaultCarrierHome;

    private org.openxava.test.ejb.CarrierHome getDefaultCarrierHome() throws Exception {
        if (defaultCarrierHome == null) {
            defaultCarrierHome = (org.openxava.test.ejb.CarrierHome) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.ejb/Carrier"), org.openxava.test.ejb.CarrierHome.class);
        }
        return defaultCarrierHome;
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.ejb.Warehouse getMainWarehouse() {
        try {
            return getMainWarehouseHome().findByPrimaryKey(getMainWarehouseKey());
        } catch (ObjectNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_reference_error", "MainWarehouse", "Office2"));
        }
    }

    /**
	 * 
	 */
    public void setMainWarehouse(org.openxava.test.ejb.Warehouse newMainWarehouse) {
        this.modified = true;
        try {
            if (newMainWarehouse == null) setMainWarehouseKey(null); else setMainWarehouseKey((org.openxava.test.ejb.WarehouseKey) newMainWarehouse.getPrimaryKey());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("set_reference_error", "MainWarehouse", "Office2"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.ejb.WarehouseKey getMainWarehouseKey() {
        org.openxava.test.ejb.WarehouseKey key = new org.openxava.test.ejb.WarehouseKey();
        key._Number = getMainWarehouse_number();
        key.zoneNumber = getMainWarehouse_zoneNumber();
        return key;
    }

    /**
	 * 
	 */
    public void setMainWarehouseKey(org.openxava.test.ejb.WarehouseKey key) {
        this.modified = true;
        if (key == null) {
            key = new org.openxava.test.ejb.WarehouseKey();
        }
        setMainWarehouse_number(key._Number);
        setMainWarehouse_zoneNumber(key.zoneNumber);
    }

    /**		
	 * @ejb:interface-method
	 *
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public int getMainWarehouse_zoneNumber() {
        return getZoneNumber();
    }

    public void setMainWarehouse_zoneNumber(int MainWarehouse_zoneNumber) {
    }

    /**		
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @jboss:column-name "WAREHOUSE_NUMBER"
	 */
    public abstract java.lang.Integer getMainWarehouse_number();

    public abstract void setMainWarehouse_number(java.lang.Integer newMainWarehouse_number);

    private org.openxava.test.ejb.WarehouseHome mainWarehouseHome;

    private org.openxava.test.ejb.WarehouseHome getMainWarehouseHome() throws Exception {
        if (mainWarehouseHome == null) {
            mainWarehouseHome = (org.openxava.test.ejb.WarehouseHome) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.ejb/Warehouse"), org.openxava.test.ejb.WarehouseHome.class);
        }
        return mainWarehouseHome;
    }

    private MetaModel metaModel;

    private MetaModel getMetaModel() throws XavaException {
        if (metaModel == null) {
            metaModel = MetaComponent.get("Office2").getMetaEntity();
        }
        return metaModel;
    }

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.ejb.Office2Data getData();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setData(org.openxava.test.ejb.Office2Data data);

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.ejb.Office2Value getOffice2Value();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setOffice2Value(org.openxava.test.ejb.Office2Value value);

    public void setEntityContext(javax.ejb.EntityContext ctx) {
        super.setEntityContext(ctx);
    }

    public void unsetEntityContext() {
        super.unsetEntityContext();
    }

    private void initMembers() {
        setNumber(0);
        setZoneNumber(0);
        setName(null);
        setReceptionist(0);
    }
}
