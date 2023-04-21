package gumbo.arg;

import gumbo.core.resource.util.ResourceUtils;
import gumbo.core.util.AssertUtils;
import gumbo.core.util.Checker;
import gumbo.core.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utilities related to argument options and parsing.
 * @author jonb
 */
public class ArgUtils {

    private ArgUtils() {
    }

    /**
	 * Concatenates none or more arrays of arguments, skipping any null groups
	 * or empty arguments. Otherwise, arguments are copied as-is (i.e. quotes
	 * and whitespace are the same).
	 * @param argGroups Temp input group of temp input group of args. If null
	 * returns an empty array.
	 * @return Ceded array of non-empty arguments. Possibly empty, never null.
	 */
    public static String[] concatArgs(String[]... argGroups) {
        if (argGroups == null) return new String[0];
        List<String> argList = new ArrayList<String>();
        for (String[] argGroup : argGroups) {
            if (argGroup == null) continue;
            for (String arg : argGroup) {
                if (arg == null) continue;
                if (arg.isEmpty()) continue;
                argList.add(arg);
            }
        }
        return argList.toArray(new String[0]);
    }

    /**
	 * Parses a group of optionally quoted string into an array of arguments.
	 * @param args Temp input group of args. Possibly empty, never null.
	 * @return Ceded array of non-empty arguments. Possibly empty, never null.
	 * @see StringUtils#parseQuotedStrings(String)
	 */
    public static String[] parseArgGroup(Collection<String> args) {
        AssertUtils.assertNonEmptyArgAll(args);
        StringBuilder argString = new StringBuilder();
        for (String arg : args) {
            argString.append(" " + arg);
        }
        return parseArgString(argString.toString());
    }

    /**
	 * Parses a string of white-space delimited and optionally quoted
	 * sub-strings as an array of command line arguments.
	 * @param argString String of arguments. Possibly empty,
	 * never null.
	 * @return Ceded array of arguments. Possibly empty, never null.
	 * @see StringUtils#parseQuotedStrings(String)
	 */
    public static String[] parseArgString(String argString) {
        AssertUtils.assertNonNullArg(argString);
        List<String> result = StringUtils.parseQuotedStrings(argString);
        return result.toArray(new String[0]);
    }

    /**
	 * Parses the lines in a text file as an array of command line arguments.
	 * Intended as an alternative mechanism for inputting command line arguments
	 * to an application. The file contents is treated as a single string
	 * containing white-space delimited and optionally quoted arguments. Assumes
	 * a non-quoted "#" indicates the start of a comment, and the rest of the
	 * line is ignored. Assumes that "\" at the end of a line is a line
	 * continuation character, and is ignored (i.e. it is not needed).
	 * @param argFile The argument file. Never null.
	 * @return Ceded array of arguments. Possibly empty, never null.
	 * @throws IllegalStateException if the file cannot be accessed.
	 * @see StringUtils#parseQuotedStrings(String)
	 */
    public static String[] parseArgFile(File argFile) {
        List<String> argLines = ResourceUtils.getTextLines(argFile);
        return parseArgLines(argLines);
    }

    /**
	 * Parses the lines from a text file as an array of command line arguments. 
	 * Intended as an alternative mechanism for inputting command
	 * line arguments to an application. The file contents is treated as a
	 * single string containing white-space delimited and optionally quoted
	 * arguments. Assumes a non-quoted "#" indicates the start of a comment, and
	 * the rest of the line is ignored. Assumes that "\" at the end of a line is
	 * a line continuation character, and is ignored (i.e. it is not needed).
	 * @param argLines List of non-null argument file lines. Possibly empty,
	 * never null.
	 * @return Ceded array of arguments. Possibly empty, never null.
	 * @see StringUtils#parseQuotedStrings(String)
	 */
    public static String[] parseArgLines(List<String> argLines) {
        AssertUtils.assertNonNullArg(argLines);
        List<String> result = new ArrayList<String>();
        for (String line : argLines) {
            if (line.endsWith("\\")) {
                line = line.substring(0, line.length() - 1);
            }
            List<String> lineArgs = StringUtils.parseQuotedStrings(line);
            if (lineArgs.isEmpty()) continue;
            List<Boolean> quotedArgs = StringUtils.detectQuotedStrings(line);
            for (int argI = 0; argI < lineArgs.size(); argI++) {
                String lineArg = lineArgs.get(argI);
                if (lineArg.startsWith("#") && !quotedArgs.get(argI)) break;
                result.add(lineArg);
            }
        }
        return result.toArray(new String[0]);
    }

    /**
	 * Filters arguments to exclude system properties (i.e. begin with "-D").
	 * @param args Temp input group of arguments. Never null.
	 * @return Ceded array of filtered arguments. Never null.
	 */
    public static String[] excludeSysProps(String[] args) {
        return filterArguments(args, newSysPropExcluder());
    }

    /**
	 * Filters arguments according the checker, with approved arguments being
	 * included in the result. If the checker is stateful it should be created
	 * new and ceded (i.e. no way to rest it for reuse). Intended for the
	 * elimination of undesirable arguments prior to parsing (such as system
	 * properties).
	 * @param args Temp input group of arguments. Never null.
	 * @param includer Shared exposed checker. Never null.
	 * @return Ceded array of filtered arguments. Never null.
	 */
    public static String[] filterArguments(String[] args, Checker<String> checker) {
        AssertUtils.assertNonNullArg(args);
        AssertUtils.assertNonNullArg(checker);
        List<String> result = new ArrayList<String>();
        for (String arg : args) {
            if (checker != null && !checker.approves(arg)) continue;
            result.add(arg);
        }
        return result.toArray(new String[0]);
    }

    /**
	 * Returns a new stateless checker that excludes system properties (i.e.
	 * begin with "-D").
	 * @return Ceded checker. Never null.
	 */
    public static Checker<String> newSysPropExcluder() {
        return new Checker<String>() {

            @Override
            public boolean approves(String target) {
                if (target == null) return false;
                if (target.startsWith("-D")) return false;
                return true;
            }
        };
    }
}
