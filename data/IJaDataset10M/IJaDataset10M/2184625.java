package net.kortsoft.gameportlet.culandcon;

import net.kortsoft.gameportlet.culandcon.Population.PopulationSpeciality;
import net.kortsoft.gameportlet.culandcon.gamestuff.Die;
import net.kortsoft.gameportlet.culandcon.gamestuff.Dice;

public abstract class LeaderBenefits {

    static CultureBenefit ruler = new CultureBenefit() {

        public void execute(Culture culture) {
            int roll = Dice.die6.roll();
            culture.addGold(roll);
            culture.getCulAndConGame().getGameLog().log("Ruler leader generates " + roll + " gold to your culture");
        }
    };

    static CultureBenefit general = CultureBenefits.BonusBattleRoll;

    public static CultureBenefit thinker = CultureBenefits.BonusResearchRoll;

    public static CultureBenefit builder = new CultureBenefit() {

        public void execute(Culture culture) {
            int roll = Dice.die6.roll();
            culture.addPopulation(PopulationSpeciality.Labor, roll);
            culture.getCulAndConGame().getGameLog().log("This builder leader attracts " + roll + " labor specialist for your culture");
        }
    };

    public static CultureBenefit religious = new CultureBenefit() {

        public void execute(Culture culture) {
            int roll = Dice.die6.roll();
            culture.addPopulation(roll);
            culture.getCulAndConGame().getGameLog().log("This religious leader attracts " + roll + " agriculture specialist for your culture");
        }
    };

    public static CultureBenefit diplomat = new DiplomatLeaderBenefit();

    private static final class DiplomatLeaderBenefit implements CultureBenefit, AvoidAttack {

        public void execute(Culture culture) {
            culture.addAvoidAttackBonus(this);
        }

        public boolean avoidAttack() {
            return new Die(6).roll() > 4;
        }
    }
}
