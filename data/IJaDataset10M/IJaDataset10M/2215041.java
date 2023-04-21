package pl.nni.msz.engine;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import pl.nni.msz.db.Agents;
import pl.nni.msz.db.Companies;
import pl.nni.msz.db.Economies;
import pl.nni.msz.db.Simulations;
import pl.nni.msz.db.interfaces.DBInterface;
import pl.nni.msz.randoms.RandomNumberGenerator;

public class EconomyProcessor {

    Collection activeAgents;

    Collection activeCompanies;

    Agents agent;

    Float pitCollected;

    Float abandonedCapital;

    Companies company;

    Integer currentPeriod;

    Economies economy;

    Integer employedLastPeriod;

    Integer agentsSurvived;

    Integer companiesSurvived;

    Iterator iterAgents;

    Iterator iterCompanies;

    RandomNumberGenerator randoms;

    Simulations simulation;

    public void finalUpdate() {
        DBInterface.getInstance().mergeEconomy(economy);
        simulation.setT(simulation.getT() + 1);
        DBInterface.getInstance().setSimulation(simulation);
        DBInterface.getInstance().updateSimulation(simulation);
        DBInterface.getInstance().updateAgents(activeAgents);
        DBInterface.getInstance().updateCompanies(activeCompanies);
    }

    public Integer getCurrentPeriod() {
        return currentPeriod;
    }

    public Economies getEconomy() {
        return economy;
    }

    public Simulations getSimulation() {
        return simulation;
    }

    public void readEconomy() {
        randoms = new RandomNumberGenerator();
        simulation = DBInterface.getInstance().getSimulation();
        currentPeriod = simulation.getT();
        economy = DBInterface.getInstance().getEconomy(currentPeriod);
        activeAgents = DBInterface.getInstance().getActiveAgents();
        activeCompanies = DBInterface.getInstance().getActiveCompanies();
        pitCollected = new Float(0);
        abandonedCapital = new Float(0);
    }

    public void setSimulation(Simulations simulation) {
        this.simulation = simulation;
    }

    public void shuffleObjects() {
        Collections.shuffle((List) activeCompanies);
        Collections.shuffle((List) activeAgents);
    }

    public void governmentActions() {
        for (iterAgents = activeAgents.iterator(); iterAgents.hasNext(); ) {
            agent = (Agents) iterAgents.next();
            if (agent.getTLifeEnd() == null) {
                agent.setCapital(agent.getCapital() + abandonedCapital / agentsSurvived);
                try {
                    agent.setCapital(agent.getCapital() + economy.getPitCollected() * economy.getPolicyController() / agentsSurvived);
                } catch (ArithmeticException divByZero) {
                }
            }
        }
        for (iterCompanies = activeCompanies.iterator(); iterCompanies.hasNext(); ) {
            company = (Companies) iterCompanies.next();
            if (company.getTClosed() == null) {
                try {
                    company.setA(company.getA() * (1 + economy.getPitCollected() * (1 - economy.getPolicyController()) / economy.getGdp()));
                } catch (ArithmeticException divByZero) {
                }
            }
        }
    }

    public void agentActions() {
        for (iterAgents = activeAgents.iterator(); iterAgents.hasNext(); ) {
            agent = (Agents) iterAgents.next();
            Collections.shuffle((List) activeCompanies);
            iterCompanies = activeCompanies.iterator();
            Companies employer = (Companies) iterCompanies.next();
            agent.setCompanies(employer);
            agent.calculateLabourSupply(employer);
            employer.calculateProduction(agent, economy);
            agent.calculatePrice(employer, economy, simulation);
            agent.calculateWage(employer, economy, simulation);
            agent.calculateInterestRate(employer, economy, simulation);
            employer.produce();
            agent.setConsumption(employer.getCurrentProduction());
            agent.payments(employer);
            employer.payments(agent);
            pitCollected += agent.taxes(economy);
            agent.calculateUtility();
        }
    }

    public void resetObjects() {
        for (iterCompanies = activeCompanies.iterator(); iterCompanies.hasNext(); ) {
            company = (Companies) iterCompanies.next();
            company.reset();
        }
    }

    public void calculateIndicators() {
        economy.setPitCollected(pitCollected);
        Float gdp = new Float(0);
        Float price = new Float(0);
        Float wage = new Float(0);
        Float labourSupply = new Float(0);
        Float interestRate = new Float(0);
        Float totalCapital = new Float(0);
        Float utility = new Float(0);
        companiesSurvived = 0;
        for (iterCompanies = activeCompanies.iterator(); iterCompanies.hasNext(); ) {
            company = (Companies) iterCompanies.next();
            company.survive(economy);
            company.expand(economy);
            if (company.getTClosed() == null) companiesSurvived += 1;
        }
        agentsSurvived = 0;
        for (iterAgents = activeAgents.iterator(); iterAgents.hasNext(); ) {
            agent = (Agents) iterAgents.next();
            gdp += agent.getConsumption();
            price += agent.getConsumption() * agent.getPrice();
            totalCapital += agent.getCapital();
            interestRate += agent.getCapital() * agent.getInterestRate();
            labourSupply += agent.getLabourSupply();
            wage += agent.getLabourSupply() * agent.getWage();
            utility += agent.getUtility();
            abandonedCapital += agent.survive(economy);
            if (agent.getTLifeEnd() == null) {
                agentsSurvived += 1;
            }
        }
        price = price / gdp;
        wage = wage / labourSupply;
        interestRate = interestRate / totalCapital;
        economy.setCapitalSupply(totalCapital);
        economy.setLabourSupply(labourSupply);
        economy.setGdp(gdp);
        economy.setUtility(utility);
        Integer numeraire = simulation.getNumeraire();
        if (numeraire.equals(1)) {
            economy.setPrice(price);
            economy.setWage(wage);
        }
        if (numeraire.equals(2)) {
            economy.setInterestRate(interestRate);
            economy.setPrice(price);
        }
        if (numeraire.equals(3)) {
            economy.setInterestRate(interestRate);
            economy.setWage(wage);
        }
    }
}
