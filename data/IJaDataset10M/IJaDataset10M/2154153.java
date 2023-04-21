package org.kablink.teaming.gwt.client.binderviews;

import java.util.Map;
import org.kablink.teaming.gwt.client.binderviews.ViewReady;
import org.kablink.teaming.gwt.client.binderviews.folderdata.ColumnWidth;
import org.kablink.teaming.gwt.client.util.BinderInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;

/**
 * Personal Workspaces (root binder) view.
 * 
 * @author drfoster@novell.com
 */
public class PersonalWorkspacesView extends DataTableFolderViewBase {

    private PersonalWorkspacesView(BinderInfo folderInfo, ViewReady viewReady) {
        super(folderInfo, viewReady, "vibe-personalWorkspacesDataTable");
    }

    /**
	 * Resets the columns as appropriate for the personal workspaces
	 * view.
	 * 
	 * Unless otherwise specified the widths default to be a percentage
	 * value.  Default sizes as per the JSP page.  See
	 * profile_list.jsp.
	 * 
	 * Overrides the DataTableFolderViewBase.adjustFixedColumnWidths() method.
	 * 
	 * @param columnWidths
	 */
    @Override
    protected void adjustFixedColumnWidths(Map<String, ColumnWidth> columnWidths) {
        columnWidths.put(ColumnWidth.COLUMN_FULL_NAME, new ColumnWidth(30));
        columnWidths.put(ColumnWidth.COLUMN_EMAIL_ADDRESS, new ColumnWidth(50));
        columnWidths.put(ColumnWidth.COLUMN_LOGIN_ID, new ColumnWidth(20));
    }

    /**
	 * Called from the base class to complete the construction of this
	 * personal workspaces view.
	 * 
	 * Implements the FolderViewBase.constructView() method.
	 */
    @Override
    public void constructView() {
        getFlowPanel().addStyleName("vibe-personalWorkspacesFlowPanel");
        viewReady();
        populateContent();
    }

    /**
	 * Loads the PersonalWorkspacesView split point and returns an
	 * instance of it via the callback.
	 * 
	 * @param folderInfo
	 * @param viewReady
	 * @param vClient
	 */
    public static void createAsync(final BinderInfo folderInfo, final ViewReady viewReady, final ViewClient vClient) {
        GWT.runAsync(PersonalWorkspacesView.class, new RunAsyncCallback() {

            @Override
            public void onSuccess() {
                PersonalWorkspacesView dfView = new PersonalWorkspacesView(folderInfo, viewReady);
                vClient.onSuccess(dfView);
            }

            @Override
            public void onFailure(Throwable reason) {
                Window.alert(m_messages.codeSplitFailure_PersonalWorkspacesView());
                vClient.onUnavailable();
            }
        });
    }

    /**
	 * Returns true for panels that are to be included and false
	 * otherwise.
	 * 
	 * Overrides the FolderViewBase.includePanel() method.
	 * 
	 * @param folderPanel
	 * 
	 * @return
	 */
    @Override
    protected boolean includePanel(FolderPanels folderPanel) {
        boolean reply;
        switch(folderPanel) {
            case BREADCRUMB:
            case FILTER:
                reply = false;
                break;
            default:
                reply = true;
                break;
        }
        return reply;
    }

    /**
	 * Returns true so that the PresenceColumn shows the profile entry
	 * dialog for presence when a user has no workspace.
	 *
	 * Overrides the DataTableFolderViewBase.showProfileEntryForPresenceWithNoWS()
	 * method.
	 * 
	 * @return
	 */
    @Override
    public boolean showProfileEntryForPresenceWithNoWS() {
        return true;
    }

    /**
	 * Called from the base class to reset the content of this
	 * personal workspaces view.
	 * 
	 * Implements the FolderViewBase.resetView() method.
	 */
    @Override
    public void resetView() {
        resetContent();
        populateContent();
    }

    /**
	 * Called from the base class to resize the content of this
	 * personal workspaces view.
	 * 
	 * Implements the FolderViewBase.resizeView() method.
	 */
    @Override
    public void resizeView() {
    }
}
