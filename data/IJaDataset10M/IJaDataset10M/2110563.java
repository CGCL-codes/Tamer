package br.com.arsmachina.tapestrycrud.encoder;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.apache.tapestry5.PrimaryKeyEncoder;
import org.hibernate.EntityMode;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import br.com.arsmachina.controller.Controller;

/**
 * Partial {@link PrimaryKeyEncoder} implementation using Hibernate and a {@link Controller}.
 * 
 * @param <T> the entity class related to this controller.
 * @param <K> the type of the field that represents the entity class' primary key.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class HibernatePrimaryKeyEncoder<T, K extends Serializable> implements PrimaryKeyEncoder<K, T> {

    private final ClassMetadata classMetadata;

    private final Controller<T, K> controller;

    /**
	 * Single construtor of this class.
	 * 
	 * @param sessionFactory a {@link SessionFactory}. It cannot be null.
	 * @param controller a {@link Controller}. It cannot be null.
	 */
    @SuppressWarnings("unchecked")
    public HibernatePrimaryKeyEncoder(SessionFactory sessionFactory, Controller<T, K> controller) {
        if (sessionFactory == null) {
            throw new IllegalArgumentException("Parameter sessionFactory cannot be null");
        }
        if (controller == null) {
            throw new IllegalArgumentException("Parameter controller cannot be null");
        }
        this.controller = controller;
        final Type genericSuperclass = getClass().getGenericSuperclass();
        final ParameterizedType parameterizedType = ((ParameterizedType) genericSuperclass);
        Class<T> entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        classMetadata = sessionFactory.getClassMetadata(entityClass);
    }

    /**
	 * @see org.apache.tapestry.PrimaryKeyEncoder#toKey(java.lang.Object)
	 */
    @SuppressWarnings("unchecked")
    public K toKey(T value) {
        K key = null;
        if (value != null) {
            key = (K) classMetadata.getIdentifier(value, EntityMode.POJO);
        }
        return key;
    }

    /**
	 * Does nothing.
	 * 
	 * @see org.apache.tapestry.PrimaryKeyEncoder#prepareForKeys(java.util.List)
	 */
    public void prepareForKeys(List<K> keys) {
    }

    /**
	 * Returns <code>controller.findById(key)</code>. {@inheritDoc}
	 * 
	 * @see org.apache.tapestry.PrimaryKeyEncoder#toValue(java.io.Serializable)
	 */
    public T toValue(K key) {
        return controller.findById(key);
    }

    /**
	 * Returns the value of the <code>controller</code> property.
	 * 
	 * @return a {@link Controller<T,K>}.
	 */
    public final Controller<T, K> getController() {
        return controller;
    }
}
