package net.comensus.gh.core.dao.impl;

import net.comensus.gh.core.dao.ext.NullValueMapper;
import net.comensus.gh.core.dao.GenericDAO;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Id;
import net.comensus.gh.core.entity.AbstractEntity;
import net.comensus.gh.core.exception.CoreException;
import net.comensus.tools.reflection.ReflectionTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author faraya
 */
public abstract class AbstractJpaDAO<T extends AbstractEntity, PK extends Serializable> extends JpaDaoSupport implements GenericDAO<T, PK> {

    protected final Log log = LogFactory.getLog(getClass());

    protected Class<T> persistentClass;

    private NullValueMapper valueMapper;

    /**
     *
     * @param persistentClass
     */
    public AbstractJpaDAO(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * 
     * @return
     */
    public NullValueMapper getValueMapper() {
        return valueMapper;
    }

    /**
     *
     * @param valueMapper
     */
    public void setValueMapper(NullValueMapper valueMapper) {
        this.valueMapper = valueMapper;
    }

    /**
     * {@inheritDoc}     
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return getJpaTemplate().find("select obj from " + this.persistentClass.getName() + " obj");
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> findAllDistinct() {
        Collection result = new LinkedHashSet(findAll());
        return new ArrayList(result);
    }

    /**
     * {@inheritDoc}
     */
    public T findById(PK id) {
        T entity = getJpaTemplate().find(this.persistentClass, id);
        if (entity == null) {
            String msg = "Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...";
            log.warn(msg);
        }
        return entity;
    }

    /**
     * More reliable way to determine if an entity object is transient
     * it actually grabs the field annotaed as Id, extracts its value
     * if such value is null, definitely that's a transient object
     * otherwise if it comes with a value we try to do a find using JPA
     * I admit it, its a bit of overhead, latter we might add a cahce
     *
     * @param object
     * @return
     */
    private boolean isTransient(T object) {
        Set<Field> fields = ReflectionTools.getAnnotatedFields(object.getClass(), Id.class);
        if (CollectionUtils.isEmpty(fields)) {
            String msg = "No field was annotated as PK on class of type " + object.getClass();
            log.error(msg);
            throw new CoreException(msg);
        }
        Field idField = fields.iterator().next();
        boolean modifier = idField.isAccessible();
        try {
            idField.setAccessible(true);
            PK pk = (PK) idField.get(object);
            return (pk == null ? true : (getJpaTemplate().find(object.getClass(), pk) == null));
        } catch (IllegalAccessException e) {
            throw new CoreException(e);
        } finally {
            idField.setAccessible(modifier);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public T save(T object) {
        if (getValueMapper() != null) {
            getValueMapper().prepareNulls(object);
        }
        if (isTransient(object)) {
            getJpaTemplate().persist(object);
            return object;
        }
        return getJpaTemplate().merge(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        getJpaTemplate().remove(findById(id));
    }
}
