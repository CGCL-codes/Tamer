package org.resource;

public class FileConstants {

    public static final int THYVIN_FORMAT = 0xBeBaFeCa;

    /**
	 * Room unit format.
	 * 
	 * From version 3 and forward.
	 * Version 3 also uses thyvin format.
	 */
    public static final int ROOM_UNIT_FORMAT = 0xff001200;

    public static final int ROOM_UNIT_LAST_VERSION = 3;

    /**
	 * Room format.
	 * 
	 * From version 2 and forward.
	 * Version 2 also uses thyvin format.
	 */
    public static final int ROOM_FORMAT = 0xff001100;

    public static final int ROOM_LAST_VERSION = 2;
}
