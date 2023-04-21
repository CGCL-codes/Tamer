package dcf.server;

import dcf.client.Worker;
import java.util.Vector;

/**
 * A General task interface should be implemented by all task objects<br>
 * @author Tal Salmona
 */
public interface Task {

    /**
   * @param obj Most commonly a Vector of data to be processed.
   */
    public void setData(Object obj);

    public Object getData();

    /**
   * Divide the task into parts - if the data is a List it is already divided.
   */
    public void divide();

    public boolean hasMore();

    public Object next();

    public int getSize();

    /**
   * @param obj An Object to add to the results.
   */
    public void addResult(Object obj);

    public Object getResults();

    /**
   * Sets the display name for the task - This is used by the server to index tasks.
   * @param str The name for this task.
   */
    public void setName(String str);

    public String getName();

    /**
   * The Distributer will call this method when a Task is returned by the client.<br>
   * If the task is circualr the client will call the generate() method to create
   * additinal tasks.
   * @return <b>true</b> - If the task should create new tasks.
   *         <b>false</b> - If the task ends when returned.
   */
    public boolean isCircular();

    /**
   * If this is a circular task it should generate a vector of new tasks
   * @return A Vector of new Tasks.
   */
    public Vector generate();

    /**
   * Called by Distributer when a task is returned.
   */
    public void processResults();

    /**
   * Sets the parent for this task.
   * @param w The active Worker for this task.
   */
    public void setParent(Worker w);
}
