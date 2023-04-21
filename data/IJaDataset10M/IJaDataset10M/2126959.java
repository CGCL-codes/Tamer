package org.freelords.network.client.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.freelords.network.client.Session;
import org.freelords.network.fake.FakeUpdate;
import org.freelords.network.update.ChatUpdate;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClientChatServiceTest {

    private ClientChatService service;

    private Session session;

    private ByteArrayOutputStream outStream = new ByteArrayOutputStream();

    private ChatUpdate update;

    @Before
    public void setup() {
        session = new Session();
        Map<UUID, String> nameMap = new HashMap<UUID, String>();
        UUID clientId = UUID.randomUUID();
        nameMap.put(clientId, "SomeFavoriteName");
        session.updateClients(nameMap);
        service = new ClientChatService(new PrintStream(outStream), session);
        update = new ChatUpdate("A chat message", clientId);
    }

    @Test
    public void serviceAcceptsOnlyChatUpdates() {
        assertTrue("Service must accept chat updates.", service.accept(update));
        assertFalse("Service must not accept other updates.", service.accept(new FakeUpdate()));
    }

    @Test
    public void servicePrintsChatUpdateOnReceivingStuff() throws Exception {
        service.execute(update);
        String output = outStream.toString();
        assertTrue("ChatMessage did not appear in output.", output.contains(update.getMessage()));
        assertTrue("Chat sender did not appear in output.", output.contains(session.getClientName(update.getSenderId())));
    }
}
