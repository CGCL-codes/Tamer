package org.dcm4che2.net;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Reversion$ $Date: 2007-02-16 03:20:33 -0500 (Fri, 16 Feb 2007) $
 * @since Sep 15, 2005
 *
 */
class ItemType {

    public static final int APP_CONTEXT = 0x10;

    public static final int RQ_PRES_CONTEXT = 0x20;

    public static final int AC_PRES_CONTEXT = 0x21;

    public static final int ABSTRACT_SYNTAX = 0x30;

    public static final int TRANSFER_SYNTAX = 0x40;

    public static final int USER_INFO = 0x50;

    public static final int MAX_PDU_LENGTH = 0x51;

    public static final int IMPL_CLASS_UID = 0x52;

    public static final int ASYNC_OPS_WINDOW = 0x53;

    public static final int ROLE_SELECTION = 0x54;

    public static final int IMPL_VERSION_NAME = 0x55;

    public static final int EXT_NEG = 0x56;

    public static final int COMMON_EXT_NEG = 0x57;

    public static final int RQ_USER_IDENTITY = 0x58;

    public static final int AC_USER_IDENTITY = 0x59;
}
