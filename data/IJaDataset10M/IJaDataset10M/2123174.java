package com.dustedpixels.jasmin.chips;

/**
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class ChipsBenchmark {

    private static void test(Chip chip, int cycles) {
        long millis = System.currentTimeMillis();
        run(chip, cycles);
        millis = System.currentTimeMillis() - millis;
        millis = System.currentTimeMillis();
        run(chip, cycles);
        millis = System.currentTimeMillis() - millis;
        System.out.println(chip + ": " + millis + "ms");
    }

    private static void test(Chip2 chip, int cycles) {
        long millis = System.currentTimeMillis();
        run(chip, cycles);
        millis = System.currentTimeMillis() - millis;
        millis = System.currentTimeMillis();
        run(chip, cycles);
        millis = System.currentTimeMillis() - millis;
        System.out.println(chip + ": " + millis + "ms");
    }

    private static void run(Chip chip, int cycles) {
        while (--cycles >= 0) {
            chip.update();
        }
    }

    private static void run(Chip2 chip, int cycles) {
        chip.update(cycles);
    }

    public static void main(String[] args) {
        test(new Memory(14), 3500000);
        test(new Memory2(14), 3500000);
        test(new Memory3(14), 3500000);
        test(new Memory4(14), 3500000);
        test(new Incrementor(true), 3500000);
        test(new Incrementor2(true), 3500000);
        test(new Incrementor3(true), 3500000);
        test(new Incrementor4(true), 3500000);
        InterpretedChip interpretedChip = new InterpretedChip(new Memory(1), new Incrementor(true));
        test(interpretedChip, 3500001);
        CompiledChip compiledChip = new CompiledChip(1);
        test(compiledChip, 3500001);
    }
}
