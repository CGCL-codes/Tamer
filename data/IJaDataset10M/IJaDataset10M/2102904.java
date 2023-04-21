package org.torweg.pulse.accesscontrol.attributes;

import java.util.Set;
import org.torweg.pulse.configuration.ConfigBean;
import org.torweg.pulse.configuration.DeprecatedConfigurable;
import org.torweg.pulse.configuration.ConfigurationException;
import org.torweg.pulse.service.PulseException;

/**
 * the factory for the attributes.
 * 
 * @author Daniel Dietz, Thomas Weber
 * @version $Revision: 1915 $
 */
public class AttributeFactory implements DeprecatedConfigurable {

    /**
	 * the configuration.
	 */
    private AttributeFactoryConfig config;

    /**
	 * returns the classes of the known {@code AbstractAttribute}s.
	 * 
	 * @return the classes of the known {@code AbstractAttribute}s
	 * 
	 * @see org.torweg.pulse.accesscontrol.attributes.AttributeFactoryConfig#getAttributes()
	 */
    public final Set<Class<AbstractAttribute<?>>> getAttributes() {
        return this.config.getAttributes();
    }

    /**
	 * returns the classes of the available {@code AbstractValue}s for the
	 * passed {@code AbstractAttribute}-class.
	 * 
	 * @param clazz
	 *            the attribute-{@code Class} to retrieve the values for
	 * 
	 * @return the classes of the available {@code AbstractValue}s for the
	 *         passed {@code AbstractAttribute}-class
	 * 
	 * @see org.torweg.pulse.accesscontrol.attributes.AttributeFactoryConfig#getAttributeValues(java.lang.Class)
	 */
    public final Set<Class<AbstractValue<?>>> getAttributeValues(final Class<AbstractAttribute<?>> clazz) {
        return this.config.getAttributeValues(clazz);
    }

    /**
	 * returns a set with the available typed checks for the given attribute
	 * class.
	 * 
	 * @param clazz
	 *            the attribute class
	 * @return a set with the available checks
	 */
    public final Set<Class<AbstractTypedCheck<?>>> getTypedChecks(final Class<AbstractAttribute<?>> clazz) {
        return this.config.getTypedChecks(clazz);
    }

    /**
	 * returns an instance of the given check.
	 * 
	 * @param c
	 *            the check class
	 * @return an instance of the check
	 */
    public final AbstractTypedCheck<?> getTypedCheck(final Class<AbstractTypedCheck<?>> c) {
        try {
            return c.newInstance();
        } catch (Exception e) {
            throw new ConfigurationException("Cannot instantiate typed check " + c.getCanonicalName() + ": " + e.getLocalizedMessage(), e);
        }
    }

    /**
	 * returns an {@code AbstractAttribute} of the passed class.
	 * 
	 * @param clazz
	 *            the class to build the attribute of
	 * 
	 * @return an {@code AbstractAttribute} of the passed class
	 */
    public final AbstractAttribute<?> getAttribute(final Class<AbstractAttribute<?>> clazz) {
        return getAttribute(clazz, null, false);
    }

    /**
	 * returns an {@code AbstractAttribute} of the passed class.
	 * 
	 * @param clazz
	 *            the class to build the attribute of
	 * @param name
	 *            the name to be set for the attribute
	 * @return an {@code AbstractAttribute} of the passed class
	 */
    public final AbstractAttribute<?> getAttribute(final Class<AbstractAttribute<?>> clazz, final String name) {
        return getAttribute(clazz, name, false);
    }

    /**
	 * returns an {@code AbstractAttribute} of the passed class.
	 * 
	 * @param clazz
	 *            the class to build the attribute of
	 * @param isSystem
	 *            specifies a system-attribute
	 * 
	 * @return an {@code AbstractAttribute} of the passed class
	 */
    public final AbstractAttribute<?> getAttribute(final Class<AbstractAttribute<?>> clazz, final boolean isSystem) {
        return getAttribute(clazz, null, isSystem);
    }

    /**
	 * returns an {@code AbstractAttribute} of the passed class.
	 * 
	 * @param clazz
	 *            the class to build the attribute of
	 * @param name
	 *            the name to be set for the attribute
	 * @param isSystem
	 *            specifies a system-attribute
	 * 
	 * @return an {@code AbstractAttribute} of the passed class
	 */
    public final AbstractAttribute<?> getAttribute(final Class<AbstractAttribute<?>> clazz, final String name, final boolean isSystem) {
        try {
            return clazz.newInstance().getAttributeInstance(name, isSystem);
        } catch (InstantiationException e) {
            throw new PulseException(clazz.getCanonicalName() + ": " + e.getLocalizedMessage(), e);
        } catch (IllegalAccessException e) {
            throw new PulseException(clazz.getCanonicalName() + ": " + e.getLocalizedMessage(), e);
        }
    }

    /**
	 * initializes the {@code AttributeFactory} with the given
	 * {@code ConfigBean}.
	 * 
	 * @param c
	 *            the {@code ConfigBean}
	 * 
	 * @see org.torweg.pulse.configuration.DeprecatedConfigurable#init(org.torweg.pulse.configuration.ConfigBean)
	 */
    public void init(final ConfigBean c) {
        this.config = (AttributeFactoryConfig) c;
    }
}
