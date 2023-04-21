package org.yuzz.functor;

public class Tuples {

    public abstract static class Tuple {

        public abstract Object[] toArray();
    }

    public static class Tuple0<T> extends Tuple {

        @Override
        public Object[] toArray() {
            return new Object[] {};
        }
    }

    public static class Tuple1<T> extends Tuple {

        private final T _a;

        public Tuple1(T a) {
            _a = a;
        }

        public T _1() {
            return _a;
        }

        @Override
        public Object[] toArray() {
            return new Object[] { _a };
        }
    }

    public static class Tuple2<A, B> extends Tuple {

        private final A _a;

        private final B _b;

        public Tuple2(A a, B b) {
            _a = a;
            _b = b;
        }

        public A _1() {
            return _a;
        }

        public B _2() {
            return _b;
        }

        @Override
        public Object[] toArray() {
            return new Object[] { _a, _b };
        }

        public String toString() {
            return "(" + _a + "," + _b + ")";
        }

        public static Tuple2[] array(Tuple2... tuple2s) {
            return tuple2s;
        }
    }

    public static class Tuple3<A, B, C> extends Tuple {

        private final A _a;

        private final B _b;

        private final C _c;

        public Tuple3(A a, B b, C c) {
            _a = a;
            _b = b;
            _c = c;
        }

        public A _1() {
            return _a;
        }

        public B _2() {
            return _b;
        }

        public C _3() {
            return _c;
        }

        @Override
        public Object[] toArray() {
            return new Object[] { _a, _b, _c };
        }

        public String toString() {
            return "(" + _a + "," + _b + "," + _c + ")";
        }

        public static <A, B, C> Tuple3<A, B, C>[] array(Tuple3<A, B, C>... tuples) {
            return tuples;
        }
    }

    public static <A, B> Tuple1<A> tuple1(A a) {
        return new Tuple1<A>(a);
    }

    public static <A, B> Tuple2<A, B> tuple2(A a, B b) {
        return new Tuple2<A, B>(a, b);
    }

    public static <A, B, C> Tuple3<A, B, C> tuple3(A a, B b, C c) {
        return new Tuple3<A, B, C>(a, b, c);
    }
}
