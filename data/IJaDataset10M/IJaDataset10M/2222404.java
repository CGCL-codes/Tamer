package com.l2jserver.loginserver.network.serverpackets;

import java.util.logging.Logger;
import com.l2jserver.Config;

/**
 * Fromat: d
 * d: response
 */
public final class GGAuth extends L2LoginServerPacket {

    static final Logger _log = Logger.getLogger(GGAuth.class.getName());

    public static final int SKIP_GG_AUTH_REQUEST = 0x0b;

    private int _response;

    public GGAuth(int response) {
        _response = response;
        if (Config.DEBUG) {
            _log.warning("Reason Hex: " + (Integer.toHexString(response)));
        }
    }

    /**
	 * @see com.l2jserver.mmocore.network.SendablePacket#write()
	 */
    @Override
    protected void write() {
        writeC(0x0b);
        writeD(_response);
        writeD(0x00);
        writeD(0x00);
        writeD(0x00);
        writeD(0x00);
    }
}
