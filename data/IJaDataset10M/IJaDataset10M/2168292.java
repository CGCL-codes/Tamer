package siouxsie.desktop.task.impl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Callable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.UIManager;
import org.junit.Test;
import siouxsie.desktop.task.ITask;
import siouxsie.desktop.task.ITaskManagerListener;
import siouxsie.desktop.task.impl.TaskEvent.TaskEventType;

/**
 * @author Arnaud Cogoluegnes
 * @version $Id: TestTaskManagerPanel.java 181 2008-09-21 15:36:16Z acogo $
 */
public class TestTaskManagerPanel {

    @Test
    public void test() {
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TaskManagerPanel panel = new TaskManagerPanel();
        TaskManager manager = new TaskManager(new ArrayList<ITaskManagerListener>());
        manager.addListener(panel);
        panel.receive(new TaskEvent(new DummyTask<Void>("une tache"), TaskEventType.STARTED));
        panel.receive(new TaskEvent(new DummyTask<Void>("une autre tache"), TaskEventType.STARTED));
        panel.receive(new TaskEvent(new DummyTask<Void>("une autre hyper longue tache"), TaskEventType.STARTED));
        frame.setLayout(new BorderLayout());
        JToolBar toolbar = new JToolBar();
        toolbar.add(new TaskAction(panel));
        toolbar.add(new TaskManagerAction(manager));
        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(panel.getComponent(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private static class TaskAction extends AbstractAction {

        TaskManagerPanel panel;

        public TaskAction(TaskManagerPanel panel) {
            putValue(Action.NAME, "Add action");
            this.panel = panel;
        }

        public void actionPerformed(ActionEvent e) {
            Calendar cal = Calendar.getInstance();
            panel.receive(new TaskEvent(new DummyTask<Void>(cal.getTime().toString()), TaskEventType.STARTED));
        }
    }

    private static class TaskManagerAction extends AbstractAction {

        TaskManager manager;

        public TaskManagerAction(TaskManager manager) {
            putValue(Action.NAME, "Add manager action");
            this.manager = manager;
        }

        public void actionPerformed(ActionEvent e) {
            Calendar cal = Calendar.getInstance();
            manager.submitTask(new DummyCallable(), cal.getTime().toString());
        }
    }

    private static class DummyTask<V> implements ITask<V> {

        private String description;

        private PropertyChangeSupport support = new PropertyChangeSupport(this);

        private boolean cancelled, done;

        public DummyTask(String description) {
            super();
            this.description = description;
            cancelled = false;
            done = false;
        }

        public void addPropertyChangeListener(PropertyChangeListener lst) {
            support.addPropertyChangeListener(lst);
        }

        public void removePropertyChangeListener(PropertyChangeListener lst) {
            support.addPropertyChangeListener(lst);
        }

        public void cancel() {
            this.cancelled = true;
            support.firePropertyChange(PROPERTY_CANCELLED, false, true);
            Timer timer = new Timer(500, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    done();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }

        public void done() {
            this.done = true;
            support.firePropertyChange(PROPERTY_DONE, false, true);
        }

        ;

        public V get() {
            return null;
        }

        ;

        public String getDescription() {
            return description;
        }

        ;

        public boolean isCancelled() {
            return cancelled;
        }

        public boolean isDone() {
            return done;
        }
    }

    private static class DummyCallable implements Callable<Void> {

        public Void call() throws Exception {
            int count = 0;
            while (!Thread.currentThread().isInterrupted() && count < 5) {
                try {
                    Thread.sleep(1000);
                    count++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("interrupted !");
                    break;
                }
            }
            return null;
        }
    }
}
