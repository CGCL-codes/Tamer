package io;

import world.WorldConstants;

/**
 * @author tn
 */
public interface IOConstans extends WorldConstants {

    static final int PACKAGE_SIZE = 1472;

    static final int HEADER_NULL = 0x00;

    static final int SK_CONNECT = 0x20;

    static final int SK_ACKNOWLEDGE = 0x21;

    static final int SK_UPDATE = 0x22;

    static final int KS_CONNECT_OK = 0x23;

    static final int COMMANDS = 0x51;

    static final int UPDATE = 0x50;

    static final int INIT_TIME = 0;
}
