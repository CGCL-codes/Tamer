package org.openuss.wiki;

/**
 * @see org.openuss.wiki.WikiSiteVersion
 */
public interface WikiSiteVersionDao {

    /**
     * This constant is used as a transformation flag; entities can be converted automatically into value objects
     * or other types, different methods in a class implementing this interface support this feature: look for
     * an <code>int</code> parameter called <code>transform</code>.
     * <p/>
     * This specific flag denotes no transformation will occur.
     */
    public static final int TRANSFORM_NONE = 0;

    /**
     * This constant is used as a transformation flag; entities can be converted automatically into value objects
     * or other types, different methods in a class implementing this interface support this feature: look for
     * an <code>int</code> parameter called <code>transform</code>.
     * <p/>
     * This specific flag denotes entities must be transformed into objects of type
     * {@link org.openuss.wiki.WikiSiteContentInfo}.
     */
    public static final int TRANSFORM_WIKISITECONTENTINFO = 1;

    /**
     * Copies the fields of the specified entity to the target value object. This method is similar to
     * toWikiSiteContentInfo(), but it does not handle any attributes in the target
     * value object that are "read-only" (as those do not have setter methods exposed).
     */
    public void toWikiSiteContentInfo(org.openuss.wiki.WikiSiteVersion sourceEntity, org.openuss.wiki.WikiSiteContentInfo targetVO);

    /**
     * Converts this DAO's entity to an object of type {@link org.openuss.wiki.WikiSiteContentInfo}.
     */
    public org.openuss.wiki.WikiSiteContentInfo toWikiSiteContentInfo(org.openuss.wiki.WikiSiteVersion entity);

    /**
     * Converts this DAO's entity to a Collection of instances of type {@link org.openuss.wiki.WikiSiteContentInfo}.
     */
    public void toWikiSiteContentInfoCollection(java.util.Collection entities);

    /**
     * Copies the fields of {@link org.openuss.wiki.WikiSiteContentInfo} to the specified entity.
     * @param copyIfNull If FALSE, the value object's field will not be copied to the entity if the value is NULL. If TRUE,
     * it will be copied regardless of its value.
     */
    public void wikiSiteContentInfoToEntity(org.openuss.wiki.WikiSiteContentInfo sourceVO, org.openuss.wiki.WikiSiteVersion targetEntity, boolean copyIfNull);

    /**
     * Converts an instance of type {@link org.openuss.wiki.WikiSiteContentInfo} to this DAO's entity.
     */
    public org.openuss.wiki.WikiSiteVersion wikiSiteContentInfoToEntity(org.openuss.wiki.WikiSiteContentInfo wikiSiteContentInfo);

    /**
     * Converts a Collection of instances of type {@link org.openuss.wiki.WikiSiteContentInfo} to this
     * DAO's entity.
     */
    public void wikiSiteContentInfoToEntityCollection(java.util.Collection instances);

    /**
     * This constant is used as a transformation flag; entities can be converted automatically into value objects
     * or other types, different methods in a class implementing this interface support this feature: look for
     * an <code>int</code> parameter called <code>transform</code>.
     * <p/>
     * This specific flag denotes entities must be transformed into objects of type
     * {@link org.openuss.wiki.WikiSiteInfo}.
     */
    public static final int TRANSFORM_WIKISITEINFO = 2;

    /**
     * Copies the fields of the specified entity to the target value object. This method is similar to
     * toWikiSiteInfo(), but it does not handle any attributes in the target
     * value object that are "read-only" (as those do not have setter methods exposed).
     */
    public void toWikiSiteInfo(org.openuss.wiki.WikiSiteVersion sourceEntity, org.openuss.wiki.WikiSiteInfo targetVO);

    /**
     * Converts this DAO's entity to an object of type {@link org.openuss.wiki.WikiSiteInfo}.
     */
    public org.openuss.wiki.WikiSiteInfo toWikiSiteInfo(org.openuss.wiki.WikiSiteVersion entity);

    /**
     * Converts this DAO's entity to a Collection of instances of type {@link org.openuss.wiki.WikiSiteInfo}.
     */
    public void toWikiSiteInfoCollection(java.util.Collection entities);

    /**
     * Copies the fields of {@link org.openuss.wiki.WikiSiteInfo} to the specified entity.
     * @param copyIfNull If FALSE, the value object's field will not be copied to the entity if the value is NULL. If TRUE,
     * it will be copied regardless of its value.
     */
    public void wikiSiteInfoToEntity(org.openuss.wiki.WikiSiteInfo sourceVO, org.openuss.wiki.WikiSiteVersion targetEntity, boolean copyIfNull);

    /**
     * Converts an instance of type {@link org.openuss.wiki.WikiSiteInfo} to this DAO's entity.
     */
    public org.openuss.wiki.WikiSiteVersion wikiSiteInfoToEntity(org.openuss.wiki.WikiSiteInfo wikiSiteInfo);

    /**
     * Converts a Collection of instances of type {@link org.openuss.wiki.WikiSiteInfo} to this
     * DAO's entity.
     */
    public void wikiSiteInfoToEntityCollection(java.util.Collection instances);

    /**
     * Loads an instance of org.openuss.wiki.WikiSiteVersion from the persistent store.
     */
    public org.openuss.wiki.WikiSiteVersion load(java.lang.Long id);

    /**
     * <p>
     * Does the same thing as {@link #load(java.lang.Long)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined in this class then the result <strong>WILL BE</strong> passed through an operation which can
     * optionally transform the entity (into a value object for example). By default, transformation does
     * not occur.
     * </p>
     *
     * @param id the identifier of the entity to load.
     * @return either the entity or the object transformed from the entity.
     */
    public Object load(int transform, java.lang.Long id);

    /**
     * Loads all entities of type {@link org.openuss.wiki.WikiSiteVersion}.
     *
     * @return the loaded entities.
     */
    public java.util.Collection<org.openuss.wiki.WikiSiteVersion> loadAll();

    /**
     * <p>
     * Does the same thing as {@link #loadAll()} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entity (into a value object for example). By default, transformation does
     * not occur.
     * </p>
     *
     * @param transform the flag indicating what transformation to use.
     * @return the loaded entities.
     */
    public java.util.Collection loadAll(final int transform);

    /**
     * Creates an instance of org.openuss.wiki.WikiSiteVersion and adds it to the persistent store.
     */
    public org.openuss.wiki.WikiSiteVersion create(org.openuss.wiki.WikiSiteVersion wikiSiteVersion);

    /**
     * <p>
     * Does the same thing as {@link #create(org.openuss.wiki.WikiSiteVersion)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entity (into a value object for example). By default, transformation does
     * not occur.
     * </p>
     */
    public Object create(int transform, org.openuss.wiki.WikiSiteVersion wikiSiteVersion);

    /**
     * Creates a new instance of org.openuss.wiki.WikiSiteVersion and adds
     * from the passed in <code>entities</code> collection
     *
     * @param entities the collection of org.openuss.wiki.WikiSiteVersion
     * instances to create.
     *
     * @return the created instances.
     */
    public java.util.Collection<org.openuss.wiki.WikiSiteVersion> create(java.util.Collection<org.openuss.wiki.WikiSiteVersion> entities);

    /**
     * <p>
     * Does the same thing as {@link #create(org.openuss.wiki.WikiSiteVersion)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entities (into value objects for example). By default, transformation does
     * not occur.
     * </p>
     */
    public java.util.Collection create(int transform, java.util.Collection<org.openuss.wiki.WikiSiteVersion> entities);

    /**
     * Updates the <code>wikiSiteVersion</code> instance in the persistent store.
     */
    public void update(org.openuss.wiki.WikiSiteVersion wikiSiteVersion);

    /**
     * Updates all instances in the <code>entities</code> collection in the persistent store.
     */
    public void update(java.util.Collection<org.openuss.wiki.WikiSiteVersion> entities);

    /**
     * Removes the instance of org.openuss.wiki.WikiSiteVersion from the persistent store.
     */
    public void remove(org.openuss.wiki.WikiSiteVersion wikiSiteVersion);

    /**
     * Removes the instance of org.openuss.wiki.WikiSiteVersion having the given
     * <code>identifier</code> from the persistent store.
     */
    public void remove(java.lang.Long id);

    /**
     * Removes all entities in the given <code>entities<code> collection.
     */
    public void remove(java.util.Collection<org.openuss.wiki.WikiSiteVersion> entities);

    /**
 * <p>
 * finds all WikiSiteVersions for a WikiSite
 * </p>
 * <p>
 * @param wikiSite the wikiSite the Versions are searched for
 * </p>
 * <p>
 * @return a List of WikiSiteVersions for the WikiSite
 * </p>
     */
    public java.util.List findByWikiSite(org.openuss.wiki.WikiSite wikiSite);

    /**
     * <p>
     * Does the same thing as {@link #findByWikiSite(org.openuss.wiki.WikiSite)} with an
     * additional argument called <code>queryString</code>. This <code>queryString</code>
     * argument allows you to override the query string defined in {@link #findByWikiSite(org.openuss.wiki.WikiSite)}.
     * </p>
     */
    public java.util.List findByWikiSite(String queryString, org.openuss.wiki.WikiSite wikiSite);

    /**
     * <p>
     * Does the same thing as {@link #findByWikiSite(org.openuss.wiki.WikiSite)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * finder results will <strong>NOT</strong> be transformed during retrieval.
     * If this flag is any of the other constants defined here
     * then finder results <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entities (into value objects for example). By default, transformation does
     * not occur.
     * </p>
     */
    public java.util.List findByWikiSite(int transform, org.openuss.wiki.WikiSite wikiSite);

    /**
     * <p>
     * Does the same thing as {@link #findByWikiSite(boolean, org.openuss.wiki.WikiSite)} with an
     * additional argument called <code>queryString</code>. This <code>queryString</code>
     * argument allows you to override the query string defined in {@link #findByWikiSite(int, org.openuss.wiki.WikiSite wikiSite)}.
     * </p>
     */
    public java.util.List findByWikiSite(int transform, String queryString, org.openuss.wiki.WikiSite wikiSite);

    /**
 * 
     */
    public java.util.List findByAuthor(org.openuss.security.User author);

    /**
     * <p>
     * Does the same thing as {@link #findByAuthor(org.openuss.security.User)} with an
     * additional argument called <code>queryString</code>. This <code>queryString</code>
     * argument allows you to override the query string defined in {@link #findByAuthor(org.openuss.security.User)}.
     * </p>
     */
    public java.util.List findByAuthor(String queryString, org.openuss.security.User author);

    /**
     * <p>
     * Does the same thing as {@link #findByAuthor(org.openuss.security.User)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * finder results will <strong>NOT</strong> be transformed during retrieval.
     * If this flag is any of the other constants defined here
     * then finder results <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entities (into value objects for example). By default, transformation does
     * not occur.
     * </p>
     */
    public java.util.List findByAuthor(int transform, org.openuss.security.User author);

    /**
     * <p>
     * Does the same thing as {@link #findByAuthor(boolean, org.openuss.security.User)} with an
     * additional argument called <code>queryString</code>. This <code>queryString</code>
     * argument allows you to override the query string defined in {@link #findByAuthor(int, org.openuss.security.User author)}.
     * </p>
     */
    public java.util.List findByAuthor(int transform, String queryString, org.openuss.security.User author);
}
