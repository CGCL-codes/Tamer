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
 * @ejb:bean name="Subfamily2" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.ejb/Subfamily2" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.ejb.ISubfamily2"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Subfamily2" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByFamily(int number)" query="SELECT OBJECT(o) FROM Subfamily2 o WHERE o.family_number = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByFamily(int number)" query="SELECT OBJECT(o) FROM Subfamily2 o WHERE o.family_number = ?1 " 
 * 
 * @jboss:table-name "XAVATEST_SUBFAMILY2"
 *
 * @author Javier Paniza
 */
public abstract class Subfamily2Bean extends EJBReplicableBase implements org.openxava.test.ejb.ISubfamily2, EntityBean {

    private boolean creating = false;

    private boolean modified = false;

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.ejb.Subfamily2Key ejbCreate(Map values) throws CreateException, ValidationException {
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
    public org.openxava.test.ejb.Subfamily2Key ejbCreate(org.openxava.test.ejb.Subfamily2Data data) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setData(data);
        setNumber(data.getNumber());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.ejb.Subfamily2Data data) throws CreateException, ValidationException {
    }

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.ejb.Subfamily2Key ejbCreate(org.openxava.test.ejb.Subfamily2Value value) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setSubfamily2Value(value);
        setNumber(value.getNumber());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.ejb.Subfamily2Value value) throws CreateException, ValidationException {
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

    private org.openxava.converters.NoConversionConverter remarksConverter;

    private org.openxava.converters.NoConversionConverter getRemarksConverter() {
        if (remarksConverter == null) {
            try {
                remarksConverter = (org.openxava.converters.NoConversionConverter) getMetaModel().getMapping().getConverter("remarks");
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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Remarks", "Subfamily2", "java.lang.String"));
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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Remarks", "Subfamily2", "java.lang.String"));
        }
    }

    private org.openxava.converters.TrimStringConverter descriptionConverter;

    private org.openxava.converters.TrimStringConverter getDescriptionConverter() {
        if (descriptionConverter == null) {
            try {
                descriptionConverter = (org.openxava.converters.TrimStringConverter) getMetaModel().getMapping().getConverter("description");
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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Subfamily2", "String"));
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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Subfamily2", "String"));
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
	 * @ejb:interface-method
	 */
    public org.openxava.test.ejb.Family2 getFamily() {
        try {
            return getFamilyHome().findByPrimaryKey(getFamilyKey());
        } catch (ObjectNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_reference_error", "Family", "Subfamily2"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setFamily(org.openxava.test.ejb.Family2 newFamily) {
        this.modified = true;
        try {
            if (newFamily == null) setFamilyKey(null); else setFamilyKey((org.openxava.test.ejb.Family2Key) newFamily.getPrimaryKey());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("set_reference_error", "Family", "Subfamily2"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.ejb.Family2Key getFamilyKey() {
        org.openxava.test.ejb.Family2Key key = new org.openxava.test.ejb.Family2Key();
        key.number = getFamily_number();
        return key;
    }

    /**
	 * @ejb:interface-method
	 */
    public void setFamilyKey(org.openxava.test.ejb.Family2Key key) {
        this.modified = true;
        if (key == null) {
            key = new org.openxava.test.ejb.Family2Key();
        }
        setFamily_number(key.number);
    }

    /**		
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * 
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @jboss:column-name "FAMILY"
	 */
    public abstract int getFamily_number();

    public abstract void setFamily_number(int newFamily_number);

    private org.openxava.test.ejb.Family2Home familyHome;

    private org.openxava.test.ejb.Family2Home getFamilyHome() throws Exception {
        if (familyHome == null) {
            familyHome = (org.openxava.test.ejb.Family2Home) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.ejb/Family2"), org.openxava.test.ejb.Family2Home.class);
        }
        return familyHome;
    }

    private MetaModel metaModel;

    private MetaModel getMetaModel() throws XavaException {
        if (metaModel == null) {
            metaModel = MetaComponent.get("Subfamily2").getMetaEntity();
        }
        return metaModel;
    }

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.ejb.Subfamily2Data getData();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setData(org.openxava.test.ejb.Subfamily2Data data);

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.ejb.Subfamily2Value getSubfamily2Value();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setSubfamily2Value(org.openxava.test.ejb.Subfamily2Value value);

    public void setEntityContext(javax.ejb.EntityContext ctx) {
        super.setEntityContext(ctx);
    }

    public void unsetEntityContext() {
        super.unsetEntityContext();
    }

    private void initMembers() {
        setNumber(0);
        setDescription(null);
        setRemarks(null);
    }
}
