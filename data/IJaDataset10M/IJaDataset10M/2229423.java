package com.googlecode.brui.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * A registry of all available {@link Validator}s. By default the registry
 * contains {@link Validator}s for the most common simple types. Custom
 * {@link Validator}s can be registered by either calling
 * {@link #register(Class, Validator)} or by modifying the registry
 * configuration file.
 */
public final class ValidatorRegistry {

    private static final Logger LOGGER = Logger.getLogger(ValidatorRegistry.class.getName());

    /**
	 * Name of the properties bundle which contains the custom validator
	 * configuration.
	 */
    private static final String REGISTRY_BUNDLE = "brui-validators";

    private static ValidatorRegistry instance;

    /** All known validators, mapped by the type they validate. */
    private final Map<Class<?>, Validator> registry = new HashMap<Class<?>, Validator>();

    /**
	 * Create a registry with all known and custom types pre-registered.
	 */
    ValidatorRegistry() {
        registerKnownTypes();
        registerCustomTypes();
    }

    /**
	 * @return the {@link ValidatorRegistry} singleton instance.
	 */
    public static ValidatorRegistry getInstance() {
        if (instance == null) {
            instance = new ValidatorRegistry();
        }
        return instance;
    }

    /**
	 * Get the {@link Validator} that has been registered with the given type.
	 * 
	 * @param type the property type to get the {@link Validator} for.
	 * @return the {@link Validator} associated with the given type or
	 *         <code>null</code> if there is none.
	 */
    public Validator get(final Class<?> type) {
        return registry.get(type);
    }

    /**
	 * Associate a {@link Validator} with a property type. Any {@link Validator}
	 * already registered with the type will be overriden with the new
	 * {@link Validator}.
	 * 
	 * @param simpleType the type to register the {@link Validator} with.
	 * @param validator the {@link Validator} to register.
	 */
    public void register(final Class<?> simpleType, final Validator validator) {
        if (simpleType == null) {
            throw new NullPointerException("Cannot register a null type");
        }
        if (validator == null) {
            throw new NullPointerException("Cannot register a null validator");
        }
        if (validator.getType().equals(simpleType) || simpleType.isPrimitive() && validator.getType().equals(getWrapperType(simpleType))) {
            registry.put(simpleType, validator);
        } else {
            LOGGER.warning(String.format("Validator '%s' cannot be used to validate instances of '%s'", validator.getClass().getName(), simpleType.getName()));
        }
    }

    private static Class<?> getWrapperType(final Class<?> type) {
        if (boolean.class.equals(type)) {
            return Boolean.class;
        } else if (int.class.equals(type)) {
            return Integer.class;
        } else if (long.class.equals(type)) {
            return Long.class;
        } else if (char.class.equals(type)) {
            return Character.class;
        } else if (double.class.equals(type)) {
            return Double.class;
        } else if (float.class.equals(type)) {
            return Float.class;
        } else if (short.class.equals(type)) {
            return Short.class;
        } else if (byte.class.equals(type)) {
            return Byte.class;
        } else if (void.class.equals(type)) {
            return Void.class;
        }
        throw new IllegalArgumentException("Not a primitive type " + type);
    }

    private void register(final Class<?> simpleType, final Class<?> validatorType) {
        if (simpleType == null) {
            throw new NullPointerException("Cannot register a null type");
        }
        if (validatorType == null) {
            throw new NullPointerException("Cannot register a null validator");
        }
        if (!Validator.class.isAssignableFrom(validatorType)) {
            LOGGER.warning(String.format("Type '%s' does not implement %s", validatorType.getName(), Validator.class));
            return;
        }
        Validator validator = null;
        try {
            validator = (Validator) validatorType.newInstance();
        } catch (final InstantiationException e) {
            LOGGER.warning(String.format("Could not instantiate validator '%s'", validatorType.getName()));
        } catch (final IllegalAccessException e) {
            LOGGER.warning(String.format("Could not instantiate validator '%s'", validatorType.getName()));
        }
        if (validator != null) {
            register(simpleType, validator);
        }
    }

    private void register(final String simpleTypeName, final String validatorTypeName) {
        if (simpleTypeName == null) {
            throw new NullPointerException("Cannot register a null type");
        }
        if (validatorTypeName == null) {
            throw new NullPointerException("Cannot register a null validator");
        }
        Class<?> simpleType = null;
        Class<?> validatorType = null;
        try {
            simpleType = Class.forName(simpleTypeName);
        } catch (final ClassNotFoundException e) {
            LOGGER.warning("Could not find a simple type named " + simpleTypeName);
        }
        try {
            validatorType = Class.forName(validatorTypeName);
        } catch (final ClassNotFoundException e) {
            LOGGER.warning("Could not find a validator named " + validatorTypeName);
        }
        if (simpleType != null && validatorType != null) {
            register(simpleType, validatorType);
        }
    }

    private void registerCustomTypes() {
        try {
            final ResourceBundle bundle = ResourceBundle.getBundle(REGISTRY_BUNDLE);
            for (final String key : bundle.keySet()) {
                register(key, bundle.getString(key));
            }
        } catch (final MissingResourceException e) {
            LOGGER.warning("Could not find validator registry configuration: " + REGISTRY_BUNDLE);
        }
    }

    private void registerKnownTypes() {
        register(String.class, new StringValidator());
        final Validator booleanValidator = new BooleanValidator();
        register(Boolean.class, booleanValidator);
        register(boolean.class, booleanValidator);
        final IntegerValidator integerValidator = new IntegerValidator();
        register(Integer.class, integerValidator);
        register(int.class, integerValidator);
        final DoubleValidator doubleValidator = new DoubleValidator();
        register(Double.class, doubleValidator);
        register(double.class, doubleValidator);
        register(Date.class, new DateValidator());
    }
}
