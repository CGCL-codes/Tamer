package cheesymock;

import java.lang.reflect.*;
import junit.framework.*;

public class StrictPropertyOverrideTest extends TestCase {

    public void testCreationWithNullDelegate() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        s.creation(Interface1.class, null);
    }

    public void testCreationWithGetter() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        s.creation(Interface1.class, new Object() {

            String propOne = "test";
        });
    }

    public void testCreationWithBooleanGetter() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        s.creation(Interface2.class, new Object() {

            boolean propOne = true;
        });
    }

    public void testCreationWithSetter() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        s.creation(Interface3.class, new Object() {

            String propOne = "test";
        });
    }

    public void testCreationWithMissingGetter() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        try {
            s.creation(Interface1.class, new Object() {

                String propTwo = "test";
            });
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreationWithMissingBooleanGetter() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        try {
            s.creation(Interface2.class, new Object() {

                boolean propTwo = true;
            });
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreationWithMissingSetter() throws Exception {
        try {
            StrictPropertyOverride s = new StrictPropertyOverride();
            s.creation(Interface3.class, new Object() {

                String propTwo = "test";
            });
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testTestCreationWithTrickyGetter() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        try {
            s.creation(Interface1.class, new Object() {

                String trickyOne = "test";
            });
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testTestCreationWithWrongTypeGetter() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        try {
            s.creation(Interface1.class, new Object() {

                String wrongTypeOne = "test";
            });
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreationWithTrickyBooleanGetter() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        try {
            s.creation(Interface2.class, new Object() {

                boolean trickyOne = true;
            });
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreationWithWrongTypeBooleanGetter() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        try {
            s.creation(Interface2.class, new Object() {

                boolean wrongTypeOne = true;
            });
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreationWithTrickySetter() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        try {
            s.creation(Interface3.class, new Object() {

                String trickyOne = "test";
            });
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreationWithWrongTypeSetter() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        try {
            s.creation(Interface3.class, new Object() {

                String wrongTypeOne = "test";
            });
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreationWithIgnoreProperty() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        s.creation(Interface1.class, new Object() {

            @Ignore
            int ignore = 0;
        });
    }

    public void testCreationWithInnerFinal() throws Exception {
        StrictPropertyOverride s = new StrictPropertyOverride();
        final Object obj = Cheesy.mock(Object.class);
        s.creation(Interface4.class, new Object() {

            public Object doStuff(Object o) {
                assertEquals(obj, o);
                return obj;
            }
        });
    }

    public static interface Interface1 {

        public String getPropOne();

        public String getTrickyOne(String s);

        public int getWrongTypeOne();
    }

    public static interface Interface2 {

        public boolean isPropOne();

        public boolean isPropOne(boolean b);

        public int isWrongTypeOne();
    }

    public static interface Interface3 {

        public void setPropOne(String propOne);

        public String setTrickyOne(String s);

        public void setWrongTypeOne(int i);
    }

    public static interface Interface4 {

        public Object doStuff(Object o);
    }
}
