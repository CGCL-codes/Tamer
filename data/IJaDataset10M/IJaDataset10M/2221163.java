package org.aacc.campaigns;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aacc.campaigns.notifications.*;

/**
 *  Contains real time statistical information for a campaign.
 */
public class CampaignRealTimeStats implements Observer {

    private static int UPDATER_PRIORITY = 2;

    /**
     *  Total amount of calls dialed
     */
    private long accummulatedCalls = 0;

    /**
     * Amount of active calls overall (answered and not answered)
     */
    private long activeCalls = 0;

    /**
     * Amount of active answered calls
     */
    private long activeAnsweredCalls = 0;

    /**
     * Total amount of answered calls
     */
    private long accummulatedAnsweredCalls = 0;

    /**
     *  Amount of transfers performed, per campaign
     */
    private long transfers = 0;

    /**
     *  Total talk time, per campaign
     */
    private long talkTime = 0;

    /**
     * Amount of dropped (abandoned) calls
     */
    private long drops = 0;

    /**
     * Accumulated queue time for dropped calls
     */
    private long dropTime = 0;

    /**
     *  Total try time, i.e. the time consumed trying to call a contact (dialing)
     */
    private long tryTime = 0;

    /**
     * Amount of sales made. It's just a tally, doesn't differenciate among different
     * campaign products, or even if more than one product is sold in the same transaction.
     */
    private long sales = 0;

    /**
     * Amount of accumulated call time for calls that end in sales
     */
    private long timeToSale = 0;

    /**
     *  Campaign associated with the statistics
     */
    private AbstractCampaign campaign;

    /**
     * This object is used to lock operations, for thread safety
     */
    private Object lockCallCount = new Object();

    /**
     * : Amount of calls currently in queue
     */
    private int N_CurrInQueue = 0;

    /**
     * Time (age) of oldest call in queue
     */
    private int T_CurrMaxInQueue = 0;

    /**
     * Amount of calls received by queue
     */
    private int N_Entered = 0;

    /**
     * Amount of calls answered by agents
     */
    private int N_Answered = 0;

    /**
     * Acummulated queue time for all answered calls.
     */
    private int T_Answered = 0;

    /**
     * Amount of calls answered within the 1 seconds
     */
    private int N_AnsweredOn1 = 0;

    /**
     * Amount of calls answered within the 10 seconds
     */
    private int N_AnsweredOn10 = 0;

    /**
     * Amount of calls answered within the 30 seconds
     */
    private int N_AnsweredOn30 = 0;

    /**
     * Amount of calls answered within the 45 seconds
     */
    private int N_AnsweredOn45 = 0;

    /**
     * Amount of calls answered within the 60 seconds
     */
    private int N_AnsweredOn60 = 0;

    /**
     * Amount of abandoned calls
     */
    private int N_Abandoned = 0;

    /**
     * Acummulated abandon time
     */
    private int T_Abandoned = 0;

    /**
     * Calls abandoned within 10 seconds
     */
    private int N_AbandonedOn10 = 0;

    /**
     * Calls abandoned within 30 seconds
     */
    private int N_AbandonedOn30 = 0;

    /**
     * Calls abandoned within 45 seconds
     */
    private int N_AbandonedOn45 = 0;

    /**
     * Calls abandoned within 60 seconds
     */
    private int N_AbandonedOn60 = 0;

    /**
     * Agents logged in
     */
    private int N_LoggedInAgents = 0;

    /**
     * Paused Agents
     */
    private int N_PausedAgents = 0;

    /**
     * Average queue time Formula= T_Answered / N_Answered
     */
    private float Avg_T_Answer = 0;

    /**
     * Percentage of calls answered by agents. Formula=  (N_Answered * 100) / N_Entered
     */
    private float P_Answered = 0;

    /**
     * Average time to abandon. Formula= T_Abandoned / N_Abandoned
     */
    private float Avg_T_Abandon = 0;

    /**
     * Percentage of abandoned calls. Formula=  (N_ Abandoned * 100) / N_Entered
     */
    private float P_Abandoned = 0;

    /**
     * Available agents. Formula = N_LoggedInAgents - N_PausedAgents
     */
    private int N_AvailableAgents = 0;

    /**
     * This class creates an execution thread that will update realtime statistics 
     * after a predefined amount of time.
     */
    private class RealTimeUpdater implements Runnable {

        public RealTimeUpdater() {
            super();
            Thread t = new Thread(this, RealTimeUpdater.class.getSimpleName());
            try {
                t.setPriority(UPDATER_PRIORITY);
            } catch (Exception e) {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, null, e);
            }
            t.start();
        }

        public void run() {
            try {
                long waitTime = 3000;
                long plusMinusPct = 30;
                long waitMin = waitTime - (waitTime * plusMinusPct / 100);
                long waitMax = waitTime + (waitTime * plusMinusPct / 100);
                long waitFirstTime = Math.round(Math.random() * (waitMax - waitMin) + waitMin);
                Thread.sleep(waitFirstTime);
                while (true) {
                    if (campaign.isActive()) {
                        if (campaign.getClass().equals(AgentCampaign.class)) {
                        }
                    }
                    Thread.sleep(waitTime);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, null, ex);
            }
        }
    }

    private RealTimeUpdater realTimeUpdater;

    public CampaignRealTimeStats(AbstractCampaign campaign) {
        this.campaign = campaign;
        realTimeUpdater = new RealTimeUpdater();
    }

    /**
     * Set all counters to zero
     */
    public void resetStatistics() {
    }

    /**
     * When a new call is being dialed for this campaign, 
     */
    public void notifyCall() {
        synchronized (lockCallCount) {
            accummulatedCalls++;
            activeCalls++;
        }
    }

    /**
     * Adds one to the counter when a call is answered (i.e. a contact is reached)
     */
    public void notifyAnsweredCall() {
        synchronized (lockCallCount) {
            accummulatedAnsweredCalls++;
            activeAnsweredCalls++;
        }
    }

    /**
     * Reports a call that wasn't successfully dialed (e.g. busy, unavailable, etc.)
     */
    public void notifyNonAnsweredHangup(long tryTime) {
        synchronized (lockCallCount) {
            activeCalls--;
            this.tryTime += tryTime;
        }
    }

    /**
     * Reports when an answered call is finished
     */
    public void notifyAnsweredHangup(long talkTime) {
        synchronized (lockCallCount) {
            activeCalls--;
            activeAnsweredCalls--;
            this.talkTime += talkTime;
        }
    }

    /**
     * Reports a call that was dropped
     */
    public void notifyDrop(long secs) {
        synchronized (lockCallCount) {
            activeCalls--;
            activeAnsweredCalls--;
            drops++;
            dropTime += secs;
        }
    }

    /**
     * Report when a transfer is performed
     */
    public void notifyTransfer() {
        synchronized (lockCallCount) {
            transfers++;
        }
    }

    /**
     * Notify time in seconds spent dialing a call
     * @param time
     */
    public void notifyTryTime(long time) {
        synchronized (lockCallCount) {
            this.tryTime += tryTime;
        }
    }

    public void notifySale(long timeToSale) {
        synchronized (lockCallCount) {
            sales++;
            this.timeToSale += timeToSale;
        }
    }

    public long getAccummulatedCalls() {
        return accummulatedCalls;
    }

    public void setAccummulatedCalls(long accummulatedCalls) {
        this.accummulatedCalls = accummulatedCalls;
    }

    public long getActiveCalls() {
        return activeAnsweredCalls;
    }

    public void setActiveCalls(long activeCalls) {
        this.activeAnsweredCalls = activeCalls;
    }

    public AbstractCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(AbstractCampaign campaign) {
        this.campaign = campaign;
    }

    public long getTryTime() {
        return tryTime;
    }

    public void setTryTime(long tryTime) {
        this.tryTime = tryTime;
    }

    public long getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(long talkTime) {
        this.talkTime = talkTime;
    }

    public long getTransfers() {
        return transfers;
    }

    public void setTransfers(long transfers) {
        this.transfers = transfers;
    }

    public long getAccummulatedAnsweredCalls() {
        return accummulatedAnsweredCalls;
    }

    public void setAccummulatedAnsweredCalls(long accummulatedAnsweredCalls) {
        this.accummulatedAnsweredCalls = accummulatedAnsweredCalls;
    }

    public long getActiveAnsweredCalls() {
        return activeAnsweredCalls;
    }

    public void setActiveAnsweredCalls(long activeAnsweredCalls) {
        this.activeAnsweredCalls = activeAnsweredCalls;
    }

    public long getDropTime() {
        return dropTime;
    }

    public void setDropTime(long dropTime) {
        this.dropTime = dropTime;
    }

    public long getDrops() {
        return drops;
    }

    public void setDrops(long drops) {
        this.drops = drops;
    }

    public long getSales() {
        return sales;
    }

    public void setSales(long sales) {
        this.sales = sales;
    }

    public long getTimeToSale() {
        return timeToSale;
    }

    public void setTimeToSale(long timeToSale) {
        this.timeToSale = timeToSale;
    }

    @Override
    public String toString() {
        String s = "";
        s += "campaign: " + campaign.getName();
        s += "|allocatedResources: " + campaign.getAllocatedResourcesCount();
        s += "|availableResources: " + campaign.getAvailableResourcesCount();
        if (campaign.getClass() == AgentCampaign.class) {
            AgentCampaign agentCampaign = (AgentCampaign) campaign;
            s += "|agentsPaused: " + agentCampaign.getPausedAgents();
        }
        s += "|accummulatedAnsweredCalls: " + accummulatedAnsweredCalls;
        s += "|accummulatedCalls: " + accummulatedCalls;
        s += "|activeAnsweredCalls: " + activeAnsweredCalls;
        s += "|activeCalls: " + activeCalls;
        s += "|dialMethod: " + campaign.getDialMethod().getClass().getSimpleName();
        s += "|dialRatio: " + campaign.getDialsPerFreeResourceRatio();
        s += "|dropTime: " + dropTime;
        s += "|drops: " + drops;
        s += "|paused: " + String.valueOf(campaign.isPaused());
        s += "|running: " + String.valueOf(campaign.isRunning());
        s += "|sales: " + sales;
        s += "|timeToSale: " + timeToSale;
        s += "|talkTime: " + talkTime;
        s += "|transfers: " + transfers;
        s += "|tryTime: " + tryTime;
        return s;
    }

    /**
     * Processes events received from observed objects (i.e. the campaign).<br>
     * This is not being used at this time, but could be in a near future...
     * @param o
     * @param arg
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof NotifyActiveCampaign) {
        } else if (arg instanceof NotifyPausedCampaign) {
        }
    }
}
