package br.com.arsmachina.tapestrycrud.module;

import org.apache.tapestry5.PrimaryKeyEncoder;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;

/**
 * Interface that defines information about a module, whatever its conventions are.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface TapestryCrudModule {

    /**
	 * Returns the activation context encoder class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return an {@link ActivationContextEncoder} or null (if no corresponding one is found).
	 */
    public <T> Class<? extends ActivationContextEncoder<T>> getActivationContextEncoderClass(Class<T> entityClass);

    /**
	 * Returns the encoder class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return an {@link Encoder} or null (if no corresponding one is found).
	 */
    public <T> Class<? extends Encoder<T, ?>> getEncoderClass(Class<T> entityClass);

    /**
	 * Returns the label encoder class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link LabelEncoder} or null (if no corresponding one is found).
	 */
    public <T> Class<? extends LabelEncoder<T>> getLabelEncoderClass(Class<T> entityClass);

    /**
	 * Returns the label encoder class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link PrimaryKeyEncoder} or null (if no corresponding one is found).
	 */
    public <T> Class<? extends PrimaryKeyEncoder<?, T>> getPrimaryKeyEncoderClass(Class<T> entityClass);

    /**
	 * Returns the module name. Just used for logging and debugging purposes.
	 * 
	 * @return a {@link String}.
	 */
    public String getName();
}
