package ie.iqda.documentmanipulation;

import ie.iqda.datastructures.PersistentMap;
import ie.iqda.io.ErrorLog;
import ie.iqda.io.TextFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Performs anonymization on transcripts and other text files etc.
 *
 * The data anonymizer works by taking in two files:
 * 1. A file of Regular Expressions (RegEx)
 * 2. A list of name mappings.
 *
 * The RegEx file contains rules like:
 * %t -> %r
 * %t's -> %r's
 *
 * And the name mapping file contains rules like:
 * Keith -> John
 * Robert -> Frederick
 *
 * A Cartesian product is then taken of the two files to create a list of
 * all the replacements to be applied. In this case:
 * Keith -> John
 * Keith's -> John's
 * Robert -> Frederick
 * Robert's -> Frederick's
 *
 * The %t corresponds to the first column and %r to the latter.
 *
 * NOTE: Java RegExs are used so certain symbols require escaping, such as the
 * period which alone has special meaning in RegExs. To ignore case in RegExs,
 * add (?i) to the start of the appropriate rules in the %t column.
 *
 * @author Keith Ó Dúlaigh <keith@keithodulaigh.com>
 * @version Jan 29 2011
 */
public class DataAnonymizer extends DocumentTransformer {

    private PersistentMap regex;

    private PersistentMap names;

    private Map replacements;

    /**
     * Constructs a new data anonymizer.
     * 
     * @param regexFile
     * @param nameFile
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public DataAnonymizer(String regexFile, String nameFile) {
        replacements = new HashMap<String, String>();
        setRegExFile(regexFile);
        setNameFile(nameFile);
        listReplacements();
    }

    /**
     * Gets the name of the file that contains the regular expressions.
     * 
     * @return
     */
    public String getRegExFile() {
        return regex.getFileName();
    }

    /**
     * Sets the file where the regular expressions are stored.
     *
     * @param file
     */
    private void setRegExFile(String file) {
        regex = new PersistentMap(file);
    }

    /**
     * Gets the name of the file that stores the name mappings.
     * 
     * @return
     */
    public String getNameFile() {
        return names.getFileName();
    }

    /**
     * Sets the file that contains the name mappings.
     * 
     * @param file
     */
    private void setNameFile(String file) {
        names = new PersistentMap(file);
    }

    /**
     * Transforms the input file by applying anonymization.
     * 
     * @param input
     * @param output
     */
    @Override
    public void transform(String input, String output) {
        String file = new TextFile(input).read();
        String comparison = new TextFile(input).read();
        String tag = "REPLACEMENT_" + System.currentTimeMillis();
        TreeSet<String> allNames = new TreeSet<String>(replacements.keySet());
        Pattern pattern;
        Matcher outMatcher;
        Matcher comparisonMatcher;
        try {
            String name = allNames.last();
            for (int index = allNames.size() - 1; index >= 0; index--) {
                if (name.startsWith("(") && !name.startsWith("(?i)")) {
                    continue;
                }
                pattern = Pattern.compile(name, Pattern.MULTILINE);
                outMatcher = pattern.matcher(file);
                comparisonMatcher = pattern.matcher(comparison);
                file = outMatcher.replaceAll(tag + replacements.get(name).toString());
                comparison = comparisonMatcher.replaceAll("{" + name + "|" + replacements.get(name).toString() + "}");
                if (index != 0) {
                    name = allNames.headSet(name).last();
                }
            }
        } catch (NoSuchElementException error) {
            ErrorLog.instance().addEntry(error);
        } catch (IllegalArgumentException error) {
            ErrorLog.instance().addEntry(error);
        }
        file = file.replaceAll(tag, "");
        comparison.replaceAll(tag, "");
        pattern = Pattern.compile("##([\\w]+)");
        outMatcher = pattern.matcher(file);
        file = outMatcher.replaceAll("##" + System.getProperty("line.separator") + "\t$1");
        pattern = Pattern.compile("##}([\\w]+)");
        comparisonMatcher = pattern.matcher(comparison);
        comparison = comparisonMatcher.replaceAll("##}" + System.getProperty("line.separator") + "\t$1");
        new TextFile(output).write(file, false);
        new TextFile(output + ".cmp.txt").write(comparison, false);
    }

    /**
     * Constructs an appropriate replacement rule for each of the
     * regular expressions applied to each of the names.
     */
    public void listReplacements() {
        Iterator<String> nameIterator = names.keySet().iterator();
        String currentRegex = new String();
        String currentName = new String();
        while (nameIterator.hasNext()) {
            Iterator<String> regexIterator = regex.keySet().iterator();
            currentName = nameIterator.next();
            while (regexIterator.hasNext()) {
                currentRegex = regexIterator.next();
                if (names.get(currentName).toString().length() < 1 || names.get(currentName).toString().equalsIgnoreCase(" ")) {
                    continue;
                }
                String currentNameWithNewLines = currentName.trim().replaceAll(" ", "[\\\\s]{1,2}").trim();
                replacements.put(currentRegex.replace("%t", currentNameWithNewLines), ((String) regex.get(currentRegex)).replaceAll("%r", (String) names.get(currentName)).trim());
            }
        }
    }

    /**
     * Gets a string representation of the object.
     * 
     * @return
     */
    @Override
    public String toString() {
        return replacements.toString();
    }
}
