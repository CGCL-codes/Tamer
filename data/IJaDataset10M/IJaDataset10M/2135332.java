package gov.nasa.jpf.search;

import java.io.FileWriter;
import java.io.IOException;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPFException;
import gov.nasa.jpf.VM;
import gov.nasa.jpf.Search;
import gov.nasa.jpf.SearchListener;
import gov.nasa.jpf.SearchListenerMulticaster;
import gov.nasa.jpf.SearchState;
import gov.nasa.jpf.State;
import gov.nasa.jpf.Error;
import gov.nasa.jpf.ErrorList;
import gov.nasa.jpf.Path;
import gov.nasa.jpf.Property;
import gov.nasa.jpf.util.Debug;
import gov.nasa.jpf.Transition;
import gov.nasa.jpf.util.DynamicIntArray;

/**
 * the mother of all search classes. Mostly takes care of listeners, keeping
 * track of state attributes and errors. This class mainly keeps the
 * general search info like depth, configured properties etc.
 */
public abstract class AbstractSearch implements Search {

    protected ErrorList errors = new ErrorList();

    protected int depth = 0;

    protected VM vm;

    protected Property property;

    protected boolean isEndState = false;

    protected boolean isNewState = true;

    protected boolean matchDepth;

    protected long minFreeMemory;

    protected int depthLimit;

    protected String lastSearchConstraint;

    protected boolean done = false;

    protected boolean doBacktrack = false;

    SearchListener listener;

    Config config;

    /** (on demand) storage to keep track of state depths */
    DynamicIntArray stateDepth;

    protected AbstractSearch(Config config, VM vm) {
        this.vm = vm;
        this.config = config;
        depthLimit = config.getInt("search.depth_limit", -1);
        matchDepth = config.getBoolean("search.match_depth");
        minFreeMemory = config.getMemorySize("search.min_free", 1024 << 10);
        try {
            property = getProperties(config);
            if (property == null) {
                Debug.println(Debug.ERROR, "no property");
            }
            addListeners(config);
        } catch (Throwable t) {
            Debug.println(Debug.ERROR, "Search initialization failed: " + t);
        }
    }

    public void addProperty(Property newProperty) {
        property = PropertyMulticaster.add(property, newProperty);
    }

    public void removeProperty(Property oldProperty) {
        property = PropertyMulticaster.remove(property, oldProperty);
    }

    /**
   * return set of configured properties
   * note there is a nameclash here - JPF 'properties' have nothing to do with
   * Java properties (java.util.Properties)
   */
    protected Property getProperties(Config config) throws Config.Exception {
        Property props = null;
        Object[] a = config.getInstances("search.properties", Property.class);
        if (a != null) {
            for (int i = 0; i < a.length; i++) {
                props = PropertyMulticaster.add(props, (Property) a[i]);
            }
        }
        return props;
    }

    protected boolean hasPropertyTermination() {
        if (isPropertyViolated()) {
            if (done) {
                return true;
            }
        }
        return false;
    }

    boolean isPropertyViolated() {
        if ((property != null) && !property.check(this, vm)) {
            error(property, vm.getPath());
            return true;
        }
        return false;
    }

    void addListeners(Config config) throws Config.Exception {
        Object[] listeners = config.getInstances("search.listener", SearchListener.class);
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                addListener((SearchListener) listeners[i]);
            }
        }
    }

    public void addListener(SearchListener newListener) {
        listener = SearchListenerMulticaster.add(listener, newListener);
    }

    public void removeListener(SearchListener removeListener) {
        listener = SearchListenerMulticaster.remove(listener, removeListener);
    }

    public ErrorList getErrors() {
        return errors;
    }

    public VM getVM() {
        return vm;
    }

    public boolean isEndState() {
        return isEndState;
    }

    public boolean hasNextState() {
        return !isEndState();
    }

    public boolean isNewState() {
        boolean isNew = vm.isNewState();
        if (matchDepth) {
            int id = vm.getStateId();
            if (isNew) {
                setStateDepth(id, depth);
            } else {
                if (depth >= getStateDepth(id)) {
                    return false;
                }
            }
        }
        return isNew;
    }

    public boolean isVisitedState() {
        return !isNewState();
    }

    public int getSearchDepth() {
        return depth;
    }

    public String getSearchConstraint() {
        return lastSearchConstraint;
    }

    public Transition getTransition() {
        return vm.getLastTransition();
    }

    public int getStateNumber() {
        return vm.getStateId();
    }

    public boolean requestBacktrack() {
        return false;
    }

    public boolean supportsBacktrack() {
        return false;
    }

    public boolean supportsRestoreState() {
        return false;
    }

    protected int getMaxSearchDepth() {
        int searchDepth = Integer.MAX_VALUE;
        if (depthLimit > 0) {
            int initialDepth = vm.getPathLength();
            if ((Integer.MAX_VALUE - initialDepth) > depthLimit) {
                searchDepth = depthLimit + initialDepth;
            }
        }
        return searchDepth;
    }

    public int getDepthLimit() {
        return depthLimit;
    }

    protected SearchState getSearchState() {
        return new AbstractSearchState();
    }

    protected void error(Property property, Path path) {
        Error error = new Error(property, path);
        if (config.getBoolean("search.print_errors")) {
            Debug.println(Debug.ERROR, error);
        } else {
            Debug.println(Debug.ERROR, "\tFound error #" + errors.size());
        }
        String fname = config.getString("search.error_path");
        boolean getAllErrors = config.getBoolean("search.multiple_errors");
        if (fname != null) {
            if (getAllErrors) {
                int i = fname.lastIndexOf('.');
                if (i >= 0) {
                    fname = fname.substring(0, i) + '-' + errors.size() + fname.substring(i);
                }
            }
            savePath(path, fname);
        }
        errors.addError(error);
        done = !getAllErrors;
        notifyPropertyViolated();
    }

    public void savePath(Path path, String fname) {
        try {
            FileWriter w = new FileWriter(fname);
            vm.savePath(path, w);
            w.close();
        } catch (IOException e) {
            Debug.println(Debug.ERROR, "Failed to saved trace: " + fname);
        }
    }

    protected void notifyStateAdvanced() {
        if (listener != null) {
            listener.stateAdvanced(this);
        }
    }

    protected void notifyStateProcessed() {
        if (listener != null) {
            listener.stateProcessed(this);
        }
    }

    protected void notifyStateRestored() {
        if (listener != null) {
            listener.stateRestored(this);
        }
    }

    protected void notifyStateBacktracked() {
        if (listener != null) {
            listener.stateBacktracked(this);
        }
    }

    protected void notifyPropertyViolated() {
        if (listener != null) {
            listener.propertyViolated(this);
        }
    }

    protected void notifySearchStarted() {
        if (listener != null) {
            listener.searchStarted(this);
        }
    }

    protected void notifySearchConstraintHit(String constraintId) {
        if (listener != null) {
            lastSearchConstraint = constraintId;
            listener.searchConstraintHit(this);
        }
    }

    protected void notifySearchFinished() {
        if (listener != null) {
            listener.searchFinished(this);
        }
    }

    protected boolean forward() {
        boolean ret = vm.forward();
        if (ret) {
            isNewState = vm.isNewState();
        } else {
            isNewState = false;
        }
        isEndState = vm.isEndState();
        return ret;
    }

    protected boolean backtrack() {
        isNewState = false;
        isEndState = false;
        return vm.backtrack();
    }

    protected void restoreState(State state) {
    }

    void setStateDepth(int stateId, int depth) {
        if (stateDepth == null) {
            stateDepth = new DynamicIntArray(1024);
        }
        stateDepth.set(stateId, depth);
    }

    int getStateDepth(int stateId) {
        try {
            return stateDepth.get(stateId);
        } catch (Throwable x) {
            throw new JPFException("failed to determine state depth: " + x);
        }
    }

    /**
   * check if we have a minimum amount of free memory left. If not, we rather want to stop in time
   * (with a threshold amount left) so that we can report something useful, and not just die silently
   * with a OutOfMemoryError (which isn't handled too gracefully by most VMs)
   */
    boolean checkStateSpaceLimit() {
        Runtime rt = Runtime.getRuntime();
        long avail = rt.freeMemory();
        if (avail < minFreeMemory) {
            rt.gc();
            avail = rt.freeMemory();
            if (avail < minFreeMemory) {
                return false;
            }
        }
        return true;
    }

    /**
   * DOCUMENT ME!
   */
    class AbstractSearchState implements SearchState {

        int depth;

        AbstractSearchState() {
            depth = AbstractSearch.this.depth;
        }

        public int getSearchDepth() {
            return depth;
        }
    }
}
