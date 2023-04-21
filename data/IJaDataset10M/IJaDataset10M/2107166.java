package org.jucetice.javascript.extensions;

import org.jucetice.javascript.utils.Validator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ErrorExtension {

    /**
	 * Date extensions.
	 * 
	 * We try to keep here the most valuable extensions in order to leave the
	 * javascript codebase clean from unneded files
	 * 
	 */
    public static void register(Scriptable scope) {
        ScriptableObject objProto = (ScriptableObject) ScriptableObject.getClassPrototype(scope, "Error");
        objProto.defineFunctionProperties(new String[] { "toString" }, ErrorExtension.class, ScriptableObject.DONTENUM | ScriptableObject.PERMANENT | ScriptableObject.READONLY);
        objProto.sealObject();
    }

    /**
	 * 
	 * @param cx
	 * @param scope
	 * @param thisObj
	 * @param args
	 * @return
	 */
    public static Object toString(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        ScriptableObject object = (ScriptableObject) thisObj;
        Object message = object.get("message", object);
        if (Validator.isValid(message)) return message;
        return "[object Error]";
    }
}
