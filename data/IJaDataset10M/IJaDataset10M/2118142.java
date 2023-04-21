package org.kompiro.readviewer.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.kompiro.readviewer.service.INotificationListener;
import org.kompiro.readviewer.service.INotificationMessage;
import org.kompiro.readviewer.service.INotificationService;
import org.kompiro.readviewer.service.INotificationnNotifier;
import org.kompiro.readviewer.service.NotificationEvent;
import org.kompiro.readviewer.service.StatusHandler;
import org.kompiro.readviewer.ui.preferences.PercsPreference;

public class SynchronizeJob extends Job implements INotificationnNotifier, IPropertyChangeListener {

    private final List<INotificationMessage> notifications;

    private final List<INotificationListener> listeners;

    private long lastUpdate = 0L;

    private boolean modifiing;

    private INotificationListener notificationToView;

    public SynchronizeJob() {
        super("synchronize job");
        notifications = new ArrayList<INotificationMessage>();
        Collections.synchronizedList(notifications);
        listeners = new ArrayList<INotificationListener>();
    }

    boolean addListner(INotificationListener listener) {
        return listeners.add(listener);
    }

    boolean removeListner(INotificationListener listener) {
        return listeners.remove(listener);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            monitor.beginTask("Synchronize...", 100);
            getMessages(monitor);
            sortMessages();
            monitor.worked(10);
            notifyMessages();
            monitor.done();
            return Status.OK_STATUS;
        } finally {
            schedule(getRefreshPeriod());
        }
    }

    private int getRefreshPeriod() {
        return 60 * 1000 * PercsPreference.getPeriod();
    }

    private void getMessages(IProgressMonitor monitor) {
        modifiing = true;
        notifications.clear();
        int serviceCount = getNotificationService().size() == 0 ? 1 : getNotificationService().size();
        int ratio = 80 / serviceCount;
        for (final INotificationService service : getNotificationService()) {
            if (service == null) continue;
            String taskTitle = "Get infomation about :" + service.getServiceName();
            monitor.subTask(taskTitle);
            try {
                Job syncJob = new Job(taskTitle) {

                    @Override
                    protected IStatus run(IProgressMonitor monitor) {
                        try {
                            service.synchronizeFromSite();
                        } catch (Exception e) {
                            return new Status(Status.INFO, UIActivator.PLUGIN_ID, IStatus.OK, "error has occured", e);
                        }
                        return Status.OK_STATUS;
                    }
                };
                syncJob.schedule();
                waitLoading(service);
                startSynchronizeEvent();
                waitSync(syncJob);
            } catch (Exception e) {
                StatusHandler.fail(e, "Error has occured.", true);
            }
            notifications.addAll(service.getContents());
            monitor.worked(ratio);
        }
        ContentsFilter filter = new ContentsFilter();
        List<INotificationMessage> filtered = filter.filteredByDatetime(notifications, lastUpdate);
        lastUpdate = new Date().getTime() - getRefreshPeriod();
        notifications.clear();
        notifications.addAll(filtered);
        modifiing = false;
    }

    private void waitSync(Job syncJob) {
        while (Job.RUNNING == syncJob.getState()) {
        }
    }

    private void waitLoading(INotificationService service) {
        while (!service.isLoading()) {
        }
    }

    private void startSynchronizeEvent() {
        NotificationEvent e = new NotificationEvent(this);
        notificationToView.notificated(e);
    }

    private List<INotificationService> getNotificationService() {
        return UIActivator.getDefault().getNotificationService();
    }

    private void sortMessages() {
        Collections.sort(notifications, new Comparator<INotificationMessage>() {

            public int compare(INotificationMessage lhs, INotificationMessage rhs) {
                return rhs.compareTo(lhs);
            }
        });
    }

    private void notifyMessages() {
        for (INotificationListener listener : listeners) {
            NotificationEvent e = new NotificationEvent(this);
            listener.notificated(e);
        }
    }

    void setNotificationToView(INotificationListener notificationToView) {
        this.notificationToView = notificationToView;
    }

    public List<INotificationMessage> getNotificationMessages() {
        while (modifiing) {
        }
        return this.notifications;
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (PercsPreference.PERIOD_KEY.equals(event.getProperty())) {
            cancel();
            schedule(getRefreshPeriod());
        }
    }
}
