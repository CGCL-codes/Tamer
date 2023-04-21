package com.sandlex.smictm.model;

import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.PersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlArrayList;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Alexey Peskov
 */
public class Model extends Observable {

    private List tasks;

    private List closedTasks;

    private List<TaskEventBean> calendarTasks;

    private Date selectionDate = new Date(System.currentTimeMillis());

    public Model(String path) {
        PersistenceStrategy strategy = new FilePersistenceStrategy(new File(path));
        tasks = new XmlArrayList(strategy);
        PersistenceStrategy closedStrategy = new FilePersistenceStrategy(new File(path + File.separator + "closed"));
        closedTasks = new XmlArrayList(closedStrategy);
        selectTasksForDate();
    }

    public void addTask(String text) {
        tasks.add(new Task(text));
        selectTasksForDate();
    }

    public void updateTask(int index, String name) {
        Task newTask = new Task((Task) tasks.get(index), name);
        tasks.set(index, newTask);
        selectTasksForDate();
    }

    public Task getTask(int index) {
        return (Task) tasks.get(index);
    }

    public int getTasksNumber() {
        return tasks.size();
    }

    public void addState(int index, String state) {
        Task task = (Task) tasks.get(index);
        task.addState(state);
        tasks.remove(index);
        if (state.equals(State.Completed.toString()) || state.equals(State.Cancelled.toString())) {
            closedTasks.add(task);
        } else {
            tasks.add(task);
        }
        selectTasksForDate();
    }

    public boolean isDateHasTasks(Date date) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        for (Object task : tasks) {
            for (TaskEvent activity : ((Task) task).getActivities()) {
                if (df.format(activity.getDate()).equals(df.format(date))) {
                    return true;
                }
            }
        }
        for (Object task : closedTasks) {
            for (TaskEvent activity : ((Task) task).getActivities()) {
                if (df.format(activity.getDate()).equals(df.format(date))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void selectTasksForDate() {
        calendarTasks = new ArrayList<TaskEventBean>();
        Map<Task, String> calendarTasksMap = new TreeMap<Task, String>();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        for (Object task : tasks) {
            for (TaskEvent activity : ((Task) task).getActivities()) {
                if (df.format(activity.getDate()).equals(df.format(selectionDate))) {
                    calendarTasksMap.put((Task) task, calendarTasksMap.containsKey(task) ? calendarTasksMap.get(task) + " > " + activity.getName() : activity.getName());
                }
            }
        }
        for (Object task : closedTasks) {
            for (TaskEvent activity : ((Task) task).getActivities()) {
                if (df.format(activity.getDate()).equals(df.format(selectionDate))) {
                    calendarTasksMap.put((Task) task, calendarTasksMap.containsKey(task) ? calendarTasksMap.get(task) + " > " + activity.getName() : activity.getName());
                }
            }
        }
        for (Task task : calendarTasksMap.keySet()) {
            calendarTasks.add(new TaskEventBean(task, calendarTasksMap.get(task)));
        }
        setChanged();
        notifyObservers(null);
    }

    public int getCalendarTasksNumber() {
        return calendarTasks.size();
    }

    public TaskEventBean getCalendarTask(int index) {
        return calendarTasks.get(index);
    }

    public void setSelectionDate(Date selectionDate) {
        this.selectionDate = selectionDate;
    }

    public void addActivity(int index, Activity activity) {
        Task task = (Task) tasks.get(index);
        if (task.getState().equals(State.InProgress) && task.isActivityNew(activity)) {
            task.addActivity(activity);
            tasks.remove(index);
            tasks.add(task);
            selectTasksForDate();
        }
    }
}
