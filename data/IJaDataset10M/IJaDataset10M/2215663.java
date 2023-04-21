package net.sf.lua4j.engine;

/**
 * @version $Revision: $ $Date: $
 */
public class LuaNil implements LuaObject {

    public static final LuaObject NIL = new LuaNil();

    public Object getUnderlying() {
        return null;
    }
}
