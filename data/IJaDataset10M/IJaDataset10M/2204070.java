package net.teqlo.components.standard.emailV0_1;

import net.teqlo.TeqloException;
import net.teqlo.components.standard.javascriptV0_01.AbstractScriptComponent;
import net.teqlo.db.ExecutorLookup;
import net.teqlo.db.ServiceLookup;

public class EmailActionsExecutor extends AbstractEmailExecutor {

    /**
	 * Constructor as per superclass
	 * @param component
	 * @param fqn
	 */
    public EmailActionsExecutor(AbstractScriptComponent component, ExecutorLookup el, ServiceLookup sl) throws TeqloException {
        super(component, el, sl);
    }
}
