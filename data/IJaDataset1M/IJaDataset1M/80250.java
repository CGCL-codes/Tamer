package org.netbeans.cubeon.ui.query;

import java.awt.Component;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.cubeon.tasks.spi.repository.TaskRepository;
import org.netbeans.cubeon.ui.query.NewQueryWizardAction.WizardObject;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

class ChooseRepositoryWizard implements WizardDescriptor.Panel<WizardObject> {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private ChooseRepository component;

    private List<TaskRepository> repositorys;

    public ChooseRepositoryWizard(List<TaskRepository> repositorys) {
        this.repositorys = repositorys;
    }

    public Component getComponent() {
        if (component == null) {
            component = new ChooseRepository(this, repositorys);
        }
        return component;
    }

    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    public boolean isValid() {
        return component.isTaskRepositorySelected();
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

    final void fireChangeEvent() {
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
    }

    public void storeSettings(WizardObject settings) {
        settings.setRepository(component.getTaskRepository());
    }
}
