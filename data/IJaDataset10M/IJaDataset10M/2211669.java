package ispyb.server.data.ejb;

/**
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 *
 * @ejb.bean
 *    type="CMP"
 *    cmp-version="2.x"
 *    name="DewarLocationList"
 *    local-jndi-name="ispyb/DewarLocationListLocalHome"
 *    view-type="local"
 *    primkey-field="locationId"
 *
 * @ejb.value-object
 *    name="DewarLocationList"
 *    extends="ispyb.server.data.interfaces.DewarLocationListLightValue"
 *    match="*"
 *    instantiation="eager"
 *
 * @ejb.value-object
 *    name="DewarLocationListLight"
 *    match="light"
 *
 *
 * @ejb.finder
 *    signature="java.util.Collection findAll()"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT OBJECT(o) FROM DewarLocationList o"
 *    description="Find all entities."
 *
 * @ejb.finder
 *    signature="java.util.Collection findByLocationName(java.lang.String locationName)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM DewarLocationList o WHERE o.locationName = ?1"
 *    description="Finder for not indexed column locationName."
 *
 * @ejb.persistence table-name="DewarLocationList"
 * @ejb.transaction type="Required" 
 *
 *-----
 * 
 * @jboss.persistence datasource="${datasource.experimental.jndi.name}" datasource-mapping="${datasource.experimental.mapping}"
 *
 * @ejb.pk class = "java.lang.Integer"
 * 		generate = "false"
 * 
 * @jboss.unknown-pk 
 * 		class="java.lang.Integer" 
 * 		column-name="locationId"
 * 		jdbc-type="INTEGER"
 * 		sql-type="INTEGER"
 * 		auto-increment="true"
 * 
 * @jboss.entity-command name="${jboss.entity-command.name}" class="${jboss.entity-command.class}"  
 * 
 * @ejb.facade 
 * 		view-type="local" type="Stateless"
 * 
 * -----
 *
 */
public abstract class DewarLocationListBean implements javax.ejb.EntityBean {

    /**
    * Returns the locationId
    *
    * @return the locationId
    *
    * @ejb.pk-field
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="locationId"
    * @ejb.value-object match="light"
    */
    public abstract java.lang.Integer getLocationId();

    /**
    * Sets the locationId
    *
    * @param locationId the new locationId value
    */
    public abstract void setLocationId(java.lang.Integer locationId);

    /**
    * Returns the locationName
    *
    * @return the locationName
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="locationName"
    * @ejb.value-object match="light"
    */
    public abstract java.lang.String getLocationName();

    /**
    * Sets the locationName
    *
    * @param locationName the new locationName value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setLocationName(java.lang.String locationName);

    /**
    * This create method takes only mandatory (non-nullable) parameters.
    *
    * When the client invokes a create method, the EJB container invokes the ejbCreate method. 
    * Typically, an ejbCreate method in an entity bean performs the following tasks: 
    * <UL>
    * <LI>Inserts the entity state into the database.</LI>
    * <LI>Initializes the instance variables.</LI>
    * <LI>Returns the primary key.</LI>
    * </UL>
    *
    * @param locationId the locationId value
    * @param locationName the locationName value
    * @return the primary key of the new instance
    *
    * @ejb.create-method
    */
    public java.lang.Integer ejbCreate(java.lang.String locationName) throws javax.ejb.CreateException {
        setLocationName(locationName);
        return null;
    }

    /**
    * The container invokes this method immediately after it calls ejbCreate. 
    *
    * @param locationId the locationId value
    * @param locationName the locationName value
    */
    public void ejbPostCreate(java.lang.Integer locationId, java.lang.String locationName) throws javax.ejb.CreateException {
    }

    /**
    * Method required by new version of xdoclet? Might be a bug, this seems to be the quickest fix.
    */
    public void makeDirty() {
    }

    /**
    * Return the light value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract ispyb.server.data.interfaces.DewarLocationListLightValue getDewarLocationListLightValue();

    /**
    * Set the light value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract void setDewarLocationListLightValue(ispyb.server.data.interfaces.DewarLocationListLightValue value);

    /**
    * Return the value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract ispyb.server.data.interfaces.DewarLocationListValue getDewarLocationListValue();

    /**
    * Set the value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract void setDewarLocationListValue(ispyb.server.data.interfaces.DewarLocationListValue value);

    /**
    * Create and return a value object populated with the data from
    * this bean.
    *
    * Standard method that must be on all Beans for the TreeBuilder to
    * work its magic.
    *
    * @return Returns a value object containing the data within this bean.
    *
    * @ejb.interface-method view-type="local"
    */
    public ispyb.server.data.interfaces.DewarLocationListValue getValueObject() {
        ispyb.server.data.interfaces.DewarLocationListValue valueObject = new ispyb.server.data.interfaces.DewarLocationListValue();
        valueObject.setLocationId(getLocationId());
        valueObject.setLocationName(getLocationName());
        return valueObject;
    }

    /**
    * Creates an instance based on a value object
    *
    * When the client invokes a create method, the EJB container invokes the ejbCreate method. 
    * Typically, an ejbCreate method in an entity bean performs the following tasks: 
    * <UL>
    * <LI>Inserts the entity state into the database.</LI>
    * <LI>Initializes the instance variables.</LI>
    * <LI>Returns the primary key.</LI>
    * </UL>
    *
    * @param value the value object used to initialise the new instance
    * @return the primary key of the new instance
    *
    * @ejb.create-method
    */
    public java.lang.Integer ejbCreate(ispyb.server.data.interfaces.DewarLocationListLightValue value) throws javax.ejb.CreateException {
        setLocationName(value.getLocationName());
        return null;
    }

    /**
    * The container invokes this method immediately after it calls ejbCreate. 
    *
    * @param value the value object used to initialise the new instance
    */
    public void ejbPostCreate(ispyb.server.data.interfaces.DewarLocationListLightValue value) throws javax.ejb.CreateException {
    }
}
