package net.wgen.op.http.filter;

import net.wgen.op.util.AssertionUtils;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Fired by the ParamTriggerFilter when it claims responds to a parameter name.
 *
 * @author Paul Feuer, Wireless Generation, Inc.
 * @version $Id: ParamTrigger.java 8 2007-01-17 15:37:22Z paulfeuer $
 */
public class ParamTrigger {

    private static final Logger LOG = Logger.getLogger(ParamTrigger.class.getName());

    private String _name = null;

    private String _parameterName = null;

    private String _sessionAttributeKey = null;

    private String _sessionRemoveValue = null;

    private List _listeners = new ArrayList();

    public ParamTrigger(String parameterName) {
        AssertionUtils.assertNotNull(parameterName, "parameterName");
        _parameterName = parameterName;
        _name = parameterName;
    }

    public ParamTrigger(String parameterName, String sessionAttributeKey, String sessionRemoveValue) {
        _name = parameterName;
        _parameterName = parameterName;
        _sessionAttributeKey = sessionAttributeKey;
        _sessionRemoveValue = sessionRemoveValue;
    }

    public ParamTrigger(String name, String parameterName, String sessionAttributeKey, String sessionRemoveValue) {
        _name = name;
        _parameterName = parameterName;
        _sessionAttributeKey = sessionAttributeKey;
        _sessionRemoveValue = sessionRemoveValue;
    }

    /**
     * The name of the trigger.
     * @return the name of the trigger
     */
    public String getName() {
        return _name;
    }

    /**
     * Whether or not this trigger cares at all about the parameter.
     *
     * @param parameterName
     *
     * @return whether or not this trigger cares at all about the parameter
     */
    protected boolean respondsTo(String parameterName) {
        return _parameterName.equalsIgnoreCase(parameterName);
    }

    /**
     * Register a listener for trigger firing.
     *
     * @param listener
     */
    public void addListener(ParamTriggerListener listener) {
        _listeners.add(listener);
    }

    /**
     * Remove that listener from trigger firings.
     *
     * @param listener
     */
    public void removeListener(ParamTriggerListener listener) {
        _listeners.remove(listener);
    }

    /**
     * Called by the ParamTriggerFilter after processing the querystring to pull additional values
     * out of the session attributes.
     *
     * @param triggerState
     * @param request
     */
    protected void fireSessionStoredAttribs(ParamTriggerState triggerState, HttpServletRequest request) {
        if (_sessionAttributeKey != null) {
            Object savedValue = request.getSession().getAttribute(_sessionAttributeKey);
            if (savedValue != null) {
                LOG.debug(triggerState.getTraceKey() + " Found '" + _sessionAttributeKey + "' in session with value '" + savedValue + "'");
                notifyListeners(_parameterName, savedValue, triggerState);
            }
        }
    }

    /**
     *
     * @param parameterName
     * @param value
     * @param triggerState
     * @param request
     */
    protected void fireTrigger(String parameterName, String value, ParamTriggerState triggerState, HttpServletRequest request) {
        evaluateSessionStoring(parameterName, value, triggerState, request);
        notifyListeners(parameterName, value, triggerState);
    }

    /**
     * Called by the ParamTriggerFilter when the filterChain returns from processing.
     *
     * @param triggerState
     * @param request
     */
    protected void notifyOfRequestCompletion(ParamTriggerState triggerState, HttpServletRequest request) {
        synchronized (_listeners) {
            for (int i = 0; i < _listeners.size(); i++) {
                ((ParamTriggerListener) _listeners.get(i)).handleFilterCompletion(this, triggerState, request);
            }
        }
    }

    /**
     * If there is a specified _sessionAttributeKey, we should store the value in the session
     * attributes unless the value is equal to the _sessionRemoveValue, in which case we should
     * remove the session attribute.
     *
     * @param parameterName
     * @param value
     * @param triggerState
     * @param request
     */
    protected void evaluateSessionStoring(String parameterName, String value, ParamTriggerState triggerState, HttpServletRequest request) {
        if (_sessionAttributeKey != null) {
            boolean turnOff = value != null && _sessionRemoveValue != null && value.equalsIgnoreCase(_sessionRemoveValue);
            if (turnOff) {
                LOG.debug(triggerState.getTraceKey() + " trigger '" + getName() + "' removing " + _sessionAttributeKey + " from session");
                request.getSession().removeAttribute(_sessionAttributeKey);
            } else {
                LOG.debug(triggerState.getTraceKey() + " trigger '" + getName() + "' adding " + _sessionAttributeKey + "=" + value + " to session");
                request.getSession().setAttribute(_sessionAttributeKey, value);
            }
        }
    }

    /**
     * Fire the listeners registered for this trigger.
     *
     * @param parameterName
     * @param value
     * @param triggerState
     */
    protected void notifyListeners(String parameterName, Object value, ParamTriggerState triggerState) {
        synchronized (_listeners) {
            for (int i = 0; i < _listeners.size(); i++) {
                ((ParamTriggerListener) _listeners.get(i)).handleTriggerValue(this, parameterName, value, triggerState);
            }
        }
    }
}
