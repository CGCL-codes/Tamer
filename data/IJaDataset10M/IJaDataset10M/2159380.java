package org.jucetice.javascript.classes;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.jucetice.javascript.ScriptEngine;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptableTimer extends ScriptableObject {

    static final String CLASSNAME = "Timer";

    Timer timer = null;

    /**
	 * 
	 */
    public ScriptableTimer() {
        timer = new Timer();
    }

    /**
	 * 
	 * @param cx
	 * @param args
	 * @param ctorObj
	 * @param inNewExpr
	 * @return
	 */
    public static Scriptable jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr) {
        return new ScriptableTimer();
    }

    /**
     * 
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     */
    public static void jsFunction_setTimeout(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        if (args.length < 2) throw new IllegalArgumentException("Illegal number of arguments in setTimeout call !");
        int numberOfMsInTheFuture = (int) Context.toNumber(args[1]);
        Date timeToRun = new Date(System.currentTimeMillis() + numberOfMsInTheFuture);
        ScriptableTimer timer = (ScriptableTimer) thisObj;
        ScriptEngine engine = (ScriptEngine) cx.getThreadLocal("engine");
        if (args[0] instanceof Function) {
            Function func = (Function) args[0];
            Scriptable scope = thisObj;
            timer.purge();
            timer.setTimeout(new ScriptableTimer.FutureFunctionCallTimerTask(cx, engine, scope, timer, func), timeToRun);
        }
    }

    /**
     * 
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     */
    public static void jsFunction_setInterval(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        if (args.length < 2) throw new IllegalArgumentException("Illegal number of arguments in setInterval call !");
        int numberOfMsRepeat = (int) Context.toNumber(args[1]);
        int numberOfMsDelay = args.length == 3 ? (int) Context.toNumber(args[2]) : numberOfMsRepeat;
        ScriptableTimer timer = (ScriptableTimer) thisObj;
        ScriptEngine engine = (ScriptEngine) cx.getThreadLocal("engine");
        if (args[0] instanceof Function) {
            Function func = (Function) args[0];
            Scriptable scope = thisObj;
            timer.purge();
            timer.setInterval(new ScriptableTimer.FutureFunctionCallTimerTask(cx, engine, scope, timer, func), numberOfMsDelay, numberOfMsRepeat);
        }
    }

    /**
     * 
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     */
    public static void jsFunction_cancel(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        ScriptableTimer timer = (ScriptableTimer) thisObj;
        timer.cancel();
        timer.purge();
    }

    /**
     * 
     * @author kororaa
     *
     */
    static class FutureFunctionCallTimerTask extends TimerTask {

        Context cx;

        ScriptEngine engine;

        Scriptable scope;

        ScriptableObject timer;

        Function func;

        FutureFunctionCallTimerTask(Context newContext, ScriptEngine newEngine, Scriptable newScope, ScriptableObject newTimer, Function newFunc) {
            this.cx = newContext;
            this.engine = newEngine;
            this.scope = newScope;
            this.timer = newTimer;
            this.func = newFunc;
        }

        public void run() {
            cx.putThreadLocal("engine", engine);
            func.call(cx, scope, timer, Context.emptyArgs);
        }
    }

    /**
     * 
     * @param task
     * @param timeToRun
     */
    protected void setInterval(TimerTask task, int delay, int period) {
        timer.scheduleAtFixedRate(task, delay, period);
    }

    /**
     * 
     * @param task
     * @param timeToRun
     */
    protected void setTimeout(TimerTask task, Date timeToRun) {
        timer.schedule(task, timeToRun);
    }

    /**
     * 
     * @param task
     * @param timeToRun
     */
    protected void cancel() {
        timer.cancel();
    }

    /**
     * 
     * @param task
     * @param timeToRun
     */
    protected void purge() {
        timer.purge();
    }

    /**
     * 
     */
    public String getClassName() {
        return CLASSNAME;
    }
}
