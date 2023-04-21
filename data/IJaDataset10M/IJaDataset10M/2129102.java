package org.didicero.base.entity;

/**
 * <p>
 * Base EJB3 DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>org.didicero.base.entity.Answer</code>.
 * </p>
 *
 * @see org.didicero.base.entity.AnswerDao
 */
@javax.ejb.TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
@javax.ejb.Local({ org.didicero.base.entity.AnswerDao.class })
@javax.annotation.security.PermitAll
public abstract class AnswerDaoBase implements org.didicero.base.entity.AnswerDao {

    @javax.annotation.Resource
    protected javax.ejb.SessionContext context;

    /**
     * Inject persistence context didicero     
     */
    @javax.persistence.PersistenceContext(unitName = "didicero")
    protected javax.persistence.EntityManager emanager;

    /**
     * @see org.didicero.base.entity.AnswerDao#load(int,)
     */
    public Object load(final int transform, final java.lang.Long id) throws org.didicero.base.entity.AnswerDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Answer.load - 'id' can not be null");
        }
        try {
            final Object entity = (org.didicero.base.entity.Answer) emanager.find(org.didicero.base.entity.Answer.class, id);
            return transformEntity(transform, (org.didicero.base.entity.Answer) entity);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.AnswerDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#load()
     */
    public org.didicero.base.entity.Answer load(final java.lang.Long id) throws org.didicero.base.entity.AnswerDaoException {
        return (org.didicero.base.entity.Answer) this.load(TRANSFORM_NONE, id);
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#loadAll()
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.Answer> loadAll() throws org.didicero.base.entity.AnswerDaoException {
        return (java.util.Collection<org.didicero.base.entity.Answer>) this.loadAll(TRANSFORM_NONE);
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#loadAll(int)
     */
    public java.util.Collection loadAll(final int transform) throws org.didicero.base.entity.AnswerDaoException {
        try {
            javax.persistence.Query query = emanager.createNamedQuery("Answer.findAll");
            java.util.List<org.didicero.base.entity.Answer> results = query.getResultList();
            this.transformEntities(transform, results);
            return results;
        } catch (Exception ex) {
            throw new org.didicero.base.entity.AnswerDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#create(org.didicero.base.entity.Answer)
     */
    public org.didicero.base.entity.Answer create(org.didicero.base.entity.Answer answer) throws org.didicero.base.entity.AnswerDaoException {
        return (org.didicero.base.entity.Answer) this.create(TRANSFORM_NONE, answer);
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#create(int transform, org.didicero.base.entity.Answer)
     */
    public Object create(final int transform, final org.didicero.base.entity.Answer answer) throws org.didicero.base.entity.AnswerDaoException {
        if (answer == null) {
            throw new IllegalArgumentException("Answer.create - 'answer' can not be null");
        }
        try {
            emanager.persist(answer);
            emanager.flush();
            return this.transformEntity(transform, answer);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.AnswerDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#create(java.util.Collection<org.didicero.base.entity.Answer>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.Answer> create(final java.util.Collection<org.didicero.base.entity.Answer> entities) throws org.didicero.base.entity.AnswerDaoException {
        return create(TRANSFORM_NONE, entities);
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#create(int, java.util.Collection<org.didicero.base.entity.Answer>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection create(final int transform, final java.util.Collection<org.didicero.base.entity.Answer> entities) throws org.didicero.base.entity.AnswerDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("Answer.create - 'entities' can not be null");
        }
        java.util.Collection results = new java.util.ArrayList();
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                results.add(create(transform, (org.didicero.base.entity.Answer) entityIterator.next()));
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.AnswerDaoException(ex);
        }
        return results;
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#create(boolean, int, java.lang.String, org.didicero.base.entity.Language, java.lang.Long)
     */
    public org.didicero.base.entity.Answer create(boolean ok, int position, java.lang.String text, org.didicero.base.entity.Language lang, java.lang.Long guid) throws org.didicero.base.entity.AnswerDaoException {
        return (org.didicero.base.entity.Answer) this.create(TRANSFORM_NONE, ok, position, text, lang, guid);
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#create(int, boolean, int, java.lang.String, org.didicero.base.entity.Language, java.lang.Long)
     */
    public Object create(final int transform, boolean ok, int position, java.lang.String text, org.didicero.base.entity.Language lang, java.lang.Long guid) throws org.didicero.base.entity.AnswerDaoException {
        org.didicero.base.entity.Answer entity = new org.didicero.base.entity.Answer();
        entity.setOk(ok);
        entity.setPosition(position);
        entity.setText(text);
        entity.setLang(lang);
        entity.setGuid(guid);
        return this.create(transform, entity);
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#update(org.didicero.base.entity.Answer)
     */
    public void update(org.didicero.base.entity.Answer answer) throws org.didicero.base.entity.AnswerDaoException {
        if (answer == null) {
            throw new IllegalArgumentException("Answer.update - 'answer' can not be null");
        }
        try {
            emanager.merge(answer);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.AnswerDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#update(java.util.Collection<org.didicero.base.entity.Answer>)
     */
    public void update(final java.util.Collection<org.didicero.base.entity.Answer> entities) throws org.didicero.base.entity.AnswerDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("Answer.update - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                update((org.didicero.base.entity.Answer) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.AnswerDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#remove(org.didicero.base.entity.Answer)
     */
    public void remove(org.didicero.base.entity.Answer answer) throws org.didicero.base.entity.AnswerDaoException {
        if (answer == null) {
            throw new IllegalArgumentException("Answer.remove - 'answer' can not be null");
        }
        try {
            emanager.remove(answer);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.AnswerDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#remove(java.lang.Long)
     */
    public void remove(java.lang.Long id) throws org.didicero.base.entity.AnswerDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Answer.remove - 'id' can not be null");
        }
        try {
            final org.didicero.base.entity.Answer entity = (org.didicero.base.entity.Answer) this.load(id);
            if (entity != null) {
                this.remove(entity);
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.AnswerDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.AnswerDao#remove(java.util.Collection<org.didicero.base.entity.Answer>)
     */
    public void remove(java.util.Collection<org.didicero.base.entity.Answer> entities) throws org.didicero.base.entity.AnswerDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("Answer.remove - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                remove((org.didicero.base.entity.Answer) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.AnswerDaoException(ex);
        }
    }

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>org.didicero.base.entity.AnswerDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link org.didicero.base.entity.AnswerDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,java.util.Collection)
     */
    protected Object transformEntity(final int transform, final org.didicero.base.entity.Answer entity) {
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
     * {@link #transformEntity(int, org.didicero.base.entity.Answer)}
     * method. This method does not instantiate a new collection.
     * <p/>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>org.didicero.base.entity.AnswerDao</code>
     * @param entities the collection of entities to transform
     * @return the same collection as the argument, but this time containing the transformed entities
     * @see #transformEntity(int, org.didicero.base.entity.Answer)
     */
    protected void transformEntities(final int transform, final java.util.Collection entities) {
        switch(transform) {
            case TRANSFORM_NONE:
            default:
        }
    }
}
