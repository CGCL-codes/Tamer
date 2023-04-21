package siouxsie.desktop.core.impl.windowlayout;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import siouxsie.desktop.core.Application;
import siouxsie.desktop.core.DesktopBus;
import siouxsie.desktop.core.event.ApplicationSelectedEvent;

public class JXTaskPaneListApplications {

    private JScrollPane sp;

    private JXTaskPaneContainer taskPaneContainer;

    private DesktopBus bus;

    public JXTaskPaneListApplications(Collection<Application> applications, DesktopBus desktopBus) {
        this.bus = desktopBus;
        taskPaneContainer = new JXTaskPaneContainer();
        sp = new JScrollPane(taskPaneContainer);
        Map<String, JXTaskPane> mapCategoryToTaskPane = new HashMap();
        for (Application app : applications) {
            JXTaskPane taskpane = mapCategoryToTaskPane.get(app.getCategoryId());
            if (taskpane == null) {
                taskpane = new JXTaskPane();
                mapCategoryToTaskPane.put(app.getCategoryId(), taskpane);
                taskpane.setTitle(app.getApplicationCategory().getName());
                taskpane.setToolTipText(app.getApplicationCategory().getDescription());
                if (app.getApplicationCategory().getIcon() != null) {
                    taskpane.setIcon(app.getApplicationCategory().getIcon());
                }
                taskPaneContainer.add(taskpane);
            }
            taskpane.add(new SwitchApplicationAction(app));
        }
    }

    public JComponent getComponent() {
        return sp;
    }

    class SwitchApplicationAction extends AbstractAction {

        private Application application;

        public SwitchApplicationAction(Application app) {
            application = app;
            putValue(Action.NAME, application.getName());
        }

        public void actionPerformed(ActionEvent evt) {
            bus.sendEvent(new ApplicationSelectedEvent(application));
        }
    }
}
