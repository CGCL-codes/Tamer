package net.sf.doolin.app.sc.common.oxml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.doolin.app.sc.common.model.Planet;
import net.sf.doolin.app.sc.common.model.PlanetInvestmentType;
import net.sf.doolin.app.sc.common.model.Player;
import net.sf.doolin.app.sc.common.model.PlayerView;
import net.sf.doolin.app.sc.common.model.SCGame;
import net.sf.doolin.app.sc.common.type.Position;

public class OXMLPlanet {

    private int id;

    private String name;

    private double mass;

    private double temperature;

    private long resources;

    private Position position;

    private int ownerId = -1;

    private int population = -1;

    private long income = -1;

    private final List<OXMLPlayerView> playerViewList = new ArrayList<OXMLPlayerView>();

    public Planet convert(SCGame game) {
        Planet planet = new Planet(this.id, this.name);
        planet.setMass(this.mass);
        planet.setTemperature(this.temperature);
        planet.setResources(this.resources);
        planet.setPosition(this.position);
        if (this.ownerId >= 0) {
            planet.setPopulation(this.population);
            planet.setIncome(this.income);
            Player player = game.getPlayer(this.ownerId);
            planet.setOwner(player);
        }
        for (OXMLPlayerView oPlayerView : this.playerViewList) {
            int playerId = oPlayerView.getPlayerId();
            Player player = game.getPlayer(playerId);
            PlayerView playerView = new PlayerView(planet, player, oPlayerView.isExplored());
            planet.setPlayerView(playerView);
            playerView.setInvestment(oPlayerView.getInvestment());
            playerView.setBudgetInvestment(oPlayerView.getBudgetInvestment());
            playerView.setStock(oPlayerView.getStock());
            if (playerView.isExplored()) {
                playerView.setYear(oPlayerView.getYear());
                int knownOwnerId = oPlayerView.getKnownOwnerId();
                if (knownOwnerId >= 0) {
                    playerView.setKnownOwner(game.getPlayer(knownOwnerId));
                    playerView.setKnownPopulation(oPlayerView.getKnownPopulation());
                }
            }
            Map<PlanetInvestmentType, BigDecimal> investmentMap = playerView.getPlanetInvestment().getInvestmentMap();
            investmentMap.clear();
            for (OXMLPlanetInvestment oPlanetInvestment : oPlayerView.getPlanetInvestmentList()) {
                investmentMap.put(oPlanetInvestment.getType(), oPlanetInvestment.getValue());
            }
        }
        return planet;
    }

    public int getId() {
        return this.id;
    }

    public long getIncome() {
        return this.income;
    }

    public double getMass() {
        return this.mass;
    }

    public String getName() {
        return this.name;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public List<OXMLPlayerView> getPlayerViewList() {
        return this.playerViewList;
    }

    public int getPopulation() {
        return this.population;
    }

    public Position getPosition() {
        return this.position;
    }

    public long getResources() {
        return this.resources;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIncome(long income) {
        this.income = income;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setResources(long resources) {
        this.resources = resources;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
