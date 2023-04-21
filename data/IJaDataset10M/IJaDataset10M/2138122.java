package org.jgentleframework.reflection;

import java.lang.reflect.Modifier;

/**
 * The Class SingleClassIdentificationImpl.
 * 
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Jul 22, 2008
 * @see Identification
 */
class SingleClassIdentificationImpl implements SingleClassIdentification {

    /** The name. */
    String name = null;

    /** The modifier. */
    int modifiers = Modifier.PUBLIC;

    /**
	 * Constructor.
	 */
    public SingleClassIdentificationImpl() {
    }

    /**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of class.
	 * @param modifiers
	 *            the modifiers
	 */
    public SingleClassIdentificationImpl(String name, int modifiers) {
        this.name = name;
        this.modifiers = modifiers;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Class<?>[] getMember() {
        if (this.name == null || this.name.isEmpty()) {
            throw new ReflectException("The identification data is not enough or invalid !");
        }
        Class<?>[] result = new Class<?>[1];
        try {
            Class<?> clazz = Class.forName(this.name);
            if (clazz.getModifiers() == this.modifiers) result[0] = clazz;
        } catch (ClassNotFoundException e) {
            ReflectException ex = new ReflectException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
        return result;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public String getName() {
        return this.name;
    }
}
