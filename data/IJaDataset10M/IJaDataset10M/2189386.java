package objects.bombing;

import objects.Galaxy;
import objects.Planet;
import objects.Race;
import objects.ShipGroup;
import objects.production.IProduction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PlanetBombings {

    private final Planet planet;

    private final Race owner;

    private final IProduction production;

    private final List<Race> witnesses = new ArrayList<Race>();

    private final List<Bombing> bombings = new ArrayList<Bombing>();

    public int phase = 0;

    public PlanetBombings(Planet planet) {
        this.planet = planet;
        owner = planet.getOwner();
        production = planet.getProduction();
        if (owner != null) witnesses.add(owner);
    }

    public PlanetBombings(Planet planet, Race owner, IProduction production) {
        this.planet = planet;
        this.owner = owner;
        this.production = production;
    }

    public final void addBombing(ShipGroup group) {
        Race who = group.getOwner();
        Bombing bombing;
        int i = bombings.size();
        while (true) {
            if (--i < 0) {
                bombing = new Bombing(who);
                bombings.add(bombing);
                witnesses.add(who);
                break;
            }
            bombing = bombings.get(i);
            if (bombing.who() == who) break;
        }
        bombing.addBombing(group);
    }

    public final void addBombing(Bombing bombing) {
        bombings.add(bombing);
    }

    public final List<Race> getWitnesses() {
        return Collections.unmodifiableList(witnesses);
    }

    public final void addWitness(Race race) {
        if (!witnesses.contains(race)) witnesses.add(race);
    }

    public final void doIt(IBombingGenerator bombingGenerator) {
        Collections.sort(bombings);
        for (int i = 0; i < bombings.size(); ++i) {
            Bombing bombing = bombings.get(i);
            if (bombing.bomb(bombingGenerator, planet) != Bombing.Result.Damaged) {
                bombings.subList(i + 1, bombings.size()).clear();
                return;
            }
        }
    }

    public void collectWitnesses(Galaxy galaxy) {
        for (Race race : galaxy.getRaces()) if (!isWitness(race)) for (ShipGroup group : race.getShipGroups()) if (group.atPlanet(getPlanet())) {
            addWitness(race);
            break;
        }
    }

    public final Planet getPlanet() {
        return planet;
    }

    public final Race getOwner() {
        return owner;
    }

    public final List<Bombing> getBombings() {
        return Collections.unmodifiableList(bombings);
    }

    public final boolean isWitness(Race race) {
        for (Race witness : witnesses) if (witness.trusts(race)) return true;
        return false;
    }

    public final IProduction getProduction() {
        return production;
    }
}
