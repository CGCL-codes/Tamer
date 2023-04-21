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
 * @ejb:bean name="Clerk" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/Clerk" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IClerk"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Clerk" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Clerk findByZoneNumberOfficeNumberNumber(int zoneNumber,int officeNumber,int number)" query="SELECT OBJECT(o) FROM Clerk o WHERE o.zoneNumber = ?1 and o.officeNumber = ?2 and o.number = ?3" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Clerk findByZoneNumberOfficeNumberNumber(int zoneNumber,int officeNumber,int number)" query="SELECT OBJECT(o) FROM Clerk o WHERE o.zoneNumber = ?1 and o.officeNumber = ?2 and o.number = ?3" 
 * 
 * @jboss:table-name "XAVATEST.CLERK"
 *
 * @author Javier Paniza
 */
public abstract class ClerkBean extends EJBReplicableBase implements org.openxava.test.model.IClerk, EntityBean {

    private boolean creating = false;

    private boolean modified = false;

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.ClerkKey ejbCreate(Map values) throws CreateException, ValidationException {
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
    public org.openxava.test.model.ClerkKey ejbCreate(org.openxava.test.model.ClerkData data) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setData(data);
        setZoneNumber(data.getZoneNumber());
        setOfficeNumber(data.getOfficeNumber());
        setNumber(data.getNumber());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.model.ClerkData data) throws CreateException, ValidationException {
    }

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.ClerkKey ejbCreate(org.openxava.test.model.ClerkValue value) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setClerkValue(value);
        setZoneNumber(value.getZoneNumber());
        setOfficeNumber(value.getOfficeNumber());
        setNumber(value.getNumber());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.model.ClerkValue value) throws CreateException, ValidationException {
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

    private static org.openxava.converters.IConverter arrivalTimeConverter;

    private org.openxava.converters.IConverter getArrivalTimeConverter() {
        if (arrivalTimeConverter == null) {
            try {
                arrivalTimeConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("arrivalTime");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "arrivalTime"));
            }
        }
        return arrivalTimeConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "ARRIVALTIME"
	 */
    public abstract java.sql.Time get_ArrivalTime();

    public abstract void set_ArrivalTime(java.sql.Time newArrivalTime);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.sql.Time getArrivalTime() {
        try {
            return (java.sql.Time) getArrivalTimeConverter().toJava(get_ArrivalTime());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "ArrivalTime", "Clerk", "java.sql.Time"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setArrivalTime(java.sql.Time newArrivalTime) {
        try {
            this.modified = true;
            set_ArrivalTime((java.sql.Time) getArrivalTimeConverter().toDB(newArrivalTime));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "ArrivalTime", "Clerk", "java.sql.Time"));
        }
    }

    private static org.openxava.converters.IConverter endingTimeConverter;

    private org.openxava.converters.IConverter getEndingTimeConverter() {
        if (endingTimeConverter == null) {
            try {
                endingTimeConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("endingTime");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "endingTime"));
            }
        }
        return endingTimeConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "ENDINGTIME"
	 */
    public abstract java.lang.String get_EndingTime();

    public abstract void set_EndingTime(java.lang.String newEndingTime);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public String getEndingTime() {
        try {
            return (String) getEndingTimeConverter().toJava(get_EndingTime());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "EndingTime", "Clerk", "String"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setEndingTime(String newEndingTime) {
        try {
            this.modified = true;
            set_EndingTime((java.lang.String) getEndingTimeConverter().toDB(newEndingTime));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "EndingTime", "Clerk", "String"));
        }
    }

    private static org.openxava.converters.IConverter nameConverter;

    private org.openxava.converters.IConverter getNameConverter() {
        if (nameConverter == null) {
            try {
                nameConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("name");
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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Name", "Clerk", "String"));
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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Name", "Clerk", "String"));
        }
    }

    /**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "OFFICE"
	 */
    public abstract int getOfficeNumber();

    /**
	  * 
	  */
    public abstract void setOfficeNumber(int newOfficeNumber);

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

    private static org.openxava.converters.IConverter commentsConverter;

    private org.openxava.converters.IConverter getCommentsConverter() {
        if (commentsConverter == null) {
            try {
                commentsConverter = (org.openxava.converters.IConverter) getMetaModel().getMapping().getConverter("comments");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "comments"));
            }
        }
        return commentsConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "COMMENTS"
	 */
    public abstract byte[] get_Comments();

    public abstract void set_Comments(byte[] newComments);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.lang.String getComments() {
        try {
            return (java.lang.String) getCommentsConverter().toJava(get_Comments());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Comments", "Clerk", "java.lang.String"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setComments(java.lang.String newComments) {
        try {
            this.modified = true;
            set_Comments((byte[]) getCommentsConverter().toDB(newComments));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Comments", "Clerk", "java.lang.String"));
        }
    }

    private static MetaModel metaModel;

    public MetaModel getMetaModel() throws XavaException {
        if (metaModel == null) {
            metaModel = MetaComponent.get("Clerk").getMetaEntity();
        }
        return metaModel;
    }

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.model.ClerkData getData();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setData(org.openxava.test.model.ClerkData data);

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.model.ClerkValue getClerkValue();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setClerkValue(org.openxava.test.model.ClerkValue value);

    public void setEntityContext(javax.ejb.EntityContext ctx) {
        super.setEntityContext(ctx);
    }

    public void unsetEntityContext() {
        super.unsetEntityContext();
    }

    private void initMembers() {
        setZoneNumber(0);
        setOfficeNumber(0);
        setNumber(0);
        setName(null);
        setArrivalTime(null);
        setEndingTime(null);
        setComments(null);
    }
}
