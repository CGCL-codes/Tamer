package mms;

import mms.clock.VirtualClockHelper;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentState;

public abstract class Reasoning extends MusicalAgentComponent {

    VirtualClockHelper clock = getAgent().getClock();

    public Reasoning(String myName, MusicalAgent myAgent) {
        super(myName, myAgent, Constants.COMP_REASONING);
        if (myAgent.getProperty(Constants.PROCESS_MODE, null).equals(Constants.MODE_REAL_TIME)) {
        }
    }

    public void setWakeUp(long time) {
        getAgent().getClock().schedule(getAgent(), new Reason(getAgent()), time);
    }

    class Reason extends OneShotBehaviour {

        public Reason(Agent a) {
            super(a);
        }

        public void action() {
            if (getAgent().state == 2) {
                MusicalAgent.logger.info("[" + getAgent().getLocalName() + "] " + "Iniciei o racioc�nio");
                process();
                if (getAgent().getProperty(Constants.PROCESS_MODE, null).equals(Constants.MODE_BATCH)) {
                    setWakeUp(clock.getCurrentTime() + 1);
                }
                getAgent().reasoningProcessDone(getName());
            }
        }
    }

    private class ReasonCyclic extends CyclicBehaviour {

        public ReasonCyclic(Agent a) {
            super(a);
        }

        public void action() {
            System.out.println("Teste!");
        }
    }

    protected void newEventHandler(EventHandler evtHdl) {
    }

    ;

    public void newSense(String eventType) {
    }

    ;

    public void needAction(Actuator sourceActuator, long workingFrame) {
    }

    ;

    public void process() {
    }

    ;
}
