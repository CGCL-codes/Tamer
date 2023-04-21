package org.exist.eclipse.xquery.core.internal.parser.visitors;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.dltk.ast.declarations.Argument;
import org.exist.eclipse.xquery.core.internal.parser.XQueryParser;
import org.w3c.xqparser.SimpleNode;
import org.w3c.xqparser.XPathVisitor;

/**
 * Start point for traversing the parameterlist {@link SimpleNode} from parser
 * and distribute the parameter to specific visitor {@link ParameterVisitor}.
 * 
 * @see ParameterVisitor
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ParameterListVisitor implements XPathVisitor, NodeTypes {

    private XQueryParser _parser;

    private List<Argument> _arguments;

    public ParameterListVisitor(XQueryParser parser) {
        _parser = parser;
        _arguments = new ArrayList<Argument>();
    }

    public Object visit(SimpleNode node, Object data) {
        if (PARAM.equals(node.toString())) {
            ParameterVisitor visitor = new ParameterVisitor(_parser);
            node.childrenAccept(visitor, data);
            _arguments.add(visitor.getArgument());
        }
        return data;
    }

    /**
	 * @return <code>true</code> if there exists some {@link Argument}.
	 */
    public boolean hasArguments() {
        return !_arguments.isEmpty();
    }

    /**
	 * @return all created {@link Argument}.
	 */
    public List<Argument> getArguments() {
        return _arguments;
    }
}
