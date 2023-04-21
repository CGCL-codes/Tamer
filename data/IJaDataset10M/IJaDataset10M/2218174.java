package com.volantis.mcs.repository;

import com.volantis.mcs.accessors.common.AssetAccessor;
import com.volantis.mcs.accessors.common.ComponentAccessor;
import com.volantis.mcs.assets.LinkAsset;
import com.volantis.mcs.assets.LinkAssetIdentity;
import com.volantis.mcs.components.LinkComponent;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.synergetics.cache.GenericCache;
import java.util.Vector;

/**
 * This class provides the external interface to the management of
 * link components and assets within the repository.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @deprecated Use {@link com.volantis.mcs.project.PolicyManager}.
 *             This was deprecated in version 3.5.1.
 */
public final class LinkRepositoryManager extends RepositoryManager {

    /**
     * The type of policies managed by this class.
     */
    private static final PolicyType POLICY_TYPE = PolicyType.LINK;

    /**
   * The object that provides access to link components.
   */
    private final ComponentAccessor componentAccessor;

    /**
   * The object that provides access to link assets.
   */
    private final AssetAccessor assetAccessor;

    /**
   * Set the component cache.
   * @param cache GenericCache to use
   */
    public void setComponentCache(GenericCache cache) {
    }

    /**
   * Set the asset cache.
   * @param cache GenericCache to use
   */
    public void setAssetCache(GenericCache cache) {
        throw new UnsupportedOperationException();
    }

    /**
   * Refresh the component cache.
   */
    public void refreshComponentCache() {
        flushCache(POLICY_TYPE);
    }

    /**
   * Refresh the asset cache.
   */
    public void refreshAssetCache() {
        throw new UnsupportedOperationException();
    }

    /**
   * Creates a new <code>LinkRepositoryManager</code> instance.
   *
   * @param connection a <code>RepositoryConnection</code> value
   */
    public LinkRepositoryManager(RepositoryConnection connection) {
        this(connection.getRepository());
    }

    /**
     * Creates a new <code>LinkRepositoryManager</code> instance.
     *
     * @param repository a <code>Repository</code> value
     */
    public LinkRepositoryManager(Repository repository) {
        this(repository, null, null);
    }

    /**
     * Initializes a new <code>LinkRepositoryManager</code> instance.
     *
     * @param repository         the <code>Repository</code> to be used
     * @param project            the default project.
     * @param policyCacheFlusher allows manager to flush policy caches if used
     *                           at runtime.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    public LinkRepositoryManager(Repository repository, Project project, DeprecatedPolicyCacheFlusher policyCacheFlusher) {
        super(repository, project, policyCacheFlusher);
        componentAccessor = new ComponentAccessor(accessor, defaultProject, POLICY_TYPE);
        assetAccessor = new AssetAccessor(accessor, LinkAsset.class, defaultProject);
    }

    /**
   * Add the specified link component to the repository.
   * @param connection a connection to the repository
   * @param linkComponent the LinkComponent object that is to be added to
   * the repository
   * @exception RepositoryException if an error occurs
   */
    public void addLinkComponent(RepositoryConnection connection, LinkComponent linkComponent) throws RepositoryException {
        componentAccessor.addObject(getConnection(connection), linkComponent);
    }

    /**
   * Remove the specified link component from the repository.
   * @param connection a connection to the repository
   * @param name  the name of the LinkComponent that is to be
   * removed from the repository
   * @exception RepositoryException if an error occurs
   */
    public void removeLinkComponent(RepositoryConnection connection, String name) throws RepositoryException {
        componentAccessor.removeObject(getConnection(connection), name);
    }

    /**
   * Retrieve the specified link component from the repository.
   * @param connection a connection to the repository
   * @param name  the name of the LinkComponent that is to be
   * retrieved from the repository
   * @return the LinkComponent that was retrieved from the repository or null
   * if no match was found.
   * @exception RepositoryException if an error occurs
   */
    public LinkComponent retrieveLinkComponent(RepositoryConnection connection, String name) throws RepositoryException {
        return (LinkComponent) componentAccessor.retrieveObject(getConnection(connection), name);
    }

    /**
   * Rename a link component
   * @param connection a connection to the repository
   * @param name the current name of the link component
   * @param newName the new name for the link component
   * @exception RepositoryException if an error occurs
   */
    public void renameLinkComponent(RepositoryConnection connection, String name, String newName) throws RepositoryException {
        componentAccessor.renameObject(getConnection(connection), name, newName);
    }

    /**
   * copy the fields from one link component to another link component
   * @param connection a connection to the repository
   * @param name the name of link component to copy from
   * @param newName the name of the link component to copy to
   * @exception RepositoryException if an error occurs
   *
   * @deprecated No longer supported, throws exception.
   */
    public void copyLinkComponent(RepositoryConnection connection, String name, String newName) throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    /**
   * Retrieve all the link components in the repository, put them into
   * a Vector and return the Vector.
   * @param connection a connection to the repository
   * @return a Vector containing all the LinkComponent objects available
   * from the Repository
   * @exception RepositoryException if an error occurs
   */
    public Vector retrieveAllLinkComponents(RepositoryConnection connection) throws RepositoryException {
        return new Vector(componentAccessor.retrieveAllComponents(getConnection(connection)));
    }

    /**
   * Return an enumeration of names for all linkcomponents in the repository
   * @param connection a connection to the repository
   * @return the enumeration of names
   * @exception RepositoryException if an error occurs
   */
    public RepositoryEnumeration enumerateLinkComponentNames(RepositoryConnection connection) throws RepositoryException {
        return componentAccessor.enumerateComponentNames(getConnection(connection));
    }

    /**
   * Lock a link component.
   * @param connection a connection to the repository
   * @param name the name of the link component from the lock...
   * @exception RepositoryException if an error occurs
   *
   * @deprecated Policies can no longer be locked.
   */
    public void lockLinkComponent(RepositoryConnection connection, String name) throws RepositoryException {
    }

    /**
   * Unlock a link component.
   * @param connection a connection to the repository
   * @param name the name of the link component to unlock
   * @exception RepositoryException if an error occurs
   *
   * @deprecated Policies can no longer be locked.
   */
    public void unlockLinkComponent(RepositoryConnection connection, String name) throws RepositoryException {
    }

    /**
   * Add the specified link asset to the repository.
   * @param connection a connection to the repository
   * @param linkAsset the link asset that is to be added to the repository
   * @exception RepositoryException if an error occurs
   */
    public void addLinkAsset(RepositoryConnection connection, LinkAsset linkAsset) throws RepositoryException {
        assetAccessor.addObject(getConnection(connection), linkAsset);
    }

    /**
   * remove the specified link asset from the repository.
   * @param connection a connection to the repository
   * @param name the name of the associated link component
   * @param deviceName name of the associated device
   * @exception RepositoryException if an error occurs
   */
    public void removeLinkAsset(RepositoryConnection connection, String name, String deviceName) throws RepositoryException {
        LinkAssetIdentity identity = new LinkAssetIdentity(defaultProject, name, deviceName);
        assetAccessor.removeObject(getConnection(connection), identity);
    }

    /**
   * Retrieve the specified link asset from the repository
   * @param connection a connection to the repository
   * @param name the name of the associated link component
   * @param deviceName name of the associated device
   * @return the link asset object from the repository
   * @exception RepositoryException if an error occurs
   */
    public LinkAsset retrieveLinkAsset(RepositoryConnection connection, String name, String deviceName) throws RepositoryException {
        LinkAssetIdentity identity = new LinkAssetIdentity(defaultProject, name, deviceName);
        return (LinkAsset) assetAccessor.retrieveObject(getConnection(connection), identity);
    }

    /**
   * Rename the specified link asset entry
   * @param connection a connection to the repository
   * @param name the current name of the link asset
   * @param deviceName name of the associated device
   * @param newName the new name for the link asset
   * @exception RepositoryException if an error occurs
   */
    public void renameLinkAsset(RepositoryConnection connection, String name, String deviceName, String newName) throws RepositoryException {
        LinkAssetIdentity identity = new LinkAssetIdentity(defaultProject, name, deviceName);
        assetAccessor.moveAsset(getConnection(connection), identity, newName);
    }

    /**
   * copy the fields of a specified link asset entry to another
   * link asset entry
   * @param connection a connection to the repository
   * @param name the  name of the link asset to copy from
   * @param deviceName name of the associated device
   * @param newName the name for the link asset to copy to
   * @exception RepositoryException if an error occurs
   *
   * @deprecated No longer supported, throws exception.
   */
    public void copyLinkAsset(RepositoryConnection connection, String name, String deviceName, String newName) throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    /**
   * delete all link assets associated with a given link component
   * @param connection a connection to the repository
   * @param name name of the associated link component
   * @exception RepositoryException if an error occurs
   */
    public void removeComponentLinkAssets(RepositoryConnection connection, String name) throws RepositoryException {
        assetAccessor.removeChildren(getConnection(connection), name);
    }

    /**
   * Retrieve all the assets for the specified link component from the
   * repository.
   * @param connection a connection to the repository
   * @param name name of the associated link component.
   * @return a Vector containing the LinkComponents retrieved from the
   * repository
   * @exception RepositoryException if an error occurs
   */
    public Vector retrieveComponentLinkAssets(RepositoryConnection connection, String name) throws RepositoryException {
        return new Vector(assetAccessor.retrieveChildren(getConnection(connection), name));
    }

    /**
   * Rename all link assets for a given name
   * @param connection a connection to the repository
   * @param name the current name of the link assets
   * @param newName the new name for the link assets
   * @exception RepositoryException if an error occurs
   */
    public void renameComponentLinkAssets(RepositoryConnection connection, String name, String newName) throws RepositoryException {
        assetAccessor.moveAssets(getConnection(connection), name, newName);
    }

    /**
   * copy the fields of a specified link asset entry to another
   * link asset entry
   * @param connection a connection to the repository
   * @param name the  name of the link asset to copy from
   * @param newName the name for the link asset to copy to
   * @exception RepositoryException if an error occurs
   *
   * @deprecated No longer supported, throws exception.
   */
    public void copyComponentLinkAssets(RepositoryConnection connection, String name, String newName) throws RepositoryException {
        throw new UnsupportedOperationException();
    }
}
