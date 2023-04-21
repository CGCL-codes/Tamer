package com.gencom.fun.ogame.simulator.ui;

import javax.swing.SwingUtilities;
import com.gencom.fun.ogame.model.Universe;
import com.gencom.fun.ogame.simulator.SimulationEngine;

public class Main {

    private SimulationEngine engine;

    public Main() {
        engine = new SimulationEngine(500, new Universe(100, 500, 16));
        for (int i = 0; i < 20000; i++) {
            engine.createNewPlayer("Test player " + i);
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainWindow window = new MainWindow(engine);
                window.doEventLoop();
            }
        });
    }

    public static void main(String[] args) {
        new Main();
    }
}
