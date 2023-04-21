package data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import data.constants.GalaxyConstants;
import data.constants.RaceState;
import data.controls.Game;
import util.Utils;

public class Race extends AbstractData implements Comparable<Race> {

    private TechBlock tech;

    private double population;

    private double industry;

    private double votes;

    private String name;

    private int nPlanets;

    private Game game;

    private RaceState state;

    private Team team;

    private String note;

    @Override
    public void copy(AbstractData data) {
        if (data == null) return;
        super.copy(data);
        Race r = (Race) data;
        tech = r.tech;
        population = r.population;
        industry = r.industry;
        votes = r.votes;
        name = r.name;
        nPlanets = r.nPlanets;
        game = r.game;
        state = r.state;
        team = r.team;
    }

    public Race() {
    }

    public Race(String[] l, Game game, boolean isFull) {
        this.game = game;
        name = l[0];
        tech = new TechBlock(Utils.d(l[1]), Utils.d(l[2]), Utils.d(l[3]), Utils.d(l[4]));
        population = Utils.d(l[5]);
        industry = Utils.d(l[6]);
        nPlanets = Utils.i(l[7]);
        if (isFull) {
            state = RaceState.UNDEFINED;
        } else state = RaceState.get(l[8]);
        votes = Utils.d(l[9]);
        int i = 10;
        if (!game.getTeams().isEmpty()) {
            team = game.getTeam(l[i++]);
            team.addRace(this);
        }
        if (i < l.length) {
            i++;
        }
        if (i < l.length && "WINNER".equals(l[i])) {
        }
    }

    public Game getGame() {
        return game;
    }

    public Team getTeam() {
        return team;
    }

    public TechBlock getTech() {
        return tech;
    }

    public void setTech(TechBlock tech) {
        this.tech = tech;
    }

    public RaceState getRelations() {
        return state;
    }

    public void setRelations(RaceState state) {
        this.state = state;
    }

    public boolean isRip() {
        return name.endsWith("_RIP");
    }

    public boolean isYour() {
        return this == game.getYou();
    }

    public double getPopulation() {
        if (!isYour()) return population;
        double s = 0;
        for (Planet p : game.getPlanets()) if (p.getOwner() == this) s += p.getPopulation();
        return s;
    }

    public double getIndustry() {
        if (!isYour()) return industry;
        double s = 0;
        for (Planet p : game.getPlanets()) if (p.getOwner() == this) s += p.getIndustry();
        return s;
    }

    public double getProduction() {
        if (!isYour()) return industry + (population - industry) / GalaxyConstants.PROD_PER_POP;
        double s = 0;
        for (Planet p : game.getPlanets()) if (p.getOwner() == this) s += p.getProduction();
        return s;
    }

    public double getVotes() {
        if (game.votes == null || game.votes.isEmpty()) return 0;
        double v = 0;
        if (!isYour()) v = votes;
        Vote vote = game.votes.get(0);
        if (name.equals(vote.raceName)) v += vote.votes;
        return v;
    }

    public int getPlanetsNumber() {
        if (!isYour()) return nPlanets;
        int n = 0;
        for (Planet p : game.getPlanets()) if (p.isYour()) n++;
        return n;
    }

    public double getLevel() {
        return game.getRaceLevel(this);
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        if (note == null) note = game.getConfig().subnode("RaceNote").getString(name, "");
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        game.getConfig().subnode("RaceNote").putString(name, note);
    }

    public Color getColor() {
        return game.getColor(this, null);
    }

    public List<Planet> getPlanets() {
        List<Planet> planets = new ArrayList<Planet>();
        for (Planet p : game.getPlanets()) if (p.getOwner() == this) planets.add(p);
        return planets;
    }

    public List<Group> getGroups() {
        List<Group> groups = new ArrayList<Group>();
        for (Group g : game.getGroups()) if (g.getOwner() == this) groups.add(g);
        return groups;
    }

    public List<Fleet> getFleets() {
        List<Fleet> fleets = new ArrayList<Fleet>();
        for (Fleet f : game.getFleets()) if (f.getOwner() == this) fleets.add(f);
        return fleets;
    }

    public List<ShipType> getShipTypes() {
        List<ShipType> shipTypes = new ArrayList<ShipType>();
        for (ShipType t : game.getShipTypes()) if (t.getOwner() == this) shipTypes.add(t);
        return shipTypes;
    }

    public List<Science> getSciences() {
        List<Science> sciences = new ArrayList<Science>();
        for (Science s : game.getSciences()) if (s.getOwner() == this) sciences.add(s);
        return sciences;
    }

    public Fleet getFleet(String name) {
        return game.getFleet(name, this);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(Race o) {
        return o == null ? -1 : getName().compareTo(o.getName());
    }
}
