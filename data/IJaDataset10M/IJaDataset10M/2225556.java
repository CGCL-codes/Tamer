package org.netbeans.cubeon.ui.repository;

import java.awt.Component;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.cubeon.tasks.spi.repository.TaskRepositoryType.ConfigurationHandler;
import org.netbeans.cubeon.ui.repository.NewRepositoryWizardAction.WizardObject;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

class RepositorySettingsWizard implements WizardDescriptor.Panel<WizardObject>, ChangeListener {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private RepositorySettings component;

    public Component getComponent() {
        if (component == null) {
            component = new RepositorySettings();
        }
        return component;
    }

    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    public boolean isValid() {
        ConfigurationHandler handler = component.getHandler();
        return handler != null && handler.isValidConfiguration();
    }

    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);

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

    public void readSettings(WizardObject settings) {
        component.setWizardObject(settings);
        component.getHandler().addChangeListener(this);
    }

    public void storeSettings(WizardObject settings) {
        ConfigurationHandler handler = component.getHandler();
        assert handler != null;
        component.getHandler().removeChangeListener(this);
        settings.setRepository(handler.getTaskRepository());
    }

    public void stateChanged(ChangeEvent e) {
        fireChangeEvent();
    }
}
