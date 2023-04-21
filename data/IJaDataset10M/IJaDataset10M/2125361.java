package net.derquinse.common.meta;

import javax.annotation.Nullable;
import org.joda.time.ReadableInstant;
import com.google.common.base.Predicate;

/**
 * Class for string-valued property descriptor.
 * @author Andres Rodriguez
 * @param <C> Containing type.
 */
public abstract class ReadableInstantMetaProperty<C> extends ComparableMetaProperty<C, ReadableInstant> {

    /**
	 * Default constructor.
	 * @param name Property name.
	 * @param required True if the property is required.
	 * @param validity Validity predicate.
	 * @param defaultValue Default value for the property.
	 */
    protected ReadableInstantMetaProperty(String name, boolean required, @Nullable Predicate<? super ReadableInstant> validity, @Nullable ReadableInstant defaultValue) {
        super(name, required, validity, defaultValue);
    }

    /**
	 * Constructor.
	 * @param name Property name.
	 * @param required True if the property is required.
	 * @param validity Validity predicate.
	 */
    protected ReadableInstantMetaProperty(String name, boolean required, @Nullable Predicate<? super ReadableInstant> validity) {
        super(name, required, validity);
    }

    /**
	 * Constructor.
	 * @param name Property name.
	 * @param required True if the property is required.
	 */
    protected ReadableInstantMetaProperty(String name, boolean required) {
        super(name, required);
    }
}
