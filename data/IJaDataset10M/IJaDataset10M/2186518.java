package org.opensourcephysics.stp.sensitive;

import java.text.NumberFormat;
import org.opensourcephysics.controls.*;
import org.opensourcephysics.frames.DisplayFrame;

public class LJgasApp extends AbstractSimulation {

    LJgas gas;

    DisplayFrame displayFrame = new DisplayFrame("");

    NumberFormat numberformat = NumberFormat.getInstance();

    public LJgasApp() {
        gas = new LJgas();
        displayFrame.addDrawable(gas);
        displayFrame.setPreferredMinMax(-0.2 * gas.cellLength, 1.2 * gas.cellLength, -0.2 * gas.cellLength, 1.2 * gas.cellLength);
        numberformat.setMaximumIntegerDigits(5);
        numberformat.setMinimumIntegerDigits(1);
        numberformat.setMinimumFractionDigits(1);
    }

    public void initialize() {
        gas.cellLength = control.getDouble("cell length");
        gas.initialize();
        control.println("time = " + numberformat.format(0));
    }

    public void doStep() {
        for (int i = 0; i < 20; i++) {
            gas.step();
        }
        control.println("time = " + numberformat.format(gas.t));
        displayFrame.render();
    }

    public void reset() {
        gas.initialize();
        control.setValue("cell length", gas.cellLength);
        control.setValue("perturbation strength", 1.00001);
        displayFrame.setPreferredMinMax(-0.2 * gas.cellLength, 1.2 * gas.cellLength, -0.2 * gas.cellLength, 1.2 * gas.cellLength);
        displayFrame.render();
    }

    public void perturb() {
        double a = control.getDouble("perturbation strength");
        gas.perturb(a);
        gas.zeroAverages();
    }

    public void reverse() {
        gas.reverse();
        gas.zeroAverages();
    }

    public static void main(String[] args) {
        SimulationControl control = SimulationControl.createApp(new LJgasApp(), args);
        control.addButton("perturb", "Perturb");
    }
}
