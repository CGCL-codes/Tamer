package basic;

import alice.cartago.*;

@ARTIFACT_INFO(states = { "idle", "making" }, start_state = "idle")
public class CoffeeMachine extends Artifact {

    int nCupDone;

    boolean forceDrinkRelease;

    @OPERATION
    void init() {
        defineObsProperty("selection", "");
        defineObsProperty("sugarLevel", 0.0);
        nCupDone = 0;
        forceDrinkRelease = false;
    }

    @OPERATION(states = { "idle" })
    void selectCoffee() {
        log("select coffee");
        updateObsProperty("selection", "coffee");
    }

    @OPERATION(states = { "idle" })
    void selectTea() {
        log("select tea");
        updateObsProperty("selection", "tea");
    }

    @OPERATION(states = { "idle" })
    void make() {
        String selection = getObsProperty("selection").stringValue();
        if (selection.equals("")) {
            signal("no_drink_selected");
        } else {
            log("making " + selection);
            forceDrinkRelease = false;
            switchToState("making");
            signal("making_" + selection);
            nextStep("timeToReleaseDrink");
            nextStep("forcedToReleaseDrink");
        }
    }

    @OPSTEP(tguard = 2000)
    void timeToReleaseDrink() {
        log("time to release drink");
        releaseDrink();
    }

    @OPSTEP(guard = "makingStopped")
    void forcedToReleaseDrink() {
        log("forced to release drink");
        releaseDrink();
    }

    private void releaseDrink() {
        String selection = getObsProperty("selection").stringValue();
        double sugarLevel = getObsProperty("sugarLevel").doubleValue();
        String drink = selection + "(" + (++nCupDone) + "," + sugarLevel + ")";
        log("release " + drink);
        signal(selection + "_ready", drink);
        updateObsProperty("sugarLevel", 0.0);
        updateObsProperty("selection", "");
        switchToState("idle");
    }

    @GUARD
    boolean makingStopped() {
        return forceDrinkRelease;
    }

    @OPERATION(states = { "making" })
    void addSugar() {
        double sugarLevel = getObsProperty("sugarLevel").doubleValue();
        sugarLevel += 0.1;
        if (sugarLevel > 1) {
            sugarLevel = 1;
        }
        updateObsProperty("sugarLevel", sugarLevel);
    }

    @OPERATION(states = { "making" })
    void stop() {
        log("stopped");
        forceDrinkRelease = true;
    }
}
