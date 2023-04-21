package net.sourceforge.sfdt.internal.core.command.ant;

import java.util.Map;
import net.sourceforge.sfdt.internal.core.command.ISfCommandRunner;
import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;

public class AntSfCommandRunner extends WorkspaceJob implements ISfCommandRunner {

    private IStatus status = null;

    private Path antFilePath;

    private String arguments;

    private Map<String, String> properties;

    private String[] tasks;

    public AntSfCommandRunner(String name) {
        super(name);
    }

    @Override
    public IStatus run() {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                schedule();
            }
        });
        return status;
    }

    @Override
    public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
        AntRunner runner = new AntRunner();
        runner.addBuildLogger("org.apache.tools.ant.DefaultLogger");
        if (arguments != null) {
            runner.setArguments(arguments);
        }
        if (properties != null) {
            runner.addUserProperties(properties);
        }
        runner.setBuildFileLocation(antFilePath.toOSString());
        runner.setExecutionTargets(tasks);
        runner.run();
        return status = Status.OK_STATUS;
    }

    /**
	 * @return the status
	 */
    public IStatus getStatus() {
        return status;
    }

    /**
	 * @return the antFilePath
	 */
    public Path getAntFilePath() {
        return antFilePath;
    }

    /**
	 * @param antFilePath the antFilePath to set
	 */
    public void setAntFilePath(Path antFilePath) {
        this.antFilePath = antFilePath;
    }

    /**
	 * @return the arguments
	 */
    public String getArguments() {
        return arguments;
    }

    /**
	 * @param arguments the arguments to set
	 */
    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    /**
	 * @return the properties
	 */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
	 * @param properties the properties to set
	 */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
	 * @return the tasks
	 */
    public String[] getTasks() {
        return tasks;
    }

    /**
	 * @param tasks the tasks to set
	 */
    public void setTasks(String[] tasks) {
        this.tasks = tasks;
    }
}
