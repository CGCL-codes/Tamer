package com.db4o.nb.wizards;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

/**
 * Base wizard panel for this module. It contains a collection of useful methods
 * which help controlling the user flow through the wizard.
 *
 * @author Rogue.
 */
public abstract class AbstractWizardPanel implements WizardDescriptor.Panel {

    private final Set listeners = new HashSet(1);

    /**
     * Get the default help context.
     */
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * Add a change listener for this wizard panel.
     */
    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    /**
     * Remove a change listener from this wizard panel.
     */
    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    /**
     * Fires a change event.
     */
    protected final void fireChangeEvent() {
        Iterator itr;
        synchronized (listeners) {
            itr = new HashSet(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (itr.hasNext()) {
        }
    }

    /**
     * Specifies if the current panel has completed processing or not.
     */
    public abstract boolean isPanelProcessingComplete();

    /**
     * Terminate any processing in the panel.
     */
    public abstract void terminateProcessing();
}
