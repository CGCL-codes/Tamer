package org.openuss.registration;

/**
 * <p>
 * Base Spring DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>org.openuss.registration.InstituteActivationCode</code>.
 * </p>
 *
 * @see org.openuss.registration.InstituteActivationCode
 */
public abstract class InstituteActivationCodeDaoBase extends org.springframework.orm.hibernate3.support.HibernateDaoSupport implements org.openuss.registration.InstituteActivationCodeDao {

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#load(int, java.lang.Long)
     */
    public java.lang.Object load(final int transform, final java.lang.Long id) {
        if (id == null) {
            throw new IllegalArgumentException("InstituteActivationCode.load - 'id' can not be null");
        }
        final java.lang.Object entity = this.getHibernateTemplate().get(org.openuss.registration.InstituteActivationCodeImpl.class, id);
        return transformEntity(transform, (org.openuss.registration.InstituteActivationCode) entity);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#load(java.lang.Long)
     */
    public org.openuss.registration.InstituteActivationCode load(java.lang.Long id) {
        return (org.openuss.registration.InstituteActivationCode) this.load(TRANSFORM_NONE, id);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#loadAll()
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.openuss.registration.InstituteActivationCode> loadAll() {
        return this.loadAll(TRANSFORM_NONE);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#loadAll(int)
     */
    public java.util.Collection loadAll(final int transform) {
        final java.util.Collection results = this.getHibernateTemplate().loadAll(org.openuss.registration.InstituteActivationCodeImpl.class);
        this.transformEntities(transform, results);
        return results;
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#create(org.openuss.registration.InstituteActivationCode)
     */
    public org.openuss.registration.InstituteActivationCode create(org.openuss.registration.InstituteActivationCode instituteActivationCode) {
        return (org.openuss.registration.InstituteActivationCode) this.create(TRANSFORM_NONE, instituteActivationCode);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#create(int transform, org.openuss.registration.InstituteActivationCode)
     */
    public java.lang.Object create(final int transform, final org.openuss.registration.InstituteActivationCode instituteActivationCode) {
        if (instituteActivationCode == null) {
            throw new IllegalArgumentException("InstituteActivationCode.create - 'instituteActivationCode' can not be null");
        }
        this.getHibernateTemplate().save(instituteActivationCode);
        return this.transformEntity(transform, instituteActivationCode);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#create(java.util.Collection<org.openuss.registration.InstituteActivationCode>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.openuss.registration.InstituteActivationCode> create(final java.util.Collection<org.openuss.registration.InstituteActivationCode> entities) {
        return create(TRANSFORM_NONE, entities);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#create(int, java.util.Collection<org.openuss.registration.InstituteActivationCode>)
     */
    public java.util.Collection create(final int transform, final java.util.Collection<org.openuss.registration.InstituteActivationCode> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("InstituteActivationCode.create - 'entities' can not be null");
        }
        this.getHibernateTemplate().execute(new org.springframework.orm.hibernate3.HibernateCallback() {

            public java.lang.Object doInHibernate(org.hibernate.Session session) throws org.hibernate.HibernateException {
                for (java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                    create(transform, (org.openuss.registration.InstituteActivationCode) entityIterator.next());
                }
                return null;
            }
        }, true);
        return entities;
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#update(org.openuss.registration.InstituteActivationCode)
     */
    public void update(org.openuss.registration.InstituteActivationCode instituteActivationCode) {
        if (instituteActivationCode == null) {
            throw new IllegalArgumentException("InstituteActivationCode.update - 'instituteActivationCode' can not be null");
        }
        try {
            this.getHibernateTemplate().update(instituteActivationCode);
        } catch (org.springframework.dao.DataAccessException ex) {
            if (ex.getCause() instanceof org.hibernate.NonUniqueObjectException) {
                logger.debug("Catched NonUniqueObjectException " + ex.getCause().getMessage());
                getSession().merge(instituteActivationCode);
            } else {
                throw ex;
            }
        }
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#update(java.util.Collection<org.openuss.registration.InstituteActivationCode>)
     */
    public void update(final java.util.Collection<org.openuss.registration.InstituteActivationCode> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("InstituteActivationCode.update - 'entities' can not be null");
        }
        this.getHibernateTemplate().execute(new org.springframework.orm.hibernate3.HibernateCallback() {

            public java.lang.Object doInHibernate(org.hibernate.Session session) throws org.hibernate.HibernateException {
                for (java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                    update((org.openuss.registration.InstituteActivationCode) entityIterator.next());
                }
                return null;
            }
        }, true);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#remove(org.openuss.registration.InstituteActivationCode)
     */
    public void remove(org.openuss.registration.InstituteActivationCode instituteActivationCode) {
        if (instituteActivationCode == null) {
            throw new IllegalArgumentException("InstituteActivationCode.remove - 'instituteActivationCode' can not be null");
        }
        this.getHibernateTemplate().delete(instituteActivationCode);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#remove(java.lang.Long)
     */
    public void remove(java.lang.Long id) {
        if (id == null) {
            throw new IllegalArgumentException("InstituteActivationCode.remove - 'id can not be null");
        }
        org.openuss.registration.InstituteActivationCode entity = this.load(id);
        if (entity != null) {
            this.remove(entity);
        }
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#remove(java.util.Collection<org.openuss.registration.InstituteActivationCode>)
     */
    public void remove(java.util.Collection<org.openuss.registration.InstituteActivationCode> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("InstituteActivationCode.remove - 'entities' can not be null");
        }
        this.getHibernateTemplate().deleteAll(entities);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#findByActivationCode(java.lang.String)
     */
    public org.openuss.registration.InstituteActivationCode findByActivationCode(java.lang.String code) {
        return (org.openuss.registration.InstituteActivationCode) this.findByActivationCode(TRANSFORM_NONE, code);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#findByActivationCode(java.lang.String, java.lang.String)
     */
    public org.openuss.registration.InstituteActivationCode findByActivationCode(final java.lang.String queryString, final java.lang.String code) {
        return (org.openuss.registration.InstituteActivationCode) this.findByActivationCode(TRANSFORM_NONE, queryString, code);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#findByActivationCode(int, java.lang.String)
     */
    public java.lang.Object findByActivationCode(final int transform, final java.lang.String code) {
        return this.findByActivationCode(transform, "from org.openuss.registration.InstituteActivationCode as instituteActivationCode where instituteActivationCode.code = ?", code);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#findByActivationCode(int, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public java.lang.Object findByActivationCode(final int transform, final java.lang.String queryString, final java.lang.String code) {
        try {
            org.hibernate.Query queryObject = super.getSession(false).createQuery(queryString);
            queryObject.setCacheable(true);
            queryObject.setParameter(0, code);
            java.util.Set results = new java.util.LinkedHashSet(queryObject.list());
            java.lang.Object result = null;
            if (results != null) {
                if (results.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException("More than one instance of 'org.openuss.registration.InstituteActivationCode" + "' was found when executing query --> '" + queryString + "'");
                } else if (results.size() == 1) {
                    result = results.iterator().next();
                }
            }
            result = transformEntity(transform, (org.openuss.registration.InstituteActivationCode) result);
            return result;
        } catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#findByInstitute(org.openuss.lecture.Institute)
     */
    public java.util.List findByInstitute(org.openuss.lecture.Institute institute) {
        return this.findByInstitute(TRANSFORM_NONE, institute);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#findByInstitute(java.lang.String, org.openuss.lecture.Institute)
     */
    public java.util.List findByInstitute(final java.lang.String queryString, final org.openuss.lecture.Institute institute) {
        return this.findByInstitute(TRANSFORM_NONE, queryString, institute);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#findByInstitute(int, org.openuss.lecture.Institute)
     */
    public java.util.List findByInstitute(final int transform, final org.openuss.lecture.Institute institute) {
        return this.findByInstitute(transform, "from org.openuss.registration.InstituteActivationCode as instituteActivationCode where instituteActivationCode.institute = ?", institute);
    }

    /**
     * @see org.openuss.registration.InstituteActivationCodeDao#findByInstitute(int, java.lang.String, org.openuss.lecture.Institute)
     */
    @SuppressWarnings("unchecked")
    public java.util.List findByInstitute(final int transform, final java.lang.String queryString, final org.openuss.lecture.Institute institute) {
        try {
            org.hibernate.Query queryObject = super.getSession(false).createQuery(queryString);
            queryObject.setCacheable(true);
            queryObject.setParameter(0, institute);
            java.util.List results = queryObject.list();
            transformEntities(transform, results);
            return results;
        } catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>org.openuss.registration.InstituteActivationCodeDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link org.openuss.registration.InstituteActivationCodeDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,java.util.Collection)
     */
    protected java.lang.Object transformEntity(final int transform, final org.openuss.registration.InstituteActivationCode entity) {
        java.lang.Object target = null;
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
     * {@link #transformEntity(int,org.openuss.registration.InstituteActivationCode)}
     * method. This method does not instantiate a new collection.
     * <p/>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>org.openuss.registration.InstituteActivationCodeDao</code>
     * @param entities the collection of entities to transform
     * @see #transformEntity(int,org.openuss.registration.InstituteActivationCode)
     */
    protected void transformEntities(final int transform, final java.util.Collection entities) {
        switch(transform) {
            case TRANSFORM_NONE:
            default:
        }
    }
}
