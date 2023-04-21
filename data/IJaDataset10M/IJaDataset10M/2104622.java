package org.bsplus.client.mvp;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.Observable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import java.util.List;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;
import org.bsplus.client.event.InitCreateIssueEvent;
import org.bsplus.client.event.InitCreateIssueEventHandler;
import org.bsplus.client.event.SetCentreComponentEvent;
import org.bsplus.client.util.GenericAsyncCallback;
import org.bsplus.rpc.IssueRPCService;
import org.bsplus.rpc.IssueRPCServiceAsync;
import org.bsplus.rpc.ProjectRPCService;
import org.bsplus.rpc.ProjectRPCServiceAsync;
import org.bsplus.rpc.UserRPCService;
import org.bsplus.rpc.UserRPCServiceAsync;
import org.bsplus.rpc.dto.IssueDTO;
import org.bsplus.rpc.dto.IssueSeverityDTO;
import org.bsplus.rpc.dto.IssueTypeDTO;
import org.bsplus.rpc.dto.ProjectDTO;
import org.bsplus.rpc.dto.UserDTO;

public class CreateIssuePresenter extends WidgetPresenter<CreateIssuePresenter.Display> implements InitCreateIssueEventHandler {

    private IssueRPCServiceAsync issueRPCService = GWT.create(IssueRPCService.class);

    private ProjectRPCServiceAsync projectRPCService = GWT.create(ProjectRPCService.class);

    private UserRPCServiceAsync userRPCService = GWT.create(UserRPCService.class);

    public interface Display extends WidgetDisplay {

        void init();

        void setIssueTypes(List<IssueTypeDTO> issueTypes);

        void setIssueSeveritys(List<IssueSeverityDTO> issueSeveritys);

        void setProjects(List<ProjectDTO> projects);

        void setUsers(List<UserDTO> users);

        IssueTypeDTO getIssueType();

        IssueSeverityDTO getIssueSeverity();

        ProjectDTO getProject();

        UserDTO getAssignee();

        String getSummary();

        String getDescription();

        Observable getCreateButton();
    }

    @Inject
    public CreateIssuePresenter(Display display, EventBus eventBus) {
        super(display, eventBus);
        eventBus.addHandler(InitCreateIssueEvent.TYPE, this);
        getDisplay().getCreateButton().addListener(Events.Select, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent ce) {
                IssueDTO issue = new IssueDTO();
                issue.setIssueType(getDisplay().getIssueType());
                issue.setIssueSeverity(getDisplay().getIssueSeverity());
                issue.setAssignee(getDisplay().getAssignee());
                issue.setProject(getDisplay().getProject());
                issue.setSummary(getDisplay().getSummary());
                issue.setDescription(getDisplay().getDescription());
                issueRPCService.createIssue(issue, new GenericAsyncCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        Window.alert("Issue Created: " + result);
                        getDisplay().init();
                    }
                });
            }
        });
    }

    @Override
    protected void onBind() {
        issueRPCService.getAllIssueTypes(new GenericAsyncCallback<List<IssueTypeDTO>>() {

            @Override
            public void onSuccess(List<IssueTypeDTO> result) {
                getDisplay().setIssueTypes(result);
            }
        });
        issueRPCService.getAllIssueSeverities(new GenericAsyncCallback<List<IssueSeverityDTO>>() {

            @Override
            public void onSuccess(List<IssueSeverityDTO> result) {
                getDisplay().setIssueSeveritys(result);
            }
        });
        projectRPCService.getAllProjects(new GenericAsyncCallback<List<ProjectDTO>>() {

            @Override
            public void onSuccess(List<ProjectDTO> result) {
                getDisplay().setProjects(result);
            }
        });
        userRPCService.getAllUsers(new GenericAsyncCallback<List<UserDTO>>() {

            @Override
            public void onSuccess(List<UserDTO> result) {
                getDisplay().setUsers(result);
            }
        });
        getDisplay().init();
    }

    @Override
    protected void onUnbind() {
    }

    @Override
    public Place getPlace() {
        return null;
    }

    @Override
    protected void onPlaceRequest(PlaceRequest request) {
    }

    @Override
    public void refreshDisplay() {
    }

    @Override
    public void revealDisplay() {
    }

    @Override
    public void init(InitCreateIssueEvent event) {
        bind();
        eventBus.fireEvent(new SetCentreComponentEvent(getDisplay().asWidget()));
    }
}
