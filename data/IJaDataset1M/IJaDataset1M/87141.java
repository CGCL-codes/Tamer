package org.didicero.base.entity;

/**
 * <p>
 * Base EJB3 DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>org.didicero.base.entity.MathFormula</code>.
 * </p>
 *
 * @see org.didicero.base.entity.MathFormulaDao
 */
@javax.ejb.TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
@javax.ejb.Local({ org.didicero.base.entity.MathFormulaDao.class })
@javax.annotation.security.PermitAll
public abstract class MathFormulaDaoBase implements org.didicero.base.entity.MathFormulaDao {

    @javax.annotation.Resource
    protected javax.ejb.SessionContext context;

    /**
     * Inject persistence context didicero     
     */
    @javax.persistence.PersistenceContext(unitName = "didicero")
    protected javax.persistence.EntityManager emanager;

    /**
     * @see org.didicero.base.entity.MathFormulaDao#load(int,)
     */
    public Object load(final int transform, final java.lang.Long id) throws org.didicero.base.entity.MathFormulaDaoException {
        if (id == null) {
            throw new IllegalArgumentException("MathFormula.load - 'id' can not be null");
        }
        try {
            final Object entity = (org.didicero.base.entity.MathFormula) emanager.find(org.didicero.base.entity.MathFormula.class, id);
            return transformEntity(transform, (org.didicero.base.entity.MathFormula) entity);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MathFormulaDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#load()
     */
    public org.didicero.base.entity.MathFormula load(final java.lang.Long id) throws org.didicero.base.entity.MathFormulaDaoException {
        return (org.didicero.base.entity.MathFormula) this.load(TRANSFORM_NONE, id);
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#loadAll()
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.MathFormula> loadAll() throws org.didicero.base.entity.MathFormulaDaoException {
        return (java.util.Collection<org.didicero.base.entity.MathFormula>) this.loadAll(TRANSFORM_NONE);
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#loadAll(int)
     */
    public java.util.Collection loadAll(final int transform) throws org.didicero.base.entity.MathFormulaDaoException {
        try {
            javax.persistence.Query query = emanager.createNamedQuery("MathFormula.findAll");
            java.util.List<org.didicero.base.entity.MathFormula> results = query.getResultList();
            this.transformEntities(transform, results);
            return results;
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MathFormulaDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#create(org.didicero.base.entity.MathFormula)
     */
    public org.didicero.base.entity.MathFormula create(org.didicero.base.entity.MathFormula mathFormula) throws org.didicero.base.entity.MathFormulaDaoException {
        return (org.didicero.base.entity.MathFormula) this.create(TRANSFORM_NONE, mathFormula);
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#create(int transform, org.didicero.base.entity.MathFormula)
     */
    public Object create(final int transform, final org.didicero.base.entity.MathFormula mathFormula) throws org.didicero.base.entity.MathFormulaDaoException {
        if (mathFormula == null) {
            throw new IllegalArgumentException("MathFormula.create - 'mathFormula' can not be null");
        }
        try {
            emanager.persist(mathFormula);
            emanager.flush();
            return this.transformEntity(transform, mathFormula);
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MathFormulaDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#create(java.util.Collection<org.didicero.base.entity.MathFormula>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.didicero.base.entity.MathFormula> create(final java.util.Collection<org.didicero.base.entity.MathFormula> entities) throws org.didicero.base.entity.MathFormulaDaoException {
        return create(TRANSFORM_NONE, entities);
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#create(int, java.util.Collection<org.didicero.base.entity.MathFormula>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection create(final int transform, final java.util.Collection<org.didicero.base.entity.MathFormula> entities) throws org.didicero.base.entity.MathFormulaDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("MathFormula.create - 'entities' can not be null");
        }
        java.util.Collection results = new java.util.ArrayList();
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                results.add(create(transform, (org.didicero.base.entity.MathFormula) entityIterator.next()));
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MathFormulaDaoException(ex);
        }
        return results;
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#create(int, int, java.lang.String, java.lang.String, org.didicero.base.types.MimeType, java.lang.Boolean, java.util.Date, java.util.Date, java.lang.String, java.lang.String, java.lang.String)
     */
    public org.didicero.base.entity.MathFormula create(int orderNr, int length, java.lang.String data, java.lang.String name, org.didicero.base.types.MimeType mimeType, java.lang.Boolean isWorkingCopy, java.util.Date createdate, java.util.Date changedate, java.lang.String uri, java.lang.String createuser, java.lang.String changeuser) throws org.didicero.base.entity.MathFormulaDaoException {
        return (org.didicero.base.entity.MathFormula) this.create(TRANSFORM_NONE, orderNr, length, data, name, mimeType, isWorkingCopy, createdate, changedate, uri, createuser, changeuser);
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#create(int, int, int, java.lang.String, java.lang.String, org.didicero.base.types.MimeType, java.lang.Boolean, java.util.Date, java.util.Date, java.lang.String, java.lang.String, java.lang.String)
     */
    public Object create(final int transform, int orderNr, int length, java.lang.String data, java.lang.String name, org.didicero.base.types.MimeType mimeType, java.lang.Boolean isWorkingCopy, java.util.Date createdate, java.util.Date changedate, java.lang.String uri, java.lang.String createuser, java.lang.String changeuser) throws org.didicero.base.entity.MathFormulaDaoException {
        org.didicero.base.entity.MathFormula entity = new org.didicero.base.entity.MathFormula();
        entity.setOrderNr(orderNr);
        entity.setLength(length);
        entity.setData(data);
        entity.setName(name);
        entity.setMimeType(mimeType);
        entity.setIsWorkingCopy(isWorkingCopy);
        entity.setCreatedate(createdate);
        entity.setChangedate(changedate);
        entity.setUri(uri);
        entity.setCreateuser(createuser);
        entity.setChangeuser(changeuser);
        return this.create(transform, entity);
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#update(org.didicero.base.entity.MathFormula)
     */
    public void update(org.didicero.base.entity.MathFormula mathFormula) throws org.didicero.base.entity.MathFormulaDaoException {
        if (mathFormula == null) {
            throw new IllegalArgumentException("MathFormula.update - 'mathFormula' can not be null");
        }
        try {
            emanager.merge(mathFormula);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MathFormulaDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#update(java.util.Collection<org.didicero.base.entity.MathFormula>)
     */
    public void update(final java.util.Collection<org.didicero.base.entity.MathFormula> entities) throws org.didicero.base.entity.MathFormulaDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("MathFormula.update - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                update((org.didicero.base.entity.MathFormula) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MathFormulaDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#remove(org.didicero.base.entity.MathFormula)
     */
    public void remove(org.didicero.base.entity.MathFormula mathFormula) throws org.didicero.base.entity.MathFormulaDaoException {
        if (mathFormula == null) {
            throw new IllegalArgumentException("MathFormula.remove - 'mathFormula' can not be null");
        }
        try {
            emanager.remove(mathFormula);
            emanager.flush();
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MathFormulaDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#remove(java.lang.Long)
     */
    public void remove(java.lang.Long id) throws org.didicero.base.entity.MathFormulaDaoException {
        if (id == null) {
            throw new IllegalArgumentException("MathFormula.remove - 'id' can not be null");
        }
        try {
            final org.didicero.base.entity.MathFormula entity = (org.didicero.base.entity.MathFormula) this.load(id);
            if (entity != null) {
                this.remove(entity);
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MathFormulaDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#remove(java.util.Collection<org.didicero.base.entity.MathFormula>)
     */
    public void remove(java.util.Collection<org.didicero.base.entity.MathFormula> entities) throws org.didicero.base.entity.MathFormulaDaoException {
        if (entities == null) {
            throw new IllegalArgumentException("MathFormula.remove - 'entities' can not be null");
        }
        try {
            for (final java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                remove((org.didicero.base.entity.MathFormula) entityIterator.next());
            }
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MathFormulaDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#findResourceByUri(java.lang.String)
     */
    public org.didicero.base.entity.Resource findResourceByUri(java.lang.String uri) throws org.didicero.base.entity.MathFormulaDaoException {
        return (org.didicero.base.entity.Resource) this.findResourceByUri(TRANSFORM_NONE, uri);
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#findResourceByUri(java.lang.String, java.lang.String)
     */
    public org.didicero.base.entity.Resource findResourceByUri(final java.lang.String queryString, final java.lang.String uri) throws org.didicero.base.entity.MathFormulaDaoException {
        return (org.didicero.base.entity.Resource) this.findResourceByUri(TRANSFORM_NONE, queryString, uri);
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#findResourceByUri(int, java.lang.String)
     */
    public Object findResourceByUri(final int transform, final java.lang.String uri) throws org.didicero.base.entity.MathFormulaDaoException {
        try {
            javax.persistence.Query queryObject = emanager.createNamedQuery("MathFormula.findResourceByUri");
            queryObject.setParameter("uri", uri);
            Object result = queryObject.getSingleResult();
            result = transformEntity(transform, (org.didicero.base.entity.MathFormula) result);
            return result;
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MathFormulaDaoException(ex);
        }
    }

    /**
     * @see org.didicero.base.entity.MathFormulaDao#findResourceByUri(int, java.lang.String, java.lang.String)
     */
    public Object findResourceByUri(final int transform, final java.lang.String queryString, final java.lang.String uri) throws org.didicero.base.entity.MathFormulaDaoException {
        try {
            javax.persistence.Query queryObject = emanager.createQuery(queryString);
            queryObject.setParameter("uri", uri);
            Object result = queryObject.getSingleResult();
            result = transformEntity(transform, (org.didicero.base.entity.MathFormula) result);
            return result;
        } catch (Exception ex) {
            throw new org.didicero.base.entity.MathFormulaDaoException(ex);
        }
    }

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>org.didicero.base.entity.MathFormulaDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link org.didicero.base.entity.MathFormulaDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,java.util.Collection)
     */
    protected Object transformEntity(final int transform, final org.didicero.base.entity.MathFormula entity) {
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
     * {@link #transformEntity(int, org.didicero.base.entity.MathFormula)}
     * method. This method does not instantiate a new collection.
     * <p/>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>org.didicero.base.entity.MathFormulaDao</code>
     * @param entities the collection of entities to transform
     * @return the same collection as the argument, but this time containing the transformed entities
     * @see #transformEntity(int, org.didicero.base.entity.MathFormula)
     */
    protected void transformEntities(final int transform, final java.util.Collection entities) {
        switch(transform) {
            case TRANSFORM_NONE:
            default:
        }
    }
}
