package magictool.task;

import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
import magictool.Cancelable;
import magictool.Project;

/**
 * TaskManager manages and executes a list of scheduled tasks. It extends the AbstractListModel
 * as it can display the list of tasks in a JList. TaskManger implements the cancelable interface
 * as the execution of the scheduled tasks may be stopped in progress.
 */
public class TaskManager extends AbstractListModel implements Cancelable {

    /**list of scheduled tasks in order of scheduled execution*/
    protected Vector tasks = new Vector();

    /**whether or not the execution of the tasks has been canceled*/
    protected boolean cancel = false;

    /**whether or not the process of executing all tasks schedules is completed or not*/
    protected boolean over = false;

    /**current task to be executed*/
    protected Task currentTask = null;

    /**project associated with the task manager*/
    protected Project project;

    /**
   * Constructs a task manager given its associted project
   * @param project project associated with the task manager
   */
    public TaskManager(Project project) {
        this.project = project;
    }

    /**
   * cancels the execution of the scheduled tasks
   */
    public void cancel() {
        cancel = true;
        if (!currentTask.cancel()) {
            JOptionPane.showMessageDialog(null, "Current Task Is Not Cancelable. Process Will End After This Task Completes");
        }
        for (int i = 0; i < tasks.size(); i++) {
            Task t = (Task) tasks.elementAt(i);
            if (t.getStatus() != t.FAILED && t.getStatus() != t.COMPLETED) t.setStatus(t.READY);
        }
    }

    /**
   * executes all of the scheduled tasks in order
   */
    public void run() {
        Thread thread = new Thread() {

            public void run() {
                for (int i = 0; i < tasks.size(); i++) {
                    Task t = (Task) tasks.elementAt(i);
                    if (t.getStatus() != t.FAILED && t.getStatus() != t.COMPLETED) t.setStatus(t.WAITING);
                }
                for (int i = 0; i < tasks.size() && !cancel; i++) {
                    currentTask = (Task) tasks.elementAt(i);
                    if (currentTask.status != Task.COMPLETED && currentTask.status != Task.FAILED) {
                        currentTask.setStatus(Task.RUNNING);
                        currentTask.execute();
                        while (!currentTask.isFinished()) {
                        }
                        fireContentsChanged(this, 0, tasks.size());
                        if (i == tasks.size() - 1) over = true;
                    }
                }
            }
        };
        thread.start();
        fireContentsChanged(this, 0, tasks.size());
    }

    /**
   * starts the execution of the scheduled tasks
   */
    public void start() {
        cancel = false;
        over = false;
        run();
    }

    /**
   * returns the number of successfully completed tasks
   * @return number of successfully completed tasks
   */
    public int getSuccessful() {
        int success = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (((Task) tasks.elementAt(i)).getStatus() == Task.COMPLETED) success++;
        }
        return success;
    }

    /**
   * returns whether or not the process of executing all tasks schedules is completed or not
   * @return whether or not the process of executing all tasks schedules is completed or not
   */
    public boolean isFinished() {
        return over;
    }

    /**
   * returns a project which is composed of all files in the project associated with the
   * task manager as well as those which are currently scheduled to be created. This allows
   * the user to created files from files that currently do not exist but will at the time
   * of the creation of the new file.
   * @return project containing all available files to be used for creating new files
   */
    public Project getTaskProject() {
        Project p = new Project();
        p.setPath(project.getPath());
        p.setName(project.getName());
        String files[] = project.getAllFiles();
        for (int i = 0; i < files.length; i++) {
            p.addFile(files[i]);
        }
        for (int i = 0; i < tasks.size(); i++) {
            p.addFile(((Task) tasks.elementAt(i)).getCreatedFile());
        }
        return p;
    }

    /**
   * adds a task to the manager - the task is placed last
   * @param t task to be added to the manager
   */
    public void addTask(Task t) {
        tasks.addElement(t);
        this.fireIntervalAdded(this, tasks.size() - 1, tasks.size() - 1);
    }

    /**
   * removes a task from the manager at the specified location. Also all tasks which
   * require any file created from the task removed are also removed
   * @param num location of the task to be removed
   */
    public void removeTask(int num) {
        Vector c = new Vector();
        c.add(((Task) tasks.elementAt(num)).getCreatedFile());
        tasks.removeElementAt(num);
        this.fireIntervalRemoved(this, num, num);
        boolean done = false;
        while (!done) {
            int removed = 0;
            int s = tasks.size();
            for (int i = 0; i < s; i++) {
                Task t = (Task) tasks.elementAt(i - removed);
                for (int j = 0; j < c.size(); j++) {
                    if (t.requiresFile((String) c.elementAt(j))) {
                        c.add(t.getCreatedFile());
                        tasks.removeElementAt(i - removed);
                        this.fireIntervalRemoved(this, i - removed, i - removed);
                    }
                }
            }
            if (removed == 0) done = true;
        }
    }

    /**
   * removes a task from the manager. Also all tasks which
   * require any file created from the task removed are also removed.
   * @param t task to be removed from the manager
   */
    public void removeTask(Task t) {
        for (int i = 0; i < tasks.size(); i++) {
            if (((Task) tasks.elementAt(i)).equals(t)) {
                removeTask(i);
                break;
            }
        }
    }

    /**
   * removes all tasks from the manager
   */
    public void removeAll() {
        int s = tasks.size();
        tasks.removeAllElements();
        this.fireIntervalRemoved(this, 0, s - 1);
    }

    /**
   * removes all completed and failed tasks from the manager
   */
    public void removeCompleted() {
        int removed = 0;
        int s = tasks.size();
        for (int i = 0; i < s; i++) {
            Task t = (Task) tasks.elementAt(i - removed);
            if (t.getStatus() == t.COMPLETED || t.getStatus() == t.FAILED) {
                tasks.removeElementAt(i - removed);
                this.fireIntervalRemoved(this, i - removed, i - removed);
                removed++;
            }
        }
    }

    /**
   * moves a task up the scheduled list if it is possible
   * @param num number of task to move up the list
   */
    public void moveTaskUp(int num) {
        if (canMoveUp(num)) {
            Task t = (Task) tasks.elementAt(num - 1);
            tasks.setElementAt(tasks.elementAt(num), num - 1);
            tasks.setElementAt(t, num);
            this.fireContentsChanged(this, num - 1, num);
        }
    }

    /**
   * moves a task down the scheduled list if it is possible
   * @param num number of task to move down the list
   */
    public void moveTaskDown(int num) {
        if (canMoveDown(num)) {
            Task t = (Task) tasks.elementAt(num + 1);
            tasks.setElementAt(tasks.elementAt(num), num + 1);
            tasks.setElementAt(t, num);
            this.fireContentsChanged(this, num, num + 1);
        }
    }

    /**
   * returns whether or not a filename is created by one of the tasks
   * @param file filename to be determined if it is created or not
   * @return whether or not a filename is created by one of the tasks
   */
    public boolean fileExists(String file) {
        for (int i = 0; i < tasks.size(); i++) {
            if (((Task) tasks.elementAt(i)).createdFile.equalsIgnoreCase(file)) return true;
        }
        return false;
    }

    /**
   * returns whether or not removing a task will remove a required file for other tasks
   * @param t task to be removed
   * @return whether or not removing a task will remove a required file for other tasks
   */
    public boolean removesRequiredFile(Task t) {
        int num = 0;
        while (!((Task) tasks.elementAt(num)).equals(t) && num < tasks.size()) num++;
        if (num == tasks.size()) return false; else {
            String created = t.createdFile;
            for (int i = num + 1; i < tasks.size(); i++) {
                if (((Task) tasks.elementAt(num)).requiresFile(created)) return true;
            }
        }
        return false;
    }

    /**
   * returns whether or not removing a task at a specified location will remove a required file for other tasks
   * @param num location of task to be removed
   * @return whether or not removing a task at a specified location will remove a required file for other tasks
   */
    public boolean removesRequiredFile(int num) {
        if (num >= tasks.size()) return true;
        String created = ((Task) tasks.elementAt(num)).getCreatedFile();
        for (int i = num + 1; i < tasks.size(); i++) {
            if (((Task) tasks.elementAt(i)).requiresFile(created)) return true;
        }
        return false;
    }

    /**
   * returns whether or not a task can move up one spot in the list. This can occur if it
   * is not in the first spot and if it does not require the file created by the task right
   * above it
   * @param num location of the task to move up
   * @return whether or not a task can move up one spot in the list
   */
    public boolean canMoveUp(int num) {
        if (num < 1 || num >= tasks.size()) return false;
        return (!((Task) tasks.elementAt(num)).requiresFile(((Task) tasks.elementAt(num - 1)).createdFile));
    }

    /**
   * returns whether or not a task can move down one spot in the list. This can occur if it
   * is not in the last spot and if it does not require the file created by the task right
   * below it
   * @param num location of the task to move down
   * @return whether or not a task can move down one spot in the list
   */
    public boolean canMoveDown(int num) {
        if (num < 0 || num >= tasks.size() - 1) return false;
        return (!((Task) tasks.elementAt(num + 1)).requiresFile(((Task) tasks.elementAt(num)).createdFile));
    }

    /**
   * returns whether or not a task can move up one spot in the list. This can occur if it
   * is not in the first spot and if it does not require the file created by the task right
   * above it
   * @param t task to move up
   * @return whether or not a task can move up one spot in the list
   */
    public boolean canMoveUp(Task t) {
        int num = 0;
        while (!((Task) tasks.elementAt(num)).equals(t) && num < tasks.size()) num++;
        if (num == tasks.size() || num < 1) return false; else {
            return (!((Task) tasks.elementAt(num)).requiresFile(((Task) tasks.elementAt(num - 1)).createdFile));
        }
    }

    /**
   * returns the task at a given index
   * @param index location of task to be returns
   * @return task at a given index
   */
    public Object getElementAt(int index) {
        if (index >= tasks.size() || index < 0) return null;
        return tasks.elementAt(index);
    }

    /**
   * returns the number of scheduled tasks
   * @return number of scheduled tasks
   */
    public int getSize() {
        return tasks.size();
    }

    /**
   * returns the status of all tasks which are waiting back to ready. This is useful when
   * the task manager is canceled but it is not desired to lose all the tasks scheduled.
   */
    public void returnToReady() {
        for (int i = 0; i < tasks.size(); i++) {
            Task t = (Task) tasks.elementAt(i);
            if (t.getStatus() == t.WAITING) {
                t.setStatus(t.READY);
            }
        }
        this.fireContentsChanged(this, 0, tasks.size());
    }
}
