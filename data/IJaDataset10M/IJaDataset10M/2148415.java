package com.fujitsu.arcon.njs.actions;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.TimerTask;
import org.unicore.ajo.AbstractAction;
import org.unicore.ajo.WaitUntil;
import org.unicore.outcome.AbstractActionStatus;
import org.unicore.outcome.Outcome;
import org.unicore.outcome.StatusHistory;
import org.unicore.outcome.WaitUntil_Outcome;
import com.fujitsu.arcon.njs.KnownActionDB;
import com.fujitsu.arcon.njs.NJSGlobals;

/**
 *
 * @author Sven van den Berghe, fujitsu
 *
 * @version $Revision: 1.2 $ $Date: 2004/06/30 13:45:26 $
 *
 **/
public class DoWaitUntil extends NKnownAction implements KnownAction.StatusChange {

    public static class Factory extends KnownAction.Factory {

        public KnownAction create(AbstractAction a, Outcome o, ParentAction p, RootAJO r, KnownActionDB k) {
            return new DoWaitUntil(a, o, p, r, k);
        }
    }

    public DoWaitUntil(AbstractAction a, Outcome o, ParentAction p, RootAJO r, KnownActionDB k) {
        super(a, p, r, k);
        if (o == null) {
            outcome = new WaitUntil_Outcome((WaitUntil) a, AbstractActionStatus.PENDING);
        } else {
            outcome = (WaitUntil_Outcome) o;
        }
        setAAType("WTUNTL");
        if (outcome.getStatus().isEquivalent(AbstractActionStatus.EXECUTING)) {
            NJSGlobals.getTimer().schedule(new DWUTimerTask2(), 1);
        }
    }

    public boolean isInteresting() {
        return true;
    }

    public synchronized void process() {
        if (dontProcess()) return;
        WaitUntil wu = (WaitUntil) action;
        if (wu.getTime() == null) {
            failedIncarnation("No time");
            return;
        }
        if (wu.getTime().getOffset() < 0l) {
            failedIncarnation("Negative offset");
            return;
        }
        logTrace("Executing until: " + ((WaitUntil) action).getTime());
        outcome.setReason("Waiting until: " + ((WaitUntil) action).getTime());
        statusChanged();
    }

    private TimerTask my_timer_task;

    public void statusChanged() {
        WaitUntil wu = (WaitUntil) action;
        if (wu.getTime().getTarget() == null) {
            try {
                NJSGlobals.getTimer().schedule(new DWUTimerTask(), wu.getTime().getOffset() - System.currentTimeMillis());
                setStatus(AbstractActionStatus.EXECUTING);
                return;
            } catch (Exception ex) {
                successful("Wait time passed before start of execution");
                return;
            }
        } else {
            KnownAction ka;
            try {
                ka = getKnownAction(wu.getTime().getTarget().getId());
            } catch (com.fujitsu.arcon.njs.interfaces.NJSException nex) {
                failedExecution(nex.getMessage());
                return;
            }
            boolean not_done = true;
            while (not_done) {
                try {
                    Iterator i = ka.getOutcome().getStatusHistory().iterator();
                    while (i.hasNext()) {
                        StatusHistory.Entry s = (StatusHistory.Entry) i.next();
                        if (s.getStatus().isEquivalent(wu.getTime().getStatus())) {
                            try {
                                my_timer_task = new DWUTimerTask();
                                NJSGlobals.getTimer().schedule(my_timer_task, s.getTime().getTime() + wu.getTime().getOffset() - System.currentTimeMillis());
                            } catch (Exception ex) {
                                successful("Wait time passed before start of execution");
                            }
                            return;
                        }
                    }
                    not_done = false;
                } catch (ConcurrentModificationException cmex) {
                    if (ka == this) return;
                }
            }
            if (ka.getStatus().equals(AbstractActionStatus.DONE)) {
                ka.addStatusChangeListener(this);
            } else if (ka.getStatus().isEquivalent(AbstractActionStatus.DONE)) {
                failedExecution("Target action never went through <" + wu.getTime().getStatus() + "> finished as <" + ka.getStatus() + ">");
            } else {
                ka.addStatusChangeListener(this);
            }
        }
    }

    class DWUTimerTask extends TimerTask {

        public void run() {
            DoWaitUntil.this.completeExecution();
        }
    }

    class DWUTimerTask2 extends TimerTask {

        public void run() {
            DoWaitUntil.this.statusChanged();
        }
    }

    void completeExecution() {
        successful("Wait time passed");
        my_timer_task = null;
    }

    public void abort() throws com.fujitsu.arcon.njs.interfaces.NJSException {
        if (getStatus().isEquivalent(AbstractActionStatus.EXECUTING)) {
            if (my_timer_task != null) my_timer_task.cancel();
        }
        super.abort();
    }

    protected void dropOutcome() {
        outcome = new WaitUntil_Outcome((WaitUntil) action, getStatus());
        logTrace("Previous Outcome deleted after a CANCEL");
    }

    public String getDetailedListing() {
        String result = super.getDetailedListing();
        try {
            result += "TIME:            " + ((WaitUntil) action).getTime() + "\n";
        } catch (Exception ex) {
            result += "ERROR GETTING INFORMATION, INCOMPLETE FIELDS? " + ex + "\n";
        }
        return result;
    }
}
