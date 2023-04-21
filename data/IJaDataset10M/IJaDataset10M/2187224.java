package org.openuss.messaging;

/**
 * <p>
 * Base Spring DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>org.openuss.messaging.TemplateMessage</code>.
 * </p>
 *
 * @see org.openuss.messaging.TemplateMessage
 */
public abstract class TemplateMessageDaoBase extends org.springframework.orm.hibernate3.support.HibernateDaoSupport implements org.openuss.messaging.TemplateMessageDao {

    /**
     * @see org.openuss.messaging.TemplateMessageDao#load(int, java.lang.Long)
     */
    public java.lang.Object load(final int transform, final java.lang.Long id) {
        if (id == null) {
            throw new IllegalArgumentException("TemplateMessage.load - 'id' can not be null");
        }
        final java.lang.Object entity = this.getHibernateTemplate().get(org.openuss.messaging.TemplateMessageImpl.class, id);
        return transformEntity(transform, (org.openuss.messaging.TemplateMessage) entity);
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#load(java.lang.Long)
     */
    public org.openuss.messaging.TemplateMessage load(java.lang.Long id) {
        return (org.openuss.messaging.TemplateMessage) this.load(TRANSFORM_NONE, id);
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#loadAll()
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.openuss.messaging.TemplateMessage> loadAll() {
        return this.loadAll(TRANSFORM_NONE);
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#loadAll(int)
     */
    public java.util.Collection loadAll(final int transform) {
        final java.util.Collection results = this.getHibernateTemplate().loadAll(org.openuss.messaging.TemplateMessageImpl.class);
        this.transformEntities(transform, results);
        return results;
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#create(org.openuss.messaging.TemplateMessage)
     */
    public org.openuss.messaging.TemplateMessage create(org.openuss.messaging.TemplateMessage templateMessage) {
        return (org.openuss.messaging.TemplateMessage) this.create(TRANSFORM_NONE, templateMessage);
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#create(int transform, org.openuss.messaging.TemplateMessage)
     */
    public java.lang.Object create(final int transform, final org.openuss.messaging.TemplateMessage templateMessage) {
        if (templateMessage == null) {
            throw new IllegalArgumentException("TemplateMessage.create - 'templateMessage' can not be null");
        }
        this.getHibernateTemplate().save(templateMessage);
        return this.transformEntity(transform, templateMessage);
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#create(java.util.Collection<org.openuss.messaging.TemplateMessage>)
     */
    @SuppressWarnings({ "unchecked" })
    public java.util.Collection<org.openuss.messaging.TemplateMessage> create(final java.util.Collection<org.openuss.messaging.TemplateMessage> entities) {
        return create(TRANSFORM_NONE, entities);
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#create(int, java.util.Collection<org.openuss.messaging.TemplateMessage>)
     */
    public java.util.Collection create(final int transform, final java.util.Collection<org.openuss.messaging.TemplateMessage> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("TemplateMessage.create - 'entities' can not be null");
        }
        this.getHibernateTemplate().execute(new org.springframework.orm.hibernate3.HibernateCallback() {

            public java.lang.Object doInHibernate(org.hibernate.Session session) throws org.hibernate.HibernateException {
                for (java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                    create(transform, (org.openuss.messaging.TemplateMessage) entityIterator.next());
                }
                return null;
            }
        }, true);
        return entities;
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#update(org.openuss.messaging.TemplateMessage)
     */
    public void update(org.openuss.messaging.TemplateMessage templateMessage) {
        if (templateMessage == null) {
            throw new IllegalArgumentException("TemplateMessage.update - 'templateMessage' can not be null");
        }
        try {
            this.getHibernateTemplate().update(templateMessage);
        } catch (org.springframework.dao.DataAccessException ex) {
            if (ex.getCause() instanceof org.hibernate.NonUniqueObjectException) {
                logger.debug("Catched NonUniqueObjectException " + ex.getCause().getMessage());
                getSession().merge(templateMessage);
            } else {
                throw ex;
            }
        }
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#update(java.util.Collection<org.openuss.messaging.TemplateMessage>)
     */
    public void update(final java.util.Collection<org.openuss.messaging.TemplateMessage> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("TemplateMessage.update - 'entities' can not be null");
        }
        this.getHibernateTemplate().execute(new org.springframework.orm.hibernate3.HibernateCallback() {

            public java.lang.Object doInHibernate(org.hibernate.Session session) throws org.hibernate.HibernateException {
                for (java.util.Iterator entityIterator = entities.iterator(); entityIterator.hasNext(); ) {
                    update((org.openuss.messaging.TemplateMessage) entityIterator.next());
                }
                return null;
            }
        }, true);
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#remove(org.openuss.messaging.TemplateMessage)
     */
    public void remove(org.openuss.messaging.TemplateMessage templateMessage) {
        if (templateMessage == null) {
            throw new IllegalArgumentException("TemplateMessage.remove - 'templateMessage' can not be null");
        }
        this.getHibernateTemplate().delete(templateMessage);
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#remove(java.lang.Long)
     */
    public void remove(java.lang.Long id) {
        if (id == null) {
            throw new IllegalArgumentException("TemplateMessage.remove - 'id can not be null");
        }
        org.openuss.messaging.TemplateMessage entity = this.load(id);
        if (entity != null) {
            this.remove(entity);
        }
    }

    /**
     * @see org.openuss.messaging.TemplateMessageDao#remove(java.util.Collection<org.openuss.messaging.TemplateMessage>)
     */
    public void remove(java.util.Collection<org.openuss.messaging.TemplateMessage> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("TemplateMessage.remove - 'entities' can not be null");
        }
        this.getHibernateTemplate().deleteAll(entities);
    }

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>org.openuss.messaging.TemplateMessageDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link org.openuss.messaging.TemplateMessageDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,java.util.Collection)
     */
    protected java.lang.Object transformEntity(final int transform, final org.openuss.messaging.TemplateMessage entity) {
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
     * {@link #transformEntity(int,org.openuss.messaging.TemplateMessage)}
     * method. This method does not instantiate a new collection.
     * <p/>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>org.openuss.messaging.TemplateMessageDao</code>
     * @param entities the collection of entities to transform
     * @see #transformEntity(int,org.openuss.messaging.TemplateMessage)
     */
    protected void transformEntities(final int transform, final java.util.Collection entities) {
        switch(transform) {
            case TRANSFORM_NONE:
            default:
        }
    }
}
