package org.xmlpull.v1.builder.xpath.jaxen.function;

import org.xmlpull.v1.builder.xpath.jaxen.Context;
import org.xmlpull.v1.builder.xpath.jaxen.Function;
import org.xmlpull.v1.builder.xpath.jaxen.FunctionCallException;
import java.util.List;

/**
 * <p><b>4.1</b> <code><i>number</i> position()</code>
 *
 * @author bob mcwhirter (bob @ werken.com)
 */
public class PositionFunction implements Function {

    public Object call(Context context, List args) throws FunctionCallException {
        if (args.size() == 0) {
            return evaluate(context);
        }
        throw new FunctionCallException("position() requires no arguments.");
    }

    public static Number evaluate(Context context) {
        return new Double(context.getPosition());
    }
}
