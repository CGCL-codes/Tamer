package DE.FhG.IGD.semoa.shell;

import java.util.*;

/**
 *
 *
 * @author Jan Peters
 * @version "$Id: ASTListStatement.java 668 2002-07-01 12:53:04Z jpeters $"
 */
public class ASTListStatement extends SimpleNode {

    public ASTListStatement(int id) {
        super(id);
    }

    public Object eval(ShellParser p) {
        StringBuffer strbuf;
        ArrayList arrayList;
        String[] list;
        int n;
        int i;
        arrayList = new ArrayList();
        strbuf = new StringBuffer((String) jjtGetChild(0).eval(p));
        n = jjtGetNumChildren();
        for (i = 1; i < n; i++) {
            if (!jjtGetChild(i - 1).seperated()) {
                strbuf.append((String) jjtGetChild(i).eval(p));
            } else {
                arrayList.add(strbuf.toString());
                strbuf = new StringBuffer((String) jjtGetChild(i).eval(p));
            }
        }
        arrayList.add(strbuf.toString());
        list = (String[]) arrayList.toArray(new String[0]);
        return list;
    }

    public String prettyPrint() {
        StringBuffer str;
        int n;
        int i;
        str = new StringBuffer();
        n = jjtGetNumChildren();
        for (i = 0; i < n; i++) {
            str.append(jjtGetChild(i).prettyPrint());
        }
        str.append("; ");
        return str.toString();
    }
}
