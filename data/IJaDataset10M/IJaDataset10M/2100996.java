package test.chat.server.service;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import chat.persistence.entity.User;
import chat.server.service.exceptions.SessionIsNotRegisteredException;
import chat.server.session.ChatSessionManager;
import chat.server.session.ChatSessionManagerImpl;
import chat.shared.dto.ChatSessionInfo;

public class SessionManagerImplTest {

    ChatSessionManager sessionManager;

    @Before
    public void setUp() {
        sessionManager = new ChatSessionManagerImpl();
    }

    @Test
    public void createNewSessionTest() {
        User user = new User();
        user.setNickname("Mock user");
        ChatSessionInfo id = sessionManager.createNewSession(user);
        Assert.assertEquals("expected 0L", Long.valueOf(0L), id.getSessionId());
        ChatSessionInfo id2 = sessionManager.createNewSession(user);
        Assert.assertNotSame(id, id2);
        Assert.assertEquals(Long.valueOf(1L), id2.getSessionId());
    }

    @Test
    public void retrieveMessageTest() throws SessionIsNotRegisteredException {
        User user1 = new User();
        user1.setNickname("Mock user 1");
        User user2 = new User();
        user2.setNickname("Mock user 2");
        User user3 = new User();
        user3.setNickname("Mock user 3");
        Long id1 = sessionManager.createNewSession(user1).getSessionId();
        Long id2 = sessionManager.createNewSession(user2).getSessionId();
        Long id3 = sessionManager.createNewSession(user3).getSessionId();
        sessionManager.pushMessage(id1, "hi there!");
        sessionManager.pushMessage(id1, "hi there!x2");
        Assert.assertEquals(0, sessionManager.retrieveNewMessages(id1).size());
        Assert.assertEquals(2, sessionManager.retrieveNewMessages(id2).size());
        Assert.assertEquals(2, sessionManager.retrieveNewMessages(id3).size());
        Assert.assertEquals(0, sessionManager.retrieveNewMessages(id2).size());
    }
}
