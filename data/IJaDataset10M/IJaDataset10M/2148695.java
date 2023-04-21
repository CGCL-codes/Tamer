package org.openuss.lecture;

/**
 * @see org.openuss.lecture.University
 */
public interface UniversityDao {

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
     * {@link org.openuss.lecture.UniversityInfo}.
     */
    public static final int TRANSFORM_UNIVERSITYINFO = 1;

    /**
     * Copies the fields of the specified entity to the target value object. This method is similar to
     * toUniversityInfo(), but it does not handle any attributes in the target
     * value object that are "read-only" (as those do not have setter methods exposed).
     */
    public void toUniversityInfo(org.openuss.lecture.University sourceEntity, org.openuss.lecture.UniversityInfo targetVO);

    /**
     * Converts this DAO's entity to an object of type {@link org.openuss.lecture.UniversityInfo}.
     */
    public org.openuss.lecture.UniversityInfo toUniversityInfo(org.openuss.lecture.University entity);

    /**
     * Converts this DAO's entity to a Collection of instances of type {@link org.openuss.lecture.UniversityInfo}.
     */
    public void toUniversityInfoCollection(java.util.Collection entities);

    /**
     * Copies the fields of {@link org.openuss.lecture.UniversityInfo} to the specified entity.
     * @param copyIfNull If FALSE, the value object's field will not be copied to the entity if the value is NULL. If TRUE,
     * it will be copied regardless of its value.
     */
    public void universityInfoToEntity(org.openuss.lecture.UniversityInfo sourceVO, org.openuss.lecture.University targetEntity, boolean copyIfNull);

    /**
     * Converts an instance of type {@link org.openuss.lecture.UniversityInfo} to this DAO's entity.
     */
    public org.openuss.lecture.University universityInfoToEntity(org.openuss.lecture.UniversityInfo universityInfo);

    /**
     * Converts a Collection of instances of type {@link org.openuss.lecture.UniversityInfo} to this
     * DAO's entity.
     */
    public void universityInfoToEntityCollection(java.util.Collection instances);

    /**
     * Loads an instance of org.openuss.lecture.University from the persistent store.
     */
    public org.openuss.lecture.University load(java.lang.Long id);

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
     * Loads all entities of type {@link org.openuss.lecture.University}.
     *
     * @return the loaded entities.
     */
    public java.util.Collection<org.openuss.lecture.University> loadAll();

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
     * Creates an instance of org.openuss.lecture.University and adds it to the persistent store.
     */
    public org.openuss.lecture.University create(org.openuss.lecture.University university);

    /**
     * <p>
     * Does the same thing as {@link #create(org.openuss.lecture.University)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entity (into a value object for example). By default, transformation does
     * not occur.
     * </p>
     */
    public Object create(int transform, org.openuss.lecture.University university);

    /**
     * Creates a new instance of org.openuss.lecture.University and adds
     * from the passed in <code>entities</code> collection
     *
     * @param entities the collection of org.openuss.lecture.University
     * instances to create.
     *
     * @return the created instances.
     */
    public java.util.Collection<org.openuss.lecture.University> create(java.util.Collection<org.openuss.lecture.University> entities);

    /**
     * <p>
     * Does the same thing as {@link #create(org.openuss.lecture.University)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entities (into value objects for example). By default, transformation does
     * not occur.
     * </p>
     */
    public java.util.Collection create(int transform, java.util.Collection<org.openuss.lecture.University> entities);

    /**
     * Updates the <code>university</code> instance in the persistent store.
     */
    public void update(org.openuss.lecture.University university);

    /**
     * Updates all instances in the <code>entities</code> collection in the persistent store.
     */
    public void update(java.util.Collection<org.openuss.lecture.University> entities);

    /**
     * Removes the instance of org.openuss.lecture.University from the persistent store.
     */
    public void remove(org.openuss.lecture.University university);

    /**
     * Removes the instance of org.openuss.lecture.University having the given
     * <code>identifier</code> from the persistent store.
     */
    public void remove(java.lang.Long id);

    /**
     * Removes all entities in the given <code>entities<code> collection.
     */
    public void remove(java.util.Collection<org.openuss.lecture.University> entities);

    /**
 * 
     */
    public java.util.List findByEnabled(boolean enabled);

    /**
     * <p>
     * Does the same thing as {@link #findByEnabled(boolean)} with an
     * additional argument called <code>queryString</code>. This <code>queryString</code>
     * argument allows you to override the query string defined in {@link #findByEnabled(boolean)}.
     * </p>
     */
    public java.util.List findByEnabled(String queryString, boolean enabled);

    /**
     * <p>
     * Does the same thing as {@link #findByEnabled(boolean)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * finder results will <strong>NOT</strong> be transformed during retrieval.
     * If this flag is any of the other constants defined here
     * then finder results <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entities (into value objects for example). By default, transformation does
     * not occur.
     * </p>
     */
    public java.util.List findByEnabled(int transform, boolean enabled);

    /**
     * <p>
     * Does the same thing as {@link #findByEnabled(boolean, boolean)} with an
     * additional argument called <code>queryString</code>. This <code>queryString</code>
     * argument allows you to override the query string defined in {@link #findByEnabled(int, boolean enabled)}.
     * </p>
     */
    public java.util.List findByEnabled(int transform, String queryString, boolean enabled);

    /**
 * 
     */
    public java.util.List findByTypeAndEnabled(org.openuss.lecture.UniversityType universityType, boolean enabled);

    /**
     * <p>
     * Does the same thing as {@link #findByTypeAndEnabled(org.openuss.lecture.UniversityType, boolean)} with an
     * additional argument called <code>queryString</code>. This <code>queryString</code>
     * argument allows you to override the query string defined in {@link #findByTypeAndEnabled(org.openuss.lecture.UniversityType, boolean)}.
     * </p>
     */
    public java.util.List findByTypeAndEnabled(String queryString, org.openuss.lecture.UniversityType universityType, boolean enabled);

    /**
     * <p>
     * Does the same thing as {@link #findByTypeAndEnabled(org.openuss.lecture.UniversityType, boolean)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * finder results will <strong>NOT</strong> be transformed during retrieval.
     * If this flag is any of the other constants defined here
     * then finder results <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entities (into value objects for example). By default, transformation does
     * not occur.
     * </p>
     */
    public java.util.List findByTypeAndEnabled(int transform, org.openuss.lecture.UniversityType universityType, boolean enabled);

    /**
     * <p>
     * Does the same thing as {@link #findByTypeAndEnabled(boolean, org.openuss.lecture.UniversityType, boolean)} with an
     * additional argument called <code>queryString</code>. This <code>queryString</code>
     * argument allows you to override the query string defined in {@link #findByTypeAndEnabled(int, org.openuss.lecture.UniversityType universityType, boolean enabled)}.
     * </p>
     */
    public java.util.List findByTypeAndEnabled(int transform, String queryString, org.openuss.lecture.UniversityType universityType, boolean enabled);

    /**
 * 
     */
    public org.openuss.lecture.University findByShortcut(java.lang.String shortcut);

    /**
     * <p>
     * Does the same thing as {@link #findByShortcut(java.lang.String)} with an
     * additional argument called <code>queryString</code>. This <code>queryString</code>
     * argument allows you to override the query string defined in {@link #findByShortcut(java.lang.String)}.
     * </p>
     */
    public org.openuss.lecture.University findByShortcut(String queryString, java.lang.String shortcut);

    /**
     * <p>
     * Does the same thing as {@link #findByShortcut(java.lang.String)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * finder results will <strong>NOT</strong> be transformed during retrieval.
     * If this flag is any of the other constants defined here
     * then finder results <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entities (into value objects for example). By default, transformation does
     * not occur.
     * </p>
     */
    public Object findByShortcut(int transform, java.lang.String shortcut);

    /**
     * <p>
     * Does the same thing as {@link #findByShortcut(boolean, java.lang.String)} with an
     * additional argument called <code>queryString</code>. This <code>queryString</code>
     * argument allows you to override the query string defined in {@link #findByShortcut(int, java.lang.String shortcut)}.
     * </p>
     */
    public Object findByShortcut(int transform, String queryString, java.lang.String shortcut);
}
