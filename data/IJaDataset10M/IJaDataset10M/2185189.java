package org.iqual.chaplin.example.basic.defaultMethod;

import junit.framework.TestCase;
import org.iqual.chaplin.FromContext;
import org.iqual.chaplin.Binder;
import org.iqual.chaplin.ToContext;
import static org.iqual.chaplin.DynaCastUtils.$;

/**
 * @author Zbynek Slajchrt
 * @since Jun 2, 2009 9:51:51 PM
 */
public class DefaultMethodTest extends TestCase {

    public abstract static class A {

        @FromContext
        int getX() {
            return 1;
        }

        public int computeX() {
            return 3 * getX();
        }
    }

    public abstract static class B extends A {

        @FromContext
        int getX() {
            return 2;
        }
    }

    @Binder
    public static class Comp {

        @ToContext
        final A a = $();

        @ToContext
        final B b = $();

        public int getFromA() {
            return a.computeX();
        }

        public int getFromB() {
            return b.computeX();
        }
    }

    @Binder
    public static class SuperComp extends Comp {

        public int getX() {
            return 10;
        }
    }

    public void testDefaultMethod() {
        Comp c = new Comp();
        assertEquals(3, c.getFromA());
        assertEquals(6, c.getFromB());
    }

    public void testOveriddenDefaultMethod() {
    }
}
