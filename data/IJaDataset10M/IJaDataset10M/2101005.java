package org.jazzteam.Factorials;

public class Factorial {

    public int calculate(int number) {
        int rezult = 1;
        for (int i = 1; i <= number; i++) {
            rezult *= i;
        }
        if (number < 0) {
            return 0;
        }
        return rezult;
    }
}
