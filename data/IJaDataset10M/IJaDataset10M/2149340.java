package com.tensegrity.webetlclient.modules.model.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tensegrity.palowebviewer.modules.util.client.Logger;
import com.tensegrity.webetlclient.modules.core.client.IServerAPIAsync;
import com.tensegrity.webetlclient.modules.core.client.model.ETLProject;

public class RemoveProjectTask implements AsyncCallback {

    private final ETLProject project;

    private final ApplicationModel appModel;

    public RemoveProjectTask(ETLProject project, ApplicationModel model) {
        this.project = project;
        this.appModel = model;
    }

    public void send() {
        IServerAPIAsync api = appModel.getServerAPI();
        String name = project.getInitialName();
        if (name != null) {
            api.removeProject(project.getServer(), name, this);
        } else {
            onSuccess(Boolean.TRUE);
        }
    }

    public void onFailure(Throwable t) {
        Logger.error(t.getMessage());
    }

    public void onSuccess(Object arg) {
        appModel.removeNode(project);
    }
}
