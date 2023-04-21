package com.netx.eap.R1.bl;

import com.netx.basic.R1.l10n.ContentID;

public class L10n extends com.netx.bl.R1.core.L10n {

    protected static final String MODULE_EAP = "EAP";

    protected static final String TYPE_VAL = "Val";

    public static final ContentID EAP_VAL_FIRST_CHAR_MUST_BE_LETTER = new ContentID(MODULE_EAP, TYPE_VAL, "first-char-must-be-letter");

    public static final ContentID EAP_VAL_ILLEGAL_CHARS = new ContentID(MODULE_EAP, TYPE_VAL, "illegal-chars");

    public static final ContentID EAP_VAL_NO_DIGITS_ALLOWED = new ContentID(MODULE_EAP, TYPE_VAL, "no-digits-allowed");

    public static final ContentID EAP_VAL_PASSWORD_NO_SPACES = new ContentID(MODULE_EAP, TYPE_VAL, "password-no-spaces");

    public static final ContentID EAP_VAL_ONE_ROLE_REQUIRED = new ContentID(MODULE_EAP, TYPE_VAL, "one-role-required");

    public static final ContentID EAP_VAL_ONE_PERMISSION_REQUIRED = new ContentID(MODULE_EAP, TYPE_VAL, "one-permission-required");

    public static final ContentID EAP_VAL_ONE_PRIMARY_ROLE = new ContentID(MODULE_EAP, TYPE_VAL, "one-primary-role");

    public static final ContentID EAP_VAL_DUPLICATE_USER_PERMISSION = new ContentID(MODULE_EAP, TYPE_VAL, "duplicate-user-permission");

    public static final ContentID EAP_VAL_ILLEGAL_USER_PERMISSION = new ContentID(MODULE_EAP, TYPE_VAL, "illegal-user-permission");
}
