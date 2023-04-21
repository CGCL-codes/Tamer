package hu.usz.inf.netspotter.agentlogic;

import java.util.Vector;

public interface AgentLogic {

    public String getTestCaseId();

    public void run();

    public void prepare();

    public Vector<FeedBack> getFeedback();
}
