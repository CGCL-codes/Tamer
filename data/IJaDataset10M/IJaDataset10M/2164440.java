package dukeAll;

import java.util.Arrays;

public class OlympicCandlesWhileLoop {

    public int numerOfNights(int[] candles) {
        int nights = 1;
        int numCandles = candles.length;
        label: while (true) {
            Arrays.sort(candles);
            for (int i = numCandles - 1; i > numCandles - 1 - nights; i--) {
                candles[i]--;
                if (candles[i] < 0) break label;
            }
            nights++;
        }
        return nights - 1;
    }
}
