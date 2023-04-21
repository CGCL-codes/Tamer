package test.ejb.cmr;

/**
    * This class is part of Middlegen airlines, and it is a CMP EJB accessing the country table.
    *
    * @author <a href="http://middlegen.codehaus.org/">Middlegen</a>
    * @todo generate create methods which don't take pk as arg (and use an arbitrary pk generator internally)
    * @ejb.bean
    *    cmp-version="2.x"
    *    local-jndi-name="airline.CountryLocalHome"
    *    name="Country"
    *    primkey-field="countryId"
    *    type="CMP"
    *    view-type="local"
    * @ejb.finder
    *    method-intf="LocalHome"
    *    query="SELECT OBJECT(o) FROM Country o"
    *    result-type-mapping="Local"
    *    signature="java.util.Collection findAll()"
    * @ejb.finder
    *    method-intf="LocalHome"
    *    query="SELECT DISTINCT OBJECT(o) FROM Country o WHERE o.country = ?1"
    *    result-type-mapping="Local"
    *    signature="java.util.Collection findByCountry(test.ejb.cmr.CountryLocal country)"
    * @ejb.finder
    *    method-intf="LocalHome"
    *    query="SELECT DISTINCT OBJECT(o) FROM Country o WHERE o.name = ?1"
    *    result-type-mapping="Local"
    *    signature="java.util.Collection findByName(java.lang.String name)"
    * @ejb.persistence
    * @weblogic.data-source-name airline.database
    * @XXweblogic.data-source-name $table.datasourceName
    * @XXweblogic.resource-description
    * @orion.bean
    *    data-source="airline.database"
    *
    * @ejb:util generate="physical"
    */
public abstract class CountryBean implements javax.ejb.EntityBean {

    /**
    * Context set by container
    */
    private javax.ejb.EntityContext _entityContext;

    /**
    * Returns the countryId
    *
    * @todo support OracleClob,OracleBlob on WLS
    * @return the countryId
    * @ejb.persistent-field
    * @ejb.persistence
    *    column-name="country_id"
    * @ejb.interface-method
    *    view-type="local"
    * @ejb.pk-field
    */
    public abstract java.lang.Integer getCountryId();

    /**
    * Sets the countryId
    *
    * @param java.lang.Integer the new countryId value
    */
    public abstract void setCountryId(java.lang.Integer countryId);

    /**
    * Returns the ownerIdFk
    *
    * @todo support OracleClob,OracleBlob on WLS
    * @return the ownerIdFk
    * @ejb.persistent-field
    * @ejb.persistence
    *    column-name="owner_id_fk"
    */
    public abstract java.lang.Integer getOwnerIdFk();

    /**
    * Sets the ownerIdFk
    *
    * @param java.lang.Integer the new ownerIdFk value
    */
    public abstract void setOwnerIdFk(java.lang.Integer ownerIdFk);

    /**
    * Returns the name
    *
    * @todo support OracleClob,OracleBlob on WLS
    * @return the name
    * @ejb.persistent-field
    * @ejb.persistence
    *    column-name="name"
    * @ejb.interface-method
    *    view-type="local"
    */
    public abstract java.lang.String getName();

    /**
    * Sets the name
    *
    * @param java.lang.String the new name value
    * @ejb.interface-method
    *    view-type="local"
    */
    public abstract void setName(java.lang.String name);

    /**
    * Returns a collection of related test.ejb.cmr.CityLocal
    *
    * @return a collection of related test.ejb.cmr.CityLocal
    * @ejb.interface-method
    *    view-type="local"
    * @ejb.relation
    *    name="city-country"
    *    role-name="country-has-city"
    *    target-ejb="City"
    *    target-multiple="no"
    *    target-role-name="city-has-country"
    * @weblogic.target-column-map
    *    foreign-key-column="country_id_fk"
	* @jboss:target-relation
	*    related-pk-field="countryId"
	*    fk-column="country_id_fk"
    */
    public abstract java.util.Collection getCities();

    /**
    * Sets a collection of related test.ejb.cmr.CityLocal
    *
    * @param a collection of related test.ejb.cmr.CityLocal
    * @ejb.interface-method
    *    view-type="local"
    * @param cities the new CMR value
    */
    public abstract void setCities(java.util.Collection cities);

    /**
    * Returns a collection of related test.ejb.cmr.CountryLocal
    *
    * @return a collection of related test.ejb.cmr.CountryLocal
    * @ejb.interface-method
    *    view-type="local"
    * @ejb.relation
    *    name="country-country"
    *    role-name="country-has-country1"
    * @weblogic.target-column-map
    *    foreign-key-column="owner_id_fk"
    * @jboss Unidirectional 1->N relationship unsupported by XDoclet
    */
    public abstract java.util.Collection getCountries();

    /**
    * Sets a collection of related test.ejb.cmr.CountryLocal
    *
    * @param a collection of related test.ejb.cmr.CountryLocal
    * @ejb.interface-method
    *    view-type="local"
    * @param countries the new CMR value
    */
    public abstract void setCountries(java.util.Collection countries);

    /**
    * Returns the related test.ejb.cmr.CountryLocal
    *
    * @return the related test.ejb.cmr.CountryLocal
    * @ejb.interface-method
    *    view-type="local"
    * @ejb.relation
    *    name="country-country"
    *    role-name="country-has-country2"
    * @weblogic.column-map
    *    foreign-key-column="owner_id_fk"
    * @jboss.relation
    *    fk-column="owner_id_fk"
    *    related-pk-field="countryId"
    */
    public abstract test.ejb.cmr.CountryLocal getCountry();

    /**
    * Sets the related test.ejb.cmr.CountryLocal
    *
    * @param test.ejb.cmr.CountryLocal the related country
    * @ejb.interface-method
    *    view-type="local"
    * @param country the new CMR value
    */
    public abstract void setCountry(test.ejb.cmr.CountryLocal country);

    /**
    * This create method takes only mandatory (non-nullable) parameters. The pk columns must be provided. When the client invokes a create method, the EJB container invokes the ejbCreate method. Typically, an ejbCreate method in an entity bean performs the following tasks: <UL> <LI>Inserts the entity state into the database.</LI> <LI>Initializes the instance variables.</LI> <LI>Returns the primary key.</LI> </UL>
    *
    * @param countryId the countryId value
    * @param name the name value
    * @param country mandatory CMR field
    * @return the primary key of the new instance
    * @ejb.create-method
    */
    public java.lang.Integer ejbCreate(java.lang.Integer countryId, java.lang.String name, test.ejb.cmr.CountryLocal country) throws javax.ejb.CreateException {
        setCountryId(countryId);
        setName(name);
        return null;
    }

    /**
    * The container invokes thos method immediately after it calls ejbCreate.
    *
    * @param countryId the countryId value
    * @param name the name value
    * @param country mandatory CMR field
    */
    public void ejbPostCreate(java.lang.Integer countryId, java.lang.String name, test.ejb.cmr.CountryLocal country) throws javax.ejb.CreateException {
        setCountry(country);
    }

    /**
    * The container invokes setEntityContext just once - when it creates the bean instance.
    *
    */
    public void setEntityContext(javax.ejb.EntityContext entityContext) {
        _entityContext = entityContext;
    }

    /**
    * At the end of the life cycle, the container removes the instance from the pool and invokes this method.
    *
    */
    public void unsetEntityContext() {
        _entityContext = null;
    }

    /**
    * The container invokes this method to instruct the instance to synchronize its state by loading it state from the underlying database.
    *
    */
    public void ejbLoad() {
    }

    /**
    * The container invokes this method when the instance is taken out of the pool of available instances to become associated with a specific EJB object.
    *
    */
    public void ejbActivate() {
    }

    /**
    * The container invokes this method on an instance before the instance becomes disassociated with a specific EJB object.
    *
    */
    public void ejbPassivate() {
    }

    /**
    * The container invokes this method before it removes the EJB object that is currently associated with the instance.
    *
    */
    public void ejbRemove() throws javax.ejb.RemoveException {
    }

    /**
    * The container invokes this method to instruct the instance to synchronize its state by storing it to the underlying database.
    *
    */
    public void ejbStore() {
    }
}
