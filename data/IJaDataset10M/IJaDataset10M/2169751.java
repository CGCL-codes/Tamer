package com.strategicgains.openef.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 *  The option pattern is defined of those characters accepted as options
 *  or arguments.  Argument options are followed by '~' in the string.  All
 *  others are assumed to be switches (options).
 *
 *  Example:  If Options = "abC~c~de~fgh~" then
 *            'a', 'b', 'd', 'f', and 'g' are option switches, 
 *            and 'C', 'c', 'e' and 'h' are followed with arguments.
 *
 *  If the switch character reported by DOS is '/' then using the above option
 *  line:
 *     Command /g /a /b /c xyzt extras
 *
 *  Yields:
 *     'a', 'b', and 'g' are returned as option switches.
 *     'c' returns with "xyzt" as its argument in Arg.
 *     "extras" is not an option and require special handling by using the argv 
 *     pointers later in the program.
 *
 * @author Todd Fredrich
 * @since  Oct 23, 2003
 */
public class CommandLineParser {

    private static final char ARGUMENT_INDICATOR = '~';

    private static final char SWITCH_CHARACTER = '-';

    private String pattern;

    private Set patternSwitches;

    private Set patternOptions;

    private Vector arguments;

    private HashMap optionArguments;

    private Set setSwitches;

    /**
	 * Constructs a new CommandLineParser object using the pattern.
	 */
    public CommandLineParser(String pattern) {
        super();
        setPattern(pattern);
    }

    /**
	 * Method: setPattern()
	 * 
	 * @param pattern
	 */
    private void setPattern(String pattern) {
        this.pattern = pattern;
        patternSwitches = new HashSet();
        patternOptions = new HashSet();
        for (int i = 0; i < pattern.length(); ++i) {
            char token = pattern.charAt(i);
            if (i < pattern.length() - 1) {
                char indicator = pattern.charAt(i + 1);
                if (indicator == ARGUMENT_INDICATOR) {
                    patternOptions.add(new Character(token));
                    ++i;
                } else {
                    patternSwitches.add(new Character(token));
                }
            } else {
                patternSwitches.add(new Character(token));
            }
        }
    }

    public void parse(String[] args) throws CommandLineException {
        optionArguments = new HashMap();
        arguments = new Vector();
        setSwitches = new HashSet();
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                char switchChar = arg.charAt(0);
                if (switchChar == SWITCH_CHARACTER) {
                    for (int j = 1; j < arg.length(); ++j) {
                        Character option = new Character(arg.charAt(j));
                        if (patternSwitches.contains(option)) {
                            setSwitches.add(option);
                        } else if (patternOptions.contains(option)) {
                            if (arg.length() > 2) {
                                throw new CommandLineException("Invalid command line option: " + arg);
                            }
                            if (i + 1 >= args.length) {
                                throw new CommandLineException("Parameter required for option: " + arg);
                            }
                            optionArguments.put(option, args[++i]);
                        } else {
                            throw new CommandLineException("Invalid command line option: " + option);
                        }
                    }
                } else {
                    arguments.add(arg);
                }
            }
        }
    }

    public boolean isOptionSet(char optionToken) {
        boolean isSet = false;
        Character option = new Character(optionToken);
        if (setSwitches.contains(option)) {
            isSet = true;
        } else if (optionArguments.containsKey(option)) {
            isSet = true;
        }
        return isSet;
    }

    public String getOptionArgument(char optionToken) {
        return (String) optionArguments.get(new Character(optionToken));
    }

    public String[] getArguments() {
        String[] strings = new String[arguments.size()];
        return (String[]) arguments.toArray(strings);
    }

    /**
	 * Method: getPattern()
	 * 
	 * @return
	 */
    public String getPattern() {
        return pattern;
    }
}
