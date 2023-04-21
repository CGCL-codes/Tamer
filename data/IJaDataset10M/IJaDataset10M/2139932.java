package net.sourceforge.pmd.eclipse.runtime.cmd;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.rule.AbstractRuleViolation;

/**
 * This is an implementation of IRuleViolation.
 * It is meant to rebuild a RuleViolation from a PMD Marker.
 * This object is used to generate violation reports.
 * 
 * @author Herlin
 * @author Brian Remedios
 */
class FakeRuleViolation extends AbstractRuleViolation {

    private static final RuleContext DummyContext = new RuleContext();

    /**
     * Default constructor take a rule object to initialize.
     * All other variables have default values to empty;
     * @param rule
     */
    public FakeRuleViolation(Rule theRule) {
        super(theRule, DummyContext, null, null);
    }

    /**
     * @param beginLine The beginLine to set.
     */
    public void setBeginLine(int beginLine) {
        this.beginLine = beginLine;
    }

    /**
     * @param className The className to set.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param endColumn The endColumn to set.
     */
    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    /**
     * @param endLine The endLine to set.
     */
    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    /**
     * @param filename The filename to set.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @param methodName The methodName to set.
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * @param packageName The packageName to set.
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @param variableName The variableName to set.
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}
