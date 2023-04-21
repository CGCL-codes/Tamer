package com.gencom.fun.ogame.model;

public class SolarSystem {

    private PlanetPosition[] positions;

    private Galaxy galaxy;

    private int index;

    public SolarSystem(Universe universe, Galaxy galaxy, int index) {
        this.index = index;
        this.galaxy = galaxy;
        positions = new PlanetPosition[universe.getPositions()];
        for (int i = 0; i < universe.getPositions(); i++) {
            positions[i] = new PlanetPosition(this, i);
        }
    }

    public int getIndex() {
        return index;
    }

    public PlanetPosition getPlanetPosition(int i) {
        return positions[i];
    }

    public Galaxy getGalaxy() {
        return galaxy;
    }
}
