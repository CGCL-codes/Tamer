package su.msu.cs.lvk.xml2pixy.ast.python;

import org.jdom.Element;
import su.msu.cs.lvk.xml2pixy.ast.ASTNode;
import su.msu.cs.lvk.xml2pixy.ast.ListBuilder;
import su.msu.cs.lvk.xml2pixy.transform.Node;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Panther
 * Date: 24.09.2008
 * Time: 22:45:45
 */
public class DictNode extends PythonNode {

    protected List<PythonNode> keys = new ArrayList<PythonNode>();

    protected List<PythonNode> values = new ArrayList<PythonNode>();

    public DictNode(List<PythonNode> keys, List<PythonNode> values) {
        this.keys = keys;
        this.values = values;
        setAsParent(keys);
        setAsParent(values);
    }

    public DictNode(Element jdom) {
        super(jdom);
        boolean key = false;
        for (Object child : jdom.getChild("items").getChildren()) {
            PythonNode newNode = makeNode((Element) child);
            newNode.setParent(this);
            ((key = !key) ? keys : values).add(newNode);
        }
    }

    public DictNode(Node node) {
        super(node);
        boolean key = false;
        for (Node child : node.getChildren("items").get(0).getChildren()) {
            PythonNode newNode = (PythonNode) child.getAstNode();
            newNode.setParent(this);
            ((key = !key) ? keys : values).add(newNode);
        }
    }

    public void print(PrintStream out) {
        boolean first = true;
        out.append('{');
        for (int i = 0; i < keys.size() && i < values.size(); i++) {
            if (!first) out.append(", ");
            first = false;
            keys.get(i).print(out);
            out.append(": ");
            values.get(i).print(out);
        }
        out.append('}');
    }

    public List<ASTNode> getChildren() {
        return new ListBuilder<ASTNode>().add(keys).add(values).toList();
    }

    public void setKeys(List<PythonNode> keys) {
        this.keys = keys;
        setAsParent(keys);
    }

    public void setValues(List<PythonNode> values) {
        this.values = values;
        setAsParent(values);
    }

    public List<PythonNode> getKeys() {
        return keys;
    }

    public List<PythonNode> getValues() {
        return values;
    }

    public boolean replace(PythonNode what, PythonNode with) {
        int i;
        if ((i = keys.indexOf(what)) >= 0) {
            keys.set(i, with);
        } else if ((i = values.indexOf(what)) >= 0) {
            values.set(i, with);
        } else {
            return false;
        }
        setAsParent(with);
        return true;
    }
}
