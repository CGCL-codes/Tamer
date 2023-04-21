package net.jadoth.collections.hashing;

import static net.jadoth.util.chars.VarChar.MediumVarChar;
import net.jadoth.collections.interfaces.Sized;
import net.jadoth.collections.types.XGettingList;
import net.jadoth.lang.HashEqualator;
import net.jadoth.lang.functional.Procedure;
import net.jadoth.lang.reference.ReferenceType;
import net.jadoth.util.KeyValue;
import net.jadoth.util.chars.VarChar;

/**
 * @author Thomas M�nz
 *
 */
public interface HashCollection<E> extends Sized {

    public float hashDensity();

    /**
	 * Sets the hash density (1/density) of this hashing collection if applicable.
	 * <p>
	 * If this procedure is not applicable for the hash collection (e.g. an immutable hash collection), calling this
	 * method has no effect.
	 *
	 * @param hashDensity the new hash density to be set.
	 * @throws IllegalArgumentException if the passed value would have an effect but is less than or equal to 0.
	 */
    public void setHashDensity(final float hashDensity);

    public HashLogic<E> hashLogic();

    public HashEqualator<E> hashEquality();

    public Analysis<? extends HashCollection<E>> analyze();

    public boolean hasVolatileHashElements();

    public ReferenceType hashReferenceType();

    public int hashDistributionRange();

    @Override
    public int size();

    public class Analysis<H> {

        private final H subject;

        private final int size;

        private final float hashDensity;

        private final int slotCount;

        private final int shortestEntryChainLength;

        private final double averageEntryChainLength;

        private final int longestEntryChainLength;

        private final int distributionRange;

        private final XGettingList<KeyValue<Integer, Integer>> chainLengthDistribution;

        private final double distributionEfficienty;

        private final double storageEfficienty;

        public Analysis(final H subject, final int size, final float hashDensity, final int slotCount, final int shortestEntryChainLength, final int longestEntryChainLength, final int distributionRange, final XGettingList<KeyValue<Integer, Integer>> chainLengthDistribution) {
            super();
            this.subject = subject;
            this.size = size;
            this.hashDensity = hashDensity;
            this.slotCount = slotCount;
            this.shortestEntryChainLength = shortestEntryChainLength;
            this.averageEntryChainLength = (double) size / slotCount;
            this.longestEntryChainLength = longestEntryChainLength;
            this.chainLengthDistribution = chainLengthDistribution;
            this.distributionEfficienty = 1 / this.averageEntryChainLength;
            this.storageEfficienty = (double) slotCount / distributionRange;
            this.distributionRange = distributionRange;
        }

        public H getSubject() {
            return this.subject;
        }

        public float getHashDensity() {
            return this.hashDensity;
        }

        public int getSlotCount() {
            return this.slotCount;
        }

        public int getShortestEntryChainLength() {
            return this.shortestEntryChainLength;
        }

        public double getAverageEntryChainLength() {
            return this.averageEntryChainLength;
        }

        public int getLongestEntryChainLength() {
            return this.longestEntryChainLength;
        }

        public XGettingList<KeyValue<Integer, Integer>> getChainLengthDistribution() {
            return this.chainLengthDistribution;
        }

        public int getSize() {
            return this.size;
        }

        public double getDistributionEfficienty() {
            return this.distributionEfficienty;
        }

        public double getStorageEfficienty() {
            return this.storageEfficienty;
        }

        public int getDistributionRange() {
            return this.distributionRange;
        }

        /**
		 * @return
		 * @see java.lang.Object#toString()
		 */
        @Override
        public String toString() {
            final VarChar vc = MediumVarChar().append("subject: ").append(this.subject.getClass() + " @" + System.identityHashCode(this.subject)).lf().append("size: ").append(this.size).lf().append("hashDensity: ").append(this.hashDensity).lf().append("slotCount: ").append(this.slotCount).lf().append("shortestEntryChainLength: ").append(this.shortestEntryChainLength).lf().append("averageEntryChainLength: ").append(this.averageEntryChainLength).lf().append("longestEntryChainLength: ").append(this.longestEntryChainLength).lf().append("distributionRange: ").append(this.distributionRange).lf().append("distributionEfficienty: ").append(this.distributionEfficienty).lf().append("storageEfficienty: ").append(this.storageEfficienty).lf().lf().append("slot occupation: ").lf();
            final int[] a = new int[this.distributionRange];
            this.chainLengthDistribution.execute(new Procedure<KeyValue<Integer, Integer>>() {

                @Override
                public void apply(final KeyValue<Integer, Integer> e) {
                    vc.append(e.key()).append(": ").append(e.value()).append('\t').repeat(e.value(), '|').lf();
                    for (int i = e.key() + 1; i-- > 1; ) {
                        a[i] += e.value();
                    }
                }
            });
            a[0] = this.chainLengthDistribution.get(0).value();
            vc.lf().append("entry distribution: ").lf();
            for (int i = 0; i < a.length; i++) {
                vc.append(i == 0 ? "empty" : "rank" + i).append(": ").append(a[i]).append('\t').repeat(a[i], '|').lf();
            }
            return vc.toString();
        }
    }
}
