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
 * @ejb:bean name="FilterBySubfamily" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/FilterBySubfamily" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IFilterBySubfamily"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="FilterBySubfamily" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findBySubfamilyTo(int number)" query="SELECT OBJECT(o) FROM FilterBySubfamily o WHERE o._SubfamilyTo_number = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findBySubfamilyTo(int number)" query="SELECT OBJECT(o) FROM FilterBySubfamily o WHERE o._SubfamilyTo_number = ?1 " 	
 * @ejb:finder signature="Collection findBySubfamily(int number)" query="SELECT OBJECT(o) FROM FilterBySubfamily o WHERE o._Subfamily_number = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findBySubfamily(int number)" query="SELECT OBJECT(o) FROM FilterBySubfamily o WHERE o._Subfamily_number = ?1 " 	
 * @ejb:finder signature="FilterBySubfamily findBy()" query="SELECT OBJECT(o) FROM FilterBySubfamily o" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="FilterBySubfamily findBy()" query="SELECT OBJECT(o) FROM FilterBySubfamily o" 
 * 
 * @jboss:table-name "FilterBySubfamily"
 *
 * @author Javier Paniza
 */
public abstract class FilterBySubfamilyBean extends EJBReplicableBase implements org.openxava.test.model.IFilterBySubfamily, EntityBean {

    private boolean creating = false;

    private boolean modified = false;

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.FilterBySubfamilyKey ejbCreate(Map values) throws CreateException, ValidationException {
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
    public org.openxava.test.model.FilterBySubfamilyKey ejbCreate(org.openxava.test.model.FilterBySubfamilyData data) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setData(data);
        return null;
    }

    public void ejbPostCreate(org.openxava.test.model.FilterBySubfamilyData data) throws CreateException, ValidationException {
    }

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.model.FilterBySubfamilyKey ejbCreate(org.openxava.test.model.FilterBySubfamilyValue value) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setFilterBySubfamilyValue(value);
        return null;
    }

    public void ejbPostCreate(org.openxava.test.model.FilterBySubfamilyValue value) throws CreateException, ValidationException {
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

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.ISubfamily2 getSubfamilyTo() {
        try {
            return getSubfamilyToHome().findByPrimaryKey(getSubfamilyToKey());
        } catch (ObjectNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_reference_error", "SubfamilyTo", "FilterBySubfamily"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.Subfamily2Remote getSubfamilyToRemote() {
        return (org.openxava.test.model.Subfamily2Remote) getSubfamilyTo();
    }

    /**
	 * @ejb:interface-method
	 */
    public void setSubfamilyTo(org.openxava.test.model.ISubfamily2 newSubfamilyTo) {
        this.modified = true;
        try {
            if (newSubfamilyTo == null) setSubfamilyToKey(null); else {
                if (newSubfamilyTo instanceof org.openxava.test.model.Subfamily2) {
                    throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
                }
                org.openxava.test.model.Subfamily2Remote remote = (org.openxava.test.model.Subfamily2Remote) newSubfamilyTo;
                setSubfamilyToKey((org.openxava.test.model.Subfamily2Key) remote.getPrimaryKey());
            }
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("set_reference_error", "SubfamilyTo", "FilterBySubfamily"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.Subfamily2Key getSubfamilyToKey() {
        org.openxava.test.model.Subfamily2Key key = new org.openxava.test.model.Subfamily2Key();
        key.number = getSubfamilyTo_number();
        return key;
    }

    /**
	 * @ejb:interface-method
	 */
    public void setSubfamilyToKey(org.openxava.test.model.Subfamily2Key key) {
        this.modified = true;
        if (key == null) {
            key = new org.openxava.test.model.Subfamily2Key();
            setSubfamilyTo_number(key.number);
        } else {
            setSubfamilyTo_number(key.number);
        }
    }

    /**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "subfamilyTo_number"
	 */
    public abstract int get_SubfamilyTo_number();

    public abstract void set_SubfamilyTo_number(int newSubfamilyTo_number);

    /**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public int getSubfamilyTo_number() {
        return get_SubfamilyTo_number();
    }

    public void setSubfamilyTo_number(int newSubfamilyTo_number) {
        set_SubfamilyTo_number(newSubfamilyTo_number);
    }

    private org.openxava.test.model.Subfamily2Home subfamilyToHome;

    private org.openxava.test.model.Subfamily2Home getSubfamilyToHome() throws Exception {
        if (subfamilyToHome == null) {
            subfamilyToHome = (org.openxava.test.model.Subfamily2Home) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.model/Subfamily2"), org.openxava.test.model.Subfamily2Home.class);
        }
        return subfamilyToHome;
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.ISubfamily2 getSubfamily() {
        try {
            return getSubfamilyHome().findByPrimaryKey(getSubfamilyKey());
        } catch (ObjectNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_reference_error", "Subfamily", "FilterBySubfamily"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.Subfamily2Remote getSubfamilyRemote() {
        return (org.openxava.test.model.Subfamily2Remote) getSubfamily();
    }

    /**
	 * @ejb:interface-method
	 */
    public void setSubfamily(org.openxava.test.model.ISubfamily2 newSubfamily) {
        this.modified = true;
        try {
            if (newSubfamily == null) setSubfamilyKey(null); else {
                if (newSubfamily instanceof org.openxava.test.model.Subfamily2) {
                    throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
                }
                org.openxava.test.model.Subfamily2Remote remote = (org.openxava.test.model.Subfamily2Remote) newSubfamily;
                setSubfamilyKey((org.openxava.test.model.Subfamily2Key) remote.getPrimaryKey());
            }
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("set_reference_error", "Subfamily", "FilterBySubfamily"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.model.Subfamily2Key getSubfamilyKey() {
        org.openxava.test.model.Subfamily2Key key = new org.openxava.test.model.Subfamily2Key();
        key.number = getSubfamily_number();
        return key;
    }

    /**
	 * @ejb:interface-method
	 */
    public void setSubfamilyKey(org.openxava.test.model.Subfamily2Key key) {
        this.modified = true;
        if (key == null) {
            key = new org.openxava.test.model.Subfamily2Key();
            setSubfamily_number(key.number);
        } else {
            setSubfamily_number(key.number);
        }
    }

    /**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "subfamily_number"
	 */
    public abstract int get_Subfamily_number();

    public abstract void set_Subfamily_number(int newSubfamily_number);

    /**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
    public int getSubfamily_number() {
        return get_Subfamily_number();
    }

    public void setSubfamily_number(int newSubfamily_number) {
        set_Subfamily_number(newSubfamily_number);
    }

    private org.openxava.test.model.Subfamily2Home subfamilyHome;

    private org.openxava.test.model.Subfamily2Home getSubfamilyHome() throws Exception {
        if (subfamilyHome == null) {
            subfamilyHome = (org.openxava.test.model.Subfamily2Home) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.model/Subfamily2"), org.openxava.test.model.Subfamily2Home.class);
        }
        return subfamilyHome;
    }

    private static MetaModel metaModel;

    public MetaModel getMetaModel() throws XavaException {
        if (metaModel == null) {
            metaModel = MetaComponent.get("FilterBySubfamily").getMetaEntity();
        }
        return metaModel;
    }

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.model.FilterBySubfamilyData getData();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setData(org.openxava.test.model.FilterBySubfamilyData data);

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.model.FilterBySubfamilyValue getFilterBySubfamilyValue();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setFilterBySubfamilyValue(org.openxava.test.model.FilterBySubfamilyValue value);

    public void setEntityContext(javax.ejb.EntityContext ctx) {
        super.setEntityContext(ctx);
    }

    public void unsetEntityContext() {
        super.unsetEntityContext();
    }

    private void initMembers() {
        setSubfamilyToKey(null);
        setSubfamilyKey(null);
    }
}
