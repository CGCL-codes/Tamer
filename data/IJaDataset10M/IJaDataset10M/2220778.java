package net.ekology.ekosystem;

import net.ekology.graphics.*;
import net.ekology.ekosystem.datatypes.*;
import net.ekology.core.datatypes.AutomatonEvent;
import net.ekology.core.datatypes.AutomatonEventType;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import java.util.GregorianCalendar;
import java.awt.Graphics2D;
import java.io.Serializable;

/**
 * Simbolizes an ecosystem i.e. biotic and abiotic agents plus a biotope
 * 
 * @author Aarón Tavío - aaron.tavio at gmail.com
 * @version 1.0.0 - 20081019-1700
 */
public class EKosystem implements Drawable, Serializable {

    private Calendar oCreationDate;

    private long lExecutionTime;

    private double dTimeFactor;

    private Hashtable<String, Vector<EKEvent>> htEKEvent;

    private Hashtable<String, Agent> htAgent;

    private Biotope oBiotope;

    public EKosystem() {
        oCreationDate = new GregorianCalendar();
        lExecutionTime = 0;
        dTimeFactor = 1.0;
        htEKEvent = new Hashtable<String, Vector<EKEvent>>();
        htAgent = new Hashtable<String, Agent>();
        oBiotope = null;
    }

    public EKosystem(Biotope oBiotope) {
        oCreationDate = new GregorianCalendar();
        lExecutionTime = 0;
        dTimeFactor = 1.0;
        htEKEvent = new Hashtable<String, Vector<EKEvent>>();
        htAgent = new Hashtable<String, Agent>();
        setBiotope(oBiotope);
    }

    public EKosystem(Calendar oCreationDate, long lExecutionTime, Hashtable htEKEvent, Biotope oBiotope) {
        this.oCreationDate = oCreationDate;
        this.lExecutionTime = lExecutionTime;
        this.dTimeFactor = 1.0;
        this.htEKEvent = htEKEvent;
        this.oBiotope = oBiotope;
        this.htAgent = new Hashtable<String, Agent>();
    }

    public Biotope getBiotope() {
        return oBiotope;
    }

    public void setBiotope(Biotope oBiotope) {
        this.oBiotope = oBiotope;
        oBiotope.setEKosystem(this);
        oBiotope.initialize();
    }

    public void addAgent(Agent oAgent) {
        if (htAgent.get(oAgent.getID()) == null) htAgent.put(oAgent.getID(), oAgent);
    }

    public int getAgentCount() {
        return htAgent.size();
    }

    public Agent[] getAgents() {
        Agent[] aAgent;
        aAgent = new Agent[htAgent.size()];
        return htAgent.values().toArray(aAgent);
    }

    public Agent getAgent(String sID) {
        return (sID != null) ? htAgent.get(sID) : null;
    }

    public String[] getAgentIDs() {
        String[] aAgentID;
        aAgentID = new String[htAgent.size()];
        return htAgent.keySet().toArray(aAgentID);
    }

    public void removeAgent(String sAgentID) {
        htAgent.remove(sAgentID);
        htEKEvent.remove(sAgentID);
    }

    public boolean containsAgent(String sID) {
        return htAgent.containsKey(sID);
    }

    public void addEvent(EKEvent oEKEvent) {
        Vector<EKEvent> vEKEvent;
        vEKEvent = htEKEvent.get(oEKEvent.getTarget());
        if (vEKEvent == null) vEKEvent = new Vector<EKEvent>();
        vEKEvent.add(oEKEvent);
        htEKEvent.put(oEKEvent.getTarget(), vEKEvent);
    }

    /**
     * Returns the events of an agent and erases the corresponding entry in the event table
     * <p>
     * Invokes oBiotope.checkState() to generate events
     * 
     * @param sAgentID the agent's identifier
     * @param sCurrentState the agent's current state name
     * @return vector containing trigered EKEvents for a given agent in a given state
     */
    public Vector<EKEvent> getEvents(String sAgentID, String sCurrentState) {
        Vector<EKEvent> vEKEvent;
        oBiotope.checkState(sAgentID, sCurrentState);
        vEKEvent = htEKEvent.get(sAgentID);
        htEKEvent.remove(sAgentID);
        return vEKEvent;
    }

    /**
     * Given an agent, an automaton event and the available time, passes the control
     * to the agent
     * <p>
     * <code>AgentAutomatonManager</code> invokes this method to comunicate automaton
     * events to agents. Invokes Agent._ENTRY(), ._DO() or ._EXIT() when needed. Time is measured in milliseconds
     * 
     * @param sAgentID the agent's identifier
     * @param oAutomatonEvent the event to be processed by the agent
     * @param iTimeSlice available time
     * @return time spent by the agent
     */
    public int attendAutomatonEvent(String sAgentID, AutomatonEvent oAutomatonEvent, int iTimeSlice) {
        Agent oAgent;
        int iTimeUsed;
        int iEKTimeSlice;
        int iEKTimeUsed;
        oAgent = htAgent.get(sAgentID);
        if (oAgent != null) {
            iEKTimeSlice = (int) Math.ceil(iTimeSlice * dTimeFactor);
            if (oAutomatonEvent.oType == AutomatonEventType.ENTRY) iEKTimeUsed = oAgent._ENTRY(oAutomatonEvent.sStateName, oAutomatonEvent.oFiringEvent, iEKTimeSlice); else if (oAutomatonEvent.oType == AutomatonEventType.DO) iEKTimeUsed = oAgent._DO(oAutomatonEvent.sStateName, iEKTimeSlice); else iEKTimeUsed = oAgent._EXIT(oAutomatonEvent.sStateName, oAutomatonEvent.oFiringEvent, iEKTimeSlice);
            iTimeUsed = (int) Math.ceil(iEKTimeUsed / dTimeFactor);
        } else iTimeUsed = iTimeSlice;
        return iTimeUsed;
    }

    public void addElapsedTime(int iMiliseconds) {
        lExecutionTime += iMiliseconds;
        oBiotope.addElapsedTime((int) dTimeFactor * iMiliseconds);
    }

    public double getTimeFactor() {
        return dTimeFactor;
    }

    public void setTimeFactor(double dTimeFactor) {
        this.dTimeFactor = dTimeFactor;
    }

    public void loopBegin() {
        if (oBiotope != null) oBiotope.preprocess();
    }

    public void loopEnd() {
        if (oBiotope != null) oBiotope.postprocess();
    }

    public void draw(Graphics2D g2) {
        if (oBiotope != null) oBiotope.draw(g2);
    }
}
