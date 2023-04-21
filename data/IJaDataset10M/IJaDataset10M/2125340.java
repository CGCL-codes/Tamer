package com.google.gwt.resources.css.ast;

/**
 * Represents a CSS at-rule that CssResource is unable to process.
 */
public class CssUnknownAtRule extends CssNode {

    private final String rule;

    public CssUnknownAtRule(String rule) {
        this.rule = rule;
    }

    /**
   * Returns the entire unprocessed at-rule declaration.
   */
    public String getRule() {
        return rule;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    public void traverse(CssVisitor visitor, Context context) {
        if (visitor.visit(this, context)) {
        }
        visitor.endVisit(this, context);
    }
}
