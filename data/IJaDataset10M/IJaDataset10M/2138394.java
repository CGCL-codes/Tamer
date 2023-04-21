package instance;

import java.util.List;
import list.*;
import structures.Arguments;

public class Options {

    public static boolean CRITICAL_ERROR = true;

    private static final int ARG_INCLUDE_LIST = 0, ARG_EXCLUDE_LIST = 1, ARG_TRICKY_LIST = 2, ARG_ALL_CONTEXT = 3, ARG_NO_CONTEXT = 4, ARG_EVERY_CONTEXT = 5, ARG_DISCARD_TRICKY = 6, ARG_MATCH_INCLUDE_ONLY = 7, ARG_PARSABLE_OUTPUT = 8, ARG_NO_TABLE = 9, ARG_MAX_THREADS = 10;

    private static final String[][] VALID_ARGS = { { "-i", "--include-list" }, { "-e", "--exclude-list" }, { "-t", "--tricky-list" }, { "-a", "--all-context" }, { "-n", "--no-context" }, { "-v", "--every-context" }, { "-d", "--discard-tricky" }, { "-m", "--match-include-only" }, { "-p", "--parsable-output" }, { "-s", "--no-table" }, { "-h", "--max-threads" } };

    private static final String INFINITY = "infinity";

    private static boolean alwaysUsingContext = false, usingContext = true, usingEveryContext = false, discardingTricky = false, matchingIncludeOnly = false, usingParsableOutput = false, showingTable = true;

    private static int maxThreads = 1;

    private static List<String> documents = null;

    public static boolean initialize(String[] args) {
        boolean criticalError = false;
        Arguments arguments = new Arguments(args);
        System.out.println(arguments.toString());
        documents = arguments.getFreeArgs();
        alwaysUsingContext = arguments.contains(VALID_ARGS[ARG_ALL_CONTEXT]);
        usingContext = !arguments.contains(VALID_ARGS[ARG_NO_CONTEXT]);
        usingEveryContext = arguments.contains(VALID_ARGS[ARG_EVERY_CONTEXT]);
        discardingTricky = arguments.contains(VALID_ARGS[ARG_DISCARD_TRICKY]);
        matchingIncludeOnly = arguments.contains(VALID_ARGS[ARG_MATCH_INCLUDE_ONLY]);
        usingParsableOutput = arguments.contains(VALID_ARGS[ARG_PARSABLE_OUTPUT]);
        showingTable = !arguments.contains(VALID_ARGS[ARG_NO_TABLE]);
        List<String> includeLists = arguments.get(VALID_ARGS[ARG_INCLUDE_LIST]);
        List<String> excludeLists = arguments.get(VALID_ARGS[ARG_EXCLUDE_LIST]);
        List<String> trickyLists = arguments.get(VALID_ARGS[ARG_TRICKY_LIST]);
        List<String> maxThreadsArgs = arguments.get(VALID_ARGS[ARG_MAX_THREADS]);
        if (maxThreadsArgs != null && maxThreadsArgs.size() != 0) {
            String lastArg = maxThreadsArgs.get(maxThreadsArgs.size() - 1);
            try {
                maxThreads = Integer.parseInt(lastArg);
            } catch (NumberFormatException e) {
                if (lastArg.toLowerCase().equals(INFINITY)) {
                    maxThreads = documents.size();
                }
            }
            if (maxThreads <= 0) {
                maxThreads = 1;
            }
        }
        if (includeLists == null || includeLists.size() == 0) {
            System.err.println("CRITICAL ERROR: You did not specify an 'include' list to use.");
            criticalError = true;
        } else {
            for (String filename : includeLists) {
                if (!Include.instance.addFile(filename)) {
                    System.err.println("ERROR: Could use \"" + filename + "\" as an 'include' file -" + " it does not exist.");
                }
            }
        }
        if (excludeLists == null || excludeLists.size() == 0) {
            System.err.println("ERROR: You did not specify an 'exclude' list to use.");
        } else {
            for (String filename : excludeLists) {
                if (!Exclude.instance.addFile(filename)) {
                    System.err.println("ERROR: Could use \"" + filename + "\" as an 'exclude' file -" + " it does not exist.");
                }
            }
        }
        if (trickyLists == null || trickyLists.size() == 0) {
            System.err.println("ERROR: You did not specify a 'tricky' list to use.");
        } else {
            for (String filename : trickyLists) {
                if (!Tricky.instance.addFile(filename)) {
                    System.err.println("ERROR: Could use \"" + filename + "\" as a 'tricky' file -" + " it does not exist.");
                }
            }
        }
        Exclude.instance.addInclude(Include.instance);
        Misspell.instance.addSet(Include.instance.keySet());
        arguments = null;
        return criticalError;
    }

    public static List<String> getDocuments() {
        return documents;
    }

    public static boolean isAlwaysUsingContext() {
        return alwaysUsingContext;
    }

    public static boolean isUsingContext() {
        return usingContext;
    }

    public static boolean isUsingEveryContext() {
        return usingEveryContext;
    }

    public static boolean isDiscardingTricky() {
        return discardingTricky;
    }

    public static boolean isMatchingIncludeOnly() {
        return matchingIncludeOnly;
    }

    public static boolean isUsingParsableOutput() {
        return usingParsableOutput;
    }

    public static boolean isShowingTable() {
        return showingTable;
    }

    public static int getMaxThreads() {
        return maxThreads;
    }
}
