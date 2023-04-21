package org.taak.module.core;

import org.taak.*;
import org.taak.error.*;
import org.taak.function.*;
import org.taak.json.JSONDecoder;

/**
 * The built in function to capitalize the first letter in a string.
 */
public class JsonDecode extends UnaryFunction {

    public static final JsonDecode INSTANCE = new JsonDecode();

    public static final String NAME = "jsonDecode";

    public Object apply(Context context, Object arg) {
        if (arg == null) {
            return null;
        }
        JSONDecoder decoder = new JSONDecoder(context);
        if (arg instanceof String) {
            return decoder.decode((String) arg);
        } else {
            Class type = arg.getClass();
            if (type.isArray()) {
                if (Byte.TYPE.equals(type.getComponentType())) {
                    return decoder.decode(new String((byte[]) arg));
                }
            }
        }
        throw new TypeError("Invalid argument type to deserialize()");
    }

    public String getName() {
        return NAME;
    }
}
