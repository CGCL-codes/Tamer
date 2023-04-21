package de.fhg.igd.semoa.shell;

/**
 *
 *
 * @author Roger Hartmann
 * @version "$Id: ASTWhileStatement.java 1722 2006-04-13 14:57:28Z jpeters $"
 */
public class ASTWhileStatement extends SimpleNode {

    public ASTWhileStatement(int id) {
        super(id);
    }

    public Object eval(ShellParser p) {
        Object ret;
        ret = null;
        while (jjtGetChild(0).eval(p) != null) {
            ret = jjtGetChild(1).eval(p);
        }
        return ret;
    }

    public String prettyPrint() {
        StringBuffer str;
        str = new StringBuffer();
        str.append("while ");
        str.append(jjtGetChild(0).prettyPrint());
        str.append("do ");
        str.append(jjtGetChild(1).prettyPrint());
        str.append("done");
        return str.toString();
    }
}
