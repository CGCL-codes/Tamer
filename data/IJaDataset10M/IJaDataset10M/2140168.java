package org.isurf.spmbl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItemManager;
import org.drools.runtime.process.WorkflowProcessInstance;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReports;
import org.isurf.spmbl.workitems.ParseALE;
import org.isurf.spmiddleware.utils.XMLUtils;

/**
 * 
 */
public class KnowledgeAgentIT {

    private static final Logger logger = Logger.getLogger(KnowledgeAgentIT.class);

    private StatefulKnowledgeSession session;

    private KnowledgeRuntimeLogger klogger;

    public static void main(String[] args) {
    }

    public KnowledgeAgentIT() {
        this.session = createSession();
        registerWorkItems();
    }

    private StatefulKnowledgeSession createSession() {
        KnowledgeBase kbase = loadRuleBase();
        StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
        klogger = KnowledgeRuntimeLoggerFactory.newFileLogger(session, "logs/ale");
        session.fireAllRules();
        return session;
    }

    private KnowledgeBase loadRuleBase() {
        KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("MyAgent");
        kagent.applyChangeSet(ResourceFactory.newClassPathResource("change-set.xml"));
        KnowledgeBase kbase = kagent.getKnowledgeBase();
        ResourceFactory.getResourceChangeNotifierService().start();
        ResourceFactory.getResourceChangeScannerService().start();
        return kbase;
    }

    /**
	 * Receives an ALE and creates and sends the EPCIS event.
	 * 
	 * @param message The ALE as an XML String.
	 */
    public void handleMessage(Serializable message) {
        logger.debug(this + "handleMessage: message = " + message);
        String xml = (String) message;
        try {
            ECReports ale = (ECReports) XMLUtils.fromXML(xml, ECReports.class);
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("ale", ale);
            WorkflowProcessInstance process = (WorkflowProcessInstance) session.startProcess("SimpleEPCISDocument", parameters);
            session.insert(process);
            session.fireAllRules();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
	 * Registers custom work items.
	 */
    private void registerWorkItems() {
        WorkItemManager workItemManager = session.getWorkItemManager();
        ParseALE handler = new ParseALE(session);
        workItemManager.registerWorkItemHandler("ParseALE", handler);
    }

    public void dispose() {
        klogger.close();
    }
}
