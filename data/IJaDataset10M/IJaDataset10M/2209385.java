package org.grobid.core.utilities;

import java.util.*;

/**
 * Distance and aligment of strings based on Levenshtein edit distances using the
 * standard <b>Dynamic Programming</b> algorithm. Standard Levenshtein distance is
 * implemented, i.e. without transpose!
 * <p/>
 * Based on algorithm at
 * - http://www.csse.monash.edu.au/~lloyd/tildeStrings/Alignment/92.IPL.html
 * - http://www.csse.monash.edu.au/~lloyd/tildeFP/Haskell/1998/Edit01/
 * - Xuan Luo implementation http://xuanluo.bol.ucla.edu/Levenshtein.java
 */
public class LevenshteinDistanceAligment<E> {

    private final E[] a, b;

    private Diagonal<E> diag;

    private int dist;

    private boolean twist = false;

    public LevenshteinDistanceAligment(E[] _a, E[] _b) {
        if (_a.length < _b.length) {
            a = _a;
            b = _b;
            twist = false;
        } else {
            a = _b;
            b = _a;
            twist = true;
        }
        compute();
    }

    public enum Op {

        Start, Match, Insert, Delete, Substitute
    }

    private static class Diagonal<E> {

        /**
         * diagonal starts at a[0], b[abs(offset)]
         * lower half has negative offset
         */
        private final int offset;

        private final E[] a;

        /**
         * left string
         */
        private final E[] b;

        /**
         * top string
         */
        private Diagonal<E> prev;

        /**
         * below-left diagonal
         */
        private Diagonal<E> next = null;

        private final List<Integer> elements = new ArrayList<Integer>();

        public Diagonal(E[] _a, E[] _b, Diagonal<E> diagBelow, int o) {
            assert Math.abs(o) <= _b.length;
            a = _a;
            b = _b;
            prev = diagBelow;
            offset = o;
            elements.add(Math.abs(offset));
        }

        /**
         * returns below diagonal, creating it if necessary
         */
        public Diagonal<E> getBelow() {
            if (prev == null) {
                assert offset == 0;
                prev = new Diagonal<E>(b, a, this, -1);
            }
            return prev;
        }

        /**
         * returns above diagonal, creating it if necessary
         */
        public Diagonal<E> getAbove() {
            if (next == null) next = new Diagonal<E>(a, b, this, offset >= 0 ? offset + 1 : offset - 1);
            return next;
        }

        /**
         * get entry to the left
         */
        public int getW(int i) {
            assert i >= 0 && (offset != 0 || i > 0);
            return getBelow().get(offset == 0 ? i - 1 : i);
        }

        /**
         * get entry above
         */
        public int getN(int i) {
            assert i > 0;
            return getAbove().get(i - 1);
        }

        /**
         * compute element j of this diagonal
         */
        public int get(int j) {
            assert j >= 0 && j <= b.length - Math.abs(offset) && j <= a.length;
            if (j < elements.size()) return elements.get(j);
            int me = elements.get(elements.size() - 1);
            while (elements.size() <= j) {
                int nw = me;
                int i = elements.size();
                if (a[i - 1].equals(b[Math.abs(offset) + i - 1])) me = nw; else {
                    int w = getW(i);
                    if (w < nw) me = 1 + w; else {
                        int n = getN(i);
                        me = 1 + Math.min(nw, n);
                    }
                }
                elements.add(me);
            }
            return me;
        }

        /**
         * get the last operation used to get to a certain element
         */
        public Op getOp(int i) {
            if (i == 0) {
                if (offset == 0) return Op.Start; else if (offset > 0) return Op.Insert; else return Op.Delete;
            } else if ((offset + i - 1) >= 0) {
                if (a[i - 1].equals(b[offset + i - 1])) return Op.Match; else {
                    int me = get(i);
                    int w = getW(i);
                    int nw = get(i - 1);
                    if (me == 1 + w) return offset >= 0 ? Op.Insert : Op.Delete; else if (me == 1 + nw) return Op.Substitute; else return offset >= 0 ? Op.Delete : Op.Insert;
                }
            } else {
                int me = get(i);
                int w = getW(i);
                int nw = get(i - 1);
                if (me == 1 + w) return offset >= 0 ? Op.Insert : Op.Delete; else if (me == 1 + nw) return Op.Substitute; else return offset >= 0 ? Op.Delete : Op.Insert;
            }
        }
    }

    /**
     * perform the Levenshtein distance computation of sequences a and b
     */
    private void compute() {
        Diagonal<E> mainDiag = new Diagonal<E>(a, b, null, 0);
        int lba = b.length - a.length;
        if (lba >= 0) {
            diag = mainDiag;
            for (int i = 0; i < lba; i++) diag = diag.getAbove();
        } else {
            diag = mainDiag.getBelow();
            for (int i = 0; i < ~lba; i++) diag = diag.getAbove();
        }
        dist = diag.get(Math.min(a.length, b.length));
    }

    /**
     * retrieves the Levenshtein distance
     */
    public int getDistance() {
        return dist;
    }

    /**
     * retrieves the Levenshtein distance alignment
     */
    public List<Op> getAlignment() {
        Diagonal<E> diag = this.diag;
        LinkedList<Op> alignment = new LinkedList<Op>();
        int i = Math.min(a.length, b.length);
        LOOP: while (true) {
            if (diag == null) break LOOP;
            Op op = diag.getOp(i);
            switch(op) {
                case Match:
                case Substitute:
                    i--;
                    break;
                case Insert:
                    if (diag.offset == 0) {
                        diag = diag.prev;
                        i--;
                    } else if (diag.offset >= 0) {
                        diag = diag.prev;
                    } else {
                        diag = diag.next;
                        i--;
                    }
                    break;
                case Delete:
                    if (diag.offset == 0) {
                        diag = diag.next;
                        i--;
                    } else if (diag.offset >= 0) {
                        diag = diag.next;
                        i--;
                    } else {
                        diag = diag.prev;
                    }
                    break;
                case Start:
                    break LOOP;
            }
            alignment.add(op);
        }
        Collections.reverse(alignment);
        if (twist) {
            LinkedList<Op> alignment2 = new LinkedList<Op>();
            for (Op op : alignment) {
                if (op == Op.Delete) alignment2.add(Op.Insert); else if (op == Op.Insert) alignment2.add(Op.Delete); else alignment2.add(op);
            }
            alignment = alignment2;
        }
        return alignment;
    }

    public static Character[] str2chararray(String x) {
        Character[] result = new Character[x.length()];
        for (int i = 0; i < x.length(); i++) {
            result[i] = x.charAt(i);
        }
        return result;
    }

    public static void main(String[] args) {
        assert args.length == 2;
        LevenshteinDistanceAligment<Character> foo = new LevenshteinDistanceAligment<Character>(str2chararray(args[0]), str2chararray(args[1]));
        System.out.println("Levenshtein distance = " + foo.getDistance());
        System.out.println("Alignment: " + foo.getAlignment());
    }
}
