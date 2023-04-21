package com.velocityme.www.actionforms;

import org.apache.struts.action.*;

/**
 *
 * @author  Robert
 */
public class TimeIdActionForm extends ActionForm {

    private int m_timeId;

    /** Creates a new instance of TaskTimeActionForm */
    public TimeIdActionForm() {
    }

    public int getTimeId() {
        return m_timeId;
    }

    public void setTimeId(int timeId) {
        m_timeId = timeId;
    }
}
