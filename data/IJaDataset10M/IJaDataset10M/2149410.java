package com.neolab.crm.client.app.widgets.tables;

import java.util.ArrayList;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.neolab.crm.client.app.base.AbstractAsyncCallback;
import com.neolab.crm.client.app.base.ImageFactory;
import com.neolab.crm.client.app.events.ProjectActivityEvent;
import com.neolab.crm.client.app.events.ProjectUserChangeEvent;
import com.neolab.crm.client.app.gin.Injector;
import com.neolab.crm.client.app.widgets.UserPreviewWidget;
import com.neolab.crm.client.app.widgets.WidgetFactory;
import com.neolab.crm.client.app.widgets.dialogs.AddUserDialog;
import com.neolab.crm.client.app.widgets.dialogs.AddUserDialog.AddActivityHandler;
import com.neolab.crm.client.app.widgets.tables.resources.NeoCellListResources;
import com.neolab.crm.client.fwk.PushButton;
import com.neolab.crm.client.fwk.containers.FlowContainer;
import com.neolab.crm.shared.domain.Project;
import com.neolab.crm.shared.domain.User;
import com.neolab.crm.shared.resources.Option;
import com.neolab.crm.shared.resources.rpc.Response;

public class UsersCellList extends FlowContainer {

    private CellList<User> list;

    private Project project;

    private AbstractDataProvider<User> provider;

    private AddActivityHandler handler;

    private boolean member;

    public UsersCellList(Project project, boolean member, final AbstractDataProvider<User> provider, AddActivityHandler addActivityHandler) {
        super(false);
        this.handler = addActivityHandler;
        this.provider = provider;
        this.project = project;
        this.member = member;
        render();
        Injector.INSTANCE.getEventBus().addHandler(ProjectUserChangeEvent.TYPE, new ProjectUserChangeEvent.Handler<ProjectUserChangeEvent>() {

            @Override
            public void on(ProjectUserChangeEvent e) {
                provider.removeDataDisplay(list);
                provider.addDataDisplay(list);
            }
        });
    }

    @Override
    protected void render() {
        HorizontalPanel head = new HorizontalPanel();
        head.add(new Label("Users"));
        head.addStyleName("users-List-Head");
        addWidget(head);
        PushButton addUser = new PushButton(" + ");
        addUser.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                new AddUserDialog(project.getPid(), handler);
            }
        });
        PushButton leaveProject = new PushButton("Leave");
        leaveProject.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ArrayList<Integer> uids = new ArrayList<Integer>();
                uids.add(Injector.INSTANCE.getApplication().getActiveUser().getUid());
                Injector.INSTANCE.getNeoService().removeActivity(project.getPid(), uids, new AbstractAsyncCallback<Response>() {

                    @Override
                    public void success(Response result) {
                        WidgetFactory.alert("You have been removed from project");
                        Injector.INSTANCE.getEventBus().fireEvent(new ProjectActivityEvent(project.getPid(), false));
                    }
                });
            }
        });
        PushButton joinProject = new PushButton(" Join project ");
        joinProject.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ArrayList<Integer> uids = new ArrayList<Integer>();
                uids.add(Injector.INSTANCE.getApplication().getActiveUser().getUid());
                handler.addActivity(project.getPid(), uids, null);
            }
        });
        if (member) {
            head.add(leaveProject);
            head.setCellHorizontalAlignment(leaveProject, HasHorizontalAlignment.ALIGN_RIGHT);
        } else {
            head.add(joinProject);
            head.setCellHorizontalAlignment(joinProject, HasHorizontalAlignment.ALIGN_RIGHT);
        }
        if (Injector.INSTANCE.getApplication().hasPrivilege(Option.ADD_USERS_TO_PROJECT)) {
            head.add(addUser);
        }
        head.setCellHorizontalAlignment(addUser, HasHorizontalAlignment.ALIGN_RIGHT);
        list = new CellList<User>(new UserCell(project, "dblclick"), NeoCellListResources.INSTANCE, User.KEY_PROVIDER);
        addWidget(list);
        setWidth("250px");
        list.setPageSize(30);
        list.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        list.setEmptyListWidget(new Label("No active users"));
        list.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
        final SingleSelectionModel<User> selectionModel = new SingleSelectionModel<User>(User.KEY_PROVIDER);
        list.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            public void onSelectionChange(SelectionChangeEvent event) {
            }
        });
        provider.addDataDisplay(list);
    }

    private class UserCell extends AbstractCell<User> {

        private Project project;

        public UserCell(String string) {
            super(string);
        }

        public UserCell(Project project, String string) {
            this(string);
            this.project = project;
        }

        @Override
        public void render(Context context, User value, SafeHtmlBuilder sb) {
            sb.appendHtmlConstant("<table>");
            sb.appendHtmlConstant("<tr><td rowspan='2'>");
            sb.appendHtmlConstant(ImageFactory.getSmallUserImage(value.getImage()).getElement().getString());
            sb.appendHtmlConstant("</td>");
            sb.appendHtmlConstant("<td >");
            sb.appendEscaped(value.getFirstName() + " " + value.getLastName());
            sb.appendHtmlConstant("</td></tr><tr><td style='font-size:80%; font-style: italic; '>");
            sb.appendEscaped(value.getEmail());
            sb.appendHtmlConstant("</td></tr></table>");
        }

        @Override
        public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent, User value, NativeEvent event, ValueUpdater<User> valueUpdater) {
            if (value == null) {
                return;
            }
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            if ("dblclick".equals(event.getType())) {
                UserPreviewWidget d = new UserPreviewWidget(project, value, 1);
            }
        }
    }
}
