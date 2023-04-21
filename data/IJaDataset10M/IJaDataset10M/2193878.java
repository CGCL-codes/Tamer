package com.jrandrews.statemachine;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Minimal action; logs it was used, and any parameters set for it; used in prototyping 
 * and debugging flow.
 * 
 * @author JAndrews
 */
public abstract class BaseStateUpdate implements StateUpdate {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    protected List m_params = new ArrayList();

    /**
     * Retrieves a parameter, assumed to be a String, or null.
     * @param i index of the parameter to retrieve; 0 to "n-1"
     * @return
     */
    protected String getStringParameter(int i) {
        String result = null;
        if (m_params.size() < i + 1) {
            logger.debug("Unable to retrieve expected parameter " + i + ": params = " + m_params);
        } else if (m_params.get(i) instanceof String) {
            result = (String) m_params.get(i);
        } else {
            logger.debug("params[" + i + "] is a " + m_params.get(i).getClass().getName() + "; expected a String");
        }
        return result;
    }

    /**
     * Retrieves a parameter, assumed to be an Integer, or null.
     * @param i index of the parameter to retrieve; 0 to "n-1"
     * @return the identified parameter, or null if it is missing or can't be converted to an Integer
     */
    protected Integer getIntegerParameter(int i) {
        Integer result = null;
        if (m_params.size() < i + 1) {
            logger.debug("Unable to retrieve expected parameter " + i + ": params = " + m_params);
        } else if (m_params.get(i) instanceof Integer) {
            result = (Integer) m_params.get(i);
        } else {
            logger.debug("params[" + i + "] is a " + m_params.get(i).getClass().getName() + "; expected an Integer");
        }
        return result;
    }

    /**
     * Replaces the current list with the newly-provided list.
     * @param params a list of params to set, or null.
     */
    public void setParameters(List params) {
        if (params != null) m_params = params; else m_params = new ArrayList();
    }

    /** @see StateUpdate#invoke() */
    public abstract void invoke(Context context);
}
