package net.sourceforge.plantuml.sudoku;

import java.util.Random;

public class SudokuDLX implements ISudoku {

    private final String tab[];

    private final long seed;

    private final long rate;

    public SudokuDLX(Long seed) {
        if (seed == null) {
            this.seed = Math.abs(new Random().nextLong());
        } else {
            this.seed = Math.abs(seed.longValue());
        }
        final DLXEngine engine = new DLXEngine(new Random(this.seed));
        final String s = engine.generate(10000, 100000);
        rate = engine.rate(s.replace("\n", "").trim());
        tab = s.split("\\s");
    }

    public long getRatting() {
        return rate;
    }

    public long getSeed() {
        return seed;
    }

    public int getGiven(int x, int y) {
        final char c = tab[x].charAt(y);
        if (c == '.') {
            return 0;
        }
        return c - '0';
    }

    public void print() {
        for (String s : tab) {
            System.err.println(s);
        }
        System.err.println("Rate=" + rate);
        System.err.println("Seed=" + Long.toString(seed, 36).toUpperCase());
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            final SudokuDLX sudoku = new SudokuDLX(null);
            sudoku.print();
        }
    }
}
