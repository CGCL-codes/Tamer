package net.galacticwar.lua;

import java.io.IOException;

/**
 * 
 * Provides methods for parsing LUA files
 * 
 * @author Yoann Meste (aka Mancer)
 *
 */
public class LuaDeserialize {

    private static final int TYPE_NUMBER = 0;

    private static final int TYPE_STRING = 1;

    private static final int TYPE_NIL = 2;

    private static final int TYPE_BOOLEAN = 3;

    private static final int TABLE_BEGIN = 4;

    private static final int TABLE_END = 5;

    private static int peekType(LittleEndianInputStream in) throws IOException {
        if (in.markSupported()) {
            in.mark(10);
            int type = in.read();
            in.reset();
            return type;
        } else {
            throw new RuntimeException("Mark are not supported");
        }
    }

    public static Object parseLua(LittleEndianInputStream in) throws IOException, LuaException {
        int type = in.read();
        switch(type) {
            case TYPE_NIL:
                {
                    return null;
                }
            case TYPE_BOOLEAN:
                {
                    boolean val = in.read() != 0;
                    return val;
                }
            case TYPE_NUMBER:
                {
                    float val = in.readFloat();
                    return val;
                }
            case TYPE_STRING:
                {
                    String s = in.readString();
                    return s;
                }
            case TABLE_BEGIN:
                {
                    LuaTable table = new LuaTable();
                    while (peekType(in) != TABLE_END) {
                        Object key = parseLua(in);
                        Object value = parseLua(in);
                        LuaPair pair = new LuaPair();
                        pair.push(key);
                        pair.setEqual(true);
                        pair.push(value);
                        table.add(pair);
                    }
                    int endTable = in.read();
                    assert (endTable == TABLE_END);
                    return table;
                }
            case TABLE_END:
                {
                    throw new LuaException("Error: unexpected end-of-table");
                }
            default:
                throw new LuaException("Unknown lua data");
        }
    }
}
