package ttu.iti0011.pold.praktikum01;

import java.util.ArrayList;

public class YahtzeePCFactory {

    public static final ArrayList<YahtzeePC> available() {
        final ArrayList<YahtzeePC> available = new ArrayList<YahtzeePC>();
        available.add(new YahtzeePCOne());
        available.add(new YahtzeePCTwo());
        available.add(new YahtzeePCThree());
        available.add(new YahtzeePCFour());
        available.add(new YahtzeePCFive());
        available.add(new YahtzeePCSix());
        available.add(new YahtzeePCThreeOfKing());
        available.add(new YahtzeePCFourhOfKing());
        available.add(new YahtzeePCFullHouse());
        available.add(new YahtzeePCSmallStraight());
        available.add(new YahtzeePCLargStraight());
        available.add(new YahtzeePCYahtzee());
        available.add(new YahtzeePCChance());
        return available;
    }

    public static ArrayList<YahtzeePC> getWinning(DiceRound diceRound) {
        return getWinning(diceRound, false);
    }

    public static ArrayList<YahtzeePC> getWinning(DiceRound diceRound, boolean withDebug) {
        ArrayList<YahtzeePC> result = new ArrayList<YahtzeePC>();
        for (YahtzeePC className : available()) {
            YahtzeePC scoreInstance;
            try {
                scoreInstance = className.getClass().newInstance();
                scoreInstance.setDiceRound(diceRound);
            } catch (Exception e) {
                continue;
            }
            result.add(scoreInstance);
            if (withDebug) {
                String outputName = scoreInstance.getClass().getSimpleName();
                System.out.print(Renderer.fillWithSpaces(outputName, 30, Renderer.BEFORE));
                if (false == scoreInstance.isWinning()) {
                    System.out.println("LOSS");
                    continue;
                }
                System.out.println("WIN: " + " with points: " + scoreInstance.getPoints());
            }
        }
        return result;
    }
}
