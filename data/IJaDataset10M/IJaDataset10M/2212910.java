package org.netbeans.module.flexbean.modules.project;

import org.netbeans.api.project.Project;
import org.netbeans.module.flexbean.modules.project.actions.FlexProjectBuildCommand;
import org.netbeans.module.flexbean.modules.project.actions.FlexProjectCleanCommand;
import org.netbeans.module.flexbean.modules.project.actions.FlexProjectCommand;
import org.netbeans.module.flexbean.modules.project.actions.FlexProjectRunCommand;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.util.Lookup;

/**
 * Action on the whole project
 * @author arnaud
 */
public class FlexProjectActionProvider implements ActionProvider {

    private final Project _project;

    private static final String[] _supportedActions = { COMMAND_DELETE, COMMAND_COPY, COMMAND_MOVE, COMMAND_RENAME, COMMAND_BUILD, COMMAND_CLEAN, COMMAND_RUN };

    public FlexProjectActionProvider(Project project) {
        this._project = project;
    }

    /**
     * @see ActionProvider#getSupportedActions() 
     * @return
     */
    public String[] getSupportedActions() {
        return _supportedActions;
    }

    /**
     * @see ActionProvider#invokeAction(java.lang.String, org.openide.util.Lookup) 
     * @param command
     * @param context
     * @throws java.lang.IllegalArgumentException
     */
    public void invokeAction(String command, Lookup context) throws IllegalArgumentException {
        if (COMMAND_DELETE.equals(command)) {
            final FlexProjectCommand cleanCmd = new FlexProjectCleanCommand(_project);
            cleanCmd.execute(null);
            DefaultProjectOperations.performDefaultDeleteOperation(_project);
            return;
        }
        if (COMMAND_COPY.equals(command)) {
            DefaultProjectOperations.performDefaultCopyOperation(_project);
            return;
        }
        if (COMMAND_MOVE.equals(command)) {
            DefaultProjectOperations.performDefaultMoveOperation(_project);
            return;
        }
        if (COMMAND_RENAME.equals(command)) {
            DefaultProjectOperations.performDefaultRenameOperation(_project, null);
            return;
        }
        if (COMMAND_BUILD.equals(command)) {
            final FlexProjectCommand buildCmd = new FlexProjectBuildCommand(_project);
            buildCmd.execute(context);
            return;
        }
        if (COMMAND_CLEAN.equals(command)) {
            final FlexProjectCommand cleanCmd = new FlexProjectCleanCommand(_project);
            cleanCmd.execute(context);
            return;
        }
        if (COMMAND_RUN.equals(command)) {
            final FlexProjectCommand runCmd = new FlexProjectRunCommand(_project);
            runCmd.execute(context);
            return;
        }
    }

    /**
     * @see ActionProvider#isActionEnabled(java.lang.String, org.openide.util.Lookup) 
     * @param command
     * @param context
     * @return
     * @throws java.lang.IllegalArgumentException
     */
    public boolean isActionEnabled(String command, Lookup context) throws IllegalArgumentException {
        if (command.equals(COMMAND_RUN) && FlexProjectHelper.isComponentProject(_project)) {
            return false;
        }
        return true;
    }
}
