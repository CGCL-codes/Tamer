package com.l2jserver.gameserver.network.gameserverpackets;

import java.io.IOException;
import com.l2jserver.util.network.BaseSendablePacket;

/**
 * 
 * @author mrTJO
 */
public class SendMail extends BaseSendablePacket {

    public SendMail(String accountName, String mailId, String... args) {
        writeC(0x09);
        writeS(accountName);
        writeS(mailId);
        writeC(args.length);
        for (int i = 0; i < args.length; i++) {
            writeS(args[i]);
        }
    }

    @Override
    public byte[] getContent() throws IOException {
        return getBytes();
    }
}
