package slash.slachecker.behaviour;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import java.util.Enumeration;
import slash.dsm.client.DsmClient;
import slash.entity.Context;
import slash.entity.Notify;
import slash.entity.SLAContract;
import slash.slachecker.agent.SLACheckerAgent;
import slash.util.DataWriter;
import slash.util.PropertiesReader;

public class SLACheckerBehaviour extends TickerBehaviour {

    private static final long serialVersionUID = -500981357584073158L;

    private SLACheckerAgent sc;

    private DsmClient dsmClient;

    private int check;

    public SLACheckerBehaviour(SLACheckerAgent sc) {
        super(sc, Integer.parseInt(PropertiesReader.getProperty("slachecker.tick")));
        this.check = Integer.parseInt(PropertiesReader.getProperty("slachecker.check"));
        this.sc = sc;
        this.dsmClient = new DsmClient(sc);
    }

    protected void onTick() {
        System.out.println("Sono su " + myAgent.here().getName());
        for (int i = 0; i < sc.getContractList().size(); i++) {
            SLAContract contract = sc.getContractList().get(i);
            Context status = sc.getContextTable().get(contract.getPublisher());
            Context statusSub = sc.getContextTable().get(contract.getSubscriber());
            float lat = 0, rel = 0, req = 0;
            if ((status != null) && (((lat = status.getAvgLatency()) > contract.getLatency()) || ((rel = status.getAvgReliability()) > contract.getReliability()) || ((req = statusSub.getAvgReqInterval()) > contract.getReqInterval()))) {
                System.out.println("Violated with latency " + lat + " > " + contract.getLatency() + ", reliability " + rel + " > " + contract.getReliability() + ", reqInterval " + req + " > " + contract.getReqInterval());
                dsmClient.out(contract.getPublisher().getLocalName(), "slacontract-violation", contract);
                dsmClient.out(contract.getSubscriber().getLocalName(), "slacontract-violation", contract);
            }
        }
        AID best = getBestNode();
        AID cm = new AID("cm" + sc.getAssociatedID(), AID.ISLOCALNAME);
        System.out.println("Associated with: " + cm.getLocalName());
        Context context = sc.getContextTable().get(cm);
        float avgCpu, avgRam, avgMemory, avgEnergy;
        if (context != null) {
            avgCpu = context.getAvgCpu();
            avgRam = context.getAvgRam();
            avgMemory = context.getAvgMemory();
            avgEnergy = context.getAvgEnergy();
            System.out.println("Actual context--> cpu: " + avgCpu + ", ram: " + avgRam + ", memory: " + avgMemory + ", energy: " + avgEnergy + ", index: " + context.calcIndex());
            if (context.calcIndex() > check) {
                if (best != null) {
                    System.out.println("queue: " + myAgent.getCurQueueSize());
                    myAgent.doMove(sc.getContextTable().get(best).getLocation());
                    int bestN = Integer.parseInt(String.valueOf(best.getLocalName().charAt(best.getLocalName().length() - 1)));
                    System.out.println("Migration to " + best.getLocalName());
                    Notify notify = new Notify(sc.getAssociatedID(), bestN);
                    dsmClient.update("notify", "notify", notify);
                    sc.setAssociatedID(bestN);
                } else System.out.println("IMPOSSIBILE MIGRARE!!!");
            }
        }
    }

    public AID getBestNode() {
        AID best = null;
        AID next;
        Enumeration<AID> keys = sc.getContextTable().keys();
        Context context;
        float avgCpu, avgRam, avgMemory, avgEnergy;
        float total = 500, iter = 0;
        while (keys.hasMoreElements()) {
            next = keys.nextElement();
            context = sc.getContextTable().get(next);
            if (context != null) {
                avgCpu = context.getAvgCpu();
                avgRam = context.getAvgRam();
                avgMemory = context.getAvgMemory();
                avgEnergy = context.getAvgEnergy();
                iter = avgCpu + avgRam + avgMemory + (1 - avgEnergy);
                DataWriter.writeData("index" + next.getLocalName().charAt(2), context.calcIndex());
                System.out.println("Context[" + next.getLocalName() + "]--> cpu: " + avgCpu + ", ram: " + avgRam + ", memory: " + avgMemory + ", energy: " + avgEnergy + ", index: " + context.calcIndex());
                if (avgCpu < Context.CPU_LIMIT && avgRam < Context.RAM_LIMIT && avgMemory < Context.MEMORY_LIMIT && avgEnergy > Context.ENERGY_LIMIT && iter < total) {
                    best = next;
                    total = iter;
                }
            }
        }
        return best;
    }
}
