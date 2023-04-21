package rcsceneTests.junit.sceneInfo;

import sceneInfo.PlayerInfo;
import junit.framework.TestCase;

public class PlayerInfoTest extends TestCase {

    /** Test the constructors for the PlayerInfo class
	 * 
	 */
    public void testConstructors() {
        PlayerInfo pi = new PlayerInfo();
        assertTrue(pi.getType().equals("player"));
        assertTrue(pi.getTeamName().equals(""));
        assertEquals(pi.getUniformNumber(), 0);
        assertFalse(pi.isGoalie());
        assertEquals(pi.getBodyDir(), 0.0f);
        assertEquals(pi.getHeadDir(), 0.0f);
        pi = new PlayerInfo("myTeam", 5, true);
        assertTrue(pi.getType().equals("player"));
        assertTrue(pi.getTeamName().equals("myTeam"));
        assertEquals(pi.getUniformNumber(), 5);
        assertTrue(pi.isGoalie());
        assertEquals(pi.getBodyDir(), 0.0f);
        assertEquals(pi.getHeadDir(), 0.0f);
        pi = new PlayerInfo("randomname", 99, 5, 8);
        assertTrue(pi.getType().equals("player"));
        assertTrue(pi.getTeamName().equals("randomname"));
        assertEquals(pi.getUniformNumber(), 99);
        assertEquals(pi.getBodyDir(), 5f);
        assertEquals(pi.getHeadDir(), 8f);
    }

    /** Test the getters and setters
	 * 
	 */
    public void testGettersAndSetters() {
        PlayerInfo pi = new PlayerInfo();
        pi.setTeamName("newTeamName");
        assertTrue(pi.getTeamName().equals("newTeamName"));
        pi.setTeamName("\"newTeamName\"");
        assertTrue(pi.getTeamName().equals("newTeamName"));
        pi.setUniformNumber(25);
        assertEquals(pi.getUniformNumber(), 25);
        pi.setGoalie(true);
        assertTrue(pi.isGoalie());
        pi.setBodyDir(12.8f);
        assertEquals(pi.getBodyDir(), 12.8f);
        pi.setHeadDir(9.2f);
        assertEquals(pi.getHeadDir(), 9.2f);
    }
}
