package br.com.arsmachina.tapestrycrud.services.impl;

import java.io.Serializable;
import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.ValueEncoderFactory;

/**
 * A {@link ValueEncoder} implementation based in a {@link PrimaryKeyEncoder}.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the entity class related to this DAO.
 * @param <K> the type of the field that represents the entity class' primary key.
 */
public class PrimaryKeyEncoderValueEncoder<T, K extends Serializable> implements ValueEncoderFactory<T>, ValueEncoder<T> {

    private final Class<K> primaryKeyType;

    private final PrimaryKeyEncoder<K, T> primaryKeyEncoder;

    private final TypeCoercer typeCoercer;

    /**
	 * @param primaryKeyType
	 */
    @SuppressWarnings("unchecked")
    public PrimaryKeyEncoderValueEncoder(Class<K> primaryKeyType, PrimaryKeyEncoder<K, T> primaryKeyEncoder, TypeCoercer typeCoercer) {
        if (primaryKeyType == null) {
            throw new IllegalArgumentException("Parameter primaryKeyType cannot be null");
        }
        if (primaryKeyEncoder == null) {
            throw new IllegalArgumentException("Parameter primaryKeyEncoder cannot be null");
        }
        if (typeCoercer == null) {
            throw new IllegalArgumentException("Parameter typeCoercer cannot be null");
        }
        this.primaryKeyEncoder = primaryKeyEncoder;
        this.primaryKeyType = primaryKeyType;
        this.typeCoercer = typeCoercer;
    }

    public ValueEncoder<T> create(Class<T> type) {
        return this;
    }

    public String toClient(T value) {
        final K key = primaryKeyEncoder.toKey(value);
        final String clientValue = typeCoercer.coerce(key, String.class);
        return clientValue;
    }

    public T toValue(String clientValue) {
        K key = typeCoercer.coerce(clientValue, primaryKeyType);
        T value = primaryKeyEncoder.toValue(key);
        return value;
    }
}
