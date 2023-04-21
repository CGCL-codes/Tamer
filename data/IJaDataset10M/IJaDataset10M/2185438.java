package internalactions;

import jason.asSemantics.Circumstance;
import jason.asSemantics.CircumstanceListener;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Event;
import jason.asSemantics.Intention;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.InternalActionLiteral;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.PlanBodyImpl;
import jason.asSyntax.Term;
import jason.asSyntax.Trigger;
import jason.asSyntax.PlanBody.BodyType;
import jason.stdlib.suspend;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** * Internal action: .<b><code>.waitNoStr(<i>E</i>,<i>T</i>)</code></b>. *  * <p> * Description: version of .wait action in standard Jason lib The difference is its argument is not * a string. Useful for using literals as variables since .concat works on literals and literal do * not accept \" as a valid char. In summary <code>.waitNoStr(bel,Literal)<code> := * <code>.wait(.concat(""\",Literal,"\"")<code> which is not permitted in <code>.wait<code>. *  * <p> Parameters: * <ul> * <li><i>+ event</i> (string): the event to wait for.<br/> * <li>+ timeout (number).<br/> * </ul> *  *  * Parameters that are not string are concatenated using the toString method of their class. *  * <p>Examples:<ul> * <li> <code>.wait(1000)</code>: suspend the intention for 1 second. * *<li> <code>.wait("+",bel(1))</code>: suspend the intention until the belief *<code>bel(1)</code> is added in the belief base. * *<li> <code>.wait("!",LiteralSymbol, 2000)</code>: suspend the intention until the goal *<code>LiteralSymbol</code> is triggered or 2 seconds have passed, whatever happens *first. In case the event does not happens in two seconds, the internal action *fails. * *  * @author lacay * */
public class waitNoStr extends DefaultInternalAction {

    private static final long serialVersionUID = 5747459909753571205L;

    private Logger logger = Logger.getLogger("ExtrospectiveJason." + waitNoStr.class.getName());

    @Override
    public boolean canBeUsedInContext() {
        return false;
    }

    @Override
    public boolean suspendIntention() {
        return true;
    }

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        long timeout = -1;
        Trigger te = null;
        try {
            if (args[0].isNumeric()) {
                NumberTerm time = (NumberTerm) args[0];
                timeout = (long) time.solve();
            } else if (args[0].isString() && args.length > 1) {
                String inputArg = args[0].toString().substring(1, args[0].toString().lastIndexOf("\""));
                Term st = (Term) args[1];
                st.apply(un);
                if (inputArg.equalsIgnoreCase("+")) {
                    String strAdd = st.toString().substring(1, st.toString().lastIndexOf("\""));
                    te = Trigger.parseTrigger("+" + strAdd);
                } else {
                    String strAdd = st.toString().substring(1, st.toString().lastIndexOf("\""));
                    te = Trigger.parseTrigger("!" + strAdd);
                }
                if (args.length == 3) {
                    NumberTerm tot = (NumberTerm) args[1];
                    timeout = (long) tot.solve();
                }
            }
        } catch (Exception e) {
            ts.getLogger().log(Level.SEVERE, "Error at .waitNoStr.", e);
            return false;
        }
        WaitEvent wet = new WaitEvent(te, un, ts, timeout);
        wet.start();
        return true;
    }

    private List<WaitEvent> threads = Collections.synchronizedList(new ArrayList<WaitEvent>());

    public void stopAllWaits() {
        for (WaitEvent t : threads) {
            t.interrupt();
        }
    }

    class WaitEvent extends Thread implements CircumstanceListener {

        Trigger te;

        String sTE;

        Unifier un;

        Intention si;

        TransitionSystem ts;

        Circumstance c;

        boolean ok = false;

        boolean drop = false;

        boolean stopByTimeout = false;

        long timeout = -1;

        WaitEvent(Trigger te, Unifier un, TransitionSystem ts, long to) {
            super("wait " + te);
            this.te = te;
            this.un = un;
            this.ts = ts;
            c = ts.getC();
            si = c.getSelectedIntention();
            this.timeout = to;
            c.addEventListener(this);
            if (te != null) {
                sTE = te.toString();
            } else {
                sTE = "time" + (timeout);
            }
            sTE = si.getId() + "/" + sTE;
            c.getPendingIntentions().put(sTE, si);
            threads.add(this);
        }

        public void run() {
            try {
                waitEvent();
                c.removeEventListener(this);
                if (c.getPendingIntentions().remove(sTE) == si && !c.getIntentions().contains(si) && !drop) {
                    si.peek().removeCurrentStep();
                    if (stopByTimeout && te != null) {
                        si.peek().getPlan().getBody().add(0, new PlanBodyImpl(BodyType.internalAction, new InternalActionLiteral(".fail")));
                    }
                    if (si.isSuspended()) {
                        String k = suspend.SUSPENDED_INT + si.getId();
                        c.getPendingIntentions().put(k, si);
                    } else {
                        c.addIntention(si);
                    }
                }
            } catch (Exception e) {
                ts.getLogger().log(Level.SEVERE, "Error at .wait thread", e);
            } finally {
                threads.remove(this);
            }
        }

        public synchronized void waitEvent() {
            long init = System.currentTimeMillis();
            long pass = 0;
            while (!ok && !drop) {
                try {
                    if (timeout == -1) {
                        wait();
                    } else {
                        long to = timeout - pass;
                        if (to <= 0) to = 100;
                        wait(to);
                        pass = System.currentTimeMillis() - init;
                        if (pass >= timeout) {
                            stopByTimeout = true;
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    drop = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public synchronized void eventAdded(Event e) {
            if (te != null && !drop && un.unifies(te, e.getTrigger())) {
                ok = true;
                notifyAll();
            }
        }

        public synchronized void intentionDropped(Intention i) {
            if (i.equals(si)) {
                ok = false;
                drop = true;
                notifyAll();
            }
        }

        public void intentionAdded(Intention i) {
        }

        public String toString() {
            return sTE;
        }
    }
}
