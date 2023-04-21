package net.sf.wtk.common.math;

public class Predicates {

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> TRUE() {
        return (Predicate<T>) TRUE;
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> FALSE() {
        return (Predicate<T>) FALSE;
    }

    public static <T> Predicate<T> not(final Predicate<T> p) {
        if (p == TRUE) return FALSE();
        if (p == FALSE) return TRUE();
        return new Not<T>(p);
    }

    public static <T> Predicate<T> and(final Predicate<T> p1, final Predicate<T> p2) {
        if (p1 == TRUE) return p2;
        if (p2 == TRUE) return p1;
        if ((p1 == FALSE) || (p2 == FALSE)) return FALSE();
        return new And2<T>(p1, p2);
    }

    public static <T> Predicate<T> and(final Predicate<T> p1, final Predicate<T> p2, final Predicate<T> p3) {
        if (p1 == TRUE) return and(p2, p3);
        if (p2 == TRUE) return and(p1, p3);
        if (p3 == TRUE) return and(p1, p2);
        if ((p1 == FALSE) || (p2 == FALSE) || (p3 == FALSE)) return FALSE();
        return new And3<T>(p3, p1, p2);
    }

    public static <T> Predicate<T> and(final Predicate<T>... ps) {
        int writeIndex = 0;
        for (int cnt = ps.length, testIndex = 0; testIndex < cnt; testIndex++) {
            Predicate<T> p = ps[testIndex];
            if (p == FALSE) return FALSE();
            if (p == TRUE) continue;
            ps[writeIndex++] = p;
        }
        final int cnt = writeIndex;
        switch(cnt) {
            case 0:
                return TRUE();
            case 1:
                return ps[0];
            case 2:
                return new And2<T>(ps[1], ps[0]);
            case 3:
                return new And3<T>(ps[0], ps[1], ps[2]);
            default:
                return new AndN<T>(cnt, ps);
        }
    }

    public static <T> Predicate<T> or(Predicate<T> p1, Predicate<T> p2) {
        if (p1 == FALSE) return p2;
        if (p2 == FALSE) return p1;
        if (p1 == TRUE || p2 == TRUE) return TRUE();
        return new Or2<T>(p2, p1);
    }

    public static <T> Predicate<T> or(Predicate<T> p1, Predicate<T> p2, Predicate<T> p3) {
        if (p1 == FALSE) return or(p2, p3);
        if (p2 == FALSE) return or(p1, p3);
        if (p3 == FALSE) return or(p1, p2);
        if (p1 == TRUE || p2 == TRUE || p3 == TRUE) return TRUE();
        return new Or3<T>(p1, p2, p3);
    }

    public static <T> Predicate<T> or(final Predicate<T>... ps) {
        int writeIndex = 0;
        for (int cnt = ps.length, testIndex = 0; testIndex < cnt; testIndex++) {
            Predicate<T> p = ps[testIndex];
            if (p == TRUE) return TRUE();
            if (p == FALSE) continue;
            ps[writeIndex++] = p;
        }
        final int cnt = writeIndex;
        switch(cnt) {
            case 0:
                return FALSE();
            case 1:
                return ps[0];
            case 2:
                return new Or2<T>(ps[1], ps[0]);
            case 3:
                return new Or3<T>(ps[0], ps[1], ps[2]);
            default:
                return new OrN<T>(cnt, ps);
        }
    }

    private static final Predicate<?> TRUE = new Predicate<Object>() {

        public boolean evaluate(Object obj) {
            return true;
        }
    };

    private static final Predicate<?> FALSE = new Predicate<Object>() {

        public boolean evaluate(Object obj) {
            return false;
        }
    };

    private static final class Not<T> implements Predicate<T> {

        private final Predicate<T> p;

        private Not(Predicate<T> p) {
            this.p = p;
        }

        public boolean evaluate(T obj) {
            return !p.evaluate(obj);
        }
    }

    private static final class And2<T> implements Predicate<T> {

        private final Predicate<T> p1;

        private final Predicate<T> p2;

        private And2(Predicate<T> p1, Predicate<T> p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public boolean evaluate(T obj) {
            return p1.evaluate(obj) && p2.evaluate(obj);
        }
    }

    private static final class And3<T> implements Predicate<T> {

        private final Predicate<T> p3;

        private final Predicate<T> p1;

        private final Predicate<T> p2;

        private And3(Predicate<T> p3, Predicate<T> p1, Predicate<T> p2) {
            this.p3 = p3;
            this.p1 = p1;
            this.p2 = p2;
        }

        public boolean evaluate(T obj) {
            return p1.evaluate(obj) && p2.evaluate(obj) && p3.evaluate(obj);
        }
    }

    private static final class AndN<T> implements Predicate<T> {

        private final Predicate<T>[] ps;

        private final int cnt;

        private AndN(int cnt, Predicate<T>[] ps) {
            this.cnt = cnt;
            this.ps = ps;
        }

        public boolean evaluate(T obj) {
            for (int n = 0; n < cnt; n++) {
                if (!ps[n].evaluate(obj)) return false;
            }
            return true;
        }
    }

    private static final class Or2<T> implements Predicate<T> {

        private final Predicate<T> p2;

        private final Predicate<T> p1;

        private Or2(Predicate<T> p2, Predicate<T> p1) {
            this.p2 = p2;
            this.p1 = p1;
        }

        public boolean evaluate(T obj) {
            return p1.evaluate(obj) || p2.evaluate(obj);
        }
    }

    private static final class Or3<T> implements Predicate<T> {

        private final Predicate<T> p1;

        private final Predicate<T> p2;

        private final Predicate<T> p3;

        private Or3(Predicate<T> p1, Predicate<T> p2, Predicate<T> p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }

        public boolean evaluate(T obj) {
            return p1.evaluate(obj) || p2.evaluate(obj) || p3.evaluate(obj);
        }
    }

    private static final class OrN<T> implements Predicate<T> {

        private final int cnt;

        private final Predicate<T>[] ps;

        private OrN(int cnt, Predicate<T>[] ps) {
            this.cnt = cnt;
            this.ps = ps;
        }

        public boolean evaluate(T obj) {
            for (int n = 0; n < cnt; n++) {
                if (ps[n].evaluate(obj)) return true;
            }
            return false;
        }
    }
}
