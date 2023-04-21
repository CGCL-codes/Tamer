package com.l2jserver.gameserver.network.communityserver.readpackets;

import java.util.logging.Logger;
import com.l2jserver.gameserver.network.communityserver.CommunityServerThread;
import org.netcon.BaseReadPacket;

/**
 * @authors  Forsaiken, Gigiikun
 */
public final class AuthResponse extends BaseReadPacket {

    private static final Logger _log = Logger.getLogger(AuthResponse.class.getName());

    public static final byte AUTHED = 0;

    public static final byte REASON_WRONG_HEX_ID = 1;

    public static final byte REASON_HEX_ID_IN_USE = 2;

    public static final byte REASON_WRONG_SQL_DP_ID = 3;

    public static final byte REASON_SQL_DP_ID_IN_USE = 4;

    private final CommunityServerThread _cst;

    public AuthResponse(final byte[] data, final CommunityServerThread cst) {
        super(data);
        _cst = cst;
    }

    @Override
    public final void run() {
        final int status = super.readC();
        switch(status) {
            case AUTHED:
                _cst.setAuthed(true);
                break;
        }
        _log.info("COMMUNITY_SERVER_THREAD: Auth " + status);
    }
}
