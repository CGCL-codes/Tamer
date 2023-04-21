package com.brasee.games.lobby.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import com.brasee.games.GamesUser;
import com.brasee.games.lobby.LobbyUiController;

public class RefreshUserCommandTest extends AbstractLobbyCommandTest {

    private GamesUser user;

    private MockHttpServletRequest request;

    private MockHttpSession session;

    @Before
    public void setUp() {
        user = new GamesUser("User", "111111");
        request = new MockHttpServletRequest();
        session = new MockHttpSession();
        session.setAttribute(LobbyUiController.GAMES_USER_SESSION_VARIABLE, user);
        request.setSession(session);
    }

    @Test
    public void testRefreshUserCommandReturnsSuccessResultForValidInput() {
        request.addParameter("command", "refresh_user");
        String expectedResult = "{\"result\":\"success\"}";
        assertEquals(expectedResult, processRequest(request, RefreshUserCommand.class));
    }

    @Test
    public void testRefreshUserCommandReturnsFailureResultWhenUserNameIsNull() {
        user = new GamesUser(null, "111111");
        session.setAttribute(LobbyUiController.GAMES_USER_SESSION_VARIABLE, user);
        request.addParameter("command", "refresh_user");
        String expectedResult = "{\"result\":\"failure\"}";
        assertEquals(expectedResult, processRequest(request, RefreshUserCommand.class));
    }

    @Test
    public void testRefreshUserCommandReturnsFailureResultWhenSessionIsNull() {
        request.setSession(null);
        request.addParameter("command", "refresh_user");
        String expectedResult = "{\"result\":\"failure\"}";
        assertEquals(expectedResult, processRequest(request, RefreshUserCommand.class));
    }
}
