package net.sf.doolin.app.sc.game.support;

import java.math.BigDecimal;
import java.util.Map;
import net.sf.doolin.app.sc.engine.ClientID;
import net.sf.doolin.app.sc.engine.support.AbstractClientResponse;
import net.sf.doolin.app.sc.game.SCClientResponse;
import net.sf.doolin.app.sc.game.response.PEconomy;
import net.sf.doolin.app.sc.game.response.PPlanet;
import net.sf.doolin.app.sc.game.type.TechId;

public class SCClientResponseImpl extends AbstractClientResponse implements SCClientResponse {

    private static final long serialVersionUID = 1L;

    private boolean historyClear;

    private int historyMaxSize;

    private Map<TechId, BigDecimal> techBudget;

    private Map<Integer, PPlanet> planetList;

    private PEconomy economy;

    public SCClientResponseImpl(ClientID clientID) {
        super(clientID);
    }

    @Override
    public PEconomy getEconomy() {
        return this.economy;
    }

    @Override
    public int getHistoryMaxSize() {
        return this.historyMaxSize;
    }

    @Override
    public Map<Integer, PPlanet> getPlanetList() {
        return this.planetList;
    }

    @Override
    public Map<TechId, BigDecimal> getTechBudget() {
        return this.techBudget;
    }

    @Override
    public boolean isHistoryClear() {
        return this.historyClear;
    }

    public void setEconomy(PEconomy economy) {
        this.economy = economy;
    }

    public void setHistoryClear(boolean historyClear) {
        this.historyClear = historyClear;
    }

    public void setHistoryMaxSize(int historyMaxSize) {
        this.historyMaxSize = historyMaxSize;
    }

    public void setPlanetList(Map<Integer, PPlanet> planetList) {
        this.planetList = planetList;
    }

    public void setTechBudget(Map<TechId, BigDecimal> techBudget) {
        this.techBudget = techBudget;
    }
}
