package edu.ucla.stat.SOCR.distributions;

import edu.ucla.stat.SOCR.core.*;

public class PokerDiceDistribution extends Distribution {

    static final int c = 7776;

    public PokerDiceDistribution() {
        super.setParameters(0, 6, 1, DISCRETE);
        name = name();
    }

    public double getDensity(double x) {
        double d = 0;
        int i = (int) (x + 0.5);
        switch(i) {
            case 0:
                d = 720.0 / c;
                break;
            case 1:
                d = 3600.0 / c;
                break;
            case 2:
                d = 1800.0 / c;
                break;
            case 3:
                d = 1200.0 / c;
                break;
            case 4:
                d = 300.0 / c;
                break;
            case 5:
                d = 150.0 / c;
                break;
            case 6:
                d = 6.0 / c;
                break;
        }
        return d;
    }

    public String name() {
        return "Poker Dice Distribution";
    }
}
