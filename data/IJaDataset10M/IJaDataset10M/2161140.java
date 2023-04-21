package com.peterhi.server.handler;

import java.io.IOException;
import java.net.SocketAddress;
import com.peterhi.net.message.VoiceMessage;
import com.peterhi.server.DatagramHandler;
import com.peterhi.server.Main;
import com.peterhi.PeterHi;

public class VoiceMessageHandler implements DatagramHandler<VoiceMessage> {

    public void handle(SocketAddress sa, VoiceMessage message) throws IOException {
        Main.broadcast(message, PeterHi.DATAGRAM, false);
    }
}
