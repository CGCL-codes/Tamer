package org.didicero.base.entity;

/**
 * <p>
 * Base EJB3 DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>org.didicero.base.entity.Formatting</code>.
 * </p>
 *
 * @see org.didicero.base.entity.FormattingDao
 */
@javax.ejb.TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
@javax.ejb.Local({ org.didicero.base.entity.FormattingDao.class })
@javax.annotation.security.PermitAll
public abstract class FormattingDaoBase implements org.didicero.base.entity.FormattingDao {

    @javax.annotation.Resource
    protected javax.ejb.SessionContext context;

    /**
     * Inject persistence context didicero     
     */
    @javax.persistence.PersistenceContext(unitName = "didicero")
    protected javax.persistence.EntityManager emanager;

    /**
     * @see org.didicero.base.entity.FormattingDao#load(int,)
     */
    public Object load(final int transform, final java.lang.Long id) throws org.didicero.base.entity.FormattingDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Formatting.load - 'id' can not be null");
        }
        try {
            final Object entity = (org.didicero.base.entity.Formatting) emanager.find(org.didicero.base.entity.Formatting.class, id);
            return transformEntity(transform, (org.didicero.base.entity.Formatting) entity);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.FormattingDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#load()
     */
    public org.didicero.base.entity.Formatting load(final java.lang.Long id) throws org.didicero.base.entity.FormattingDaoException {
        return (org.didicero.base.entity.Formatting) this.load(TRANSFORM_NONE, id);
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#loadAll()
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.Formatting> loadAll() throws org.didicero.base.entity.FormattingDaoException {
        return (java.util.Collection<org.didicero.base.entity.Formatting>) this.loadAll(TRANSFORM_NONE);
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#loadAll(int)
     */
    public java.util.Collection loadAll(final int transform) throws org.didicero.base.entity.FormattingDaoException {
        try {
            javax.persistence.Query query = emanager.createNamedQuery("Formatting.findAll");
            java.util.List<org.didicero.base.entity.Formatting> results = query.getResultList();
            this.transformEntities(transform, results);
            return results;
        } catch (Exception ex) {
            throw new org.didicero.base.entity.FormattingDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#create(org.didicero.base.entity.Formatting)
     */
    public org.didicero.base.entity.Formatting create(org.didicero.base.entity.Formatting formatting) throws org.didicero.base.entity.FormattingDaoException {
        return (org.didicero.base.entity.Formatting) this.create(TRANSFORM_NONE, formatting);
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#create(int transform, org.didicero.base.entity.Formatting)
     */
    public Object create(final int transform, final org.didicero.base.entity.Formatting formatting) throws org.didicero.base.entity.FormattingDaoException {
        if (formatting == null) {
            throw new IllegalArgumentException("Formatting.create - 'formatting' can not be null");
        }
        try {
            emanager.persist(formatting);
            emanager.flush();
            return this.transformEntity(transform, formatting);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.FormattingDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#create(java.util.Collection<org.didicero.base.entity.Formatting>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.Formatting> create(final java.util.Collection<org.didicero.base.entity.Formatting> entities) throws org.didicero.base.entity.FormattingDaoException {
        return create(TRANSFORM_NONE, entities);
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#create(int, java.util.Collection<org.didicero.base.entity.Formatting>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection create(final int transform, final java.util.Collection<org.didicero.base.entity.Formatting> entities) throws org.didicero.base.entity.FormattingDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("Formatting.create - 'entities' can not be null");
        }
        java.util.Collection results = new java.util.ArrayList();
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                results.add(create(transform, (org.didicero.base.entity.Formatting) entityIterator.next()));
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.FormattingDaoException(ex);
        }
        return results;
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#create(java.lang.String, java.lang.Boolean, java.util.Date, java.util.Date, java.lang.String, java.lang.String, java.lang.String)
     */
    public org.didicero.base.entity.Formatting create(java.lang.String name, java.lang.Boolean isWorkingCopy, java.util.Date createdate, java.util.Date changedate, java.lang.String uri, java.lang.String createuser, java.lang.String changeuser) throws org.didicero.base.entity.FormattingDaoException {
        return (org.didicero.base.entity.Formatting) this.create(TRANSFORM_NONE, name, isWorkingCopy, createdate, changedate, uri, createuser, changeuser);
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#create(int, java.lang.String, java.lang.Boolean, java.util.Date, java.util.Date, java.lang.String, java.lang.String, java.lang.String)
     */
    public Object create(final int transform, java.lang.String name, java.lang.Boolean isWorkingCopy, java.util.Date createdate, java.util.Date changedate, java.lang.String uri, java.lang.String createuser, java.lang.String changeuser) throws org.didicero.base.entity.FormattingDaoException {
        org.didicero.base.entity.Formatting entity = new org.didicero.base.entity.Formatting();
        entity.setName(name);
        entity.setIsWorkingCopy(isWorkingCopy);
        entity.setCreatedate(createdate);
        entity.setChangedate(changedate);
        entity.setUri(uri);
        entity.setCreateuser(createuser);
        entity.setChangeuser(changeuser);
        return this.create(transform, entity);
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#update(org.didicero.base.entity.Formatting)
     */
    public void update(org.didicero.base.entity.Formatting formatting) throws org.didicero.base.entity.FormattingDaoException {
        if (formatting == null) {
            throw new IllegalArgumentException("Formatting.update - 'formatting' can not be null");
        }
        try {
            emanager.merge(formatting);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.FormattingDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#update(java.util.Collection<org.didicero.base.entity.Formatting>)
     */
    public void update(final java.util.Collection<org.didicero.base.entity.Formatting> entities) throws org.didicero.base.entity.FormattingDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("Formatting.update - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                update((org.didicero.base.entity.Formatting) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.FormattingDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#remove(org.didicero.base.entity.Formatting)
     */
    public void remove(org.didicero.base.entity.Formatting formatting) throws org.didicero.base.entity.FormattingDaoException {
        if (formatting == null) {
            throw new IllegalArgumentException("Formatting.remove - 'formatting' can not be null");
        }
        try {
            emanager.remove(formatting);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.FormattingDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#remove(java.lang.Long)
     */
    public void remove(java.lang.Long id) throws org.didicero.base.entity.FormattingDaoException {
        if (id == null) {
            throw new IllegalArgumentException("Formatting.remove - 'id' can not be null");
        }
        try {
            final org.didicero.base.entity.Formatting entity = (org.didicero.base.entity.Formatting) this.load(id);
            if (entity != null) {
                this.remove(entity);
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.FormattingDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#remove(java.util.Collection<org.didicero.base.entity.Formatting>)
     */
    public void remove(java.util.Collection<org.didicero.base.entity.Formatting> entities) throws org.didicero.base.entity.FormattingDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("Formatting.remove - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                remove((org.didicero.base.entity.Formatting) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.FormattingDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#findResourceByUri(java.lang.String)
     */
    public org.didicero.base.entity.Resource findResourceByUri(java.lang.String uri) throws org.didicero.base.entity.FormattingDaoException {
        return (org.didicero.base.entity.Resource) this.findResourceByUri(TRANSFORM_NONE, uri);
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#findResourceByUri(java.lang.String, java.lang.String)
     */
    public org.didicero.base.entity.Resource findResourceByUri(final java.lang.String queryString, final java.lang.String uri) throws org.didicero.base.entity.FormattingDaoException {
        return (org.didicero.base.entity.Resource) this.findResourceByUri(TRANSFORM_NONE, queryString, uri);
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#findResourceByUri(int, java.lang.String)
     */
    public Object findResourceByUri(final int transform, final java.lang.String uri) throws org.didicero.base.entity.FormattingDaoException {
        try {
            javax.persistence.Query queryObject = emanager.createNamedQuery("Formatting.findResourceByUri");
            queryObject.setParameter("uri", uri);
            Object result = queryObject.getSingleResult();
            result = transformEntity(transform, (org.didicero.base.entity.Formatting) result);
            return result;
        } catch (Exception ex) {
            throw new org.didicero.base.entity.FormattingDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.FormattingDao#findResourceByUri(int, java.lang.String, java.lang.String)
     */
    public Object findResourceByUri(final int transform, final java.lang.String queryString, final java.lang.String uri) throws org.didicero.base.entity.FormattingDaoException {
        try {
            javax.persistence.Query queryObject = emanager.createQuery(queryString);
            queryObject.setParameter("uri", uri);
            Object result = queryObject.getSingleResult();
            result = transformEntity(transform, (org.didicero.base.entity.Formatting) result);
            return result;
        } catch (Exception ex) {
            throw new org.didicero.base.entity.FormattingDaoException(ex);
        }
    }

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>org.didicero.base.entity.FormattingDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link org.didicero.base.entity.FormattingDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,java.util.Collection)
     */
    protected Object transformEntity(final int transform, final org.didicero.base.entity.Formatting entity) {
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
     * {@link #transformEntity(int, org.didicero.base.entity.Formatting)}
     * method. This method does not instantiate a new collection.
     * <p/>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>org.didicero.base.entity.FormattingDao</code>
     * @param entities the collection of entities to transform
     * @return the same collection as the argument, but this time containing the transformed entities
     * @see #transformEntity(int, org.didicero.base.entity.Formatting)
     */
    protected void transformEntities(final int transform, final java.util.Collection entities) {
        switch(transform) {
            case TRANSFORM_NONE:
            default:
        }
    }
}
