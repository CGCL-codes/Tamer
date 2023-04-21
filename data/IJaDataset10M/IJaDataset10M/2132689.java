package org.jcvi.vics.web.gwt.admin.editproject.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProjectServiceAsync {

    public void getProjectName(AsyncCallback callback);

    public void getAllProjects(AsyncCallback callback);
}
