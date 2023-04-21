package protoj.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.listener.Log4jListener;

/**
 * A convenience class that hides some of the details of interacting with ant
 * programmatically. An ant task wrapper will typically hold and instance of
 * this class to ease implementation.
 * <p>
 * This class effectively mimicks an ant script that contains an outer project
 * element which in turn contains a single inner target element. The
 * {@link #addTask(Task)} method is used to provide the individual ant task
 * elements that should make up that target element.
 * 
 * @author Ashley Williams
 * 
 */
public final class AntTarget {

    private Target target;

    private Project project;

    private String name;

    /**
	 * The name of a target can appear in log4j reports for example so bear this
	 * in mind when choosing one.
	 * 
	 * @param name
	 */
    public AntTarget(String name) {
        this.name = name;
        project = new Project();
        project.setName("protoj-ant-project");
        project.init();
        target = new Target();
        target.setName("target");
        project.addTarget(target);
    }

    /**
	 * Enables logging at the specified level.
	 * 
	 * @param level
	 *            one of the <code>org.apache.tools.ant.Project.MSG_XXX</code>
	 *            logging levels
	 */
    public void initLogging(int level) {
        Log4jListener listener = new Log4jListener();
        project.addBuildListener(listener);
    }

    /**
	 * Adds a task sequentially to the underlying ant target.
	 * 
	 * @param task
	 */
    public void addTask(Task task) {
        task.setProject(target.getProject());
        target.addTask(task);
    }

    /**
	 * Executes the underlying ant target. Any tasks attached to the target will
	 * therefore be executed.
	 */
    public void execute() {
        target.execute();
    }

    public Target getTarget() {
        return target;
    }

    public Project getProject() {
        return project;
    }

    public String getName() {
        return name;
    }
}
