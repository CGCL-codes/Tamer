package org.sinaxe.context;

import org.jaxen.Function;
import org.jaxen.Context;
import org.jaxen.FunctionCallException;
import java.util.List;
import java.util.ArrayList;

public class XPathAttributeFunction extends SinaxeXPathFunction {

    public static final String name = "attribute";

    Context context = null;

    ArrayList args = new ArrayList();

    public XPathAttributeFunction() {
    }

    public String getName() {
        return name;
    }

    public Object call(Context context, List args) throws FunctionCallException {
        checkArgs(args, 2, 3);
        Function stringFunction = getFunction(context, "string");
        String uri = null;
        String qname;
        String value;
        if (args.size() == 3) {
            uri = (String) stringFunction.call(context, args.subList(0, 1));
            qname = (String) stringFunction.call(context, args.subList(1, 2));
            value = (String) stringFunction.call(context, args.subList(2, 3));
        } else {
            qname = (String) stringFunction.call(context, args.subList(0, 1));
            value = (String) stringFunction.call(context, args.subList(1, 2));
        }
        return updater.createAttribute(null, uri, qname, value);
    }
}
