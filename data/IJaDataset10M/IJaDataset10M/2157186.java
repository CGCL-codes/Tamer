package org.openuss.braincontest;

import java.util.Collection;
import java.util.List;

/**
 * @see org.openuss.braincontest.Answer
 */
public interface AnswerDao {

    /**
	 * This constant is used as a transformation flag; entities can be converted
	 * automatically into value objects or other types, different methods in a
	 * class implementing this interface support this feature: look for an
	 * <code>int</code> parameter called <code>transform</code>. <p/> This
	 * specific flag denotes no transformation will occur.
	 */
    public static final int TRANSFORM_NONE = 0;

    /**
	 * This constant is used as a transformation flag; entities can be converted
	 * automatically into value objects or other types, different methods in a
	 * class implementing this interface support this feature: look for an
	 * <code>int</code> parameter called <code>transform</code>. <p/> This
	 * specific flag denotes entities must be transformed into objects of type
	 * {@link org.openuss.braincontest.AnswerInfo}.
	 */
    public static final int TRANSFORM_ANSWERINFO = 1;

    /**
	 * Copies the fields of the specified entity to the target value object.
	 * This method is similar to toAnswerInfo(), but it does not handle any
	 * attributes in the target value object that are "read-only" (as those do
	 * not have setter methods exposed).
	 */
    public void toAnswerInfo(Answer sourceEntity, AnswerInfo targetVO);

    /**
	 * Converts this DAO's entity to an object of type
	 * {@link org.openuss.braincontest.AnswerInfo}.
	 */
    public AnswerInfo toAnswerInfo(Answer entity);

    /**
	 * Converts this DAO's entity to a Collection of instances of type
	 * {@link org.openuss.braincontest.AnswerInfo}.
	 */
    public void toAnswerInfoCollection(Collection entities);

    /**
	 * Copies the fields of {@link org.openuss.braincontest.AnswerInfo} to the
	 * specified entity.
	 * 
	 * @param copyIfNull
	 *            If FALSE, the value object's field will not be copied to the
	 *            entity if the value is NULL. If TRUE, it will be copied
	 *            regardless of its value.
	 */
    public void answerInfoToEntity(AnswerInfo sourceVO, Answer targetEntity, boolean copyIfNull);

    /**
	 * Converts an instance of type {@link org.openuss.braincontest.AnswerInfo}
	 * to this DAO's entity.
	 */
    public Answer answerInfoToEntity(AnswerInfo answerInfo);

    /**
	 * Converts a Collection of instances of type
	 * {@link org.openuss.braincontest.AnswerInfo} to this DAO's entity.
	 */
    public void answerInfoToEntityCollection(Collection instances);

    /**
	 * Loads an instance of org.openuss.braincontest.Answer from the persistent
	 * store.
	 */
    public Answer load(AnswerPK answerPk);

    /**
	 * <p>
	 * Does the same thing as {@link #load(Long)} with an additional
	 * flag called <code>transform</code>. If this flag is set to
	 * <code>TRANSFORM_NONE</code> then the returned entity will
	 * <strong>NOT</strong> be transformed. If this flag is any of the other
	 * constants defined in this class then the result <strong>WILL BE</strong>
	 * passed through an operation which can optionally transform the entity
	 * (into a value object for example). By default, transformation does not
	 * occur.
	 * </p>
	 * 
	 * @param id
	 *            the identifier of the entity to load.
	 * @return either the entity or the object transformed from the entity.
	 */
    public Object load(int transform, org.openuss.braincontest.AnswerPK answerPk);

    /**
	 * Loads all entities of type {@link org.openuss.braincontest.Answer}.
	 * 
	 * @return the loaded entities.
	 */
    public Collection<org.openuss.braincontest.Answer> loadAll();

    /**
	 * <p>
	 * Does the same thing as {@link #loadAll()} with an additional flag called
	 * <code>transform</code>. If this flag is set to
	 * <code>TRANSFORM_NONE</code> then the returned entity will
	 * <strong>NOT</strong> be transformed. If this flag is any of the other
	 * constants defined here then the result <strong>WILL BE</strong> passed
	 * through an operation which can optionally transform the entity (into a
	 * value object for example). By default, transformation does not occur.
	 * </p>
	 * 
	 * @param transform
	 *            the flag indicating what transformation to use.
	 * @return the loaded entities.
	 */
    public Collection loadAll(final int transform);

    /**
	 * Creates an instance of org.openuss.braincontest.Answer and adds it to the
	 * persistent store.
	 */
    public Answer create(Answer answer);

    /**
	 * <p>
	 * Does the same thing as {@link #create(org.openuss.braincontest.Answer)}
	 * with an additional flag called <code>transform</code>. If this flag is
	 * set to <code>TRANSFORM_NONE</code> then the returned entity will
	 * <strong>NOT</strong> be transformed. If this flag is any of the other
	 * constants defined here then the result <strong>WILL BE</strong> passed
	 * through an operation which can optionally transform the entity (into a
	 * value object for example). By default, transformation does not occur.
	 * </p>
	 */
    public Object create(int transform, Answer answer);

    /**
	 * Creates a new instance of org.openuss.braincontest.Answer and adds from
	 * the passed in <code>entities</code> collection
	 * 
	 * @param entities
	 *            the collection of org.openuss.braincontest.Answer instances to
	 *            create.
	 * 
	 * @return the created instances.
	 */
    public Collection<Answer> create(Collection<Answer> entities);

    /**
	 * <p>
	 * Does the same thing as {@link #create(org.openuss.braincontest.Answer)}
	 * with an additional flag called <code>transform</code>. If this flag is
	 * set to <code>TRANSFORM_NONE</code> then the returned entity will
	 * <strong>NOT</strong> be transformed. If this flag is any of the other
	 * constants defined here then the result <strong>WILL BE</strong> passed
	 * through an operation which can optionally transform the entities (into
	 * value objects for example). By default, transformation does not occur.
	 * </p>
	 */
    public Collection create(int transform, Collection<Answer> entities);

    /**
	 * Updates the <code>answer</code> instance in the persistent store.
	 */
    public void update(Answer answer);

    /**
	 * Updates all instances in the <code>entities</code> collection in the
	 * persistent store.
	 */
    public void update(Collection<Answer> entities);

    /**
	 * Removes the instance of org.openuss.braincontest.Answer from the
	 * persistent store.
	 */
    public void remove(Answer answer);

    /**
	 * Removes the instance of org.openuss.braincontest.Answer having the given
	 * <code>identifier</code> from the persistent store.
	 */
    public void remove(org.openuss.braincontest.AnswerPK answerPk);

    /**
	 * Removes all entities in the given <code>entities<code> collection.
	 */
    public void remove(java.util.Collection<org.openuss.braincontest.Answer> entities);

    /**
 * 
     */
    public Answer findByContestAndSolver(Long solverId, Long contestId);

    /**
	 * <p> Does the same thing as
	 * {@link #findByContestAndSolver(Long, Long)} with an
	 * additional argument called <code>queryString</code>. This
	 * <code>queryString</code> argument allows you to override the query string
	 * defined in
	 * {@link #findByContestAndSolver(Long, Long)}. </p>
	 */
    public Answer findByContestAndSolver(String queryString, Long solverId, Long contestId);

    /**
	 * <p>
	 * Does the same thing as
	 * {@link #findByContestAndSolver(Long, Long)} with an
	 * additional flag called <code>transform</code>. If this flag is set to
	 * <code>TRANSFORM_NONE</code> then finder results will <strong>NOT</strong>
	 * be transformed during retrieval. If this flag is any of the other
	 * constants defined here then finder results <strong>WILL BE</strong>
	 * passed through an operation which can optionally transform the entities
	 * (into value objects for example). By default, transformation does not
	 * occur.
	 * </p>
	 */
    public Object findByContestAndSolver(int transform, Long solverId, Long contestId);

    /**
	 * <p>
	 * Does the same thing as
	 * {@link #findByContestAndSolver(boolean, Long, Long)}
	 * with an additional argument called <code>queryString</code>. This
	 * <code>queryString</code> argument allows you to override the query string
	 * defined in {@link #findByContestAndSolver(int, Long solverId,
	 * Long contestId)}.
	 * </p>
	 */
    public Object findByContestAndSolver(int transform, String queryString, Long solverId, Long contestId);

    /**
	 * 	
     */
    public List findBySolver(Long solverId);
}
