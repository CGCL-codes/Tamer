package net.sf.gap.grid;

import gridsim.GridSim;
import gridsim.datagrid.index.DataGIS;
import gridsim.datagrid.index.TopRegionalRC;
import gridsim.net.Link;
import gridsim.net.RIPRouter;
import gridsim.net.SimpleLink;
import java.util.HashMap;
import java.util.Vector;
import net.sf.gap.agents.middleware.AbstractAgentPlatform;
import net.sf.gap.agents.middleware.AgentMiddleware;
import net.sf.gap.grid.components.AbstractGridElement;
import net.sf.gap.xml.types.ScenarioType;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class AbstractVirtualOrganization {

    private int MIPS;

    private boolean traceFlag;

    private AbstractAgentPlatform platform;

    private Vector<AbstractGridElement> ces;

    private Vector<AbstractGridElement> ses;

    private int numCEs;

    private int numSEs;

    protected HashMap<Integer, RIPRouter> mapCEs;

    protected HashMap<Integer, RIPRouter> mapSEs;

    private DataGIS dataGIS;

    private TopRegionalRC topRegionalRC;

    private NetworkTopology topology;

    private Vector<AgentMiddleware> ams;

    private String RBname;

    private ScenarioType scenario;

    /**
	 * Creates a new instance of AbstractVirtualOrganization
	 */
    public AbstractVirtualOrganization() {
    }

    public abstract void initialize();

    protected abstract void initializeCEs();

    protected abstract void initializeSEs();

    protected abstract void initializeAgents();

    protected abstract void createEntities() throws Exception;

    public AbstractAgentPlatform getPlatform() {
        return this.platform;
    }

    public void setPlatform(AbstractAgentPlatform agentPlatform) {
        this.platform = agentPlatform;
    }

    public Vector<AbstractGridElement> getCEs() {
        return this.ces;
    }

    public void setCEs(Vector<AbstractGridElement> ces) {
        this.ces = ces;
    }

    public Vector<AbstractGridElement> getSEs() {
        return this.ses;
    }

    public void setSEs(Vector<AbstractGridElement> SEs) {
        this.ses = SEs;
    }

    public int getNumCEs() {
        return this.numCEs;
    }

    public void setNumCEs(int numCEs) {
        this.numCEs = numCEs;
    }

    public int getNumSEs() {
        return this.numSEs;
    }

    public void setNumSEs(int numSEs) {
        this.numSEs = numSEs;
    }

    protected TopRegionalRC createTopRegionalRC() throws Exception {
        TopRegionalRC trrc = null;
        Link l = new SimpleLink("trrc_link", 1000000000.0, 1.0, 1500);
        trrc = new TopRegionalRC(l);
        return trrc;
    }

    protected DataGIS createDataGIS() throws Exception {
        DataGIS gis = null;
        gis = new DataGIS("DataGIS", 1000000000.0);
        GridSim.setGIS(gis);
        return gis;
    }

    public NetworkTopology getTopology() {
        return this.topology;
    }

    public void setTopology(NetworkTopology topology) {
        this.topology = topology;
    }

    public TopRegionalRC getTopRegionalRC() {
        return this.topRegionalRC;
    }

    public void setTopRegionalRC(TopRegionalRC topRegionalRC) {
        this.topRegionalRC = topRegionalRC;
    }

    public DataGIS getDataGIS() {
        return this.dataGIS;
    }

    public void setDataGIS(DataGIS dataGIS) {
        this.dataGIS = dataGIS;
    }

    public HashMap<Integer, RIPRouter> getMapCEs() {
        return this.mapCEs;
    }

    public void setMapCEs(HashMap<Integer, RIPRouter> mapCEs) {
        this.mapCEs = mapCEs;
    }

    public HashMap<Integer, RIPRouter> getMapSEs() {
        return this.mapSEs;
    }

    public void setMapSEs(HashMap<Integer, RIPRouter> mapSEs) {
        this.mapSEs = mapSEs;
    }

    public Vector<AgentMiddleware> getAMs() {
        return this.ams;
    }

    public void setAMs(Vector<AgentMiddleware> AMs) {
        this.ams = AMs;
    }

    public int getNumAMs() {
        return (this.getNumCEs() + this.getNumSEs());
    }

    public abstract void createAndAttachAgentPlatform() throws Exception;

    public abstract void createAndAttachAgents() throws Exception;

    public abstract void createAndAttachCEs() throws Exception;

    public abstract void createAndAttachSEs() throws Exception;

    public abstract void createAndAttachUsers() throws Exception;

    public boolean isTraceFlag() {
        return traceFlag;
    }

    public void setTraceFlag(boolean traceFlag) {
        this.traceFlag = traceFlag;
    }

    public String getRBname() {
        return RBname;
    }

    public void setRBname(String RBname) {
        this.RBname = RBname;
    }

    public ScenarioType getScenario() {
        return scenario;
    }

    public void setScenario(ScenarioType scenario) {
        this.scenario = scenario;
    }

    public int getMIPS() {
        return MIPS;
    }

    public void setMIPS(int MIPS) {
        this.MIPS = MIPS;
    }
}
