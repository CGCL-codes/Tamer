package de.mxro.examples.k03ss;

public abstract class ListAsRing implements List {

    public static List mkEmpty() {
        return empty;
    }

    public static List mkOne(Object e) {
        return new Ring(e);
    }

    public Object head() {
        return this.at(0);
    }

    public List cons(Object e) {
        return new Ring(e).concat(this);
    }

    public List append(Object e) {
        return this.concat(new Ring(e));
    }

    public List insertAt(int i, Object e) {
        this.splitAt(i);
        return list1.concat(mkOne(e).concat(list2));
    }

    public List removeAt(int i) {
        this.splitAt(i);
        return list1.concat(list2.tail());
    }

    private static final List empty = new Empty();

    protected static List list1, list2;

    protected abstract void splitAt(int i);

    private static final class Empty extends ListAsRing {

        public boolean isEmpty() {
            return true;
        }

        public int length() {
            return 0;
        }

        public Object at(int i) {
            throw new IndexOutOfBoundsException("in at");
        }

        public List tail() {
            throw new IndexOutOfBoundsException("in tail");
        }

        public List concat(List l2) {
            return l2;
        }

        @Override
        protected void splitAt(int i) {
            if (i != 0) throw new IndexOutOfBoundsException("in splitAt");
            list1 = list2 = empty;
        }

        @Override
        public String toString() {
            return "[]";
        }
    }

    private static final class Ring extends ListAsRing {

        private Object info;

        private Ring next;

        Ring(Object e) {
            this.info = e;
            this.next = this;
        }

        public boolean isEmpty() {
            return false;
        }

        public int length() {
            int res;
            Ring r1;
            res = 1;
            r1 = this.next;
            while (r1 != this) {
                res++;
                r1 = r1.next;
            }
            return res;
        }

        public Object at(int i) {
            Ring r1 = this.next;
            while (i != 0) {
                i--;
                r1 = r1.next;
            }
            return r1.info;
        }

        public List tail() {
            if (this == this.next) return empty; else {
                this.splitAt(1);
                return list2;
            }
        }

        public List concat(List l2) {
            if (l2.isEmpty()) return this; else {
                if (!(l2 instanceof Ring)) throw new IllegalArgumentException("concat");
                final Ring r2 = (Ring) l2;
                final Ring t = this.next;
                this.next = r2.next;
                r2.next = t;
                return r2;
            }
        }

        @Override
        protected void splitAt(int i) {
            if (i == 0) {
                list1 = empty;
                list2 = this;
                return;
            }
            Ring r1 = this.next;
            while (i != 1) {
                r1 = r1.next;
                i--;
            }
            if (r1 == this) {
                list1 = this;
                list2 = empty;
            } else {
                final Ring p = r1.next;
                r1.next = this.next;
                this.next = p;
                list1 = r1;
                list2 = p;
            }
        }

        @Override
        public String toString() {
            Ring r1 = this.next;
            String res = "[" + r1.info;
            while (r1 != this) {
                r1 = r1.next;
                res += "," + r1.info;
            }
            res += "]";
            return res;
        }
    }

    public static void main(String[] args) {
        List l1 = ListAsRing.mkOne("Jens");
        System.out.println(l1);
        final List l2 = ListAsRing.mkOne("Nico");
        final List l3 = l1.concat(l2);
        System.out.println(l3);
        List l4;
        l1 = ListAsRing.mkOne("Jens");
        l4 = l1.cons("Nico");
        System.out.println(l4);
        l1 = ListAsRing.mkOne("Jens");
        l4 = l1.append("Nico");
        System.out.println(l4);
    }
}
