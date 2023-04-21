package nl.uva.saf.test;

import java.util.ArrayList;
import java.util.HashMap;
import junit.framework.Assert;
import nl.uva.saf.fdl.ActionSelector;
import nl.uva.saf.fdl.ast.Action;
import nl.uva.saf.fdl.ast.Behaviour;
import nl.uva.saf.fdl.ast.ConditionAlways;
import nl.uva.saf.fdl.ast.ConditionAnd;
import nl.uva.saf.fdl.ast.FightAction;
import nl.uva.saf.fdl.ast.FightChoice;
import nl.uva.saf.fdl.ast.Fighter;
import nl.uva.saf.fdl.ast.FighterAttribute;
import nl.uva.saf.fdl.ast.ITreeNode;
import nl.uva.saf.fdl.ast.MoveAction;
import nl.uva.saf.fdl.ast.MoveChoice;
import nl.uva.saf.fdl.ast.Rule;
import nl.uva.saf.fdl.types.ConditionType;
import nl.uva.saf.fdl.types.FightActionType;
import nl.uva.saf.fdl.types.MoveActionType;
import nl.uva.saf.test.mocks.RandomMock;
import org.junit.Before;
import org.junit.Test;

public class ActionSelectorTest {

    private ITreeNode fighter;

    @Test
    public void alwaysConditionSelectedWithAlwaysOnTrueTest() {
        HashMap<ConditionType, Boolean> truthTable = new HashMap<ConditionType, Boolean>();
        truthTable.put(ConditionType.always, true);
        RandomMock numberGenerator = new RandomMock();
        ActionSelector selector = new ActionSelector(numberGenerator);
        selector.selectActions(fighter, truthTable);
        Assert.assertTrue(selector.getMoveAction() == MoveActionType.stand);
        Assert.assertTrue(selector.getFightAction() == FightActionType.block_low);
    }

    @Test
    public void alwaysConditionSelectedWithEverythingFalseTest() {
        HashMap<ConditionType, Boolean> truthTable = new HashMap<ConditionType, Boolean>();
        truthTable.put(ConditionType.always, false);
        truthTable.put(ConditionType.near, false);
        truthTable.put(ConditionType.far, false);
        truthTable.put(ConditionType.much_stronger, false);
        truthTable.put(ConditionType.stronger, false);
        truthTable.put(ConditionType.even, false);
        truthTable.put(ConditionType.weaker, false);
        truthTable.put(ConditionType.much_weaker, false);
        RandomMock numberGenerator = new RandomMock();
        ActionSelector selector = new ActionSelector(numberGenerator);
        selector.selectActions(fighter, truthTable);
        Assert.assertTrue(selector.getMoveAction() == MoveActionType.stand);
        Assert.assertTrue(selector.getFightAction() == FightActionType.block_low);
    }

    @Test
    public void selectAlwaysTest() {
        RandomMock numberGenerator = new RandomMock();
        ActionSelector selector = new ActionSelector(numberGenerator);
        selector.selectAlways(fighter);
        Assert.assertTrue(selector.getMoveAction() == MoveActionType.stand);
        Assert.assertTrue(selector.getFightAction() == FightActionType.block_low);
    }

    @Before
    public void setup() {
        MoveChoice moveChoice;
        FightChoice fightChoice;
        Rule rule;
        ArrayList<Action> actions;
        Behaviour behaviour;
        ArrayList<FighterAttribute> attributes;
        ConditionAlways condition;
        ArrayList<String> operands;
        attributes = new ArrayList<FighterAttribute>();
        actions = new ArrayList<Action>();
        actions.add(new MoveAction("move_away"));
        actions.add(new MoveAction("stand"));
        moveChoice = new MoveChoice(actions);
        actions = new ArrayList<Action>();
        actions.add(new FightAction("kick_high"));
        fightChoice = new FightChoice(actions);
        rule = new Rule(moveChoice, fightChoice);
        operands = new ArrayList<String>();
        operands.add("near");
        operands.add("weaker");
        condition = new ConditionAnd(operands);
        behaviour = new Behaviour(condition, rule);
        attributes.add(behaviour);
        actions = new ArrayList<Action>();
        actions.add(new MoveAction("stand"));
        actions.add(new MoveAction("crouch"));
        moveChoice = new MoveChoice(actions);
        actions = new ArrayList<Action>();
        actions.add(new FightAction("block_low"));
        actions.add(new FightAction("punch_high"));
        fightChoice = new FightChoice(actions);
        rule = new Rule(moveChoice, fightChoice);
        behaviour = new Behaviour(new ConditionAlways(), rule);
        attributes.add(behaviour);
        fighter = new Fighter("ActionSelectorTest", attributes);
    }
}
