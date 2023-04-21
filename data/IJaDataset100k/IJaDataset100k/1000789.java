package Puzzles;

/**
 * @author Oleg Orlov
 */
public class Puzzle34 {

    public static void main(String[] args) {
        final int START = 2000000000;
        int count = 0;
        double f = START;
        double cond = START + 50;
        for (; f < START + 50; f++) count++;
        System.out.println(count);
    }
}
