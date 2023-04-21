package de.axa.smartfix.monitoring.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ITreeSelection;
import de.axa.smartfix.monitoring.gui.MonitorWindow;
import de.axa.smartfix.monitoring.model.CompositeMonitor;
import de.axa.smartfix.monitoring.model.MonitorManager;

public class AddCompositeMonitorAction extends Action {

    MonitorWindow window;

    public AddCompositeMonitorAction(MonitorWindow window, String text) {
        this.window = window;
        setText(text);
    }

    @Override
    public void run() {
        ITreeSelection selection = (ITreeSelection) window.getTreeView().getSelection();
        if (selection.isEmpty()) {
            MonitorManager mm = window.getMonitorManager();
            mm.addMonitor(new CompositeMonitor());
        }
    }
}
