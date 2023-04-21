package net.sf.twip;

import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TwiP.class)
public class AutoTwipChainCollectionTest {

    public static class MyType0 {

        private final int i1;

        private final int i2;

        public MyType0(int i1, int i2) {
            this.i1 = i1;
            this.i2 = i2;
        }

        @Override
        public String toString() {
            return "(" + i1 + ":" + i2 + ")";
        }
    }

    public static class MyType1 {

        private final int i;

        private final MyType0 a;

        public MyType1(int i, MyType0 a) {
            this.i = i;
            this.a = a;
        }

        @Override
        public String toString() {
            return "{" + i + ":" + a + "}";
        }
    }

    @AutoTwip
    public static Set<MyType1> autoTwipWithAutoTwip(MyType0 a) {
        return new HashSet<MyType1>(Arrays.asList(new MyType1(0, a), new MyType1(1, a), new MyType1(2, a)));
    }

    @AutoTwip
    public static List<MyType0> autoTwipWithInt(int i) {
        return Arrays.asList(new MyType0(0, i), new MyType0(1, i), new MyType0(2, i));
    }

    @Test
    public void shouldChain(MyType0 m) {
        assert m != null;
    }

    @Test
    public void shouldChain(MyType1 m) {
        assert m != null;
    }
}
