package org.isurf.spmbl.workitems;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.fosstrak.ale.xsd.epcglobal.EPC;

/**
 * Work item to be used in Drools processes. Filters out EPCs which do not match the specified regular expression.
 */
public class FilterEPCsByRegex implements WorkItemHandler {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FilterEPCsByRegex.class);

    /**
	 * Constructs a {@link FilterEPCsByReader}.
	 */
    public FilterEPCsByRegex(StatefulKnowledgeSession ksession) {
    }

    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        Set<EPC> epcs = (Set<EPC>) workItem.getParameter("epcs");
        String regex = (String) workItem.getParameter("regex");
        Set<EPC> filteredEPCS = filter(epcs, regex);
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("filteredEPCS", filteredEPCS);
        manager.completeWorkItem(workItem.getId(), results);
    }

    /**
	 * Filters the EPCs which do not match.
	 * 
	 * @param epcs
	 * @param regex
	 * @return
	 */
    protected Set<EPC> filter(Set<EPC> epcs, String regex) {
        logger.debug("filter: epcs = " + epcs + "; regex = " + regex);
        Set<EPC> filteredEPCs = new HashSet<EPC>();
        if (epcs != null && regex != null) {
            Pattern pattern = Pattern.compile(regex);
            for (EPC epc : epcs) {
                Matcher m = pattern.matcher(epc.getValue());
                if (m.matches()) {
                    filteredEPCs.add(epc);
                }
            }
        }
        logger.debug("filter: filteredEPCs = " + filteredEPCs);
        return filteredEPCs;
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
    }
}
