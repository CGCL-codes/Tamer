package org.jmetis.expression.parser;

import javax.el.ELException;
import javax.el.MethodInfo;
import javax.el.PropertyNotWritableException;
import org.jmetis.expression.ELSupport;
import org.jmetis.expression.EvaluationContext;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author$
 */
public abstract class SimpleNode extends ELSupport implements Node {

    protected Node parent;

    protected Node[] children;

    protected int id;

    protected String image;

    public SimpleNode(int i) {
        this.id = i;
    }

    public SimpleNode(ELParser p, int i) {
        this.id = i;
    }

    public void jjtOpen() {
    }

    public void jjtClose() {
    }

    public void jjtSetParent(Node n) {
        this.parent = n;
    }

    public Node jjtGetParent() {
        return this.parent;
    }

    public void jjtAddChild(Node n, int i) {
        if (this.children == null) {
            this.children = new Node[i + 1];
        } else if (i >= this.children.length) {
            Node c[] = new Node[i + 1];
            System.arraycopy(this.children, 0, c, 0, this.children.length);
            this.children = c;
        }
        this.children[i] = n;
    }

    public Node jjtGetChild(int i) {
        return this.children[i];
    }

    public int jjtGetNumChildren() {
        return this.children == null ? 0 : this.children.length;
    }

    @Override
    public String toString() {
        if (this.image != null) {
            return ELParserTreeConstants.jjtNodeName[this.id] + "[" + this.image + "]";
        }
        return ELParserTreeConstants.jjtNodeName[this.id];
    }

    public String toString(String prefix) {
        return prefix + this.toString();
    }

    public void dump(String prefix) {
        System.out.println(this.toString(prefix));
        if (this.children != null) {
            for (int i = 0; i < this.children.length; ++i) {
                SimpleNode n = (SimpleNode) this.children[i];
                if (n != null) {
                    n.dump(prefix + " ");
                }
            }
        }
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Class getType(EvaluationContext ctx) throws ELException {
        throw new UnsupportedOperationException();
    }

    public Object getValue(EvaluationContext ctx) throws ELException {
        throw new UnsupportedOperationException();
    }

    public boolean isReadOnly(EvaluationContext ctx) throws ELException {
        return true;
    }

    public void setValue(EvaluationContext ctx, Object value) throws ELException {
        throw new PropertyNotWritableException("error.syntax.set");
    }

    public void accept(ELParserVisitor visitor) throws Exception {
        visitor.visit(this);
        if (this.children != null && this.children.length > 0) {
            for (Node element : this.children) {
                element.jjtAccept(visitor, null);
            }
        }
    }

    public Object invoke(EvaluationContext ctx, Class[] paramTypes, Object[] paramValues) throws ELException {
        throw new UnsupportedOperationException();
    }

    public MethodInfo getMethodInfo(EvaluationContext ctx, Class[] paramTypes) throws ELException {
        throw new UnsupportedOperationException();
    }
}
