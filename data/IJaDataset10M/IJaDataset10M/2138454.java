package us.blanshard.stubout.tests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import us.blanshard.stubout.Stubber;
import us.blanshard.stubout.StubbingTestCase;
import us.blanshard.stubout.tests.model.ABadClass;
import us.blanshard.stubout.tests.model.ModelClass;
import us.blanshard.stubout.tests.model.ModelClassReplacement;

public class TestClassReplacement extends TestCase {

    public static TestSuite suite() {
        return new TestSuite(new Class[] { TestBadClass.class, TestModelClass.class, TestExceptions.class, TestLogging.class });
    }

    public static class TestBadClass extends StubbingTestCase {

        public static final String SOME_VALUE = "Some replacement value";

        public void beforeReloading() {
            Stubber.replaceClass("us.blanshard.stubout.tests.model.ABadClass", MyBadClassReplacement.class);
            Stubber.replaceClass(ModelClass.class, ModelClassReplacement.class);
        }

        public static class MyBadClassReplacement {

            private static final MyBadClassReplacement instance = new MyBadClassReplacement();

            private MyBadClassReplacement() {
            }

            public static MyBadClassReplacement getInstance() {
                return instance;
            }

            public String getSomeValue() {
                return SOME_VALUE;
            }
        }

        public void testBadClass() {
            assertEquals(SOME_VALUE, ABadClass.getInstance().getSomeValue());
        }
    }

    public static class TestModelClass extends StubbingTestCase {

        public void beforeReloading() {
            Stubber.replaceClass(ModelClass.class, ModelClassReplacement.class);
        }

        public void testModelClass() {
            assertEquals("ModelClassReplacement", new ModelClass().toString());
        }
    }

    public static class TestExceptions extends StubbingTestCase {

        public void testNullArgs() {
            try {
                Stubber.replaceClass(null, "xyzzy");
                fail();
            } catch (IllegalArgumentException e) {
            }
            try {
                Stubber.replaceClass("xyzzy", (String) null);
                fail();
            } catch (IllegalArgumentException e) {
            }
            try {
                Stubber.exclude(null);
                fail();
            } catch (IllegalArgumentException e) {
            }
        }

        public void testReplacingNestedClass() {
            try {
                Stubber.replaceClass(TestBadClass.class, getClass());
                fail();
            } catch (IllegalArgumentException e) {
            }
        }

        public void beforeReloading() {
            try {
                Stubber.replaceClass(Stubber.class, getClass());
                fail();
            } catch (IllegalArgumentException e) {
            }
            Stubber.excludePackage("xyzzy");
            try {
                Stubber.replaceClass("xyzzy.asdf", "abc.xyz");
                fail();
            } catch (IllegalArgumentException e) {
            }
            Stubber.replaceClass("asdf.xyzzy", "abc.xyz");
            try {
                Stubber.excludePackage("asdf");
                fail();
            } catch (IllegalArgumentException e) {
            }
        }

        public void testReplacingFromWithinTest() {
            try {
                Stubber.replaceClass(ModelClass.class, ModelClassReplacement.class);
                fail();
            } catch (IllegalStateException e) {
            }
        }

        public void testExcludingFromWithinTest() {
            try {
                Stubber.excludePackage("xyzzy");
                fail();
            } catch (IllegalStateException e) {
            }
        }
    }

    /**
     * Tests Apache commons logging, which requires the Thread context class
     * loader to be the stubbing class loader.
     */
    public static class TestLogging extends StubbingTestCase {

        public void beforeReloading() {
            Stubber.excludePackage("sun");
        }

        public void test() {
            for (int i = 0; i < 16; ++i) LogFactory.getLog("test" + i);
        }
    }
}
