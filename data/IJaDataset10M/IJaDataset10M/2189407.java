package ec.vector;

import ec.*;
import ec.util.*;

/**
 * FloatVectorSpecies is a subclass of VectorSpecies with special
 * constraints for floating-point vectors, namely FloatVectorIndividual and
 * DoubleVectorIndividual.
 *
 * <p>FloatVectorSpecies can specify min/max numeric constraints on gene values
 * in three different ways.
 *
 * <ol>
 * <li> You may provide a default min and max value.
 *      This is done by specifying:
 *      <p><i>base</i>.<tt>min-gene</tt>
 *      <br><i>base</i>.<tt>max-gene</tt>
 *      <p><i>Note:</i> you <b>must</b> provide these values even if you don't use them,
 *      as they're used as defaults by #2 and #3 below.
 *<p>
 * <li> You may provide min and max values for genes in segments (regions) along
 *      the genome.  This is done by specifying:
 *      <p><i>base</i>.<tt>num-segments</tt>
 *      The segments may be defined by either start or end indices of genes. 
 *      This is controlled by specifying the value of:
 *      <p><i>base</i>.<tt>segment-type</tt>
 *      which can assume the value of start or end, with start being the default.
 *      The indices are defined using Java array style, i.e. the first gene has the index of 0, 
 *      and the last gene has the index of genome-size - 1.
 *      <p>Using this method, each segment is specified by<i>j</i>...
 *      <p><i>base</i>.<tt>segment.</tt><i>j</i><tt>.start</tt>
 *      <br><i>base</i>.<tt>segment.</tt><i>j</i><tt>.min-gene</tt>
 *      <br><i>base</i>.<tt>segment.</tt><i>j</i><tt>.max-gene</tt>
 *      if segment-type value was chosen as start or by:
 *      <p><i>base</i>.<tt>segment.</tt><i>j</i><tt>.end</tt>
 *      <br><i>base</i>.<tt>segment.</tt><i>j</i><tt>.min-gene</tt>
 *      <br><i>base</i>.<tt>segment.</tt><i>j</i><tt>.max-gene</tt>
 *      if segment-type value is equal to end.
 *<p>
 * <li> You may provide min and max values for each separate gene.  
 *      This is done by specifying (for each gene location <i>i</i> you wish to specify)
 *      <p><i>base</i>.<tt>min-gene</tt>.<i>i</i>
 *      <br><i>basn</i>.<tt>max-gene</tt>.<i>i</i>
 * </ol>
 * 
 * <p>Any settings for #3 override #2, and both override #1. 
 *
 * <p>
 * FloatVectorSpecies provides support for three ways of mutating a gene:
 * <ul>
 * <li>replacing the gene's value with a value uniformly-drawn from the gene's
 * range (the default behavior, legacy from the previous versions).</li>
 * <li>perturbing the gene's value with gaussian noise; if the gene-by-gene range 
 * is used, than the standard deviation is scaled to reflect each gene's range. 
 * If the gaussian mutation's standard deviation is too large for the range,
 * than there's a large probability the mutated value will land outside range.
 * We will try again a number of times (100) before giving up and using the 
 * previous mutation method.</li>
 * <li>perturbing the gene's value with noise chosen from a <i>polynomial distribution</i>,
 * similar to the gaussian distribution.  The polynomial distribution was popularized
 * by Kalyanmoy Deb and is found in many of his publications (see http://www.iitk.ac.in/kangal/deb.shtml).
 * The polynomial distribution has two options.  First, there is the <i>index</i>.  This
 * variable defines the shape of the distribution and is in some sense the equivalent of the
 * standard deviation in the gaussian distribution.  The index is an integer.  If it is zero,
 * the polynomial distribution is simply the uniform distribution from [1,-1].  If it is 1, the
 * polynomial distribution is basically a triangular distribution from [1,-1] peaking at 0.  If
 * it is 2, the polynomial distribution follows a squared function, again peaking at 0.  Larger
 * values result in even more peaking and narrowness.  The default values used in nearly all of
 * the NSGA-II and Deb work is 20.  Second, there is whether or not the value is intended for
 * <i>bounded</i> genes.  The default polynomial distribution is used when we assume the gene can
 * take on literally any value, even beyond the min and max values.  For genes which are restricted
 * to be between min and max, there is an alternative version of the polynomial distribution, used by
 * Deb's team but not discussed much in the literature, desiged for that situation.  We assume boundedness
 * by default, and have found it to be somewhat better for NSGA-II and SPEA2 problems.  For a description
 * of this alternative version, see "A Niched-Penalty Approach for Constraint Handling in Genetic Algorithms"
 * by Kalyanmoy Deb and Samir Agrawal.  Deb's default implementation bounds the result to min or max;
 * instead ECJ's implementation of the polynomial distribution retries until it finds a legal value.  This
 * will be just fine for ranges like [0,1], but for smaller ranges you may be waiting a long time.
 * </ul>
 * 
 * 
 * <p>
 * <b>Parameters</b><br>
 * <table>
 * <tr>
 * <td valign=top><i>base</i>.<tt>min-gene</tt><br>
 * <font size=-1>double (default=0.0)</font></td>
 * <td valign=top>(the minimum gene value)</td>
 * </tr>
 * 
 * <tr>
 * <td valign=top><i>base</i>.<tt>max-gene</tt><br>
 * <font size=-1>double &gt;= <i>base</i>.min-gene</font></td>
 * <td valign=top>(the maximum gene value)</td>
 * </tr>
 * 
 * <tr>
 * <td valign=top><i>base</i>.<tt>min-gene</tt>.<i>i</i><br>
 * <font size=-1>double (default=<i>base</i>.<tt>min-gene</tt>)</font></td>
 * <td valign=top>(the minimum gene value for gene <i>i</i>)</td>
 * </tr>
 * 
 * <tr>
 * <td valign=top><i>base</i>.<tt>max-gene</tt>.<i>i</i><br>
 * <font size=-1>double &gt;= <i>base</i>.min-gene.<i>i</i> (default=<i>base</i>.<tt>max-gene</tt>)</font></td>
 * <td valign=top>(the maximum gene value for gene <i>i</i>)</td>
 * </tr>
 * 
 * <tr><td valign=top><i>base.</i>.<tt>num-segments</tt><br>
 * <font size=-1>int &gt;= 1 (default=no segments used)</font></td>
 * <td valign=top>(the number of gene segments defined)</td>
 * </tr>
 * 
 * <tr><td valign=top><i>base.</i>.<tt>segment-type</tt><br>
 * <font size=-1>start (default) or end</font></td>
 * <td valign=top>(defines the way in which segments are defined: either by providing start indices (segment-type=start) or by providing end indices (segment-type=end)</td>
 * </tr>
 *
 * <tr><td valign=top><i>base.</i>.<tt>segment</tt>.<i>j</i>.<tt>start</tt><br>
 * <font size=-1>0 &lt;= int &lt; genome length</font></td>
 * <td valign=top>(the start index of gene segment <i>j</i> -- the end of a segment is before the start of the next segment)</td>
 * <td valign=top>(used when the value of segment-type parameter is equal to start)</td>
 * </tr>
 *
 * <tr><td valign=top><i>base.</i>.<tt>segment</tt>.<i>j</i>.<tt>end</tt><br>
 * <font size=-1>0 &lt;= int &lt; genome length</font></td>
 * <td valign=top>(the end of gene segment <i>j</i> -- the start of a segment is after the end of the previous segment)</td>
 * <td valign=top>(used when the value of segment-type parameter is equal to end)</td>
 * </tr>
 *
 * <tr><td valign=top><i>base.</i>.<tt>segment</tt>.<i>j</i>.<tt>min-gene</tt><br>
 * <font size=-1>double (default=0.0)</font></td>
 * <td valign=top>(the minimum gene value for segment <i>j</i>)</td>
 * </tr>
 *
 * <tr><td valign=top><i>base.</i>.<tt>segment</tt>.<i>j</i>.<tt>max-gene</tt><br>
 * <font size=-1>double &gt;= <i>base.</i>.<tt>segment</tt>.<i>j</i>.<tt>min-gene</tt></td>
 * <td valign=top>(the maximum gene value for segment <i>j</i>)</td>
 * </tr>
 * 
 * <tr>
 * <td valign=top><i>base</i>.<tt>mutation-type</tt><br>
 * <font size=-1><tt>reset</tt>, <tt>gauss</tt>, or <tt>polynomial</tt> (default=<tt>reset</tt>)</font></td>
 * <td valign=top>(the mutation type)</td>
 * </tr>
 * 
 * <tr>
 * <td valign=top><i>base</i>.<tt>mutation-stdev</tt><br>
 * <font size=-1>double &ge; 0</font></td>
 * <td valign=top>(the standard deviation or the gauss perturbation)</td>
 * </tr>
 * 
 * <tr>
 * <td valign=top><i>base</i>.<tt>out-of-bounds-retries</tt><br>
 *  <font size=-1>int &ge; 0 (default=100)</font></td>
 *  <td valign=top>(number of times the gaussian mutation got the gene out of range 
 *  before we give up and reset the gene's value; 0 means "never give up")</td>
 * </tr>
 *
 * <tr>
 * <td valign=top><i>base</i>.<tt>distribution-index</tt><br>
 * <font size=-1>int &ge; 0</font></td>
 * <td valign=top>(the mutation distribution index for the polynomial mutation distribution)</td>
 * </tr>
 * 
 * <tr>
 * <td valign=top><i>base</i>.<tt>alternative-polynomial-version</tt><br>
 *  <font size=-1>boolean (default=true)</font></td>
 *  <td valign=top>(whether to use the "bounded" variation of the polynomial mutation or the standard ("unbounded") version)</td>
 * </tr>
 * 
 * <tr>
 * <td valign=top><i>base</i>.<tt>mutation-bounded</tt><br>
 *  <font size=-1>boolean (default=true)</font></td>
 *  <td valign=top>(whether mutation is restricted to only being within the min/max gene values.  Does not apply to SimulatedBinaryCrossover (which is always bounded))</td>
 * </tr>
 * 
 * </table>
 * @author Sean Luke, Gabriel Balan, Rafal Kicinger
 * @version 2.0
 */
public class FloatVectorSpecies extends VectorSpecies {

    public static final String P_MINGENE = "min-gene";

    public static final String P_MAXGENE = "max-gene";

    public static final String P_MUTATIONTYPE = "mutation-type";

    public static final String P_STDEV = "mutation-stdev";

    public static final String P_MUTATION_DISTRIBUTION_INDEX = "mutation-distribution-index";

    public static final String P_POLYNOMIAL_ALTERNATIVE = "alternative-polynomial-version";

    public static final String V_RESET_MUTATION = "reset";

    public static final String V_GAUSS_MUTATION = "gauss";

    public static final String V_POLYNOMIAL_MUTATION = "polynomial";

    public static final String P_OUTOFBOUNDS_RETRIES = "out-of-bounds-retries";

    public static final String P_NUM_SEGMENTS = "num-segments";

    public static final String P_SEGMENT_TYPE = "segment-type";

    public static final String P_SEGMENT_START = "start";

    public static final String P_SEGMENT_END = "end";

    public static final String P_SEGMENT = "segment";

    public static final String P_MUTATION_BOUNDED = "mutation-bounded";

    public static final int C_RESET_MUTATION = 0;

    public static final int C_GAUSS_MUTATION = 1;

    public static final int C_POLYNOMIAL_MUTATION = 2;

    public double[] minGenes;

    public double[] maxGenes;

    /** What kind of mutation do we have? */
    public int mutationType;

    public double gaussMutationStdev;

    public boolean mutationIsBounded;

    public int outOfBoundsRetries = 100;

    public int mutationDistributionIndex;

    public boolean polynomialIsAlternative;

    static final double SIMULATED_BINARY_CROSSOVER_EPS = 1.0e-14;

    private boolean outOfBoundsRetriesWarningPrinted = false;

    public void outOfRangeRetryLimitReached(EvolutionState state) {
        if (!outOfBoundsRetriesWarningPrinted) {
            outOfBoundsRetriesWarningPrinted = true;
            state.output.warning("The limit of 'out-of-range' retries for gaussian mutation was reached.");
        }
    }

    public double maxGene(int gene) {
        double[] m = maxGenes;
        if (m.length <= gene) {
            if (!dynamicInitialSize && !warned) warnAboutGene(gene);
            gene = m.length - 1;
        }
        return m[gene];
    }

    public double minGene(int gene) {
        double[] m = minGenes;
        if (m.length <= gene) {
            if (!dynamicInitialSize && !warned) warnAboutGene(gene);
            gene = m.length - 1;
        }
        return m[gene];
    }

    public boolean inNumericalTypeRange(double geneVal) {
        if (i_prototype instanceof FloatVectorIndividual) return (geneVal <= Float.MAX_VALUE && geneVal >= -Float.MAX_VALUE); else if (i_prototype instanceof DoubleVectorIndividual) return true; else return false;
    }

    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);
        Parameter def = defaultBase();
        minGenes = new double[genomeSize];
        maxGenes = new double[genomeSize];
        double minGene = state.parameters.getDoubleWithDefault(base.push(P_MINGENE), def.push(P_MINGENE), 0);
        double maxGene = state.parameters.getDouble(base.push(P_MAXGENE), def.push(P_MAXGENE), minGene);
        if (maxGene < minGene) state.output.fatal("FloatVectorSpecies must have a default min-gene which is <= the default max-gene", base.push(P_MAXGENE), def.push(P_MAXGENE));
        for (int x = 0; x < genomeSize; x++) {
            minGenes[x] = minGene;
            maxGenes[x] = maxGene;
        }
        int numSegments = 0;
        if (state.parameters.exists(base.push(P_NUM_SEGMENTS), def.push(P_NUM_SEGMENTS))) {
            if (dynamicInitialSize) state.output.warnOnce("Using dynamic initial sizing, but per-segment min/max gene declarations.  This is probably wrong.  You probably want to use global min/max declarations.", base.push(P_NUM_SEGMENTS), def.push(P_NUM_SEGMENTS));
            numSegments = state.parameters.getIntWithDefault(base.push(P_NUM_SEGMENTS), def.push(P_NUM_SEGMENTS), 0);
            if (numSegments == 0) state.output.warning("The number of genome segments has been defined to be equal to 0.\n" + "Hence, no genome segments will be defined.", base.push(P_NUM_SEGMENTS), def.push(P_NUM_SEGMENTS)); else if (numSegments < 0) state.output.fatal("Invalid number of genome segments: " + numSegments + "\nIt must be a nonnegative value.", base.push(P_NUM_SEGMENTS), def.push(P_NUM_SEGMENTS));
            String segmentType = state.parameters.getStringWithDefault(base.push(P_SEGMENT_TYPE), def.push(P_SEGMENT_TYPE), P_SEGMENT_START);
            if (segmentType.equalsIgnoreCase(P_SEGMENT_START)) initializeGenomeSegmentsByStartIndices(state, base, def, numSegments, minGene, maxGene); else if (segmentType.equalsIgnoreCase(P_SEGMENT_END)) initializeGenomeSegmentsByEndIndices(state, base, def, numSegments, minGene, maxGene); else state.output.fatal("Invalid specification of genome segment type: " + segmentType + "\nThe " + P_SEGMENT_TYPE + " parameter must have the value of " + P_SEGMENT_START + " or " + P_SEGMENT_END, base.push(P_SEGMENT_TYPE), def.push(P_SEGMENT_TYPE));
        }
        boolean foundStuff = false;
        boolean warnedMin = false;
        boolean warnedMax = false;
        for (int x = 0; x < genomeSize; x++) {
            if (!state.parameters.exists(base.push(P_MINGENE).push("" + x), def.push(P_MINGENE).push("" + x))) {
                if (foundStuff && !warnedMin) {
                    state.output.warning("FloatVectorSpecies has missing min-gene values for some genes.\n" + "The first one is gene #" + x + ".", base.push(P_MINGENE).push("" + x), def.push(P_MINGENE).push("" + x));
                    warnedMin = true;
                }
            } else {
                if (dynamicInitialSize) state.output.warnOnce("Using dynamic initial sizing, but per-gene min/max gene declarations.  This is probably wrong.  You probably want to use global min/max declarations.", base.push(P_MINGENE).push("" + x), base.push(P_MINGENE).push("" + x));
                minGenes[x] = state.parameters.getDoubleWithDefault(base.push(P_MINGENE).push("" + x), def.push(P_MINGENE).push("" + x), minGene);
                foundStuff = true;
            }
            if (!state.parameters.exists(base.push(P_MAXGENE).push("" + x), def.push(P_MAXGENE).push("" + x))) {
                if (foundStuff && !warnedMax) {
                    state.output.warning("FloatVectorSpecies has missing max-gene values for some genes.\n" + "The first one is gene #" + x + ".", base.push(P_MAXGENE).push("" + x), def.push(P_MAXGENE).push("" + x));
                    warnedMax = true;
                }
            } else {
                if (dynamicInitialSize) state.output.warnOnce("Using dynamic initial sizing, but per-gene min/max gene declarations.  This is probably wrong.  You probably want to use global min/max declarations.", base.push(P_MINGENE).push("" + x), base.push(P_MINGENE).push("" + x));
                maxGenes[x] = state.parameters.getDoubleWithDefault(base.push(P_MAXGENE).push("" + x), def.push(P_MAXGENE).push("" + x), maxGene);
                foundStuff = true;
            }
        }
        for (int x = 0; x < genomeSize; x++) {
            if (maxGenes[x] != maxGenes[x]) state.output.fatal("FloatVectorSpecies found that max-gene[" + x + "] is NaN");
            if (minGenes[x] != minGenes[x]) state.output.fatal("FloatVectorSpecies found that min-gene[" + x + "] is NaN");
            if (maxGenes[x] < minGenes[x]) state.output.fatal("FloatVectorSpecies must have a min-gene[" + x + "] which is <= the max-gene[" + x + "]");
            if (!inNumericalTypeRange(minGenes[x])) state.output.fatal("This FloatvectorSpecies has a prototype of the kind: " + i_prototype.getClass().getName() + ", but doesn't have a min-gene[" + x + "] value within the range of this prototype's genome's data types");
            if (!inNumericalTypeRange(maxGenes[x])) state.output.fatal("This FloatvectorSpecies has a prototype of the kind: " + i_prototype.getClass().getName() + ", but doesn't have a max-gene[" + x + "] value within the range of this prototype's genome's data types");
        }
        mutationIsBounded = state.parameters.getBoolean(base.push(P_MUTATION_BOUNDED), def.push(P_MUTATION_BOUNDED), true);
        String mtype = state.parameters.getStringWithDefault(base.push(P_MUTATIONTYPE), def.push(P_MUTATIONTYPE), null);
        mutationType = C_RESET_MUTATION;
        if (mtype == null) state.output.warning("No mutation type given for FloatVectorSpecies, assuming 'reset' mutation", base.push(P_MUTATIONTYPE), def.push(P_MUTATIONTYPE)); else if (mtype.equalsIgnoreCase(V_RESET_MUTATION)) mutationType = C_RESET_MUTATION; else if (mtype.equalsIgnoreCase(V_POLYNOMIAL_MUTATION)) mutationType = C_POLYNOMIAL_MUTATION; else if (mtype.equalsIgnoreCase(V_GAUSS_MUTATION)) mutationType = C_GAUSS_MUTATION; else state.output.fatal("FloatVectorSpecies given a bad mutation type: " + mtype, base.push(P_MUTATIONTYPE), def.push(P_MUTATIONTYPE));
        if (mutationType == C_POLYNOMIAL_MUTATION) {
            mutationDistributionIndex = state.parameters.getInt(base.push(P_MUTATION_DISTRIBUTION_INDEX), def.push(P_MUTATION_DISTRIBUTION_INDEX), 0);
            if (mutationDistributionIndex < 0) state.output.fatal("If FloatVectorSpecies is going to use polynomial mutation, the distribution index must be defined and >= 0.", base.push(P_MUTATION_DISTRIBUTION_INDEX), def.push(P_MUTATION_DISTRIBUTION_INDEX));
            polynomialIsAlternative = state.parameters.getBoolean(base.push(P_POLYNOMIAL_ALTERNATIVE), def.push(P_POLYNOMIAL_ALTERNATIVE), true);
            outOfBoundsRetries = state.parameters.getIntWithDefault(base.push(P_OUTOFBOUNDS_RETRIES), def.push(P_OUTOFBOUNDS_RETRIES), outOfBoundsRetries);
            if (outOfBoundsRetries < 0) {
                state.output.fatal("If it's going to use polynomial mutation, FloatvectorSpecies must have a positive number of out-of-bounds retries or 0 (for don't give up).  " + "This is even the case if doing so-called \"bounded\" polynomial mutation, which auto-bounds anyway, or if the mutation is unbounded.  " + "In either case, just provide an arbitrary value, which will be ignored.", base.push(P_OUTOFBOUNDS_RETRIES), def.push(P_OUTOFBOUNDS_RETRIES));
            }
        }
        if (mutationType == C_GAUSS_MUTATION) {
            gaussMutationStdev = state.parameters.getDouble(base.push(P_STDEV), def.push(P_STDEV), 0);
            if (gaussMutationStdev <= 0) state.output.fatal("If it's going to use gaussian mutation, FloatvectorSpecies must have a strictly positive standard deviation", base.push(P_STDEV), def.push(P_STDEV));
            outOfBoundsRetries = state.parameters.getIntWithDefault(base.push(P_OUTOFBOUNDS_RETRIES), def.push(P_OUTOFBOUNDS_RETRIES), outOfBoundsRetries);
            if (outOfBoundsRetries < 0) {
                state.output.fatal("If it's going to use gaussian mutation, FloatvectorSpecies must have a positive number of out-of-bounds retries or 0 (for don't give up).  " + "This is even the case if the mutation is unbounded.  In that case, just provide an arbitrary value, which will be ignored.", base.push(P_OUTOFBOUNDS_RETRIES), def.push(P_OUTOFBOUNDS_RETRIES));
            }
        }
    }

    private void initializeGenomeSegmentsByStartIndices(final EvolutionState state, final Parameter base, final Parameter def, int numSegments, double minGene, double maxGene) {
        boolean warnedMin = false;
        boolean warnedMax = false;
        double currentSegmentMinGeneValue = Double.MAX_VALUE;
        double currentSegmentMaxGeneValue = Double.MIN_VALUE;
        int previousSegmentEnd = genomeSize;
        int currentSegmentEnd = 0;
        for (int i = numSegments - 1; i >= 0; i--) {
            if (state.parameters.exists(base.push(P_SEGMENT).push("" + i).push(P_SEGMENT_START), def.push(P_SEGMENT).push("" + i).push(P_SEGMENT_START))) {
                currentSegmentEnd = state.parameters.getInt(base.push(P_SEGMENT).push("" + i).push(P_SEGMENT_START), def.push(P_SEGMENT).push("" + i).push(P_SEGMENT_START));
            } else {
                state.output.fatal("Genome segment " + i + " has not been defined!" + "\nYou must specify start indices for " + numSegments + " segment(s)", base.push(P_SEGMENT).push("" + i).push(P_SEGMENT_START), base.push(P_SEGMENT).push("" + i).push(P_SEGMENT_START));
            }
            if (currentSegmentEnd >= previousSegmentEnd || currentSegmentEnd < 0) state.output.fatal("Invalid start index value for segment " + i + ": " + currentSegmentEnd + "\nThe value must be smaller than " + previousSegmentEnd + " and greater than or equal to  " + 0);
            if (i == 0 && currentSegmentEnd != 0) state.output.fatal("Invalid start index value for the first segment " + i + ": " + currentSegmentEnd + "\nThe value must be equal to " + 0);
            if (!state.parameters.exists(base.push(P_SEGMENT).push("" + i).push(P_MINGENE), base.push(P_SEGMENT).push("" + i).push(P_MINGENE))) {
                if (!warnedMin) {
                    state.output.warning("IntegerVectorSpecies has missing min-gene values for some segments.\n" + "The first segment is #" + i + ".", base.push(P_SEGMENT).push("" + i), base.push(P_SEGMENT).push("" + i));
                    warnedMin = true;
                }
                currentSegmentMinGeneValue = minGene;
            } else {
                currentSegmentMinGeneValue = state.parameters.getDoubleWithDefault(base.push(P_SEGMENT).push("" + i).push(P_MINGENE), base.push(P_SEGMENT).push("" + i).push(P_MINGENE), minGene);
                if (!inNumericalTypeRange(currentSegmentMinGeneValue)) state.output.error("This IntegerVectorSpecies has a prototype of the kind: " + i_prototype.getClass().getName() + ", but doesn't have a min-gene " + " value for segment " + i + " within the range of this prototype's genome's data types", base.push(P_SEGMENT).push("" + i).push(P_MINGENE), base.push(P_SEGMENT).push("" + i).push(P_MINGENE));
            }
            if (!state.parameters.exists(base.push(P_SEGMENT).push("" + i).push(P_MAXGENE), base.push(P_SEGMENT).push("" + i).push(P_MAXGENE))) {
                if (!warnedMax) {
                    state.output.warning("IntegerVectorSpecies has missing max-gene values for some segments.\n" + "The first segment is #" + i + ".", base.push(P_SEGMENT).push("" + i), base.push(P_SEGMENT).push("" + i));
                    warnedMax = true;
                }
                currentSegmentMaxGeneValue = maxGene;
            } else {
                currentSegmentMaxGeneValue = state.parameters.getDoubleWithDefault(base.push(P_SEGMENT).push("" + i).push(P_MAXGENE), base.push(P_SEGMENT).push("" + i).push(P_MAXGENE), maxGene);
                if (!inNumericalTypeRange(currentSegmentMaxGeneValue)) state.output.fatal("This IntegerVectorSpecies has a prototype of the kind: " + i_prototype.getClass().getName() + ", but doesn't have a max-gene " + " value for segment " + i + " within the range of this prototype's genome's data types", base.push(P_SEGMENT).push("" + i).push(P_MAXGENE), base.push(P_SEGMENT).push("" + i).push(P_MAXGENE));
            }
            if (currentSegmentMaxGeneValue < currentSegmentMinGeneValue) state.output.fatal("IntegerVectorSpecies must have a min-gene value for segment " + i + " which is <= the max-gene value", base.push(P_SEGMENT).push("" + i).push(P_MAXGENE), base.push(P_SEGMENT).push("" + i).push(P_MAXGENE));
            for (int j = previousSegmentEnd - 1; j >= currentSegmentEnd; j--) {
                minGenes[j] = currentSegmentMinGeneValue;
                maxGenes[j] = currentSegmentMaxGeneValue;
            }
            previousSegmentEnd = currentSegmentEnd;
        }
    }

    private void initializeGenomeSegmentsByEndIndices(final EvolutionState state, final Parameter base, final Parameter def, int numSegments, double minGene, double maxGene) {
        boolean warnedMin = false;
        boolean warnedMax = false;
        double currentSegmentMinGeneValue = Double.MAX_VALUE;
        double currentSegmentMaxGeneValue = Double.MIN_VALUE;
        int previousSegmentEnd = -1;
        int currentSegmentEnd = 0;
        for (int i = 0; i < numSegments; i++) {
            if (state.parameters.exists(base.push(P_SEGMENT).push("" + i).push(P_SEGMENT_END), def.push(P_SEGMENT).push("" + i).push(P_SEGMENT_END))) {
                currentSegmentEnd = state.parameters.getInt(base.push(P_SEGMENT).push("" + i).push(P_SEGMENT_END), def.push(P_SEGMENT).push("" + i).push(P_SEGMENT_END));
            } else {
                state.output.fatal("Genome segment " + i + " has not been defined!" + "\nYou must specify end indices for " + numSegments + " segment(s)", base.push(P_SEGMENT).push("" + i).push(P_SEGMENT_END), base.push(P_SEGMENT).push("" + i).push(P_SEGMENT_END));
            }
            if (currentSegmentEnd <= previousSegmentEnd || currentSegmentEnd >= genomeSize) state.output.fatal("Invalid end index value for segment " + i + ": " + currentSegmentEnd + "\nThe value must be greater than " + previousSegmentEnd + " and smaller than " + genomeSize);
            if (i == numSegments - 1 && currentSegmentEnd != (genomeSize - 1)) state.output.fatal("Invalid end index value for the last segment " + i + ": " + currentSegmentEnd + "\nThe value must be equal to the index of the last gene in the genome:  " + (genomeSize - 1));
            if (!state.parameters.exists(base.push(P_SEGMENT).push("" + i).push(P_MINGENE), base.push(P_SEGMENT).push("" + i).push(P_MINGENE))) {
                if (!warnedMin) {
                    state.output.warning("IntegerVectorSpecies has missing min-gene values for some segments.\n" + "The first segment is #" + i + ".", base.push(P_SEGMENT).push("" + i), base.push(P_SEGMENT).push("" + i));
                    warnedMin = true;
                }
                currentSegmentMinGeneValue = minGene;
            } else {
                currentSegmentMinGeneValue = state.parameters.getDoubleWithDefault(base.push(P_SEGMENT).push("" + i).push(P_MINGENE), base.push(P_SEGMENT).push("" + i).push(P_MINGENE), minGene);
                if (!inNumericalTypeRange(currentSegmentMinGeneValue)) state.output.error("This IntegerVectorSpecies has a prototype of the kind: " + i_prototype.getClass().getName() + ", but doesn't have a min-gene " + " value for segment " + i + " within the range of this prototype's genome's data types", base.push(P_SEGMENT).push("" + i).push(P_MINGENE), base.push(P_SEGMENT).push("" + i).push(P_MINGENE));
            }
            if (!state.parameters.exists(base.push(P_SEGMENT).push("" + i).push(P_MAXGENE), base.push(P_SEGMENT).push("" + i).push(P_MAXGENE))) {
                if (!warnedMax) {
                    state.output.warning("IntegerVectorSpecies has missing max-gene values for some segments.\n" + "The first segment is #" + i + ".", base.push(P_SEGMENT).push("" + i), base.push(P_SEGMENT).push("" + i));
                    warnedMax = true;
                }
                currentSegmentMaxGeneValue = maxGene;
            } else {
                currentSegmentMaxGeneValue = state.parameters.getDoubleWithDefault(base.push(P_SEGMENT).push("" + i).push(P_MAXGENE), base.push(P_SEGMENT).push("" + i).push(P_MAXGENE), maxGene);
                if (!inNumericalTypeRange(currentSegmentMaxGeneValue)) state.output.fatal("This IntegerVectorSpecies has a prototype of the kind: " + i_prototype.getClass().getName() + ", but doesn't have a max-gene " + " value for segment " + i + " within the range of this prototype's genome's data types", base.push(P_SEGMENT).push("" + i).push(P_MAXGENE), base.push(P_SEGMENT).push("" + i).push(P_MAXGENE));
            }
            if (currentSegmentMaxGeneValue < currentSegmentMinGeneValue) state.output.fatal("IntegerVectorSpecies must have a min-gene value for segment " + i + " which is <= the max-gene value", base.push(P_SEGMENT).push("" + i).push(P_MAXGENE), base.push(P_SEGMENT).push("" + i).push(P_MAXGENE));
            for (int j = previousSegmentEnd + 1; j <= currentSegmentEnd; j++) {
                minGenes[j] = currentSegmentMinGeneValue;
                maxGenes[j] = currentSegmentMaxGeneValue;
            }
            previousSegmentEnd = currentSegmentEnd;
        }
    }
}
