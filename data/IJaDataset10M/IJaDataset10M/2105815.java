package net.sourceforge.processdash.data.repository;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;
import net.sourceforge.processdash.data.ImmutableStringData;
import net.sourceforge.processdash.data.MalformedValueException;
import net.sourceforge.processdash.data.SaveableData;
import net.sourceforge.processdash.data.SimpleData;
import net.sourceforge.processdash.data.compiler.CompiledScript;
import net.sourceforge.processdash.data.compiler.Compiler;
import net.sourceforge.processdash.data.compiler.ExecutionException;
import net.sourceforge.processdash.data.compiler.ListStack;
import net.sourceforge.processdash.util.LightweightSet;
import net.sourceforge.processdash.util.LightweightSynchronizedSet;

public class CompiledFunction implements SaveableData, AliasedData, DataListener {

    public static final String ANONYMOUS_EDITABLE_ALIAS = "";

    protected static final SimpleData VALUE_NEVER_QUERIED = new ImmutableStringData("", false, false);

    /** The name of the data element we are calculating */
    protected String name = null;

    /** The data name prefix for performing calculations */
    protected String prefix;

    /** The script to run to perform the calculation */
    protected CompiledScript script;

    /** The data repository */
    protected DataRepository data;

    /** The value we obtained when we ran our calculation last. */
    protected SimpleData value = VALUE_NEVER_QUERIED;

    /** Track the aliased name of our final value.
     * {@see net.sourceforge.processdash.data.repository.AliasedData } */
    protected String aliasFor;

    /** A list of the names of all data elements in the repository that we
     * are currently listening to. */
    protected LightweightSet currentSubscriptions = new LightweightSynchronizedSet();

    /** A mutex that helps us keep track of the need to recalculate */
    protected DirtyTracker extChanges = new DirtyTracker();

    public CompiledFunction(String name, String script, DataRepository r, String prefix) throws MalformedValueException {
        try {
            if (script.charAt(0) == '{') script = script.substring(1);
            this.script = Compiler.compile(script);
            this.data = r;
            this.name = name;
            this.prefix = prefix;
        } catch (Exception ce) {
            throw new MalformedValueException();
        }
    }

    public CompiledFunction(String name, CompiledScript script, DataRepository r, String prefix) {
        this.script = script;
        this.data = r;
        this.name = name;
        this.prefix = prefix;
    }

    public CompiledScript getScript() {
        return script;
    }

    protected void recalc() {
        if (isDisposed()) return;
        Set calcNameSet = (Set) CURRENTLY_CALCULATING.get();
        if (calcNameSet.contains(name)) {
            logger.warning("Encountered recursively defined data " + "when calculating " + name + " - ABORTING");
            return;
        }
        SimpleData oldValue = value;
        SimpleData newValue = null;
        String newAlias = null;
        SubscribingExpressionContext context = null;
        int retryCount = 10;
        while (retryCount-- > 0 && extChanges.isDirty()) {
            context = new SubscribingExpressionContext(data, prefix, this, name, currentSubscriptions);
            ListStack stack = new ListStack();
            int changeCount = -1;
            try {
                calcNameSet.add(name);
                changeCount = extChanges.getUnhandledChangeCount();
                script.run(stack, context);
                newAlias = (String) stack.peekDescriptor();
                newValue = (SimpleData) stack.pop();
                if (newValue != null && newAlias == null) newValue = (SimpleData) newValue.getEditable(false);
            } catch (ExecutionException e) {
                logger.warning("Error executing " + name + ": " + e);
                newValue = null;
            } finally {
                calcNameSet.remove(name);
            }
            if (extChanges.maybeClearDirty(changeCount, newValue, newAlias)) break; else if (retryCount > 0) logger.finer("Retrying calculating " + name);
        }
        if (context == null) return;
        if (retryCount <= 0) logger.warning("Ran out of retries while calculating " + name);
        context.removeOldSubscriptions();
        currentSubscriptions.trimToSize();
        if (oldValue != VALUE_NEVER_QUERIED && !eq(oldValue, value)) data.valueRecalculated(name, this);
    }

    private boolean eq(SimpleData a, SimpleData b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        return (a.isEditable() == b.isEditable() && a.isDefined() == b.isDefined() && a.equals(b));
    }

    public void dataValueChanged(DataEvent e) {
        setDirty();
    }

    public void dataValuesChanged(Vector v) {
        setDirty();
    }

    private void setDirty() {
        if (isDisposed() == false) {
            extChanges.setDirty();
            data.valueRecalculated(name, this);
        }
    }

    public boolean isEditable() {
        return false;
    }

    public void setEditable(boolean e) {
    }

    public boolean isDefined() {
        return true;
    }

    public void setDefined(boolean d) {
    }

    public String saveString() {
        return script.saveString();
    }

    public String getAliasedDataName() {
        if (isDisposed() == false && extChanges.isDirty()) recalc();
        return aliasFor;
    }

    public SimpleData getSimpleValue() {
        if (isDisposed() == false && extChanges.isDirty()) recalc();
        return value;
    }

    protected boolean isDisposed() {
        return name == null;
    }

    public void dispose() {
        name = prefix = aliasFor = null;
        script = null;
        value = null;
        extChanges = null;
        if (currentSubscriptions != null) {
            if (data.deferDeletions) data.deleteDataListener(this); else try {
                synchronized (currentSubscriptions) {
                    for (Iterator i = currentSubscriptions.iterator(); i.hasNext(); ) {
                        String s = (String) i.next();
                        data.removeDataListener(s, this);
                    }
                    currentSubscriptions.clear();
                }
            } catch (NullPointerException npe) {
            }
            currentSubscriptions = null;
        }
        data = null;
    }

    public SaveableData getEditable(boolean editable) {
        return this;
    }

    /** Class for tracking the need to recalculate.
     * 
     * This class tracks the number of external change notifications received.
     * Before a calculation, this value is queried.  If the calculation 
     * finishes and this count hasn't changed, then the calculation is 
     * considered a success.  If the count <b>did</b> change during the
     * calculation, it means that we received an external change notification 
     * while calculating. Thus, the calculated value is suspect and should be
     * recomputed.
     * 
     * As a side note, this work is performed by this object instead of by
     * the CompiledFunction object, because we want to avoid holding 
     * synchronization locks on the CompiledFunction object whenever possible.
     * (This helps avoid potential deadlocks within the DataRepository.)
     */
    protected class DirtyTracker {

        private int unhandledChangeCount = 1;

        public synchronized void setDirty() {
            unhandledChangeCount++;
        }

        public synchronized boolean isDirty() {
            return unhandledChangeCount > 0;
        }

        public synchronized int getUnhandledChangeCount() {
            return unhandledChangeCount;
        }

        public synchronized boolean maybeClearDirty(int newHandledCount, SimpleData newValue, String newAlias) {
            if (newHandledCount == unhandledChangeCount) {
                value = newValue;
                if (newAlias == ANONYMOUS_EDITABLE_ALIAS) aliasFor = null; else aliasFor = newAlias;
                unhandledChangeCount = 0;
                return true;
            } else {
                return false;
            }
        }
    }

    private static final Logger logger = Logger.getLogger(CompiledFunction.class.getName());

    /** A list of the data names that are currently being calculated by this
     * thread (to prevent infinite circular recursion)
     */
    protected static ThreadLocal CURRENTLY_CALCULATING = new ThreadLocal() {

        protected Object initialValue() {
            return new HashSet();
        }
    };
}
