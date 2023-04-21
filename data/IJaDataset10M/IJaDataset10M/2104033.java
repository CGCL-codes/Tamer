package fr.fg.server.test.action.player;

import java.util.List;
import org.json.JSONObject;
import fr.fg.server.data.DataAccess;
import fr.fg.server.data.Player;
import fr.fg.server.data.Treaty;
import fr.fg.server.test.action.TestAction;

public class TestTreaties extends TestAction {

    private Player player1, player2;

    private Treaty treaty;

    public void init() {
        player1 = new Player("TestPlayerTreaty1", "", "", "", "");
        DataAccess.save(player1);
        player2 = new Player("TestPlayerTreaty2", "", "", "", "");
        DataAccess.save(player2);
    }

    public void testSetTreaties() throws Exception {
        setPlayer(player1.getLogin());
        JSONObject answer = doRequest("declarewar", "player=" + player2.getLogin());
        assertEquals("success", answer.getString("type"));
        treaty = null;
        List<Treaty> treaties = player1.getTreaties();
        synchronized (treaties) {
            for (Treaty eachTreaty : treaties) {
                if (eachTreaty.implyPlayer(player2.getId())) {
                    treaty = eachTreaty;
                    break;
                }
            }
        }
        assertNotNull(treaty);
        assertEquals("war", treaty.getType());
        assertEquals(0, treaty.getSource());
        answer = doRequest("offerpeace", "player=" + player2.getLogin());
        assertEquals("success", answer.getString("type"));
        treaty = null;
        treaties = player1.getTreaties();
        synchronized (treaties) {
            for (Treaty eachTreaty : treaties) {
                if (eachTreaty.implyPlayer(player2.getId())) {
                    treaty = eachTreaty;
                    break;
                }
            }
        }
        assertNotNull(treaty);
        assertEquals("war", treaty.getType());
        assertEquals(player1.getId(), treaty.getSource());
        setPlayer(player2.getLogin());
        answer = doRequest("offerpeace", "player=" + player1.getLogin() + "&accept=false");
        assertEquals("success", answer.getString("type"));
        treaty = null;
        treaties = player2.getTreaties();
        synchronized (treaties) {
            for (Treaty eachTreaty : treaties) {
                if (eachTreaty.implyPlayer(player1.getId())) {
                    treaty = eachTreaty;
                    break;
                }
            }
        }
        assertNotNull(treaty);
        assertEquals("war", treaty.getType());
        assertEquals(0, treaty.getSource());
        answer = doRequest("offerpeace", "player=" + player1.getLogin());
        assertEquals("success", answer.getString("type"));
        treaty = null;
        treaties = player2.getTreaties();
        synchronized (treaties) {
            for (Treaty eachTreaty : treaties) {
                if (eachTreaty.implyPlayer(player1.getId())) {
                    treaty = eachTreaty;
                    break;
                }
            }
        }
        assertNotNull(treaty);
        assertEquals("war", treaty.getType());
        assertEquals(player2.getId(), treaty.getSource());
        setPlayer(player1.getLogin());
        answer = doRequest("offerpeace", "player=" + player2.getLogin() + "&accept=true");
        assertEquals("success", answer.getString("type"));
        treaty = null;
        treaties = player1.getTreaties();
        synchronized (treaties) {
            for (Treaty eachTreaty : treaties) {
                if (eachTreaty.implyPlayer(player2.getId())) {
                    treaty = eachTreaty;
                    break;
                }
            }
        }
        assertNull(treaty);
        answer = doRequest("offerally", "player=" + player2.getLogin());
        assertEquals("success", answer.getString("type"));
        treaty = null;
        treaties = player1.getTreaties();
        synchronized (treaties) {
            for (Treaty eachTreaty : treaties) {
                if (eachTreaty.implyPlayer(player2.getId())) {
                    treaty = eachTreaty;
                    break;
                }
            }
        }
        assertNotNull(treaty);
        assertEquals("ally", treaty.getType());
        assertEquals(player1.getId(), treaty.getSource());
        setPlayer(player2.getLogin());
        answer = doRequest("offerally", "player=" + player1.getLogin() + "&accept=false");
        assertEquals("success", answer.getString("type"));
        treaty = null;
        treaties = player2.getTreaties();
        synchronized (treaties) {
            for (Treaty eachTreaty : treaties) {
                if (eachTreaty.implyPlayer(player1.getId())) {
                    treaty = eachTreaty;
                    break;
                }
            }
        }
        assertNull(treaty);
        answer = doRequest("offerally", "player=" + player1.getLogin());
        assertEquals("success", answer.getString("type"));
        treaty = null;
        treaties = player2.getTreaties();
        synchronized (treaties) {
            for (Treaty eachTreaty : treaties) {
                if (eachTreaty.implyPlayer(player1.getId())) {
                    treaty = eachTreaty;
                    break;
                }
            }
        }
        assertNotNull(treaty);
        assertEquals("ally", treaty.getType());
        assertEquals(player2.getId(), treaty.getSource());
        setPlayer(player1.getLogin());
        answer = doRequest("offerally", "player=" + player2.getLogin() + "&accept=true");
        assertEquals("success", answer.getString("type"));
        treaty = null;
        treaties = player1.getTreaties();
        synchronized (treaties) {
            for (Treaty eachTreaty : treaties) {
                if (eachTreaty.implyPlayer(player2.getId())) {
                    treaty = eachTreaty;
                    break;
                }
            }
        }
        assertNotNull(treaty);
        assertEquals("ally", treaty.getType());
        assertEquals(0, treaty.getSource());
        answer = doRequest("breakally", "player=" + player2.getLogin());
        assertEquals("success", answer.getString("type"));
        treaty = null;
        treaties = player1.getTreaties();
        synchronized (treaties) {
            for (Treaty eachTreaty : treaties) {
                if (eachTreaty.implyPlayer(player2.getId())) {
                    treaty = eachTreaty;
                    break;
                }
            }
        }
        assertNull(treaty);
    }

    public void cleanUp() {
        player1.delete();
        player2.delete();
    }
}
