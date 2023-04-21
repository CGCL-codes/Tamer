package com.coderdream.chapter17.observer.a1;

import java.util.Random;

public class RandomNumberGenerator extends NumberGenerator {

    private Random random = new Random();

    private int number;

    @Override
    public void execute() {
        for (int i = 0; i < 10; i++) {
            number = random.nextInt(50);
            notifyObservers();
        }
    }

    @Override
    public int getNumber() {
        return number;
    }
}
