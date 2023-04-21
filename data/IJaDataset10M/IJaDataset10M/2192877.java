package net.sf.selfim.core;

import java.lang.reflect.InvocationTargetException;
import net.sf.selfim.core.info.*;
import net.sf.selfim.core.info.User;
import junit.framework.TestCase;

/**
 * @author: Costin Emilian GRIGORE
 */
public class InformationFactoryTest extends TestCase {

    /**
     * Constructor for InformationFactoryTest.
     * @param arg0
     */
    public InformationFactoryTest(String arg0) {
        super(arg0);
    }

    public void testIsLikePattern1() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        assertTrue(InformationFactory.isLikePattern(new User(), new User("raiser", new FileKBLocation())));
    }

    public void testIsLikePattern2() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        assertTrue(InformationFactory.isLikePattern(new User("raiser", null), new User("raiser", null)));
        assertTrue(InformationFactory.isLikePattern(new User("raiser", null), new User("raiser", new FileKBLocation())));
        assertTrue(InformationFactory.isLikePattern(new User("raiser", null), new User("raiser", new FileKBLocation())));
    }

    public void testIsLikePattern3() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        assertFalse(InformationFactory.isLikePattern(new User("raiser", new FileKBLocation()), new User("raiser", null)));
    }
}
