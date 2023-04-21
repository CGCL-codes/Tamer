package lotto649;

import java.util.ArrayList;
import java.util.Random;

public class Lotto {

    private static Random rnd;

    public static void main(String[] args) {
        rnd = new Random();
        ArrayList<Integer> choices = new ArrayList<Integer>();
        for (int i = 1; i <= 49; ++i) {
            choices.add(i);
        }
        ArrayList<Integer> myPicks = new ArrayList<Integer>();
        for (int i = 1; i <= 6; ++i) {
            int randIndex = rnd.nextInt(choices.size());
            int pick = choices.get(randIndex);
            myPicks.add(pick);
            choices.remove(randIndex);
        }
        System.out.printf("%s", myPicks);
    }
}
