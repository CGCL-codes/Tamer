package org.omwg.mediation.junit.comparators;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.omwg.mediation.ObjectTest;
import org.omwg.mediation.language.objectmodel.api.datatypes.StringXs;
import org.omwg.mediation.language.objectmodel.impl.comparators.EmptyImpl;
import org.omwg.mediation.language.objectmodel.impl.datatypes.ListXsImpl;
import org.omwg.mediation.language.objectmodel.impl.datatypes.StringXsImpl;

public class EmptyTest extends TestCase {

    private static final ListXsImpl<StringXs> LIST = new ListXsImpl<StringXs>(3);

    private static final ListXsImpl<StringXs> LISTMORE = new ListXsImpl<StringXs>(3);

    static {
        LIST.add(new StringXsImpl("a"));
        LIST.add(new StringXsImpl("b"));
        LIST.add(new StringXsImpl("c"));
        LISTMORE.add(new StringXsImpl("d"));
        LISTMORE.add(new StringXsImpl("e"));
        LISTMORE.add(new StringXsImpl("f"));
    }

    public static Test suite() {
        return new TestSuite(EmptyTest.class, EmptyTest.class.getSimpleName());
    }

    public void testBasic() {
        final EmptyImpl REF = new EmptyImpl(LIST);
        final EmptyImpl MUTABLE = new EmptyImpl();
        MUTABLE.setArgument(LIST);
        assertEquals("Something wrong with the setMethods", REF, MUTABLE);
        assertEquals("Something wrong with getArgument", LIST, MUTABLE.getArgument());
    }

    public void testEquals() {
        ObjectTest.runTestEquals(new EmptyImpl(LIST), new EmptyImpl(LIST), new EmptyImpl(null));
        ObjectTest.runTestEquals(new EmptyImpl(LIST), new EmptyImpl(LIST), new EmptyImpl(LISTMORE));
    }

    public void testClone() {
        ObjectTest.runTestClone(new EmptyImpl(LIST));
    }

    public void testHashCode() {
        ObjectTest.runTestHashCode(new EmptyImpl(LIST), new EmptyImpl(LIST));
    }
}
