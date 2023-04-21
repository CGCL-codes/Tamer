package net.jadoth.util;

import static net.jadoth.util.chars.VarChar.MediumVarChar;
import java.text.DecimalFormat;
import net.jadoth.Jadoth;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.lang.Equalator;
import net.jadoth.lang.Similator;
import net.jadoth.lang.functional.Joiner;
import net.jadoth.math.JaMath;
import net.jadoth.util.chars.JaChars;
import net.jadoth.util.chars.VarChar;

/**
 * Logic for bidirectionally and exclusively linking all matching items from two collections according
 * to equality and/or sufficient similarity.
 * <p>
 * Exclusviely means each item in both collections can at most be linked with one item from the other collection.
 * Bidirectionally means the link between two items always has two directions. If item A is linked to item B, item B
 * is inherently linked to item A as well.
 * <p>
 * Equality and similarity are defined by {@link Equalator} and {@link Similator} functions that can be passed at
 * creation time. All values controlling the matching algorithm can be optionally configured in the factory class
 * if the default configuration is not desired. Additionally, a callback function for deciding found matches with
 * questionable similarity can be injected.
 * <p>
 * This is a powerful general purpose means of building associations of two sets of similar but not equal elements.<br>
 * A very simple use case is the formal recognition of a changed table column structure (for which this class
 * was originally developed).
 * <p>
 * For example given the following two hypothetical definitions (old and new) of column names:<br>
 * <br>
 * <u><b>Old</b></u>:
 * <ul>
 * <li>Name</li>
 * <li>Firstname</li>
 * <li>Age</li>
 * <li>Address</li>
 * <li>Freetext</li>
 * <li>Email</li>
 * <li>OtherAddress</li>
 * </ul>
 * and<br>
 * <br>
 * <u><b>New</b></u>:
 * <ul>
 * <li>firstname</li>
 * <li>lastname</li>
 * <li>age</li>
 * <li>emailAddress</li>
 * <li>postalAddress</li>
 * <li>noteLink</li>
 * <li>newColumn1</li>
 * <li>someMiscAddress</li>
 * </ul>
 * When using a case insensitive modified Levenshtein {@link Similator}
 * (see {@link JaChars#levenshteinSubstringSimilarity}) the algorithm produces the following associations:
 * <pre>
 * firstname       <-1.00-> Firstname
 * lastname        <-0.75-> Name
 * age             <-1.00-> Age
 * emailAddress    <-0.71-> Email
 * postalAddress   <-0.77-> Address
 * noteLink        +
 * newColumn1      +
 * someMiscAddress <-0.56-> OtherAddress
 *                        x Freetext
 * </pre>
 *
 * @author Thomas M�nz
 *
 * @param <T> the type of the items being matched.
 */
public interface ItemMatcher<E> {

    public double getSimilarityThreshold();

    public double getSingletonPrecedenceThreshold();

    public double getSingletonPrecedenceBonus();

    public double getNoiseFactor();

    public Equalator<? super E> equalator();

    public Similator<? super E> similator();

    public MatchCallback<? super E> matchCallback();

    public ItemMatcher<E> setSimilarityThreshold(double similarityThreshold);

    public ItemMatcher<E> setSingletonPrecedenceThreshold(double singletonPrecedenceThreshold);

    public ItemMatcher<E> setSingletonPrecedenceBonus(double singletonPrecedenceBonus);

    public ItemMatcher<E> setNoisefactor(double noiseFactor);

    public ItemMatcher<E> setSimilator(Similator<? super E> similator);

    public ItemMatcher<E> setEqualator(Equalator<? super E> equalator);

    public ItemMatcher<E> setMatchCallback(MatchCallback<? super E> decisionCallback);

    public ItemMatch<E> match(XGettingCollection<? extends E> source, XGettingCollection<? extends E> target);

    public class Static {

        static final double DEFAULT_SIMILARITY_THRESHOLD = 0.50D;

        static final double DEFAULT_SINGLETON_PRECEDENCE_THRESHOLD = 0.75D;

        static final double DEFAULT_SINGLETON_PRECEDENCE_BONUS = 1.25D;

        static final double DEFAULT_NOISE_FACTOR = 0.50;

        static final DecimalFormat SIM_FORMAT = JaChars.createDecimalFormatter("0.00", java.util.Locale.ENGLISH);

        static int calcMatchCount(final int[] s2tMapping) {
            int matchCount = 0;
            for (int i = 0; i < s2tMapping.length; i++) {
                if (s2tMapping[i] < 0) continue;
                matchCount++;
            }
            return matchCount;
        }

        static int maxTargetQuantifier(final int[] sTargets) {
            int maxQuantifier = 0;
            for (int t = 0; t < sTargets.length; t++) {
                if (sTargets[t] > maxQuantifier) {
                    maxQuantifier = sTargets[t];
                }
            }
            return maxQuantifier;
        }

        static int maxSourceQuantifier(final int[][] quantifiers, final int t) {
            int maxQuantifier = 0;
            for (int s = 0; s < quantifiers.length; s++) {
                if (quantifiers[s][t] > maxQuantifier) {
                    maxQuantifier = quantifiers[s][t];
                }
            }
            return maxQuantifier;
        }

        protected static <E> VarChar assembleState(final ItemMatch<E> im, final VarChar vc, final String title, final Joiner<VarChar, ? super E> appender) {
            final int mc = calcMatchCount(im.s2tMapping);
            vc.append(title).lf().append("[candidateCount = " + Math.min(im.sourceCandidateCount, im.targetCandidateCount) + "]").append("[totalSimilarity = " + JaMath.round3(im.getTotalSimilarity()) + "]").lf().append("[matchCount = " + mc + "]").append("[averageSimilarity = " + (mc == 0 ? 0 : JaMath.round3(im.getTotalSimilarity() / mc)) + "]").lf();
            vc.append("s\\t").tab();
            final int tLength = im.quantifiers[0].length;
            for (int t = 0; t < tLength; t++) {
                vc.append(t).tab();
            }
            vc.append("s2t").lf();
            for (int s = 0; s < im.quantifiers.length; s++) {
                final int[] sTargets = im.quantifiers[s];
                vc.append(s).tab();
                for (int t = 0; t < sTargets.length; t++) {
                    if (sTargets[t] > 0) {
                        vc.append(SIM_FORMAT.format(ItemMatch.similarity(sTargets[t])));
                    }
                    vc.tab();
                }
                if (im.s2tMapping[s] >= 0) {
                    vc.append("#" + im.s2tMapping[s]).tab();
                    appender.apply(vc, im.source[s]);
                    vc.tab();
                    appender.apply(vc, im.target[im.s2tMapping[s]]);
                } else if (im.sCandCount[s] > 0) {
                    vc.append("[" + im.sCandCount[s] + "]");
                } else {
                    vc.tab();
                    appender.apply(vc, im.source[s]);
                    vc.tab();
                }
                vc.lf();
            }
            vc.append("t2s").tab();
            for (int t = 0; t < tLength; t++) {
                if (im.t2sMapping[t] >= 0) {
                    vc.append("#" + im.t2sMapping[t]);
                } else if (im.tCandCount[t] > 0) {
                    vc.append("[" + im.tCandCount[t] + "]");
                }
                vc.tab();
            }
            vc.lf();
            vc.lf();
            vc.lf();
            return vc;
        }

        public static <E> VarChar assembleMappingSchemeHorizontal(final ItemMatchResult<E> im, final VarChar vc, final Joiner<VarChar, ? super E> appender) {
            final VarChar line2 = MediumVarChar(), line3 = MediumVarChar(), line4 = MediumVarChar();
            for (final E e : im.getInputSources()) {
                appender.apply(vc, e);
                vc.tab();
            }
            for (final KeyValue<E, E> e : im.getMatchesInSourceOrder()) {
                if (e != null) {
                    line2.append('|');
                    line3.append('|');
                    appender.apply(line4, e.value());
                } else {
                    line2.append('+');
                }
                line2.tab();
                line3.tab();
                line4.tab();
            }
            for (final E e : im.getUnmatchedTargets()) {
                line3.append('-').tab();
                appender.apply(line4, e).tab();
            }
            return vc.lf().append(line2).lf().append(line3).lf().append(line4);
        }

        public static <E> VarChar assembleMappingSchemeVertical(final ItemMatch<E> im, final VarChar vc, final Joiner<VarChar, ? super E> appender) {
            for (int s = 0; s < im.source.length; s++) {
                appender.apply(vc, im.source[s]);
                if (im.linkedTargets[s] != null) {
                    vc.append("\t<-");
                    vc.append(SIM_FORMAT.format(ItemMatch.similarity(im.linkedSrcQuantifiers[s])));
                    vc.append("->\t");
                    appender.apply(vc, im.linkedTargets[s]);
                } else {
                    vc.append("\t+");
                }
                vc.lf();
            }
            for (int t = 0; t < im.trg.length; t++) {
                if (im.trg[t] != null) {
                    vc.append("\t       x\t");
                    appender.apply(vc, im.trg[t]);
                    vc.lf();
                }
            }
            return vc;
        }
    }

    public interface MatchCallback<E> {

        public boolean isValidMatch(E sourceItem, E targetItem, double similarity, int sourceCandidateCount, int targetCandidateCount);
    }

    public class Implementation<E> implements ItemMatcher<E> {

        private Equalator<? super E> equalator = Jadoth.equals;

        private Similator<? super E> similator = null;

        private MatchCallback<? super E> suspiciousMatchDecider = null;

        private double similarityThreshold = ItemMatcher.Static.DEFAULT_SIMILARITY_THRESHOLD;

        private double singletonPrecedenceThreshold = ItemMatcher.Static.DEFAULT_SINGLETON_PRECEDENCE_THRESHOLD;

        private double singletonPrecedenceBonus = ItemMatcher.Static.DEFAULT_SINGLETON_PRECEDENCE_BONUS;

        private double noiseFactor = ItemMatcher.Static.DEFAULT_NOISE_FACTOR;

        @Override
        public double getSimilarityThreshold() {
            return this.similarityThreshold;
        }

        @Override
        public double getSingletonPrecedenceThreshold() {
            return this.singletonPrecedenceThreshold;
        }

        @Override
        public double getSingletonPrecedenceBonus() {
            return this.singletonPrecedenceBonus;
        }

        @Override
        public double getNoiseFactor() {
            return this.noiseFactor;
        }

        @Override
        public Equalator<? super E> equalator() {
            return this.equalator;
        }

        @Override
        public Similator<? super E> similator() {
            return this.similator;
        }

        @Override
        public MatchCallback<? super E> matchCallback() {
            return this.suspiciousMatchDecider;
        }

        @Override
        public ItemMatcher<E> setSimilarityThreshold(final double similarityThreshold) {
            this.similarityThreshold = similarityThreshold;
            return this;
        }

        @Override
        public ItemMatcher<E> setSingletonPrecedenceThreshold(final double singletonPrecedenceThreshold) {
            this.singletonPrecedenceThreshold = singletonPrecedenceThreshold;
            return this;
        }

        @Override
        public ItemMatcher<E> setSingletonPrecedenceBonus(final double singletonPrecedenceBonus) {
            this.singletonPrecedenceBonus = singletonPrecedenceBonus;
            return this;
        }

        @Override
        public ItemMatcher<E> setNoisefactor(final double noiseFactor) {
            this.noiseFactor = noiseFactor;
            return this;
        }

        @Override
        public ItemMatcher<E> setSimilator(final Similator<? super E> similator) {
            this.similator = similator;
            return this;
        }

        @Override
        public ItemMatcher<E> setEqualator(final Equalator<? super E> equalator) {
            this.equalator = equalator;
            return this;
        }

        @Override
        public ItemMatcher<E> setMatchCallback(final MatchCallback<? super E> suspiciousMatchDecider) {
            this.suspiciousMatchDecider = suspiciousMatchDecider;
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public ItemMatch<E> match(final XGettingCollection<? extends E> source, final XGettingCollection<? extends E> target) {
            return new ItemMatch<E>(this, (E[]) source.toArray(), (E[]) target.toArray()).match();
        }
    }
}
