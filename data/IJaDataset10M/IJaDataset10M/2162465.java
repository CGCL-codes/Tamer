package org.thechiselgroup.choosel.workbench.client.workspace;

import org.thechiselgroup.choosel.core.client.command.CommandManager;
import org.thechiselgroup.choosel.dnd.client.windows.Desktop;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

/**
 * Responsibility: link current workspace to system resources (desktop, etc)
 */
public class DefaultWorkspaceManager implements WorkspaceManager {

    private final CommandManager commandManager;

    private final Desktop desktop;

    private HandlerManager handlerManager = new HandlerManager(this);

    private Workspace workspace;

    @Inject
    public DefaultWorkspaceManager(Desktop desktop, CommandManager commandManager) {
        assert desktop != null;
        assert commandManager != null;
        this.commandManager = commandManager;
        this.desktop = desktop;
    }

    @Override
    public HandlerRegistration addSwitchedWorkspaceEventHandler(WorkspaceSwitchedEventHandler h) {
        return handlerManager.addHandler(WorkspaceSwitchedEvent.TYPE, h);
    }

    @Override
    public void createNewWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setName("Untitled Workspace");
        workspace.setSavingState(WorkspaceSavingState.NOT_SAVED);
        desktop.clearWindows();
        setWorkspace(workspace);
    }

    @Override
    public Workspace getWorkspace() {
        if (workspace == null) {
            createNewWorkspace();
        }
        return workspace;
    }

    @Override
    public void setWorkspace(Workspace workspace) {
        assert workspace != null;
        if ((workspace.getId() != null) && (workspace.getId().equals(this.workspace.getId()))) {
            return;
        }
        this.workspace = workspace;
        commandManager.clear();
        handlerManager.fireEvent(new WorkspaceSwitchedEvent(workspace));
    }
}
