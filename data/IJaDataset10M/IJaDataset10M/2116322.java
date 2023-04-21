package gridrm.test.cic;

import gridrm.test.cic.tests.CICExperiment;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import test.common.Logger;
import test.common.TestException;
import test.common.TestGroup;
import test.common.TesterAgent;

public class CICExperimentAgent extends TesterAgent {

    public static final String SERVICE_TYPE_EXP_COORDINATOR = "experiment-coordinator";

    public static final String SERVICE_NAME = "experiment-coordinator";

    private Logger log = Logger.getLogger();

    protected TestGroup getTestGroup() {
        log.log("Preparing CIC Experiment...");
        TestGroup tg = new TestGroup("gridrm/test/cic/cicExperimentsList.xml") {

            protected void initialize(Agent a) throws TestException {
                log.log("Initializing CIC Experiment Group...");
                DFAgentDescription dfd = new DFAgentDescription();
                dfd.setName(getAID());
                ServiceDescription sd = new ServiceDescription();
                sd.setType(SERVICE_TYPE_EXP_COORDINATOR);
                sd.setName(SERVICE_NAME);
                dfd.addServices(sd);
                try {
                    DFService.register(CICExperimentAgent.this, dfd);
                } catch (FIPAException fe) {
                    throw new TestException("Could not register CICExperimentAgent in the DF.");
                }
            }

            protected void shutdown(Agent a) {
                log.log("Shutting down CIC Experiment Group...");
                try {
                    DFService.deregister(a);
                } catch (FIPAException e) {
                    log.log("Could not deregister from DF.");
                }
                super.shutdown(a);
            }
        };
        tg.specifyArgument(CICExperiment.ARG_SPAMMER_COUNT, CICExperiment.ARG_SPAMMER_COUNT);
        tg.specifyArgument(CICExperiment.ARG_TOTAL_MSG_COUNT, CICExperiment.ARG_TOTAL_MSG_COUNT);
        tg.specifyArgument(CICExperiment.ARG_VARYING_PARAM_MIN, CICExperiment.ARG_VARYING_PARAM_MIN);
        tg.specifyArgument(CICExperiment.ARG_VARYING_PARAM_MAX, CICExperiment.ARG_VARYING_PARAM_MAX);
        tg.specifyArgument(CICExperiment.ARG_VARYING_PARAM_STEP, CICExperiment.ARG_VARYING_PARAM_STEP);
        tg.specifyArgument(CICExperiment.ARG_VARYING_PARAM_TYPE, CICExperiment.ARG_VARYING_PARAM_TYPE);
        return tg;
    }
}
