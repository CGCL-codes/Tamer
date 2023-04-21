package bbalc.tools;

/**
 * The Propability class calculates the propabilities for an action.
 * @author dirk
 */
public class Propability {

    /**
	 * calculates the propability for a standard action.
	 * @param target (int) the target number ( 1 <= target <= 6 )
	 * @param modificator (int) the modificator which is applied to this action
	 * @return (int) a propability
	 */
    public static int getActionPropability(int target, int modificator) {
        int goodDice = 7 + modificator - target;
        if (goodDice < 1) goodDice = 1; else if (goodDice > 5) goodDice = 5;
        return ((100 / 6) + 1) * goodDice;
    }

    /**
	 * calculates the propability for a standard action with pro skill used.
	 * @param target (int) the target number ( 1 <= target <= 6 ) 
	 * @param modificator (int) the modificator which is applied to this action
	 * @return (int) a propability
	 */
    public static int getActionPropabilityPro(int target, int modificator) {
        int base = getActionPropability(target, modificator);
        return base + ((100 - base) * base / 200);
    }

    /**
	 * calculates the propability for a standard action with a reroll.
	 * @param target (int) the target number ( 1 <= target <= 6 )  
	 * @param modificator (int) the modificator which is applied to this action
	 * @return (int) a propability
	 */
    public static int getActionPropabilityReroll(int target, int modificator) {
        int base = getActionPropability(target, modificator);
        return base + ((100 - base) * base / 100);
    }

    /**
	 * calculates the propability for a block action with pro skill used.
	 * @param dices (int) number of dices ( -3 <= dices <= 3 )
	 * @param target (int) the target number ( 1 <= target <= 6 ) (see ???) 
	 * @return (int) a propability
	 */
    public static int getBlockPropabilityPro(int dices, int target) {
        int base = getBlockPropability(dices, target);
        return base + ((100 - base) * base / 200);
    }

    /**
	 * calculates the propability for a block action with a reroll.
	 * @param dices (int) number of dices ( -3 <= dices <= 3 )
	 * @param target (int) the target number ( 1 <= target <= 6 ) (see ???) 
	 * @return (int) a propability
	 */
    public static int getPropabilityReroll(int dices, int target) {
        int base = getBlockPropability(dices, target);
        return base + ((100 - base) * base / 100);
    }

    /**
	 * calculates the propability for a block action.
	 * @param dices (int) number of dices ( -3 <= dices <= 3 )
	 * @param target (int) the target number ( 1 <= target <= 6 ) (see ???) 
	 * @return (int) a propability
	 */
    public static int getBlockPropability(int dices, int target) {
        if ((target > 0) && (target < 7)) {
            if ((dices > 0) && (dices <= 3)) return (int) (100 * (1 - Math.pow(((double) target - 1) / 6, dices))); else if ((dices < 0) && (dices >= -3)) return (int) (100 * (Math.pow((7.0 - target) / 6, -dices))); else throw new RuntimeException("Propability.getBlockPropability> parameter out of bounds: dices=" + dices);
        } else throw new RuntimeException("Propability.getBlockPropability> parameter out of bounds: target=" + target);
    }
}
