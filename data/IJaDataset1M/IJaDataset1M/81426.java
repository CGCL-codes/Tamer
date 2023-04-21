package edu.umich.soar.debugger.doc.events;

import sml.Agent;

/************************************************************************
 * 
 * Event fired when a frame switches to tracking a different agent.
 * 
 ************************************************************************/
public class AgentFocusEvent extends java.util.EventObject {

    private static final long serialVersionUID = 2864185010890828622L;

    public static final int kGettingFocus = 1;

    public static final int kLosingFocus = 2;

    public static final int kGone = 3;

    private int m_Event;

    private Agent m_Agent;

    public AgentFocusEvent(Object source, int event, Agent agent) {
        super(source);
        m_Event = event;
        m_Agent = agent;
    }

    public boolean isGettingFocus() {
        return m_Event == kGettingFocus;
    }

    public boolean isLosingFocus() {
        return m_Event == kLosingFocus;
    }

    public boolean isAgentGone() {
        return m_Event == kGone;
    }

    public Agent getAgent() {
        return m_Agent;
    }
}
