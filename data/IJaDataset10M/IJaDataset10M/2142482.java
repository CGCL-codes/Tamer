package org.kablink.teaming.gwt.client.mainmenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.kablink.teaming.gwt.client.GwtTeaming;
import org.kablink.teaming.gwt.client.event.TeamingEvents;
import org.kablink.teaming.gwt.client.mainmenu.FolderOptionsDlg.FolderOptionsDlgClient;
import org.kablink.teaming.gwt.client.util.BinderInfo;
import org.kablink.teaming.gwt.client.util.ContextBinderProvider;
import org.kablink.teaming.gwt.client.util.GwtClientHelper;
import org.kablink.teaming.gwt.client.widgets.TagThisDlg;
import org.kablink.teaming.gwt.client.widgets.TagThisDlg.TagThisDlgClient;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;

/**
 * Class used for the Manage menu item popup.  
 * 
 * @author drfoster@novell.com
 */
public class ManageMenuPopup extends MenuBarPopupBase {

    private final String IDBASE = "manage_";

    private BinderInfo m_currentBinder;

    private List<ToolbarItem> m_actionsBucket;

    private List<ToolbarItem> m_configBucket;

    private List<ToolbarItem> m_ignoreBucket;

    private List<ToolbarItem> m_miscBucket;

    private List<ToolbarItem> m_teamAndEmailBucket;

    private List<ToolbarItem> m_toolbarItemList;

    private TeamManagementInfo m_tmi;

    private ToolbarItem m_brandingTBI;

    private ToolbarItem m_calendarImportTBI;

    private ToolbarItem m_commonActionsTBI;

    private ToolbarItem m_emailContributorsTBI;

    private ToolbarItem m_emailNotificationTBI;

    private ToolbarItem m_folderViewsTBI;

    private ToolbarItem m_shareThisTBI;

    private ToolbarItem m_trackBinderTBI;

    private ToolbarItem m_trackPersonTBI;

    private TagThisDlg m_tagThisDlg = null;

    private ManageMenuPopup(ContextBinderProvider binderProvider, String manageName) {
        super(binderProvider, manageName);
    }

    private boolean addNestedItemFromUrl(List<ToolbarItem> bucket, ToolbarItem tbi, String action) {
        return addNestedItemFromUrl(bucket, tbi, action, null);
    }

    private boolean addNestedItemFromUrl(List<ToolbarItem> bucket, ToolbarItem tbi, String action, String operation) {
        List<ToolbarItem> tbiList = ((null == tbi) ? null : tbi.getNestedItemsList());
        if ((null == tbiList) || tbiList.isEmpty()) {
            return false;
        }
        action = ("action=" + action.toLowerCase());
        if (null != operation) {
            operation = ("operation=" + operation.toLowerCase());
        }
        for (Iterator<ToolbarItem> tbiIT = tbiList.iterator(); tbiIT.hasNext(); ) {
            ToolbarItem nestedTBI = tbiIT.next();
            String url = nestedTBI.getUrl();
            if (!(GwtClientHelper.hasString(url))) {
                continue;
            }
            url = url.toLowerCase();
            if (0 < url.indexOf(action)) {
                if ((null == operation) || (0 < url.indexOf(operation))) {
                    tbiList.remove(nestedTBI);
                    bucket.add(nestedTBI);
                    return true;
                }
            }
        }
        return false;
    }

    private static void copyNestedToolbarItems(ToolbarItem tbiDest, ToolbarItem tbiSrc) {
        if ((null != tbiDest) && (null != tbiSrc)) {
            List<ToolbarItem> nestedSrcTBIList = tbiSrc.getNestedItemsList();
            if (null != nestedSrcTBIList) {
                for (Iterator<ToolbarItem> tbiIT = nestedSrcTBIList.iterator(); tbiIT.hasNext(); ) {
                    tbiDest.addNestedItem(tbiIT.next());
                }
            }
        }
    }

    private void fillBuckets() {
        ToolbarItem localTBI;
        m_actionsBucket = new ArrayList<ToolbarItem>();
        m_configBucket = new ArrayList<ToolbarItem>();
        m_ignoreBucket = new ArrayList<ToolbarItem>();
        m_miscBucket = new ArrayList<ToolbarItem>();
        m_teamAndEmailBucket = new ArrayList<ToolbarItem>();
        addNestedItemFromUrl(m_actionsBucket, m_commonActionsTBI, "add_binder", "add_folder");
        addNestedItemFromUrl(m_actionsBucket, m_commonActionsTBI, "add_binder", "add_subFolder");
        addNestedItemFromUrl(m_actionsBucket, m_commonActionsTBI, "add_binder", "add_workspace");
        addNestedItemFromUrl(m_actionsBucket, m_commonActionsTBI, "modify_binder", "delete");
        addNestedItemFromUrl(m_actionsBucket, m_commonActionsTBI, "modify_binder", "modify");
        addNestedItemFromUrl(m_actionsBucket, m_commonActionsTBI, "modify_binder", "copy");
        addNestedItemFromUrl(m_actionsBucket, m_commonActionsTBI, "modify_binder", "move");
        addNestedItemFromUrl(m_actionsBucket, m_commonActionsTBI, "export_import");
        addNestedItemFromUrl(m_actionsBucket, m_commonActionsTBI, "manage_binder_quota");
        addNestedItemFromUrl(m_actionsBucket, m_commonActionsTBI, "manage_definitions");
        addNestedItemFromUrl(m_actionsBucket, m_commonActionsTBI, "configure_configuration");
        if ((null != m_tmi) && m_tmi.isTeamManagementEnabled()) {
            if (m_tmi.isViewAllowed()) {
                localTBI = new ToolbarItem();
                localTBI.setName("viewTeam");
                localTBI.setTitle(m_messages.mainMenuManageViewTeam());
                localTBI.setTeamingEvent(TeamingEvents.VIEW_CURRENT_BINDER_TEAM_MEMBERS);
                m_teamAndEmailBucket.add(localTBI);
            }
            if (m_tmi.isManageAllowed()) {
                localTBI = new ToolbarItem();
                localTBI.setName("editTeam");
                localTBI.setTitle(m_messages.mainMenuManageEditTeam());
                localTBI.setUrl(m_tmi.getManageUrl());
                localTBI.addQualifier("popup", "true");
                localTBI.addQualifier("popupHeight", String.valueOf(TeamManagementInfo.POPUP_HEIGHT));
                localTBI.addQualifier("popupWidth", String.valueOf(TeamManagementInfo.POPUP_WIDTH));
                m_teamAndEmailBucket.add(localTBI);
            }
            if (m_tmi.isTeamMeetingAllowed()) {
                localTBI = new ToolbarItem();
                localTBI.setName("meetTeam");
                localTBI.setTitle(m_messages.mainMenuManageStartTeamConference());
                localTBI.setUrl(m_tmi.getTeamMeetingUrl());
                localTBI.addQualifier("popup", "true");
                localTBI.addQualifier("popupHeight", String.valueOf(TeamManagementInfo.POPUP_HEIGHT));
                localTBI.addQualifier("popupWidth", String.valueOf(TeamManagementInfo.POPUP_WIDTH));
                m_teamAndEmailBucket.add(localTBI);
            }
            if (m_tmi.isSendMailAllowed()) {
                localTBI = new ToolbarItem();
                localTBI.setName("mailTeam");
                localTBI.setTitle(m_messages.mainMenuManageEmailTeam());
                localTBI.setUrl(m_tmi.getSendMailUrl());
                localTBI.addQualifier("popup", "true");
                localTBI.addQualifier("popupHeight", String.valueOf(TeamManagementInfo.POPUP_HEIGHT));
                localTBI.addQualifier("popupWidth", String.valueOf(TeamManagementInfo.POPUP_WIDTH));
                m_teamAndEmailBucket.add(localTBI);
            }
        }
        if (null != m_emailContributorsTBI) m_teamAndEmailBucket.add(m_emailContributorsTBI);
        if (null != m_trackPersonTBI) m_miscBucket.add(m_trackPersonTBI);
        if (null != m_trackBinderTBI) m_miscBucket.add(m_trackBinderTBI);
        if (null != m_shareThisTBI) m_miscBucket.add(m_shareThisTBI);
        if (null != m_brandingTBI) {
            m_configBucket.add(m_brandingTBI);
        }
        addNestedItemFromUrl(m_configBucket, m_commonActionsTBI, "configure_definitions");
        addNestedItemFromUrl(m_configBucket, m_commonActionsTBI, "config_email");
        addNestedItemFromUrl(m_configBucket, m_commonActionsTBI, "configure_access_control");
        addNestedItemFromUrl(m_ignoreBucket, m_commonActionsTBI, "site_administration");
        addNestedItemFromUrl(m_ignoreBucket, m_commonActionsTBI, "binder_report");
    }

    private boolean hasNestedItems(ToolbarItem tbi, int atLeast) {
        return ((null == tbi) ? false : tbi.hasNestedToolbarItems(atLeast));
    }

    private boolean hasNestedItems(ToolbarItem tbi) {
        return hasNestedItems(tbi, 1);
    }

    /**
	 * Stores information about the currently selected binder.
	 * 
	 * Implements the MenuBarPopupBase.setCurrentBinder() abstract
	 * method.
	 * 
	 * @param binderInfo
	 */
    @Override
    public void setCurrentBinder(BinderInfo binderInfo) {
        m_currentBinder = binderInfo;
    }

    /**
	 * Stores team management information for use by the menu.
	 * 
	 * @param tmi
	 */
    public void setTeamManagementInfo(TeamManagementInfo tmi) {
        m_tmi = tmi;
    }

    /**
	 * Store information about the context based toolbar requirements
	 * via a List<ToolbarItem>.
	 * 
	 * Implements the MenuBarPopupBase.setToolbarItemList() abstract
	 * method.
	 * 
	 * @param toolbarItemList
	 */
    @Override
    public void setToolbarItemList(List<ToolbarItem> toolbarItemList) {
        m_toolbarItemList = toolbarItemList;
    }

    /**
	 * Called to determine if given the List<ToolbarItem>, should
	 * the menu be shown.  Returns true if it should be shown and false
	 * otherwise.
	 * 
	 * Implements the MenuBarPopupBase.shouldShowMenu() abstract
	 * method.
	 * 
	 * @return
	 */
    @Override
    public boolean shouldShowMenu() {
        ToolbarItem categoriesTBI;
        for (Iterator<ToolbarItem> tbiIT = m_toolbarItemList.iterator(); tbiIT.hasNext(); ) {
            ToolbarItem tbi = tbiIT.next();
            String tbName = tbi.getName();
            if (tbName.equalsIgnoreCase("ssFolderToolbar")) {
                ToolbarItem adminTBI = tbi.getNestedToolbarItem("administration");
                categoriesTBI = ((null == adminTBI) ? null : adminTBI.getNestedToolbarItem("categories"));
                if (null != categoriesTBI) {
                    m_commonActionsTBI = new ToolbarItem();
                    copyNestedToolbarItems(m_commonActionsTBI, categoriesTBI.getNestedToolbarItem(null));
                    copyNestedToolbarItems(m_commonActionsTBI, categoriesTBI.getNestedToolbarItem("addBinder"));
                    copyNestedToolbarItems(m_commonActionsTBI, categoriesTBI.getNestedToolbarItem("configuration"));
                    copyNestedToolbarItems(m_commonActionsTBI, categoriesTBI.getNestedToolbarItem("folders"));
                    copyNestedToolbarItems(m_commonActionsTBI, categoriesTBI.getNestedToolbarItem("workspace"));
                }
            } else if (tbName.equalsIgnoreCase("ssFolderViewsToolbar")) {
                ToolbarItem displayStylesTBI = tbi.getNestedToolbarItem("display_styles");
                categoriesTBI = ((null == displayStylesTBI) ? null : displayStylesTBI.getNestedToolbarItem("categories"));
                if (null != categoriesTBI) {
                    m_folderViewsTBI = categoriesTBI.getNestedToolbarItem("folderviews");
                }
            } else if (tbName.equalsIgnoreCase("ssEmailSubscriptionToolbar")) {
                m_emailNotificationTBI = tbi.getNestedToolbarItem("email");
            } else if (tbName.equalsIgnoreCase("ssCalendarImportToolbar")) {
                ToolbarItem calendarTBI = tbi.getNestedToolbarItem("calendar");
                if (null != calendarTBI) {
                    categoriesTBI = calendarTBI.getNestedToolbarItem("categories");
                    if (null != categoriesTBI) {
                        m_calendarImportTBI = categoriesTBI.getNestedToolbarItem("calendar");
                    }
                }
            } else if (tbName.equalsIgnoreCase("ssGwtMiscToolbar")) {
                m_brandingTBI = tbi.getNestedToolbarItem("branding");
                m_emailContributorsTBI = tbi.getNestedToolbarItem("sendEmail");
                m_shareThisTBI = tbi.getNestedToolbarItem("share");
                m_trackBinderTBI = tbi.getNestedToolbarItem("track");
                m_trackPersonTBI = tbi.getNestedToolbarItem("trackPerson");
            }
        }
        boolean reply = ((null != m_brandingTBI) || (null != m_emailContributorsTBI) || (null != m_emailNotificationTBI) || (null != m_shareThisTBI) || (null != m_trackBinderTBI) || (null != m_trackPersonTBI) || ((null != m_calendarImportTBI) && m_calendarImportTBI.hasNestedToolbarItems()) || ((null != m_commonActionsTBI) && m_commonActionsTBI.hasNestedToolbarItems()) || ((null != m_folderViewsTBI) && m_folderViewsTBI.hasNestedToolbarItems(2)));
        if (reply) {
            fillBuckets();
        }
        return reply;
    }

    private void showFolderOptions(final ToolbarItem folderViewsTBI, final ToolbarItem calendarImportTBI) {
        if ((!(hasNestedItems(folderViewsTBI, 2))) && (!(hasNestedItems(calendarImportTBI)))) {
            return;
        }
        final String foId = (IDBASE + "FolderOptions");
        MenuPopupAnchor mtA = new MenuPopupAnchor(foId, m_messages.mainMenuManageFolderOptions(), null, new Command() {

            @Override
            public void execute() {
                ScheduledCommand showDlg = new ScheduledCommand() {

                    @Override
                    public void execute() {
                        showFolderOptionsAsync(foId, folderViewsTBI, calendarImportTBI);
                    }
                };
                Scheduler.get().scheduleDeferred(showDlg);
            }
        });
        addContentMenuItem(mtA);
    }

    private void showFolderOptionsAsync(String foId, ToolbarItem folderViewsTBI, ToolbarItem calendarImportTBI) {
        FolderOptionsDlg.createAsync(false, true, getRelativeX(), getRelativeY(), m_currentBinder.getBinderId(), ((null == calendarImportTBI) ? null : calendarImportTBI.getNestedItemsList()), ((null == folderViewsTBI) ? null : folderViewsTBI.getNestedItemsList()), new FolderOptionsDlgClient() {

            @Override
            public void onUnavailable() {
            }

            @Override
            public void onSuccess(FolderOptionsDlg dlg) {
                dlg.addStyleName("folderOptionsDlg");
                dlg.show();
            }
        });
    }

    /**
	 * Completes construction of the menu.
	 * 
	 * Implements the MenuBarPopupBase.populateMenu() abstract method.
	 */
    @Override
    public void populateMenu() {
        if (!(hasContent())) {
            addContextMenuItemsFromList(IDBASE, m_actionsBucket);
            boolean hasTeamAndEmailSection = (!(m_teamAndEmailBucket.isEmpty()));
            if (hasTeamAndEmailSection && isSpacerNeeded()) {
                addSpacerMenuItem();
            }
            addContextMenuItemsFromList(IDBASE, m_teamAndEmailBucket);
            boolean hasMiscSection = (!(m_miscBucket.isEmpty()));
            if (!hasMiscSection) {
                hasMiscSection = m_currentBinder.isBinderWorkspace();
            }
            if (hasMiscSection && isSpacerNeeded()) {
                addSpacerMenuItem();
            }
            showTagThis();
            addContextMenuItemsFromList(IDBASE, m_miscBucket);
            boolean hasConfigSection = (!(m_configBucket.isEmpty()));
            if (hasConfigSection && isSpacerNeeded()) {
                addSpacerMenuItem();
            }
            addContextMenuItemsFromList(IDBASE, m_configBucket);
            addContextMenuItem(IDBASE, m_emailNotificationTBI);
            showFolderOptions(m_folderViewsTBI, m_calendarImportTBI);
            boolean hasLeftOversSection = ((null != m_commonActionsTBI) && m_commonActionsTBI.hasNestedToolbarItems());
            if (hasLeftOversSection && isSpacerNeeded()) {
                addSpacerMenuItem();
            }
            addNestedContextMenuItems(IDBASE, m_commonActionsTBI);
        }
    }

    private void showTagThis() {
        String menuText;
        final String dlgCaption;
        if (m_currentBinder.isBinderFolder()) {
            menuText = m_messages.mainMenuManageTagThisFolder();
            dlgCaption = m_messages.mainMenuTagThisDlgHeaderFolder();
        } else if (m_currentBinder.isBinderWorkspace()) {
            switch(m_currentBinder.getWorkspaceType()) {
                case PROFILE_ROOT:
                case NOT_A_WORKSPACE:
                    return;
            }
            menuText = m_messages.mainMenuManageTagThisWorkspace();
            dlgCaption = m_messages.mainMenuTagThisDlgHeaderWorkspace();
        } else {
            return;
        }
        final String menuId = (IDBASE + "TagThis");
        MenuPopupAnchor mtA = new MenuPopupAnchor(menuId, menuText, null, new Command() {

            @Override
            public void execute() {
                ScheduledCommand showDlg = new ScheduledCommand() {

                    @Override
                    public void execute() {
                        showTagThisAsync(menuId, dlgCaption);
                    }
                };
                Scheduler.get().scheduleDeferred(showDlg);
            }
        });
        addContentMenuItem(mtA);
    }

    private void showTagThisAsync(String menuId, String dlgCaption) {
        if (null == m_tagThisDlg) {
            TagThisDlg.createAsync(false, true, null, getRelativeX(), getRelativeY(), dlgCaption, new TagThisDlgClient() {

                @Override
                public void onUnavailable() {
                }

                @Override
                public void onSuccess(TagThisDlg dlg) {
                    m_tagThisDlg = dlg;
                    showTagThisNow();
                }
            });
        } else {
            showTagThisNow();
        }
    }

    private void showTagThisNow() {
        TagThisDlg.initAndShow(m_tagThisDlg, m_currentBinder.getBinderId(), m_currentBinder.getBinderTitle(), m_currentBinder.getBinderType());
    }

    /**
	 * Callback interface to interact with the manage menu popup
	 * asynchronously after it loads. 
	 */
    public interface ManageMenuPopupClient {

        void onSuccess(ManageMenuPopup mmp);

        void onUnavailable();
    }

    /**
	 * Loads the ManageMenuPopup split point and returns an
	 * instance of it via the callback.
	 *
	 * @param binderProvider
	 * @param name
	 * @param mmpClient
	 */
    public static void createAsync(final ContextBinderProvider binderProvider, final String name, final ManageMenuPopupClient mmpClient) {
        GWT.runAsync(ManageMenuPopup.class, new RunAsyncCallback() {

            @Override
            public void onSuccess() {
                ManageMenuPopup mmp = new ManageMenuPopup(binderProvider, name);
                mmpClient.onSuccess(mmp);
            }

            @Override
            public void onFailure(Throwable reason) {
                Window.alert(GwtTeaming.getMessages().codeSplitFailure_ManageMenuPopup());
                mmpClient.onUnavailable();
            }
        });
    }
}
