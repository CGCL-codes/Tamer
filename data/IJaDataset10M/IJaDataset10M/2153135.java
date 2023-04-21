package org.form4j.form.model.conditions;

import java.awt.FocusTraversalPolicy;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.form4j.form.border.FormBorder;
import org.form4j.form.field.Field;
import org.form4j.form.model.Model;
import org.form4j.form.model.ModelConditionEvent;
import org.form4j.form.model.ModelConditionListener;
import org.form4j.form.traversal.DeclarationOrderFocusTraversalPolicy;
import org.form4j.form.util.StringUtil;
import org.form4j.form.util.xml.XMLPrintHelper;

/**
 * Implements condition handling for form4j model.
 *
 * @author $Author: cjuon $
 * @version 0.2 $Revision: 1.11 $ $Date: 2008/06/11 02:37:36 $
 **/
public class ConditionManagerImpl implements ConditionManager {

    /**
     * Construct manager to handle conditions for form4j dom model.
     * @param xmlModel the underlying model
     */
    public ConditionManagerImpl(final Model xmlModel) {
        this.model = xmlModel;
        conditionEvaluator = new DirtyAndXPathConditionEvaluator(xmlModel);
    }

    /** register a ModelConditionListener. Gets fired whenever the existing
      * DOM Data document changes (e.g. through setDataDocument()) AND the associated xpath predicate has changed.
      * @param listener the listener to fire for model condition change events
     **/
    public final void addModelConditionListener(final ModelConditionListener listener) {
        if (LOG.isDebugEnabled()) LOG.debug("listener " + listener + " xpath " + (listener != null ? listener.getConditionXPathPredicate() : "NULL"));
        String xpathPredicate = listener.getConditionXPathPredicate();
        Vector listeners = (Vector) conditionListeners.get(xpathPredicate);
        if (listeners == null) {
            listeners = new Vector();
            conditionListeners.put(xpathPredicate, listeners);
        }
        listeners.remove(listener);
        listeners.addElement(listener);
    }

    /**
     * remove a previously registered global ModelConditionListener.
     * @param listener the listener to remove
     **/
    public final void removeModelConditionListener(final ModelConditionListener listener) {
        final Vector listeners = (Vector) conditionListeners.get(listener.getConditionXPathPredicate());
        if (listeners != null) listeners.remove(listener);
    }

    /**
     * Evaluate an xpath condition immediately.
     * @param xpathPredicate the xpath predicate for the condition
     * @return true if the xpathPredicate returns non-null node, false otherwise
     */
    public final Boolean evaluateCondition(final String xpathPredicate) {
        long t = 0L;
        if (recording) t = System.currentTimeMillis();
        Boolean value = conditionEvaluator.evaluate(xpathPredicate);
        if (recording) {
            t = (System.currentTimeMillis() - t);
            recordDelay(xpathPredicate, (int) t);
        }
        return value;
    }

    /**
     * Reset cached xpath condition.
     * @param xpathPredicate the condition's xpath predicate
     */
    public void resetCondition(final String xpathPredicate) {
        currentConditions.remove(xpathPredicate);
    }

    /**
     * evaluate all conditions and fire associated condition listeners if changed .
     */
    public final void reEvaluateConditions() {
        boolean relaxed = false;
        final Hashtable relaxedListeners = new Hashtable();
        while (!relaxed) {
            relaxed = true;
            final Enumeration en = conditionListeners.keys();
            while (en.hasMoreElements()) {
                final String xpathPredicate = (String) en.nextElement();
                final Vector listeners = (Vector) conditionListeners.get(xpathPredicate);
                if (listeners != null && relaxedListeners.get(relaxedListenersKey(listeners)) == null) relaxed = evaluateUnrelaxedCondition(relaxedListeners, xpathPredicate, listeners);
            }
        }
    }

    /**
     * reset any caches associated with condition managers.
     */
    public final void reset() {
        synchronized (this) {
            currentConditions = new Hashtable();
        }
    }

    /**
     * @return true if any conditions are to be handled.
     */
    public final boolean hasConditions() {
        return (conditionListeners.size() > 0);
    }

    /**
     * start recording of condition firing
     */
    public void startConditionRecording() {
        recording = true;
        stat = new StringBuffer();
        statChangeTable = new Hashtable();
        statRelaxedTable = new Hashtable();
        statDelayTable = new Hashtable();
        statFiredTable = new Hashtable();
    }

    /**
     * Stop condition firing recording and return statistics
     * @return statistics
     */
    public String stopConditionRecording() {
        recording = false;
        Vector delays = new Vector();
        for (Enumeration keys = statDelayTable.keys(); keys.hasMoreElements(); ) {
            String key = (String) keys.nextElement();
            int delay = ((Integer) statDelayTable.get(key)).intValue();
            delays.addElement(new DelayElement(delay, key));
        }
        Collections.sort(delays);
        StringBuffer statistics = new StringBuffer();
        for (int i = 0; i < delays.size(); i++) {
            DelayElement d = (DelayElement) delays.elementAt(i);
            String key = d.getKey();
            int delay = d.getDelay();
            int firings = 0;
            try {
                firings = ((Integer) statFiredTable.get(key)).intValue();
            } catch (Exception e) {
            }
            int evals = 0;
            try {
                evals = ((Integer) statChangeTable.get(key)).intValue();
            } catch (Exception e) {
            }
            statistics.append(StringUtil.pad("" + delay, 5) + "msec, evals=" + evals + ", fired=" + firings + " --> " + key + "\n");
        }
        return statistics.toString();
    }

    private static class DelayElement implements Comparable {

        public int compareTo(Object arg0) {
            DelayElement other = (DelayElement) arg0;
            if (other.getDelay() == getDelay()) return 0;
            if (getDelay() < other.getDelay()) return 1; else return -1;
        }

        private int delay;

        private String key;

        public DelayElement(int delay, String key) {
            this.key = key;
            this.delay = delay;
        }

        public int getDelay() {
            return delay;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    private void recordChanges(final String xpathPredicate) {
        if (recording) {
            int cnt = 0;
            try {
                cnt = ((Integer) statChangeTable.get(xpathPredicate)).intValue();
            } catch (Exception e) {
            }
            cnt++;
            statChangeTable.put(xpathPredicate, new Integer(cnt));
        }
    }

    private void recordFiring(final String xpathPredicate, long t, ModelConditionListener listener) {
        if (recording) {
            long deltaT = (System.currentTimeMillis() - t);
            int cnt = 0;
            try {
                cnt = ((Integer) statFiredTable.get(xpathPredicate)).intValue();
            } catch (Exception e) {
            }
            cnt++;
            statFiredTable.put(xpathPredicate, new Integer(cnt));
            if (deltaT > 10) {
                LOG.debug("Condition name " + listener.getConditionName());
                String fieldInfo = "";
                try {
                    fieldInfo = XMLPrintHelper.xmlDocumentToString(((Field) listener).getFieldDescriptor());
                } catch (Exception e) {
                }
                LOG.debug("FIRING " + deltaT + "  " + listener + "(" + fieldInfo + ") " + xpathPredicate);
            }
        }
    }

    private void recordRelaxations(final String xpathPredicate) {
        if (recording) {
            int cnt = 0;
            try {
                cnt = ((Integer) statRelaxedTable.get(xpathPredicate)).intValue();
            } catch (Exception e) {
            }
            cnt++;
            statRelaxedTable.put(xpathPredicate, new Integer(cnt));
        }
    }

    private void recordDelay(final String xpathPredicate, int delay) {
        if (recording) {
            int cnt = 0;
            try {
                cnt = ((Integer) statDelayTable.get(xpathPredicate)).intValue();
            } catch (Exception e) {
            }
            cnt += delay;
            statDelayTable.put(xpathPredicate, new Integer(cnt));
        }
    }

    private static final String TEST_XPATH = "/bean/Abgang/bean/Zertifikat/bean and (/bean[normalize-space(PageBuchung/text()) = '1']) and (/bean[@page != '1'])";

    private void processConditionChange(final String xpathPredicate, final Vector listeners, final Boolean currentValue, final Boolean newValue) {
        currentConditions.put(xpathPredicate, newValue);
        final ModelConditionEvent evt = new ModelConditionEvent(this, xpathPredicate, currentValue, newValue);
        final Vector condListeners = (Vector) listeners.clone();
        for (int i = 0; i < condListeners.size(); i++) {
            LOG.debug("Condition FIRE[" + xpathPredicate + "," + i + ",]  " + condListeners.elementAt(i));
            long t = System.currentTimeMillis();
            ((ModelConditionListener) condListeners.elementAt(i)).conditionChange(evt);
            recordFiring(xpathPredicate, t, (ModelConditionListener) condListeners.elementAt(i));
        }
    }

    private boolean evaluateUnrelaxedCondition(final Hashtable relaxedListeners, final String xpathPredicate, final Vector listeners) {
        boolean relaxed = true;
        final Boolean currentValue = (Boolean) currentConditions.get(xpathPredicate);
        final Boolean newValue = evaluateCondition(xpathPredicate);
        if (LOG.isDebugEnabled()) LOG.debug("CONDITION CHECK(" + xpathPredicate + ") old: " + currentValue + ", new: " + newValue + ", changed: " + (!newValue.equals(currentValue)));
        if (!newValue.equals(currentValue)) {
            relaxed = false;
            recordChanges(xpathPredicate);
            long t = System.currentTimeMillis();
            processConditionChange(xpathPredicate, listeners, currentValue, newValue);
            recordDelay(xpathPredicate, (int) (System.currentTimeMillis() - t));
        } else {
            if (listeners != null) {
                recordRelaxations(xpathPredicate);
                relaxedListeners.put(relaxedListenersKey(listeners), "");
            }
        }
        return relaxed;
    }

    private Object relaxedListenersKey(Vector listeners) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < listeners.size(); i++) {
            Object o = listeners.elementAt(i);
            if (o instanceof ModelConditionListener) {
                if (sb.length() > 0) sb.append(",");
                sb.append(((ModelConditionListener) o).getConditionName());
            } else sb.append(o);
        }
        return sb.toString();
    }

    private static final Logger LOG = Logger.getLogger(ConditionManagerImpl.class.getName());

    private final Model model;

    private Hashtable conditionListeners = new Hashtable();

    private Hashtable currentConditions = new Hashtable();

    private ConditionEvaluator conditionEvaluator = null;

    private StringBuffer stat = new StringBuffer();

    private boolean recording = false;

    private Hashtable statChangeTable = new Hashtable();

    private Hashtable statRelaxedTable = new Hashtable();

    private Hashtable statDelayTable = new Hashtable();

    private Hashtable statFiredTable = new Hashtable();
}
