package com.yennick.fighter.bot;

import java.util.ArrayList;
import java.util.List;

public class Bot {

    private final String fighterName;

    private final List<Personality> personality;

    private final List<Behaviour> behaviour;

    public Bot(String fighterName) {
        this.fighterName = fighterName;
        this.behaviour = new ArrayList<Behaviour>();
        this.personality = new ArrayList<Personality>();
    }

    public String getFighterName() {
        return fighterName;
    }

    public void addPersonality(Personality personality) {
        personality.add(personality);
    }

    public void addBehaviour(Behaviour behaviour) {
        this.behaviour.add(behaviour);
    }

    private int getPersonality(String getP) {
        for (Personality pers : this.personality) {
            if (pers.getCharacteristic() == getP) return pers.getValue();
        }
        return 5;
    }

    private double getWeight() {
        return (this.getPersonality("punchPower") + this.getPersonality("kickPower")) / 2;
    }

    private double getHeight() {
        return (this.getPersonality("puchReach") + this.getPersonality("kickReach")) / 2;
    }

    public double getSpeed() {
        return 0.5 * (getHeight() - getWeight());
    }

    public String toString() {
        return fighterName + "\n" + this.personality.toString() + "\n" + this.behaviour.toString();
    }
}
