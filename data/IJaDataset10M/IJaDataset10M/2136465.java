package org.openymsg.conference;

import java.io.IOException;
import org.mockito.Mockito;
import org.openymsg.YahooConference;
import org.openymsg.YahooContact;
import org.openymsg.YahooProtocol;
import org.openymsg.network.YMSG9Packet;
import org.openymsg.testing.PacketReader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ConferenceMessageResponseTest {

    String username = "testuser";

    private SessionConferenceImpl session;

    @BeforeMethod
    public void beforeMethod() {
        session = Mockito.mock(SessionConferenceImpl.class);
    }

    @Test
    public void test() throws IOException {
        String test = "Magic:YMSG Version:16 Length:111 Service:CONFMSG Status:SERVER_ACK SessionId:0x56cf6d  [1] [testuser] [3] [testbuddy] [14] [myMessage] [57] [testuser-8iVmHcCkflGJpBXpjBbzCw--] [97] [1]";
        YMSG9Packet packet = PacketReader.readString(test);
        ConferenceMessageResponse response = new ConferenceMessageResponse(session);
        response.execute(packet);
        String id = "testuser-8iVmHcCkflGJpBXpjBbzCw--";
        YahooConference conference = new ConferenceImpl(id);
        YahooContact sender = new YahooContact("testbuddy", YahooProtocol.YAHOO);
        String message = "myMessage";
        Mockito.verify(session).receivedConferenceMessage(conference, sender, message);
    }
}
