package su.msu.cs.lvk.xml2pixy.ast.python;

import org.jdom.Element;
import su.msu.cs.lvk.xml2pixy.ast.ASTNode;
import su.msu.cs.lvk.xml2pixy.ast.ListBuilder;
import su.msu.cs.lvk.xml2pixy.transform.Node;
import java.io.PrintStream;
import java.util.List;

/**
 * User: KlimovGA
 * Date: 01.10.2008
 * Time: 22:26:56
 */
public class SliceNode extends PythonNode {

    protected PythonNode expr;

    protected PythonNode upper;

    protected PythonNode lower;

    protected String flags;

    protected SliceNode() {
        super();
    }

    public SliceNode(Element jdom) {
        super(jdom);
        expr = makeNode(getFirst(jdom, "expr"));
        upper = makeNode(getFirst(jdom, "upper"));
        lower = makeNode(getFirst(jdom, "lower"));
        setAsParent(expr, upper, lower);
        flags = jdom.getAttributeValue("flags");
    }

    public SliceNode(Node node) {
        super(node);
        expr = makeNode(getFirst(node, "expr"));
        upper = makeNode(getFirst(node, "upper"));
        lower = makeNode(getFirst(node, "lower"));
        setAsParent(expr, upper, lower);
        flags = node.getJdomElement().getAttributeValue("flags");
    }

    public void print(PrintStream out) {
        expr.print(out);
        out.append('[');
        lower.print(out);
        out.append(':');
        upper.print(out);
        out.append(']');
    }

    public PythonNode getExpr() {
        return expr;
    }

    public PythonNode getUpper() {
        return upper;
    }

    public PythonNode getLower() {
        return lower;
    }

    public String getFlags() {
        return flags;
    }

    public void setExpr(PythonNode expr) {
        this.expr = expr;
        setAsParent(expr);
    }

    public void setUpper(PythonNode upper) {
        this.upper = upper;
        setAsParent(upper);
    }

    public void setLower(PythonNode lower) {
        this.lower = lower;
        setAsParent(lower);
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public List<ASTNode> getChildren() {
        return new ListBuilder<ASTNode>().add(expr).add(lower).add(upper).toList();
    }

    public boolean replace(PythonNode what, PythonNode with) {
        if (what == expr) {
            setExpr(with);
        }
        if (what == lower) {
            setLower(with);
        }
        if (what == upper) {
            setUpper(with);
        }
        return false;
    }
}
