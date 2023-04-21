package net.sf.traser.store.dispatcher;

import java.util.logging.Logger;
import net.sf.traser.common.Identifier;
import net.sf.traser.component.BaseComponent;
import org.apache.axiom.om.OMElement;

/**
 *
 * @author szmarcell
 */
public class ConstantDispatcher extends BaseComponent implements Dispatcher {

    /**
     * This field is used to log messages.
     */
    private static final Logger log = Logger.getLogger("net.sf.traser.store.dispatch.URLBasedDispatcher");

    /**
     * This field holds the value to return when asked.
     */
    private boolean answer;

    @Override
    @SuppressWarnings("unchecked")
    public void init(OMElement config) {
        super.init(config);
        answer = Boolean.parseBoolean((String) getInstance("answer"));
    }

    public boolean idIsHostedBy(Identifier id) {
        return answer;
    }
}
