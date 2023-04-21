package net.aviv.javacowboys;

import java.util.*;
import java.io.*;


/**
   Shooter
   
   - only allows the Control to call setters, or takeTurn.
 **/

public class Shooter extends Observable implements Serializable {
    // these are not persistant.
    transient Control     control;
    transient ShooterUI   ui;
    transient Strategy    strategy;

    // these are sort of persistant.
    String      strategyName;
    String      name;

    int         health;
    int         accuracy;
    int         money;

    /*
      Note:  The stats are not being tracked correctly right now.
      I'm leaving this for later [aviv, 1/22/00]
    int         kills; // tracked by Control for now
    int         moneyEarned;  // tracked by Control for now
    */
    int         roundsPlayed; // tracked by PopulationTrackerImpl for now

    public void clearTransientData() {
        deleteObservers();
        if(ui != null) {
            ui.clearTransientData();
        }
        if(strategy != null) {
            strategy.clearTransientData();
        }

        control = null;
        ui = null;
        strategy = null;
    }

    // 
    public Shooter(Strategy strategy, String name) {
        this.strategy = strategy;
        this.strategyName = strategy.name();
        this.name = name;
    }

    public void setLocation(int location) {
        ui = new ShooterUI(location, this);
        ui.init(control);

        Assert.assert(strategy.shooter() == this, 
                      "strategy.shooter() == this");
    }

    public void setControl(Control control) {
        this.control = control;
        strategy = ShooterCreator.strategyFromName(strategyName);
        strategy.init(control, this);

        Assert.assert(strategy.shooter() == this, 
                      "strategy.shooter() == this");
    }

    public void takeTurn(Turn turn) {
        if(turn.shooter() != this) {
            System.out.println("problem here lkj lm.,cxoije");
        }
        Assert.assert(strategy.shooter() == this, 
                      "strategy.shooter() == this");
        strategy.takeTurn(turn);
    }

    public Strategy strategy() {
        return strategy;
    }
    public UI ui() {
        return ui;
    }
    public String name() {
        return name;
    }
}
