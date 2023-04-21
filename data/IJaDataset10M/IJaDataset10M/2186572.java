package net.sourceforge.jabm.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.agent.AgentList;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationEvent;
import net.sourceforge.jabm.strategy.Strategy;
import net.sourceforge.jabm.util.MutableStringWrapper;

public class FittestStrategyReportVariables implements ReportVariables {

    protected MutableStringWrapper fileNamePrefix;

    protected String fileNameSuffix;

    protected ObjectOutputStream out;

    protected Agent fittestAgent;

    protected int fileCounter;

    public FittestStrategyReportVariables(MutableStringWrapper fileNamePrefix, String fileNameSuffix) {
        super();
        this.fileNamePrefix = fileNamePrefix;
        this.fileNameSuffix = fileNameSuffix;
    }

    @Override
    public void compute(SimEvent event) {
        if (event instanceof SimulationEvent) {
            AgentList agents = ((SimulationEvent) event).getSimulation().getSimulationController().getPopulation().getAgentList();
            AgentList sortedAgents = new AgentList(agents.getAgents());
            sortedAgents.sortAgentsByFitness();
            this.fittestAgent = sortedAgents.get(sortedAgents.size() - 1);
            Strategy fittestStrategy = fittestAgent.getStrategy();
            try {
                out.writeObject(fittestStrategy);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Map<Object, Number> getVariableBindings() {
        HashMap<Object, Number> result = new HashMap<Object, Number>();
        result.put("fittestagent.fitness", this.fittestAgent.getPayoff());
        return result;
    }

    @Override
    public void initialise(SimEvent event) {
        fileCounter++;
        createOutputFile();
    }

    public void createOutputFile() {
        try {
            out = new ObjectOutputStream(new FileOutputStream(fileNamePrefix + fileNameSuffix + "-" + fileCounter + ".bin"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MutableStringWrapper getFileNamePrefix() {
        return fileNamePrefix;
    }

    public void setFileNamePrefix(MutableStringWrapper fileNamePrefix) {
        this.fileNamePrefix = fileNamePrefix;
    }

    public String getFileNameSuffix() {
        return fileNameSuffix;
    }

    public void setFileNameSuffix(String fileNameSuffix) {
        this.fileNameSuffix = fileNameSuffix;
    }

    @Override
    public void eventOccurred(SimEvent event) {
    }

    @Override
    public void dispose(SimEvent event) {
    }

    public String getName() {
        return this.getClass().getName();
    }
}
