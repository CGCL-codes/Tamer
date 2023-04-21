package com.spoledge.audao.parser.gql.impl.soft.func;

import java.util.List;
import static com.spoledge.audao.parser.gql.impl.ParserUtils.*;

/**
 * SUBSTR( text, start [, count ]) .<br/>
 *
 * start: 1,2,3... (1 = first char)
 *        if negative, then counts backwards (-1 = last char)
 *        if 0, then starting at the first char.<br/>
 *
 * count: optional number of characters.
 */
public class FuncSUBSTR extends Func {

    protected Object getFunctionValueImpl(List<Object> args) {
        String s = argString(args.get(0));
        if (s == null) return null;
        int index = argInt(args.get(1));
        if (index < 0) {
            index = s.length() + index;
            if (index < 0) index = 0;
        } else if (index > 0) index--;
        if (index >= s.length()) return "";
        if (args.size() > 2) {
            int count = argInt(args.get(2));
            if (count + index < s.length()) {
                return s.substring(index, index + count);
            }
        }
        return s.substring(index);
    }

    protected void checkNumOfParams(List<Object> args) {
        checkNumOfParams(2, 3, args);
    }
}
