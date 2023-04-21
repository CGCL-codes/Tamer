package org.marre.wap.wbxml;

public final class WbxmlConstants {

    /**
     * Change the code page for the current token state. Followed by a single
     * u_int8 indicating the new code page number.
     */
    public static final byte TOKEN_SWITCH_PAGE = 0x00;

    /** Indicates the end of an attribute list or the end of an element. */
    public static final byte TOKEN_END = 0x01;

    /**
     * A character entity. Followed by a mb_u_int32 encoding the character
     * entity number.
     */
    public static final byte TOKEN_ENTITY = 0x02;

    /** Inline string. Followed by a termstr. */
    public static final byte TOKEN_STR_I = 0x03;

    /**
     * An unknown attribute name, or unknown tag posessing no attributes or
     * content.Followed by a mb_u_int32 that encodes an offset into the string
     * table.
     */
    public static final byte TOKEN_LITERAL = 0x04;

    /**
     * Inline string document-type-specific extension token. Token is followed
     * by a termstr.
     */
    public static final byte TOKEN_EXT_I_0 = 0x40;

    /**
     * Inline string document-type-specific extension token. Token is followed
     * by a termstr.
     */
    public static final byte TOKEN_EXT_I_1 = 0x41;

    /**
     * Inline string document-type-specific extension token. Token is followed
     * by a termstr.
     */
    public static final byte TOKEN_EXT_I_2 = 0x42;

    /** Processing instruction. */
    public static final byte TOKEN_PI = 0x43;

    /** An unknown tag posessing content but no attributes. */
    public static final byte TOKEN_LITERAL_C = 0x44;

    /**
     * Inline integer document-type-specific extension token. Token is followed
     * by a mb_u_int32.
     */
    public static final byte TOKEN_EXT_T_0 = (byte) 0x80;

    /**
     * Inline integer document-type-specific extension token. Token is followed
     * by a mb_u_int32.
     */
    public static final byte TOKEN_EXT_T_1 = (byte) 0x81;

    /**
     * Inline integer document-type-specific extension token. Token is followed
     * by a mb_u_int32.
     */
    public static final byte TOKEN_EXT_T_2 = (byte) 0x82;

    /**
     * String table reference. Followed by a mb_u_int32 encoding a byte offset
     * from the beginning of the string table.
     */
    public static final byte TOKEN_STR_T = (byte) 0x83;

    /** An unknown tag posessing attributes but no content. */
    public static final byte TOKEN_LITERAL_A = (byte) 0x84;

    /** Single-byte document-type-specific extension token. */
    public static final byte TOKEN_EXT_0 = (byte) 0xC0;

    /** Single-byte document-type-specific extension token. */
    public static final byte TOKEN_EXT_1 = (byte) 0xC1;

    /** Single-byte document-type-specific extension token. */
    public static final byte TOKEN_EXT_2 = (byte) 0xC2;

    /** Opaque document-type-specific data. */
    public static final byte TOKEN_OPAQ = (byte) 0xC3;

    /** An unknown tag posessing both attributes and content. */
    public static final byte TOKEN_LITERAL_AC = (byte) 0xC4;

    /** Tag contains content. */
    public static final byte TOKEN_KNOWN_C = (byte) 0x40;

    /** Tag contains attributes. */
    public static final byte TOKEN_KNOWN_A = (byte) 0x80;

    /** Tag contains attributes. */
    public static final byte TOKEN_KNOWN_AC = (byte) 0xC0;

    /** Tag contains attributes. */
    public static final byte TOKEN_KNOWN = (byte) 0x00;

    public static final String[] KNOWN_PUBLIC_DOCTYPES = { "-//WAPFORUM//DTD WML 1.0//EN", "-//WAPFORUM//DTD WTA 1.0//EN", "-//WAPFORUM//DTD WML 1.1//EN", "-//WAPFORUM//DTD SI 1.0//EN", "-//WAPFORUM//DTD SL 1.0//EN", "-//WAPFORUM//DTD CO 1.0//EN", "-//WAPFORUM//DTD CHANNEL 1.1//EN", "-//WAPFORUM//DTD WML 1.2//EN", "-//WAPFORUM//DTD WML 1.3//EN", "-//WAPFORUM//DTD PROV 1.0//EN", "-//WAPFORUM//DTD WTA-WML 1.2//EN", "-//WAPFORUM//DTD EMN 1.0//EN", "-//OMA//DTD DRMREL 1.0//EN" };

    private WbxmlConstants() {
    }
}
