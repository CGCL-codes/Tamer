package com.tomgibara.crinch.perm;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import com.tomgibara.crinch.bits.BitVector;
import com.tomgibara.crinch.math.CrinchMath;

public final class Permutation implements Comparable<Permutation>, Serializable {

    static final long serialVersionUID = -9053863703146584610L;

    private static final int[] NO_CYCLES = {};

    public static Permutation identity(int size) {
        if (size < 0) throw new IllegalArgumentException("negative size");
        int[] correspondence = new int[size];
        for (int i = 0; i < size; i++) {
            correspondence[i] = i;
        }
        return new Permutation(correspondence, NO_CYCLES);
    }

    public static Permutation reverse(int size) {
        if (size < 0) throw new IllegalArgumentException("negative size");
        int[] correspondence = new int[size];
        for (int i = 0; i < size; i++) {
            correspondence[i] = size - i - 1;
        }
        int h = size / 2;
        int[] cycles = new int[h * 2];
        for (int i = 0, j = 0; i < h; i++) {
            cycles[j++] = i;
            cycles[j++] = i - size;
        }
        return new Permutation(correspondence, cycles);
    }

    public static Permutation rotate(int size, int distance) {
        if (size < 0) throw new IllegalArgumentException("negative size");
        if (size < 2) return identity(size);
        distance = -distance % size;
        if (distance == 0) return identity(size);
        int[] correspondence = new int[size];
        if (distance < 0) distance += size;
        for (int i = 0; i < size; i++) {
            correspondence[i] = (i + distance) % size;
        }
        int[] cycles = new int[size];
        for (int i = 0, j = i, c = correspondence[j]; c != 0; i++, j = c, c = correspondence[j]) {
            cycles[i] = c;
        }
        cycles[size - 1] = -1 - cycles[size - 1];
        return new Permutation(correspondence, cycles);
    }

    public static Permutation transpose(int size, int i, int j) {
        if (size < 0) throw new IllegalArgumentException("negative size");
        if (i < 0 || j < 0 || i >= size || j >= size) throw new IllegalArgumentException("invalid indices");
        if (i == j) return identity(size);
        int[] correspondence = new int[size];
        for (int k = 0; k < size; k++) {
            correspondence[k] = k;
        }
        correspondence[i] = j;
        correspondence[j] = i;
        int[] cycles = { i, -1 - j };
        return new Permutation(correspondence, cycles);
    }

    private final int[] correspondence;

    private int[] cycles = null;

    private Info info = null;

    private Permutation(int[] correspondence, int[] cycles) {
        this.correspondence = correspondence;
        this.cycles = cycles;
    }

    Permutation(Generator generator) {
        this.correspondence = generator.correspondence.clone();
    }

    public Permutation(int... correspondence) {
        if (correspondence == null) throw new IllegalArgumentException("null correspondence");
        this.correspondence = correspondence;
        cycles = computeCycles(true);
    }

    public int getSize() {
        return correspondence.length;
    }

    public int[] getCorrespondence() {
        return correspondence.clone();
    }

    public Info getInfo() {
        return info == null ? info = new Info() : info;
    }

    public Generator generator() {
        return new Generator(correspondence.clone());
    }

    public <P extends Permutable> P permute(P permutable) {
        if (permutable == null) throw new IllegalArgumentException("null permutable");
        if (permutable.getPermutableSize() != correspondence.length) throw new IllegalArgumentException("size mismatched");
        int[] cycles = getCycles();
        for (int i = 0, initial = -1, previous = -1; i < cycles.length; i++) {
            int next = cycles[i];
            if (initial < 0) {
                initial = next;
            } else {
                if (next < 0) {
                    next = -1 - next;
                    initial = -1;
                }
                permutable = (P) permutable.transpose(previous, next);
            }
            previous = next;
        }
        return permutable;
    }

    public int compareTo(Permutation that) {
        if (this == that) return 0;
        int thisSize = this.correspondence.length;
        int thatSize = that.correspondence.length;
        if (thisSize < thatSize) return -1;
        if (thisSize > thatSize) return 1;
        int[] thisArray = this.correspondence;
        int[] thatArray = that.correspondence;
        for (int i = 0; i < thisSize; i++) {
            int c = thisArray[i] - thatArray[i];
            if (c == 0) continue;
            return c;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(correspondence);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Permutation)) return false;
        Permutation that = (Permutation) obj;
        return Arrays.equals(this.correspondence, that.correspondence);
    }

    @Override
    public String toString() {
        return Arrays.toString(correspondence);
    }

    void generator(Generator generator) {
        System.arraycopy(this.correspondence, 0, generator.correspondence, 0, this.correspondence.length);
    }

    private Object writeReplace() throws ObjectStreamException {
        return new Serial(correspondence);
    }

    private int[] getCycles() {
        if (cycles == null) {
            cycles = computeCycles(false);
        }
        return cycles;
    }

    private int[] computeCycles(boolean verify) {
        if (verify) {
            for (int i = 0; i < correspondence.length; i++) {
                int c = correspondence[i];
                if (c < 0 || c >= correspondence.length) throw new IllegalArgumentException("invalid correspondence");
            }
        }
        int[] array = correspondence.clone();
        int[] cycles = new int[array.length + 1];
        int index = 0;
        outer: while (true) {
            for (int i = 0; i < array.length; i++) {
                int a = array[i];
                if (a == -1) {
                    continue;
                }
                if (a == i) {
                    array[i] = -1;
                    continue;
                }
                int[] correspondence = new int[array.length];
                for (int k = 0; k < array.length; k++) {
                    correspondence[k] = k;
                }
                for (int j = i; ; ) {
                    int b = array[j];
                    if (verify && b == -1) throw new IllegalArgumentException("invalid correspondence");
                    array[j] = -1;
                    if (b == i) {
                        cycles[index++] = -1 - b;
                        break;
                    }
                    cycles[index++] = b;
                    j = b;
                }
                continue outer;
            }
            break;
        }
        return cycles.length > index ? Arrays.copyOf(cycles, index) : cycles;
    }

    private static class Serial implements Serializable {

        private static final long serialVersionUID = -8684974003295220000L;

        int[] correspondence;

        Serial(int[] correspondence) {
            this.correspondence = correspondence;
        }

        private Object readResolve() throws ObjectStreamException {
            return new Permutation(correspondence);
        }
    }

    public final class Info {

        private final int numberOfCycles;

        private final int numberOfTranspositions;

        private final boolean identity;

        private final boolean odd;

        BitVector fixedPoints;

        Set<Permutation> disjointCycles;

        BigInteger lengthOfOrbit;

        public Info() {
            int numberOfCycles = 0;
            int[] cycles = getCycles();
            for (int i = 0; i < cycles.length; i++) {
                if (cycles[i] < 0) numberOfCycles++;
            }
            this.numberOfCycles = numberOfCycles;
            numberOfTranspositions = cycles.length - numberOfCycles;
            identity = numberOfTranspositions == 0;
            odd = (numberOfTranspositions % 2) == 1;
        }

        public Permutation getPermutation() {
            return Permutation.this;
        }

        public boolean isIdentity() {
            return identity;
        }

        public int getNumberOfTranspositions() {
            return numberOfTranspositions;
        }

        public boolean isOdd() {
            return odd;
        }

        public BitVector getFixedPoints() {
            if (fixedPoints == null) {
                int[] array = correspondence;
                fixedPoints = new BitVector(array.length);
                for (int i = 0; i < array.length; i++) {
                    fixedPoints.setBit(i, array[i] == i);
                }
            }
            return fixedPoints;
        }

        public int getNumberOfCycles() {
            return numberOfCycles;
        }

        public Set<Permutation> getDisjointCycles() {
            if (disjointCycles == null) {
                switch(numberOfCycles) {
                    case 0:
                        disjointCycles = Collections.emptySet();
                        break;
                    case 1:
                        disjointCycles = Collections.singleton(Permutation.this);
                        break;
                    default:
                        Set<Permutation> set = new HashSet<Permutation>();
                        int[] array = null;
                        for (int i = 0; i < cycles.length; i++) {
                            if (array == null) {
                                array = new int[correspondence.length];
                                for (int j = 0; j < array.length; j++) {
                                    array[j] = j;
                                }
                            }
                            int a = cycles[i];
                            if (a < 0) {
                                a = -1 - a;
                                array[a] = correspondence[a];
                                set.add(new Permutation(array));
                                array = null;
                            } else {
                                array[a] = correspondence[a];
                            }
                        }
                        disjointCycles = Collections.unmodifiableSet(set);
                }
            }
            return disjointCycles;
        }

        public BigInteger getLengthOfOrbit() {
            if (lengthOfOrbit == null) {
                if (numberOfCycles == 0) {
                    lengthOfOrbit = BigInteger.ONE;
                } else {
                    BigInteger[] lengths = new BigInteger[numberOfCycles];
                    int[] cycles = getCycles();
                    int count = 0;
                    int length = 0;
                    for (int i = 0; i < cycles.length; i++) {
                        if (cycles[i] < 0) {
                            lengths[count++] = BigInteger.valueOf(length + 1);
                            length = 0;
                        } else {
                            length++;
                        }
                    }
                    lengthOfOrbit = CrinchMath.lcm(lengths);
                }
            }
            return lengthOfOrbit;
        }

        @Override
        public int hashCode() {
            return Permutation.this.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof Info)) return false;
            Info that = (Info) obj;
            return this.getPermutation().equals(that.getPermutation());
        }

        @Override
        public String toString() {
            return "Permutation.Info for " + getPermutation().toString();
        }
    }

    public static final class Generator implements Permutable {

        final int[] correspondence;

        private OrderedSequence orderedSequence = null;

        Generator(int[] correspondence) {
            this.correspondence = correspondence;
        }

        public OrderedSequence getOrderedSequence() {
            return orderedSequence == null ? orderedSequence = new OrderedSequence() : orderedSequence;
        }

        public Generator set(Permutation permutation) {
            if (permutation == null) throw new IllegalArgumentException("null permutation");
            if (permutation.getSize() != correspondence.length) throw new IllegalArgumentException("incorrect size");
            permutation.generator(this);
            return this;
        }

        public Generator identity() {
            for (int i = 0; i < correspondence.length; i++) {
                correspondence[i] = i;
            }
            return this;
        }

        public Generator rotate(int distance) {
            int size = correspondence.length;
            if (size == 0) return this;
            distance = distance % size;
            if (distance == 0) return this;
            if (distance < 0) distance += size;
            for (int start = 0, count = 0; count != size; start++) {
                int prev = correspondence[start];
                int i = start;
                do {
                    i += distance;
                    if (i >= size) i -= size;
                    int next = correspondence[i];
                    correspondence[i] = prev;
                    prev = next;
                    count++;
                } while (i != start);
            }
            return this;
        }

        public Generator invert() {
            int[] array = new int[correspondence.length];
            for (int i = 0; i < array.length; i++) {
                array[correspondence[i]] = i;
            }
            System.arraycopy(array, 0, correspondence, 0, array.length);
            return this;
        }

        public Generator reverse() {
            int h = correspondence.length / 2;
            for (int i = 0, j = correspondence.length - 1; i < h; i++, j--) {
                swap(i, j);
            }
            return this;
        }

        public Generator shuffle(Random random) {
            for (int i = correspondence.length - 1; i > 0; i--) {
                transpose(i, random.nextInt(i + 1));
            }
            return this;
        }

        public Generator apply(Permutation permutation) {
            permutation.permute(this);
            return this;
        }

        public Generator power(int power) {
            if (power == 0) {
                identity();
                return this;
            }
            if (power < 0) {
                invert();
                power = -power;
            }
            if (power == 1) {
                return this;
            }
            Permutation p = permutation();
            identity();
            while (power > 0) {
                if ((power & 1) == 1) p.permute(this);
                p = p.generator().apply(p).permutation();
                power >>= 1;
            }
            return this;
        }

        public Permutation permutation() {
            return new Permutation(this);
        }

        @Override
        public int getPermutableSize() {
            return correspondence.length;
        }

        @Override
        public Generator transpose(int i, int j) {
            if (i < 0) throw new IllegalArgumentException("negative i");
            if (j < 0) throw new IllegalArgumentException("negative j");
            if (i > correspondence.length) throw new IllegalArgumentException("i greater than or equal to size");
            if (j > correspondence.length) throw new IllegalArgumentException("j greater than or equal to size");
            if (j != i) swap(i, j);
            return this;
        }

        @Override
        public String toString() {
            return Arrays.toString(correspondence);
        }

        private void swap(int i, int j) {
            int t = correspondence[i];
            correspondence[i] = correspondence[j];
            correspondence[j] = t;
        }

        private void nextByNumber(boolean ascending) {
            int len = correspondence.length;
            int j = -1;
            for (int i = len - 2; i >= 0; i--) {
                if (ascending ? correspondence[i] < correspondence[i + 1] : correspondence[i] > correspondence[i + 1]) {
                    j = i;
                    break;
                }
            }
            if (j == -1) throw new IllegalStateException("no such permutation");
            int c = correspondence[j];
            int k = 0;
            for (int i = len - 1; i > j; i--) {
                if (ascending ? c < correspondence[i] : c > correspondence[i]) {
                    k = i;
                    break;
                }
            }
            swap(j, k);
            int h = (j + 1 + len) / 2;
            for (int i = j + 1, m = len - 1; i < h; i++, m--) {
                swap(i, m);
            }
        }

        private class OrderedSequence implements PermutationSequence {

            public boolean hasNext() {
                int[] array = correspondence;
                for (int i = 1; i < array.length; i++) {
                    if (array[i] > array[i - 1]) return true;
                }
                return false;
            }

            public boolean hasPrevious() {
                int[] array = correspondence;
                for (int i = 1; i < array.length; i++) {
                    if (array[i] < array[i - 1]) return true;
                }
                return false;
            }

            @Override
            public PermutationSequence next() {
                nextByNumber(true);
                return this;
            }

            @Override
            public PermutationSequence previous() {
                nextByNumber(false);
                return this;
            }

            @Override
            public Generator getGenerator() {
                return Generator.this;
            }

            @Override
            public String toString() {
                return "OrderedSequence at " + Generator.this.toString();
            }
        }
    }
}
