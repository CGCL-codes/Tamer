package su.msu.cs.lvk.xml2pixy.ast.python;

import org.jdom.Element;
import su.msu.cs.lvk.xml2pixy.ast.ASTNode;
import su.msu.cs.lvk.xml2pixy.ast.ListBuilder;
import su.msu.cs.lvk.xml2pixy.transform.Node;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Panther
 * Date: 24.09.2008
 * Time: 23:03:12
 */
public class ExecNode extends PythonNode {

    protected PythonNode expr;

    protected PythonNode locals;

    protected PythonNode globals;

    public ExecNode(Element jdom) {
        super(jdom);
        expr = makeNode((Element) jdom.getChild("expr").getChildren().get(0));
        expr.setParent(this);
        List localsList = jdom.getChild("locals").getChildren();
        if (!localsList.isEmpty()) {
            locals = makeNode((Element) jdom.getChild("locals").getChildren().get(0));
        }
        List globalsList = jdom.getChild("globals").getChildren();
        if (!globalsList.isEmpty()) {
            globals = makeNode((Element) jdom.getChild("globals").getChildren().get(0));
        }
        setAsParent(locals, globals);
    }

    public ExecNode(Node node) {
        super(node);
        expr = (PythonNode) node.getChildren("expr").get(0).getChildren().get(0).getAstNode();
        expr.setParent(this);
        locals = (PythonNode) node.getChildren("locals").get(0).getChildren().get(0).getAstNode();
        locals.setParent(this);
        globals = (PythonNode) node.getChildren("globals").get(0).getChildren().get(0).getAstNode();
        globals.setParent(this);
    }

    public void print(PrintStream out) {
        out.append("exec ");
        if (expr != null) expr.print(out);
        if (locals != null) {
            out.append(" in ");
            locals.print(out);
        }
        if (globals != null) {
            out.append(", ");
            globals.print(out);
        }
    }

    public List<ASTNode> getChildren() {
        return new ListBuilder<ASTNode>().add(expr).add(locals).add(globals).toList();
    }

    public PythonNode getExpr() {
        return expr;
    }

    public void setExpr(PythonNode expr) {
        this.expr = expr;
        setAsParent(expr);
    }

    public PythonNode getLocals() {
        return locals;
    }

    public void setLocals(PythonNode locals) {
        this.locals = locals;
        setAsParent(locals);
    }

    public PythonNode getGlobals() {
        return globals;
    }

    public void setGlobals(PythonNode globals) {
        this.globals = globals;
        setAsParent(globals);
    }
}
