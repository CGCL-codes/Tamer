package org.sinaxe.context;

import org.jaxen.Function;
import org.jaxen.Context;
import org.jaxen.FunctionCallException;
import java.util.List;
import java.util.ArrayList;

public class XPathElementFunction extends SinaxeXPathFunction {

    public static final String name = "element";

    Context context = null;

    ArrayList args = new ArrayList();

    public XPathElementFunction() {
    }

    public String getName() {
        return name;
    }

    public Object call(Context context, List args) throws FunctionCallException {
        checkArgs(args, 1, 3);
        Function stringFunction = getFunction(context, "string");
        String uri = null;
        String qname;
        Object content = null;
        if (args.size() == 1) {
            qname = (String) stringFunction.call(context, args.subList(0, 1));
        } else if (args.size() == 2) {
            qname = (String) stringFunction.call(context, args.subList(0, 1));
            content = args.get(1);
        } else {
            uri = (String) stringFunction.call(context, args.subList(0, 1));
            qname = (String) stringFunction.call(context, args.subList(1, 2));
            content = args.get(2);
        }
        Object elem = updater.createElement(null, uri, qname);
        if (content != null) XPathAppendFunction.append(context, elem, content);
        return elem;
    }
}
