package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.DimensionallyInterpretableSemanticSpace;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.common.WordComparator;
import edu.ucla.sspace.text.WordIterator;
import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorIO;
import edu.ucla.sspace.util.SortedMultiMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A utility class that operates as a command-line tool for interacting with
 * semantic space files.  The utility also provides script execution
 * capabilities for its commands.  This allows users to develop custom methods
 * of interacting with one or more semantic spaces.  In additoin, scripting can
 * help automate certain forms of tests on the expected contents of a semantic
 * space.
 *
 * @author David Jurgens
 */
public class SemanticSpaceExplorer {

    /**
     * A set of commands that can be issued to the semantic space explorer.
     */
    private enum Command {

        LOAD, UNLOAD, GET_NEIGHBORS, GET_SIMILARITY, COMPARE_SSPACE_VECTORS, HELP, WRITE_COMMAND_RESULTS, SET_CURRENT_SSPACE, GET_CURRENT_SSPACE, PRINT_VECTOR, ALIAS, GET_WORDS, DESCRIBE_DIMENSION, DESCRIBE_SEMANTIC_SPACE
    }

    /**
     * A mapping from the abbreviation for a command to its {@link Command}
     * instance.
     */
    private static final Map<String, Command> abbreviatedCommands = new HashMap<String, Command>();

    static {
        for (Command c : Command.values()) {
            String[] commandWords = c.toString().split("_");
            StringBuilder abbv = new StringBuilder();
            for (String w : commandWords) abbv.append(w.charAt(0));
            abbreviatedCommands.put(abbv.toString().toLowerCase(), c);
        }
    }

    /**
     * The comparator to be used when identifying the nearest neighbors to words
     * in a semantic space.
     */
    private final WordComparator wordComparator;

    /**
     * The mapping from file name to the {@code SemanticSpace} that was loaded
     * from that file.
     */
    private final Map<String, SemanticSpace> fileNameToSSpace;

    /**
     * The mapping from the alias of a semantic space to the file name from
     * which it was loaded.
     */
    private final Map<String, String> aliasToFileName;

    /**
     * The current {@code SemanticSpace} to be used when invoking commands
     */
    private SemanticSpace current;

    /**
     * Constructs an instance of {@code SemanticSpaceExplorer}.
     */
    private SemanticSpaceExplorer() {
        this.wordComparator = new WordComparator();
        fileNameToSSpace = new LinkedHashMap<String, SemanticSpace>();
        aliasToFileName = new HashMap<String, String>();
        current = null;
    }

    /**
     * Returns the name of the file form which the current {@code SemanticSpace}
     * was loaded, or {@code null} if no semantic space is currently open.
     *
     * @return the name of the file from which the current space was loaded
     */
    private String getCurrentSSpaceFileName() {
        for (Map.Entry<String, SemanticSpace> e : fileNameToSSpace.entrySet()) {
            if (e.getValue() == current) {
                return e.getKey();
            }
        }
        return null;
    }

    /**
     * Returns the {@code SemanticSpace} linked to the name, either as an alias
     * or as a file name.
     *
     * @param name the alias or file name of a loaded semantic space
     *
     * @return the loaded semantic space or {@code null} no space with the
     *         provided name exists
     */
    private SemanticSpace getSSpace(String name) {
        String aliased = aliasToFileName.get(name);
        return (aliased != null) ? fileNameToSSpace.get(aliased) : fileNameToSSpace.get(name);
    }

    /**
     * Executes the specified command and writes any output to standard out.  If
     * an error occurs an error message will be written instead.
     *
     * @param commandTokens the series of tokens that comprise the command and
     *        all of its arguments
     *
     * @return {@code true} if the command was successfully executed
     */
    public boolean execute(Iterator<String> commandTokens) {
        return execute(commandTokens, System.out);
    }

    /**
     * Executes the specified command and writes any output to the provided
     * stream.  If an error occurs an error message will be written to the
     * stream instead.
     *
     * @param commandTokens the series of tokens that comprise the command and
     *        all of its arguments
     * @param out the stream to which any output should be written
     *
     * @return {@code true} if the command was successfully executed
     */
    private boolean execute(Iterator<String> commandTokens, PrintStream out) {
        if (!commandTokens.hasNext()) return false;
        String commandStr = commandTokens.next();
        Command command = null;
        try {
            command = Command.valueOf(commandStr.replaceAll("-", "_").toUpperCase());
        } catch (IllegalArgumentException iae) {
            command = abbreviatedCommands.get(commandStr);
            if (command == null) {
                out.println("Unknown command: " + commandStr);
                return false;
            }
        }
        command_switch: switch(command) {
            case LOAD:
                {
                    if (!commandTokens.hasNext()) {
                        out.println("missing .sspace file argument");
                        return false;
                    }
                    String sspaceFileName = commandTokens.next();
                    if (fileNameToSSpace.containsKey(sspaceFileName)) break;
                    SemanticSpace sspace = null;
                    try {
                        sspace = SemanticSpaceIO.load(sspaceFileName);
                    } catch (Throwable t) {
                        out.println("an error occurred while loading the semantic " + "space from " + sspaceFileName + ":\n" + t);
                        t.printStackTrace();
                    }
                    fileNameToSSpace.put(sspaceFileName, sspace);
                    current = sspace;
                    break;
                }
            case UNLOAD:
                {
                    if (!commandTokens.hasNext()) {
                        out.println("missing .sspace file argument");
                        return false;
                    }
                    String sspaceName = commandTokens.next();
                    String aliased = aliasToFileName.get(sspaceName);
                    SemanticSpace removed = null;
                    if (aliased != null) {
                        aliasToFileName.remove(sspaceName);
                        removed = fileNameToSSpace.remove(aliased);
                    } else {
                        removed = fileNameToSSpace.remove(sspaceName);
                        Iterator<Map.Entry<String, String>> it = aliasToFileName.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, String> e = it.next();
                            if (e.getValue().equals(sspaceName)) {
                                it.remove();
                                break;
                            }
                        }
                    }
                    if (removed == current) {
                        Iterator<SemanticSpace> it = fileNameToSSpace.values().iterator();
                        current = (it.hasNext()) ? it.next() : null;
                    }
                    break;
                }
            case ALIAS:
                {
                    if (!commandTokens.hasNext()) {
                        out.println("missing .sspace file argument");
                        return false;
                    }
                    String fileName = commandTokens.next();
                    if (!fileNameToSSpace.containsKey(fileName)) {
                        out.println(fileName + "is not currently loaded");
                        return false;
                    }
                    if (!commandTokens.hasNext()) {
                        out.println("missing alias name");
                        return false;
                    }
                    String alias = commandTokens.next();
                    aliasToFileName.put(alias, fileName);
                    break;
                }
            case GET_NEIGHBORS:
                {
                    if (!commandTokens.hasNext()) {
                        out.println("missing word argument");
                        return false;
                    }
                    String focusWord = commandTokens.next();
                    int neighbors = 10;
                    if (commandTokens.hasNext()) {
                        String countStr = commandTokens.next();
                        try {
                            neighbors = Integer.parseInt(countStr);
                        } catch (NumberFormatException nfe) {
                            out.println("invalid number of neighbors: " + countStr);
                            return false;
                        }
                    }
                    Similarity.SimType simType = Similarity.SimType.COSINE;
                    if (commandTokens.hasNext()) {
                        String simTypeStr = commandTokens.next().toUpperCase();
                        try {
                            simType = Similarity.SimType.valueOf(simTypeStr);
                        } catch (IllegalArgumentException iae) {
                            for (Similarity.SimType t : Similarity.SimType.values()) if (t.name().startsWith(simTypeStr)) simType = t;
                            if (simType == null) {
                                out.println("invalid similarity measure: " + simTypeStr);
                                return false;
                            }
                        }
                    }
                    SortedMultiMap<Double, String> mostSimilar = wordComparator.getMostSimilar(focusWord, current, neighbors, simType);
                    if (mostSimilar == null) {
                        out.println(focusWord + " is not in the current semantic space");
                    } else {
                        for (Map.Entry<Double, String> e : mostSimilar.entrySet()) {
                            out.println(e.getValue() + "\t" + e.getKey());
                        }
                    }
                    break;
                }
            case GET_SIMILARITY:
                {
                    if (current == null) {
                        out.println("no current semantic space");
                        return false;
                    }
                    if (!commandTokens.hasNext()) {
                        out.println("missing word argument");
                        return false;
                    }
                    String word1 = commandTokens.next();
                    if (!commandTokens.hasNext()) {
                        out.println("missing word argument");
                        return false;
                    }
                    String word2 = commandTokens.next();
                    Similarity.SimType simType = Similarity.SimType.COSINE;
                    if (commandTokens.hasNext()) {
                        String simTypeStr = commandTokens.next().toUpperCase();
                        try {
                            simType = Similarity.SimType.valueOf(simTypeStr);
                        } catch (IllegalArgumentException iae) {
                            for (Similarity.SimType t : Similarity.SimType.values()) if (t.name().startsWith(simTypeStr)) simType = t;
                            if (simType == null) {
                                out.println("invalid similarity measure: " + simTypeStr);
                                return false;
                            }
                        }
                    }
                    Vector word1vec = current.getVector(word1);
                    if (word1vec == null) {
                        out.println(word1 + " is not in semantic space " + getCurrentSSpaceFileName());
                        break;
                    }
                    Vector word2vec = current.getVector(word2);
                    if (word2vec == null) {
                        out.println(word2 + " is not in semantic space " + getCurrentSSpaceFileName());
                        break;
                    }
                    double similarity = Similarity.getSimilarity(simType, word1vec, word2vec);
                    out.println(similarity);
                    break;
                }
            case COMPARE_SSPACE_VECTORS:
                {
                    if (!commandTokens.hasNext()) {
                        out.println("missing word argument");
                        return false;
                    }
                    String word = commandTokens.next();
                    if (!commandTokens.hasNext()) {
                        out.println("missing sspace argument");
                        return false;
                    }
                    String name1 = commandTokens.next();
                    SemanticSpace sspace1 = getSSpace(name1);
                    if (sspace1 == null) {
                        out.println("no such semantic space: " + name1);
                        return false;
                    }
                    if (!commandTokens.hasNext()) {
                        out.println("missing sspace argument");
                        return false;
                    }
                    String name2 = commandTokens.next();
                    SemanticSpace sspace2 = getSSpace(name2);
                    if (sspace2 == null) {
                        out.println("no such semantic space: " + name2);
                        return false;
                    }
                    Similarity.SimType simType = Similarity.SimType.COSINE;
                    if (commandTokens.hasNext()) {
                        String simTypeStr = commandTokens.next();
                        try {
                            simType = Similarity.SimType.valueOf(simTypeStr);
                        } catch (IllegalArgumentException iae) {
                            out.println("invalid similarity measure: " + simTypeStr);
                            return false;
                        }
                    }
                    Vector sspace1vec = sspace1.getVector(word);
                    if (sspace1vec == null) {
                        out.println(word + " is not in semantic space " + name1);
                        break;
                    }
                    Vector sspace2vec = sspace2.getVector(word);
                    if (sspace2vec == null) {
                        out.println(word + " is not in semantic space " + name2);
                        break;
                    }
                    if (sspace1vec.length() != sspace2vec.length()) {
                        out.println(name1 + " and " + name2 + " have different numbers " + "of dimensions and are not comparable.");
                        break;
                    }
                    double similarity = Similarity.getSimilarity(simType, sspace1vec, sspace2vec);
                    out.println(similarity);
                    break;
                }
            case HELP:
                {
                    out.println("available commands:\n" + getCommands());
                    break;
                }
            case WRITE_COMMAND_RESULTS:
                {
                    if (!commandTokens.hasNext()) {
                        out.println("missing file destination argument");
                        return false;
                    }
                    String fileName = commandTokens.next();
                    try {
                        PrintStream ps = new PrintStream(fileName);
                        execute(commandTokens, ps);
                        ps.close();
                    } catch (IOException ioe) {
                        out.println("An error occurred while writing to " + fileName + ":\n" + ioe);
                    }
                    break;
                }
            case PRINT_VECTOR:
                {
                    if (current == null) {
                        out.println("no current semantic space");
                        return false;
                    }
                    if (!commandTokens.hasNext()) {
                        out.println("missing word argument");
                        return false;
                    }
                    String word = commandTokens.next();
                    Vector vec = current.getVector(word);
                    if (vec == null) {
                        out.println(word + " is not in semantic space " + getCurrentSSpaceFileName());
                        break;
                    }
                    out.println(VectorIO.toString(vec));
                    break;
                }
            case SET_CURRENT_SSPACE:
                {
                    if (!commandTokens.hasNext()) {
                        out.println("missing .sspace file argument");
                        return false;
                    }
                    String spaceName = commandTokens.next();
                    String fileName = aliasToFileName.get(spaceName);
                    if (fileName == null) fileName = spaceName;
                    SemanticSpace s = fileNameToSSpace.get(fileName);
                    if (s == null) {
                        out.println("no such .sspace (file is not currently loaded)");
                        return false;
                    }
                    current = s;
                    break;
                }
            case GET_CURRENT_SSPACE:
                {
                    String currentSpaceName = getCurrentSSpaceFileName();
                    if (currentSpaceName != null) out.println(currentSpaceName); else out.println("none");
                    break;
                }
            case GET_WORDS:
                {
                    String prefix = null;
                    if (commandTokens.hasNext()) prefix = commandTokens.next();
                    Set<String> words = current.getWords();
                    for (String word : words) {
                        if (prefix == null) out.println(word); else if (word.startsWith(prefix)) out.println(word);
                    }
                    break;
                }
            case DESCRIBE_DIMENSION:
                {
                    if (current instanceof DimensionallyInterpretableSemanticSpace) {
                        if (!commandTokens.hasNext()) {
                            out.println("Must supply a dimension number");
                            break;
                        }
                        int dim = -1;
                        String next = commandTokens.next();
                        try {
                            dim = Integer.parseInt(next);
                        } catch (NumberFormatException nfe) {
                            out.println("Invalid dimension: " + next);
                            break;
                        }
                        DimensionallyInterpretableSemanticSpace<?> diss = (DimensionallyInterpretableSemanticSpace) current;
                        try {
                            out.println(diss.getDimensionDescription(dim).toString());
                        } catch (Exception e) {
                            out.println(e.getMessage());
                        }
                    } else out.println("Current space has no dimension descriptions");
                    break;
                }
            case DESCRIBE_SEMANTIC_SPACE:
                {
                    if (current == null) {
                        out.println("no .sspace loaded");
                        break;
                    }
                    String name = current.getSpaceName();
                    boolean hasDimDescriptions = current instanceof DimensionallyInterpretableSemanticSpace;
                    int dims = current.getVectorLength();
                    int words = current.getWords().size();
                    boolean isSparse = (current.getWords().isEmpty()) || current.getVector(current.getWords().iterator().next()) instanceof SparseVector;
                    out.println(name + ": " + words + " words, " + dims + " dimensions" + ((hasDimDescriptions) ? " with descriptions" : "") + ((isSparse) ? ", sparse vectors" : ", dense vectors"));
                    break;
                }
            default:
                assert false : command;
        }
        return true;
    }

    /**
     * Returns a formatted list of the available commands that a {@code
     * SemanticSpaceExplorer} instance will recognize.
     *
     * @return the commands
     */
    private static String getCommands() {
        return "  load file1.sspace [file2.sspace...]\n" + "  unload file1.sspace [file2.sspace...]\n" + "  get-neighbors word [number (default 10)] [similarity measure]\n" + "  get-similarity word1 word2 [similarity measure " + "(default cosine)]\n" + "  compare-sspace-vectors word sspace1 sspace2 " + "[similarity measure (default: cosine)]\n" + "  help\n" + "  set-current-sspace filename.sspace\n" + "  get-current-sspace\n" + "  alias filename.sspace name\n" + "  write-command-results output-file command...\n" + "  print-vector word\n" + "  get-words [string-prefix]\n" + "  describe-dimension number\n" + "  describe-semantic-space\n";
    }

    /**
     * Prints the options and supported commands used by this program.
     *
     * @param options the options supported by the system
     */
    private static void usage(ArgOptions options) {
        System.out.println("usage: java SemanticSpaceExplorer [options]\n\n" + "Command line options:\n" + options.prettyPrint() + "\n\nExplorer commands:\n" + getCommands());
    }

    public static void main(String[] args) {
        ArgOptions options = new ArgOptions();
        options.addOption('h', "help", "Generates a help message and exits", false, null, "Program Options");
        options.addOption('f', "executeFile", "Executes the commands in the " + "specified file and exits", true, "FILE", "Program Options");
        options.addOption('s', "saveRecord", "Saves a record of all the " + "executed commands to the specfied file", true, "FILE", "Program Options");
        options.parseOptions(args);
        if (options.hasOption("help")) {
            usage(options);
            return;
        }
        PrintWriter recordFile = null;
        if (options.hasOption("saveRecord")) {
            try {
                recordFile = new PrintWriter(options.getStringOption("saveRecord"));
            } catch (IOException ioe) {
                System.out.println("Unable to open file for saving commands:\n" + ioe);
            }
        }
        BufferedReader commandsToExecute = null;
        if (options.hasOption("executeFile")) {
            try {
                commandsToExecute = new BufferedReader(new FileReader(options.getStringOption("executeFile")));
            } catch (IOException ioe) {
                System.out.println("unable to open commands file " + options.getStringOption("executeFile") + ":\n" + ioe);
                return;
            }
        } else {
            commandsToExecute = new BufferedReader(new InputStreamReader(System.in));
        }
        boolean suppressPrompt = options.hasOption("executeFile");
        SemanticSpaceExplorer explorer = new SemanticSpaceExplorer();
        try {
            if (!suppressPrompt) System.out.print("> ");
            for (String command = null; (command = commandsToExecute.readLine()) != null; ) {
                Iterator<String> commandTokens = new WordIterator(command);
                if (explorer.execute(commandTokens) && recordFile != null) {
                    recordFile.println(command);
                }
                if (!suppressPrompt) System.out.print("> ");
            }
        } catch (IOException ioe) {
            System.out.println("An error occurred while reading in a command:\n" + ioe);
        }
        if (recordFile != null) {
            recordFile.close();
        }
    }
}
