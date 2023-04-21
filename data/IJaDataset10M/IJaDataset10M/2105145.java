package net.rptools.common.expression.function;

import net.rptools.common.expression.Result;
import net.rptools.common.expression.RunData;
import org.lsmp.djep.xjep.CommandVisitorI;
import org.lsmp.djep.xjep.XJep;
import org.nfunk.jep.ASTConstant;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

public class Attack extends PostfixMathCommand implements CommandVisitorI {

    public Node process(Node node, Node[] children, XJep xjep) throws ParseException {
        int threat = 20;
        try {
            if (children != null) {
                switch(children.length) {
                    case 1:
                        threat = evaluateNodeValue(children[0], xjep).intValue();
                        break;
                    default:
                        throw new ParseException("Illegal number of arguments");
                }
            }
            RunData data = RunData.getCurrent();
            Result result = data.getResult();
            int roll = data.randomInt(1, 20);
            if (roll == 1) {
                result.setDescription("miss");
            } else if (roll == 20) {
                result.setDescription("hit/threat");
            } else if (roll >= threat) {
                result.setDescription("threat");
            }
            return xjep.getNodeFactory().buildConstantNode(roll);
        } catch (ParseException e) {
            throw e;
        } catch (Exception e) {
            throw new ParseException(e.getMessage());
        }
    }

    private Number evaluateNodeValue(Node node, XJep jep) throws Exception {
        if (node instanceof ASTConstant) {
            return (Number) ((ASTConstant) node).getValue();
        } else {
            return (Number) jep.evaluate(node);
        }
    }
}
