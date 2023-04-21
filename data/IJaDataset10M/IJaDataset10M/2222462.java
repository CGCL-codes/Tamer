package rogue.netbeans.module.importsources;

import java.awt.Component;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;

/**
 * WizardIterator implementation for handling the various wizard panels of this
 * module.
 *
 * @author Rogue
 */
public final class ImportSourcesWizardIterator implements WizardDescriptor.Iterator {

    private int index;

    private AbstractWizardPanel[] panels;

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private AbstractWizardPanel[] getPanels() {
        if (panels == null) {
            panels = new AbstractWizardPanel[] { new WizardPanel1(), new WizardPanel2(), new WizardPanel3() };
            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                steps[i] = c.getName();
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }

    public AbstractWizardPanel current() {
        return getPanels()[index];
    }

    public String name() {
        return "( " + (index + 1) + " of " + getPanels().length + " )";
    }

    public boolean hasNext() {
        return current().isPanelProcessingComplete() && index < getPanels().length - 1;
    }

    public boolean hasPrevious() {
        return current().isPanelProcessingComplete() && index > 0;
    }

    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    /**
     * Terminate any background tasks.
     */
    public void terminateAllRunningTasks() {
        current().terminateProcessing();
    }

    private transient Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);

    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    protected final void fireChangeEvent() {
        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<ChangeListener>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        listeners = new HashSet<ChangeListener>(1);
    }
}
