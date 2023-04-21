package org.xmlfield.core.api;

import org.xmlfield.core.internal.XmlFieldFactory;

/**
 * <p>
 * An <code>XmlFieldNodeModifierFactory</code> instance can be used to create {@link XmlFieldParser} objects.
 * </p>
 * 
 * <p>
 * See {@link #newInstance()} for lookup mechanism.
 * </p>
 * 
 * @author Guillaume Mary <guillaume.mary@capgemini.com>
 */
public abstract class XmlFieldNodeModifierFactory extends XmlFieldFactory {

    /**
     * <p>
     * Get a new <code>XmlFieldNodeModifierFactory</code> instance.
     * 
     * @return Instance of an <code>XmlFieldNodeModifierFactory</code>.
     * 
     * @throws RuntimeException
     *             When there is a failure in creating an <code>XmlFieldNodeModifierFactory</code>
     */
    public static final XmlFieldNodeModifierFactory newInstance() {
        return newInstance(XmlFieldNodeModifierFactory.class);
    }

    /**
     * <p>
     * Return a new <code>XmlFieldNodeModifier</code> using the underlying object model determined when the
     * <code>XmlFieldNodeModifierFactory</code> was instantiated.
     * </p>
     * 
     * @return New instance of an <code>XmlFieldNodeModifier</code>.
     */
    public abstract XmlFieldNodeModifier newModifier();
}
