package org.thechiselgroup.choosel.workbench.server.workspace;

import java.util.List;
import org.thechiselgroup.choosel.core.client.util.ServiceException;
import org.thechiselgroup.choosel.core.client.util.task.Task;
import org.thechiselgroup.choosel.workbench.client.workspace.dto.WorkspaceDTO;
import org.thechiselgroup.choosel.workbench.client.workspace.dto.WorkspacePreviewDTO;
import org.thechiselgroup.choosel.workbench.client.workspace.service.WorkspacePersistenceService;
import org.thechiselgroup.choosel.workbench.server.ChooselServiceServlet;
import org.thechiselgroup.choosel.workbench.server.PMF;
import com.google.appengine.api.users.UserServiceFactory;

public class WorkspacePersistenceServiceServlet extends ChooselServiceServlet implements WorkspacePersistenceService {

    private WorkspacePersistenceService service = null;

    private WorkspacePersistenceService getServiceDelegate() {
        if (service == null) {
            service = new WorkspacePersistenceServiceImplementation(PMF.get(), new WorkspaceSecurityManager(UserServiceFactory.getUserService()));
        }
        return service;
    }

    @Override
    public WorkspaceDTO loadWorkspace(final Long id) throws ServiceException {
        return execute(new Task<WorkspaceDTO>() {

            @Override
            public WorkspaceDTO execute() throws ServiceException {
                return getServiceDelegate().loadWorkspace(id);
            }
        });
    }

    @Override
    public List<WorkspacePreviewDTO> loadWorkspacePreviews() throws ServiceException {
        return execute(new Task<List<WorkspacePreviewDTO>>() {

            @Override
            public List<WorkspacePreviewDTO> execute() throws ServiceException {
                return getServiceDelegate().loadWorkspacePreviews();
            }
        });
    }

    @Override
    public Long saveWorkspace(final WorkspaceDTO workspace) throws ServiceException {
        return execute(new Task<Long>() {

            @Override
            public Long execute() throws ServiceException {
                return getServiceDelegate().saveWorkspace(workspace);
            }
        });
    }
}
