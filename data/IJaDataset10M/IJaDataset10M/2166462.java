package gate.chineseSeg;

public class ConstantParameters {

    static final String SEPARATTORLN = ";;";

    static final char SEPARATTOR_BLANK = ' ';

    static final char SEPARATTOR_BLANK_wide = '　';

    static final char REPLACEMENT_BLANK = 'K';

    static final char REPLACEMENT_Letter = 'L';

    static final char REPLACEMENT_Digit = 'N';

    static final char BEGIN_Char = 'B';

    static final char END_Char = 'E';

    static final char NEWLINE_Char = 'R';

    static final String LABEL_L = "1";

    static final String LABEL_M = "2";

    static final String LABEL_R = "3";

    static final String LABEL_S = "4";

    public static final String FILETYPEOFSAVEDFILE = ".save";

    static final String FILENAME_TERMS = "terms.txt";

    static final String FILENAME_resultsDir = "segmented";

    static final String FILENAMEOFLabelList = "labels.txt";

    /** Name of the file storing the feature vectors in sparse format. */
    public static final String FILENAMEOFFeatureVectorData = "featureVectorsData" + FILETYPEOFSAVEDFILE;

    ;

    /** Name of the tempory file storing the feature vectors in sparse format. */
    public static final String TempFILENAMEofFVData = "featureVectorsDataTemp" + FILETYPEOFSAVEDFILE;

    ;

    /** Name of the file storing the learned models */
    public static final String FILENAMEOFModels = "learnedModels" + FILETYPEOFSAVEDFILE;

    /** Name of log file. */
    public static final String FILENAMEOFLOGFILE = "logFileForChineseSeg" + FILETYPEOFSAVEDFILE;

    static final String NONFEATURE = "_NA_";

    /** Maximal number of the unique NLP features. */
    public static final int MAXIMUMFEATURES = 6000000;
}
