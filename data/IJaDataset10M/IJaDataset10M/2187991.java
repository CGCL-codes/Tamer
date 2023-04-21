package ispyb.server.data.ejb;

/**
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 *
 * @ejb.bean
 *    type="CMP"
 *    cmp-version="2.x"
 *    name="Proposal"
 *    local-jndi-name="ispyb/ProposalLocalHome"
 *    view-type="local"
 *    primkey-field="proposalId"
 *
 * @ejb.value-object
 *    name="Proposal"
 *    extends="ispyb.server.data.interfaces.ProposalLightValue"
 *	   match="*"
 *	   instantiation="eager"
 *
 * @ejb.value-object
 *    name="ProposalLight"
 *	   match="light"
 *
 * @ejb.finder
 *    signature="java.util.Collection findAll()"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT OBJECT(o) FROM Proposal o ${ejb.proposal.findAll.filter}"
 *    
 *
 * @ejb.finder
 *    signature="java.util.Collection findByTitle(java.lang.String title)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Proposal o WHERE o.title = ?1"
 *    description="title is not indexed."
 * 
 * @ejb.finder
 *    signature="java.util.Collection findByCode(java.lang.String code)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Proposal o WHERE o.code = ?1"
 *    description="code is not indexed."
 * 
 * @ejb.finder
 *    signature="java.util.Collection findByNumber(java.lang.Integer number)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Proposal o WHERE o.number = ?1"
 *    description="number is not indexed."
 * 
 *  @ejb.finder
 *    signature="java.util.Collection findByCodeAndNumber(java.lang.String code, java.lang.Integer number)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Proposal o WHERE o.code = ?1 AND o.number = ?2"
 *    description="code, number are not indexed."
 * 
 * @ejb.finder
 *    signature="java.util.Collection findByPersonId(java.lang.Integer personId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Proposal o WHERE o.personId = ?1"
 *    
 * @ejb.finder
 *    signature="java.util.Collection findByProposalType(java.lang.String proposalType)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Proposal o WHERE o.proposalType = ?1"
 *    description="proposalType is not indexed."
 * 
 * @ejb.persistence table-name="Proposal"  
 * 
 * @jboss.persistence datasource="${datasource.experimental.jndi.name}" datasource-mapping="${datasource.experimental.mapping}"
 * @ejb.transaction type="Required" 
 *  
 * @ejb.pk class = "java.lang.Integer"
 * 		generate = "false"
 * 
 * @jboss.unknown-pk 
 * 		class="java.lang.Integer" 
 * 		column-name="proposalId"
 * 		jdbc-type="INTEGER"
 * 		sql-type="INTEGER"
 * 		auto-increment="true"
 *   
 * @jboss.entity-command name="${jboss.entity-command.name}" class="${jboss.entity-command.class}"   
 * 
 * @ejb.facade 
 * view-type="local" type="Stateless"
 * 
 *
 */
public abstract class ProposalBean implements javax.ejb.EntityBean {

    /**
    * Returns the proposalId
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the proposalId
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="proposalId"
    * @ejb.value-object match="light"
    */
    public abstract java.lang.Integer getProposalId();

    /**
    * Sets the proposalId
    *
    * @param proposalId the new proposalId value
    */
    public abstract void setProposalId(java.lang.Integer proposalId);

    /**
    * Returns the personId
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the personId
    *
    * @ejb.persistent-field
    * @ejb.persistence column-name="personId"
    * @ejb.value-object match="light"
    */
    public abstract java.lang.Integer getPersonId();

    /**
    * Sets the personId
    *
    * @param personId the new personId value
    */
    public abstract void setPersonId(java.lang.Integer personId);

    /**
    * Returns the title
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the title
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="title"
    * @ejb.value-object match="light"
    */
    public abstract java.lang.String getTitle();

    /**
    * Sets the title
    *
    * @param title the new title value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setTitle(java.lang.String title);

    /**
    * Returns the code
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the code
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="${ejb.proposalCode.column-name}"
    * @ejb.value-object match="light"
    */
    public abstract java.lang.String getCode();

    /**
    * Sets the code
    *
    * @param code the new code value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setCode(java.lang.String code);

    /**
    * Returns the number
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the number
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="${ejb.proposalNumber.column-name}"
    * @ejb.value-object match="light"
    */
    public abstract java.lang.Integer getNumber();

    /**
    * Sets the number
    *
    * @param number the new number value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setNumber(java.lang.Integer number);

    /**
    * Returns the proposalType
    *
    * @return the proposalType
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="proposalType"
    * @ejb.value-object match="light"
    */
    public abstract java.lang.String getProposalType();

    /**
    * Sets the proposalType
    *
    * @param code the new proposalType value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setProposalType(java.lang.String proposalType);

    /**
    * This is a bi-directional one-to-many relationship CMR method
    *
    * @return a java.util.Collection of related ispyb.server.data.interfaces.SessionLocal.
    *
    * @ejb.interface-method view-type="local"
    *
    * @ejb.relation
    *    name="Proposal-cmp20-Session-cmp20"
    *    role-name="Proposal-cmp20-has-Session-cmp20"
    *
    * @jboss.relation-mapping style="foreign-key"
    *
    * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.SessionLightValue"
    *   aggregate-name="Session"
    *   members="ispyb.server.data.interfaces.SessionLocal"
    *   members-name="Session"
    *   match="all"
    *   relation="external"
    *   type="java.util.Collection"
    */
    public abstract java.util.Collection getSessions();

    /**
    * Sets a collection of related ispyb.server.data.interfaces.SessionLocal
    *
    * @param a collection of related ispyb.server.data.interfaces.SessionLocal
    *
    * @ejb.interface-method view-type="local"
    *
    * @param sessions the new CMR value
    */
    public abstract void setSessions(java.util.Collection sessions);

    /**
    * This is a bi-directional one-to-many relationship CMR method
    *
    * @return a ispyb.server.data.interfaces.LabContactLocal of related ispyb.server.data.interfaces.LabContactLocal.
    *
    * @ejb.interface-method view-type="local"
    *
    * @ejb.relation
    *    name="Proposal-cmp20-LabContact-cmp20"
    *    role-name="Proposal-cmp20-has-LabContact-cmp20"
    *
    * @jboss.relation-mapping style="foreign-key"
    *
    *
     * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.LabContactLightValue"
    *   aggregate-name="LabContact"
    *   members="ispyb.server.data.interfaces.LabContactLocal"
    *   members-name="LabContact"
    *   match="all"
    *   relation="external"
    *   type="java.util.Collection"
    */
    public abstract java.util.Collection getLabContacts();

    /**
    * Sets a collection of related ispyb.server.data.interfaces.LabContactLocal
    *
    * @param labContacts a collection of related ispyb.server.data.interfaces.LabContactLocal
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract void setLabContacts(java.util.Collection labContacts);

    /**
    * This is a bi-directional one-to-many relationship CMR method
    *
    * @return a java.util.Collection of related ispyb.server.data.interfaces.ShippingLocal.
    *
    * @ejb.interface-method view-type="local"
    *
    * @ejb.relation
    *    name="Proposal-cmp20-Shipping-cmp20"
    *    role-name="Proposal-cmp20-has-Shipping-cmp20"
    *
    * @jboss.relation-mapping style="foreign-key"
    *
    * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.ShippingLightValue"
    *   aggregate-name="Shipping"
    *   members="ispyb.server.data.interfaces.ShippingLocal"
    *   members-name="Shipping"
    *   match="all"
    *   relation="external"
    *   type="java.util.Collection"
    */
    public abstract java.util.Collection getShippings();

    /**
    * Sets a collection of related ispyb.server.data.interfaces.ShippingLocal
    *
    * @param a collection of related ispyb.server.data.interfaces.ShippingLocal
    *
    * @ejb.interface-method view-type="local"
    *
    * @param shippings the new CMR value
    */
    public abstract void setShippings(java.util.Collection shippings);

    /**
    * This is a bi-directional one-to-many relationship CMR method
    *
    * @return a java.util.Collection of related ispyb.server.data.interfaces.ProteinLocal.
    *
    * @ejb.interface-method view-type="local"
    *
    * @ejb.relation
    *    name="Proposal-cmp20-Protein-cmp20"
    *    role-name="Proposal-cmp20-has-Protein-cmp20"
    *
    * @jboss.relation-mapping style="foreign-key"
    *
    * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.ProteinLightValue"
    *   aggregate-name="Protein"
    *   members="ispyb.server.data.interfaces.ProteinLocal"
    *   members-name="Protein"
    *   match="all"
    *   relation="external"
    *   type="java.util.Collection"
    */
    public abstract java.util.Collection getProteins();

    /**
    * Sets a collection of related ispyb.server.data.interfaces.ProteinLocal
    *
    * @param a collection of related ispyb.server.data.interfaces.ProteinLocal
    *
    * @ejb.interface-method view-type="local"
    *
    * @param proteins the new CMR value
    */
    public abstract void setProteins(java.util.Collection proteins);

    /**
    * This is a bi-directional one-to-many relationship CMR method
    *
    * @return the related ispyb.server.data.interfaces.PersonLocal.
    *
    * @ejb.interface-method view-type="local"
    *
    * @ejb.relation
    *    name="Person-cmp20-Proposal-cmp20"
    *    role-name="Proposal-cmp20-has-Person-cmp20"
    *
    * @jboss.relation-mapping style="foreign-key"
    *
    * @weblogic.column-map
    *    foreign-key-column="personId"
    *    key-column="personId"
    *
    * @jboss.relation
    *    fk-constraint="false"
    *    fk-column="personId"
    *    related-pk-field="personId"
    *
    * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.PersonLightValue"
    *   aggregate-name="Person"
    *   match="all"
    *   relation="external"
    */
    public abstract ispyb.server.data.interfaces.PersonLocal getPerson();

    /**
    * Sets the related ispyb.server.data.interfaces.PersonLocal
    *
    * @param ispyb.server.data.interfaces.ProposalLocal the related $target.variableName
    *
    * @ejb.interface-method view-type="local"
    *
    * @param person the new CMR value
    */
    public abstract void setPerson(ispyb.server.data.interfaces.PersonLocal person);

    /**
    * This is a bi-directional one-to-many relationship CMR method
    *
    * @return a ispyb.server.data.interfaces.DatamatrixInSampleChangerLocal of related ispyb.server.data.interfaces.DatamatrixInSampleChangerLocal.
    *
    * @ejb.interface-method view-type="local"
    *
    * @ejb.relation
    *    name="Proposal-cmp20-DatamatrixInSampleChanger-cmp20"
    *    role-name="Proposal-cmp20-has-DatamatrixInSampleChanger-cmp20"
    *
    * @jboss.relation-mapping style="foreign-key"
    *
    *
     * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.DatamatrixInSampleChangerLightValue"
    *   aggregate-name="DatamatrixInSampleChanger"
    *   members="ispyb.server.data.interfaces.DatamatrixInSampleChangerLocal"
    *   members-name="DatamatrixInSampleChanger"
    *   match="all"
    *   relation="external"
    *   type="java.util.Collection"
    */
    public abstract java.util.Collection getDatamatrixInSampleChangers();

    /**
    * Sets a collection of related ispyb.server.data.interfaces.DatamatrixInSampleChangerLocal
    *
    * @param datamatrixInSampleChangers a collection of related ispyb.server.data.interfaces.DatamatrixInSampleChangerLocal
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract void setDatamatrixInSampleChangers(java.util.Collection datamatrixInSampleChanger);

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
    * @param person mandatory CMR field
    * @return the primary key of the new instance
    *
    * @ejb.create-method
    */
    public java.lang.Integer ejbCreate(ispyb.server.data.interfaces.PersonLocal person) throws javax.ejb.CreateException {
        return null;
    }

    /**
    * The container invokes thos method immediately after it calls ejbCreate. 
    *
    * @param person mandatory CMR field
    */
    public void ejbPostCreate(ispyb.server.data.interfaces.PersonLocal person) throws javax.ejb.CreateException {
        setPerson(person);
    }

    /**
    * This create method takes all parameters (both nullable and not nullable). 
    *
    * When the client invokes a create method, the EJB container invokes the ejbCreate method. 
    * Typically, an ejbCreate method in an entity bean performs the following tasks: 
    * <UL>
    * <LI>Inserts the entity state into the database.</LI>
    * <LI>Initializes the instance variables.</LI>
    * <LI>Returns the primary key.</LI>
    * </UL>
    *
    * @param title the title value
    * @param code the code value
    * @param number the number value
    * @param person CMR field
    * @param proposalType the proposalType value
    * @return the primary key of the new instance
    *
    * @ejb.create-method
    */
    public java.lang.Integer ejbCreate(java.lang.String title, java.lang.String code, java.lang.Integer number, ispyb.server.data.interfaces.PersonLocal person, java.lang.String proposalType) throws javax.ejb.CreateException {
        setTitle(title);
        setCode(code);
        setNumber(number);
        setProposalType(proposalType);
        return null;
    }

    /**
    * The container invokes this method immediately after it calls ejbCreate. 
    *
    * @param title the title value
    * @param code the code value
    * @param number the number value
    * @param person CMR field
    * @param proposalType the proposalType value
    */
    public void ejbPostCreate(java.lang.String title, java.lang.String code, java.lang.Integer number, ispyb.server.data.interfaces.PersonLocal person, java.lang.String proposalType) throws javax.ejb.CreateException {
        setPerson(person);
    }

    /**
    * Return the light value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract ispyb.server.data.interfaces.ProposalLightValue getProposalLightValue();

    /**
    * Set the light value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract void setProposalLightValue(ispyb.server.data.interfaces.ProposalLightValue value);

    /**
    * Return the value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract ispyb.server.data.interfaces.ProposalValue getProposalValue();

    /**
    * Set the value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract void setProposalValue(ispyb.server.data.interfaces.ProposalValue value);

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
    public ispyb.server.data.interfaces.ProposalValue getValueObject() {
        ispyb.server.data.interfaces.ProposalValue valueObject = new ispyb.server.data.interfaces.ProposalValue();
        valueObject.setProposalId(getProposalId());
        valueObject.setTitle(getTitle());
        valueObject.setCode(getCode());
        valueObject.setNumber(getNumber());
        valueObject.setProposalType(getProposalType());
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
    public java.lang.Integer ejbCreate(ispyb.server.data.interfaces.ProposalLightValue value) throws javax.ejb.CreateException {
        setPersonId(value.getPersonId());
        setTitle(value.getTitle());
        setCode(value.getCode());
        setNumber(value.getNumber());
        setProposalType(value.getProposalType());
        return null;
    }

    /**
    * The container invokes this method immediately after it calls ejbCreate. 
    *
    * @param value the value object used to initialise the new instance
    */
    public void ejbPostCreate(ispyb.server.data.interfaces.ProposalLightValue value) throws javax.ejb.CreateException {
    }
}
