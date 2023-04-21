package slash.resource.behaviour;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import slash.dsm.client.DsmClient;
import slash.resource.agent.MemoryAgent;
import slash.util.DataWriter;
import slash.util.PropertiesReader;

public class MemoryBehaviour extends TickerBehaviour {

    private static final long serialVersionUID = -3231027298580344751L;

    private float memory;

    private AID cmAid;

    private MemoryAgent agent;

    private DsmClient dsmClient;

    public MemoryBehaviour(AID cmAid, MemoryAgent agent) {
        super(agent, Integer.parseInt(PropertiesReader.getProperty("memory.tick")));
        this.cmAid = cmAid;
        this.agent = agent;
        this.dsmClient = new DsmClient(agent);
        this.memory = (float) ((Math.random() * 100) % 60);
    }

    private float generate() {
        if (agent.isLocalSC()) memory += 0.2 * (float) ((Math.random() * 100) % 10); else memory -= 0.2 * (float) ((Math.random() * 100) % 10);
        if (memory < 0 || memory > 100) memory = (float) ((Math.random() * 100) % 60);
        return memory;
    }

    protected void onTick() {
        generate();
        DataWriter.writeData(myAgent.getLocalName(), memory);
        Float memoryStr = memory;
        dsmClient.update(agent.getLocalName(), "memory", memoryStr);
    }
}
