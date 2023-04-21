package org.sosy_lab.common.configuration;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.sosy_lab.common.Pair;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/** This class collects all {@link Option}s of a program. */
public class OptionCollector {

    private static final int CHARS_PER_LINE = 75;

    private static final HashSet<String> errorMessages = new LinkedHashSet<String>();

    private static String sourcePath = "";

    /** The main-method collects all classes of a program and
   * then it searches for all {@link Option}s.
   *
   * @param args use '-v' for verbose output */
    public static void main(final String[] args) {
        boolean verbose = false;
        for (String arg : args) {
            if ("-v".equals(arg) || "-verbose".equals(arg)) {
                verbose = true;
            }
        }
        System.out.println(getCollectedOptions(verbose));
    }

    /** This function collect options from all classes
   * and return a formatted String.
   *
   * @param verbose short or long output? */
    public static String getCollectedOptions(final boolean verbose) {
        sourcePath = getSourcePath();
        final SortedMap<String, Pair<String, String>> map = new TreeMap<String, Pair<String, String>>();
        PrintStream originalStdOut = System.out;
        System.setOut(System.err);
        boolean appendCommonOptions = true;
        for (Class<?> c : getClasses()) {
            collectOptions(c, map, verbose);
            if (c.getPackage().getName().startsWith("org.sosy_lab.common")) {
                appendCommonOptions = false;
            }
        }
        System.setOut(originalStdOut);
        for (String error : errorMessages) {
            System.err.println(error);
        }
        final StringBuilder content = new StringBuilder();
        if (appendCommonOptions) {
            try {
                content.append(Resources.toString(Resources.getResource("org/sosy_lab/common/ConfigurationOptions.txt"), Charsets.UTF_8));
            } catch (Exception e) {
                System.err.println("Could not find options of org.sosy-lab.common classes: " + e.getMessage());
            }
        }
        String description = "";
        for (Pair<String, String> descriptionAndInfo : map.values()) {
            if (descriptionAndInfo.getFirst().isEmpty() || !description.equals(descriptionAndInfo.getFirst())) {
                content.append("\n");
                content.append(descriptionAndInfo.getFirst());
                description = descriptionAndInfo.getFirst();
            }
            content.append(descriptionAndInfo.getSecond());
        }
        return content.toString();
    }

    /** This method tries to get Source-Path. This path is used
   * to get default values for options without instantiating the classes. */
    private static String getSourcePath() {
        Enumeration<URL> resources = getClassLoaderResources();
        while (resources.hasMoreElements()) {
            final File file = new File(resources.nextElement().getFile());
            final String testPath = file.toString().substring(0, file.toString().length() - 3);
            if (new File(testPath + "src/org/sosy_lab").isDirectory()) {
                return testPath + "src/";
            }
        }
        return "";
    }

    /** This function returns the contextClassLoader-Resources. */
    private static Enumeration<URL> getClassLoaderResources() {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        Enumeration<URL> resources = null;
        try {
            resources = classLoader.getResources("");
        } catch (IOException e) {
            System.err.println("Could not get recources of classloader.");
        }
        return resources;
    }

    /** This method collects every {@link Option} of a class.
   *
   * @param c class where to take the Option from
   * @param map map with collected Options
   * @param verbose short or long output? */
    private static void collectOptions(final Class<?> c, final SortedMap<String, Pair<String, String>> map, final boolean verbose) {
        for (final Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Option.class)) {
                getOptionsDescription(c, map);
                final String optionName = getOptionName(c, field);
                final String defaultValue = getDefaultValue(field);
                final StringBuilder optionInfo = new StringBuilder();
                optionInfo.append(optionName);
                if (verbose) {
                    optionInfo.append("\n  field:    " + field.getName() + "\n");
                    optionInfo.append("  class:    " + field.getDeclaringClass().toString().substring(6) + "\n");
                    optionInfo.append("  type:     " + field.getType().getSimpleName() + "\n");
                    optionInfo.append("  default value: ");
                    if (!defaultValue.isEmpty()) {
                        optionInfo.append(defaultValue);
                    } else {
                        optionInfo.append("not available");
                    }
                } else {
                    if (!defaultValue.isEmpty()) {
                        optionInfo.append(" = " + defaultValue);
                    } else {
                        optionInfo.append(" = no default value");
                    }
                }
                optionInfo.append("\n");
                optionInfo.append(getAllowedValues(field, verbose));
                if (map.containsKey(optionName)) {
                    Pair<String, String> oldValues = map.get(optionName);
                    String description = getOptionDescription(field);
                    if (!description.equals(oldValues.getFirst())) {
                        description += oldValues.getFirst();
                    }
                    String commonOptionInfo = optionInfo.toString();
                    if (!commonOptionInfo.equals(oldValues.getSecond())) {
                        commonOptionInfo += oldValues.getSecond();
                    }
                    map.put(optionName, Pair.of(description, commonOptionInfo));
                } else {
                    map.put(optionName, Pair.of(getOptionDescription(field), optionInfo.toString()));
                }
            }
        }
    }

    /** This function returns the formatted description of an {@link Option}.
   *
   * @param field field with the option */
    private static String getOptionDescription(final Field field) {
        final Option option = field.getAnnotation(Option.class);
        String text = option.description();
        if (field.getAnnotation(Deprecated.class) != null) {
            text = "DEPRECATED: " + text;
        }
        return formatText(text);
    }

    /** This function adds the formatted description of {@link Options}
   * to the map, if a prefix is defined.
   *
   * @param c class with options
   * @param map where the formatted options-description is added */
    private static void getOptionsDescription(final Class<?> c, final SortedMap<String, Pair<String, String>> map) {
        if (c.isAnnotationPresent(Options.class)) {
            final Options classOption = c.getAnnotation(Options.class);
            if (!classOption.prefix().isEmpty() && !classOption.description().isEmpty()) {
                map.put(classOption.prefix(), Pair.of(formatText(classOption.description()), ""));
            }
        }
    }

    /** This function formats text and splits lines, if they are too long.
   * This functions adds "#" before each line.*/
    public static String formatText(final String text) {
        return formatText(text, "# ", true);
    }

    /** This function formats text and splits lines, if they are too long. */
    public static String formatText(final String text, final String lineStart, final boolean useLineStartInFirstLine) {
        if (text.isEmpty()) {
            return text;
        }
        final String[] lines = text.split("\n");
        final List<String> splittedLines = new ArrayList<String>();
        for (String line : lines) {
            while (line.length() > CHARS_PER_LINE) {
                int spaceIndex = line.lastIndexOf(" ", CHARS_PER_LINE);
                if (spaceIndex == -1) {
                    spaceIndex = line.indexOf(" ");
                }
                if (spaceIndex == -1) {
                    spaceIndex = line.length() - 1;
                }
                final String start = line.substring(0, spaceIndex);
                if (!start.isEmpty()) {
                    splittedLines.add(start);
                }
                line = line.substring(spaceIndex + 1);
            }
            splittedLines.add(line);
        }
        if (splittedLines.get(splittedLines.size() - 1).isEmpty()) {
            splittedLines.remove(splittedLines.size() - 1);
        }
        StringBuilder formattedLines = new StringBuilder();
        if (!useLineStartInFirstLine && splittedLines.size() > 0) {
            formattedLines.append(splittedLines.remove(0));
            formattedLines.append('\n');
        }
        for (String line : splittedLines) {
            formattedLines.append(lineStart);
            formattedLines.append(line);
            formattedLines.append('\n');
        }
        return formattedLines.toString();
    }

    /** This function returns the name of an {@link Option}.
   * If no optionname is defined, the name of the field is returned.
   * If a prefix is defined, it is added in front of the name.
   *
   * @param c class with the field
   * @param field field with the option */
    private static String getOptionName(final Class<?> c, final Field field) {
        String optionName = "";
        if (c.isAnnotationPresent(Options.class)) {
            final Options classOption = c.getAnnotation(Options.class);
            if (!classOption.prefix().isEmpty()) {
                optionName += classOption.prefix() + ".";
            }
        }
        final Option option = field.getAnnotation(Option.class);
        if (option.name().isEmpty()) {
            optionName += field.getName();
        } else {
            optionName += option.name();
        }
        return optionName;
    }

    /** This function searches for the default field value of an {@link Option}
   * in the sourcefile of the actual field/class and returns it
   * or an emtpy String, if the value not found.
   *
   * This part only works, if you have the source code.
   *
   * @param field where to get the default value */
    private static String getDefaultValue(final Field field) {
        final String content = getContentOfFile(field);
        String fieldString = Modifier.toString(field.getModifiers());
        String genericType = field.getGenericType().toString();
        if (genericType.matches(".*<.*>")) {
            genericType = genericType.replaceAll("^[^<]*\\.", "");
            genericType = genericType.replaceAll("<[^\\?][^<]*\\.", "<");
            genericType = genericType.replaceAll("<\\?[^<]*\\.", "<\\\\?\\\\s+extends\\\\s+");
            fieldString += "\\s+" + genericType;
        } else {
            fieldString += "\\s+" + field.getType().getSimpleName();
        }
        fieldString += "\\s+" + field.getName();
        String defaultValue = getDefaultValueFromContent(content, fieldString);
        if (field.getType().isEnum()) {
            if (defaultValue.isEmpty()) {
                String type = field.getType().toString();
                type = type.substring(type.lastIndexOf(".") + 1).replace("$", ".");
                fieldString = Modifier.toString(field.getModifiers()) + "\\s+" + type + "\\s+" + field.getName();
                defaultValue = getDefaultValueFromContent(content, fieldString);
            }
            if (defaultValue.contains(".")) {
                defaultValue = defaultValue.substring(defaultValue.lastIndexOf(".") + 1);
            }
        }
        if (defaultValue.equals("null")) {
            defaultValue = "";
        }
        return defaultValue;
    }

    /** This function returns the content of a sourcefile as String.
   *
   * @param field the field, the sourcefile belongs to */
    private static String getContentOfFile(final Field field) {
        String filename = field.getDeclaringClass().toString().substring(6).replace(".", "/");
        if (filename.contains("$")) {
            filename = filename.substring(0, filename.indexOf("$"));
        }
        filename = sourcePath + filename + ".java";
        try {
            return com.google.common.io.Files.toString(new File(filename), Charset.defaultCharset());
        } catch (IOException e) {
            errorMessages.add("INFO: Could not read sourcefiles " + "for getting the default values.");
            return "";
        }
    }

    /** This function searches for fieldstring in content and
   * returns the value of the field.
   *
   * @param content sourcecode where to search
   * @param fieldString name of the field, which value is returned */
    private static String getDefaultValueFromContent(final String content, final String fieldPattern) {
        String defaultValue = "";
        String[] splitted = content.split(fieldPattern);
        if (splitted.length > 1) {
            final String rest = splitted[1];
            defaultValue = rest.substring(0, rest.indexOf(";")).trim();
            if (defaultValue.startsWith("=")) {
                defaultValue = defaultValue.substring(1).trim();
                while (defaultValue.contains("/*")) {
                    defaultValue = defaultValue.substring(0, defaultValue.indexOf("/*")) + defaultValue.substring(defaultValue.indexOf("*/") + 2);
                }
                if (defaultValue.contains("//")) {
                    defaultValue = defaultValue.substring(0, defaultValue.indexOf("//"));
                }
                if (defaultValue.startsWith("new File(")) {
                    defaultValue = defaultValue.substring("new File(".length(), defaultValue.length() - 1);
                }
                if (defaultValue.startsWith("ImmutableSet.of(")) {
                    defaultValue = "{" + defaultValue.substring("ImmutableSet.of(".length(), defaultValue.length() - 1) + "}";
                }
            }
        } else {
            final String stringSetFieldPattern = fieldPattern.replace("\\s+Set\\s+", "\\s+Set<String>\\s+");
            if (content.contains(stringSetFieldPattern)) {
                return getDefaultValueFromContent(content, stringSetFieldPattern);
            }
        }
        return defaultValue.trim();
    }

    /** This function returns the allowed values or interval for a field.
   *
   * @param field field with the {@link Option}-annotation
   * @param verbose short or long output? */
    private static String getAllowedValues(final Field field, final boolean verbose) {
        String allowedValues = "";
        final Class<?> type = field.getType();
        if (type.isEnum()) {
            try {
                final Field[] enums = Class.forName(type.toString().substring(6)).getFields();
                final String[] enumTitles = new String[enums.length];
                for (int i = 0; i < enums.length; i++) {
                    enumTitles[i] = enums[i].getName();
                }
                allowedValues = "  enum:     " + formatText(java.util.Arrays.toString(enumTitles), "             ", false);
            } catch (ClassNotFoundException e) {
            }
        }
        allowedValues += getOptionValues(field, verbose);
        allowedValues += getClassOptionValues(field, verbose);
        allowedValues += getFileOptionValues(field, verbose);
        allowedValues += getIntegerOptionValues(field, verbose);
        allowedValues += getTimeSpanOptionValues(field, verbose);
        return allowedValues;
    }

    /** This method returns text representing the values,
   * that are defined in the {@link Option}-annotation. */
    private static String getOptionValues(Field field, boolean verbose) {
        final Option option = field.getAnnotation(Option.class);
        assert option != null;
        String str = "";
        if (option.values().length != 0) {
            str += "  allowed values: " + java.util.Arrays.toString(option.values()) + "\n";
        }
        if (verbose && !option.regexp().isEmpty()) {
            str += "  regexp:   " + option.regexp() + "\n";
        }
        if (verbose && option.toUppercase()) {
            str += "  uppercase: true\n";
        }
        return str;
    }

    /** This method returns text representing the values,
   * that are defined in the {@link ClassOption}-annotation. */
    private static String getClassOptionValues(Field field, boolean verbose) {
        final ClassOption classOption = field.getAnnotation(ClassOption.class);
        String str = "";
        if (classOption != null) {
            if (verbose && !classOption.packagePrefix().isEmpty()) {
                str += "  packagePrefix: " + classOption.packagePrefix() + "\n";
            }
        }
        return str;
    }

    /** This method returns text representing the values,
   * that are defined in the {@link FileOption}-annotation. */
    private static String getFileOptionValues(Field field, boolean verbose) {
        final FileOption fileOption = field.getAnnotation(FileOption.class);
        String str = "";
        if (fileOption != null) {
            if (verbose) {
                str += "  type of file: " + fileOption.value() + "\n";
            }
        }
        return str;
    }

    /** This method returns text representing the values,
   * that are defined in the {@link IntegerOption}-annotation. */
    private static String getIntegerOptionValues(Field field, boolean verbose) {
        final IntegerOption intOption = field.getAnnotation(IntegerOption.class);
        String str = "";
        if (intOption != null) {
            if (verbose) {
                if (intOption.min() == Long.MIN_VALUE) {
                    str += "  min:      Long.MIN_VALUE\n";
                } else {
                    str += "  min:      " + intOption.min() + "\n";
                }
                if (intOption.max() == Long.MAX_VALUE) {
                    str += "  max:      Long.MAX_VALUE\n";
                } else {
                    str += "  max:      " + intOption.max() + "\n";
                }
            }
        }
        return str;
    }

    /** This method returns text representing the values,
   * that are defined in the {@link TimeSpanOption}-annotation. */
    private static String getTimeSpanOptionValues(Field field, boolean verbose) {
        final TimeSpanOption timeSpanOption = field.getAnnotation(TimeSpanOption.class);
        String str = "";
        if (timeSpanOption != null) {
            if (verbose) {
                str += "  code unit:     " + timeSpanOption.codeUnit() + "\n";
                str += "  default unit:  " + timeSpanOption.defaultUserUnit() + "\n";
                if (timeSpanOption.min() == Long.MIN_VALUE) {
                    str += "  time min:      Long.MIN_VALUE\n";
                } else {
                    str += "  time min:      " + timeSpanOption.min() + "\n";
                }
                if (timeSpanOption.max() == Long.MAX_VALUE) {
                    str += "  time max:      Long.MAX_VALUE\n";
                } else {
                    str += "  time max:      " + timeSpanOption.max() + "\n";
                }
            }
        }
        return str;
    }

    /**
   * Collects all classes accessible from the context class loader which
   * belong to the given package and subpackages.
   *
   * @return list of classes
   */
    private static List<Class<?>> getClasses() {
        Enumeration<URL> resources = getClassLoaderResources();
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        while (resources.hasMoreElements()) {
            final File file = new File(resources.nextElement().getFile());
            collectClasses(file, "", classes);
        }
        return classes;
    }

    /**
   * Recursive method used to find all classes in a given directory and subdirs.
   *
   * @param directory the base directory
   * @param packageName the package name for classes found inside the base directory
   * @param classes list where the classes are added.
   */
    private static void collectClasses(final File directory, final String packageName, final List<Class<?>> classes) {
        if (directory.exists()) {
            final File[] files = directory.listFiles();
            Arrays.sort(files);
            for (final File file : files) {
                final String fileName = file.getName();
                if (file.isDirectory() && !fileName.startsWith(".svn")) {
                    String newPackage = packageName.isEmpty() ? fileName : (packageName + "." + fileName);
                    collectClasses(file, newPackage, classes);
                } else if (fileName.endsWith(".class")) {
                    final String nameOfClass = packageName + '.' + fileName.substring(0, fileName.length() - 6);
                    try {
                        final Class<?> foundClass = Class.forName(nameOfClass);
                        if (!Modifier.isInterface(foundClass.getModifiers())) {
                            classes.add(foundClass);
                        }
                    } catch (ClassNotFoundException e) {
                    } catch (UnsatisfiedLinkError e) {
                        errorMessages.add("INFO: Could not load '" + fileName + "' for getting Option-annotations: " + e.getMessage());
                    } catch (NoClassDefFoundError e) {
                        return;
                    }
                }
            }
        }
    }
}
