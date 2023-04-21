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
 * @ejb:bean name="Subfamily" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/Subfamily" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.ISubfamily"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Subfamily" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Subfamily findByOid(java.lang.String oid)" query="SELECT OBJECT(o) FROM Subfamily o WHERE o.oid = ?1" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Subfamily findByOid(java.lang.String oid)" query="SELECT OBJECT(o) FROM Subfamily o WHERE o.oid = ?1" 
 * 
 * @jboss:table-name "XAVATEST.SUBFAMILY"
 *
 * @author Javier Paniza
 */
public abstract class SubfamilyBean extends EJBReplicableBase implements org.openxava.test.model.ISubfamily, EntityBean {

    private boolean creating = false;

    private boolean modified = false;

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.SubfamilyKey ejbCreate(Map values) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        executeSets(values);
        try {
            org.openxava.calculators.UUIDCalculator oidCalculator = (org.openxava.calculators.UUIDCalculator) getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().createCalculator();
            oidCalculator.setModel(this);
            setOid((String) oidCalculator.calculate());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("entity_create_error", "Subfamily", ex.getLocalizedMessage()));
        }
        return null;
    }

    public void ejbPostCreate(Map values) throws CreateException, ValidationException {
    }

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.SubfamilyKey ejbCreate(org.openxava.test.model.SubfamilyData data) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setData(data);
        setOid(data.getOid());
        try {
            org.openxava.calculators.UUIDCalculator oidCalculator = (org.openxava.calculators.UUIDCalculator) getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().createCalculator();
            oidCalculator.setModel(this);
            setOid((String) oidCalculator.calculate());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("entity_create_error", "Subfamily", ex.getLocalizedMessage()));
        }
        return null;
    }

    public void ejbPostCreate(org.openxava.test.model.SubfamilyData data) throws CreateException, ValidationException {
    }

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.SubfamilyKey ejbCreate(org.openxava.test.model.SubfamilyValue value) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setSubfamilyValue(value);
        setOid(value.getOid());
        try {
            org.openxava.calculators.UUIDCalculator oidCalculator = (org.openxava.calculators.UUIDCalculator) getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().createCalculator();
            oidCalculator.setModel(this);
            setOid((String) oidCalculator.calculate());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("entity_create_error", "Subfamily", ex.getLocalizedMessage()));
        }
        return null;
    }

    public void ejbPostCreate(org.openxava.test.model.SubfamilyValue value) throws CreateException, ValidationException {
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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Subfamily", "String"));
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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Subfamily", "String"));
        }
    }

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public String getFamily() {
        boolean cmtActivated = false;
        if (!org.openxava.hibernate.XHibernate.isCmt()) {
            org.openxava.hibernate.XHibernate.setCmt(true);
            cmtActivated = true;
        }
        try {
            org.openxava.calculators.EmptyStringCalculator familyCalculator = (org.openxava.calculators.EmptyStringCalculator) getMetaModel().getMetaProperty("family").getMetaCalculator().createCalculator();
            return (String) familyCalculator.calculate();
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "Family", "Subfamily", ex.getLocalizedMessage()));
        } finally {
            if (cmtActivated) {
                org.openxava.hibernate.XHibernate.setCmt(false);
            }
        }
    }

    public void setFamily(String newFamily) {
    }

    /**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "OID"
	 */
    public abstract String getOid();

    /**
	  * 
	  */
    public abstract void setOid(String newOid);

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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Remarks", "Subfamily", "java.lang.String"));
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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Remarks", "Subfamily", "java.lang.String"));
        }
    }

    private static org.openxava.converters.IConverter numberConverter;

    private org.openxava.converters.IConverter getNumberConverter() {
        if (numberConverter == null) {
            try {
                numberConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("number");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "number"));
            }
        }
        return numberConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "NUMBER"
	 */
    public abstract java.lang.Integer get_Number();

    public abstract void set_Number(java.lang.Integer newNumber);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public int getNumber() {
        try {
            return ((Integer) getNumberConverter().toJava(get_Number())).intValue();
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Number", "Subfamily", "int"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setNumber(int newNumber) {
        try {
            this.modified = true;
            set_Number((java.lang.Integer) getNumberConverter().toDB(new Integer(newNumber)));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Number", "Subfamily", "int"));
        }
    }

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public String getRemarksDB() {
        boolean cmtActivated = false;
        if (!org.openxava.hibernate.XHibernate.isCmt()) {
            org.openxava.hibernate.XHibernate.setCmt(true);
            cmtActivated = true;
        }
        try {
            org.openxava.test.calculators.SubfamilyPureRemarksCalculator remarksDBCalculator = (org.openxava.test.calculators.SubfamilyPureRemarksCalculator) getMetaModel().getMetaProperty("remarksDB").getMetaCalculator().createCalculator();
            remarksDBCalculator.setModel(this);
            return (String) remarksDBCalculator.calculate();
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "RemarksDB", "Subfamily", ex.getLocalizedMessage()));
        } finally {
            if (cmtActivated) {
                org.openxava.hibernate.XHibernate.setCmt(false);
            }
        }
    }

    public void setRemarksDB(String newRemarksDB) {
    }

    private static org.openxava.converters.IConverter familyNumberConverter;

    private org.openxava.converters.IConverter getFamilyNumberConverter() {
        if (familyNumberConverter == null) {
            try {
                familyNumberConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("familyNumber");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "familyNumber"));
            }
        }
        return familyNumberConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "FAMILY"
	 */
    public abstract java.lang.Integer get_FamilyNumber();

    public abstract void set_FamilyNumber(java.lang.Integer newFamilyNumber);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public int getFamilyNumber() {
        try {
            return ((Integer) getFamilyNumberConverter().toJava(get_FamilyNumber())).intValue();
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "FamilyNumber", "Subfamily", "int"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setFamilyNumber(int newFamilyNumber) {
        try {
            this.modified = true;
            set_FamilyNumber((java.lang.Integer) getFamilyNumberConverter().toDB(new Integer(newFamilyNumber)));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "FamilyNumber", "Subfamily", "int"));
        }
    }

    private static MetaModel metaModel;

    public MetaModel getMetaModel() throws XavaException {
        if (metaModel == null) {
            metaModel = MetaComponent.get("Subfamily").getMetaEntity();
        }
        return metaModel;
    }

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.model.SubfamilyData getData();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setData(org.openxava.test.model.SubfamilyData data);

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.model.SubfamilyValue getSubfamilyValue();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setSubfamilyValue(org.openxava.test.model.SubfamilyValue value);

    public void setEntityContext(javax.ejb.EntityContext ctx) {
        super.setEntityContext(ctx);
    }

    public void unsetEntityContext() {
        super.unsetEntityContext();
    }

    private void initMembers() {
        setOid(null);
        setNumber(0);
        setFamilyNumber(0);
        setDescription(null);
        setRemarks(null);
    }
}
