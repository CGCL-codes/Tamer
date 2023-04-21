package ch.elca.leaf.buildsystem.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Task;
import ch.elca.leaf.buildsystem.model.ProjectRepository;

/**
 * This task reloads targets from a Ant build file into the current Ant
 * project. Old targets are removed. Use with care !
 *
 * <script type="text/javascript">printFileStatus
 *   ("$Source$",
 *   "$Revision: 162 $", "$Date: 2005-03-17 04:16:02 -0500 (Thu, 17 Mar 2005) $", "$Author: yma $"
 * );</script>
 *
 * @author Yves Martin (YMA)R1vT1vF4f
 * @version $Revision: 162 $
 */
public class ReloadProject extends Task {

    /** Project file to load */
    private File m_file;

    /**
     * The name of the file to import.
     * @param file the name of the file
     */
    public void setFile(File file) {
        this.m_file = file;
    }

    /**
     * Loads the project file as a Ant project.
     */
    public void execute() {
        if (m_file == null) {
            throw new BuildException(getTaskName() + " requires file attribute");
        }
        ProjectRepository pr = ProjectRepository.getInstance();
        pr.setBootstrapper(getProject());
        if (!m_file.exists()) {
            String message = "Cannot find " + m_file + " loaded from " + getLocation().getFileName();
            throw new BuildException(message);
        }
        log("Reloading build file " + m_file, Project.MSG_INFO);
        Project current = getProject();
        current.getTargets().clear();
        ProjectHelper helper = (ProjectHelper) current.getReference("ant.projectHelper");
        helper.getImportStack().remove(current.getName());
        try {
            helper.parse(getProject(), m_file);
        } catch (BuildException ex) {
            throw ProjectHelper.addLocationToBuildException(ex, getLocation());
        }
    }
}
