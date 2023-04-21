package test.java.unittest;

import main.java.chessfootball.model.game.*;
import main.java.chessfootball.model.play.Put;
import main.java.chessfootball.model.regulation.Mode;
import main.java.chessfootball.model.regulation.Period;
import main.java.chessfootball.model.rule.GameRule;
import main.java.chessfootball.model.rule.MatchRule;
import main.java.chessfootball.model.rule.Rule;
import main.java.chessfootball.model.rule.TimeRule;
import org.junit.Assert;
import org.junit.Test;
import test.java.snapshot.GameSnapshot;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Igor
 * Date: 24.07.2011
 * Time: 10:16:23
 */
public class GameTest extends Assert {

    @Test
    public void testStartGame() throws Exception {
        Game game = GameSnapshot.startGameTwoTimes();
        System.out.println(game);
        assertEquals(Mode.OUT_GAME, game.getMatch().getMode());
        assertEquals(Period.FT, game.getMatch().getTime().getPeriod());
        assertEquals(Side.HOME, game.getMatch().getTime().getTurnSide());
        assertEquals(Side.HOME, game.getMatch().getTime().getStartSide());
        assertEquals((Integer) 0, game.getMatch().getTime().getTurns());
        assertEquals((Integer) 0, game.getMatch().getTime().getMoves());
        assertEquals(Half.RIGHT, game.getField().getTeam(Side.HOME).getHalf());
        assertEquals(Half.LEFT, game.getField().getTeam(Side.OPPOSING).getHalf());
        assertEquals(Half.RIGHT, game.getField().getTeam(Side.HOME).getHalf());
        assertEquals(new Cell(15, 9), game.getField().getBall().getCell());
    }

    @Test
    public void testTurn() throws Exception {
        GameRule gameRule = new GameRule(true, 8, true, true, 3, true);
        List<TimeRule> times = new ArrayList<TimeRule>();
        times.add(new TimeRule(Period.FT, 2, false, false, false));
        times.add(new TimeRule(Period.ST, 2, false, false, false));
        times.add(new TimeRule(Period.OT, 2, false, true, false));
        Uniform[] uniforms = new Uniform[2];
        uniforms[0] = Uniform.BLUE;
        uniforms[1] = Uniform.RED;
        MatchRule matchRule = new MatchRule(times, 3, 10, null, Side.HOME, Half.RIGHT, uniforms);
        Rule rule = new Rule(gameRule, matchRule);
        Game game = new Game(rule);
        System.out.println(game);
        assertEquals(Half.RIGHT, game.getField().getTeam(Side.HOME).getHalf());
        assertEquals(Half.LEFT, game.getField().getTeam(Side.OPPOSING).getHalf());
        game.put(new Put(Side.HOME, 1, new Cell(29, 9)));
        game.put(new Put(Side.HOME, 2, new Cell(20, 9)));
        game.put(new Put(Side.HOME, 3, new Cell(20, 4)));
        game.turn();
        System.out.println(game);
        assertEquals(Side.OPPOSING, game.getMatch().getTime().getTurnSide());
        game.put(new Put(Side.OPPOSING, 1, new Cell(5, 9)));
        game.put(new Put(Side.OPPOSING, 2, new Cell(13, 9)));
        game.put(new Put(Side.OPPOSING, 3, new Cell(13, 15)));
        game.turn();
        System.out.println(game);
        assertEquals(Mode.IN_GAME, game.getMatch().getMode());
        assertEquals(Side.HOME, game.getMatch().getTime().getTurnSide());
        assertEquals(Period.FT, game.getMatch().getTime().getPeriod());
        assertEquals((Integer) 0, game.getMatch().getTime().getTurns());
        game.turn();
        assertEquals(Mode.IN_GAME, game.getMatch().getMode());
        assertEquals(Side.OPPOSING, game.getMatch().getTime().getTurnSide());
        assertEquals(Period.FT, game.getMatch().getTime().getPeriod());
        assertEquals((Integer) 1, game.getMatch().getTime().getTurns());
        game.turn();
        System.out.println(game);
        assertEquals(Mode.OUT_GAME, game.getMatch().getMode());
        assertEquals(Side.OPPOSING, game.getMatch().getTime().getTurnSide());
        assertEquals(Period.ST, game.getMatch().getTime().getPeriod());
        assertEquals((Integer) 2, game.getMatch().getTime().getTurns());
        assertEquals(Half.LEFT, game.getField().getTeam(Side.HOME).getHalf());
        assertEquals(Half.RIGHT, game.getField().getTeam(Side.OPPOSING).getHalf());
        game.put(new Put(Side.OPPOSING, 1, new Cell(29, 9)));
        game.put(new Put(Side.OPPOSING, 2, new Cell(20, 9)));
        game.put(new Put(Side.OPPOSING, 3, new Cell(20, 4)));
        game.turn();
        System.out.println(game);
        assertEquals(Side.HOME, game.getMatch().getTime().getTurnSide());
        game.put(new Put(Side.HOME, 1, new Cell(5, 9)));
        game.put(new Put(Side.HOME, 2, new Cell(13, 9)));
        game.put(new Put(Side.HOME, 3, new Cell(13, 15)));
        game.turn();
        System.out.println(game);
        assertEquals(Mode.IN_GAME, game.getMatch().getMode());
        assertEquals(Side.OPPOSING, game.getMatch().getTime().getTurnSide());
        assertEquals(Period.ST, game.getMatch().getTime().getPeriod());
        assertEquals((Integer) 2, game.getMatch().getTime().getTurns());
        game.getField().getBall().put(new Cell(0, 8));
        game.turn();
        System.out.println(game);
        assertEquals(Mode.OUT_GAME, game.getMatch().getMode());
        assertEquals(Side.HOME, game.getMatch().getTime().getTurnSide());
        assertEquals(Period.ST, game.getMatch().getTime().getPeriod());
        assertEquals((Integer) 3, game.getMatch().getTime().getTurns());
        assertEquals(Half.LEFT, game.getField().getTeam(Side.HOME).getHalf());
        assertEquals(Half.RIGHT, game.getField().getTeam(Side.OPPOSING).getHalf());
        game.put(new Put(Side.HOME, 1, new Cell(5, 9)));
        game.put(new Put(Side.HOME, 2, new Cell(13, 9)));
        game.put(new Put(Side.HOME, 3, new Cell(13, 15)));
        game.turn();
        System.out.println(game);
        game.put(new Put(Side.OPPOSING, 1, new Cell(29, 9)));
        game.put(new Put(Side.OPPOSING, 2, new Cell(20, 9)));
        game.put(new Put(Side.OPPOSING, 3, new Cell(20, 4)));
        game.turn();
        System.out.println(game);
        assertEquals(Mode.IN_GAME, game.getMatch().getMode());
        assertEquals(Side.HOME, game.getMatch().getTime().getTurnSide());
        assertEquals(Period.ST, game.getMatch().getTime().getPeriod());
        assertEquals((Integer) 3, game.getMatch().getTime().getTurns());
        game.getField().getBall().put(new Cell(29, 8));
        game.turn();
        System.out.println(game);
        assertEquals(Mode.OUT_GAME, game.getMatch().getMode());
        assertEquals(Side.HOME, game.getMatch().getTime().getTurnSide());
        assertEquals(Period.OT, game.getMatch().getTime().getPeriod());
        assertEquals((Integer) 4, game.getMatch().getTime().getTurns());
        assertEquals(Half.LEFT, game.getField().getTeam(Side.OPPOSING).getHalf());
        assertEquals(Half.RIGHT, game.getField().getTeam(Side.HOME).getHalf());
        game.put(new Put(Side.HOME, 1, new Cell(29, 9)));
        game.put(new Put(Side.HOME, 2, new Cell(20, 9)));
        game.put(new Put(Side.HOME, 3, new Cell(20, 4)));
        game.turn();
        System.out.println(game);
        assertEquals(Side.OPPOSING, game.getMatch().getTime().getTurnSide());
        game.put(new Put(Side.OPPOSING, 1, new Cell(5, 9)));
        game.put(new Put(Side.OPPOSING, 2, new Cell(13, 9)));
        game.put(new Put(Side.OPPOSING, 3, new Cell(13, 15)));
        game.turn();
        game.getField().getBall().put(new Cell(0, 8));
        game.turn();
        assertEquals(Mode.OUT_GAME, game.getMatch().getMode());
        assertEquals(Side.HOME, game.getMatch().getTime().getTurnSide());
        assertEquals(Period.OT, game.getMatch().getTime().getPeriod());
        assertEquals((Integer) 4, game.getMatch().getTime().getTurns());
    }
}
