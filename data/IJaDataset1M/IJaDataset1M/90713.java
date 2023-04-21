package su.msu.cs.lvk.xml2pixy.simple.visitor;

import org.apache.log4j.Logger;
import su.msu.cs.lvk.xml2pixy.ast.python.*;
import su.msu.cs.lvk.xml2pixy.simple.ProcessingUtils;
import su.msu.cs.lvk.xml2pixy.simple.classes.PyClass;
import su.msu.cs.lvk.xml2pixy.transform.Symbol;

/**
 * User: klimov
 * Date: 13.01.2009
 */
public class CallFuncNodeVisitor extends PythonNodeVisitor {

    private static final Logger logger = Logger.getLogger(CallFuncNodeVisitor.class);

    public void visit(PythonNode node) {
        CallFuncNode callFunc = (CallFuncNode) node;
        PythonNode callee = callFunc.getNode();
        if (callee instanceof GetattrNode) {
            GetattrNode calleeGetattr = (GetattrNode) callee;
            PythonNode obj = calleeGetattr.getExpr();
            if (obj.getClass() == NameNode.class) {
                NameNode name = (NameNode) obj;
                Symbol symbol = ProcessingUtils.trySymbol(node.getScope(), name.getName());
                if (symbol != null && symbol.isGenerator()) {
                    name.setName(ProcessingUtils.getFullName(name));
                    FunctionNode generatorFunction = (FunctionNode) symbol.getSource();
                    String fullGeneratorName = ProcessingUtils.getFullName(generatorFunction);
                    callFunc.getArgs().add(name);
                    callFunc.setNode(new NameNode(ProcessingUtils.concatMangled(fullGeneratorName, ProcessingUtils.GENERATOR_NEXT)).copyLocation(node));
                    return;
                }
            }
            callFunc.getArgs().add(0, obj);
            callFunc.setNode(new NameNode(ProcessingUtils.getMethodFullName(calleeGetattr.getAttrName(), callFunc.getArgs().size())).copyLocation(node));
        } else if (callee instanceof NameNode) {
            NameNode name = (NameNode) callee;
            Symbol symbol = ProcessingUtils.trySymbol(node.getScope(), name.getName());
            if (symbol != null) {
                if (symbol.isClass()) {
                    name.setName(ProcessingUtils.concatMangled(PyClass.CONSTRUCTOR_NAME, ProcessingUtils.getFullName((ClassNode) symbol.getSource())));
                } else if (symbol.isFunction() || symbol.isGeneratorFunction()) {
                    if (!name.getName().equals(FunctionNode.EVAL)) {
                        name.setName(ProcessingUtils.getFullName((FunctionNode) symbol.getSource()));
                    }
                }
            }
        } else {
            logger.warn("Calls are supported only for obj() and obj.meth() (" + callFunc.getLocation() + ")");
        }
    }
}
