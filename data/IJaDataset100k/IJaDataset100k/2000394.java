package br.com.arsmachina.tapestrycrud.services.impl;

import java.io.Serializable;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.PrimaryKeyEncoder;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;

/**
 * An {@link ActivationContextEncoder} implementation based in a {@link PrimaryKeyEncoder}.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the entity class related to this DAO.
 * @param <K> the type of the field that represents the entity class' primary key.
 */
class PrimaryKeyEncoderActivationContextEncoder<T, K extends Serializable> implements ActivationContextEncoder<T> {

    private final Class<K> primaryKeyType;

    private final PrimaryKeyEncoder<K, T> primaryKeyEncoder;

    /**
	 * @param primaryKeyType
	 */
    @SuppressWarnings("unchecked")
    public PrimaryKeyEncoderActivationContextEncoder(PrimaryKeyEncoder<K, T> primaryKeyEncoder, Class<K> primaryKeyType) {
        if (primaryKeyType == null) {
            throw new IllegalArgumentException("Parameter primaryKeyType cannot be null");
        }
        if (primaryKeyEncoder == null) {
            throw new IllegalArgumentException("Parameter primaryKeyEncoder cannot be null");
        }
        this.primaryKeyEncoder = primaryKeyEncoder;
        this.primaryKeyType = primaryKeyType;
    }

    /**
	 * @see br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder#toActivationContext(java.lang.Object)
	 */
    public Object toActivationContext(T object) {
        return primaryKeyEncoder.toKey(object);
    }

    /**
	 * @see br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder#toObject(org.apache.tapestry5.EventContext)
	 */
    public T toObject(EventContext value) {
        return primaryKeyEncoder.toValue(value.get(primaryKeyType, 0));
    }
}
