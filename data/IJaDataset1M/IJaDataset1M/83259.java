package com.joejag.mavenstats.client.views.forms.project.add;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.joejag.mavenstats.client.MavenStats;
import com.joejag.mavenstats.client.views.forms.project.ProjectDTOAction;
import com.joejag.mavenstats.client.dto.ProjectDTO;
import com.joejag.mavenstats.client.service.ProjectsService;
import com.joejag.mavenstats.client.service.ProjectsServiceAsync;

public class AddProjectAction implements ProjectDTOAction {

    public void performSomeAction(ProjectDTO projectDTO) {
        ProjectsServiceAsync serviceAsync = ProjectsService.App.getInstance();
        serviceAsync.addProject(projectDTO, new AsyncCallback() {

            public void onFailure(Throwable throwable) {
            }

            public void onSuccess(Object o) {
                MavenStats.instance().refreshProjectsTree();
            }
        });
    }
}
