package org.didicero.base.entity;

/**
 * <p>
 * Base EJB3 DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>org.didicero.base.entity.ValidatedQuestion</code>.
 * </p>
 *
 * @see org.didicero.base.entity.ValidatedQuestionDao
 */
@javax.ejb.TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
@javax.ejb.Local({ org.didicero.base.entity.ValidatedQuestionDao.class })
@javax.annotation.security.PermitAll
public abstract class ValidatedQuestionDaoBase implements org.didicero.base.entity.ValidatedQuestionDao {

    @javax.annotation.Resource
    protected javax.ejb.SessionContext context;

    /**
     * Inject persistence context didicero     
     */
    @javax.persistence.PersistenceContext(unitName = "didicero")
    protected javax.persistence.EntityManager emanager;

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#load(int,)
     */
    public Object load(final int transform, final java.lang.Long id) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        if (id == null) {
            throw new IllegalArgumentException("ValidatedQuestion.load - 'id' can not be null");
        }
        try {
            final Object entity = (org.didicero.base.entity.ValidatedQuestion) emanager.find(org.didicero.base.entity.ValidatedQuestion.class, id);
            return transformEntity(transform, (org.didicero.base.entity.ValidatedQuestion) entity);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ValidatedQuestionDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#load()
     */
    public org.didicero.base.entity.ValidatedQuestion load(final java.lang.Long id) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        return (org.didicero.base.entity.ValidatedQuestion) this.load(TRANSFORM_NONE, id);
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#loadAll()
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.ValidatedQuestion> loadAll() throws org.didicero.base.entity.ValidatedQuestionDaoException {
        return (java.util.Collection<org.didicero.base.entity.ValidatedQuestion>) this.loadAll(TRANSFORM_NONE);
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#loadAll(int)
     */
    public java.util.Collection loadAll(final int transform) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        try {
            javax.persistence.Query query = emanager.createNamedQuery("ValidatedQuestion.findAll");
            java.util.List<org.didicero.base.entity.ValidatedQuestion> results = query.getResultList();
            this.transformEntities(transform, results);
            return results;
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ValidatedQuestionDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#create(org.didicero.base.entity.ValidatedQuestion)
     */
    public org.didicero.base.entity.ValidatedQuestion create(org.didicero.base.entity.ValidatedQuestion validatedQuestion) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        return (org.didicero.base.entity.ValidatedQuestion) this.create(TRANSFORM_NONE, validatedQuestion);
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#create(int transform, org.didicero.base.entity.ValidatedQuestion)
     */
    public Object create(final int transform, final org.didicero.base.entity.ValidatedQuestion validatedQuestion) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        if (validatedQuestion == null) {
            throw new IllegalArgumentException("ValidatedQuestion.create - 'validatedQuestion' can not be null");
        }
        try {
            emanager.persist(validatedQuestion);
            emanager.flush();
            return this.transformEntity(transform, validatedQuestion);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ValidatedQuestionDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#create(java.util.Collection<org.didicero.base.entity.ValidatedQuestion>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.ValidatedQuestion> create(final java.util.Collection<org.didicero.base.entity.ValidatedQuestion> entities) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        return create(TRANSFORM_NONE, entities);
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#create(int, java.util.Collection<org.didicero.base.entity.ValidatedQuestion>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection create(final int transform, final java.util.Collection<org.didicero.base.entity.ValidatedQuestion> entities) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("ValidatedQuestion.create - 'entities' can not be null");
        }
        java.util.Collection results = new java.util.ArrayList();
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                results.add(create(transform, (org.didicero.base.entity.ValidatedQuestion) entityIterator.next()));
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ValidatedQuestionDaoException(ex);
        }
        return results;
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#create(float, java.lang.String)
     */
    public org.didicero.base.entity.ValidatedQuestion create(float reachedPoints, java.lang.String comment) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        return (org.didicero.base.entity.ValidatedQuestion) this.create(TRANSFORM_NONE, reachedPoints, comment);
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#create(int, float, java.lang.String)
     */
    public Object create(final int transform, float reachedPoints, java.lang.String comment) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        org.didicero.base.entity.ValidatedQuestion entity = new org.didicero.base.entity.ValidatedQuestion();
        entity.setReachedPoints(reachedPoints);
        entity.setComment(comment);
        return this.create(transform, entity);
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#create(java.lang.String, float, org.didicero.base.entity.TestQuestion)
     */
    public org.didicero.base.entity.ValidatedQuestion create(java.lang.String comment, float reachedPoints, org.didicero.base.entity.TestQuestion validationsQuest) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        return (org.didicero.base.entity.ValidatedQuestion) this.create(TRANSFORM_NONE, comment, reachedPoints, validationsQuest);
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#create(int, java.lang.String, float, org.didicero.base.entity.TestQuestion)
     */
    public Object create(final int transform, java.lang.String comment, float reachedPoints, org.didicero.base.entity.TestQuestion validationsQuest) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        org.didicero.base.entity.ValidatedQuestion entity = new org.didicero.base.entity.ValidatedQuestion();
        entity.setComment(comment);
        entity.setReachedPoints(reachedPoints);
        entity.setValidationsQuest(validationsQuest);
        return this.create(transform, entity);
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#update(org.didicero.base.entity.ValidatedQuestion)
     */
    public void update(org.didicero.base.entity.ValidatedQuestion validatedQuestion) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        if (validatedQuestion == null) {
            throw new IllegalArgumentException("ValidatedQuestion.update - 'validatedQuestion' can not be null");
        }
        try {
            emanager.merge(validatedQuestion);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ValidatedQuestionDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#update(java.util.Collection<org.didicero.base.entity.ValidatedQuestion>)
     */
    public void update(final java.util.Collection<org.didicero.base.entity.ValidatedQuestion> entities) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("ValidatedQuestion.update - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                update((org.didicero.base.entity.ValidatedQuestion) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ValidatedQuestionDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#remove(org.didicero.base.entity.ValidatedQuestion)
     */
    public void remove(org.didicero.base.entity.ValidatedQuestion validatedQuestion) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        if (validatedQuestion == null) {
            throw new IllegalArgumentException("ValidatedQuestion.remove - 'validatedQuestion' can not be null");
        }
        try {
            emanager.remove(validatedQuestion);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ValidatedQuestionDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#remove(java.lang.Long)
     */
    public void remove(java.lang.Long id) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        if (id == null) {
            throw new IllegalArgumentException("ValidatedQuestion.remove - 'id' can not be null");
        }
        try {
            final org.didicero.base.entity.ValidatedQuestion entity = this.load(id);
            if (entity != null) {
                this.remove(entity);
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ValidatedQuestionDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ValidatedQuestionDao#remove(java.util.Collection<org.didicero.base.entity.ValidatedQuestion>)
     */
    public void remove(java.util.Collection<org.didicero.base.entity.ValidatedQuestion> entities) throws org.didicero.base.entity.ValidatedQuestionDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("ValidatedQuestion.remove - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                remove((org.didicero.base.entity.ValidatedQuestion) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ValidatedQuestionDaoException(ex);
        }
    }

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>org.didicero.base.entity.ValidatedQuestionDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link org.didicero.base.entity.ValidatedQuestionDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,java.util.Collection)
     */
    protected Object transformEntity(final int transform, final org.didicero.base.entity.ValidatedQuestion entity) {
        Object target = null;
        if (entity != null) {
            switch(transform) {
                case TRANSFORM_NONE:
                default:
                    target = entity;
            }
        }
        return target;
    }

    /**
     * Transforms a collection of entities using the
     * {@link #transformEntity(int, org.didicero.base.entity.ValidatedQuestion)}
     * method. This method does not instantiate a new collection.
     * <p/>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>org.didicero.base.entity.ValidatedQuestionDao</code>
     * @param entities the collection of entities to transform
     * @return the same collection as the argument, but this time containing the transformed entities
     * @see #transformEntity(int, org.didicero.base.entity.ValidatedQuestion)
     */
    protected void transformEntities(final int transform, final java.util.Collection entities) {
        switch(transform) {
            case TRANSFORM_NONE:
            default:
        }
    }
}
