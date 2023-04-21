package org.jmidpoint;

import java.awt.GridLayout;
import javax.swing.*;

public class Start {

    public static void main(String[] args) {
        Midpoint mp1 = new Midpoint();
        Midpoint mp2 = new Midpoint(7, 1.0, 0.5, true, -252);
        double[] prob = { 0.1, 0.5, 0.2, 0.01, 0.15, 0.04 };
        mp1.getDiscreteIntField();
        mp1.getDiscreteDoubleField();
        mp1.getIntField(prob);
    }
}
