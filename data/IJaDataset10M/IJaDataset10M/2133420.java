package com.wozgonon.eventstore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import com.wozgonon.docustrate.DesignPattern;
import com.wozgonon.eventstore.BaseData.PlugInMemento;
import com.wozgonon.eventstore.UseCaseManager.ChangeEvent;

/**
 * The purpose of this class is provide a user interface independent API for
 * managing a set of background tasks.
 *
 *  <li>A list of event sources which it keeps current and notifies observers of any changes.
 *  <li>Manages threads and scheduled execution of tasks
 *  
 *  TODO how should this best be constructed and listen to EventStoreManager.ChangeListener events?
 */
@DesignPattern(url = "http://en.wikipedia.org/wiki/Observer_pattern", usage = "Observers can register with this class and be signalled on changes to the list of event sources.")
public class PluginExecutor {

    private final UseCaseManager eventStoreManager;

    PluginExecutor(UseCaseManager eventStoreManager) {
        this.eventStoreManager = eventStoreManager;
    }

    public PlugInRunnable newEventSource(Class<?> clas, String argument) throws Exception {
        final PlugInMemento memento = new PlugInMemento(clas, argument);
        memento.createTransientEventSource(this);
        final PlugInRunnable eventSource = memento.getTransientEventSource();
        getPlugIns().add(memento);
        notifyListeners();
        return eventSource;
    }

    public int size() {
        return getPlugIns().size();
    }

    public PlugInRunnable get(int row) {
        return getPlugIns().get(row).getTransientEventSource();
    }

    public void remove(int row) {
        removeWithoutNotify(row);
        notifyListeners();
    }

    public void removeWithoutNotify(int row) {
        final PlugInMemento memento = getPlugIns().get(row);
        getPlugIns().remove(row);
        memento.getTransientEventSource().getFuture().cancel(true);
    }

    private void notifyListeners() {
        this.eventStoreManager.fireChangeEvent(ChangeEvent.PLUGIN_ADDED_OR_REMOVED);
    }

    private final java.util.ArrayList<BaseData.PlugInMemento> getPlugIns() {
        return this.eventStoreManager.getBaseData().getPlugIns();
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);
}
