package org.didicero.base.entity;

/**
 * <p>
 * Base EJB3 DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>org.didicero.base.entity.TextNode</code>.
 * </p>
 *
 * @see org.didicero.base.entity.TextNodeDao
 */
@javax.ejb.TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
@javax.ejb.Local({ org.didicero.base.entity.TextNodeDao.class })
@javax.annotation.security.PermitAll
public abstract class TextNodeDaoBase implements org.didicero.base.entity.TextNodeDao {

    @javax.annotation.Resource
    protected javax.ejb.SessionContext context;

    /**
     * Inject persistence context didicero     
     */
    @javax.persistence.PersistenceContext(unitName = "didicero")
    protected javax.persistence.EntityManager emanager;

    /**
     * @see org.didicero.base.entity.TextNodeDao#load(int,)
     */
    public Object load(final int transform, final java.lang.Long id) throws org.didicero.base.entity.TextNodeDaoException {
        if (id == null) {
            throw new IllegalArgumentException("TextNode.load - 'id' can not be null");
        }
        try {
            final Object entity = (org.didicero.base.entity.TextNode) emanager.find(org.didicero.base.entity.TextNode.class, id);
            return transformEntity(transform, (org.didicero.base.entity.TextNode) entity);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.TextNodeDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#load()
     */
    public org.didicero.base.entity.TextNode load(final java.lang.Long id) throws org.didicero.base.entity.TextNodeDaoException {
        return (org.didicero.base.entity.TextNode) this.load(TRANSFORM_NONE, id);
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#loadAll()
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.TextNode> loadAll() throws org.didicero.base.entity.TextNodeDaoException {
        return (java.util.Collection<org.didicero.base.entity.TextNode>) this.loadAll(TRANSFORM_NONE);
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#loadAll(int)
     */
    public java.util.Collection loadAll(final int transform) throws org.didicero.base.entity.TextNodeDaoException {
        try {
            javax.persistence.Query query = emanager.createNamedQuery("TextNode.findAll");
            java.util.List<org.didicero.base.entity.TextNode> results = query.getResultList();
            this.transformEntities(transform, results);
            return results;
        } catch (Exception ex) {
            throw new org.didicero.base.entity.TextNodeDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#create(org.didicero.base.entity.TextNode)
     */
    public org.didicero.base.entity.TextNode create(org.didicero.base.entity.TextNode textNode) throws org.didicero.base.entity.TextNodeDaoException {
        return (org.didicero.base.entity.TextNode) this.create(TRANSFORM_NONE, textNode);
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#create(int transform, org.didicero.base.entity.TextNode)
     */
    public Object create(final int transform, final org.didicero.base.entity.TextNode textNode) throws org.didicero.base.entity.TextNodeDaoException {
        if (textNode == null) {
            throw new IllegalArgumentException("TextNode.create - 'textNode' can not be null");
        }
        try {
            emanager.persist(textNode);
            emanager.flush();
            return this.transformEntity(transform, textNode);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.TextNodeDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#create(java.util.Collection<org.didicero.base.entity.TextNode>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.TextNode> create(final java.util.Collection<org.didicero.base.entity.TextNode> entities) throws org.didicero.base.entity.TextNodeDaoException {
        return create(TRANSFORM_NONE, entities);
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#create(int, java.util.Collection<org.didicero.base.entity.TextNode>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection create(final int transform, final java.util.Collection<org.didicero.base.entity.TextNode> entities) throws org.didicero.base.entity.TextNodeDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("TextNode.create - 'entities' can not be null");
        }
        java.util.Collection results = new java.util.ArrayList();
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                results.add(create(transform, (org.didicero.base.entity.TextNode) entityIterator.next()));
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.TextNodeDaoException(ex);
        }
        return results;
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#create(java.lang.String, org.didicero.base.entity.Language, java.lang.Long)
     */
    public org.didicero.base.entity.TextNode create(java.lang.String text, org.didicero.base.entity.Language lang, java.lang.Long guid) throws org.didicero.base.entity.TextNodeDaoException {
        return (org.didicero.base.entity.TextNode) this.create(TRANSFORM_NONE, text, lang, guid);
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#create(int, java.lang.String, org.didicero.base.entity.Language, java.lang.Long)
     */
    public Object create(final int transform, java.lang.String text, org.didicero.base.entity.Language lang, java.lang.Long guid) throws org.didicero.base.entity.TextNodeDaoException {
        org.didicero.base.entity.TextNode entity = new org.didicero.base.entity.TextNode();
        entity.setText(text);
        entity.setLang(lang);
        entity.setGuid(guid);
        return this.create(transform, entity);
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#update(org.didicero.base.entity.TextNode)
     */
    public void update(org.didicero.base.entity.TextNode textNode) throws org.didicero.base.entity.TextNodeDaoException {
        if (textNode == null) {
            throw new IllegalArgumentException("TextNode.update - 'textNode' can not be null");
        }
        try {
            emanager.merge(textNode);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.TextNodeDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#update(java.util.Collection<org.didicero.base.entity.TextNode>)
     */
    public void update(final java.util.Collection<org.didicero.base.entity.TextNode> entities) throws org.didicero.base.entity.TextNodeDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("TextNode.update - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                update((org.didicero.base.entity.TextNode) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.TextNodeDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#remove(org.didicero.base.entity.TextNode)
     */
    public void remove(org.didicero.base.entity.TextNode textNode) throws org.didicero.base.entity.TextNodeDaoException {
        if (textNode == null) {
            throw new IllegalArgumentException("TextNode.remove - 'textNode' can not be null");
        }
        try {
            emanager.remove(textNode);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.TextNodeDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#remove(java.lang.Long)
     */
    public void remove(java.lang.Long id) throws org.didicero.base.entity.TextNodeDaoException {
        if (id == null) {
            throw new IllegalArgumentException("TextNode.remove - 'id' can not be null");
        }
        try {
            final org.didicero.base.entity.TextNode entity = (org.didicero.base.entity.TextNode) this.load(id);
            if (entity != null) {
                this.remove(entity);
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.TextNodeDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.TextNodeDao#remove(java.util.Collection<org.didicero.base.entity.TextNode>)
     */
    public void remove(java.util.Collection<org.didicero.base.entity.TextNode> entities) throws org.didicero.base.entity.TextNodeDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("TextNode.remove - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                remove((org.didicero.base.entity.TextNode) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.TextNodeDaoException(ex);
        }
    }

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>org.didicero.base.entity.TextNodeDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link org.didicero.base.entity.TextNodeDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,java.util.Collection)
     */
    protected Object transformEntity(final int transform, final org.didicero.base.entity.TextNode entity) {
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
     * {@link #transformEntity(int, org.didicero.base.entity.TextNode)}
     * method. This method does not instantiate a new collection.
     * <p/>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>org.didicero.base.entity.TextNodeDao</code>
     * @param entities the collection of entities to transform
     * @return the same collection as the argument, but this time containing the transformed entities
     * @see #transformEntity(int, org.didicero.base.entity.TextNode)
     */
    protected void transformEntities(final int transform, final java.util.Collection entities) {
        switch(transform) {
            case TRANSFORM_NONE:
            default:
        }
    }
}
