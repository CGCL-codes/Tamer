package com.coderdream.chapter17.observer.a1;

public class IncrementalNumberGenerator extends NumberGenerator {

    private int number;

    private int begin;

    private int end;

    private int step;

    public IncrementalNumberGenerator(int begin, int end, int step) {
        this.begin = begin;
        this.end = end;
        this.step = step;
    }

    @Override
    public void execute() {
        for (int i = begin; i < end; i += step) {
            number = i;
            notifyObservers();
        }
    }

    @Override
    public int getNumber() {
        return number;
    }
}
