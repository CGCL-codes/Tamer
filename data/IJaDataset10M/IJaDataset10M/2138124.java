package net.sourceforge.pmd.rules;

import net.sourceforge.pmd.ast.JavaParserVisitorAdapter;
import net.sourceforge.pmd.ast.ASTAllocationExpression;
import net.sourceforge.pmd.ast.ASTName;
import net.sourceforge.pmd.*;
import java.util.*;

public class DontCreateThreadsRule extends AbstractRule implements Rule {

    public String getDescription() {
        return "Don't create threads, use the ThreadService instead";
    }

    public Object visit(ASTAllocationExpression node, Object data) {
        if ((node.jjtGetChild(0) instanceof ASTName) && ((ASTName) node.jjtGetChild(0)).getImage().equals("Thread")) {
            (((RuleContext) data).getReport()).addRuleViolation(new RuleViolation(this, node.getBeginLine()));
        }
        return super.visit(node, data);
    }
}
