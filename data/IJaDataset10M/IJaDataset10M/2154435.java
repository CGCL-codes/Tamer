package imp;

/**
 * This is a single file with important directory names.
 * @author keller
 */
public class Directories {

    /**
 * name of where Impro-Visor files will be stored in the user's directory.
 */
    public static String improHome = "impro-visor-version-" + ImproVisor.version + "-files";

    /**
 * Standard sub-directory for vocabulary
 */
    public static String vocabDirName = "vocab";

    /**
 * Standard sub-directory for dictionaries
 */
    public static String dictionaryDirName = "vocab";

    /**
 * Standard sub-directory for grammars
 */
    public static String grammarDirName = "grammars";

    /**
 * Standard sub-directory for soloist files
 */
    public static String soloistDirName = "grammars";

    /**
 * Name of standard sub-directory for Solo profile
 * optionally used in lick generation.
 */
    public static String profileDirName = "grammars";

    /**
 * Standard file name for accumulated productions used in grammar learning
 */
    public static String accumulatedProductions = "accumulatedProductions.cache";

    /**
 * Name of standard sub-directory for styles
 */
    public static String styleDirName = "styles";

    /**
 * Name of standard sub-directory for importing styles 
 * from combination of midi and leadsheet
 */
    public static String styleExtractDirName = "styleExtract";

    /**
 * Name of standard sub-directory for leadsheets 
 */
    public static String leadsheetDirName = "leadsheets";

    /**
 * Name of standard sub-directory for MIDI files 
 */
    public static String midiDirName = "midi";

    /**
 * Name of the error log file.
 */
    static String errorLogFilename = "Impro-Visor-ErrorLog.txt";
}
