package org.thechiselgroup.choosel.workbench.client.workspace;

import java.util.ArrayList;
import java.util.List;
import org.thechiselgroup.choosel.workbench.client.workspace.dto.WorkspaceDTO;
import org.thechiselgroup.choosel.workbench.client.workspace.dto.WorkspacePreviewDTO;
import org.thechiselgroup.choosel.workbench.client.workspace.service.WorkspacePersistenceServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestWorkspacePersistenceServiceAsync implements WorkspacePersistenceServiceAsync {

    private List<WorkspaceDTO> dtos = new ArrayList<WorkspaceDTO>();

    @Override
    public void loadWorkspace(Long id, AsyncCallback<WorkspaceDTO> callback) {
        WorkspaceDTO workspaceDTO = dtos.get(id.intValue());
        callback.onSuccess(workspaceDTO);
    }

    @Override
    public void loadWorkspacePreviews(AsyncCallback<List<WorkspacePreviewDTO>> callback) {
    }

    @Override
    public void saveWorkspace(WorkspaceDTO workspace, AsyncCallback<Long> callback) {
        dtos.add(workspace);
        callback.onSuccess((long) (dtos.size() - 1));
    }
}
