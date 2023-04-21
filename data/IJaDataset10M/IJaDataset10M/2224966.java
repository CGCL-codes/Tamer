package nl.romme.tools.metacheck.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * A datastruct to manage/hold all Rules, Violations and Classes. Note that
 * actually 2 struct are maintaned, one for the stylist and one static struct
 * for the meta stylist.
 *
 * @author Johan Romme
 * @version $Revision: 1.1.1.1 $
 */
public class DataStruct {

    private static DataStruct metaStruct = new DataStruct();

    private Collection violations = new Vector();

    private Map categories = new HashMap();

    private Map clazzes = new HashMap();

    private Map packages = new HashMap();

    private Map rules = new HashMap();

    private Map severities = new HashMap();

    /**
     * Create a new DataStruct.
     */
    public DataStruct() {
        super();
    }

    /**
     * Getter for metaStruct.
     *
     * @return Returns the metaStruct.
     */
    public static DataStruct getMetaStruct() {
        return metaStruct;
    }

    /**
     * Getter for categories.
     *
     * @return Returns the categories.
     */
    public Map getCategories() {
        return categories;
    }

    /**
     * Getter for clazzes.
     *
     * @return Returns the clazzes.
     */
    public Map getClazzes() {
        return clazzes;
    }

    /**
     * Getter for packages.
     *
     * @return Returns the packages.
     */
    public Map getPackages() {
        return packages;
    }

    /**
     * Getter for rules.
     *
     * @return Returns the rules.
     */
    public Map getRules() {
        return rules;
    }

    /**
     * Getter for severities.
     *
     * @return Returns the severities.
     */
    public Map getSeverities() {
        return severities;
    }

    /**
     * Getter for violations.
     *
     * @return Returns the violations.
     */
    public Collection getViolations() {
        return violations;
    }

    /**
     * add a claz to the datastruct, if it is not already in, create a
     * new one, if it is already in take that one.
     *
     * @param packName name of the java package
     * @param clazName name of the java class
     *
     * @return the new claz created or the one from the Map
     */
    public Claz addClaz(final String packName, final String clazName) {
        if (this != metaStruct) {
            metaStruct.addClaz(packName, clazName);
        }
        Pack pack = Pack.addPack(packages, packName);
        Claz claz = Claz.addClaz(clazzes, pack, clazName);
        pack.addClaz(claz);
        return claz;
    }

    /**
     * add a claz to the datastruct, if it is not already in, create a
     * new one, if it is already in take that one.
     *
     * @param packName name of the java package
     * @param clazName name of the java class
     * @param author name(s) of the author(s) of this java class
     *
     * @return the new claz created or the one from the datastruct
     */
    public Claz addClaz(final String packName, final String clazName, final String author) {
        if (this != metaStruct) {
            metaStruct.addClaz(packName, clazName, author);
        }
        Claz claz = addClaz(packName, clazName);
        claz.setAuthor(author);
        return claz;
    }

    /**
     * add a rule to the datastruct, if it is not already in, create a
     * new one, if it is already in take that one.
     *
     * @param severityName textual description of the severity
     * @param categoryName textual description of the category
     * @param ruleName name of this rule
     * @param toolName the code checker that check for this rule
     *
     * @return the new claz created or the one from the datastruct
     */
    public Rule addRule(final String severityName, final String categoryName, final String ruleName, final String toolName) {
        if (this != metaStruct) {
            metaStruct.addRule(severityName, categoryName, ruleName, toolName);
        }
        Severity severity = Severity.addSeverity(severities, severityName);
        Category category = Category.addCategory(categories, categoryName);
        Rule rule = Rule.addRule(rules, severity, category, ruleName);
        severity.addRule(rule);
        category.addRule(rule);
        rule.setChecker(toolName);
        return rule;
    }

    /**
     * add a new violation to the datastruct.
     *
     * @param rule the rule that was violated
     * @param claz the class that has the violation
     * @param lineNumber the linenumber where the violation occured
     * @param message optional an additional message
     *
     * @return the violation object created
     */
    public Violation addViolation(final Rule rule, final Claz claz, int lineNumber, final String message) {
        if (this != metaStruct) {
            metaStruct.addViolation((Rule) metaStruct.getRules().get(rule.getName()), (Claz) metaStruct.getClazzes().get(claz.getName()), lineNumber, message);
        }
        Violation violation = new Violation(rule, claz);
        violation.setLineNumber(lineNumber);
        violation.setMessage(message);
        violations.add(violation);
        claz.addViolation(violation);
        rule.addViolation(violation);
        return violation;
    }

    /**
     * add a new violation to the datastruct.
     *
     * @param rule the rule that was violated
     * @param claz the class that has the violation
     * @param lineNumber the linenumber where the violation occured
     * @param columnNumber the columnnumber where the violation occured
     * @param message optional an additional message
     *
     * @return the violation object created
     */
    public Violation addViolation(final Rule rule, final Claz claz, final int lineNumber, final int columnNumber, final String message) {
        if (this != metaStruct) {
            metaStruct.addViolation((Rule) metaStruct.getRules().get(rule.getName()), (Claz) metaStruct.getClazzes().get(claz.getName()), lineNumber, columnNumber, message);
        }
        Violation violation = new Violation(rule, claz);
        violation.setLineNumber(lineNumber);
        violation.setColumnNumber(columnNumber);
        violation.setMessage(message);
        violations.add(violation);
        claz.addViolation(violation);
        rule.addViolation(violation);
        return violation;
    }
}
