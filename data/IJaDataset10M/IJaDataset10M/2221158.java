package backend.mapping.inparanoid.args;

public final class ArgumentNames {

    public static final String BLAST_OUT_FILE_ARG = "BlastOutputFile";

    public static final String E_VALUE_ARG = "EValue";

    public static final String E_VALUE_DESC = "Cutoff for the BLAST E-Value";

    public static final String SEQ_TYPES_ARG = "sequenceTypes";

    public static final String CUTOFF_ARG = "cutoff";

    public static final String OVERLAP_ARG = "overlap";

    public static final String OVERLAP_DESC = "The minimal overlap allowed";

    public static final String PROCESSORS_ARG = "processorsToUse";

    public static final String SEQ_ALIGNMENT_PROG_ARG = "SequenceAlignmentProgram";

    public static final String PROGRAM_DIR_ARG = "programDir";

    public static final String PROGRAM_DIR_DESC = "The directory where the sequence alignment program is located";

    public static final String ADD_EST_TO_CLUST_ARG = "AddESTsToINPARNOIDClusters";

    public static final String ADD_EST_TO_CLUST_ARG_DESC = "When this option is set the mapping will atempt to also include mappings for est sequences." + "\nThis option is only avaliable for the normal blastall algoritm. The mapping method will not assing inparalogs/homologs between the ESTs." + "\nPlease notice that the coverage in this case will be seen as from the shortest sequence, so that the EST are not hindered by their relatively short length";

    public static final String PHMEMORY_ARG = "PatternHunterMemory";

    public static final String PHMEMORY_ARG_DESC = "The Memory in Mb assigned to Pattern Hunter";

    public static final String MIN_SEQUENCE_LENGTH_QUERY_SEQUENCES = "MinimalQuerySequenceLength";

    public static final String MIN_SEQUENCE_LENGTH_QUERY_SEQUENCES_DESC = "The minimal length of the query seqeunces, seqeunces with a shorther length are skipped, if experiencing problems try setting this value higher.";
}
