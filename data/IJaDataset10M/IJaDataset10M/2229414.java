package com.enerjy.analyzer.java;

import org.eclipse.jdt.core.dom.ASTNode;
import com.enerjy.analyzer.FixData;
import com.enerjy.analyzer.IProblem;

/**
 * An implementation of IProblem for Java rule violations.
 */
public class JavaProblem implements IProblem {

    private final String ruleKey;

    private final ASTNode node;

    private final String message;

    private String suppressText;

    private final FixData fixData = new FixData();

    JavaProblem(String ruleKey, ASTNode node, String message) {
        this.ruleKey = ruleKey;
        this.node = node;
        this.message = message;
    }

    public String getRuleKey() {
        return ruleKey;
    }

    public String getMessage() {
        return message;
    }

    public FixData getFixData() {
        return fixData;
    }

    /**
     * @return The AST node that exhibits the problem.
     */
    public ASTNode getNode() {
        return node;
    }

    /**
     * Returns the string that can be used as an argument to a SuppressWarnings annotation to suppress this warning, or null
     * if this warning cannot be suppressed with SuppressWarnings. Note that relatively few rules can be suppressed in this
     * way since we do not add new SuppressWarnings values to those that Eclipse already supports. All rules can still be
     * suppressed with //ESCA comments; we provide this as an alternative to remove the need to provide two escapements for
     * problems that both Eclipse and Enerjy can find.
     *   
     * @return The string that can be used as an argument to an SuppressWarnings annotation to suppress this warning, or
     * null if the warning cannot be suppressed with SuppressWarnings. 
     */
    public String getSuppressText() {
        return suppressText;
    }

    /**
     * Set the string that can be used as an argument to SuppressWarnings to suppress this problem.
     * @param suppressText The argument to SuppressWarnings to suppress this problem.
     */
    public void setSuppressText(String suppressText) {
        this.suppressText = suppressText;
    }

    /**
     * Add data for fixing this problem.
     * 
     * @param key Key of data being added.
     * @param value Data to add.
     */
    public void addFixData(String key, String value) {
        fixData.put(key, value);
    }

    @Override
    public String toString() {
        return node.getStartPosition() + ": " + message;
    }
}
