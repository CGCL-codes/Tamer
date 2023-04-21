package com.sitescape.team.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.sitescape.team.modelprocessor.InstanceLevelProcessorSupport;
import com.sitescape.team.security.function.WorkArea;
import com.sitescape.util.Validator;

/**
 * This object represents a container.
 * 
 * @hibernate.class table="SS_Forums" dynamic-update="true" dynamic-insert="false" lazy="false"
 * @hibernate.discriminator type="string" length="16" column="type"
 * @hibernate.cache usage="read-write"
 * @hibernate.mapping auto-import="false"
 * need auto-import = false so names don't collide with jbpm
 * @author Jong Kim
 *
 */
public abstract class Binder extends DefinableEntity implements DefinitionArea, WorkArea, InstanceLevelProcessorSupport {

    protected boolean deleted = false;

    protected String name = "";

    protected Principal owner;

    protected Map properties;

    protected NotificationDef notificationDef;

    protected PostingDef posting;

    protected Integer upgradeVersion;

    protected Long zoneId;

    protected String type;

    protected String pathName;

    protected List definitions;

    protected Definition defaultPostingDef;

    protected List binders;

    protected Map workflowAssociations;

    protected boolean definitionsInherited = true;

    protected boolean functionMembershipInherited = true;

    private String internalId;

    protected boolean library = true;

    protected boolean uniqueTitles = false;

    public Binder() {
    }

    public Binder(Binder source) {
        super(source);
        if (source.definitions != null) definitions = new ArrayList(source.definitions);
        if (source.workflowAssociations != null) workflowAssociations = new HashMap(source.workflowAssociations);
        name = source.name;
        zoneId = source.zoneId;
        type = source.type;
        definitionsInherited = source.definitionsInherited;
        functionMembershipInherited = source.functionMembershipInherited;
        library = source.library;
        uniqueTitles = source.uniqueTitles;
        defaultPostingDef = source.defaultPostingDef;
        if (source.properties != null) properties = new HashMap(source.properties);
    }

    /**
     * @hibernate.property not-null="true"
     */
    public Long getZoneId() {
        return this.zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public boolean isZone() {
        if (getZoneId().equals(getId())) return true;
        return false;
    }

    public boolean isRoot() {
        return getParentBinder() == null;
    }

    /**
     * @hibernate.property
     */
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @hibernate.property
     */
    public boolean isLibrary() {
        return library;
    }

    public void setLibrary(boolean library) {
        this.library = library;
    }

    /**
     * @hibernate.property
     */
    public boolean isUniqueTitles() {
        return uniqueTitles;
    }

    public void setUniqueTitles(boolean uniqueTitles) {
        this.uniqueTitles = uniqueTitles;
    }

    /**
     * Internal id used to identify default binders.  This id plus
     * the zoneId are used to locate default binders.  If we just used the primary key id
     * the zones would need the same default and that may not be desirable.
     * @hibernate.property length="32"
     */
    public String getInternalId() {
        return this.internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public boolean isReserved() {
        return Validator.isNotNull(internalId);
    }

    /**
     * @hibernate.property length="1024" 
     */
    public String getPathName() {
        return this.pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    /**
     * @hibernate.bag access="field" lazy="true" cascade="all" inverse="true" optimistic-lock="false" 
	 * @hibernate.key column="parentBinder" 
	 * @hibernate.one-to-many class="com.sitescape.team.domain.Binder" 
     * @hibernate.cache usage="read-write"
     * Returns a List of binders.
     * @return
     */
    public List getBinders() {
        if (binders == null) binders = new ArrayList();
        return binders;
    }

    public void addBinder(Binder binder) {
        getBinders().add(binder);
        binder.setParentBinder(this);
    }

    public void removeBinder(Binder binder) {
        getBinders().remove(binder);
        binder.setParentBinder(null);
    }

    /**
     * @hibernate.many-to-one access="field" class="com.sitescape.team.domain.Definition"
     * @hibernate.column name="defaultPostingDef" sql-type="char(32)"
     * @return
     */
    public Definition getDefaultPostingDef() {
        return defaultPostingDef;
    }

    public void setDefaultPostingDef(Definition defaultPostingDef) {
        this.defaultPostingDef = defaultPostingDef;
    }

    /**
     * @hibernate.property length="16" insert="false" update="false"
     *
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @hibernate.property length="128" 
     * @return
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        if (isRoot()) return name;
        return getParentBinder().getFullName() + "." + name;
    }

    /**
     * @hibernate.component prefix="notify_"
     * @return
     */
    public NotificationDef getNotificationDef() {
        if (notificationDef == null) notificationDef = new NotificationDef();
        return notificationDef;
    }

    public void setNotificationDef(NotificationDef notificationDef) {
        this.notificationDef = notificationDef;
    }

    /**
     * @hibernate.many-to-one 
     * @return
     */
    public PostingDef getPosting() {
        return posting;
    }

    public void setPosting(PostingDef posting) {
        this.posting = posting;
    }

    /**
     * @hibernate.many-to-one
     */
    public Principal getOwner() {
        if (owner != null) return owner;
        HistoryStamp creation = getCreation();
        if ((creation != null) && creation.getPrincipal() != null) {
            return creation.getPrincipal();
        }
        return null;
    }

    public void setOwner(Principal owner) {
        this.owner = owner;
    }

    /**
     * @hibernate.property type="org.springframework.orm.hibernate3.support.BlobSerializableType"
     * @return
     */
    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public void setProperty(String name, Object value) {
        if (properties == null) properties = new HashMap();
        properties.put(name, value);
    }

    public Object getProperty(String name) {
        if (properties == null) return null;
        return properties.get(name);
    }

    /**
     * hibernate.property 
     */
    public Integer getUpgradeVersion() {
        return this.upgradeVersion;
    }

    public void setUpgradeVersion(Integer upgradeVersion) {
        this.upgradeVersion = upgradeVersion;
    }

    public String toString() {
        return getPathName();
    }

    public Long getWorkAreaId() {
        return getId();
    }

    public String getWorkAreaType() {
        return getEntityType().name();
    }

    public WorkArea getParentWorkArea() {
        return this.getParentBinder();
    }

    /**
	 * @hibernate.property not-null="true"
	 * @return
	 */
    public boolean isFunctionMembershipInherited() {
        if (isRoot()) return false;
        return functionMembershipInherited;
    }

    public void setFunctionMembershipInherited(boolean functionMembershipInherited) {
        this.functionMembershipInherited = functionMembershipInherited;
    }

    public boolean isFunctionMembershipInheritanceSupported() {
        if (isRoot()) return false;
        return true;
    }

    public Long getOwnerId() {
        Principal owner = getOwner();
        if (owner == null) return null;
        return owner.getId();
    }

    public String getProcessorClassName(String processorKey) {
        return (String) getProperty(processorKey);
    }

    public void setProcessorClassName(String processorKey, String processorClassName) {
        setProperty(processorKey, processorClassName);
    }

    public String getProcessorKey(String processorKey) {
        return processorKey;
    }

    public Long getDefinitionAreaId() {
        return getId();
    }

    public String getDefinitionAreaType() {
        return getEntityType().name();
    }

    public DefinitionArea getParentDefinitionArea() {
        return getParentBinder();
    }

    /**
     * @hibernate.property
     * @return
     */
    public boolean isDefinitionsInherited() {
        return definitionsInherited;
    }

    public void setDefinitionsInherited(boolean definitionsInherited) {
        this.definitionsInherited = definitionsInherited;
    }

    public boolean isDefinitionInheritanceSupported() {
        if (isRoot()) return false;
        return true;
    }

    protected List getDefs(int type) {
        if (definitionsInherited && !isRoot()) return new ArrayList(getParentBinder().getDefs(type));
        Definition def;
        List result = new ArrayList();
        if (definitions == null) definitions = new ArrayList();
        for (int i = 0; i < definitions.size(); ++i) {
            def = (Definition) definitions.get(i);
            if (def.getType() == type) {
                result.add(def);
            }
        }
        return result;
    }

    public List getDefinitions() {
        if (definitionsInherited && !isRoot()) return new ArrayList(getParentBinder().getDefinitions());
        if (definitions == null) definitions = new ArrayList();
        return definitions;
    }

    public void setDefinitions(List definitions) {
        if (this.definitions == null) this.definitions = new ArrayList();
        if (definitions != this.definitions) {
            this.definitions.clear();
            if (definitions != null) this.definitions.addAll(definitions);
        }
    }

    public void removeDefinition(Definition def) {
        getDefinitions().remove(def);
        Map myDefs = getWorkflowAssociations();
        myDefs.remove(def.getId());
    }

    public Definition getDefaultEntryDef() {
        List eDefinitions = getEntryDefinitions();
        if (eDefinitions.size() > 0) return (Definition) eDefinitions.get(0);
        return null;
    }

    public Definition getDefaultViewDef() {
        List eDefinitions = getViewDefinitions();
        if (eDefinitions.size() > 0) return (Definition) eDefinitions.get(0);
        return null;
    }

    public Definition getEntryDef() {
        Definition result = getDefaultViewDef();
        if (result == null) return entryDef;
        return result;
    }

    public Map getWorkflowAssociations() {
        if (definitionsInherited && !isRoot()) return new HashMap(getParentBinder().getWorkflowAssociations());
        if (workflowAssociations == null) workflowAssociations = new HashMap();
        return workflowAssociations;
    }

    public void setWorkflowAssociations(Map workflowAssociations) {
        if (this.workflowAssociations == null) this.workflowAssociations = new HashMap();
        if (workflowAssociations != this.workflowAssociations) {
            this.workflowAssociations.clear();
            if (workflowAssociations != null) this.workflowAssociations.putAll(workflowAssociations);
        }
    }

    /** 
     * Remove the mapping from an definition to a workflow.
     * The same workflow may be mapped to multiple times. 
     */
    public void removeWorkflow(Definition def) {
        Map myDefs = getWorkflowAssociations();
        Map defs = new HashMap(myDefs);
        for (Iterator iter = defs.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry e = (Map.Entry) iter.next();
            if (def.equals(e.getValue())) myDefs.remove(e.getKey());
        }
    }

    /**
     * String appended to processorKeys to allow for customizations
     */
    public String getProcessorTag() {
        return null;
    }

    public abstract List getEntryDefinitions();

    public abstract List getViewDefinitions();
}
