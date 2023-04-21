package guineu.taskcontrol;

/**
 *@author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 */
public interface TaskListener {

    /**
	 * Called when the status of a Task is changed.
	 *
	 * @param e
	 *            The TaskEvent which occurred
	 */
    public void statusChanged(TaskEvent e);
}
