package rbsla.eca;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import org.mandarax.kernel.InferenceException;
import org.mandarax.kernel.ResultSet;
import rbsla.wrapper.KnowledgeBaseWrapper;
import rbsla.environment.dialogs.ECARuleWindow;
import rbsla.utils.ConsoleWriter;

/**
 * Starts a Deamon which check for events and do actions, if necessary.
 * 
 * Also a Multithreading Variant is available.
 * 
 * @author <A HREF="mailto:paschke@in.tum.de">Adrian Paschke</A>
 * @version 0.1 <1 Sept 2005>
 * @since 0.1
 * 
 */
public class Daemon extends ActiveKnowledgeTask {

    private boolean debug = false;

    private boolean doAbort = false;

    protected static Logger logger = Logger.getLogger(Daemon.class);

    private boolean timer = false;

    private int interval = 1;

    private static ECARuleWindow win;

    /**
	 * Print timer information to console
	 * @param _timer boolean true = show / false = hide
	 */
    public void showTimer(boolean _timer) {
        timer = _timer;
    }

    /**
	 * set the loop interval for the eca daemon.
	 * @param _interval defines the query interval of the daemon in seconds	 
	 */
    public void setInterval(int _interval) {
        interval = _interval;
    }

    /**
	 * Returns the loop interval (in seconds)
	 */
    public int getInterval() {
        return interval;
    }

    /**
	 * Start the daemon loop
	 */
    public void run() {
        setUpDaemon();
        Calendar currentTime;
        long timeDiff = 0;
        akb = getActiveRules();
        if (akb == null) {
            System.out.println("No active rules found in ContractLog script - Execution finished");
            return;
        }
        akb.startSystemTime(SystemTime.onStartup);
        if (!debug) {
            currentTime = Calendar.getInstance();
            try {
                Thread.sleep(1010 - currentTime.get(Calendar.MILLISECOND));
            } catch (InterruptedException ex) {
            }
        } else {
            currentTime = new GregorianCalendar(2004, 1, 1, 0, 0, 0);
        }
        while (!doAbort) {
            try {
                currentTime = Calendar.getInstance();
                akb = getActiveRules();
                if (timer) {
                    Calendar timer = Calendar.getInstance();
                    System.out.println("");
                    System.out.println("----------------- time " + timer.get(Calendar.HOUR_OF_DAY) + ":" + timer.get(Calendar.MINUTE) + ":" + timer.get(Calendar.SECOND) + " (" + timer.get(Calendar.MILLISECOND) + ")" + " ----------------------");
                }
                for (Enumeration e = akb.getRuleEnumeration(); e.hasMoreElements(); ) {
                    ActiveRule rule = (ActiveRule) e.nextElement();
                    ResultSet rs = rule.getTime().evaluate(akb.getKnowledgeBaseWrapper());
                    boolean evaluate = false;
                    if (rs == null) evaluate = true; else try {
                        if (rs.first()) evaluate = true;
                    } catch (Exception ex) {
                    }
                    if (evaluate) {
                        sheduleEventConditionAction(rule, rs);
                    }
                }
                try {
                    if (debug) Thread.sleep(800); else {
                        Calendar actualTime = Calendar.getInstance();
                        timeDiff = actualTime.getTimeInMillis() - currentTime.getTimeInMillis();
                        System.out.println("Dur: " + timeDiff);
                        if (timeDiff > 1000 * interval) throw new Exception("The Validation of the ECA rules cost to much time."); else {
                            Thread.sleep((1010 - actualTime.get(Calendar.MILLISECOND)) + ((interval - 1) * 1000));
                        }
                    }
                } catch (InterruptedException ex) {
                    System.err.println(ex);
                }
            } catch (Exception ex) {
                akb.startSystemTime(SystemTime.onDelay);
                currentTime = Calendar.getInstance();
            }
        }
    }

    protected Calendar getCurrentTime() {
        return GregorianCalendar.getInstance();
    }

    public void abort() {
        doAbort = true;
    }

    private void sheduleEventConditionAction(ActiveRule rule, ResultSet rs) {
        EventConditionActionExecutor eventConditionAction = new EventConditionActionExecutor(akb, rule, rs);
        eventConditionAction.run();
    }

    KnowledgeBaseWrapper silentWrapper = null;

    public void setUpDaemon() {
        if (!rbsla.RBSLAStart.getRBSLAflag()) {
            KnowledgeBaseWrapper wrapper = akb.getKnowledgeBaseWrapper();
            ActiveKnowledgeBase akb = new ActiveKnowledgeBase(wrapper);
            ((ConsoleWriter) wrapper.getPrintWriter()).startReactive(true);
            if (silentWrapper == null) silentWrapper = wrapper.createChild();
            ConsoleWriter cw = (ConsoleWriter) silentWrapper.getPrintWriter();
            boolean oldcon = cw.consoleHidden();
            cw.showConsole(false);
            try {
                ResultSet rs = silentWrapper.solve("daemonInterval(X)");
                if ((rs != null) && (rs.first())) setInterval(((Integer) rs.getResult(Object.class, "X")).intValue());
                rs = silentWrapper.solve("timer(Flag)");
                if ((rs != null) && (rs.first())) showTimer(((Boolean) rs.getResult(Object.class, "Flag")).booleanValue());
                rs = silentWrapper.solve("console(Flag)");
                if ((rs != null) && (rs.first())) oldcon = (((Boolean) rs.getResult(Object.class, "Flag")).booleanValue());
            } catch (InferenceException ex) {
                System.err.println(ex);
                logger.warn("Exception " + ex.toString() + " Raised during start up reading ECA runtime settings from Prova script", ex);
            }
            cw.showConsole(oldcon);
        }
    }

    public ActiveKnowledgeBase getActiveRules() {
        KnowledgeBaseWrapper wrapper = akb.getKnowledgeBaseWrapper();
        ActiveKnowledgeBase akb = new ActiveKnowledgeBase(wrapper);
        if (silentWrapper == null) silentWrapper = wrapper.createChild();
        ConsoleWriter cw = (ConsoleWriter) silentWrapper.getPrintWriter();
        boolean oldcon = cw.consoleHidden();
        cw.showConsole(false);
        ResultSet rs = silentWrapper.solve("eca(TIME, EVENT, CONDITION, ACTION, POSTCONDITION, ELSE)");
        cw.showConsole(oldcon);
        try {
            if (rs == null) return null;
            boolean more = rs.first();
            if (!more) return null;
            int i = 0;
            while (more) {
                akb.addRule(new ActiveRule("ecaRule" + (i++ + 1), new ProvaTime(rs.getResult(Object.class, "TIME")), new ProvaEvent(rs.getResult(Object.class, "EVENT")), new ProvaCondition(rs.getResult(Object.class, "CONDITION")), new ProvaAction(rs.getResult(Object.class, "ACTION")), new ProvaCondition(rs.getResult(Object.class, "POSTCONDITION")), new ProvaAction(rs.getResult(Object.class, "ELSE"))));
                more = rs.next();
            }
        } catch (InferenceException ex) {
            System.err.println(ex);
            logger.warn("Exception " + ex.toString() + " Raised during inference: eca(TIME, EVENT, CONDITION, ACTION, POSTCOND, ELSE))", ex);
            return null;
        }
        return akb;
    }

    public static void setRuleWindow(ECARuleWindow window) {
        win = window;
    }
}
