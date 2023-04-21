package com.incesoft.botplatform.sdk;

/**
 * @author LiBo
 */
public interface RobotUser {

    /**
     * User is online
     */
    public static final String STATUS_ONLINE = "NLN";

    /**
     * User is offline
     */
    public static final String STATUS_OFFLINE = "FLN";

    /**
     * User is hidden
     */
    public static final String STATUS_HIDDEN = "HDN";

    /**
     * User is busy
     */
    public static final String STATUS_BUSY = "BSY";

    /**
     * User is idle
     */
    public static final String STATUS_IDLE = "IDL";

    /**
     * User will be right back
     */
    public static final String STATUS_BE_RIGHT_BACK = "BRB";

    /**
     * User is away
     */
    public static final String STATUS_AWAY = "AWY";

    /**
     * User is on the phone
     */
    public static final String STATUS_ON_THE_PHONE = "PHN";

    /**
     * User is out to lunch
     */
    public static final String STATUS_OUT_TO_LUNCH = "LUN";

    /**
     * User is online via Mobile
     */
    public static final int FLAG_MOBILE_CLIENT = 0x00000001;

    /**
     * User online with the MSN client
     */
    public static final int FLAG_MSN_CLIENT = 0x00000002;

    /**
     * User online GIF/ink awareness
     */
    public static final int FLAG_GIF_INK_AWARENESS = 0x00000004;

    /**
     * User online ISF/ink awareness
     */
    public static final int FLAG_ISF_INK_AWARENESS = 0x00000008;

    /**
     * There is a web camera on user's machine
     */
    public static final int FLAG_WEB_CAMERA = 0x00000010;

    /**
     * Support of message chunking (to allow larger messages over SB)
     */
    public static final int FLAG_MSG_CHUNKING = 0x00000020;

    /**
     * Mobile IMs permitted flag
     */
    public static final int FLAG_MOBILE_IM = 0x00000040;

    /**
     * Direct Watch enabled flag
     */
    public static final int FLAG_DIRECT_WATCH = 0x00000080;

    /**
     * The user is online via the web client
     */
    public static final int FLAG_WEB_CLIENT = 0x00000200;

    /**
     * The user is a mobile buddy.
     * The flag can be thought of as the Mobile buddy bit which indicates that
     *  the buddy is on a mobile device and the mobile properties 
     */
    public static final int FLAG_MOBILE_BUDDY = 0x00000400;

    /**
     * The user is a federated client.
     * The flag can be thought of as the Federated client bit which indicates that 
     *  the buddy is connected on a client that does not connect directly to the MSN Messenger service 
     */
    public static final int FLAG_FEDERATED_BUDDY = 0x00000800;

    /**
     * The user has its HasSpace bit set
     */
    public static final int FLAG_MSN_SPACE = 0x00001000;

    /**
     * The user supports Peer-to-Peer IM
     */
    public static final int FLAG_P2P_IM = 0x00004000;

    /**
     * The user supports Winks
     */
    public static final int FLAG_WINKS_SUPPORT = 0x00008000;

    /**
     * Client version 6.0
     */
    public static final int FLAG_CLIENT_VERSION_6_0 = 0x10000000;

    /**
     * Client version 6.1
     */
    public static final int FLAG_CLIENT_VERSION_6_1 = 0x20000000;

    /**
     * Client version 6.2
     */
    public static final int FLAG_CLIENT_VERSION_6_2 = 0x30000000;

    /**
     * Client version 7.0
     */
    public static final int FLAG_CLIENT_VERSION_7_0 = 0x40000000;

    /**
     * Get user's identifier (MSN account) 
     */
    public String getID();

    /**
     * Get user's friendly name
     */
    public String getFriendlyName();

    /**
     * Get user's status
     * @return user's status<br>
     * NLN - online<br>
     * FLN - offline<br>
     * HDN - hidden<br>
     * BSY - busy<br>
     * IDL - idle<br>
     * BRB - be right back<br>
     * AWY - away<br>
     * PHN - on the phone<br>
     * LUN - out to lunch<br>
     */
    public String getStatus();

    /**
     * Get user's client capabilities
     * @return Client ID<br>
     * <ul>
     * <li>0x00000001 - User is online via Mobile
     * <li>0x00000002 - User online with the MSN client
     * <li>0x00000004 - User online GIF/ink awareness
     * <li>0x00000008 - User online ISF/ink awareness
     * <li>0x00000010 - There is a web camera on user's machine
     * <li>0x00000020 - Support of message chunking (to allow larger messages over SB)
     * <li>0x00000040 - Mobile IMs permitted flag
     * <li>0x00000080 - Direct Watch enabled flag
     * <li>0x00000200 - The user is online via the web client
     * <li>0x00000400 - The user is a mobile buddy.The flag can be thought of as the Mobile buddy bit which indicates that the buddy is on a mobile device and the mobile properties
     * <li>0x00000800 - The user is a federated client.The flag can be thought of as the Federated client bit which indicates that the buddy is connected on a client that does not connect directly to the MSN Messenger service
     * <li>0x00001000 - The user has its HasSpace bit set
     * <li>0x00004000 - The user supports Peer-to-Peer IM
     * <li>0x00008000 - The user supports Winks
     * <li>0x10000000 - Client version 6.0
     * <li>0x20000000 - Client version 6.1
     * <li>0x30000000 - Client version 6.2
     * <li>0x40000000 - Client version 7.0
     * </ul> 
     */
    public long getClientID();
}
