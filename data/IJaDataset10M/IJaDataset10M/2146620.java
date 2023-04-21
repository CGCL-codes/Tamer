package com.gencom.fun.ogame.model;

import com.gencom.fun.ogame.model.buildings.Buildings;

public class PlanetPosition {

    private SolarSystem solarSystem;

    private int index;

    private Planet planet;

    private Coordinates coords;

    private Resources damageFieldResources;

    public PlanetPosition(SolarSystem solarSystem, int index) {
        this.solarSystem = solarSystem;
        this.index = index;
        this.coords = new Coordinates(solarSystem.getGalaxy().getIndex(), solarSystem.getIndex(), index);
    }

    public Resources getDamageFieldResources() {
        return damageFieldResources;
    }

    public SolarSystem getSolarSystem() {
        return solarSystem;
    }

    public int getIndex() {
        return index;
    }

    public boolean isOccupied() {
        return planet != null;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public Coordinates getCoordinates() {
        return coords;
    }

    public Planet buildMotherPlanetForPlayer(Player player, long actualTime) {
        Planet planet = Planet.createNewMotherPlanet(player, "Planeta Matka", this, actualTime);
        setPlanet(planet);
        player.setMotherPlanetPosition(this);
        return planet;
    }

    public Planet buildExistingPlanet(Player player, String name, int index, Resources resources, Buildings buildings) {
        Planet planet = Planet.recreatePlanet(player, name, this, resources, buildings);
        setPlanet(planet);
        player.getPlanetPositions()[index] = this;
        return planet;
    }

    public String toString() {
        return coords.toString();
    }
}
