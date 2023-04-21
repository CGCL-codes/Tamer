package ru.nsu.ccfit.pm.econ.controller.player.data;

import ru.nsu.ccfit.pm.econ.common.controller.scenario.IUScenarioProperties;

public class ScenarioProperties implements IUScenarioProperties {

    public String author;

    public String description;

    public String fullCurrencyName;

    public String name;

    public String shortCurrencyName;

    public double startingCashValue;

    public int turnLengthMinutes;

    public ScenarioProperties(IUScenarioProperties s) {
        this.author = s.getAuthor();
        this.description = s.getDescription();
        this.fullCurrencyName = s.getFullCurrencyName();
        this.name = s.getName();
        this.shortCurrencyName = s.getShortCurrencyName();
        this.startingCashValue = s.getStartingCashValue();
        this.turnLengthMinutes = s.getTurnLengthMinutes();
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getFullCurrencyName() {
        return fullCurrencyName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShortCurrencyName() {
        return shortCurrencyName;
    }

    @Override
    public double getStartingCashValue() {
        return startingCashValue;
    }

    @Override
    public int getTurnLengthMinutes() {
        return turnLengthMinutes;
    }
}
