package org.ministone.portal.domain;

/**
 * <p>
 * Base Spring DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>org.ministone.portal.domain.ViewletConfig</code>.
 * </p>
 *
 * @see org.ministone.portal.domain.ViewletConfig
 * @author Sun Wenju
 * @since 0.1
 */
public abstract class ViewletConfigDaoBase extends org.springframework.orm.hibernate3.support.HibernateDaoSupport implements org.ministone.portal.domain.ViewletConfigDao {

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#load(int, java.lang.String)
     */
    public java.lang.Object load(final int transform, final java.lang.String id) {
        if (id == null) {
            throw new IllegalArgumentException("ViewletConfig.load - 'id' can not be null");
        }
        final java.lang.Object entity = this.getHibernateTemplate().get(org.ministone.portal.domain.ViewletConfigImpl.class, id);
        return transformEntity(transform, (org.ministone.portal.domain.ViewletConfig) entity);
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#load(java.lang.String)
     */
    public org.ministone.portal.domain.ViewletConfig load(java.lang.String id) {
        return (org.ministone.portal.domain.ViewletConfig) this.load(TRANSFORM_NONE, id);
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#loadAll()
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.ministone.portal.domain.ViewletConfig> loadAll() {
        return this.loadAll(TRANSFORM_NONE);
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#loadAll(int)
     */
    public java.util.Collection loadAll(final int transform) {
        final java.util.Collection results = this.getHibernateTemplate().loadAll(org.ministone.portal.domain.ViewletConfigImpl.class);
        this.transformEntities(transform, results);
        return results;
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#create(org.ministone.portal.domain.ViewletConfig)
     */
    public org.ministone.portal.domain.ViewletConfig create(org.ministone.portal.domain.ViewletConfig viewletConfig) {
        return (org.ministone.portal.domain.ViewletConfig) this.create(TRANSFORM_NONE, viewletConfig);
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#create(int transform, org.ministone.portal.domain.ViewletConfig)
     */
    public java.lang.Object create(final int transform, final org.ministone.portal.domain.ViewletConfig viewletConfig) {
        if (viewletConfig == null) {
            throw new IllegalArgumentException("ViewletConfig.create - 'viewletConfig' can not be null");
        }
        this.getHibernateTemplate().save(viewletConfig);
        return this.transformEntity(transform, viewletConfig);
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#create(java.util.Collection<org.ministone.portal.domain.ViewletConfig>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.ministone.portal.domain.ViewletConfig> create(final java.util.Collection<org.ministone.portal.domain.ViewletConfig> entities) {
        return create(TRANSFORM_NONE, entities);
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#create(int, java.util.Collection<org.ministone.portal.domain.ViewletConfig>)
     */
    public java.util.Collection create(final int transform, final java.util.Collection<org.ministone.portal.domain.ViewletConfig> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("ViewletConfig.create - 'entities' can not be null");
        }
        this.getHibernateTemplate().execute(new org.springframework.orm.hibernate3.HibernateCallback() {

            public java.lang.Object doInHibernate(org.hibernate.Session session) throws org.hibernate.HibernateException {
                for (java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                    create(transform, (org.ministone.portal.domain.ViewletConfig) entityIterator.next());
                }
                return null;
            }
        }, true);
        return entities;
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#update(org.ministone.portal.domain.ViewletConfig)
     */
    public void update(org.ministone.portal.domain.ViewletConfig viewletConfig) {
        if (viewletConfig == null) {
            throw new IllegalArgumentException("ViewletConfig.update - 'viewletConfig' can not be null");
        }
        this.getHibernateTemplate().update(viewletConfig);
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#update(java.util.Collection<org.ministone.portal.domain.ViewletConfig>)
     */
    public void update(final java.util.Collection<org.ministone.portal.domain.ViewletConfig> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("ViewletConfig.update - 'entities' can not be null");
        }
        this.getHibernateTemplate().execute(new org.springframework.orm.hibernate3.HibernateCallback() {

            public java.lang.Object doInHibernate(org.hibernate.Session session) throws org.hibernate.HibernateException {
                for (java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                    update((org.ministone.portal.domain.ViewletConfig) entityIterator.next());
                }
                return null;
            }
        }, true);
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#remove(org.ministone.portal.domain.ViewletConfig)
     */
    public void remove(org.ministone.portal.domain.ViewletConfig viewletConfig) {
        if (viewletConfig == null) {
            throw new IllegalArgumentException("ViewletConfig.remove - 'viewletConfig' can not be null");
        }
        this.getHibernateTemplate().delete(viewletConfig);
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#remove(java.lang.String)
     */
    public void remove(java.lang.String id) {
        if (id == null) {
            throw new IllegalArgumentException("ViewletConfig.remove - 'id' can not be null");
        }
        org.ministone.portal.domain.ViewletConfig entity = this.load(id);
        if (entity != null) {
            this.remove(entity);
        }
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#remove(java.util.Collection<org.ministone.portal.domain.ViewletConfig>)
     */
    public void remove(java.util.Collection<org.ministone.portal.domain.ViewletConfig> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("ViewletConfig.remove - 'entities' can not be null");
        }
        this.getHibernateTemplate().deleteAll(entities);
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#getViewlets(org.ministone.portal.criteria.ViewletCriteria)
     */
    public java.util.Collection getViewlets(org.ministone.portal.criteria.ViewletCriteria criteria) {
        return this.getViewlets(TRANSFORM_NONE, criteria);
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#getViewlets(int, java.lang.String, org.ministone.portal.criteria.ViewletCriteria)
     */
    public java.util.Collection getViewlets(final int transform, final org.ministone.portal.criteria.ViewletCriteria criteria) {
        try {
            org.ministone.CriteriaSearch criteriaSearch = new org.ministone.CriteriaSearch(super.getSession(false), org.ministone.portal.domain.ViewletConfigImpl.class);
            criteriaSearch.getConfiguration().setFirstResult(criteria.getFirstResult());
            criteriaSearch.getConfiguration().setFetchSize(criteria.getFetchSize());
            criteriaSearch.getConfiguration().setMaximumResultSize(criteria.getMaximumResultSize());
            org.ministone.CriteriaSearchParameter parameter1 = new org.ministone.CriteriaSearchParameter(criteria.getPageId(), "containerConfig.pageConfig.id");
            criteriaSearch.addParameter(parameter1);
            java.util.List results = criteriaSearch.executeAsList();
            transformEntities(transform, results);
            return results;
        } catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>org.ministone.portal.domain.ViewletConfigDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     * <p/>
     * This method will return instances of these types:
     * <ul>
     *   <li>{@link org.ministone.portal.domain.ViewletConfig} - {@link #TRANSFORM_NONE}</li>
     *   <li>{@link org.ministone.portal.vo.Viewlet} - {@link TRANSFORM_VIEWLET}</li>
     * </ul>
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link org.ministone.portal.domain.ViewletConfigDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,java.util.Collection)
     */
    protected java.lang.Object transformEntity(final int transform, final org.ministone.portal.domain.ViewletConfig entity) {
        java.lang.Object target = null;
        if (entity != null) {
            switch(transform) {
                case TRANSFORM_VIEWLET:
                    target = toViewlet(entity);
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
     * {@link #transformEntity(int,org.ministone.portal.domain.ViewletConfig)}
     * method. This method does not instantiate a new collection.
     * <p/>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>org.ministone.portal.domain.ViewletConfigDao</code>
     * @param entities the collection of entities to transform
     * @see #transformEntity(int,org.ministone.portal.domain.ViewletConfig)
     */
    protected void transformEntities(final int transform, final java.util.Collection entities) {
        switch(transform) {
            case TRANSFORM_VIEWLET:
                toViewletCollection(entities);
                break;
            case TRANSFORM_NONE:
            default:
        }
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#toViewletCollection(java.util.Collection)
     */
    public final void toViewletCollection(java.util.Collection entities) {
        if (entities != null) {
            org.apache.commons.collections.CollectionUtils.transform(entities, VIEWLET_TRANSFORMER);
        }
    }

    /**
     * Default implementation for transforming the results of a report query into a value object. This
     * implementation exists for convenience reasons only. It needs only be overridden in the
     * {@link ViewletConfigDaoImpl} class if you intend to use reporting queries.
     * @see org.ministone.portal.domain.ViewletConfigDao#toViewlet(org.ministone.portal.domain.ViewletConfig)
     */
    protected org.ministone.portal.vo.Viewlet toViewlet(java.lang.Object[] row) {
        org.ministone.portal.vo.Viewlet target = null;
        if (row != null) {
            final int numberOfObjects = row.length;
            for (int ctr = 0; ctr < numberOfObjects; ctr++) {
                final java.lang.Object object = row[ctr];
                if (object instanceof org.ministone.portal.domain.ViewletConfig) {
                    target = this.toViewlet((org.ministone.portal.domain.ViewletConfig) object);
                    break;
                }
            }
        }
        return target;
    }

    /**
     * This anonymous transformer is designed to transform entities or report query results
     * (which result in an array of objects) to {@link org.ministone.portal.vo.Viewlet}
     * using the Jakarta Commons-Collections Transformation API.
     */
    private org.apache.commons.collections.Transformer VIEWLET_TRANSFORMER = new org.apache.commons.collections.Transformer() {

        public java.lang.Object transform(java.lang.Object input) {
            java.lang.Object result = null;
            if (input instanceof org.ministone.portal.domain.ViewletConfig) {
                result = toViewlet((org.ministone.portal.domain.ViewletConfig) input);
            } else if (input instanceof java.lang.Object[]) {
                result = toViewlet((java.lang.Object[]) input);
            }
            return result;
        }
    };

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#viewletToEntityCollection(java.util.Collection)
     */
    public final void viewletToEntityCollection(java.util.Collection instances) {
        if (instances != null) {
            for (final java.util.Iterator iterator = instances.iterator(); iterator.hasNext(); ) {
                if (!(iterator.next() instanceof org.ministone.portal.vo.Viewlet)) {
                    iterator.remove();
                }
            }
            org.apache.commons.collections.CollectionUtils.transform(instances, ViewletToEntityTransformer);
        }
    }

    private final org.apache.commons.collections.Transformer ViewletToEntityTransformer = new org.apache.commons.collections.Transformer() {

        public java.lang.Object transform(java.lang.Object input) {
            return viewletToEntity((org.ministone.portal.vo.Viewlet) input);
        }
    };

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#toViewlet(org.ministone.portal.domain.ViewletConfig, org.ministone.portal.vo.Viewlet)
     */
    public void toViewlet(org.ministone.portal.domain.ViewletConfig source, org.ministone.portal.vo.Viewlet target) {
        target.setTemplateFile(source.getTemplateFile());
        target.setOrderNum(source.getOrderNum());
        target.setViewletName(source.getViewletName());
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setDecorator(source.getDecorator());
        target.setRenderer(source.getRenderer());
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#toViewlet(org.ministone.portal.domain.ViewletConfig)
     */
    public org.ministone.portal.vo.Viewlet toViewlet(final org.ministone.portal.domain.ViewletConfig entity) {
        final org.ministone.portal.vo.Viewlet target = new org.ministone.portal.vo.Viewlet();
        this.toViewlet(entity, target);
        return target;
    }

    /**
     * @see org.ministone.portal.domain.ViewletConfigDao#viewletToEntity(org.ministone.portal.vo.Viewlet, org.ministone.portal.domain.ViewletConfig)
     */
    public void viewletToEntity(org.ministone.portal.vo.Viewlet source, org.ministone.portal.domain.ViewletConfig target, boolean copyIfNull) {
        if (copyIfNull || source.getTemplateFile() != null) {
            target.setTemplateFile(source.getTemplateFile());
        }
        if (copyIfNull || source.getOrderNum() != 0) {
            target.setOrderNum(source.getOrderNum());
        }
        if (copyIfNull || source.getViewletName() != null) {
            target.setViewletName(source.getViewletName());
        }
        if (copyIfNull || source.getTitle() != null) {
            target.setTitle(source.getTitle());
        }
        if (copyIfNull || source.getDecorator() != null) {
            target.setDecorator(source.getDecorator());
        }
        if (copyIfNull || source.getRenderer() != null) {
            target.setRenderer(source.getRenderer());
        }
    }
}
