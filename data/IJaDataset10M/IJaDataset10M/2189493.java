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
 * @ejb:bean name="Family2" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/Family2" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IFamily2"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Family2" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Family2 findByNumber(int number)" query="SELECT OBJECT(o) FROM Family2 o WHERE o.number = ?1" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Family2 findByNumber(int number)" query="SELECT OBJECT(o) FROM Family2 o WHERE o.number = ?1" 
 * 
 * @jboss:table-name "XAVATEST.FAMILY2"
 *
 * @author Javier Paniza
 */
public abstract class Family2Bean extends EJBReplicableBase implements org.openxava.test.model.IFamily2, EntityBean {

    private boolean creating = false;

    private boolean modified = false;

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.Family2Key ejbCreate(Map values) throws CreateException, ValidationException {
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
    public org.openxava.test.model.Family2Key ejbCreate(org.openxava.test.model.Family2Data data) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setData(data);
        setNumber(data.getNumber());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.model.Family2Data data) throws CreateException, ValidationException {
    }

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.Family2Key ejbCreate(org.openxava.test.model.Family2Value value) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setFamily2Value(value);
        setNumber(value.getNumber());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.model.Family2Value value) throws CreateException, ValidationException {
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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Family2", "String"));
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
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Family2", "String"));
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

    private static MetaModel metaModel;

    public MetaModel getMetaModel() throws XavaException {
        if (metaModel == null) {
            metaModel = MetaComponent.get("Family2").getMetaEntity();
        }
        return metaModel;
    }

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.model.Family2Data getData();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setData(org.openxava.test.model.Family2Data data);

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.model.Family2Value getFamily2Value();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setFamily2Value(org.openxava.test.model.Family2Value value);

    public void setEntityContext(javax.ejb.EntityContext ctx) {
        super.setEntityContext(ctx);
    }

    public void unsetEntityContext() {
        super.unsetEntityContext();
    }

    private void initMembers() {
        setNumber(0);
        setDescription(null);
    }
}
