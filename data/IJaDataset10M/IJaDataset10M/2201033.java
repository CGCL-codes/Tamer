package org.didicero.base.entity;

/**
 * <p>
 * Base EJB3 DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>org.didicero.base.entity.Content</code>.
 * </p>
 *
 * @see org.didicero.base.entity.ContentDao
 */
@javax.ejb.TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
@javax.ejb.Local({ org.didicero.base.entity.ContentDao.class })
@javax.annotation.security.PermitAll
public abstract class ContentDaoBase implements org.didicero.base.entity.ContentDao {

    @javax.annotation.Resource
    protected javax.ejb.SessionContext context;

    /**
     * Inject persistence context didicero     
     */
    @javax.persistence.PersistenceContext(unitName = "didicero")
    protected javax.persistence.EntityManager emanager;

    /**
     * @see org.didicero.base.entity.ContentDao#load(int,)
     */
    public Object load(final int transform, final java.lang.Long id) throws org.didicero.base.entity.ContentDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Content.load - 'id' can not be null");
        }
        try {
            final Object entity = (org.didicero.base.entity.Content) emanager.find(org.didicero.base.entity.Content.class, id);
            return transformEntity(transform, (org.didicero.base.entity.Content) entity);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ContentDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ContentDao#load()
     */
    public org.didicero.base.entity.Content load(final java.lang.Long id) throws org.didicero.base.entity.ContentDaoException {
        return (org.didicero.base.entity.Content) this.load(TRANSFORM_NONE, id);
    }

    /**
     * @see org.didicero.base.entity.ContentDao#loadAll()
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.Content> loadAll() throws org.didicero.base.entity.ContentDaoException {
        return (java.util.Collection<org.didicero.base.entity.Content>) this.loadAll(TRANSFORM_NONE);
    }

    /**
     * @see org.didicero.base.entity.ContentDao#loadAll(int)
     */
    public java.util.Collection loadAll(final int transform) throws org.didicero.base.entity.ContentDaoException {
        try {
            javax.persistence.Query query = emanager.createNamedQuery("Content.findAll");
            java.util.List<org.didicero.base.entity.Content> results = query.getResultList();
            this.transformEntities(transform, results);
            return results;
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ContentDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ContentDao#create(org.didicero.base.entity.Content)
     */
    public org.didicero.base.entity.Content create(org.didicero.base.entity.Content content) throws org.didicero.base.entity.ContentDaoException {
        return (org.didicero.base.entity.Content) this.create(TRANSFORM_NONE, content);
    }

    /**
     * @see org.didicero.base.entity.ContentDao#create(int transform, org.didicero.base.entity.Content)
     */
    public Object create(final int transform, final org.didicero.base.entity.Content content) throws org.didicero.base.entity.ContentDaoException {
        if (content == null) {
            throw new IllegalArgumentException("Content.create - 'content' can not be null");
        }
        try {
            emanager.persist(content);
            emanager.flush();
            return this.transformEntity(transform, content);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ContentDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ContentDao#create(java.util.Collection<org.didicero.base.entity.Content>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.Content> create(final java.util.Collection<org.didicero.base.entity.Content> entities) throws org.didicero.base.entity.ContentDaoException {
        return create(TRANSFORM_NONE, entities);
    }

    /**
     * @see org.didicero.base.entity.ContentDao#create(int, java.util.Collection<org.didicero.base.entity.Content>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection create(final int transform, final java.util.Collection<org.didicero.base.entity.Content> entities) throws org.didicero.base.entity.ContentDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("Content.create - 'entities' can not be null");
        }
        java.util.Collection results = new java.util.ArrayList();
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                results.add(create(transform, (org.didicero.base.entity.Content) entityIterator.next()));
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ContentDaoException(ex);
        }
        return results;
    }

    /**
     * @see org.didicero.base.entity.ContentDao#create(java.lang.String, org.didicero.base.entity.Language, java.lang.Long)
     */
    public org.didicero.base.entity.Content create(java.lang.String text, org.didicero.base.entity.Language lang, java.lang.Long guid) throws org.didicero.base.entity.ContentDaoException {
        return (org.didicero.base.entity.Content) this.create(TRANSFORM_NONE, text, lang, guid);
    }

    /**
     * @see org.didicero.base.entity.ContentDao#create(int, java.lang.String, org.didicero.base.entity.Language, java.lang.Long)
     */
    public Object create(final int transform, java.lang.String text, org.didicero.base.entity.Language lang, java.lang.Long guid) throws org.didicero.base.entity.ContentDaoException {
        org.didicero.base.entity.Content entity = new org.didicero.base.entity.Content();
        entity.setText(text);
        entity.setLang(lang);
        entity.setGuid(guid);
        return this.create(transform, entity);
    }

    /**
     * @see org.didicero.base.entity.ContentDao#update(org.didicero.base.entity.Content)
     */
    public void update(org.didicero.base.entity.Content content) throws org.didicero.base.entity.ContentDaoException {
        if (content == null) {
            throw new IllegalArgumentException("Content.update - 'content' can not be null");
        }
        try {
            emanager.merge(content);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ContentDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ContentDao#update(java.util.Collection<org.didicero.base.entity.Content>)
     */
    public void update(final java.util.Collection<org.didicero.base.entity.Content> entities) throws org.didicero.base.entity.ContentDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("Content.update - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                update((org.didicero.base.entity.Content) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ContentDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ContentDao#remove(org.didicero.base.entity.Content)
     */
    public void remove(org.didicero.base.entity.Content content) throws org.didicero.base.entity.ContentDaoException {
        if (content == null) {
            throw new IllegalArgumentException("Content.remove - 'content' can not be null");
        }
        try {
            emanager.remove(content);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ContentDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ContentDao#remove(java.lang.Long)
     */
    public void remove(java.lang.Long id) throws org.didicero.base.entity.ContentDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Content.remove - 'id' can not be null");
        }
        try {
            final org.didicero.base.entity.Content entity = (org.didicero.base.entity.Content) this.load(id);
            if (entity != null) {
                this.remove(entity);
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ContentDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.ContentDao#remove(java.util.Collection<org.didicero.base.entity.Content>)
     */
    public void remove(java.util.Collection<org.didicero.base.entity.Content> entities) throws org.didicero.base.entity.ContentDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("Content.remove - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                remove((org.didicero.base.entity.Content) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.ContentDaoException(ex);
        }
    }

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>org.didicero.base.entity.ContentDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link org.didicero.base.entity.ContentDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,java.util.Collection)
     */
    protected Object transformEntity(final int transform, final org.didicero.base.entity.Content entity) {
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
     * {@link #transformEntity(int, org.didicero.base.entity.Content)}
     * method. This method does not instantiate a new collection.
     * <p/>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>org.didicero.base.entity.ContentDao</code>
     * @param entities the collection of entities to transform
     * @return the same collection as the argument, but this time containing the transformed entities
     * @see #transformEntity(int, org.didicero.base.entity.Content)
     */
    protected void transformEntities(final int transform, final java.util.Collection entities) {
        switch(transform) {
            case TRANSFORM_NONE:
            default:
        }
    }
}
