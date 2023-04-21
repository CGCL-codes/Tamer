package com.enerjy.index.java;

import com.enerjy.index.AbstractMetricsData;
import com.enerjy.index.IMetricsFile;

/**
 * Data holder for source metrics collected for a single file.
 */
public class SourceMetricsData extends AbstractMetricsData {

    private static final long serialVersionUID = 1L;

    /** Lines count */
    public static final int LINES = 0;

    /** Number of line comments */
    public static final int LINE_COMMENT = 1;

    /** Number of block coments */
    public static final int BLOCK_COMMENT = 2;

    /** Number of doc comments */
    public static final int DOC_COMMENT = 3;

    /** Number of whitespace lines */
    public static final int WHITESPACE = 4;

    /** Number of statements */
    public static final int LOGICAL_LINES = 5;

    /** Number of blocks */
    public static final int BLOCKS = 6;

    /** Number of loops */
    public static final int LOOPS = 7;

    /** Number of function delcarations */
    public static final int FUNCTIONS = 8;

    /** Number of formal parameter declarations */
    public static final int PARAMS = 9;

    /** Number of return points from functions */
    public static final int RETURNS = 10;

    /** Maxium nesting depth */
    public static final int NEST_DEPTH = 11;

    /** Number of comparison operators */
    public static final int COMPARISONS = 12;

    /** Number of operators */
    public static final int OPERATORS = 13;

    /** Number of unique operators */
    public static final int UNIQUE_OPERATORS = 14;

    /** Number of operands */
    public static final int OPERANDS = 15;

    /** Number of unique operands */
    public static final int UNIQUE_OPERANDS = 16;

    /** Lines of Code */
    public static final int LOC = 17;

    /** Effective Lines of Code */
    public static final int ELOC = 18;

    /** Comments */
    public static final int COMMENTS = 19;

    /** Comments in Declarations */
    public static final int DECL_COMMENTS = 20;

    /** Comments in Executable ode */
    public static final int EXEC_COMMENTS = 21;

    /** Cyclomatic Complexity */
    public static final int CYCLOMATIC = 22;

    /** Procedure exits */
    public static final int EXITS = 23;

    /** Size of the file in bytes or characters */
    public static final int SIZE = 24;

    /** Comment density, derived from COMMENTS/ELOC */
    public static final int COMMENT_DENSITY = 25;

    /** Interface complexity, derived from PARAMS + RETURNS */
    public static final int INTERFACE_COMPLEXITY = 26;

    /** Halstead program length, derived from OPERATORS + OPERANDS */
    public static final int PROGRAM_LENGTH = 27;

    /** Halstead program vocabulary, derived from UNIQUE_OPERATORS + UNIQUE_OPERANDS */
    public static final int PROGRAM_VOCAB = 28;

    /** Halstead program value, derived from PROGRAM_LENGTH * log2(PROGRAM_VOCAB) */
    public static final int PROGRAM_VOLUME = 29;

    /** Halstead difficulty, derived from (UNIQUE_OPERATORS / UNIQUE_OPERANDS) * (OPERATORS / UNIQUE_OPERANDS) */
    public static final int HALSTEAD_DIFFICULTY = 30;

    /** Halstead effort, derived from HALSTEAD_DIFFICULTY * PROGRAM_VOLUME */
    public static final int HALSTEAD_EFFORT = 31;

    /** CRC */
    public static final int CRC = 32;

    /** Size of the metrics array */
    static final int MAX_METRICS = 33;

    private final double[] metrics = new double[JavaMetricsType.INSTANCE.getNameCount()];

    SourceMetricsData(IMetricsFile file) {
        super(JavaMetricsType.INSTANCE, file);
    }

    SourceMetricsData(IMetricsFile file, double[] metrics) {
        super(JavaMetricsType.INSTANCE, file);
        System.arraycopy(metrics, 0, this.metrics, 0, Math.min(metrics.length, this.metrics.length));
    }

    public double[] getRawData() {
        return metrics.clone();
    }

    @Override
    protected boolean isInstanceOf(Object obj) {
        return (obj instanceof SourceMetricsData);
    }
}
