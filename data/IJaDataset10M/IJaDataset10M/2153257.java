package src.ptm;

import java.util.ArrayList;

public abstract class PTMFunctionNode extends PTMNode {

    public static final String endExpression = "}}";

    public static final AbortFunction abort;

    static {
        ArrayList<String> al = new ArrayList<String>();
        al.add("}}");
        abort = PTM.createAbortFunction(al);
    }

    public PTMFunctionNode(StringBuffer content, int beginIndex, PTMNode parent, PTMRootNode root) {
        super(content, beginIndex, parent, root);
    }
}
