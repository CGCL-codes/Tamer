package nl.uva.saf.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import nl.uva.saf.fdl.types.CharacteristicType;
import nl.uva.saf.fdl.types.ConditionType;
import nl.uva.saf.simulation.ConditionSemantics;
import nl.uva.saf.simulation.FighterBot;
import nl.uva.saf.simulation.IConditionSemantics;
import nl.uva.saf.simulation.Vector2d;

public class ConditionSemanticsTest {

    private FighterBot fighter, enemy;

    private List<FighterBot> players;

    private IConditionSemantics semanticsEvaluator;

    @Test
    public void fighterIsEvenTest() {
        fighter.setAttribute(CharacteristicType.punchPower, 5);
        fighter.setAttribute(CharacteristicType.kickPower, 5);
        enemy.setAttribute(CharacteristicType.punchPower, 5);
        enemy.setAttribute(CharacteristicType.kickPower, 5);
        HashMap<ConditionType, Boolean> truths = semanticsEvaluator.getConditionStates(fighter, players);
        Assert.assertFalse(truths.get(ConditionType.much_stronger));
        Assert.assertFalse(truths.get(ConditionType.stronger));
        Assert.assertTrue(truths.get(ConditionType.even));
        Assert.assertFalse(truths.get(ConditionType.weaker));
        Assert.assertFalse(truths.get(ConditionType.much_weaker));
    }

    @Test
    public void fighterIsFarTest() {
        fighter.setAttribute(CharacteristicType.punchReach, 10);
        fighter.setAttribute(CharacteristicType.kickReach, 10);
        fighter.setPosition(new Vector2d(50, 0));
        enemy.setPosition(new Vector2d(151, 0));
        HashMap<ConditionType, Boolean> truths = semanticsEvaluator.getConditionStates(fighter, players);
        Assert.assertFalse(truths.get(ConditionType.near));
        Assert.assertTrue(truths.get(ConditionType.far));
    }

    @Test
    public void fighterIsMuchStrongerTest() {
        fighter.setAttribute(CharacteristicType.punchPower, 10);
        fighter.setAttribute(CharacteristicType.kickPower, 10);
        enemy.setAttribute(CharacteristicType.punchPower, 1);
        enemy.setAttribute(CharacteristicType.kickPower, 1);
        HashMap<ConditionType, Boolean> truths = semanticsEvaluator.getConditionStates(fighter, players);
        Assert.assertTrue(truths.get(ConditionType.much_stronger));
        Assert.assertFalse(truths.get(ConditionType.stronger));
        Assert.assertFalse(truths.get(ConditionType.even));
        Assert.assertFalse(truths.get(ConditionType.weaker));
        Assert.assertFalse(truths.get(ConditionType.much_weaker));
    }

    @Test
    public void fighterIsMuchWeakerTest() {
        fighter.setAttribute(CharacteristicType.punchPower, 1);
        fighter.setAttribute(CharacteristicType.kickPower, 1);
        enemy.setAttribute(CharacteristicType.punchPower, 10);
        enemy.setAttribute(CharacteristicType.kickPower, 10);
        HashMap<ConditionType, Boolean> truths = semanticsEvaluator.getConditionStates(fighter, players);
        Assert.assertFalse(truths.get(ConditionType.much_stronger));
        Assert.assertFalse(truths.get(ConditionType.stronger));
        Assert.assertFalse(truths.get(ConditionType.even));
        Assert.assertFalse(truths.get(ConditionType.weaker));
        Assert.assertTrue(truths.get(ConditionType.much_weaker));
    }

    @Test
    public void fighterIsNearTest() {
        fighter.setAttribute(CharacteristicType.punchReach, 10);
        fighter.setAttribute(CharacteristicType.kickReach, 10);
        fighter.setPosition(new Vector2d(50, 0));
        enemy.setPosition(new Vector2d(51, 0));
        HashMap<ConditionType, Boolean> truths = semanticsEvaluator.getConditionStates(fighter, players);
        Assert.assertTrue(truths.get(ConditionType.near));
        Assert.assertFalse(truths.get(ConditionType.far));
    }

    @Test
    public void fighterIsStrongerTest() {
        fighter.setAttribute(CharacteristicType.punchPower, 8);
        fighter.setAttribute(CharacteristicType.kickPower, 8);
        enemy.setAttribute(CharacteristicType.punchPower, 4);
        enemy.setAttribute(CharacteristicType.kickPower, 4);
        HashMap<ConditionType, Boolean> truths = semanticsEvaluator.getConditionStates(fighter, players);
        Assert.assertFalse(truths.get(ConditionType.much_stronger));
        Assert.assertTrue(truths.get(ConditionType.stronger));
        Assert.assertFalse(truths.get(ConditionType.even));
        Assert.assertFalse(truths.get(ConditionType.weaker));
        Assert.assertFalse(truths.get(ConditionType.much_weaker));
    }

    @Test
    public void fighterIsWeakerTest() {
        fighter.setAttribute(CharacteristicType.punchPower, 4);
        fighter.setAttribute(CharacteristicType.kickPower, 4);
        enemy.setAttribute(CharacteristicType.punchPower, 8);
        enemy.setAttribute(CharacteristicType.kickPower, 8);
        HashMap<ConditionType, Boolean> truths = semanticsEvaluator.getConditionStates(fighter, players);
        Assert.assertFalse(truths.get(ConditionType.much_stronger));
        Assert.assertFalse(truths.get(ConditionType.stronger));
        Assert.assertFalse(truths.get(ConditionType.even));
        Assert.assertTrue(truths.get(ConditionType.weaker));
        Assert.assertFalse(truths.get(ConditionType.much_weaker));
    }

    @Before
    public void setup() {
        fighter = new FighterBot(null);
        enemy = new FighterBot(null);
        semanticsEvaluator = new ConditionSemantics();
        players = new ArrayList<FighterBot>();
        players.add(fighter);
        players.add(enemy);
    }
}
