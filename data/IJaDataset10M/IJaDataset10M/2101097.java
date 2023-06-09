package org.nightlabs.jfire.scripting;

import java.io.Serializable;
import java.util.Collection;
import javax.jdo.JDODetachedFieldAccessException;
import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.listener.StoreCallback;
import org.apache.log4j.Logger;
import org.nightlabs.util.Util;

/**
 * Base class for objects in the JFire scripting tree and provides the
 * common properties of these objects like a name, a description and
 * a {@link ScriptParameterSet}.
 * <p>
 * Subclasses are {@link ScriptCategory} that is a container for other categories and
 * {@link Script} the actual script.
 * </p>
 * 
 * @author Marco Schulze - marco at nightlabs dot de
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 *
 * @jdo.persistence-capable
 *		identity-type="application"
 *		objectid-class="org.nightlabs.jfire.scripting.id.ScriptRegistryItemID"
 *		detachable="true"
 *		table="JFireScripting_ScriptRegistryItem"
 *
 * @jdo.inheritance strategy="new-table"
 *
 * @jdo.create-objectid-class field-order="organisationID, scriptRegistryItemType, scriptRegistryItemID"
 * 
 * @jdo.fetch-group name="ScriptRegistryItem.parent" fetch-groups="default" fields="parent"
 * @jdo.fetch-group name="ScriptRegistryItem.name" fetch-groups="default" fields="name"
 * @jdo.fetch-group name="ScriptRegistryItem.description" fetch-groups="default" fields="description"
 * @jdo.fetch-group name="ScriptRegistryItem.this" fetch-groups="default, ScriptRegistryItem.name, ScriptRegistryItem.description" fields="parent, name, description"
 * 
 * @jdo.query
 *		name="getTopLevelScriptRegistryItemsByOrganisationID"
 *		query="SELECT
 *			WHERE this.organisationID == paramOrganisationID &&
 *            this.parent == null
 *			PARAMETERS String paramOrganisationID
 *			import java.lang.String"
 * 
 * @jdo.query
 *		name="getTopLevelScriptRegistryItems"
 *		query="SELECT
 *			WHERE this.parent == null
 *			import java.lang.String"
 *
 * @jdo.query
 *		name="getTopLevelScriptRegistryItemsByType"
 *		query="SELECT
 *			WHERE this.parent == null &&
 *						this.scriptRegistryItemType == pScriptRegistryItemType
 *			PARAMETERS String pScriptRegistryItemType
 *			import java.lang.String"
 *
 * @jdo.query
 *		name="getTopLevelScriptRegistryItemsByOrganisationIDAndType"
 *		query="SELECT
 *			WHERE this.parent == null &&
 *						this.organisationID == pOrganisationID &&
 *						this.scriptRegistryItemType == pScriptRegistryItemType
 *			PARAMETERS String pOrganisationID, String pScriptRegistryItemType
 *			import java.lang.String"
 *
 */
public abstract class ScriptRegistryItem implements Serializable, StoreCallback {

    private static final long serialVersionUID = 9221181132208442543L;

    private static final String QUERY_GET_TOPLEVEL_SCRIPT_REGISTRY_ITEMS_BY_ORGANISATION_ID_AND_TYPE = "getTopLevelScriptRegistryItemsByOrganisationIDAndType";

    private static final String QUERY_GET_TOPLEVEL_SCRIPT_REGISTRY_ITEMS_BY_TYPE = "getTopLevelScriptRegistryItemsByType";

    private static final String QUERY_GET_TOPLEVEL_SCRIPT_REGISTRY_ITEMS_BY_ORGANISATION_ID = "getTopLevelScriptRegistryItemsByOrganisationID";

    private static final String QUERY_GET_TOPLEVEL_SCRIPT_REGISTRY_ITEMS = "getTopLevelScriptRegistryItems";

    public static final String FETCH_GROUP_PARENT = "ScriptRegistryItem.parentItem";

    public static final String FETCH_GROUP_NAME = "ScriptRegistryItem.name";

    public static final String FETCH_GROUP_DESCRIPTION = "ScriptRegistryItem.description";

    /**
	 * @deprecated The *.this-FetchGroups lead to bad programming style and are therefore deprecated, now. They should be removed soon! 
	 */
    public static final String FETCH_GROUP_THIS_SCRIPT_REGISTRY_ITEM = "ScriptRegistryItem.this";

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String organisationID;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String scriptRegistryItemType;

    /**
	 * @jdo.field primary-key="true"
	 * @jdo.column length="100"
	 */
    private String scriptRegistryItemID;

    /**
	 * @jdo.field persistence-modifier="persistent"
	 */
    private ScriptCategory parent;

    /**
	 * @jdo.field persistence-modifier="persistent"
	 */
    private ScriptParameterSet parameterSet;

    /**
	 * @jdo.field persistence-modifier="persistent" mapped-by="scriptRegistryItem"
	 */
    private ScriptRegistryItemName name;

    /**
	 * @jdo.field persistence-modifier="persistent"  mapped-by="scriptRegistryItem"
	 */
    private ScriptRegistryItemDescription description;

    /**
	 * @deprecated Only for JDO!
	 */
    @Deprecated
    protected ScriptRegistryItem() {
    }

    /**
	 * Create a new {@link ScriptRegistryItem} with the given primary key parameters.
	 * This is intended to be used as superconstructor of extending classes.
	 * 
	 * @param organisationID The organisationID.
	 * @param scriptRegistryItemType The scriptRegistryItemType.
	 * @param scriptRegistryItemID The scriptRegistryItemID.
	 */
    protected ScriptRegistryItem(String organisationID, String scriptRegistryItemType, String scriptRegistryItemID) {
        this.organisationID = organisationID;
        this.scriptRegistryItemType = scriptRegistryItemType;
        this.scriptRegistryItemID = scriptRegistryItemID;
        this.name = new ScriptRegistryItemName(this);
        this.description = new ScriptRegistryItemDescription(this);
    }

    /**
	 * Returns the String representation of the primary key of the item
	 * represented by the given parameters. It is the "/" separated
	 * concatenation of the given parameters.
	 * 
	 * @param organisationID The organisationID.
	 * @param scriptRegistryItemType The scriptRegistryItemType.
	 * @param scriptRegistryItemID The scriptRegistryItemID.
	 * @return The String representation of the primary key of the item
	 * 		represented by the given parameters.
	 */
    public static String getPrimaryKey(String organisationID, String scriptRegistryItemType, String scriptRegistryItemID) {
        return organisationID + '/' + scriptRegistryItemType + '/' + scriptRegistryItemID;
    }

    /**
	 * Returns the organisationID of this item.
	 * This is part of the primary key.
	 * 
	 * @return The organisationID of this item.
	 */
    public String getOrganisationID() {
        return organisationID;
    }

    /**
	 * Returns the scriptRegistryItemType of this item.
	 * This is part of the primary key.
	 * 
	 * @return The scriptRegistryItemType of this item.
	 */
    public String getScriptRegistryItemType() {
        return scriptRegistryItemType;
    }

    /**
	 * Returns the scriptRegistryItemID of this item.
	 * This is part of the primary key.
	 * 
	 * @return The scriptRegistryItemID of this item.
	 */
    public String getScriptRegistryItemID() {
        return scriptRegistryItemID;
    }

    /**
	 * Returns the parent of this item.
	 * Parents always are {@link ScriptCategory}s.
	 * 
	 * @return The parent of this item.
	 */
    protected ScriptCategory getParent() {
        return parent;
    }

    /**
	 * Sets the parent of this item.
	 * 
	 * @param parent The parent of this item.
	 */
    protected void setParent(ScriptCategory parent) {
        this.parent = parent;
    }

    /**
	 * Returns the {@link ScriptParameterSet} of this
	 * {@link ScriptRegistryItem}. In case this is a
	 * {@link ScriptCategory} this parameter-set is
	 * applied to {@link Script}s created with this as
	 * parent.
	 * 
	 * @return the {@link ScriptParameterSet} of this {@link ScriptRegistryItem}
	 */
    public ScriptParameterSet getParameterSet() {
        return parameterSet;
    }

    /**
	 * Returns the name of this {@link ScriptRegistryItem}.
	 * 
	 * @return The name of this {@link ScriptRegistryItem}.
	 */
    public ScriptRegistryItemName getName() {
        return name;
    }

    /**
	 * Returns the desciption of this {@link ScriptRegistryItem}.
	 * 
	 * @return The desciption of this {@link ScriptRegistryItem}.
	 */
    public ScriptRegistryItemDescription getDescription() {
        return description;
    }

    /**
	 * Set the {@link ScriptParameterSet} of this item.
	 * 
	 * @param parameterSet The parameterSet to set for this item.
	 */
    public void setParameterSet(ScriptParameterSet parameterSet) {
        this.parameterSet = parameterSet;
    }

    /**
	 * Checks for equality of the primary key fields.
	 * @return Whether the primary key fields match.
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ScriptRegistryItem)) return false;
        ScriptRegistryItem other = (ScriptRegistryItem) obj;
        return Util.equals(this.organisationID, other.organisationID) && Util.equals(this.scriptRegistryItemType, other.scriptRegistryItemType) && Util.equals(this.scriptRegistryItemID, other.scriptRegistryItemID);
    }

    /**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return Util.hashCode(organisationID) ^ Util.hashCode(scriptRegistryItemType) ^ Util.hashCode(scriptRegistryItemID);
    }

    /**
	 * Returns all top level (parent == null) ScriptRegistryItems for the given organisationID
	 * If organisation is null the top level registry items
	 * for all organisation are returned.
	 * 
	 * @param pm The PersistenceManager to use.
	 * @param organisationID The organisationID to use.
	 */
    public static Collection getTopScriptRegistryItemsByOrganisationID(PersistenceManager pm, String organisationID) {
        if (organisationID == null || "".equals(organisationID)) return getTopScriptRegistryItems(pm);
        Query q = pm.newNamedQuery(ScriptRegistryItem.class, QUERY_GET_TOPLEVEL_SCRIPT_REGISTRY_ITEMS_BY_ORGANISATION_ID);
        return (Collection) q.execute(organisationID);
    }

    /**
	 * Returns all top level (parent == null) ScriptRegistryItems for all
	 * organisations
	 * 
	 * @param pm The PersistenceManager to use.
	 */
    public static Collection getTopScriptRegistryItems(PersistenceManager pm) {
        Query q = pm.newNamedQuery(ScriptRegistryItem.class, QUERY_GET_TOPLEVEL_SCRIPT_REGISTRY_ITEMS);
        return (Collection) q.execute();
    }

    /**
	 * Returns all top level (parent == null) ScriptRegistryItems with the given
	 * scriptRegistryItemType
	 * 
	 * @param pm The PersistenceManager to use.
	 * @param scriptRegistryItemType the ScriptRegistryItemType to filter
	 */
    public static Collection getTopScriptRegistryItemsByType(PersistenceManager pm, String scriptRegistryItemType) {
        Query q = pm.newNamedQuery(ScriptRegistryItem.class, QUERY_GET_TOPLEVEL_SCRIPT_REGISTRY_ITEMS_BY_TYPE);
        return (Collection) q.execute(scriptRegistryItemType);
    }

    /**
	 * Returns all top level (parent == null) ScriptRegistryItems for the given organisationID
	 * and scriptRegistryItemType
	 * If organisation is null the top level registry items
	 * for all organisation are returned.
	 * 
	 * @param pm The PersistenceManager to use.
	 * @param organisationID The organisationID to use.
	 * @param scriptRegistryItemType the scriptRegistryItemType to use
	 */
    public static Collection getTopScriptRegistryItemsByOrganisationIDAndType(PersistenceManager pm, String organisationID, String scriptRegistryItemType) {
        if (organisationID == null || "".equals(organisationID)) return getTopScriptRegistryItemsByType(pm, scriptRegistryItemType);
        Query q = pm.newNamedQuery(ScriptRegistryItem.class, QUERY_GET_TOPLEVEL_SCRIPT_REGISTRY_ITEMS_BY_ORGANISATION_ID_AND_TYPE);
        return (Collection) q.execute(organisationID, scriptRegistryItemType);
    }

    public void jdoPreStore() {
        if (!JDOHelper.isNew(this)) return;
        PersistenceManager pm = JDOHelper.getPersistenceManager(this);
        if (pm == null) throw new IllegalStateException("Could not get PersistenceManager jdoPreStore()");
        ScriptRegistryItem _parent = getParent();
        if (getParameterSet() == null && parent != null) {
            if (JDOHelper.isDetached(parent)) {
                ScriptRegistryItem pParent = null;
                boolean setFromPersistent = false;
                try {
                    pParent = (ScriptRegistryItem) pm.getObjectById(JDOHelper.getObjectId(parent));
                    setFromPersistent = true;
                } catch (JDOObjectNotFoundException e) {
                }
                if (setFromPersistent) {
                    setParameterSet(pParent.getParameterSet());
                } else {
                    try {
                        this.setParameterSet(_parent.getParameterSet());
                    } catch (JDODetachedFieldAccessException e) {
                        Logger.getLogger(ScriptRegistryItem.class).error("Could not set the parameterSet initially from null to the parents one");
                    }
                }
            }
        }
    }
}
