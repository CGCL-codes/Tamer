package org.vardb.util.dao;

import org.antlr.runtime.tree.CommonTree;
import org.vardb.util.CAntlrHelper;
import org.vardb.util.CException;
import org.vardb.util.CStringHelper;
import org.vardb.util.dao.CFilter.CompositeFilter;
import org.vardb.util.dao.CFilter.ElementaryFilter;
import org.vardb.util.dao.CFilter.FieldOperator;
import org.vardb.util.dao.CFilter.Filter;
import org.vardb.util.dao.CFilter.LogicalOperator;

public class CQueryParser {

    public static Filter parse(String str) {
        try {
            VardbLexer lexer = new VardbLexer(CAntlrHelper.getStringStream(str));
            VardbParser parser = new VardbParser(CAntlrHelper.getTokenStream(lexer));
            VardbParser.expr_return r = parser.expr();
            CommonTree tree = (CommonTree) r.getTree();
            return parseTree(tree);
        } catch (Exception e) {
            throw new CException(e);
        }
    }

    private static Filter parseTree(CommonTree tree) {
        System.out.println(tree.toStringTree());
        Filter filter = handleNode(tree);
        return filter;
    }

    private static Filter handleNode(CommonTree node) {
        System.out.println("node text=" + node.getText() + " type=" + node.getType());
        if (node.getType() == VardbLexer.AND) return handleLogicalNode(node, LogicalOperator.AND); else if (node.getType() == VardbLexer.OR) return handleLogicalNode(node, LogicalOperator.OR); else if (node.getType() == VardbLexer.TERM) return handleTermNode(node);
        throw new CException("no handler for node type " + node.getType() + " [" + node.getText() + "]");
    }

    private static Filter handleLogicalNode(CommonTree node, LogicalOperator operator) {
        System.out.println("handling " + operator + " node, children=" + node.getChildCount());
        if (node.getChildCount() != 2) throw new CException(operator + " node should have two children");
        CommonTree left = CAntlrHelper.getChild(node, 0);
        CommonTree right = CAntlrHelper.getChild(node, 1);
        return new CompositeFilter(handleNode(left), operator, handleNode(right));
    }

    private static Filter handleTermNode(CommonTree node) {
        System.out.println("handling TERM node, children=" + node.getChildCount());
        if (node.getChildCount() != 3) throw new CException("TERM node should have three children");
        String field = CAntlrHelper.getChildText(node, 0);
        FieldOperator operator = FieldOperator.find(CAntlrHelper.getChildText(node, 1));
        String value = CStringHelper.unquote(CAntlrHelper.getChildText(node, 2));
        return new ElementaryFilter(field, operator, value);
    }
}
