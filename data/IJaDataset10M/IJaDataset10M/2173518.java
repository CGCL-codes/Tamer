package client;

import static org.junit.Assert.*;
import static org.junit.Assert.assertSame;
import java.util.LinkedList;
import java.util.List;
import messages.global.GameDetailsMsg;
import org.junit.Before;
import org.junit.Test;

public class GameDetailsMsgTest {

    GameDetailsMsg gameDetailsMsg;

    List<String> maps;

    @Before
    public void setUp() {
        maps = new LinkedList<String>();
        maps.add("map");
        maps.add("map2");
        gameDetailsMsg = new GameDetailsMsg("Name", maps, 5, 3);
    }

    @Test
    public void testGameDetailsMsg() {
        assertNotNull(gameDetailsMsg);
    }

    @Test
    public void testGetGameName() {
        assertSame(gameDetailsMsg.getGameName(), "Name");
    }

    @Test
    public void testGetMaps() {
        assertSame(gameDetailsMsg.getMaps(), maps);
    }

    @Test
    public void testGetNrOfPlayers() {
        assertSame(gameDetailsMsg.getNrOfPlayers(), 5);
    }

    @Test
    public void testGetTotalRounds() {
        assertSame(gameDetailsMsg.getTotalRounds(), 3);
    }
}
