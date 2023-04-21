package com.bbn.vessel.core.arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import org.jdom.Element;
import com.bbn.vessel.core.arguments.ArgSpec.Type;
import com.bbn.vessel.core.logging.Logger;
import com.bbn.vessel.core.logging.LoggerBase;
import com.bbn.vessel.core.util.CSVUtility;
import com.bbn.vessel.core.util.XMLHelper;

/**
 * A map of name=value pairs, with ability to interpret the type of the values.  There is a
 * corresponding class, {@code ArgSpec} which contains metadata about the values in an
 * {@code Arguments} and which is used by the {@code arguments to interpret the data}
 */
public class Arguments extends TreeMap<String, Object> {

    private static final long serialVersionUID = 1L;

    private final Logger logger = LoggerBase.getLogger(Arguments.class);

    /** XML tag: arguments element (a collection of argument elements) */
    public static final String TAG_ARGUMENTS = "arguments";

    /** XML tag: argument element */
    public static final String TAG_ARGUMENT = "argument";

    /** XML tag: argument name */
    public static final String TAG_NAME = "name";

    /** XML tag: argument value */
    public static final String TAG_VALUE = "value";

    /**
     * Make one from a JDOM (XML) Element.
     *
     * @param argumentsElement
     *            the Element.
     * @param argSpecs
     *            A map from argument names to ArgSpecs (i.e. argument
     *            specifications, or arg types).
     * @param argParser
     *            A parser, used for general parsing and to handle special cases
     *            (where particular types of Objects need to be created given a
     *            particular ArgSpec).
     */
    public Arguments(Element argumentsElement, ArgSpecs argSpecs, ArgumentParser argParser) {
        if (argParser == null) {
            argParser = new ArgumentParser();
        }
        List<Element> argumentElements;
        try {
            argumentElements = XMLHelper.getChildren(argumentsElement, TAG_ARGUMENT);
        } catch (RuntimeException e) {
            throw new RuntimeException("Missing " + TAG_ARGUMENTS + " for " + argumentsElement, e);
        }
        for (Element argumentElement : argumentElements) {
            String name = argumentElement.getChildText(TAG_NAME);
            if (argSpecs != null) {
                ArgSpec argSpec = argSpecs.get(name);
                if (argSpec == null) {
                    String valString = argumentElement.getChildText(TAG_VALUE);
                    throw new RuntimeException("Unknown argument name \"" + name + "\"" + (valString == null ? "" : (" with value \"" + valString.trim() + "\"")) + ", expecting one of the following names: " + argSpecs.keySet());
                }
                argParser.parseArg(argumentElement, argSpec, this);
            } else {
                argParser.parseArg(argumentElement, null, this);
            }
        }
    }

    /**
     * Parse name=value pairs.
     * <p>
     * E.g. "a=b", "x=123", ...
     *
     * @param nameValuePairs
     *            Array of name-value pairs.
     */
    public Arguments(String... nameValuePairs) {
        this(null, null, nameValuePairs);
    }

    /**
     * Parse name=value pairs.
     * <p>
     * E.g. "a=b", "x=123", ...
     *
     * @param argSpecs specifies how to interpret arg values.
     * If null no ArgSpecs all args are strings
     * @param argParser an ArgumentParser to use. If null the default is used
     * @param nameValuePairs Array of name-value pairs
     */
    public Arguments(ArgSpecs argSpecs, ArgumentParser argParser, String... nameValuePairs) {
        if (argSpecs == null) {
            argSpecs = new ArgSpecs();
        }
        if (argParser == null) {
            argParser = ArgumentParser.singleton();
        }
        for (String s : nameValuePairs) {
            int sep = s.indexOf('=');
            if (sep < 0) {
                if (s.length() == 0) {
                    continue;
                }
                throw new IllegalArgumentException("Missing '=' in " + s);
            }
            String key = s.substring(0, sep).trim();
            String value = s.substring(sep + 1).trim();
            Object objectValue = value;
            ArgSpec argSpec = argSpecs.get(key);
            if (argSpec != null) {
                objectValue = argParser.parseArg(value, argSpec);
            }
            this.put(key, objectValue);
        }
    }

    /**
     * Parse command-line arguments.
     * <p>
     *
     * <pre>
     * E.g. options:
     *   Arrays.asList(
     *     new Option("file", "--file", "-f", "readme.txt"),
     *     new Option("blah", "--b", "-b", "42"),
     *     new Option("usage", "--help", "-h"));
     * and args:
     *   "--file" "foo.txt" "-b1234"
     * </pre>
     *
     * @param options
     *            List of options.
     * @param args
     *            Command-line args.
     */
    public Arguments(List<Option> options, String... args) {
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            Option opt = null;
            for (Option o : options) {
                if (o.isMatch(s)) {
                    opt = o;
                    break;
                }
            }
            if (opt == null) {
                throw new RuntimeException("Unknown option: " + s);
            }
            String key = opt.getKey();
            String value = opt.getDefaultValue();
            if (value == null) {
                value = "true";
            } else if (opt.isSplitMatch(s)) {
                if ((i + 1) < args.length) {
                    value = args[++i];
                }
            } else {
                int n = opt.getConcatName().length();
                if (s.length() > n) {
                    value = s.substring(n);
                }
            }
            this.put(key, value);
        }
    }

    @Override
    public Object put(String key, Object value) {
        if (key.equalsIgnoreCase("Color") && (value != null) && (value instanceof String) && (((String) value).length() == 0)) logger.error("null string being put for color!!", new Exception());
        return super.put(key, value);
    }

    /**
     * duplicate the contents of another arguments
     *
     * @param toCopy
     *            The source Arguments
     */
    public Arguments(Arguments toCopy) {
        for (String key : toCopy.keySet()) {
            if (toCopy.get(key) instanceof Arguments) {
                put(key, new Arguments(toCopy.getArguments(key)));
            } else {
                put(key, toCopy.get(key));
            }
        }
    }

    /**
     * Get an argument, returning the provided default if it doesn't exist.
     *
     * @param name
     *            Name of argument to look up.
     * @param deflt
     *            The default value, returned if argument name cannot be found.
     * @return the argument, or {@code deflt} if the name cannot be found.
     */
    public Object get(String name, Object deflt) {
        Object o = get(name);
        return (o == null ? deflt : o);
    }

    /**
     * Get a String argument.
     *
     * @param name
     *            Name of argument to look up.
     * @return the {@code toString()} of the argument, or null if {@code name}
     *         cannot be found.
     */
    public String getString(String name) {
        Object o = get(name);
        return (o == null ? null : o.toString());
    }

    private String getString2(String name) {
        String s = getString(name);
        return ((s == null || s.length() == 0) ? null : s);
    }

    /**
     * Get the {@code toString()} of an argument, returning the provided default
     * if the argument cannot be found.
     *
     * @param name
     *            Name of argument to look up.
     * @param deflt
     *            The default value, returned if argument name cannot be found.
     * @return the argument, or {@code deflt} if the name cannot be found.
     */
    public String getString(String name, String deflt) {
        String value = getString(name);
        return (value == null ? deflt : value);
    }

    /**
     * Iterate through the provided {@code names} and look them up, returning
     * the {@code toString()} of the first argument we find that matches one, or
     * throwing a {@code RuntimeException} if we can't find any matches.
     *
     * @param names
     *            The array of names to look up.
     * @return the {@code toString()} of the argument that is the first one we
     *         successfully look up while iterating through {@code names}.
     * @throws RuntimeException
     *             if none of the names in {@code names} match the names of any
     *             arguments.
     */
    public String assertGetString(String... names) throws RuntimeException {
        for (String name : names) {
            String value = getString(name);
            if (value != null) {
                return value;
            }
        }
        StringBuffer buf = new StringBuffer();
        for (String name : names) {
            buf.append(buf.length() == 0 ? "Missing " : " or ").append(name);
        }
        buf.append(" argument");
        throw new RuntimeException(buf.toString());
    }

    /**
     * Look up and return an argument of type {@code boolean}.
     *
     * @param name
     *            The arg name.
     * @return the arg value, or false if it cannot be found.
     */
    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    /**
     * Look up and return an argument of type {@code boolean}, returning {@code
     * deflt} if it cannot be found.
     *
     * @param name
     *            The arg name.
     * @param deflt
     *            The default value, to be returned if {@code name} cannot be
     *            found.
     * @return the arg value, or {@code deflt} if it cannot be found.
     */
    public boolean getBoolean(String name, boolean deflt) {
        String value = getString2(name);
        return (value == null ? deflt : Boolean.parseBoolean(value));
    }

    /**
     * Look up and return an argument of type {@code int}.
     *
     * @param name
     *            The arg name.
     * @return the arg value, or -1 if it cannot be found.
     */
    public int getInt(String name) {
        return getInt(name, -1);
    }

    /**
     * Look up and return an argument of type {@code int}, returning {@code
     * deflt} if it cannot be found.
     *
     * @param name
     *            The arg name.
     * @param deflt
     *            The default value, to be returned if {@code name} cannot be
     *            found.
     * @return the arg value, or {@code deflt} if it cannot be found.
     */
    public int getInt(String name, int deflt) {
        String value = getString2(name);
        return (value == null ? deflt : Integer.parseInt(value));
    }

    /**
     * Look up and return an argument of type {@code float}.
     *
     * @param name
     *            The arg name.
     * @return the arg value, or -1 if it cannot be found.
     */
    public float getFloat(String name) {
        return getFloat(name, -1);
    }

    /**
     * Look up and return an argument of type {@code float}, returning {@code
     * deflt} if it cannot be found.
     *
     * @param name
     *            The arg name.
     * @param deflt
     *            The default value, to be returned if {@code name} cannot be
     *            found.
     * @return the arg value, or {@code deflt} if it cannot be found.
     */
    public float getFloat(String name, float deflt) {
        String value = getString2(name);
        return (value == null ? deflt : Float.parseFloat(value));
    }

    /**
     * Look up and return an argument of type {@code long}.
     *
     * @param name
     *            The arg name.
     * @return the arg value, or -1 if it cannot be found.
     */
    public long getLong(String name) {
        return getLong(name, -1);
    }

    /**
     * Look up and return an argument of type {@code long}, returning {@code
     * deflt} if it cannot be found.
     *
     * @param name
     *            The arg name.
     * @param deflt
     *            The default value, to be returned if {@code name} cannot be
     *            found.
     * @return the arg value, or {@code deflt} if it cannot be found.
     */
    public long getLong(String name, long deflt) {
        String value = getString2(name);
        return (value == null ? deflt : Long.parseLong(value));
    }

    /**
     * Look up and return an argument of type {@code double}.
     *
     * @param name
     *            The arg name.
     * @return the arg value, or -1 if it cannot be found.
     */
    public double getDouble(String name) {
        return getDouble(name, -1);
    }

    /**
     * Look up and return an argument of type {@code double}, returning {@code
     * deflt} if it cannot be found.
     *
     * @param name
     *            The arg name.
     * @param deflt
     *            The default value, to be returned if {@code name} cannot be
     *            found.
     * @return the arg value, or {@code deflt} if it cannot be found.
     */
    public double getDouble(String name, double deflt) {
        String value = getString2(name);
        return (value == null ? deflt : Double.parseDouble(value));
    }

    /**
     * Look up and return an argument of type {@code Arguments} - i.e. an
     * argument that represents a sub-group of additional arguments.
     *
     * @param name
     *            The arg name.
     * @return the arg value, or null if it cannot be found.
     */
    public Arguments getArguments(String name) {
        Object o = get(name);
        if (o == null || o instanceof Arguments) {
            return (Arguments) o;
        } else if (o instanceof Element) {
            return new Arguments((Element) o, null, new ArgumentParser());
        }
        String[] nvp = CSVUtility.parse((String) o);
        return new Arguments(nvp);
    }

    /**
     * Look up and return an argument of type {@code Vector<String>}
     * @param name
     *            The arg name.
     * @return the arg value, or null if it cannot be found.
     */
    @SuppressWarnings("unchecked")
    public Vector<String> getVector(String name) {
        Object o = get(name);
        if (o == null) return null; else if (o instanceof Vector<?>) {
            if ((((Vector<?>) o).size() > 0) && (!(((Vector<?>) o).get(0) instanceof String))) {
                logger.warn("found array element that is not a string.  " + "don't know what to do with it");
                return null;
            }
            return (Vector<String>) o;
        } else return new Vector<String>(Arrays.asList(getString2(name).split(", ")));
    }

    /**
     * Representation of an optional command-line argument.
     */
    public static final class Option {

        private final String key;

        private final String splitName;

        private final String concatName;

        private final String deflt;

        /**
         * Make one, with null for the default value.
         *
         * @param key
         *            The key.
         * @see #getKey()
         * @param splitName
         *            The split argument name.
         * @see #getLongName()
         * @param concatName
         *            The concat argument name.
         * @see #getConcatName()
         */
        public Option(String key, String splitName, String concatName) {
            this(key, splitName, concatName, null);
        }

        /**
         * Make one.
         *
         * @param key
         *            The key.
         * @see #getKey()
         * @param splitName
         *            The split argument name.
         * @see #getLongName()
         * @param concatName
         *            The concat argument name.
         * @see #getConcatName()
         * @param deflt
         *            The default value.
         */
        public Option(String key, String splitName, String concatName, String deflt) {
            this.key = key;
            this.splitName = splitName;
            this.concatName = concatName;
            this.deflt = deflt;
        }

        /** @return the key, e.g. "blah" */
        public String getKey() {
            return key;
        }

        /** @return the split argument name, e.g. "--blah" for "--blah 123" */
        public String getLongName() {
            return splitName;
        }

        /** @return the concat argument name, e.g. "-b" for "-b123" */
        public String getConcatName() {
            return concatName;
        }

        /** @return the default value, e.g. "42", or null for "true"-if-exists */
        public String getDefaultValue() {
            return deflt;
        }

        /**
         * Test for a match.
         *
         * @param s
         *            The string representation of the argument with which to
         *            compare ourselves.
         * @return true if {@link #isSplitMatch} or {@link #isConcatMatch}
         */
        public boolean isMatch(String s) {
            return isSplitMatch(s) || isConcatMatch(s);
        }

        /**
         * @param s
         *            e.g. "--blah"
         * @return true if "s" matches the splitName
         */
        public boolean isSplitMatch(String s) {
            return splitName != null && s.equalsIgnoreCase(splitName);
        }

        /**
         * @param s
         *            e.g. "-b123"
         * @return true if "s" starts with the concatName
         */
        public boolean isConcatMatch(String s) {
            return concatName != null && (s.length() >= concatName.length()) && s.regionMatches(true, 0, concatName, 0, concatName.length());
        }

        @Override
        public String toString() {
            return "  " + (concatName == null ? "" : concatName + (deflt == null ? "" : "VALUE")) + (concatName == null || splitName == null ? "" : ", ") + (splitName == null ? "" : splitName + (deflt == null ? "" : " VALUE")) + "    " + key + (deflt == null ? "" : " (" + deflt + ")");
        }
    }

    /**
     * Convert to a JDOM (XML) {@link Element} and return the new Element.
     *
     * @param xmlizer
     *            Helper {@link ArgumentValueXmlizer} that handles any special
     *            cases in conversion.
     * @return the new Element.
     */
    public Element toXML(ArgumentValueXmlizer xmlizer) {
        if (xmlizer == null) {
            xmlizer = ArgumentValueXmlizer.singleton();
        }
        Element argumentsElement = new Element(TAG_ARGUMENTS);
        List<String> keys = new ArrayList<String>(keySet());
        Collections.sort(keys);
        for (String key : keys) {
            Object value = get(key);
            if (value == null) {
                continue;
            }
            Element propertyElement = new Element(TAG_ARGUMENT);
            argumentsElement.addContent(propertyElement);
            XMLHelper.addStringElement(propertyElement, TAG_NAME, key);
            xmlizer.xmlizeValue(value, propertyElement);
        }
        return argumentsElement;
    }

    /**
     * @return a simple name-value-pair-based representation of all args. E.g.
     *         arg1Name=arg1Value, arg2Name=arg2Value, ...
     */
    public String toCsvString() {
        String ret = "";
        boolean first = true;
        for (String key : this.keySet()) {
            if (first) {
                first = false;
            } else {
                ret += ", ";
            }
            ret += key;
            ret += "=";
            ret += get(key);
        }
        return ret;
    }

    /**
     *
     * @param argNames
     *            the path through nested properties to where we should set the
     *            property
     * @param argVal value to set
     */
    public void put(List<String> argNames, Object argVal) {
        Arguments args = this;
        for (int i = 0; i < argNames.size(); i++) {
            if (i == argNames.size() - 1) {
                args.put(argNames.get(i), argVal);
            } else {
                if (args.get(argNames.get(i)) == null) {
                    args.put(argNames.get(i), new Arguments());
                }
                args = (Arguments) args.get(argNames.get(i));
            }
        }
    }

    /**
    *
    * @param propNames
    *            the path through nested properties to the desired property
    * @return the value of the named property
    */
    public Object get(List<String> propNames) {
        Object val = get(propNames.get(0));
        for (int i = 1; i < propNames.size(); i++) {
            if (val == null) {
                return null;
            }
            val = ((Arguments) val).get(propNames.get(i));
        }
        return val;
    }

    /**
     *
     * @param argSpecs argspecs describing this Arguments
     * @return true iff all argspecs marked as required (and applicable for
     * InterDependentArgSpecs) are present in this arguments
     */
    public boolean allRequiredArgsFilledIn(ArgSpecs argSpecs) {
        for (String argName : argSpecs.keySet()) {
            ArgSpec argSpec = argSpecs.get(argName);
            if ((argSpec.isRequired()) && ((!(argSpec instanceof InterdependentArgSpec)) || (((InterdependentArgSpec) argSpec).applies(this)))) {
                Object argValue = get(argName);
                if (argValue == null) {
                    return false;
                }
                if (argSpec.getArgType() == Type.STRING || argSpec.getArgType() == Type.TEXT) {
                    if (argValue.toString().trim().isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
