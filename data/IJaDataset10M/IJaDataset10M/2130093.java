package org.jaffa.presentation.portlet.session.ui;

import org.apache.log4j.Logger;
import org.jaffa.presentation.portlet.component.Component;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;

/**
 *
 * @author  PaulE
 * @version 1.0
 */
public class SessionExplorerComponent extends Component {

    private static Logger log = Logger.getLogger(SessionExplorerComponent.class);

    /** Holds value of property sessionId. */
    private String sessionId;

    /** Holds value of property componentId. */
    private String compId;

    public FormKey display() {
        FormKey fk = new FormKey(SessionExplorerForm.NAME, getComponentId());
        return fk;
    }

    public FormKey viewComponent(String sessionId) throws ApplicationExceptions, FrameworkException {
        if (log.isDebugEnabled()) log.debug("View Detailed For Session : " + sessionId);
        this.sessionId = sessionId;
        FormKey fk = new FormKey(ComponentExplorerForm.NAME, getComponentId());
        return fk;
    }

    public FormKey introspectComponent(String compId) throws ApplicationExceptions, FrameworkException {
        if (log.isDebugEnabled()) log.debug("View Detailed For Component : " + compId);
        this.compId = compId;
        FormKey fk = new FormKey(ComponentDetailsForm.NAME, getComponentId());
        return fk;
    }

    /** Getter for property sessionId.
     * @return Value of property sessionId.
     */
    public String getSessionId() {
        return this.sessionId;
    }

    /** Getter for property compId.
     * @return Value of property compId.
     */
    public String getCompId() {
        return this.compId;
    }
}
