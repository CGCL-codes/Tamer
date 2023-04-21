package net.sf.reactionlab.combust;

import java.util.*;
import net.sf.reactionlab.*;

public class Cstr extends PfrCstrParentReactor {

    private double heatLoss;

    private double temperature;

    private boolean energyEqn;

    public String getId() {
        return "cstr";
    }

    public void addErrors(Network net, List errors) {
        if (temperature < 1000 || temperature > 3000) errors.add("Temperature must be between 1000 and 3000 K.");
        if (heatLoss < 0) errors.add("Heat loss cannot be negative.");
        super.addErrors(net, errors);
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHeatLoss() {
        return heatLoss;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setHeatLoss(double heatLoss) {
        this.heatLoss = heatLoss;
    }

    public void setEnergyEquation(boolean val) {
        energyEqn = val;
    }

    public boolean getEnergyEquation() {
        return energyEqn;
    }

    public boolean hasTemperatureMode() {
        return false;
    }
}
