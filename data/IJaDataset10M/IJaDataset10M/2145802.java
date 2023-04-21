package su.msu.cs.lvk.xml2pixy.transform.pyvisitor;

import at.ac.tuwien.infosys.www.phpparser.ParseNode;
import at.ac.tuwien.infosys.www.phpparser.PhpSymbols;
import org.apache.log4j.Logger;
import su.msu.cs.lvk.xml2pixy.ast.python.*;
import su.msu.cs.lvk.xml2pixy.simple.classes.Method;
import su.msu.cs.lvk.xml2pixy.simple.visitor.ListComprehensionVisitor;
import su.msu.cs.lvk.xml2pixy.transform.astvisitor.VisitorException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: klimov
 * Date: 19.01.2009
 */
public class CallFuncNodeVisitor extends PythonNodeVisitor {

    private static final Logger logger = Logger.getLogger(CallFuncNodeVisitor.class);

    public void visit(PythonNode node) throws VisitorException {
        CallFuncNode callFunc = (CallFuncNode) node;
        if (callFunc.getNode() instanceof NameNode) {
            NameNode callee = (NameNode) callFunc.getNode();
            if (callee.getName().startsWith(Method.DISPATCHER_PREFIX + ListNode.APPEND_METHOD) && !callFunc.getArgs().isEmpty()) {
                PythonNode arg0 = callFunc.getArgs().get(0);
                PythonNode arg1 = callFunc.getArgs().get(1);
                if (arg0.getPhpNode() != null && arg1.getPhpNode() != null) {
                    if (arg0.getClass() == NameNode.class) {
                        NameNode name = (NameNode) arg0;
                        if (name.getName().endsWith(ListComprehensionVisitor.LIST_COMP_RESULT)) {
                            ParseNode refVar = helper.create(PhpSymbols.reference_variable, helper.create(PhpSymbols.reference_variable, helper.create(PhpSymbols.compound_variable, helper.create(PhpSymbols.T_VARIABLE, "$" + name.getName(), node.getLineno()))), helper.create(PhpSymbols.T_OPEN_RECT_BRACES, "[", node.getLineno()), helper.create(PhpSymbols.dim_offset, makeEpsilon()), helper.create(PhpSymbols.T_CLOSE_RECT_BRACES, "]", node.getLineno()));
                            ParseNode stmt = helper.create(PhpSymbols.unticked_statement, helper.create(PhpSymbols.expr, helper.create(PhpSymbols.expr_without_variable, makeCvar(refVar), helper.create(PhpSymbols.T_ASSIGN, "=", node.getLineno()), makeExpr(callFunc.getArgs().get(1).getPhpNode()))), helper.create(PhpSymbols.T_SEMICOLON, ";", node.getLineno()));
                            callFunc.setPhpNode(helper.create(PhpSymbols.statement, stmt));
                        }
                    }
                }
            }
            if (callFunc.getPhpNode() == null) {
                if (callee.getName().equals(FunctionNode.EVAL)) {
                    callFunc.setPhpNode(helper.create(PhpSymbols.expr, helper.create(PhpSymbols.expr_without_variable, helper.create(PhpSymbols.internal_functions_in_yacc, helper.create(PhpSymbols.T_EVAL, "eval", callFunc.getLineno()), helper.create(PhpSymbols.T_OPEN_BRACES, "(", callFunc.getLineno()), makeExpr(callFunc.getArgs().get(0).getPhpNode()), helper.create(PhpSymbols.T_CLOSE_BRACES, ")", callFunc.getLineno())))));
                }
            }
            if (callFunc.getPhpNode() == null) {
                List<ParseNode> args = new ArrayList<ParseNode>();
                for (PythonNode arg : callFunc.getArgs()) {
                    if (arg.getPhpNode() != null) {
                        args.add(prepareArg(arg.getPhpNode()));
                    } else {
                        args.add(helper.create(PhpSymbols.expr_without_variable, helper.create(PhpSymbols.scalar, helper.create(PhpSymbols.T_STRING, "null", arg.getLineno()))));
                    }
                }
                callFunc.setPhpNode(makeFunctionCall(callee.getName(), args.toArray(new ParseNode[args.size()]), null, callFunc.getLineno()));
            }
        } else {
            logger.warn("Calls are supported only for obj() and obj.meth() (" + callFunc.getLocation() + ")");
        }
    }

    protected ParseNode prepareArg(ParseNode arg) {
        if (arg.getSymbol() == PhpSymbols.expr) {
            arg = arg.getChild(0);
        }
        return makeCvar(arg);
    }
}
