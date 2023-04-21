package net.sf.dz2.util;

import java.io.File;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.List;
import net.sf.dz.util.ObjectFactory;
import net.sf.dz2.meta.model.AbstractMeta;
import junit.framework.TestCase;

/**
 * Test case for {@link ObjectFactory}.
 * 
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim
 * Tkachenko</a> 2001
 * @version $Id$
 */
public class ObjectFactoryTest extends TestCase {

    /**
     * Make sure "java.lang.String" can be instantiated as {@code Object}
     * concrete class.
     * 
     * @throws Throwable if it doesn't.
     */
    public void testExtendsConcreteClass() throws Throwable {
        ObjectFactory.instantiate("java.lang.String", Object.class);
    }

    /**
     * Make sure "java.util.LinkedList" can be instantiated as
     * {@code AbstractCollection} concrete class.
     * 
     * @throws Throwable if it doesn't.
     */
    public void testExtendsAbstractClass() throws Throwable {
        ObjectFactory.instantiate("java.util.LinkedList", AbstractCollection.class);
    }

    /**
     * Make sure abstract class "java.util.AbstractList" can not be instantiated
     * as {@code AbstractCollection}.
     * 
     * @throws Throwable if it doesn't.
     */
    public void testAbstractClass() throws Throwable {
        try {
            ObjectFactory.instantiate("net.sf.dz2.meta.model.AbstractMeta", AbstractMeta.class);
            throw new IllegalStateException("Oops");
        } catch (InstantiationException ex) {
        }
    }

    /**
     * Make sure "java.lang.String" can not be instantiated as {@code File}
     * concrete class.
     * 
     * @throws Throwable if it does.
     */
    public void testNotClass() throws Throwable {
        try {
            ObjectFactory.instantiate("java.lang.String", File.class);
            throw new IllegalStateException("Oops");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Make sure "java.util.LinkedList" can be instantiated as
     * {@code Collection} interface.
     * 
     * @throws Throwable if it doesn't.
     */
    public void testInterface() throws Throwable {
        ObjectFactory.instantiate("java.util.LinkedList", List.class);
    }

    /**
     * Make sure "java.lang.String" can not be instantiated as
     * {@code Collection} interface.
     * 
     * @throws Throwable if it does.
     */
    public void testNotInterface() throws Throwable {
        try {
            ObjectFactory.instantiate("java.lang.String", Collection.class);
            throw new IllegalStateException("Oops");
        } catch (IllegalArgumentException ex) {
        }
    }
}
