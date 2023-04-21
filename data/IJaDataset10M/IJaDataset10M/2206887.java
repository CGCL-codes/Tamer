package oext.model.rules.vistors;

import oext.model.rules.AbsoluteAttributeEquality;
import oext.model.rules.AllChildEquality;
import oext.model.rules.EqualityRule;
import oext.model.rules.EqualityRuleVisitor;
import oext.model.rules.OneAttributeEquality;
import oext.model.rules.PcDataEquality;
import oext.model.rules.ThreshholdAttributeEquality;
import oext.model.rules.ThreshholdNodeEquality;

/**
 * This visitor checks whether the rule has a parameter that can be
 * edited
 */
public class HasParameterRuleVisitor implements EqualityRuleVisitor {

    protected boolean ret = false;

    protected HasParameterRuleVisitor() {
    }

    /**
   * visits an AllChildEquality Rule
   * 
   * @param allChildEquality rule that shall be visited
   */
    public void visitAllChildEquality(AllChildEquality allChildEquality) {
        ret = false;
    }

    /**
   * visits an AbsoluteAttributeEquality rule
   * 
   * @param absoluteAttributeEquality rule that shall be visited
   */
    public void visitAbsoluteAttributeEquality(AbsoluteAttributeEquality absoluteAttributeEquality) {
        ret = false;
    }

    /**
   * visits an OneAttributeEquality rule
   * 
   * @param oneAttributeEquality rule that shall be visited
   */
    public void visitOneAttributeEquality(OneAttributeEquality oneAttributeEquality) {
        ret = true;
    }

    /**
   * visits an ThreshholdAttributeEquality rule
   * 
   * @param threshholdAttributeEquality rule that shall be visited
   */
    public void visitThreshholdAttributeEquality(ThreshholdAttributeEquality threshholdAttributeEquality) {
        ret = true;
    }

    /**
   * visits an ThreshholdNodeEquality rule
   * 
   * @param threshholdNodeEquality rule that shall be visited
   */
    public void visitThreshholdNodeEquality(ThreshholdNodeEquality threshholdNodeEquality) {
        ret = true;
    }

    /**
   * visits an PcDataEquality rule
   * 
   * @param pcDataEquality rule that shall be visited
   */
    public void visitPcDataEquality(PcDataEquality pcDataEquality) {
        ret = false;
    }

    public static boolean hasParameter(EqualityRule rule) {
        HasParameterRuleVisitor visitor = new HasParameterRuleVisitor();
        rule.visit(visitor);
        return visitor.ret;
    }
}
