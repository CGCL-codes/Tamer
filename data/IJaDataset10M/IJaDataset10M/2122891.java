package freemarker.core;

import java.util.*;
import java.io.IOException;
import freemarker.template.*;

/**
 * An instruction representing a switch-case structure.
 */
final class SwitchBlock extends TemplateElement {

    private Case defaultCase;

    private Expression testExpression;

    /**
     * @param testExpression the expression to be tested.
     */
    SwitchBlock(Expression testExpression) {
        this.testExpression = testExpression;
        nestedElements = new LinkedList();
    }

    /**
     * @param cas a Case element.
     */
    void addCase(Case cas) {
        if (cas.isDefault) {
            defaultCase = cas;
        }
        nestedElements.add(cas);
    }

    void accept(Environment env) throws TemplateException, IOException {
        boolean processedCase = false;
        Iterator iterator = nestedElements.iterator();
        try {
            while (iterator.hasNext()) {
                Case cas = (Case) iterator.next();
                boolean processCase = false;
                if (processedCase) {
                    processCase = true;
                } else if (!cas.isDefault) {
                    ComparisonExpression equalsOp = new ComparisonExpression(testExpression, cas.expression, "==");
                    processCase = equalsOp.isTrue(env);
                }
                if (processCase) {
                    env.visit(cas);
                    processedCase = true;
                }
            }
            if (!processedCase && defaultCase != null) {
                env.visit(defaultCase);
            }
        } catch (BreakInstruction.Break br) {
        }
    }

    public String getCanonicalForm() {
        StringBuffer buf = new StringBuffer("<#switch ");
        buf.append(testExpression.getCanonicalForm());
        buf.append(">");
        for (int i = 0; i < nestedElements.size(); i++) {
            Case cas = (Case) nestedElements.get(i);
            buf.append(cas.getCanonicalForm());
        }
        if (defaultCase != null) {
            buf.append(defaultCase.getCanonicalForm());
        }
        buf.append("</#switch>");
        return buf.toString();
    }

    public String getDescription() {
        return "switch " + testExpression;
    }
}
