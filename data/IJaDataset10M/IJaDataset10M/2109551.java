package br.ufal.ic.forbile.agents.persistence.behaviours;

import br.ufal.ic.forbile.agents.behaviours.GeneralActuator;
import br.ufal.ic.forbile.exceptions.MessageException;
import br.ufal.ic.forbile.infra.GenericKAO;
import br.ufal.ic.forbile.log.PersistenceOWLLog;
import br.ufal.ic.utils.Constants;
import br.ufal.ic.utils.RegionServer;
import br.ufal.ic.utils.SharedMem;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.rmi.Naming;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * RetrieveOWL1.java
 *
 * <p>A full description of this class.
 *
 * @see SomeRelatedClass.
 *
 * @author <a href="mailto:ig.ibert@gmail.com">ig</a>.
 * @since 0.1
 * @version 0.1
 *
 */
public class RetrieveOWL extends OneShotBehaviour {

    private AID receiver;

    private GenericKAO kao;

    private Map retrieveMap;

    private OWLIndividual individualOWL;

    private PersistenceOWLLog persistenceOWLLog;

    private static Logger logger;

    static {
        logger = Logger.getLogger(RetrieveOWL.class.getName());
    }

    /**
     * Creates a new instance of RetrieveOWL1
     */
    public RetrieveOWL(final PersistenceOWLLog persistenceOWLLog, final AID receiver) {
        System.out.println("this.kao = new GenericKAO(persistenceOWLLog.getOwlFile());");
        this.kao = new GenericKAO(persistenceOWLLog.getOwlFile());
        this.retrieveMap = new HashMap();
        this.persistenceOWLLog = persistenceOWLLog;
        this.receiver = receiver;
    }

    public void action() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        logger.info("------- Retrieving requisition -------");
        String key = this.persistenceOWLLog.getRetrieveKey().toUpperCase();
        if (key.equals("MAP")) {
            try {
                this.retrieveMap = kao.retrieve(this.persistenceOWLLog.getSubject(), this.persistenceOWLLog.getPredicate(), this.persistenceOWLLog.getObject());
                this.persistenceOWLLog.setRetrieve(this.retrieveMap);
            } catch (Exception ex) {
                new MessageException(ex);
            }
        } else {
            try {
                this.individualOWL = kao.retrieve(this.persistenceOWLLog.getIndividual(), this.persistenceOWLLog.getClass_());
                this.persistenceOWLLog.setIndividualOWL(this.individualOWL);
            } catch (Exception ex) {
                new MessageException(ex);
            }
        }
        try {
            SharedMem mem = (SharedMem) Naming.lookup(RegionServer.HOST_URL);
            mem.getRegion(Constants.OWLPersistence).write(this.persistenceOWLLog);
            message.setInReplyTo("RETRIEVE");
            logger.info("RetrieveOWL uses Critical Region");
            this.myAgent.addBehaviour(new GeneralActuator(this.receiver, message));
        } catch (Exception e) {
            new MessageException("Err: " + e.getMessage(), e);
        }
        this.myAgent.removeBehaviour(this);
    }
}
