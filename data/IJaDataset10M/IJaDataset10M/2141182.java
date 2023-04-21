package backend.mapping.blast.args;

/**
 * 
 * @author hindlem
 *
 */
public final class ArgumentNames {

    public static final String E_VALUE_ARG = "EValue";

    public static final String E_VALUE_DESC = "Cutoff for the BLAST E-Value";

    public static final String TARGET_CC_ARG = "TargetConceptClass";

    public static final String TARGET_CC_DESC = "Filter by a Target Concept Class [default is all]";

    public static final String QUERY_CC_ARG = "QueryConceptClass";

    public static final String QUERY_CC_DESC = "Filter by a target Query Concept Class [default is all]";

    public static final String TARGET_SEQ_TYPE_ARG = "TargetSequenceType";

    public static final String QUERY_SEQ_TYPE_ARG = "QuerySequenceType";

    public static final String PROGRAM_DIR_ARG = "programDir";

    public static final String PROGRAM_DIR_DESC = "The directory where the sequence alignment program is located";

    public static final String PHMEMORY_ARG = "PatternHunterMemory";

    public static final String PHMEMORY_ARG_DESC = "The Memory in Mb assigned to Pattern Hunter";

    public static final String PROCESSORS_ARG = "processorsToUse";

    public static final String PROCESSORS_ARG_DESC = "The number of processors to use/may be translated to the number of processes to start";

    public static final String OVERLAP_ARG = "overlap";

    public static final String OVERLAP_DESC = "The minimum overlap to tolerate";

    public static final String SEQ_ALIGNMENT_PROG_ARG = "SequenceAlignmentProgram";

    public static final String LONGEST_ALIGNMENT_THRESHOLD_ON_TAXID_ARG = "LongestAlignmentThresholdOnTaxID";

    public static final String LONGEST_ALIGNMENT_THRESHOLD_ON_TAXID_DESC = "Indicates the number (The integer paramiter) of alignments that should be taken per species, based on the top ranking coverage/alignment length of the \"longer\" sequence in the match";

    public static final String SHORTEST_ALIGNMENT_THRESHOLD_ON_TAXID_ARG = "ShortestAlignmentThresholdOnTaxID";

    public static final String SHORTEST_ALIGNMENT_THRESHOLD_ON_TAXID_DESC = "Indicates the number (The integer paramiter) of alignments that should be taken per species, based on the top ranking coverage/alignment length of the \"shorter\" sequence in the match";
}
