package rsp.library;

import rsp.Node;
import rsp.OptimizeContext;
import rsp.RSPTokenTypes;
import rsp.Type;

/**
 *  The repeat function - create a string out of a repeated string.
 */
public class Repeat extends LibraryFunction implements RSPTokenTypes {

    public Repeat() {
        super("repeat", Type.STRING, new Type[] { Type.STRING, Type.INTEGER });
    }

    public Node optimize(OptimizeContext context) {
        Node str = context.args[0];
        Node count = context.args[1];
        if (str.getType() == STRING && count.getType() == INTEGER) {
            StringBuffer s = new StringBuffer();
            for (int i = 0; i < count.getIntValue(); i++) s.append(str);
            return context.factory.createStringNode(s.toString());
        } else return null;
    }
}
