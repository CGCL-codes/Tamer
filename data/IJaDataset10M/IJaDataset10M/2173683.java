package jode.decompiler;

import jode.bytecode.ClassInfo;
import jode.bytecode.InnerClassInfo;

public class Options {

    public static final int TAB_SIZE_MASK = 0x0f;

    public static final int BRACE_AT_EOL = 0x10;

    public static final int BRACE_FLUSH_LEFT = 0x20;

    public static final int GNU_SPACING = 0x40;

    public static final int SUN_STYLE = 0x14;

    public static final int GNU_STYLE = 0x42;

    public static final int PASCAL_STYLE = 0x24;

    public static final int OPTION_LVT = 0x0001;

    public static final int OPTION_INNER = 0x0002;

    public static final int OPTION_ANON = 0x0004;

    public static final int OPTION_PUSH = 0x0008;

    public static final int OPTION_PRETTY = 0x0010;

    public static final int OPTION_DECRYPT = 0x0020;

    public static final int OPTION_ONETIME = 0x0040;

    public static final int OPTION_IMMEDIATE = 0x0080;

    public static final int OPTION_VERIFY = 0x0100;

    public static final int OPTION_CONTRAFO = 0x0200;

    public static int options = OPTION_LVT | OPTION_INNER | OPTION_ANON | OPTION_PRETTY | OPTION_DECRYPT | OPTION_VERIFY | OPTION_CONTRAFO;

    public static int outputStyle = SUN_STYLE;

    public static final boolean doAnonymous() {
        return (options & OPTION_ANON) != 0;
    }

    public static final boolean doInner() {
        return (options & OPTION_INNER) != 0;
    }

    public static boolean skipClass(ClassInfo clazz) {
        InnerClassInfo[] outers = clazz.getOuterClasses();
        if (outers != null) {
            if (outers[0].outer == null) {
                return doAnonymous();
            } else {
                return doInner();
            }
        }
        return false;
    }
}
