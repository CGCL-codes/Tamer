package com.ilog.translator.java2cs.configuration;

import org.eclipse.jdt.core.dom.Modifier;

public class DotNetModifier {

    protected static final String PUBLIC_KEY = "public";

    protected static final String PRIVATE_KEY = "private";

    protected static final String PROTECTED_KEY = "protected";

    protected static final String SEALED_KEY = "sealed";

    protected static final String ABSTRACT_KEY = "abstract";

    protected static final String STATIC_KEY = "static";

    protected static final String PARTIAL_KEY = "partial";

    protected static final String VIRTUAL_KEY = "virtual";

    protected static final String OVERRIDE_KEY = "override";

    protected static final String NEW_KEY = "new";

    protected static final String INTERNAL_KEY = "internal";

    protected static final String CONST_KEY = "const";

    protected static final String FINAL_KEY = "final";

    protected static final String STRICTFP_KEY = "strictfp";

    protected static final String READONLY_KEY = "readonly";

    public static final int PUBLIC_MOD = Modifier.PUBLIC;

    public static final int PRIVATE_MOD = Modifier.PRIVATE;

    public static final int PROTECTED_MOD = Modifier.PROTECTED;

    public static final int SEALED_MOD = 0x1000;

    public static final int ABSTRACT_MOD = Modifier.ABSTRACT;

    public static final int STATIC_MOD = Modifier.STATIC;

    public static final int PARTIAL_MOD = 0x2000;

    public static final int VIRTUAL_MOD = 0x3000;

    public static final int OVERRIDE_MOD = 0x4000;

    public static final int NEW_MOD = 0x5000;

    public static final int INTERNAL_MOD = 0x6000;

    public static final int CONST_MOD = 0x7000;

    public static final int STRICTFP_MOD = 0x8000;

    public static final int READONLY_MOD = 0x9000;

    public static final int FINAL_MOD = Modifier.FINAL;

    public static final DotNetModifier PUBLIC = new DotNetModifier(PUBLIC_MOD, PUBLIC_KEY, false, 1);

    public static final DotNetModifier PRIVATE = new DotNetModifier(PRIVATE_MOD, PRIVATE_KEY, false, 1);

    public static final DotNetModifier PROTECTED = new DotNetModifier(PROTECTED_MOD, PROTECTED_KEY, false, 1);

    public static final DotNetModifier INTERNAL = new DotNetModifier(INTERNAL_MOD, INTERNAL_KEY, true, 2);

    public static final DotNetModifier ABSTRACT = new DotNetModifier(ABSTRACT_MOD, ABSTRACT_KEY, false, 3);

    public static final DotNetModifier STATIC = new DotNetModifier(STATIC_MOD, STATIC_KEY, false, 3);

    public static final DotNetModifier SEALED = new DotNetModifier(SEALED_MOD, SEALED_KEY, true, 3);

    public static final DotNetModifier VIRTUAL = new DotNetModifier(VIRTUAL_MOD, VIRTUAL_KEY, true, 4);

    public static final DotNetModifier OVERRIDE = new DotNetModifier(OVERRIDE_MOD, OVERRIDE_KEY, true, 4);

    public static final DotNetModifier NEW = new DotNetModifier(NEW_MOD, NEW_KEY, true, 4);

    public static final DotNetModifier PARTIAL = new DotNetModifier(PARTIAL_MOD, PARTIAL_KEY, true, 5);

    public static final DotNetModifier READONLY = new DotNetModifier(READONLY_MOD, READONLY_KEY, true, 5);

    public static final DotNetModifier CONST = new DotNetModifier(CONST_MOD, CONST_KEY, true, 5);

    public static final DotNetModifier FINAL = new DotNetModifier(FINAL_MOD, FINAL_KEY, false, 2);

    public static final DotNetModifier STRICTFP = new DotNetModifier(STRICTFP_MOD, STRICTFP_KEY, false, 1);

    public static DotNetModifier fromKeyword(String mod) {
        if (DotNetModifier.PUBLIC_KEY.equals(mod)) {
            return DotNetModifier.PUBLIC;
        }
        if (DotNetModifier.PRIVATE_KEY.equals(mod)) {
            return DotNetModifier.PRIVATE;
        }
        if (DotNetModifier.PROTECTED_KEY.equals(mod)) {
            return DotNetModifier.PROTECTED;
        }
        if (DotNetModifier.SEALED_KEY.equals(mod)) {
            return DotNetModifier.SEALED;
        }
        if (DotNetModifier.ABSTRACT_KEY.equals(mod)) {
            return DotNetModifier.ABSTRACT;
        }
        if (DotNetModifier.STATIC_KEY.equals(mod)) {
            return DotNetModifier.STATIC;
        }
        if (DotNetModifier.PARTIAL_KEY.equals(mod)) {
            return DotNetModifier.PARTIAL;
        }
        if (DotNetModifier.VIRTUAL_KEY.equals(mod)) {
            return DotNetModifier.VIRTUAL;
        }
        if (DotNetModifier.OVERRIDE_KEY.equals(mod)) {
            return DotNetModifier.OVERRIDE;
        }
        if (DotNetModifier.NEW_KEY.equals(mod)) {
            return DotNetModifier.NEW;
        }
        if (DotNetModifier.INTERNAL_KEY.equals(mod)) {
            return DotNetModifier.INTERNAL;
        }
        if (DotNetModifier.CONST_KEY.equals(mod)) {
            return DotNetModifier.CONST;
        }
        if (DotNetModifier.FINAL_KEY.equals(mod)) {
            return DotNetModifier.FINAL;
        }
        if (DotNetModifier.READONLY_KEY.equals(mod)) {
            return DotNetModifier.READONLY;
        }
        return null;
    }

    private int kind = -1;

    private int height = -1;

    private String keyword = null;

    private boolean dotnetonly = false;

    protected DotNetModifier(int kind, String keyword, boolean dotnetonly, int height) {
        this.kind = kind;
        this.height = height;
        this.keyword = keyword;
        this.dotnetonly = dotnetonly;
    }

    public int getKind() {
        return kind;
    }

    public int getHeight() {
        return height;
    }

    public String getKeyword() {
        return keyword;
    }

    public boolean isDotNetOnly() {
        return dotnetonly;
    }
}
