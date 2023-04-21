package org.fudaa.ctulu;

import java.util.HashSet;
import java.util.Set;
import org.nfunk.jep.*;

/**
 * @author Fred Deniger
 * @version $Id: CtuluExprVarCollector.java,v 1.5 2006-09-19 14:36:54 deniger Exp $
 */
public class CtuluExprVarCollector implements ParserVisitor {

    final Set var_ = new HashSet();

    boolean onlyVar_;

    public CtuluExprVarCollector() {
        super();
    }

    public void findOnlyVar(final boolean _onlyVar) {
        onlyVar_ = _onlyVar;
    }

    Variable[] getFoundVar() {
        return (Variable[]) var_.toArray(new Variable[var_.size()]);
    }

    public Object visit(final ASTConstant _node, final Object _data) throws ParseException {
        return null;
    }

    public Object visit(final ASTFunNode _node, final Object _data) throws ParseException {
        return null;
    }

    public Object visit(final ASTStart _node, final Object _data) throws ParseException {
        return null;
    }

    public Object visit(final ASTVarNode _node, final Object _data) throws ParseException {
        if (_node != null && _node.getVar() != null && (!onlyVar_ || !_node.getVar().isConstant())) {
            var_.add(_node.getVar());
        }
        return null;
    }

    public Object visit(final SimpleNode _node, final Object _data) throws ParseException {
        return null;
    }
}
