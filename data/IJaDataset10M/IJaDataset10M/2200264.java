package com.l2jserver.loginserver.loginserverpackets;

import java.io.IOException;
import com.l2jserver.util.network.BaseSendablePacket;

/**
 * @author -Wooden-
 *
 */
public class PlayerAuthResponse extends BaseSendablePacket {

    public PlayerAuthResponse(String account, boolean response) {
        writeC(0x03);
        writeS(account);
        writeC(response ? 1 : 0);
    }

    @Override
    public byte[] getContent() throws IOException {
        return getBytes();
    }
}
