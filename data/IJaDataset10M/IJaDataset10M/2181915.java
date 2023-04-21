package net.sf.istcontract.aws.agentshell.components;

import net.sf.istcontract.aws.agentshell.domain.BehaviourList;
import net.sf.istcontract.aws.agentshell.domain.Event;
import net.sf.istcontract.aws.agentshell.interfaces.ICommunicationManager;
import net.sf.istcontract.aws.agentshell.interfaces.IContractManager;
import net.sf.istcontract.aws.agentshell.interfaces.IDecisionMaker;
import net.sf.istcontract.aws.agentshell.interfaces.IMessageManager;
import net.sf.istcontract.aws.agentshell.interfaces.IWorkflowManager;

public class DecisionMakerDummy implements IDecisionMaker {

    public void eventReceive(String senderModuleId, Event event) {
    }

    public void initDecisionMaker(BehaviourList bl) {
    }

    public void setParams(IContractManager my_cm, IWorkflowManager my_wm, ICommunicationManager my_cmm, IMessageManager my_mm) {
    }
}
