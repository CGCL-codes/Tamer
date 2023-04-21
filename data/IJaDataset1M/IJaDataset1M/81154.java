package org.middleheaven.tools.test;

import org.junit.Test;
import org.middleheaven.tool.test.EqualsAssert;

public class TestEquals {

    @Test
    public void testEqualsConsistency() {
        String a1 = "a";
        String a2 = new String("a");
        String b = new String("b");
        EqualsAssert.assertEqualsImplementation(a1, b, a2);
        Foo f1 = new Foo(1);
        Foo f2 = new Foo(1);
        Foo f3 = new Foo(2);
        Foo f4 = new Foo(1) {
        };
        EqualsAssert.assertEqualsImplementation(f1, f3, f2, f4);
    }

    public static class Foo {

        private int a;

        Foo(int a) {
            this.a = a;
        }

        public int hashCode() {
            return a;
        }

        public boolean equals(Object other) {
            return other instanceof Foo && ((Foo) other).a == a;
        }
    }
}
