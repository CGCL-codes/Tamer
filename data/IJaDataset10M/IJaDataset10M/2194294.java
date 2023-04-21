package messages.session.test;

import static org.junit.Assert.assertNotNull;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import messages.session.SessionStateMsg;
import org.junit.Test;
import server.ClientInfo;
import server.ServerGameSession;

public class SessionStateMsgTest {

    SessionStateMsg sessionStateMsg = new SessionStateMsg(1);

    @Test
    public void testSessionStateMsg() {
        assertNotNull(sessionStateMsg);
    }

    @Test
    public void testExecute() {
        List<String> maps = new LinkedList<String>();
        maps.add("map1");
        maps.add("map2");
        ServerGameSession session = new ServerGameSession("Gamesession", maps, 5, 3);
        Socket clientSocket = new Socket();
        ClientInfo sender = new ClientInfo(clientSocket);
        sessionStateMsg.execute(session, sender);
    }
}
