package es.iiia.sgi.propertytesters;

import org.eclipse.core.expressions.PropertyTester;
import es.iiia.sgi.editors.RuleEditor;
import es.iiia.shapegrammar.rule.RuleModel;
import es.iiia.shapegrammar.rule.RuleModification;

public class IsRuleModification extends PropertyTester {

    public IsRuleModification() {
    }

    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        if (receiver instanceof RuleEditor) {
            RuleModel rule = (RuleModel) ((RuleEditor) receiver).getModel();
            System.out.println("[IsRuleModification]" + (rule instanceof RuleModification));
            return !(rule instanceof RuleModification);
        }
        return false;
    }
}
