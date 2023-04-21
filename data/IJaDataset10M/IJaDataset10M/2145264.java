package apapl.deliberation;

import apapl.*;
import java.util.*;

/**
 * This class implements a specific deliberation cycle of a module.
 */
public class Deliberation implements Cloneable {

    private ArrayList<ModuleChangeListener> listeners;

    private ArrayList<DeliberationStep> cycle;

    private ListIterator<DeliberationStep> current;

    private boolean changed = false;

    /**
	 * Constructs the deliberation cycle.
	 */
    public Deliberation() {
        listeners = new ArrayList<ModuleChangeListener>();
        cycle = new ArrayList<DeliberationStep>();
        cycle.add(new ApplyPGrules());
        cycle.add(new TestStoppingCond());
        cycle.add(new ExecuteAllPlans());
        cycle.add(new ProcessIEvents());
        cycle.add(new ProcessMessages());
        cycle.add(new ProcessEEvents());
        current = cycle.listIterator();
    }

    /**
	 * Constructs the deliberation cycle based on given fields.
	 */
    public Deliberation(ArrayList<ModuleChangeListener> listeners, ArrayList<DeliberationStep> cycle, ListIterator<DeliberationStep> current) {
        super();
        this.listeners = new ArrayList<ModuleChangeListener>();
        this.cycle = new ArrayList<DeliberationStep>(cycle);
        this.current = this.cycle.listIterator();
        while (this.current.nextIndex() != current.nextIndex()) this.current.next();
    }

    /**
	 * Performs one step of the deliberation cycle on the given module.
	 * 
	 * @param module the module to perform the step on
	 **/
    public synchronized void step(APLModule module) {
        DeliberationResult result;
        DeliberationStep currentStep = current.next();
        synchronized (module) {
            result = currentStep.execute(module);
        }
        changed = changed || result.moduleChanged();
        notifyListeners(module, result);
        if (!current.hasNext()) {
            current = cycle.listIterator();
            if (!changed) {
                module.sleep();
            }
            changed = false;
        }
    }

    /**
	 * Sets the module change listener. Each time a deliberation step has been
	 * performed the module is notified of a change of its mental state.
	 * 
	 * @param listener the object interested in the change
	 */
    public synchronized void addModuleChangeListener(ModuleChangeListener listener) {
        listeners.add(listener);
    }

    /**
	 * Notify all interested listeners of a change of the module's mental state.
	 * 
	 * @param module the module that changed
	 * @param result the specific result of this deliberation step.
	 */
    private void notifyListeners(APLModule module, DeliberationResult result) {
        ArrayList<ModuleChangeListener> l;
        synchronized (this) {
            l = new ArrayList<ModuleChangeListener>(listeners);
        }
        for (ModuleChangeListener acl : l) {
            acl.moduleChanged(module, result);
        }
    }

    /**
	 * Resets the deliberation cycle. 
	 * The consequence of calling this method is that the next deliberation step will be 'Apply PG-Rules'.
	 */
    public void reset() {
        current = cycle.listIterator();
    }

    /**
	 * Clones the deliberation cycle. 
	 */
    public Deliberation clone() {
        return new Deliberation(new ArrayList<ModuleChangeListener>(), cycle, current);
    }
}
