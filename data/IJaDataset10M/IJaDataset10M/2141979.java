package org.didicero.base.entity;

/**
 * <p>
 * Base EJB3 DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>org.didicero.base.entity.MaterialReference</code>.
 * </p>
 *
 * @see org.didicero.base.entity.MaterialReferenceDao
 */
@javax.ejb.TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
@javax.ejb.Local({ org.didicero.base.entity.MaterialReferenceDao.class })
@javax.annotation.security.PermitAll
public abstract class MaterialReferenceDaoBase implements org.didicero.base.entity.MaterialReferenceDao {

    @javax.annotation.Resource
    protected javax.ejb.SessionContext context;

    /**
     * Inject persistence context didicero     
     */
    @javax.persistence.PersistenceContext(unitName = "didicero")
    protected javax.persistence.EntityManager emanager;

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#load(int,)
     */
    public Object load(final int transform, final java.lang.Long id) throws org.didicero.base.entity.MaterialReferenceDaoException {
        if (id == null) {
            throw new IllegalArgumentException("MaterialReference.load - 'id' can not be null");
        }
        try {
            final Object entity = (org.didicero.base.entity.MaterialReference) emanager.find(org.didicero.base.entity.MaterialReference.class, id);
            return transformEntity(transform, (org.didicero.base.entity.MaterialReference) entity);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MaterialReferenceDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#load()
     */
    public org.didicero.base.entity.MaterialReference load(final java.lang.Long id) throws org.didicero.base.entity.MaterialReferenceDaoException {
        return (org.didicero.base.entity.MaterialReference) this.load(TRANSFORM_NONE, id);
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#loadAll()
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.MaterialReference> loadAll() throws org.didicero.base.entity.MaterialReferenceDaoException {
        return (java.util.Collection<org.didicero.base.entity.MaterialReference>) this.loadAll(TRANSFORM_NONE);
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#loadAll(int)
     */
    public java.util.Collection loadAll(final int transform) throws org.didicero.base.entity.MaterialReferenceDaoException {
        try {
            javax.persistence.Query query = emanager.createNamedQuery("MaterialReference.findAll");
            java.util.List<org.didicero.base.entity.MaterialReference> results = query.getResultList();
            this.transformEntities(transform, results);
            return results;
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MaterialReferenceDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#create(org.didicero.base.entity.MaterialReference)
     */
    public org.didicero.base.entity.MaterialReference create(org.didicero.base.entity.MaterialReference materialReference) throws org.didicero.base.entity.MaterialReferenceDaoException {
        return (org.didicero.base.entity.MaterialReference) this.create(TRANSFORM_NONE, materialReference);
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#create(int transform, org.didicero.base.entity.MaterialReference)
     */
    public Object create(final int transform, final org.didicero.base.entity.MaterialReference materialReference) throws org.didicero.base.entity.MaterialReferenceDaoException {
        if (materialReference == null) {
            throw new IllegalArgumentException("MaterialReference.create - 'materialReference' can not be null");
        }
        try {
            emanager.persist(materialReference);
            emanager.flush();
            return this.transformEntity(transform, materialReference);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MaterialReferenceDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#create(java.util.Collection<org.didicero.base.entity.MaterialReference>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.MaterialReference> create(final java.util.Collection<org.didicero.base.entity.MaterialReference> entities) throws org.didicero.base.entity.MaterialReferenceDaoException {
        return create(TRANSFORM_NONE, entities);
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#create(int, java.util.Collection<org.didicero.base.entity.MaterialReference>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection create(final int transform, final java.util.Collection<org.didicero.base.entity.MaterialReference> entities) throws org.didicero.base.entity.MaterialReferenceDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("MaterialReference.create - 'entities' can not be null");
        }
        java.util.Collection results = new java.util.ArrayList();
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                results.add(create(transform, (org.didicero.base.entity.MaterialReference) entityIterator.next()));
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MaterialReferenceDaoException(ex);
        }
        return results;
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#create(boolean, java.net.URI)
     */
    public org.didicero.base.entity.MaterialReference create(boolean verified, java.net.URI uri) throws org.didicero.base.entity.MaterialReferenceDaoException {
        return (org.didicero.base.entity.MaterialReference) this.create(TRANSFORM_NONE, verified, uri);
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#create(int, boolean, java.net.URI)
     */
    public Object create(final int transform, boolean verified, java.net.URI uri) throws org.didicero.base.entity.MaterialReferenceDaoException {
        org.didicero.base.entity.MaterialReference entity = new org.didicero.base.entity.MaterialReference();
        entity.setVerified(verified);
        entity.setUri(uri);
        return this.create(transform, entity);
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#update(org.didicero.base.entity.MaterialReference)
     */
    public void update(org.didicero.base.entity.MaterialReference materialReference) throws org.didicero.base.entity.MaterialReferenceDaoException {
        if (materialReference == null) {
            throw new IllegalArgumentException("MaterialReference.update - 'materialReference' can not be null");
        }
        try {
            emanager.merge(materialReference);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MaterialReferenceDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#update(java.util.Collection<org.didicero.base.entity.MaterialReference>)
     */
    public void update(final java.util.Collection<org.didicero.base.entity.MaterialReference> entities) throws org.didicero.base.entity.MaterialReferenceDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("MaterialReference.update - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                update((org.didicero.base.entity.MaterialReference) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MaterialReferenceDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#remove(org.didicero.base.entity.MaterialReference)
     */
    public void remove(org.didicero.base.entity.MaterialReference materialReference) throws org.didicero.base.entity.MaterialReferenceDaoException {
        if (materialReference == null) {
            throw new IllegalArgumentException("MaterialReference.remove - 'materialReference' can not be null");
        }
        try {
            emanager.remove(materialReference);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MaterialReferenceDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#remove(java.lang.Long)
     */
    public void remove(java.lang.Long id) throws org.didicero.base.entity.MaterialReferenceDaoException {
        if (id == null) {
            throw new IllegalArgumentException("MaterialReference.remove - 'id' can not be null");
        }
        try {
            final org.didicero.base.entity.MaterialReference entity = (org.didicero.base.entity.MaterialReference) this.load(id);
            if (entity != null) {
                this.remove(entity);
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MaterialReferenceDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MaterialReferenceDao#remove(java.util.Collection<org.didicero.base.entity.MaterialReference>)
     */
    public void remove(java.util.Collection<org.didicero.base.entity.MaterialReference> entities) throws org.didicero.base.entity.MaterialReferenceDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("MaterialReference.remove - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                remove((org.didicero.base.entity.MaterialReference) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MaterialReferenceDaoException(ex);
        }
    }

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>org.didicero.base.entity.MaterialReferenceDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link org.didicero.base.entity.MaterialReferenceDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,java.util.Collection)
     */
    protected Object transformEntity(final int transform, final org.didicero.base.entity.MaterialReference entity) {
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
     * {@link #transformEntity(int, org.didicero.base.entity.MaterialReference)}
     * method. This method does not instantiate a new collection.
     * <p/>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>org.didicero.base.entity.MaterialReferenceDao</code>
     * @param entities the collection of entities to transform
     * @return the same collection as the argument, but this time containing the transformed entities
     * @see #transformEntity(int, org.didicero.base.entity.MaterialReference)
     */
    protected void transformEntities(final int transform, final java.util.Collection entities) {
        switch(transform) {
            case TRANSFORM_NONE:
            default:
        }
    }
}
