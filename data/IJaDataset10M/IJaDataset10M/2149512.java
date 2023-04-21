package fi.arcusys.commons.hibernate.dao;

import java.io.Serializable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * A congrete generic-purpose DAO implementation for use with entities of 
 * <em>any</em> class.
 * 
 * @author mikko
 * @version 1.0 $Rev: 70 $
 */
public class DAO<EntityClass extends Object> extends DAOBase<EntityClass, Serializable> {

    private static final long serialVersionUID = 1L;

    private Class<EntityClass> entityClass;

    public DAO(Class<EntityClass> entityClass) {
        super();
        this.entityClass = entityClass;
    }

    public DAO(Class<EntityClass> entityClass, Session specificSession) {
        super(specificSession);
        this.entityClass = entityClass;
    }

    public DAO(Class<EntityClass> entityClass, SessionFactory sf) {
        super(sf);
        this.entityClass = entityClass;
    }

    @Override
    protected Class<EntityClass> resolveEntityClass() {
        return entityClass;
    }
}
