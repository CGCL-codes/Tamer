package saf.structure.intelligence;

import saf.game.GameConstant;
import saf.game.state.BotState;
import saf.game.state.GameState;
import saf.structure.Condition;
import saf.structure.ConditionSimple;

public class ConditionIntelligence implements GameConstant {

    public static boolean checkConditionForAlways(Condition condition) {
        if (condition instanceof ConditionSimple) {
            if (CONDITION_ALWAYS.equalsIgnoreCase(((ConditionSimple) condition).getValue())) return true; else return false;
        } else return false;
    }

    public static boolean checkConditionSimple(ConditionSimple conditionSimple, BotState botState, GameState gameState) {
        String value = conditionSimple.getValue();
        int myHp = botState.getHitpoints();
        int otherHp = 0;
        for (BotState state : gameState.getBotStates()) {
            if (!state.equals(botState)) {
                otherHp = state.getHitpoints();
            }
        }
        int hpDifference = myHp - otherHp;
        int distance = gameState.getDistance();
        if (value.equals(CONDITION_ALWAYS)) return true;
        if (value.equals(CONDITION_FAR)) return distance >= CONDITION_NEAR_DISTANCE;
        if (value.equals(CONDITION_NEAR)) return distance < CONDITION_NEAR_DISTANCE;
        if (value.equals(CONDITION_MUCHSTRONGER) || value.equals(CONDITION_STRONGER)) return hpDifference > CONDITION_STRENGHTS_HP_DIFFERENCE.get(value);
        if (value.equals(CONDITION_EVEN)) return hpDifference >= CONDITION_STRENGHTS_HP_DIFFERENCE.get(CONDITION_WEAKER) && hpDifference <= CONDITION_STRENGHTS_HP_DIFFERENCE.get(CONDITION_STRONGER);
        if (value.equals(CONDITION_WEAKER) || value.equals(CONDITION_MUCHWEAKER)) return hpDifference < CONDITION_STRENGHTS_HP_DIFFERENCE.get(value);
        return false;
    }
}
