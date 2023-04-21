package org.systemsbiology.apps.gui.client.widget.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.systemsbiology.apps.gui.client.constants.OutputFileConstants;
import org.systemsbiology.apps.gui.client.controller.IController;
import org.systemsbiology.apps.gui.client.controller.IRequestErrorHandler;
import org.systemsbiology.apps.gui.client.controller.request.AccessListDeleteRequest;
import org.systemsbiology.apps.gui.client.controller.request.AccessListNewRequest;
import org.systemsbiology.apps.gui.client.data.Model;
import org.systemsbiology.apps.gui.client.data.access.IAccessModel;
import org.systemsbiology.apps.gui.client.util.FileUtils;
import org.systemsbiology.apps.gui.client.widget.general.GuiButton;
import org.systemsbiology.apps.gui.client.widget.general.OkCancelDialog;
import org.systemsbiology.apps.gui.domain.Access;
import org.systemsbiology.apps.gui.domain.TransitionListValidatorSetup;
import org.systemsbiology.apps.gui.domain.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * DialogBox for the user to enter a new project name.
 */
class ProjShareDialog extends OkCancelDialog implements IRequestErrorHandler {

    protected IController controller;

    private ProjectWidgetsMediator mediator;

    private final ScrollPanel mainPanel;

    private static final int rowOffset = 1;

    private List<ShareRow> shareRows;

    private ArrayList<User> addUserAccess = new ArrayList<User>();

    private ArrayList<User> removeUserAccess = new ArrayList<User>();

    FlexTable table = new FlexTable();

    public ProjShareDialog(IController controller, ProjectWidgetsMediator mediator) {
        super("Share settings for project: " + Model.getAccessModel().getAccess().getProject().getTitle());
        this.controller = controller;
        this.mediator = mediator;
        mainPanel = new ScrollPanel();
        table = new FlexTable();
        table.setWidth("100%");
        shareRows = new ArrayList<ShareRow>();
        initHeader();
        IAccessModel accModel = Model.getAccessModel();
        setUserList(accModel.getOtherUserList());
        if (accModel.getOtherUserList().size() > 8) {
            mainPanel.setHeight("200");
        }
        setAccessList(accModel.getOtherAccessList());
        mainPanel.add(table);
        setContent(mainPanel);
    }

    private void initHeader() {
        int col = 0;
        int row = 0;
        table.setWidget(row, col++, new Label("Share"));
        table.setWidget(row, col++, headerCellHTML("User ID"));
        table.setWidget(row, col++, headerCellHTML("First Name"));
        table.setWidget(row, col++, headerCellHTML("Last Name"));
        table.getRowFormatter().addStyleName(row, "footer");
    }

    private HTML headerCellHTML(String text) {
        HTML h = new HTML(text);
        h.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        return h;
    }

    /**
     * Returns the index of the given ShareRow object in the list. 
     * @param row
     */
    int getRowIndex(ShareRow row) {
        int i = shareRows.indexOf(row);
        if (i == -1) return -1; else return i + rowOffset;
    }

    private void setUserList(List<User> userList) {
        Iterator<User> it = userList.iterator();
        for (int i = rowOffset; it.hasNext(); i++) {
            addShareRow(i, ((User) it.next()));
        }
    }

    private void setAccessList(List<Access> accList) {
        Iterator<Access> it = accList.iterator();
        for (int i = 0; it.hasNext(); i++) {
            selectUserInList(((Access) it.next()).getUser());
        }
    }

    private void selectUserInList(User user) {
        for (int i = 0; i < shareRows.size(); i++) {
            if (shareRows.get(i).getUser().equals(user)) {
                shareRows.get(i).setSelected(true, i + rowOffset);
            }
        }
    }

    private void addShareRow(int rowIndex, User user) {
        ShareRow row = new ShareRow(this, user, table, rowIndex);
        this.shareRows.add(row);
    }

    protected boolean closeOnOkClicked() {
        if (!removeUserAccess.isEmpty()) controller.handleRequest(new AccessListDeleteRequest(removeUserAccess, Model.getAccessModel().getAccess().getProject()), ProjShareDialog.this);
        if (!addUserAccess.isEmpty()) controller.handleRequest(new AccessListNewRequest(addUserAccess, Model.getAccessModel().getAccess().getProject()), ProjShareDialog.this);
        return true;
    }

    protected void modifyAccess(User user, boolean isChecked) {
        if (isChecked == true) {
            if (removeUserAccess.contains(user)) {
                removeUserAccess.remove(user);
            } else {
                addUserAccess.add(user);
            }
        } else {
            if (addUserAccess.contains(user)) {
                addUserAccess.remove(user);
            } else {
                removeUserAccess.add(user);
            }
        }
    }

    public void handleRequestFailed(Throwable caught) {
        mediator.propagateException(caught);
    }
}
