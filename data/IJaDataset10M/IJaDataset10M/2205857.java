package org.didicero.base.entity;

/**
 * @see org.didicero.base.entity.ValidatedQuestion
 */
public interface ValidatedQuestionDao {

    /**
     * This constant is used as a transformation flag; entities can be converted automatically into value objects
     * or other types, different methods in a class implementing this interface support this feature: look for
     * an <code>int</code> parameter called <code>transform</code>.
     * <p/>
     * This specific flag denotes no transformation will occur.
     */
    public static final int TRANSFORM_NONE = 0;

    /**
     * Loads an instance of org.didicero.base.entity.ValidatedQuestion from the persistent store.
     * @param id the identifier of the entity to load.
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public org.didicero.base.entity.ValidatedQuestion load(java.lang.Long id) throws org.didicero.base.entity.ValidatedQuestionDaoException;

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
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public Object load(int transform, java.lang.Long id) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * Loads all entities of type {@link org.didicero.base.entity.ValidatedQuestion}.
     *
     * @return the loaded entities.
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public java.util.Collection<org.didicero.base.entity.ValidatedQuestion> loadAll() throws org.didicero.base.entity.ValidatedQuestionDaoException;

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
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public java.util.Collection loadAll(final int transform) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * Creates an instance of org.didicero.base.entity.ValidatedQuestion and adds it to the persistent store.
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public org.didicero.base.entity.ValidatedQuestion create(org.didicero.base.entity.ValidatedQuestion validatedQuestion) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #create(org.didicero.base.entity.ValidatedQuestion)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public Object create(int transform, org.didicero.base.entity.ValidatedQuestion validatedQuestion) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * Creates a new instance of org.didicero.base.entity.ValidatedQuestion and adds
     * from the passed in <code>entities</code> collection
     *
     * @param entities the collection of org.didicero.base.entity.ValidatedQuestion
     *        instances to create.
     *
     * @return the created instances.
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public java.util.Collection<org.didicero.base.entity.ValidatedQuestion> create(java.util.Collection<org.didicero.base.entity.ValidatedQuestion> entities) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #create(org.didicero.base.entity.ValidatedQuestion)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entities (into value objects for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public java.util.Collection create(int transform, java.util.Collection<org.didicero.base.entity.ValidatedQuestion> entities) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * <p>
     *   Creates a new <code>org.didicero.base.entity.ValidatedQuestion</code>
     *   instance from <strong>all</strong> attributes and adds it to
     *   the persistent store.
     * </p>
     */
    public org.didicero.base.entity.ValidatedQuestion create(float reachedPoints, java.lang.String comment) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #create(float, java.lang.String)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong> be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public Object create(int transform, float reachedPoints, java.lang.String comment) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * <p>
     *  Creates a new <code>org.didicero.base.entity.ValidatedQuestion</code>
     *  instance from only <strong>required</strong> properties (attributes
     *  and association ends) and adds it to the persistent store.
     * </p>
     *
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public org.didicero.base.entity.ValidatedQuestion create(java.lang.String comment, float reachedPoints, org.didicero.base.entity.TestQuestion validationsQuest) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * <p>
     *   Does the same thing as {@link #create(float, java.lang.String)} with an
     *   additional flag called <code>transform</code>.  If this flag is set to <code>TRANSFORM_NONE</code> then
     *   the returned entity will <strong>NOT</strong be transformed.  If this flag is any of the other constants
     *   defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     *   transform the entity (into a value object for example).  By default, transformation does
     *   not occur.
     * </p>
     *
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public Object create(int transform, java.lang.String comment, float reachedPoints, org.didicero.base.entity.TestQuestion validationsQuest) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * Updates the <code>validatedQuestion</code> instance in the persistent store.
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public void update(org.didicero.base.entity.ValidatedQuestion validatedQuestion) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * Updates all instances in the <code>entities</code> collection in the persistent store.
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public void update(java.util.Collection<org.didicero.base.entity.ValidatedQuestion> entities) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * Removes the instance of org.didicero.base.entity.ValidatedQuestion from the persistent store.
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public void remove(org.didicero.base.entity.ValidatedQuestion validatedQuestion) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * Removes the instance of org.didicero.base.entity.ValidatedQuestion having the given
     * <code>identifier</code> from the persistent store.
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public void remove(java.lang.Long id) throws org.didicero.base.entity.ValidatedQuestionDaoException;

    /**
     * Removes all entities in the given <code>entities<code> collection.
     * @throws org.didicero.base.entity.ValidatedQuestionDaoException
     */
    public void remove(java.util.Collection<org.didicero.base.entity.ValidatedQuestion> entities) throws org.didicero.base.entity.ValidatedQuestionDaoException;
}
