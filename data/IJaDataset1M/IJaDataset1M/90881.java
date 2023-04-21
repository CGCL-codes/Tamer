package ispyb.server.data.ejb;

/**
 * @author <a href="http://boss.bekk.no/boss/middlegen/">Middlegen</a>
 *
 *
 * @ejb.bean
 *    type="CMP"
 *    cmp-version="2.x"
 *    name="Blsample"
 *    local-jndi-name="ispyb/BlsampleLocalHome"
 *    view-type="local"
 *    primkey-field="blSampleId"
 *
 * @ejb.value-object
 *    name="Blsample"
 *    extends="ispyb.server.data.interfaces.BlsampleLightValue"
 *	   match="*"
 *	   instantiation="eager"
 *
 * @ejb.value-object
 *    name="BlsampleLight"
 *	   match="light"
 *
 * @ejb.value-object
 *    name="BlsampleLightAndEnergyScan"
 *	  match="lightAndEnergyScan"
 *
 * @ejb.finder
 *    signature="java.util.Collection findAll()"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT OBJECT(o) FROM Blsample o"
 *
 * @ejb.finder
 *    signature="java.util.Collection findByName(java.lang.String name)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.name LIKE ?1"
 *    description="name is not indexed."
 * @ejb.finder
 *    signature="java.util.Collection findByCode(java.lang.String code)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.code = ?1"
 *    description="code is not indexed."
 * @ejb.finder
 *    signature="java.util.Collection findByLocation(java.lang.String location)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.location = ?1"
 *    description="location is not indexed."
 * @ejb.finder
 *    signature="java.util.Collection findByHolderLength(java.lang.Double holderLength)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.holderLength = ?1"
 *    description="holderLength is not indexed."
 * @ejb.finder
 *    signature="java.util.Collection findByLoopLength(java.lang.Double loopLength)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.loopLength = ?1"
 *    description="loopLength is not indexed."
 * @ejb.finder
 *    signature="java.util.Collection findByLoopType(java.lang.String loopType)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.loopType = ?1"
 *    description="loopType is not indexed."
 * @ejb.finder
 *    signature="java.util.Collection findByWireWidth(java.lang.Double wireWidth)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.wireWidth = ?1"
 *    description="wireWidth is not indexed."
 * @ejb.finder
 *    signature="java.util.Collection findByComments(java.lang.String comments)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.comments = ?1"
 *    description="comments is not indexed."
 * @ejb.finder
 *    signature="java.util.Collection findByCompletionStage(java.lang.String completionStage)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.completionStage = ?1"
 *    description="completionStage is not indexed."
 * @ejb.finder
 *    signature="java.util.Collection findByStructureStage(java.lang.String structureStage)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.structureStage = ?1"
 *    description="structureStage is not indexed."
 * @ejb.finder
 *    signature="java.util.Collection findByPublicationStage(java.lang.String publicationStage)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.publicationStage = ?1"
 *    description="publicationStage is not indexed."
 * @ejb.finder
 *    signature="java.util.Collection findByPublicationComments(java.lang.String publicationComments)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.publicationComments = ?1"
 *    description="publicationComments is not indexed."
 * 
 * @ejb.finder
 *    signature="java.util.Collection findByContainerId(java.lang.Integer containerId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Container ct WHERE o.containerId = ct.containerId AND ct.containerId =?1" 
 *    
 * @ejb.finder
 *    signature="java.util.Collection findByDewarId(java.lang.Integer dewarId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Container ct , Dewar dew WHERE o.containerId = ct.containerId AND ct.dewarId = dew.dewarId AND dew.dewarId =?1" 
 * 
 * @ejb.finder
 *    signature="java.util.Collection findByProposalId(java.lang.Integer proposalId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Crystal ct , Protein pt, Proposal pp WHERE o.crystalId = ct.crystalId AND ct.proteinId = pt.proteinId AND pt.proposalId = pp.proposalId AND pp.proposalId =?1 ORDER BY o.blSampleId DESC" 
 * 
 * @ejb.finder
 *    signature="java.util.Collection findByShippingId(java.lang.Integer shippingId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Container ct , Dewar dew, Shipping sh WHERE o.containerId = ct.containerId AND ct.dewarId = dew.dewarId AND dew.shippingId = sh.shippingId AND sh.shippingId =?1"
 *    
 * @ejb.finder
 *    signature="java.util.Collection findByCodeAndShippingId(java.lang.String code, java.lang.Integer shippingId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Container ct , Dewar dew, Shipping sh WHERE o.containerId = ct.containerId AND ct.dewarId = dew.dewarId AND dew.shippingId = sh.shippingId AND o.code=?1 AND sh.shippingId =?2" 
 * 
 * @ejb.finder
 *    signature="java.util.Collection findByProteinId(java.lang.Integer proteinId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Protein pro, Crystal cry  WHERE o.crystalId = cry.crystalId AND cry.proteinId =pro.proteinId AND pro.proteinId =?1" 
 *  
 * @ejb.finder
 *    signature="java.util.Collection findByCrystalId(java.lang.Integer crystalId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Crystal cry  WHERE o.crystalId = cry.crystalId AND cry.crystalId =?1" 
 * 
 *  @ejb.finder
 *    signature="java.util.Collection findByBlSampleStatus(java.lang.String blSampleStatus)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.blSampleStatus = ?1"
 *    description="blSampleStatus is not indexed."
 * 
 *  @ejb.finder
 *    signature="java.util.Collection findByIsInSampleChanger(java.lang.Byte isInSampleChanger)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.isInSampleChanger = ?1"
 *    description="isInSampleChanger is not indexed."
 * 
 *  @ejb.finder
 *    signature="java.util.Collection findByProposalIdAndIsInSampleChanger(java.lang.Integer proposalId, java.lang.Byte isInSampleChanger)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Crystal ct , Protein pt, Proposal pp WHERE (o.crystalId = ct.crystalId AND ct.proteinId = pt.proteinId AND pt.proposalId = pp.proposalId AND pp.proposalId =?1 )AND  o.isInSampleChanger = ?2"
 *
 * @ejb.finder
 *    signature="java.util.Collection findByLastKnownCenteringPosition(java.lang.String lastKnownCenteringPosition)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.lastKnownCenteringPosition = ?1"
 *    description="Finder for not indexed column lastKnownCenteringPosition."
 * 
 * @ejb.finder
 *    signature="java.util.Collection findByNameAndProteinId(java.lang.String name, java.lang.Integer proteinId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Crystal ct WHERE o.name = ?1 AND o.crystalId = ct.crystalId AND ct.proteinId = ?2 "
 *    description="name is not indexed."
 *
 * @ejb.finder
 *    signature="java.util.Collection findByProposalIdAndBlSampleStatus(java.lang.Integer proposalId, java.lang.String status)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Crystal ct , Protein pt, Proposal pp WHERE (o.crystalId = ct.crystalId AND ct.proteinId = pt.proteinId AND pt.proposalId = pp.proposalId AND pp.proposalId =?1 )AND  o.blSampleStatus = ?2"
 *    
 * @ejb.finder
 *    signature="java.util.Collection findByCodeAndProposalId(java.lang.String code, java.lang.Integer proposalId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Crystal ct , Protein pt, Proposal pp WHERE (o.crystalId = ct.crystalId AND ct.proteinId = pt.proteinId AND pt.proposalId = pp.proposalId AND pp.proposalId =?2 )AND  o.code = ?1"
 *    
 * @ejb.finder
 *    signature="java.util.Collection findBySampleId(java.lang.Integer sampleId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Crystal ct , Protein pt, Proposal pp WHERE (o.crystalId = ct.crystalId AND ct.proteinId = pt.proteinId AND pt.proposalId = pp.proposalId )AND  o.blSampleId = ?1"
 *    
 * @ejb.finder
 *    signature="java.util.Collection findByNameAndCodeAndProposalId(java.lang.String name, java.lang.String code, java.lang.Integer proposalId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o, Crystal ct , Protein pt, Proposal pp WHERE o.name LIKE ?1 AND (o.crystalId = ct.crystalId AND ct.proteinId = pt.proteinId AND pt.proposalId = pp.proposalId AND pp.proposalId =?3 )AND  o.code LIKE ?2"
 *  
 * @ejb.finder
 *    signature="java.util.Collection findBySampleIdWithEnergyScan (java.lang.Integer blSampleId)"
 *    result-type-mapping="Local"
 *    method-intf="LocalHome"
 *    query="SELECT DISTINCT OBJECT(o) FROM Blsample o WHERE o.blSampleId = ?1"
 *
 * @ejb.persistence table-name="BLSample"  
 * 
 * @jboss.persistence datasource="${datasource.experimental.jndi.name}" datasource-mapping="${datasource.experimental.mapping}"
 * @ejb.transaction type="Required" 
 * 
 * @ejb.pk class = "java.lang.Integer"
 * 		generate = "false"
 * 
 * @jboss.unknown-pk 
 * 		class="java.lang.Integer" 
 * 		column-name="blSampleId"
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
public abstract class BlsampleBean implements javax.ejb.EntityBean {

    /**
    * Returns the blSampleId
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the blSampleId
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="blSampleId"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.Integer getBlSampleId();

    /**
    * Sets the blSampleId
    *
    * @param blSampleId the new blSampleId value
    */
    public abstract void setBlSampleId(java.lang.Integer blSampleId);

    /**
    * Returns the crystalId
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the crystalId
    *
    * @ejb.persistent-field
    * @ejb.persistence column-name="crystalId"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.Integer getCrystalId();

    /**
    * Sets the crystalId
    *
    * @param crystalId the new crystalId value
    */
    public abstract void setCrystalId(java.lang.Integer crystalId);

    /**
    * Returns the diffractionPlanId
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the diffractionPlanId
    *
    * @ejb.persistent-field
    * @ejb.persistence column-name="diffractionPlanId"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.Integer getDiffractionPlanId();

    /**
    * Sets the diffractionPlanId
    *
    * @param diffractionPlanId the new diffractionPlanId value
    */
    public abstract void setDiffractionPlanId(java.lang.Integer diffractionPlanId);

    /**
    * Returns the containerId
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the containerId
    *
    * @ejb.persistent-field
    * @ejb.persistence column-name="containerId"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.Integer getContainerId();

    /**
    * Sets the containerId
    *
    * @param containerId the new containerId value
    */
    public abstract void setContainerId(java.lang.Integer containerId);

    /**
    * Returns the name
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the name
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="name"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.String getName();

    /**
    * Sets the name
    *
    * @param name the new name value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setName(java.lang.String name);

    /**
    * Returns the code
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the code
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="code"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
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
    * Returns the location
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the location
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="location"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.String getLocation();

    /**
    * Sets the location
    *
    * @param location the new location value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setLocation(java.lang.String location);

    /**
    * Returns the holderLength
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the holderLength
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="holderLength"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.Double getHolderLength();

    /**
    * Sets the holderLength
    *
    * @param holderLength the new holderLength value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setHolderLength(java.lang.Double holderLength);

    /**
    * Returns the loopLength
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the loopLength
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="loopLength"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.Double getLoopLength();

    /**
    * Sets the loopLength
    *
    * @param loopLength the new loopLength value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setLoopLength(java.lang.Double loopLength);

    /**
    * Returns the loopType
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the loopType
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="loopType"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.String getLoopType();

    /**
    * Sets the loopType
    *
    * @param loopType the new loopType value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setLoopType(java.lang.String loopType);

    /**
    * Returns the wireWidth
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the wireWidth
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="wireWidth"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.Double getWireWidth();

    /**
    * Sets the wireWidth
    *
    * @param wireWidth the new wireWidth value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setWireWidth(java.lang.Double wireWidth);

    /**
    * Returns the comments
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the comments
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="comments"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.String getComments();

    /**
    * Sets the comments
    *
    * @param comments the new comments value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setComments(java.lang.String comments);

    /**
    * Returns the completionStage
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the completionStage
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="completionStage"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.String getCompletionStage();

    /**
    * Sets the completionStage
    *
    * @param completionStage the new completionStage value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setCompletionStage(java.lang.String completionStage);

    /**
    * Returns the structureStage
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the structureStage
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="structureStage"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.String getStructureStage();

    /**
    * Sets the structureStage
    *
    * @param structureStage the new structureStage value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setStructureStage(java.lang.String structureStage);

    /**
    * Returns the publicationStage
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the publicationStage
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="publicationStage"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.String getPublicationStage();

    /**
    * Sets the publicationStage
    *
    * @param publicationStage the new publicationStage value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setPublicationStage(java.lang.String publicationStage);

    /**
    * Returns the publicationComments
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the publicationComments
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="publicationComments"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.String getPublicationComments();

    /**
    * Sets the publicationComments
    *
    * @param publicationComments the new publicationComments value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setPublicationComments(java.lang.String publicationComments);

    /**
    * Returns the blSampleStatus
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the blSampleStatus
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="blSampleStatus"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.String getBlSampleStatus();

    /**
    * Sets the blSampleStatus
    *
    * @param blSampleStatus the new blSampleStatus value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setBlSampleStatus(java.lang.String blSampleStatus);

    /**
    * Returns the isInSampleChanger
    * @todo support OracleClob,OracleBlob on WLS
    *
    * @return the isInSampleChanger
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="isInSampleChanger"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.Byte getIsInSampleChanger();

    /**
    * Sets the isInSampleChanger
    *
    * @param isInSampleChanger the new isInSampleChanger value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setIsInSampleChanger(java.lang.Byte isInSampleChanger);

    /**
    * Returns the lastKnownCenteringPosition
    *
    * @return the lastKnownCenteringPosition
    *
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @ejb.persistence column-name="lastKnownCenteringPosition"
    * @ejb.value-object match="light"
    * @ejb.value-object match="lightAndEnergyScan"
    */
    public abstract java.lang.String getLastKnownCenteringPosition();

    /**
    * Sets the lastKnownCenteringPosition
    *
    * @param lastKnownCenteringPosition the new lastKnownCenteringPosition value
    * @ejb.interface-method view-type="local"
    */
    public abstract void setLastKnownCenteringPosition(java.lang.String lastKnownCenteringPosition);

    /**
    * This is a bi-directional many-to-many relationship CMR method
    *
    * @ejb.interface-method view-type="local"
	* 
	* @ejb.relation
	*      name="BLSample-EnergyScan"
	*      role-name="BLSample-has-EnergyScan"
	*      target-multiple="yes"
	* 
	* @jboss.relation-mapping
	*      style="relation-table" 
	* 
	* @jboss.relation-table
	*      table-name="BLSample_has_EnergyScan"
	*      create-table="true"
	*      remove-table="false"
	*      datasource="${datasource.experimental.jndi.name}"
	*      datasource-mapping="${datasource.experimental.mapping}"
	* 
	* @jboss.relation
	*      related-pk-field="energyScanId"
	*      fk-column="energyScanId"
	*      fk-constraint="false"
    * 
    * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.EnergyScanLightValue"
    *   aggregate-name="EnergyScan"
    *   members="ispyb.server.data.interfaces.EnergyScanLocal"
    *   members-name="EnergyScan"
    *   match="lightAndEnergyScan"
    *   relation="external"
    *   type="java.util.Collection"
    */
    public abstract java.util.Collection getEnergyScans();

    /**
    *
    * @ejb.interface-method view-type="local"
    *
    */
    public abstract void setEnergyScans(java.util.Collection energyScans);

    /**
    * This is a bi-directional one-to-many relationship CMR method
    *
    * @return the related ispyb.server.data.interfaces.ContainerLocal.
    *
    * @ejb.interface-method view-type="local"
    *
    * @ejb.relation
    *    name="Container-cmp20-BLSample-cmp20"
    *    role-name="BLSample-cmp20-has-Container-cmp20"
    * 	 cascade-delete="yes"
    *
    * @jboss.relation-mapping style="foreign-key"
    * 
    * @jboss.relation
    *    fk-constraint="true"
    *    fk-column="containerId"
    *    related-pk-field="containerId"
    *    
    *
    * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.ContainerLightValue"
    *   aggregate-name="Container"
    *   match="all"
    *   relation="external"
    */
    public abstract ispyb.server.data.interfaces.ContainerLocal getContainer();

    /**
    * Sets the related ispyb.server.data.interfaces.ContainerLocal
    *
    * @param ispyb.server.data.interfaces.BlsampleLocal the related $target.variableName
    *
    * @ejb.interface-method view-type="local"
    *
    * @param container the new CMR value
    */
    public abstract void setContainer(ispyb.server.data.interfaces.ContainerLocal container);

    /**
    * This is a bi-directional one-to-many relationship CMR method
    *
    * @return a java.util.Collection of related ispyb.server.data.interfaces.DataCollectionLocal.
    *
    * @ejb.interface-method view-type="local"
    *
    * @ejb.relation
    *    name="BLSample-cmp20-DataCollection-cmp20"
    *    role-name="BLSample-cmp20-has-DataCollection-cmp20"
    *
    * @jboss.relation-mapping style="foreign-key"
    *
    * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.DataCollectionLightValue"
    *   aggregate-name="DataCollection"
    *   members="ispyb.server.data.interfaces.DataCollectionLocal"
    *   members-name="DataCollection"
    *   match="all"
    *   relation="external"
    *   type="java.util.Collection"
    */
    public abstract java.util.Collection getDataCollections();

    /**
    * Sets a collection of related ispyb.server.data.interfaces.DataCollectionLocal
    *
    * @param a collection of related ispyb.server.data.interfaces.DataCollectionLocal
    *
    * @ejb.interface-method view-type="local"
    *
    * @param dataCollections the new CMR value
    */
    public abstract void setDataCollections(java.util.Collection dataCollections);

    /**
    * This is a bi-directional one-to-many relationship CMR method
    *
    * @return a ispyb.server.data.interfaces.XfefluorescenceSpectrumLocal of related ispyb.server.data.interfaces.XfefluorescenceSpectrumLocal.
    *
    * @ejb.interface-method view-type="local"
    *
    * @ejb.relation
    *    name="BLSample-cmp20-XFEFluorescenceSpectrum-cmp20"
    *    role-name="BLSample-cmp20-has-XFEFluorescenceSpectrum-cmp20"
    *
    * @jboss.relation-mapping style="foreign-key"
    *
    *
     * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.XfefluorescenceSpectrumLightValue"
    *   aggregate-name="XfefluorescenceSpectrum"
    *   members="ispyb.server.data.interfaces.XfefluorescenceSpectrumLocal"
    *   members-name="XfefluorescenceSpectrum"
    *   match="all"
    *   relation="external"
    *   type="java.util.Collection"
    */
    public abstract java.util.Collection getXfefluorescenceSpectrums();

    /**
    * Sets a collection of related ispyb.server.data.interfaces.XfefluorescenceSpectrumLocal
    *
    * @param xfefluorescenceSpectrums a collection of related ispyb.server.data.interfaces.XfefluorescenceSpectrumLocal
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract void setXfefluorescenceSpectrums(java.util.Collection xfefluorescenceSpectrums);

    /**
    * This is a bi-directional one-to-many relationship CMR method
    *
    * @return the related ispyb.server.data.interfaces.DiffractionPlanLocal.
    *
    * @ejb.interface-method view-type="local"
    *
    * @ejb.relation
    *    name="DiffractionPlan-cmp20-BLSample-cmp20"
    *    role-name="BLSample-cmp20-has-DiffractionPlan-cmp20"
    *
    * @jboss.relation-mapping style="foreign-key"
    *
    * @jboss.relation
    *    fk-constraint="true"
    *    fk-column="diffractionPlanId"
    *    related-pk-field="diffractionPlanId"
    *
    * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.DiffractionPlanLightValue"
    *   aggregate-name="DiffractionPlan"
    *   match="all"
    *   relation="external"
    */
    public abstract ispyb.server.data.interfaces.DiffractionPlanLocal getDiffractionPlan();

    /**
    * Sets the related ispyb.server.data.interfaces.DiffractionPlanLocal
    *
    * @param ispyb.server.data.interfaces.BlsampleLocal the related $target.variableName
    *
    * @ejb.interface-method view-type="local"
    *
    * @param diffractionPlan the new CMR value
    */
    public abstract void setDiffractionPlan(ispyb.server.data.interfaces.DiffractionPlanLocal diffractionPlan);

    /**
    * This is a bi-directional one-to-many relationship CMR method
    *
    * @return the related ispyb.server.data.interfaces.CrystalLocal.
    *
    * @ejb.interface-method view-type="local"
    *
    * @ejb.relation
    *    name="Crystal-cmp20-BLSample-cmp20"
    *    role-name="BLSample-cmp20-has-Crystal-cmp20"
    *
    * @jboss.relation-mapping style="foreign-key"
    *
    * @jboss.relation
    *    fk-constraint="true"
    *    fk-column="crystalId"
    *    related-pk-field="crystalId"
    *
    * @ejb.value-object
    *   aggregate="ispyb.server.data.interfaces.CrystalLightValue"
    *   aggregate-name="Crystal"
    *   match="all"
    *   relation="external"
    */
    public abstract ispyb.server.data.interfaces.CrystalLocal getCrystal();

    /**
    * Sets the related ispyb.server.data.interfaces.CrystalLocal
    *
    * @param ispyb.server.data.interfaces.BlsampleLocal the related $target.variableName
    *
    * @ejb.interface-method view-type="local"
    *
    * @param crystal the new CMR value
    */
    public abstract void setCrystal(ispyb.server.data.interfaces.CrystalLocal crystal);

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
    * @param crystal mandatory CMR field
    * @return the primary key of the new instance
    *
    * @ejb.create-method
    */
    public java.lang.Integer ejbCreate(ispyb.server.data.interfaces.CrystalLocal crystal) throws javax.ejb.CreateException {
        return null;
    }

    /**
    * The container invokes thos method immediately after it calls ejbCreate. 
    *
    * @param crystal mandatory CMR field
    */
    public void ejbPostCreate(ispyb.server.data.interfaces.CrystalLocal crystal) throws javax.ejb.CreateException {
        setCrystal(crystal);
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
    * @param name the name value
    * @param code the code value
    * @param location the location value
    * @param holderLength the holderLength value
    * @param loopLength the loopLength value
    * @param loopType the loopType value
    * @param wireWidth the wireWidth value
    * @param comments the comments value
    * @param completionStage the completionStage value
    * @param structureStage the structureStage value
    * @param publicationStage the publicationStage value
    * @param publicationComments the publicationComments value
    * @param blSampleStatus the blSampleStatus value
    * @param isInSampleChanger the isInSampleChanger value
    * @param lastKnownCenteringPosition the lastKnownCenteringPosition value
    * @param container CMR field
    * @param diffractionPlan CMR field
    * @param crystal CMR field
    * @return the primary key of the new instance
    *
    * @ejb.create-method
    */
    public java.lang.Integer ejbCreate(java.lang.String name, java.lang.String code, java.lang.String location, java.lang.Double holderLength, java.lang.Double loopLength, java.lang.String loopType, java.lang.Double wireWidth, java.lang.String comments, java.lang.String completionStage, java.lang.String structureStage, java.lang.String publicationStage, java.lang.String publicationComments, java.lang.String blSampleStatus, java.lang.Byte isInSampleChanger, java.lang.String lastKnownCenteringPosition, ispyb.server.data.interfaces.ContainerLocal container, ispyb.server.data.interfaces.DiffractionPlanLocal diffractionPlan, ispyb.server.data.interfaces.CrystalLocal crystal) throws javax.ejb.CreateException {
        setName(name);
        setCode(code);
        setLocation(location);
        setHolderLength(holderLength);
        setLoopLength(loopLength);
        setLoopType(loopType);
        setWireWidth(wireWidth);
        setComments(comments);
        setCompletionStage(completionStage);
        setPublicationComments(publicationComments);
        setStructureStage(structureStage);
        setPublicationStage(publicationStage);
        setBlSampleStatus(blSampleStatus);
        setIsInSampleChanger(isInSampleChanger);
        setLastKnownCenteringPosition(lastKnownCenteringPosition);
        setCrystal(crystal);
        return null;
    }

    /**
    * The container invokes this method immediately after it calls ejbCreate. 
    *
    * @param name the name value
    * @param code the code value
    * @param location the location value
    * @param holderLength the holderLength value
    * @param loopLength the loopLength value
    * @param loopType the loopType value
    * @param wireWidth the wireWidth value
    * @param comments the comments value
    * @param completionStage the completionStage value
    * @param structureStage the structureStage value
    * @param publicationStage the publicationStage value
    * @param publicationComments the publicationComments value
    * @param blSampleStatus the blSampleStatus value
    * @param isInSampleChanger the isInSampleChanger value
    * @param lastKnownCenteringPosition the lastKnownCenteringPosition value
    * @param container CMR field
    * @param diffractionPlan CMR field
    * @param crystal CMR field
    */
    public void ejbPostCreate(java.lang.String name, java.lang.String code, java.lang.String location, java.lang.Double holderLength, java.lang.Double loopLength, java.lang.String loopType, java.lang.Double wireWidth, java.lang.String comments, java.lang.String completionStage, java.lang.String structureStage, java.lang.String publicationStage, java.lang.String publicationComments, java.lang.String blSampleStatus, java.lang.Byte isInSampleChanger, java.lang.String lastKnownCenteringPosition, ispyb.server.data.interfaces.ContainerLocal container, ispyb.server.data.interfaces.DiffractionPlanLocal diffractionPlan, ispyb.server.data.interfaces.CrystalLocal crystal) throws javax.ejb.CreateException {
        setContainer(container);
        setDiffractionPlan(diffractionPlan);
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
    public abstract ispyb.server.data.interfaces.BlsampleLightValue getBlsampleLightValue();

    /**
    * Set the light value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract void setBlsampleLightValue(ispyb.server.data.interfaces.BlsampleLightValue value);

    /**
    * Return the value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract ispyb.server.data.interfaces.BlsampleValue getBlsampleValue();

    /**
    * Set the value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract void setBlsampleValue(ispyb.server.data.interfaces.BlsampleValue value);

    /**
    * Return the light value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract ispyb.server.data.interfaces.BlsampleLightAndEnergyScanValue getBlsampleLightAndEnergyScanValue();

    /**
    * Set the light value object version of this entity.
    *
    * @ejb.interface-method view-type="local"
    */
    public abstract void setBlsampleLightAndEnergyScanValue(ispyb.server.data.interfaces.BlsampleLightAndEnergyScanValue value);

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
    public ispyb.server.data.interfaces.BlsampleValue getValueObject() {
        ispyb.server.data.interfaces.BlsampleValue valueObject = new ispyb.server.data.interfaces.BlsampleValue();
        valueObject.setBlSampleId(getBlSampleId());
        valueObject.setName(getName());
        valueObject.setCode(getCode());
        valueObject.setLocation(getLocation());
        valueObject.setHolderLength(getHolderLength());
        valueObject.setLoopLength(getLoopLength());
        valueObject.setLoopType(getLoopType());
        valueObject.setWireWidth(getWireWidth());
        valueObject.setComments(getComments());
        valueObject.setCompletionStage(getCompletionStage());
        valueObject.setStructureStage(getStructureStage());
        valueObject.setPublicationStage(getPublicationStage());
        valueObject.setPublicationComments(getPublicationComments());
        valueObject.setBlSampleStatus(getBlSampleStatus());
        valueObject.setIsInSampleChanger(getIsInSampleChanger());
        valueObject.setLastKnownCenteringPosition(getLastKnownCenteringPosition());
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
    public java.lang.Integer ejbCreate(ispyb.server.data.interfaces.BlsampleLightValue value) throws javax.ejb.CreateException {
        setDiffractionPlanId(value.getDiffractionPlanId());
        setCrystalId(value.getCrystalId());
        setContainerId(value.getContainerId());
        setName(value.getName());
        setCode(value.getCode());
        setLocation(value.getLocation());
        setHolderLength(value.getHolderLength());
        setLoopLength(value.getLoopLength());
        setLoopType(value.getLoopType());
        setWireWidth(value.getWireWidth());
        setComments(value.getComments());
        setCompletionStage(value.getCompletionStage());
        setStructureStage(value.getStructureStage());
        setPublicationStage(value.getPublicationStage());
        setPublicationComments(value.getPublicationComments());
        setBlSampleStatus(value.getBlSampleStatus());
        setIsInSampleChanger(value.getIsInSampleChanger());
        setLastKnownCenteringPosition(value.getLastKnownCenteringPosition());
        return null;
    }

    /**
    * The container invokes this method immediately after it calls ejbCreate. 
    *
    * @param value the value object used to initialise the new instance
    */
    public void ejbPostCreate(ispyb.server.data.interfaces.BlsampleLightValue value) throws javax.ejb.CreateException {
    }
}
