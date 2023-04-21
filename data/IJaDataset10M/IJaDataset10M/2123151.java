package org.openuss.statistics;

/**
 * <p>
 * Base Spring DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>org.openuss.statistics.OnlineSession</code>.
 * </p>
 *
 * @see org.openuss.statistics.OnlineSession
 */
public abstract class OnlineSessionDaoBase extends org.springframework.orm.hibernate3.support.HibernateDaoSupport implements org.openuss.statistics.OnlineSessionDao {

    /**
     * @see org.openuss.statistics.OnlineSessionDao#load(int, java.lang.Long)
     */
    public java.lang.Object load(final int transform, final java.lang.Long id) {
        if (id == null) {
            throw new IllegalArgumentException("OnlineSession.load - 'id' can not be null");
        }
        final java.lang.Object entity = this.getHibernateTemplate().get(org.openuss.statistics.OnlineSessionImpl.class, id);
        return transformEntity(transform, (org.openuss.statistics.OnlineSession) entity);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#load(java.lang.Long)
     */
    public org.openuss.statistics.OnlineSession load(java.lang.Long id) {
        return (org.openuss.statistics.OnlineSession) this.load(TRANSFORM_NONE, id);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#loadAll()
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.openuss.statistics.OnlineSession> loadAll() {
        return this.loadAll(TRANSFORM_NONE);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#loadAll(int)
     */
    public java.util.Collection loadAll(final int transform) {
        final java.util.Collection results = this.getHibernateTemplate().loadAll(org.openuss.statistics.OnlineSessionImpl.class);
        this.transformEntities(transform, results);
        return results;
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#create(org.openuss.statistics.OnlineSession)
     */
    public org.openuss.statistics.OnlineSession create(org.openuss.statistics.OnlineSession onlineSession) {
        return (org.openuss.statistics.OnlineSession) this.create(TRANSFORM_NONE, onlineSession);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#create(int transform, org.openuss.statistics.OnlineSession)
     */
    public java.lang.Object create(final int transform, final org.openuss.statistics.OnlineSession onlineSession) {
        if (onlineSession == null) {
            throw new IllegalArgumentException("OnlineSession.create - 'onlineSession' can not be null");
        }
        this.getHibernateTemplate().save(onlineSession);
        return this.transformEntity(transform, onlineSession);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#create(java.util.Collection<org.openuss.statistics.OnlineSession>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.openuss.statistics.OnlineSession> create(final java.util.Collection<org.openuss.statistics.OnlineSession> entities) {
        return create(TRANSFORM_NONE, entities);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#create(int, java.util.Collection<org.openuss.statistics.OnlineSession>)
     */
    public java.util.Collection create(final int transform, final java.util.Collection<org.openuss.statistics.OnlineSession> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("OnlineSession.create - 'entities' can not be null");
        }
        this.getHibernateTemplate().execute(new org.springframework.orm.hibernate3.HibernateCallback() {

            public java.lang.Object doInHibernate(org.hibernate.Session session) throws org.hibernate.HibernateException {
                for (java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                    create(transform, (org.openuss.statistics.OnlineSession) entityIterator.next());
                }
                return null;
            }
        }, true);
        return entities;
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#update(org.openuss.statistics.OnlineSession)
     */
    public void update(org.openuss.statistics.OnlineSession onlineSession) {
        if (onlineSession == null) {
            throw new IllegalArgumentException("OnlineSession.update - 'onlineSession' can not be null");
        }
        try {
            this.getHibernateTemplate().update(onlineSession);
        } catch (org.springframework.dao.DataAccessException ex) {
            if (ex.getCause() instanceof org.hibernate.NonUniqueObjectException) {
                logger.debug("Catched NonUniqueObjectException " + ex.getCause().getMessage());
                getSession().merge(onlineSession);
            } else {
                throw ex;
            }
        }
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#update(java.util.Collection<org.openuss.statistics.OnlineSession>)
     */
    public void update(final java.util.Collection<org.openuss.statistics.OnlineSession> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("OnlineSession.update - 'entities' can not be null");
        }
        this.getHibernateTemplate().execute(new org.springframework.orm.hibernate3.HibernateCallback() {

            public java.lang.Object doInHibernate(org.hibernate.Session session) throws org.hibernate.HibernateException {
                for (java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                    update((org.openuss.statistics.OnlineSession) entityIterator.next());
                }
                return null;
            }
        }, true);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#remove(org.openuss.statistics.OnlineSession)
     */
    public void remove(org.openuss.statistics.OnlineSession onlineSession) {
        if (onlineSession == null) {
            throw new IllegalArgumentException("OnlineSession.remove - 'onlineSession' can not be null");
        }
        this.getHibernateTemplate().delete(onlineSession);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#remove(java.lang.Long)
     */
    public void remove(java.lang.Long id) {
        if (id == null) {
            throw new IllegalArgumentException("OnlineSession.remove - 'id can not be null");
        }
        org.openuss.statistics.OnlineSession entity = this.load(id);
        if (entity != null) {
            this.remove(entity);
        }
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#remove(java.util.Collection<org.openuss.statistics.OnlineSession>)
     */
    public void remove(java.util.Collection<org.openuss.statistics.OnlineSession> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("OnlineSession.remove - 'entities' can not be null");
        }
        this.getHibernateTemplate().deleteAll(entities);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#findUserSessions(org.openuss.statistics.UserSessionCriteria)
     */
    public java.util.List findUserSessions(org.openuss.statistics.UserSessionCriteria criteria) {
        return this.findUserSessions(TRANSFORM_NONE, criteria);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#findUserSessions(int, java.lang.String, org.openuss.statistics.UserSessionCriteria)
     */
    public java.util.List findUserSessions(final int transform, final org.openuss.statistics.UserSessionCriteria criteria) {
        try {
            org.andromda.spring.CriteriaSearch criteriaSearch = new org.andromda.spring.CriteriaSearch(super.getSession(false), org.openuss.statistics.OnlineSessionImpl.class);
            criteriaSearch.getConfiguration().setFirstResult(criteria.getFirstResult());
            criteriaSearch.getConfiguration().setFetchSize(criteria.getFetchSize());
            criteriaSearch.getConfiguration().setMaximumResultSize(criteria.getMaximumResultSize());
            org.andromda.spring.CriteriaSearchParameter parameter1 = new org.andromda.spring.CriteriaSearchParameter(criteria.getStartTime(), "startTime", org.andromda.spring.CriteriaSearchParameter.LESS_THAN_OR_EQUAL_COMPARATOR);
            parameter1.setOrderDirection(org.andromda.spring.CriteriaSearchParameter.ORDER_DESC);
            parameter1.setOrderRelevance(-1);
            criteriaSearch.addParameter(parameter1);
            org.andromda.spring.CriteriaSearchParameter parameter2 = new org.andromda.spring.CriteriaSearchParameter(criteria.getEndTime(), "endTime", true, org.andromda.spring.CriteriaSearchParameter.GREATER_THAN_COMPARATOR);
            parameter2.setOrderDirection(org.andromda.spring.CriteriaSearchParameter.ORDER_DESC);
            parameter2.setOrderRelevance(-1);
            criteriaSearch.addParameter(parameter2);
            java.util.List results = criteriaSearch.executeAsList();
            transformEntities(transform, results);
            return results;
        } catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#findActiveSessionByUser(org.openuss.security.User)
     */
    public java.util.List findActiveSessionByUser(org.openuss.security.User user) {
        return this.findActiveSessionByUser(TRANSFORM_NONE, user);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#findActiveSessionByUser(java.lang.String, org.openuss.security.User)
     */
    public java.util.List findActiveSessionByUser(final java.lang.String queryString, final org.openuss.security.User user) {
        return this.findActiveSessionByUser(TRANSFORM_NONE, queryString, user);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#findActiveSessionByUser(int, org.openuss.security.User)
     */
    public java.util.List findActiveSessionByUser(final int transform, final org.openuss.security.User user) {
        return this.findActiveSessionByUser(transform, "from OnlineSessionImpl as s WHERE s.endTime is null AND s.user = ?", user);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#findActiveSessionByUser(int, java.lang.String, org.openuss.security.User)
     */
    @SuppressWarnings("unchecked")
    public java.util.List findActiveSessionByUser(final int transform, final java.lang.String queryString, final org.openuss.security.User user) {
        try {
            org.hibernate.Query queryObject = super.getSession(false).createQuery(queryString);
            queryObject.setCacheable(true);
            queryObject.setParameter(0, user);
            java.util.List results = queryObject.list();
            transformEntities(transform, results);
            return results;
        } catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#findByUser(org.openuss.security.User)
     */
    public java.util.List findByUser(org.openuss.security.User user) {
        return this.findByUser(TRANSFORM_NONE, user);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#findByUser(java.lang.String, org.openuss.security.User)
     */
    public java.util.List findByUser(final java.lang.String queryString, final org.openuss.security.User user) {
        return this.findByUser(TRANSFORM_NONE, queryString, user);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#findByUser(int, org.openuss.security.User)
     */
    public java.util.List findByUser(final int transform, final org.openuss.security.User user) {
        return this.findByUser(transform, "from org.openuss.statistics.OnlineSession as onlineSession where onlineSession.user = ?", user);
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#findByUser(int, java.lang.String, org.openuss.security.User)
     */
    @SuppressWarnings("unchecked")
    public java.util.List findByUser(final int transform, final java.lang.String queryString, final org.openuss.security.User user) {
        try {
            org.hibernate.Query queryObject = super.getSession(false).createQuery(queryString);
            queryObject.setCacheable(true);
            queryObject.setParameter(0, user);
            java.util.List results = queryObject.list();
            transformEntities(transform, results);
            return results;
        } catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#findActiveUsers()
     */
    public java.util.List findActiveUsers() {
        try {
            return this.handleFindActiveUsers();
        } catch (Throwable th) {
            throw new java.lang.RuntimeException("Error performing 'org.openuss.statistics.OnlineSessionDao.findActiveUsers()' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #findActiveUsers()}
      */
    protected abstract java.util.List handleFindActiveUsers() throws java.lang.Exception;

    /**
     * @see org.openuss.statistics.OnlineSessionDao#loadOnlineInfo()
     */
    public org.openuss.statistics.OnlineInfo loadOnlineInfo() {
        try {
            return this.handleLoadOnlineInfo();
        } catch (Throwable th) {
            throw new java.lang.RuntimeException("Error performing 'org.openuss.statistics.OnlineSessionDao.loadOnlineInfo()' --> " + th, th);
        }
    }

    /**
      * Performs the core logic for {@link #loadOnlineInfo()}
      */
    protected abstract org.openuss.statistics.OnlineInfo handleLoadOnlineInfo() throws java.lang.Exception;

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>org.openuss.statistics.OnlineSessionDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     * <p/>
     * This method will return instances of these types:
     * <ul>
     *   <li>{@link org.openuss.statistics.OnlineSession} - {@link #TRANSFORM_NONE}</li>
     *   <li>{@link org.openuss.statistics.OnlineUserInfo} - {@link TRANSFORM_ONLINEUSERINFO}</li>
     * </ul>
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link org.openuss.statistics.OnlineSessionDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,java.util.Collection)
     */
    protected java.lang.Object transformEntity(final int transform, final org.openuss.statistics.OnlineSession entity) {
        java.lang.Object target = null;
        if (entity != null) {
            switch(transform) {
                case TRANSFORM_ONLINEUSERINFO:
                    target = toOnlineUserInfo(entity);
                    break;
                case TRANSFORM_NONE:
                default:
                    target = entity;
            }
        }
        return target;
    }

    /**
     * Transforms a collection of entities using the
     * {@link #transformEntity(int,org.openuss.statistics.OnlineSession)}
     * method. This method does not instantiate a new collection.
     * <p/>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>org.openuss.statistics.OnlineSessionDao</code>
     * @param entities the collection of entities to transform
     * @see #transformEntity(int,org.openuss.statistics.OnlineSession)
     */
    protected void transformEntities(final int transform, final java.util.Collection entities) {
        switch(transform) {
            case TRANSFORM_ONLINEUSERINFO:
                toOnlineUserInfoCollection(entities);
                break;
            case TRANSFORM_NONE:
            default:
        }
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#toOnlineUserInfoCollection(java.util.Collection)
     */
    public final void toOnlineUserInfoCollection(java.util.Collection entities) {
        if (entities != null) {
            org.apache.commons.collections.CollectionUtils.transform(entities, ONLINEUSERINFO_TRANSFORMER);
        }
    }

    /**
     * Default implementation for transforming the results of a report query into a value object. This
     * implementation exists for convenience reasons only. It needs only be overridden in the
     * {@link OnlineSessionDaoImpl} class if you intend to use reporting queries.
     * @see org.openuss.statistics.OnlineSessionDao#toOnlineUserInfo(org.openuss.statistics.OnlineSession)
     */
    protected org.openuss.statistics.OnlineUserInfo toOnlineUserInfo(java.lang.Object[] row) {
        org.openuss.statistics.OnlineUserInfo target = null;
        if (row != null) {
            final int numberOfObjects = row.length;
            for (int ctr = 0; ctr < numberOfObjects; ctr++) {
                final java.lang.Object object = row[ctr];
                if (object instanceof org.openuss.statistics.OnlineSession) {
                    target = this.toOnlineUserInfo((org.openuss.statistics.OnlineSession) object);
                    break;
                }
            }
        }
        return target;
    }

    /**
     * This anonymous transformer is designed to transform entities or report query results
     * (which result in an array of objects) to {@link org.openuss.statistics.OnlineUserInfo}
     * using the Jakarta Commons-Collections Transformation API.
     */
    private org.apache.commons.collections.Transformer ONLINEUSERINFO_TRANSFORMER = new org.apache.commons.collections.Transformer() {

        public java.lang.Object transform(java.lang.Object input) {
            java.lang.Object result = null;
            if (input instanceof org.openuss.statistics.OnlineSession) {
                result = toOnlineUserInfo((org.openuss.statistics.OnlineSession) input);
            } else if (input instanceof java.lang.Object[]) {
                result = toOnlineUserInfo((java.lang.Object[]) input);
            }
            return result;
        }
    };

    /**
     * @see org.openuss.statistics.OnlineSessionDao#onlineUserInfoToEntityCollection(java.util.Collection)
     */
    public final void onlineUserInfoToEntityCollection(java.util.Collection instances) {
        if (instances != null) {
            for (final java.util.Iterator iterator = instances.iterator(); iterator.hasNext(); ) {
                if (!(iterator.next() instanceof org.openuss.statistics.OnlineUserInfo)) {
                    iterator.remove();
                }
            }
            org.apache.commons.collections.CollectionUtils.transform(instances, OnlineUserInfoToEntityTransformer);
        }
    }

    private final org.apache.commons.collections.Transformer OnlineUserInfoToEntityTransformer = new org.apache.commons.collections.Transformer() {

        public java.lang.Object transform(java.lang.Object input) {
            return onlineUserInfoToEntity((org.openuss.statistics.OnlineUserInfo) input);
        }
    };

    /**
     * @see org.openuss.statistics.OnlineSessionDao#toOnlineUserInfo(org.openuss.statistics.OnlineSession, org.openuss.statistics.OnlineUserInfo)
     */
    public void toOnlineUserInfo(org.openuss.statistics.OnlineSession source, org.openuss.statistics.OnlineUserInfo target) {
        target.setId(source.getId());
        target.setStartTime(source.getStartTime());
        target.setEndTime(source.getEndTime());
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#toOnlineUserInfo(org.openuss.statistics.OnlineSession)
     */
    public org.openuss.statistics.OnlineUserInfo toOnlineUserInfo(final org.openuss.statistics.OnlineSession entity) {
        final org.openuss.statistics.OnlineUserInfo target = new org.openuss.statistics.OnlineUserInfo();
        this.toOnlineUserInfo(entity, target);
        return target;
    }

    /**
     * @see org.openuss.statistics.OnlineSessionDao#onlineUserInfoToEntity(org.openuss.statistics.OnlineUserInfo, org.openuss.statistics.OnlineSession)
     */
    public void onlineUserInfoToEntity(org.openuss.statistics.OnlineUserInfo source, org.openuss.statistics.OnlineSession target, boolean copyIfNull) {
        if (copyIfNull || source.getStartTime() != null) {
            target.setStartTime(source.getStartTime());
        }
        if (copyIfNull || source.getEndTime() != null) {
            target.setEndTime(source.getEndTime());
        }
    }
}
