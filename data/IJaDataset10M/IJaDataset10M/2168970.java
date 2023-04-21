package ognlscript.block;

import java.util.*;

/**
 * Alberto Vilches Ratón
 * User: avilches
 * Date: 07-oct-2006
 * Time: 11:01:59
 * To change this template use File | Settings | File Templates.
 */
public interface CommandConstants {

    public static final String LOCAL = "local";

    public static final String IF = "if";

    public static final String ELSEIF = "elseif";

    public static final String ELSE = "else";

    public static final String ENDIF = "endif";

    public static final String WHILE = "while";

    public static final String ENDDO = "enddo";

    public static final String FOREACH = "foreach";

    public static final String ENDFOR = "endfor";

    public static final String REPEAT = "repeat";

    public static final String UNTIL = "until";

    public static final String BREAK = "break";

    public static final String LOOP = "loop";

    public static final String RETURN = "return";

    public static final String RTRUE = "rtrue";

    public static final String RFALSE = "rfalse";

    public static final String PRINT = "print";

    public static final String PRINTLN = "println";

    public static final String SETPRINT = "setprint";

    public static final String STARTBUFFER = "startbuffer";

    public static final String COMMITBUFFER = "commitbuffer";

    public static final String ROLLBACKBUFFER = "rollbackbuffer";

    public static final String CLEARBUFFER = "clearbuffer";

    public static final String DEBUG = "debug";

    public static final String SWITCH = "switch";

    public static final String CASE = "case";

    public static final String ENDSWITCH = "endswitch";

    public static final String DEFAULT = "default";

    public static final String ASSERT = "assert";

    public static final String THROW = "throw";

    public static final Set<String> RESERVED_WORDS = new HashSet<String>(Arrays.asList(LOCAL, IF, ELSEIF, ELSE, ENDIF, WHILE, ENDDO, FOREACH, ENDFOR, REPEAT, UNTIL, LOOP, BREAK, ASSERT, THROW, "or", "and", "not", "in", "xor", "band", "bor", "eq", "neq", "lt", "gt", "lte", "gte", "shl", "shr", "ushr", RETURN, RTRUE, RFALSE, SWITCH, CASE, DEFAULT, ENDSWITCH));
}
