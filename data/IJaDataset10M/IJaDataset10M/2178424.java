package org.avaje.ebean.server.deploy.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.avaje.ebean.bean.BeanController;
import org.avaje.ebean.bean.BeanFinder;
import org.avaje.ebean.bean.BeanListener;
import org.avaje.ebean.server.core.ConcurrencyMode;
import org.avaje.ebean.server.deploy.BeanDescriptorOwner;
import org.avaje.ebean.server.deploy.DeployNamedQuery;
import org.avaje.ebean.server.deploy.DeployNamedUpdate;
import org.avaje.ebean.server.deploy.IdentityGeneration;
import org.avaje.ebean.server.deploy.InheritInfo;
import org.avaje.ebean.server.reflect.BeanReflect;

/**
 * Describes Beans including their deployment information.
 */
public class DeployBeanDescriptor {

    boolean baseTableNotFound;

    /**
	 * Map of BeanProperty Linked so as to preserve order.
	 */
    final LinkedHashMap<String, DeployBeanProperty> propMap = new LinkedHashMap<String, DeployBeanProperty>();

    /**
	 * The type of bean this describes.
	 */
    final Class<?> beanType;

    /**
	 * This is not sent to a remote client.
	 */
    final BeanDescriptorOwner owner;

    final Map<String, DeployNamedQuery> namedQueries = new LinkedHashMap<String, DeployNamedQuery>();

    final Map<String, DeployNamedUpdate> namedUpdates = new LinkedHashMap<String, DeployNamedUpdate>();

    DeployBeanPropertyAssocOne unidirectional;

    /**
	 * The EbeanServer name. Same as the plugin name.
	 */
    String serverName;

    /**
	 * Type of Identity generation strategy used.
	 */
    char identityGeneration = IdentityGeneration.AUTO;

    /**
	 * The name of an IdGenerator (optional).
	 */
    String idGeneratorName;

    /**
	 * The database sequence name (optional).
	 */
    String sequenceNextVal;

    /**
	 * True if this is Table based for TableBeans.
	 */
    boolean tableGenerated;

    /**
	 * True if this is an Embedded bean.
	 */
    boolean embedded;

    /**
	 * Set for beans that don't have a default constructor and are typically
	 * built using a BeanFinder instead. The Ebean "Meta" beans are examples of
	 * this.
	 */
    boolean defaultConstructor = true;

    String lazyFetchIncludes;

    /**
	 * The concurrency mode for beans of this type.
	 */
    int concurrencyMode = ConcurrencyMode.ALL;

    /**
	 * The tables this bean is dependent on.
	 */
    String[] dependantTables;

    /**
	 * Extra deployment attributes.
	 */
    HashMap<String, String> extraAttrMap = new HashMap<String, String>();

    /**
	 * The base database table.
	 */
    String baseTable;

    /**
	 * Sql table alias for the base table. Also identifies which properties are
	 * 'base table' properties.
	 */
    String baseTableAlias;

    /**
	 * Used to provide mechanism to new EntityBean instances. Generated code
	 * faster than reflection at this stage.
	 */
    BeanReflect beanReflect;

    /**
	 * The EntityBean type used to create new EntityBeans.
	 */
    Class<?> factoryType;

    /**
	 * Intercept pre post on insert,update,delete and postLoad(). Server side
	 * only.
	 */
    BeanController controller;

    /**
	 * If set overrides the find implementation. Server side only.
	 */
    BeanFinder beanFinder;

    /**
	 * Listens for post commit insert update and delete events.
	 */
    BeanListener beanListener;

    /**
	 * The table joins for this bean. Server side only.
	 */
    ArrayList<DeployTableJoin> tableJoinList = new ArrayList<DeployTableJoin>();

    /**
	 * Inheritance information. Server side only.
	 */
    InheritInfo inheritInfo;

    String name;

    /**
	 * Construct the BeanDescriptor.
	 */
    public DeployBeanDescriptor(BeanDescriptorOwner owner, Class<?> beanType) {
        this.owner = owner;
        this.beanType = beanType;
        if (owner != null) {
            this.serverName = owner.getServerName();
        }
    }

    /**
	 * Return true if the base table for this entity bean was not found.
	 */
    public boolean isBaseTableNotFound() {
        return baseTableNotFound;
    }

    /**
	 * This this to true when the base table for this entity bean
	 * was not found in the dictionary.
	 */
    public void setBaseTableNotFound(boolean baseTableNotFound) {
        this.baseTableNotFound = baseTableNotFound;
    }

    public boolean isSqlSelectBased() {
        DeployNamedQuery defaultQuery = namedQueries.get("default");
        if (defaultQuery != null) {
            return defaultQuery.isSqlSelect();
        }
        return false;
    }

    public void add(DeployNamedUpdate namedUpdate) {
        namedUpdates.put(namedUpdate.getName(), namedUpdate);
    }

    public void add(DeployNamedQuery namedQuery) {
        namedQueries.put(namedQuery.getName(), namedQuery);
    }

    public Map<String, DeployNamedQuery> getNamedQueries() {
        return namedQueries;
    }

    public Map<String, DeployNamedUpdate> getNamedUpdates() {
        return namedUpdates;
    }

    public BeanDescriptorOwner getOwner() {
        return owner;
    }

    public BeanReflect getBeanReflect() {
        return beanReflect;
    }

    /**
	 * Return the class type this BeanDescriptor describes.
	 */
    public Class<?> getBeanType() {
        return beanType;
    }

    /**
	 * Return the class type this BeanDescriptor describes.
	 */
    public Class<?> getFactoryType() {
        return factoryType;
    }

    /**
	 * Set the class used to create new EntityBean instances.
	 * <p>
	 * Normally this would be a subclass dynamically generated for this bean.
	 * </p>
	 */
    public void setFactoryType(Class<?> factoryType) {
        this.factoryType = factoryType;
    }

    /**
	 * Set the BeanReflect used to create new instances of an EntityBean. This
	 * could use reflection or code generation to do this.
	 */
    public void setBeanReflect(BeanReflect beanReflect) {
        this.beanReflect = beanReflect;
    }

    /**
	 * Return the name of the server this BeanDescriptor belongs to.
	 */
    public String getServerName() {
        return serverName;
    }

    /**
	 * Returns the Inheritance mapping information. This will be null if this
	 * type of bean is not involved in any ORM inheritance mapping.
	 */
    public InheritInfo getInheritInfo() {
        return inheritInfo;
    }

    /**
	 * Set the ORM inheritance mapping information.
	 */
    public void setInheritInfo(InheritInfo inheritInfo) {
        this.inheritInfo = inheritInfo;
    }

    /**
	 * Return true if this was generated from jdbc meta data of a table. Returns
	 * false for normal beans.
	 */
    public boolean isTableGenerated() {
        return tableGenerated;
    }

    /**
	 * Set to true when this is generated from jdbc meta data of a table.
	 */
    public void setTableGenerated(boolean tableGenerated) {
        this.tableGenerated = tableGenerated;
    }

    /**
	 * Return true if this is an embedded bean.
	 */
    public boolean isEmbedded() {
        return embedded;
    }

    /**
	 * Set to true if this is an embedded bean.
	 */
    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    /**
	 * Return true if this is a meta entity bean.
	 */
    public boolean hasDefaultConstructor() {
        return defaultConstructor;
    }

    /**
	 * Set whether this is a meta entity bean.
	 */
    public void setDefaultConstructor(boolean defaultConstructor) {
        this.defaultConstructor = defaultConstructor;
    }

    public DeployBeanPropertyAssocOne getUnidirectional() {
        return unidirectional;
    }

    public void setUnidirectional(DeployBeanPropertyAssocOne unidirectional) {
        this.unidirectional = unidirectional;
    }

    /**
	 * Remove the transient property.
	 */
    public void remove(DeployBeanProperty beanProp) {
        propMap.remove(beanProp.getName());
    }

    /**
	 * Return the concurrency mode used for beans of this type.
	 */
    public int getConcurrencyMode() {
        return concurrencyMode;
    }

    /**
	 * Set the concurrency mode used for beans of this type.
	 */
    public void setConcurrencyMode(int concurrencyMode) {
        this.concurrencyMode = concurrencyMode;
    }

    /**
	 * Return the tables this bean is dependant on. This implies that if any of
	 * these tables are modified then cached beans may be invalidated.
	 */
    public String[] getDependantTables() {
        return dependantTables;
    }

    /**
	 * Set the tables this bean is dependant on. This implies that if any of
	 * these tables are modified then cached beans may be invalidated.
	 */
    public void setDependantTables(String[] dependantTables) {
        this.dependantTables = dependantTables;
    }

    /**
	 * Return the beanListener.
	 */
    public BeanListener getBeanListener() {
        return beanListener;
    }

    /**
	 * Set the beanListener.
	 */
    public void setBeanListener(BeanListener beanListener) {
        this.beanListener = beanListener;
    }

    /**
	 * Return the beanFinder. Usually null unless overriding the finder.
	 */
    public BeanFinder getBeanFinder() {
        return beanFinder;
    }

    /**
	 * Set the BeanFinder to use for beans of this type. This is set to override
	 * the finding from the default.
	 */
    public void setBeanFinder(BeanFinder beanFinder) {
        this.beanFinder = beanFinder;
    }

    /**
	 * Return the Controller.
	 */
    public BeanController getBeanController() {
        return controller;
    }

    /**
	 * Set the Controller.
	 */
    public void setBeanController(BeanController controller) {
        this.controller = controller;
    }

    /**
	 * Return true if this bean type should use IdGeneration.
	 * <p>
	 * If this is false and the Id is null it is assumed that a database auto
	 * increment feature is being used to populate the id.
	 * </p>
	 */
    public boolean isUseIdGenerator() {
        return identityGeneration == IdentityGeneration.ID_GENERATOR;
    }

    /**
	 * Return the base table. Only properties mapped to the base table are by
	 * default persisted.
	 */
    public String getBaseTable() {
        return baseTable;
    }

    /**
	 * Set the base table. Only properties mapped to the base table are by
	 * default persisted.
	 */
    public void setBaseTable(String baseTable) {
        this.baseTable = baseTable;
    }

    /**
	 * Return the base table alias.
	 */
    public String getBaseTableAlias() {
        return baseTableAlias;
    }

    /**
	 * Set the base table alias. Set when bean is mapped to multiple tables.
	 * Used to identify which properties are db write.
	 */
    public void setBaseTableAlias(String baseTableAlias) {
        this.baseTableAlias = baseTableAlias;
    }

    /**
	 * Add a bean property.
	 */
    public DeployBeanProperty addBeanProperty(DeployBeanProperty prop) {
        return propMap.put(prop.getName(), prop);
    }

    /**
	 * Get a BeanProperty by its name.
	 */
    public DeployBeanProperty getBeanProperty(String propName) {
        return propMap.get(propName);
    }

    public Map<String, String> getExtraAttributeMap() {
        return extraAttrMap;
    }

    /**
	 * Get a named extra attribute.
	 */
    public String getExtraAttribute(String key) {
        return (String) extraAttrMap.get(key);
    }

    /**
	 * Set an extra attribute with a given name.
	 * 
	 * @param key
	 *            the name of the extra attribute
	 * @param value
	 *            the value of the extra attribute
	 */
    public void setExtraAttribute(String key, String value) {
        extraAttrMap.put(key, value);
    }

    /**
	 * Return the bean class name this descriptor is used for.
	 * <p>
	 * If this BeanDescriptor is for a table then this returns the table name
	 * instead.
	 * </p>
	 */
    public String getFullName() {
        if (tableGenerated) {
            return "table[" + baseTable + "]";
        }
        return beanType.getName();
    }

    /**
	 * Return the bean short name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Set the bean shortName.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Return the identity generation type.
	 */
    public char getIdentityGeneration() {
        return identityGeneration;
    }

    /**
	 * Set the identity generation type.
	 */
    public void setIdentityGeneration(char identityGenerationType) {
        this.identityGeneration = identityGenerationType;
    }

    /**
	 * Return the sequence name with nextval wrapping.
	 */
    public String getSequenceNextVal() {
        return sequenceNextVal;
    }

    /**
	 * Set the sequence name with nextval wrapping.
	 */
    public void setSequenceNextVal(String sequenceNextVal) {
        this.sequenceNextVal = sequenceNextVal;
    }

    /**
	 * Return the name of the IdGenerator that should be used with this type of
	 * bean. A null value could be used to specify the 'default' IdGenerator.
	 */
    public String getIdGeneratorName() {
        return idGeneratorName;
    }

    /**
	 * Set the name of the IdGenerator that should be used with this type of
	 * bean.
	 */
    public void setIdGeneratorName(String idGeneratorName) {
        this.idGeneratorName = idGeneratorName;
    }

    /**
	 * Return the includes for getReference().
	 */
    public String getLazyFetchIncludes() {
        return lazyFetchIncludes;
    }

    /**
	 * Set includes to use for lazy loading by getReference(). Note queries also
	 * build references and includes on the actual association are used for
	 * those references.
	 */
    public void setLazyFetchIncludes(String lazyFetchIncludes) {
        if (lazyFetchIncludes != null && lazyFetchIncludes.length() > 0) {
            this.lazyFetchIncludes = lazyFetchIncludes;
        }
    }

    /**
	 * Summary description.
	 */
    public String toString() {
        return getFullName();
    }

    /**
	 * Add a TableJoin to this type of bean. For Secondary table properties.
	 */
    public void addTableJoin(DeployTableJoin join) {
        tableJoinList.add(join);
    }

    public List<DeployTableJoin> getTableJoins() {
        return tableJoinList;
    }

    /**
	 * Return an Iterator of all BeanProperty.
	 */
    public Iterator<DeployBeanProperty> propertiesAll() {
        return propMap.values().iterator();
    }

    /**
	 * Return the BeanProperty that make up the unqiue id.
	 * <p>
	 * The order of these properties can be relied on to be consistent if the
	 * bean itself doesn't change or the xml deployment order does not change.
	 * </p>
	 */
    public List<DeployBeanProperty> propertiesId() {
        ArrayList<DeployBeanProperty> list = new ArrayList<DeployBeanProperty>();
        Iterator<DeployBeanProperty> it = propMap.values().iterator();
        while (it.hasNext()) {
            DeployBeanProperty prop = it.next();
            if (prop.isId()) {
                list.add(prop);
            }
        }
        return list;
    }

    /**
	 * Return an Iterator of BeanPropertyAssocOne that are not embedded. These
	 * are effectively joined beans. For ManyToOne and OneToOne associations.
	 */
    public List<DeployBeanPropertyAssocOne> propertiesAssocOne() {
        ArrayList<DeployBeanPropertyAssocOne> list = new ArrayList<DeployBeanPropertyAssocOne>();
        Iterator<DeployBeanProperty> it = propMap.values().iterator();
        while (it.hasNext()) {
            DeployBeanProperty prop = it.next();
            if (prop instanceof DeployBeanPropertyAssocOne) {
                if (!prop.isEmbedded()) {
                    list.add((DeployBeanPropertyAssocOne) prop);
                }
            }
        }
        return list;
    }

    /**
	 * Return BeanPropertyAssocMany for this descriptor.
	 */
    public List<DeployBeanPropertyAssocMany> propertiesAssocMany() {
        ArrayList<DeployBeanPropertyAssocMany> list = new ArrayList<DeployBeanPropertyAssocMany>();
        Iterator<DeployBeanProperty> it = propMap.values().iterator();
        while (it.hasNext()) {
            DeployBeanProperty prop = it.next();
            if (prop instanceof DeployBeanPropertyAssocMany) {
                list.add((DeployBeanPropertyAssocMany) prop);
            }
        }
        return list;
    }

    /**
	 * Returns 'Version' properties on this bean. These are 'Counter' or 'Update
	 * Timestamp' type properties. Note version properties can also be on
	 * embedded beans rather than on the bean itself.
	 */
    public List<DeployBeanProperty> propertiesVersion() {
        ArrayList<DeployBeanProperty> list = new ArrayList<DeployBeanProperty>();
        Iterator<DeployBeanProperty> it = propMap.values().iterator();
        while (it.hasNext()) {
            DeployBeanProperty prop = it.next();
            if (prop instanceof DeployBeanPropertyAssoc) {
            } else {
                if (!prop.isId() && prop.isVersionColumn()) {
                    list.add(prop);
                }
            }
        }
        return list;
    }

    /**
	 * base properties without the unique id properties.
	 */
    public List<DeployBeanProperty> propertiesBase() {
        ArrayList<DeployBeanProperty> list = new ArrayList<DeployBeanProperty>();
        Iterator<DeployBeanProperty> it = propMap.values().iterator();
        while (it.hasNext()) {
            DeployBeanProperty prop = it.next();
            if (prop instanceof DeployBeanPropertyAssoc) {
            } else {
                if (!prop.isId()) {
                    list.add(prop);
                }
            }
        }
        return list;
    }
}
