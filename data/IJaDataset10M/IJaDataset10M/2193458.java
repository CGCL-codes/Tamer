package com.google.code.sagetvaddons.sjq.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

final class ActiveQueueContextMenu extends PopupPanel {

    private static ActiveQueueContextMenu instance = null;

    public static ActiveQueueContextMenu getInstance(String mediaId, String taskId, boolean canDelete, String title, boolean canKill, String objType) {
        if (instance == null) instance = new ActiveQueueContextMenu();
        instance.setView(mediaId, taskId, canDelete, title, canKill, objType);
        return instance;
    }

    public static void setPosition(int left, int top) {
        if (instance == null) instance = new ActiveQueueContextMenu();
        instance.setPopupPosition(left, top);
        PriorityEditorPopup.setPosition(left, top);
    }

    private class ViewLogCommand implements Command {

        public void execute() {
            AppState state = AppState.getInstance();
            state.setLogView(mediaId, taskId, title, objType);
            state.setAppView(AppState.View.LOG_VIEWER);
            hide();
        }
    }

    private class KillTaskCommand implements Command {

        public void execute() {
            final StatusPanel status = StatusPanel.getInstance();
            String data = "m=" + URL.encodeComponent(mediaId) + "&t=" + URL.encodeComponent(taskId) + "&o=" + URL.encodeComponent(objType);
            RequestBuilder req = new RequestBuilder(RequestBuilder.POST, AppState.getInstance().getCommandURL("killTask"));
            req.setHeader("Content-Type", "application/x-www-form-urlencoded; UTF-8");
            try {
                req.sendRequest(data, new RequestCallback() {

                    public void onError(Request request, Throwable exception) {
                        status.setMessage(exception.getLocalizedMessage(), StatusPanel.MessageType.ERROR);
                    }

                    public void onResponseReceived(Request request, Response response) {
                        if (response.getText().equals("Success")) {
                            ActiveQueueTable.getInstance().reloadPage();
                            status.setMessage("Task cancelled!");
                        } else {
                            status.setMessage("Task cancellation failed!", StatusPanel.MessageType.ERROR);
                        }
                    }
                });
            } catch (RequestException e) {
                status.setMessage(e.getLocalizedMessage(), StatusPanel.MessageType.ERROR);
            }
            hide();
        }
    }

    private class DeleteRowCommand implements Command {

        public void execute() {
            final StatusPanel status = StatusPanel.getInstance();
            String data = "m=" + URL.encodeComponent(mediaId) + "&t=" + URL.encodeComponent(taskId) + "&o=" + URL.encodeComponent(objType);
            RequestBuilder req = new RequestBuilder(RequestBuilder.POST, AppState.getInstance().getCommandURL("delActiveQ"));
            req.setHeader("Content-Type", "application/x-www-form-urlencoded; UTF-8");
            try {
                req.sendRequest(data, new RequestCallback() {

                    public void onError(Request request, Throwable exception) {
                        status.setMessage(exception.getLocalizedMessage(), StatusPanel.MessageType.ERROR);
                    }

                    public void onResponseReceived(Request request, Response response) {
                        if (response.getText().equals("Success")) {
                            ActiveQueueTable.getInstance().reloadPage();
                            status.setMessage("Entry removed from table!");
                        } else {
                            status.setMessage("Entry removal failed!", StatusPanel.MessageType.ERROR);
                        }
                    }
                });
            } catch (RequestException e) {
                status.setMessage(e.getLocalizedMessage(), StatusPanel.MessageType.ERROR);
            }
            hide();
        }
    }

    private class EditPriorityCommand implements Command {

        public void execute() {
            hide();
            PriorityEditorPopup p = PriorityEditorPopup.getInstance(mediaId, taskId, null);
            p.show();
            p.selectValue();
        }
    }

    private class ClearGridCommand implements Command {

        public void execute() {
            hide();
            if (Window.confirm("Are you sure you want to delete ALL entries in this grid?")) {
                final StatusPanel status = StatusPanel.getInstance();
                RequestBuilder req = new RequestBuilder(RequestBuilder.POST, AppState.getInstance().getCommandURL("clearActiveQ"));
                req.setHeader("Content-Type", "application/x-www-form-urlencoded; UTF-8");
                try {
                    req.sendRequest(null, new RequestCallback() {

                        public void onError(Request request, Throwable exception) {
                            status.setMessage(exception.getLocalizedMessage(), StatusPanel.MessageType.ERROR);
                        }

                        public void onResponseReceived(Request request, Response response) {
                            if (response.getText().equals("Success")) {
                                ActiveQueueTable.getInstance().reloadPage();
                                status.setMessage("All entries removed");
                            } else {
                                status.setMessage("Failed to clear grid due to unknown error!", StatusPanel.MessageType.ERROR);
                            }
                        }
                    });
                } catch (RequestException e) {
                    status.setMessage(e.getLocalizedMessage(), StatusPanel.MessageType.ERROR);
                }
            }
        }
    }

    private MenuBar options;

    private ViewLogCommand viewLogCmd;

    private DeleteRowCommand delItemCmd;

    private ClearGridCommand delTblCmd;

    private EditPriorityCommand editPriorityCmd;

    private KillTaskCommand killTaskCmd;

    private String mediaId, taskId, title, objType;

    private MenuItem delItem, editPriItem, killItem;

    private ActiveQueueContextMenu() {
        super(true);
        viewLogCmd = new ViewLogCommand();
        delItemCmd = new DeleteRowCommand();
        delTblCmd = new ClearGridCommand();
        editPriorityCmd = new EditPriorityCommand();
        killTaskCmd = new KillTaskCommand();
        options = new MenuBar(true);
        options.addItem("View log", viewLogCmd);
        editPriItem = options.addItem("Edit priority", editPriorityCmd);
        delItem = options.addItem("Delete entry", delItemCmd);
        killItem = options.addItem("Cancel running task", killTaskCmd);
        options.addSeparator();
        options.addItem("Clear grid", delTblCmd);
        setWidget(options);
    }

    private void setView(String mediaId, String taskId, boolean canDelete, String title, boolean canKill, String objType) {
        this.mediaId = mediaId;
        this.taskId = taskId;
        this.title = title;
        this.objType = objType;
        if (canDelete) {
            delItem.setVisible(true);
            editPriItem.setVisible(true);
        } else {
            delItem.setVisible(false);
            editPriItem.setVisible(false);
        }
        if (canKill) killItem.setVisible(true); else killItem.setVisible(false);
    }
}
