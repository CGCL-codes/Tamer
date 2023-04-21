package com.newisys.randsolver;

import junit.framework.TestCase;
import com.newisys.random.PRNG;
import com.newisys.random.PRNGFactoryFactory;
import com.newisys.randsolver.annotation.Constraint;
import com.newisys.randsolver.annotation.Rand;
import com.newisys.randsolver.annotation.Randomizable;

/**
 * @author jon.nall
 *
 * TestEnumeration
 */
public class TestEnumerationMapper extends TestCase {

    public void testEnumeration() {
        EnumTestClass test = new EnumTestClass();
        PRNG prng = PRNGFactoryFactory.getDefaultFactory().newInstance();
        for (int i = 0; i < 100; ++i) {
            Solver.randomize(test, prng);
            assertTrue(test.mEnum == InternalEnum.ZERO || test.mEnum == InternalEnum.ONE || test.mEnum == InternalEnum.TWO || test.mEnum == InternalEnum.TEN);
            assertEquals(Integer.MAX_VALUE, test.bar);
        }
    }
}

class InternalEnum implements RandomHooks {

    private String mString;

    static {
        RandomMapperRegistry.registerMapper(InternalEnum.class, new TestEnumerationRandomMapper());
    }

    private InternalEnum(String s) {
        mString = s;
    }

    @Override
    public String toString() {
        return mString;
    }

    public void preRandomize() {
    }

    public void postRandomize() {
    }

    public static final InternalEnum ZERO = new InternalEnum("ZERO");

    public static final InternalEnum ONE = new InternalEnum("ONE");

    public static final InternalEnum TWO = new InternalEnum("TWO");

    public static final InternalEnum TEN = new InternalEnum("TEN");
}

@Randomizable(@Constraint(expr = "bar == java.lang.Integer.MAX_VALUE;"))
class EnumTestClass {

    class SubClass {

        public int foo;
    }

    @Rand
    InternalEnum mEnum;

    @Rand
    int bar;

    SubClass boom;
}
