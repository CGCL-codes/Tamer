package test.common.model.game;

import common.model.exceptions.JawException;
import common.model.exceptions.NoTurnsLeftException;
import common.model.exceptions.NotYourTurnException;
import common.model.game.AwGame;
import common.model.game.GameConfig;
import common.model.game.GameState;
import common.model.game.WeatherType;
import common.model.game.interfaces.Game;
import common.model.game.player.AwPlayer;
import common.model.game.player.Player;
import common.model.map.interfaces.GameMap;
import common.model.property.City;
import org.junit.Assert;
import org.junit.Test;
import test.TestUtil;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stefan
 * @since 2.0
 */
public class GameTest {

    @Test
    public void testGame() {
        int JOOP_START_BUDGET = 5000;
        int JOOP_SINGLE_CITY_FUNDS = 1200;
        GameMap map = TestUtil.getMap();
        List<Player> players = new ArrayList<Player>();
        Player p1 = new AwPlayer(0, "Joop", Color.GREEN, JOOP_START_BUDGET, 0, false, false, null);
        Player p2 = TestUtil.p2;
        Player p3 = TestUtil.p3;
        Player p4 = TestUtil.p4;
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        City city = TestUtil.getCityFundsOnly(JOOP_SINGLE_CITY_FUNDS);
        map.getTile(0, 0).setProperty(city);
        city.setOwner(p1);
        GameConfig gc = new GameConfig();
        gc.setStartWeather(WeatherType.CLEAR);
        gc.setTurnLimit(500);
        gc.setStaticWeather(true);
        gc.setCityfunds(JOOP_SINGLE_CITY_FUNDS);
        Game game = new AwGame(map, players, gc);
        game.startGame(p4);
        Assert.assertEquals(GameState.IN_GAME, game.getState());
        Assert.assertEquals(p4, game.getActivePlayer());
        try {
            game.endTurn(p4);
        } catch (JawException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(1, game.getTurn());
        Assert.assertEquals(p1, game.getActivePlayer());
        Assert.assertEquals(p1.getBudget(), JOOP_START_BUDGET + JOOP_SINGLE_CITY_FUNDS);
    }

    @Test(expected = NotYourTurnException.class)
    public void testEndTurnWithNonActivePlayer() throws JawException {
        Game game = TestUtil.getStartedGame();
        game.endTurn(TestUtil.p2);
    }

    @Test(expected = NoTurnsLeftException.class)
    public void testEndTurnOnLastTurn() throws JawException {
        AwGame game = (AwGame) TestUtil.getGame();
        GameConfig gc = new GameConfig();
        gc.setTurnLimit(1);
        game.applyGameConfig(gc);
        game.startGame(TestUtil.p1);
        game.endTurn(TestUtil.p1);
    }

    @Test
    public void testEndTurn() throws JawException {
        Game game = TestUtil.getStartedGame();
        game.endTurn(TestUtil.p1);
        game.endTurn(TestUtil.p2);
        game.endTurn(TestUtil.p3);
        game.endTurn(TestUtil.p4);
        game.endTurn(TestUtil.p1);
        game.endTurn(TestUtil.p2);
    }
}
