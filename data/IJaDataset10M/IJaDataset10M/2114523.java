package su.msu.cs.lvk.xml2pixy.simple.visitor;

import org.apache.commons.lang.ArrayUtils;
import su.msu.cs.lvk.xml2pixy.ast.python.CallFuncNode;
import su.msu.cs.lvk.xml2pixy.ast.python.GetattrNode;
import su.msu.cs.lvk.xml2pixy.ast.python.NameNode;
import su.msu.cs.lvk.xml2pixy.ast.python.PythonNode;
import su.msu.cs.lvk.xml2pixy.simple.ProcessingUtils;
import su.msu.cs.lvk.xml2pixy.transform.Symbol;

/**
 * User: klimov
 * Date: 13.01.2009
 */
public class NameNodeVisitor extends PythonNodeVisitor {

    public void visit(PythonNode node) {
        NameNode name = (NameNode) node;
        if ((!isUsedInCallFunc(name) || isVariable(name)) && !ArrayUtils.contains(NameNode.BUILTIN_NAMES, name.getName())) {
            name.setName(ProcessingUtils.getFullName(name));
        }
    }

    protected boolean isUsedInCallFunc(NameNode node) {
        PythonNode current = node.getParent();
        if (current instanceof GetattrNode) {
            while (current instanceof GetattrNode) {
                current = current.getParent();
            }
            return current instanceof CallFuncNode;
        } else {
            return false;
        }
    }

    protected boolean isVariable(NameNode node) {
        Symbol sym = node.getScope().getSymbol(node.getName());
        return sym == null || sym.isVariable();
    }
}
