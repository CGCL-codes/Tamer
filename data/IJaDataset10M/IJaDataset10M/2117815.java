package l2.utils;

import java.util.Comparator;

public class HigherXpPerHpFirstComparator implements Comparator<Mob> {

    private final int playerLvl;

    private final double proportion;

    private final double altXP;

    public HigherXpPerHpFirstComparator(int playerLvl, double proportion, double altXP) {
        this.playerLvl = playerLvl;
        this.proportion = proportion;
        this.altXP = altXP;
    }

    @Override
    public int compare(Mob m0, Mob m1) {
        int xp0 = XPUtils.calculateXP(m0.getLevel(), m0.getExp(), this.playerLvl, this.proportion, this.altXP);
        int xp1 = XPUtils.calculateXP(m1.getLevel(), m1.getExp(), this.playerLvl, this.proportion, this.altXP);
        double hp0_xp0 = xp0 * 1d / m0.getHp();
        double hp1_xp1 = xp1 * 1d / m1.getHp();
        return hp0_xp0 > hp1_xp1 ? 1 : hp1_xp1 > hp0_xp0 ? -1 : 0;
    }
}
