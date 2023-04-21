package net.sf.refactorit.classmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines class for modifier and constants for modifiers
 */
public final class BinModifier {

    /**
   * no one needs to initialize this - it is just static
   */
    private BinModifier() {
    }

    public static int getPrivilegeFlags(int modifiers) {
        return modifiers & PRIVILEGE_MASK;
    }

    public static String toString(int modifier) {
        for (int i = 0; i < modifiers.length; i++) {
            if (modifiers[i] == modifier) {
                return names[i];
            }
        }
        return null;
    }

    public static int toNumber(String modifier) {
        if ("package private".equals(modifier)) {
            return PACKAGE_PRIVATE;
        }
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(modifier)) {
                return modifiers[i];
            }
        }
        return -1;
    }

    /**
   * if first is stronger than second return +1;
   * if first is weaker than second return -1;
   * if first is equal to second return 0;
   * public is stronger than private;
   * private is weaker than public;
   */
    public static int compareAccesses(int first, int second) {
        if (first == second) {
            return 0;
        }
        switch(first) {
            case BinModifier.PUBLIC:
                return +1;
            case BinModifier.PROTECTED:
                if (second == BinModifier.PUBLIC) {
                    return -1;
                }
                break;
            case BinModifier.PACKAGE_PRIVATE:
                if ((second == BinModifier.PUBLIC) || (second == BinModifier.PROTECTED)) {
                    return -1;
                }
                break;
            case BinModifier.PRIVATE:
                return -1;
        }
        return +1;
    }

    public static boolean hasFlag(int modifiers, int mask) {
        return (modifiers & mask) == mask;
    }

    public static int setFlags(int modifiers, int mask) {
        if ((mask & PRIVILEGE_MASK) > 0 || mask == 0) {
            modifiers &= ~PRIVILEGE_MASK;
        }
        return modifiers | mask;
    }

    public static int clearFlags(int modifiers, int mask) {
        return modifiers & ~mask;
    }

    /**
   * Splits complex member modifier into list of simple flags.
   * @param modifier to split
   * @return list of simple modifiers
   */
    public static List splitModifier(int modifier) {
        List flags = new ArrayList(3);
        if ((modifier & PRIVILEGE_MASK) == 0) {
            flags.add(new Integer(PACKAGE_PRIVATE));
        }
        int cur = 1;
        while (cur <= STRICTFP) {
            if ((cur == STATIC || cur == ABSTRACT) && hasFlag(modifier, INTERFACE)) {
                cur <<= 1;
                continue;
            }
            if ((modifier & cur) == cur) {
                flags.add(new Integer(cur));
            }
            cur <<= 1;
        }
        return flags;
    }

    public static final int PRIVILEGE_MASK = 0x0007;

    public static final int STATIC_TO_INTERFACE_MASK = 0x3F8;

    public static final int INVALID = -5;

    public static final int NONE = 0;

    public static final int PACKAGE_PRIVATE = 0x0000;

    public static final int PUBLIC = 0x0001;

    public static final int PRIVATE = 0x0002;

    public static final int PROTECTED = 0x0004;

    public static final int STATIC = 0x0008;

    public static final int FINAL = 0x0010;

    public static final int SYNCHRONIZED = 0x0020;

    public static final int VOLATILE = 0x0040;

    public static final int TRANSIENT = 0x0080;

    public static final int NATIVE = 0x0100;

    public static final int INTERFACE = 0x0200;

    public static final int ABSTRACT = 0x0400;

    public static final int STRICTFP = 0x0800;

    public static final int SYNTHETIC = 0x00001000;

    public static final int ANNOTATION = 0x00002000;

    public static final int ENUM = 0x00004000;

    public static final int BRIDGE = 0x00000040;

    public static final int VARARGS = 0x00000080;

    public static final int SUPER = 0x0020;

    public static final int NBR_FLAGS = 12;

    public static final int[] modifiers = { BinModifier.PUBLIC, BinModifier.PRIVATE, BinModifier.PROTECTED, BinModifier.ABSTRACT, BinModifier.STATIC, BinModifier.FINAL, BinModifier.NATIVE, BinModifier.SYNCHRONIZED, BinModifier.STRICTFP, BinModifier.VOLATILE, BinModifier.TRANSIENT, BinModifier.INTERFACE, BinModifier.SYNTHETIC, BinModifier.ANNOTATION, BinModifier.ENUM };

    public static final String[] names = { "public", "private", "protected", "abstract", "static", "final", "native", "synchronized", "strictfp", "volatile", "transient", "interface", "synthetic", "@", "enum" };
}
