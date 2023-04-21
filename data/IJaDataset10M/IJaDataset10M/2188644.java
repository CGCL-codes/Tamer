package net.assimilator.substrates.space;

import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.space.JavaSpace;
import net.assimilator.core.jsb.ServiceBeanContext;
import net.assimilator.watch.StopWatch;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The SpaceStationImpl class provides an implementation of a SpaceStation. A
 * SpaceStation is a Jini Service Bean (an implementation of
 * net.assimilator.core.jsb.ServiceBean) that processes SpaceChain entries written
 * to JavaSpace services.
 *
 * <p>The SpaceStationImpl creates creates SpaceWorker objects for each
 * associated JavaSpace.  The  SpaceStationWorker continually takes SpaceChain
 * entry objects from the JavaSpace, calls the execute() method on the
 * SpaceChain, receives the result, then posts the result back to the space.
 * In this manner creating a chain of commands.
 *
 * <p>The SpaceStationImpl provides an implementation that offers developers
 * the opportunity to develop JavaSpace entries that implement the SpaceChain
 * interface, deploy the SpaceStation as a ready-made utility prepared to
 * process SpaceChain entries that have been developed.
 *
 * <p>The SpaceStation provides the opportunity to use a user provided template
 * which extends from the SpaceChain entry. The initialization parameter is
 * provided as part of the <code>ServiceBeanContext</code> and is accessed by
 * the <code>templateClass</code> parameter. If this parameter is not provided,
 * default processing occurs (blocks on take with an Entry template set to
 * SpaceChain). Processing of this parameter is as follows:
 *
 * <ul>
 * <li> If the parameter "templateClass" is provided, the value must be a fully
 * qualified classname, useable with Class.forName(). The jar needed to load the
 * provided class must be accessible by the SpaceStation's classloader.
 * <li>Once the templateClass is loaded & instantiated, the SpaceStation will
 * verify that the object is an <code>instanceof</code> SpaceChain
 * <li> If the templateClass is provided and there are any Exceptions that
 * result from the loading of the class, or the instantiated Object is not of
 * the right type SpaceStation will throw an exception as part of its initialize
 * method and fail to initialize
 * </ul>
 *
 * <p>The SpaceStation additionally allows a the use of a WeakHashMap or a
 * HashMap to store information processed by SpaceChain entry objects as
 * follows:
 *
 * <ul>
 * <li> If the parameter "useWeakHashMap" is provided, allowed value are
 * "Yes" or "No". If "No" is provided, a <code>HashMap</code> will be created.
 * Otherwise a <code>WeakHashMap</code> will be used. The default is to use a
 * <code>WeakHashMap</code>.
 * </ul>
 */
public class SpaceStationImpl extends SpaceSubstrate implements SpaceStation {

    Entry template;

    boolean useWeakHashMap = true;

    StopWatch workWatcher = null;

    static final String COMPONENT = "net.assimilator.substrates.space";

    static final Logger logger = Logger.getLogger(COMPONENT);

    /**
     * Construct a SpaceStationImpl
     */
    public SpaceStationImpl() throws Exception {
        super();
    }

    /**
     * Override parents initialize method to proide custom initialization
     */
    public void initialize(ServiceBeanContext context) throws Exception {
        workWatcher = new StopWatch("entry.execute()");
        watchRegistry.register(workWatcher);
        logger.info("Using classloader: " + this.getClass().getClassLoader());
        template = (SpaceChain) context.getConfiguration().getEntry(COMPONENT, "templateClass", SpaceChain.class, new SpaceChain());
        logger.info("Template Class name = " + template.getClass().getName());
        String sUseWeakHashMap = (String) context.getInitParameter("useWeakHashMap");
        if ((sUseWeakHashMap != null) && sUseWeakHashMap.equals("No")) {
            useWeakHashMap = false;
        }
        super.initialize(context);
    }

    /**
     * Override parent's destroy method to make sure our watches are shutdown
     */
    public void destroy() {
        watchRegistry.deregister(workWatcher);
        super.destroy();
    }

    /**
     * This method implements the abstract <code>createWorker</code> method. This
     * implementation creates a dedicated SpaceStationWorker object which
     * processes take requests on the input JavaSpace.
     *
     * <p>The SpaceStationWorker continually takes SpaceChain entry objects
     * from the JavaSpace, calls the execute() method on the SpaceChain,
     * receives the result, then posts the result back to the space. In this
     * manner creating a chain of commands.
     */
    public SpaceWorker createWorker(JavaSpace space) throws Exception {
        return (new SpaceStationWorker(space));
    }

    private String getStackTraceAsString(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        StringBuffer error = stringWriter.getBuffer();
        return error.toString();
    }

    /**
     * Initialize the Map
     *
     * @param map - The Map the initialize
     */
    private void initMap(Map map) {
        for (Iterator it = context.getInitParameterNames(); it.hasNext(); ) {
            String key = (String) it.next();
            Object obj = context.getInitParameter(key);
            if (obj != null) {
                map.put(key, obj);
            }
        }
    }

    /**
     * The SpaceStationWorker provides an implementation of a SpaceWorker
     * that repeatedly takes SpaceChain objects from a JavaSpace and invokes
     * the <code>SpaceChain.execute</code> method. SpaceStationWorker semantics
     * are defined in such a way that after the
     * <code>SpaceChain.execute()</code> method is invoked, the result are
     * posted back to the space.
     */
    protected class SpaceStationWorker implements SpaceWorker {

        protected JavaSpace space;

        int id;

        boolean keepAlive = true;

        Thread thread;

        protected SpaceStationWorker(JavaSpace space) {
            this.space = space;
        }

        public void setID(int id) {
            this.id = id;
        }

        public void shutdown() throws InterruptedException {
            keepAlive = false;
            thread.interrupt();
        }

        public void run() {
            thread = Thread.currentThread();
            logger.info("!!!! Started SpaceStationWorker [" + id + "]");
            Map map;
            if (useWeakHashMap) {
                map = new WeakHashMap();
                if (logger.isLoggable(Level.FINE)) logger.fine("SpaceStationWorker [" + id + "] using WeakHashMap");
            } else {
                map = new HashMap();
                if (logger.isLoggable(Level.FINE)) logger.fine("SpaceStationWorker [" + id + "] using HashMap");
            }
            initMap(map);
            while (!thread.isInterrupted() && keepAlive) {
                try {
                    logger.info("template classloader: " + new SpaceChain().getClass().getClassLoader());
                    Entry entry = space.take(template, null, Long.MAX_VALUE);
                    if (entry == null) {
                        continue;
                    }
                    Class command = entry.getClass();
                    workWatcher.startTiming();
                    Entry result = executeEntry(command, entry, map);
                    workWatcher.stopTiming();
                    Method responseLeaseTime = command.getMethod("responseLeaseTime", (Class[]) null);
                    Long leaseTime = (Long) responseLeaseTime.invoke(entry, (Object[]) null);
                    if (result != null) {
                        space.write(result, null, leaseTime.longValue());
                    }
                } catch (IOException ex) {
                    if (logger.isLoggable(Level.FINEST)) logger.log(Level.WARNING, "Problem communicating with the Space: " + "Stopping Worker [" + id + "]", ex); else logger.log(Level.WARNING, "Problem communicating with the Space: " + "Stopping Worker [" + id + "]");
                    return;
                } catch (NullPointerException npe) {
                    logger.log(Level.WARNING, "Processing Entry", npe);
                } catch (InterruptedException ignore) {
                } catch (UnusableEntryException ue) {
                    if (logger.isLoggable(Level.FINEST)) {
                        StringBuffer buffer = new StringBuffer();
                        if (ue.unusableFields != null) {
                            for (int i = 0; i < ue.unusableFields.length; i++) {
                                if (i > 0) buffer.append(", ");
                                buffer.append("unusable field " + "[" + ue.unusableFields[i] + "]");
                            }
                        }
                        if (ue.nestedExceptions != null) {
                            for (int i = 0; i < ue.nestedExceptions.length; i++) {
                                buffer.append("\n");
                                buffer.append(getStackTraceAsString(ue.nestedExceptions[i]));
                            }
                        }
                        logger.finest(buffer.toString());
                    }
                } catch (Exception e) {
                    logger.info("Exception occurred while using " + this.getClass().getClassLoader());
                    logger.log(Level.WARNING, "Processing Entry", e);
                } catch (Error e) {
                    logger.info("Exception occurred while using " + this.getClass().getClassLoader());
                    logger.log(Level.WARNING, "Processing Entry", e);
                    throw e;
                }
            }
            logger.info("SpaceStationWorker [" + id + "] leaving");
        }

        protected Entry executeEntry(Class command, Entry entry, Map map) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            Method execute = command.getMethod("execute", new Class[] { Map.class });
            return (Entry) execute.invoke(entry, new Object[] { map });
        }
    }
}
