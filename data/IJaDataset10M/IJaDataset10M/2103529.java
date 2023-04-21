package arch;

import edu.umich.eecs.tac.props.QueryReport;
import edu.umich.eecs.tac.props.SalesReport;

/**
 * The Interface implemented by every functionality component
 * 
 * @author Mariano Schain
 */
public interface IAgentComponent {

    /**
     * Sets the interface to the containing agent. 
     *
     * @param a implementing IAgent
     */
    public void setAgent(IAgent agent);

    /**
     * Called by the containing agent before the beginning of a new simulation
     * giving the AgentComponent the opportunity to prepare appropriately 
     */
    public void simulationSetup();

    /**
     * Called by the containing agent to indicate the start of the simulation
     * This indicates that all the simulation parameters are available to fetch from the Agent
     */
    public void simulationReady();

    /**
     * Called by the containing agent to indicate the end of the simulation
     */
    public void simulationFinished();

    /**
     * Called daily by the containing agent
     * 
     * @param quaryReport the message containing the report data as received from the server
     * @param yday the day the message refers to (yesterday)
     */
    public void handleQueryReport(QueryReport queryReport, int yday);

    /**
     * Called daily by the containing agent
     * 
     * @param SalesReport the message containing the report data as received from the server
     * @param yday the day the message refers to (yesterday)
     */
    public void handleSalesReport(SalesReport salesReport, int yday);

    /**
     * Called daily by the containing agent to indicate the end of the day so the component may 
     * perform all actions required to be ready for the next day
     * @param day the up-coming day
     */
    public void nextDay(int day);
}
