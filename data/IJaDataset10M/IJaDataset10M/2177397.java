package org.didicero.base.entity;

/**
 * @see org.didicero.base.entity.MaterialClip
 */
public interface MaterialClipDao {

    /**
     * This constant is used as a transformation flag; entities can be converted automatically into value objects
     * or other types, different methods in a class implementing this interface support this feature: look for
     * an <code>int</code> parameter called <code>transform</code>.
     * <p/>
     * This specific flag denotes no transformation will occur.
     */
    public static final int TRANSFORM_NONE = 0;

    /**
     * Loads an instance of org.didicero.base.entity.MaterialClip from the persistent store.
     * @param id the identifier of the entity to load.
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public org.didicero.base.entity.MaterialClip load(java.lang.Long id) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #load(java.lang.Long)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined in this class then the result <strong>WILL BE</strong> passed through an operation which can
     *   optionally transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @param id the identifier of the entity to load.
     * @return either the entity or the object transformed from the entity.
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public Object load(int transform, java.lang.Long id) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * Loads all entities of type {@link org.didicero.base.entity.MaterialClip}.
     *
     * @return the loaded entities.
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public java.util.Collection<org.didicero.base.entity.MaterialClip> loadAll() throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #loadAll()} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @param transform the flag indicating what transformation to use.
     * @return the loaded entities.
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public java.util.Collection loadAll(final int transform) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * Creates an instance of org.didicero.base.entity.MaterialClip and adds it to the persistent store.
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public org.didicero.base.entity.MaterialClip create(org.didicero.base.entity.MaterialClip materialClip) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #create(org.didicero.base.entity.MaterialClip)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public Object create(int transform, org.didicero.base.entity.MaterialClip materialClip) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * Creates a new instance of org.didicero.base.entity.MaterialClip and adds
     * from the passed in <code>entities</code> collection
     *
     * @param entities the collection of org.didicero.base.entity.MaterialClip
     *        instances to create.
     *
     * @return the created instances.
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public java.util.Collection<org.didicero.base.entity.MaterialClip> create(java.util.Collection<org.didicero.base.entity.MaterialClip> entities) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #create(org.didicero.base.entity.MaterialClip)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entities (into value objects for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public java.util.Collection create(int transform, java.util.Collection<org.didicero.base.entity.MaterialClip> entities) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * <p>
     *   Creates a new <code>org.didicero.base.entity.MaterialClip</code>
     *   instance from <strong>all</strong> attributes and adds it to
     *   the persistent store.
     * </p>
     */
    public org.didicero.base.entity.MaterialClip create(java.lang.String clipname) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #create(java.lang.String)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public Object create(int transform, java.lang.String clipname) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * Updates the <code>materialClip</code> instance in the persistent store.
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public void update(org.didicero.base.entity.MaterialClip materialClip) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * Updates all instances in the <code>entities</code> collection in the persistent store.
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public void update(java.util.Collection<org.didicero.base.entity.MaterialClip> entities) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * Removes the instance of org.didicero.base.entity.MaterialClip from the persistent store.
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public void remove(org.didicero.base.entity.MaterialClip materialClip) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * Removes the instance of org.didicero.base.entity.MaterialClip having the given
     * <code>identifier</code> from the persistent store.
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public void remove(java.lang.Long id) throws org.didicero.base.entity.MaterialClipDaoException;

    /**
     * Removes all entities in the given <code>entities<code> collection.
     * @throws org.didicero.base.entity.MaterialClipDaoException
     */
    public void remove(java.util.Collection<org.didicero.base.entity.MaterialClip> entities) throws org.didicero.base.entity.MaterialClipDaoException;
}
