package net.sourceforge.rules.service;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * 
 * @version $Revision: 671 $ $Date: 2010-06-01 21:43:58 -0400 (Tue, 01 Jun 2010) $
 * @author <a href="mailto:rlangbehn@users.sourceforge.net">Rainer Langbehn</a>
 */
public interface StatelessDecisionService {

    /**
	 * Executes the <code>javax.rules.RuleExecutionSet</code> registered and
	 * associated with the URI given by the parameter <code>bindUri</code>.
	 * 
	 * @param bindUri
	 * @param properties
	 * @param inputObjects
	 * @return
	 * @throws DecisionServiceException if an error prevented the execution
	 */
    List<?> decide(String bindUri, Map<?, ?> properties, List<?> inputObjects) throws DecisionServiceException;
}
