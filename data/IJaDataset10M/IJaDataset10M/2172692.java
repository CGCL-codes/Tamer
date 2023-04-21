package net.sf.gap.mc.qagesa.grid;

import eduni.simjava.Sim_system;
import gridsim.net.FIFOScheduler;
import gridsim.net.Link;
import gridsim.net.RIPRouter;
import net.sf.gap.distributions.Uniform_int;
import net.sf.gap.factories.LinkFactory;
import net.sf.gap.xml.types.ScenarioType;
import net.sf.gap.grid.AbstractVirtualOrganization;
import net.sf.gap.grid.components.AbstractGridElement;
import net.sf.gap.agents.middleware.AgentMiddleware;
import net.sf.gap.mc.QAGESA;
import net.sf.gap.mc.qagesa.grid.components.QAGESAGridElement;
import net.sf.gap.mc.qagesa.agents.TranscodingAgent;
import net.sf.gap.mc.qagesa.agents.middleware.QAGESAPlatform;
import net.sf.gap.mc.qagesa.multimedia.TranscodingSet;
import net.sf.gap.mc.qagesa.users.impl.Submitter;
import net.sf.gap.mc.qagesa.users.impl.User;
import eduni.simjava.Sim_system;
import gridsim.Machine;
import gridsim.MachineList;
import gridsim.PE;
import gridsim.PEList;
import gridsim.ResourceCalendar;
import gridsim.ResourceCharacteristics;
import gridsim.datagrid.SimpleReplicaManager;
import gridsim.datagrid.index.TopRegionalRC;
import gridsim.datagrid.storage.HarddriveStorage;
import gridsim.datagrid.storage.Storage;
import gridsim.datagrid.storage.TapeStorage;
import gridsim.net.Link;
import gridsim.net.RIPRouter;
import gridsim.net.SimpleLink;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.LinkedList;
import net.sf.gap.xml.XMLReader;
import net.sf.gap.xml.impl.XMLNetworkTopology;
import net.sf.gap.xml.types.GridElementType;
import net.sf.gap.xml.types.HardDiskType;
import net.sf.gap.xml.types.LinkType;
import net.sf.gap.xml.types.MachineListType;
import net.sf.gap.xml.types.MachineType;
import net.sf.gap.xml.types.PEType;
import net.sf.gap.xml.types.ScenarioType;
import net.sf.gap.xml.types.TapeType;
import net.sf.gap.mc.qagesa.constants.QAGESAMeasures;

/**
 *
 * @author Giovanni Novelli
 */
public class QAGESAXMLVirtualOrganization extends AbstractVirtualOrganization {

    /**
     * links delay factor
     */
    private double factor;

    private boolean traceFlag;

    private int numUsers;

    private boolean cachingEnabled;

    private int whichMeasure;

    private TranscodingSet transcodingSet;

    private int maxRequests;

    public QAGESAXMLVirtualOrganization(String xml, String xsd, double factor, int numUsers, boolean cachingEnabled, int whichMeasure, int maxRequests) throws Exception {
        XMLReader reader = new XMLReader(xsd, xml);
        ScenarioType scenario = reader.getScenario();
        this.setScenario(scenario);
        int numGEs = scenario.getGrid().getGridElements().size();
        int countSEs = 0;
        int countCEs = 0;
        for (int i = 0; i < numGEs; i++) {
            GridElementType ge = scenario.getGrid().getGridElements().get(i);
            if (ge.isSE()) {
                countSEs++;
            } else {
                countCEs++;
            }
        }
        this.setTraceFlag(scenario.isTrace());
        this.setNumCEs(countCEs);
        this.setNumSEs(countSEs);
        this.setFactor(factor);
        this.initParameters(traceFlag, factor, numUsers, cachingEnabled, whichMeasure, maxRequests);
        this.setMIPS(scenario.getMIPS());
        this.createEntities();
    }

    @Override
    public void initialize() {
        this.getTopology().initialize();
        this.mapCEs = new HashMap<Integer, RIPRouter>(this.getNumCEs());
        this.mapSEs = new HashMap<Integer, RIPRouter>(this.getNumSEs());
        this.setCEs(new Vector<AbstractGridElement>(this.getNumCEs()));
        this.setSEs(new Vector<AbstractGridElement>(this.getNumSEs()));
        this.setAMs(new Vector<AgentMiddleware>(this.getNumAMs()));
        this.initializeCEs();
        this.initializeSEs();
        this.initializeAgents();
    }

    protected void initializeCEs() {
        for (int i = 0; i < this.getNumCEs() + this.getNumSEs(); i++) {
            if (!getScenario().getGrid().getGridElements().get(i).isSE()) {
                QAGESAGridElement computingElement = (QAGESAGridElement) Sim_system.get_entity(getScenario().getGrid().getGridElements().get(i).getName());
                this.mapCEs.put(computingElement.get_id(), computingElement.getExternalRouter());
                this.getCEs().add(computingElement);
            }
        }
    }

    protected void initializeSEs() {
        for (int i = 0; i < this.getNumCEs() + this.getNumSEs(); i++) {
            if (getScenario().getGrid().getGridElements().get(i).isSE()) {
                QAGESAGridElement storageElement = (QAGESAGridElement) Sim_system.get_entity(getScenario().getGrid().getGridElements().get(i).getName());
                this.mapSEs.put(storageElement.get_id(), storageElement.getExternalRouter());
                this.getSEs().add(storageElement);
            }
        }
    }

    @Override
    protected void createEntities() throws Exception {
        this.setDataGIS(this.createDataGIS());
        this.setTopRegionalRC(this.createTopRegionalRC());
        this.setTopology(new XMLNetworkTopology(this.getScenario(), this.isTraceFlag()));
        FIFOScheduler rcSched = new FIFOScheduler("trrc_sched");
        RIPRouter router = this.getTopology().get(0);
        router.attachHost(this.getTopRegionalRC(), rcSched);
        this.createAndAttachCEs();
        this.createAndAttachSEs();
        this.createAndAttachAgentPlatform();
        this.createAndAttachAgents();
        this.createAndAttachUsers();
    }

    protected void initializeAgents() {
        int totalAgents = 0;
        for (int i = 0; i < this.getNumCEs() + this.getNumSEs(); i++) {
            if (!this.getScenario().getGrid().getGridElements().get(i).isSE()) {
                String cename = this.getScenario().getGrid().getGridElements().get(i).getName();
                QAGESAGridElement computingElement = (QAGESAGridElement) Sim_system.get_entity(cename);
                int numAgents = computingElement.getNumPE();
                for (int j = 0; j < numAgents; j++) {
                    TranscodingAgent agent = (TranscodingAgent) Sim_system.get_entity("AGENT_" + totalAgents);
                    this.getPlatform().addAgent(agent, computingElement);
                    totalAgents++;
                }
            }
        }
    }

    private void initParameters(boolean traceFlag, double factor, int numUsers, boolean cachingEnabled, int whichMeasure, int maxRequests) {
        this.setTraceFlag(traceFlag);
        this.setNumUsers(numUsers);
        this.setFactor(factor);
        this.setCachingEnabled(cachingEnabled);
        this.setWhichMeasure(whichMeasure);
        this.setMaxRequests(maxRequests);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void createAndAttachCEs() throws Exception {
        for (int i = 0; i < this.getNumCEs() + this.getNumSEs(); i++) {
            GridElementType geItem = getScenario().getGrid().getGridElements().get(i);
            if (geItem.isRB()) {
                this.setRBname(geItem.getName());
            }
            String linkName = geItem.getLink();
            if (getScenario().getTopology().getMapLinks().containsKey(linkName)) {
                LinkType linkItem = getScenario().getTopology().getMapLinks().get(linkName);
                if (linkItem.isBidirectional()) {
                    Link link = new SimpleLink(linkItem.getName(), linkItem.getBaudrate(), linkItem.getDelay(), linkItem.getMTU());
                    String arch = "System Architecture";
                    String os = "Operating System";
                    double time_zone = 12.0;
                    double cost = 1.0;
                    MachineListType mListType = geItem.getMachineList();
                    MachineList mList = new MachineList();
                    int np = 0;
                    int m = mListType.getItems().size();
                    for (int j = 0; j < m; j++) {
                        MachineType mType = mListType.getItems().get(j);
                        PEList peList = new PEList();
                        for (int k = 0; k < mType.getPeList().getItems().size(); k++) {
                            PEType peType = mType.getPeList().getItems().get(k);
                            for (int kkk = 0; kkk < QAGESA.peExpansion; kkk++) {
                                int kk = (k * QAGESA.peExpansion) + kkk;
                                peList.add(new PE(kk, peType.getMIPS()));
                                np++;
                            }
                        }
                        mList.add(new Machine(j, peList));
                    }
                    ResourceCharacteristics resConfig = new ResourceCharacteristics(arch, os, mList, ResourceCharacteristics.TIME_SHARED, time_zone, cost);
                    String geName = geItem.getName();
                    long seed = 11L * 13 * 17 * 19 * 23 + 1;
                    double peakLoad = QAGESA.gridload;
                    double offPeakLoad = QAGESA.gridload;
                    double holidayLoad = QAGESA.gridload;
                    LinkedList<Integer> Weekends = new LinkedList<Integer>();
                    Weekends.add(new Integer(Calendar.SATURDAY));
                    Weekends.add(new Integer(Calendar.SUNDAY));
                    LinkedList Holidays = new LinkedList();
                    QAGESAGridElement ge = null;
                    try {
                        ResourceCalendar cal = new ResourceCalendar(time_zone, peakLoad, offPeakLoad, holidayLoad, Weekends, Holidays, seed);
                        SimpleReplicaManager rm = new SimpleReplicaManager("RM_" + geName, geName);
                        ge = new QAGESAGridElement(geName, link, resConfig, cal, rm);
                        if (geItem.isSE()) {
                            ge.setSE(true);
                            for (int ihd = 0; ihd < geItem.getStorage().getHardDiskList().getItems().size(); ihd++) {
                                HardDiskType hdType = geItem.getStorage().getHardDiskList().getItems().get(ihd);
                                Storage storage = new HarddriveStorage(hdType.getName(), hdType.getCapacity() * 1000000000);
                                ge.addStorage(storage);
                            }
                            for (int it = 0; it < geItem.getStorage().getTapeList().getItems().size(); it++) {
                                TapeType tapeType = geItem.getStorage().getTapeList().getItems().get(it);
                                Storage storage = new TapeStorage(tapeType.getName(), tapeType.getCapacity() * 1000000000);
                                ge.addStorage(storage);
                            }
                        } else {
                            ge.setSE(false);
                        }
                        ge.createLocalRC();
                        ge.setHigherReplicaCatalogue(TopRegionalRC.DEFAULT_NAME);
                        ge.setNumPE(np);
                        ge.setNumWN(m);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    RIPRouter router = (RIPRouter) Sim_system.get_entity(linkItem.getToEntity());
                    ge.attachRouter(router);
                } else {
                    throw new Exception("Missing link to network for GE " + geItem.getName());
                }
            }
        }
    }

    public void createAndAttachSEs() throws Exception {
    }

    public void createAndAttachAgentPlatform() throws Exception {
        @SuppressWarnings("unused") Uniform_int r = new Uniform_int("createAndAttachAgentPlatform");
        this.setPlatform(new QAGESAPlatform(false));
        QAGESAPlatform agent = (QAGESAPlatform) this.getPlatform();
        for (int i = 0; i < this.getNumCEs() + this.getNumSEs(); i++) {
            if (this.getScenario().getGrid().getGridElements().get(i).isRB()) {
                String cename = this.getScenario().getGrid().getGridElements().get(i).getName();
                QAGESAGridElement ce = (QAGESAGridElement) Sim_system.get_entity(cename);
                agent.setGridElement(ce);
                agent.setVirtualOrganization(this);
                ce.attachPlatform(agent);
                break;
            }
        }
        for (int i = 0; i < this.getNumCEs() + this.getNumSEs(); i++) {
            if (!this.getScenario().getGrid().getGridElements().get(i).isSE()) {
                String cename = this.getScenario().getGrid().getGridElements().get(i).getName();
                QAGESAGridElement ce = (QAGESAGridElement) Sim_system.get_entity(cename);
                ce.setAgentPlatform(agent);
            }
        }
        for (int i = 0; i < this.getNumCEs() + this.getNumSEs(); i++) {
            if (this.getScenario().getGrid().getGridElements().get(i).isSE()) {
                String sename = this.getScenario().getGrid().getGridElements().get(i).getName();
                QAGESAGridElement se = (QAGESAGridElement) Sim_system.get_entity(sename);
                se.setAgentPlatform(agent);
            }
        }
        agent.createServices();
    }

    public void createAndAttachAgents() throws Exception {
        int totalAgents = 0;
        for (int i = 0; i < this.getNumCEs() + this.getNumSEs(); i++) {
            if (!this.getScenario().getGrid().getGridElements().get(i).isSE()) {
                String cename = this.getScenario().getGrid().getGridElements().get(i).getName();
                QAGESAGridElement ce = (QAGESAGridElement) Sim_system.get_entity(cename);
                int numAgents = ce.getNumPE();
                for (int j = 0; j < numAgents; j++) {
                    TranscodingAgent agent = new TranscodingAgent(ce, "AGENT_" + totalAgents, 0, false, this.isCachingEnabled());
                    totalAgents++;
                    ce.attachAgent(agent);
                }
            }
        }
        this.getPlatform().setTotalAgents(totalAgents);
    }

    public void createAndAttachSubmitters() throws Exception {
        @SuppressWarnings("unused") Uniform_int r = new Uniform_int("createAndAttachSubmitters");
        int N = this.getTopology().getNumRouters();
        int index;
        RIPRouter router = null;
        Link link = null;
        for (int i = 0; i < this.getNumUsers(); i++) {
            index = (i + QAGESA.fromRouter) % N;
            router = this.getTopology().get(index);
            link = LinkFactory.UserLink(640000, 20);
            Submitter submitter = new Submitter("SUBMITTER_" + i, link, false);
            router.attachHost(submitter, submitter.getUserSched());
            submitter.setVirtualOrganization(this);
        }
    }

    public void createAndAttachUsers() throws Exception {
        double baudrate = 20971520;
        double delay = 1.0;
        this.setTranscodingSet(new TranscodingSet("measures/videos.csv", "measures/chunks.csv"));
        @SuppressWarnings("unused") Uniform_int r = new Uniform_int("createAndAttachUsers");
        int N = this.getTopology().getNumRouters();
        int index;
        RIPRouter router = null;
        Link link = null;
        String movieTag = "aa06f7ddedc7460bd439298494c1e968";
        int numRequests = this.getMaxRequests();
        boolean repeated = QAGESA.repeated;
        for (int i = 0; i < this.getNumUsers(); i++) {
            index = (i + QAGESA.fromRouter) % 4;
            router = this.getTopology().get(index);
            switch(this.getWhichMeasure()) {
                case QAGESAMeasures.RMR:
                    link = LinkFactory.UserLink(baudrate, delay);
                    User rmrUser = new User("RMRUSER_" + i, link, false, QAGESA.randomSelection, numRequests, repeated, movieTag, User.MEASURE_RESPONSE);
                    router.attachHost(rmrUser, rmrUser.getUserSched());
                    rmrUser.setVirtualOrganization(this);
                    rmrUser.setUid(i);
                    break;
                case QAGESAMeasures.MR:
                    link = LinkFactory.UserLink(baudrate, delay);
                    User mruser = new User("MRUSER_" + i, link, false, QAGESA.randomSelection, numRequests, repeated, movieTag, User.MEASURE_RESPONSE);
                    router.attachHost(mruser, mruser.getUserSched());
                    mruser.setVirtualOrganization(this);
                    mruser.setUid(i);
                    break;
                case QAGESAMeasures.RMS:
                    link = LinkFactory.UserLink(baudrate, delay);
                    User rmsUser = new User("RMSUSER_" + i, link, false, QAGESA.randomSelection, numRequests, repeated, movieTag, User.MEASURE_STREAMING);
                    router.attachHost(rmsUser, rmsUser.getUserSched());
                    rmsUser.setVirtualOrganization(this);
                    rmsUser.setUid(i);
                    break;
                case QAGESAMeasures.MS:
                    link = LinkFactory.UserLink(baudrate, delay);
                    User msuser = new User("MSUSER_" + i, link, false, QAGESA.randomSelection, numRequests, repeated, movieTag, User.MEASURE_STREAMING);
                    router.attachHost(msuser, msuser.getUserSched());
                    msuser.setVirtualOrganization(this);
                    msuser.setUid(i);
                    break;
                case QAGESAMeasures.RMF:
                    link = LinkFactory.UserLink(baudrate, delay);
                    User rmfUser = new User("RMFUSER_" + i, link, false, QAGESA.randomSelection, numRequests, repeated, movieTag, User.MEASURE_FIRST);
                    router.attachHost(rmfUser, rmfUser.getUserSched());
                    rmfUser.setVirtualOrganization(this);
                    rmfUser.setUid(i);
                    break;
                case QAGESAMeasures.MF:
                    link = LinkFactory.UserLink(baudrate, delay);
                    User mfUser = new User("MFUSER_" + i, link, false, QAGESA.randomSelection, numRequests, repeated, movieTag, User.MEASURE_FIRST);
                    router.attachHost(mfUser, mfUser.getUserSched());
                    mfUser.setVirtualOrganization(this);
                    mfUser.setUid(i);
                    break;
                default:
                    break;
            }
        }
    }

    public int getNumAMs() {
        return this.getNumCEs() + this.getNumSEs();
    }

    public TranscodingSet getTranscodingSet() {
        return transcodingSet;
    }

    public void setTranscodingSet(TranscodingSet transcodingSet) {
        this.transcodingSet = transcodingSet;
    }

    public int getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(int numUsers) {
        this.numUsers = numUsers;
    }

    public boolean isTraceFlag() {
        return traceFlag;
    }

    public void setTraceFlag(boolean traceFlag) {
        this.traceFlag = traceFlag;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public boolean isCachingEnabled() {
        return cachingEnabled;
    }

    public void setCachingEnabled(boolean cachingEnabled) {
        this.cachingEnabled = cachingEnabled;
    }

    public int getWhichMeasure() {
        return whichMeasure;
    }

    public void setWhichMeasure(int whichMeasure) {
        this.whichMeasure = whichMeasure;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }
}
