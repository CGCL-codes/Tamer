package org.kablink.teaming.gwt.server.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kablink.teaming.ObjectKeys;
import org.kablink.teaming.context.request.RequestContextHolder;
import org.kablink.teaming.domain.Binder;
import org.kablink.teaming.domain.CustomAttribute;
import org.kablink.teaming.domain.EntityIdentifier;
import org.kablink.teaming.domain.EntityIdentifier.EntityType;
import org.kablink.teaming.domain.Definition;
import org.kablink.teaming.domain.Folder;
import org.kablink.teaming.domain.ProfileBinder;
import org.kablink.teaming.domain.SimpleName;
import org.kablink.teaming.domain.SimpleName.SimpleNamePK;
import org.kablink.teaming.domain.TemplateBinder;
import org.kablink.teaming.domain.User;
import org.kablink.teaming.domain.UserProperties;
import org.kablink.teaming.domain.Workspace;
import org.kablink.teaming.gwt.client.GwtTeamingException;
import org.kablink.teaming.gwt.client.event.InvokeSendEmailToTeamEvent;
import org.kablink.teaming.gwt.client.event.TeamingEvents;
import org.kablink.teaming.gwt.client.mainmenu.RecentPlaceInfo;
import org.kablink.teaming.gwt.client.mainmenu.TeamManagementInfo;
import org.kablink.teaming.gwt.client.mainmenu.ToolbarItem;
import org.kablink.teaming.gwt.client.mainmenu.ToolbarItem.NameValuePair;
import org.kablink.teaming.gwt.client.rpc.shared.GetFolderToolbarItemsRpcResponseData;
import org.kablink.teaming.gwt.client.rpc.shared.StringRpcResponseData;
import org.kablink.teaming.gwt.client.util.BinderInfo;
import org.kablink.teaming.module.admin.AdminModule;
import org.kablink.teaming.module.admin.AdminModule.AdminOperation;
import org.kablink.teaming.module.binder.BinderModule;
import org.kablink.teaming.module.binder.BinderModule.BinderOperation;
import org.kablink.teaming.module.definition.DefinitionUtils;
import org.kablink.teaming.module.folder.FolderModule;
import org.kablink.teaming.module.folder.FolderModule.FolderOperation;
import org.kablink.teaming.module.license.LicenseChecker;
import org.kablink.teaming.module.profile.ProfileModule;
import org.kablink.teaming.module.profile.ProfileModule.ProfileOperation;
import org.kablink.teaming.module.template.TemplateModule;
import org.kablink.teaming.portletadapter.AdaptedPortletURL;
import org.kablink.teaming.ssfs.util.SsfsUtil;
import org.kablink.teaming.util.AllModulesInjected;
import org.kablink.teaming.util.NLT;
import org.kablink.teaming.util.SPropsUtil;
import org.kablink.teaming.util.SimpleProfiler;
import org.kablink.teaming.web.WebKeys;
import org.kablink.teaming.web.util.BinderHelper;
import org.kablink.teaming.web.util.DefinitionHelper;
import org.kablink.teaming.web.util.GwtUIHelper;
import org.kablink.teaming.web.util.ListFolderHelper;
import org.kablink.teaming.web.util.MiscUtil;
import org.kablink.teaming.web.util.PermaLinkUtil;
import org.kablink.teaming.web.util.Tabs;
import org.kablink.teaming.web.util.WebUrlUtil;
import org.kablink.teaming.web.util.GwtUIHelper.TrackInfo;
import org.kablink.util.BrowserSniffer;
import org.kablink.util.search.Constants;

/**
 * Helper methods for the GWT UI server code that services menu bar
 * requests.
 *
 * @author drfoster@novell.com
 */
public class GwtMenuHelper {

    protected static Log m_logger = LogFactory.getLog(GwtMenuHelper.class);

    private static final String ABOUT = "about";

    private static final String ACCESS_CONTROL = "accessControl";

    private static final String ADD_BINDER = "addBinder";

    private static final String ADD_FOLDER = "addFolder";

    private static final String ADD_WORKSPACE = "addWorkspace";

    private static final String ADMINISTRATION = "administration";

    private static final String BRANDING = "branding";

    private static final String CALENDAR = "calendar";

    private static final String CATEGORIES = "categories";

    private static final String CLIPBOARD = "clipboard";

    private static final String CONFIGURATION = "configuration";

    private static final String CONFIGURE_COLUMNS = "configureColumns";

    private static final String CONFIGURE_DEFINITIONS = "configureDefinitions";

    private static final String CONFIGURE_EMAIL = "configEmail";

    private static final String COPY = "copy";

    private static final String DELETE = "delete";

    private static final String DISPLAY_STYLES = "display_styles";

    private static final String EMAIL = "email";

    private static final String FOLDER_VIEWS = "folderViews";

    private static final String ICALENDAR = "iCalendar";

    private static final String IMPORT_EXPORT = "importExport";

    private static final String MANAGE_DEFINITIONS = "manageDefinitions";

    private static final String MANAGE_TEMPLATES = "manageTemplates";

    private static final String MANUAL_SYNC = "manualSync";

    private static final String MOBILE_UI = "mobileUI";

    private static final String MODIFY = "modify";

    private static final String MOVE = "move";

    private static final String PERMALINK = "permalink";

    private static final String REPORTS = "reports";

    private static final String SCHEDULE_SYNC = "scheduleSync";

    private static final String SEND_EMAIL = "sendEmail";

    private static final String SHARE = "share";

    private static final String SIMPLE_NAMES = "simpleNames";

    private static final String SS_FORUM = "ss_forum";

    private static final String SUBSCRIBE_ATOM = "subscribeAtom";

    private static final String SUBSCRIBE_RSS = "subscribeRSS";

    private static final String TRASH = "trash";

    private static final String UNSEEN = "unseen";

    private static final String VIEW_AS_WEBDAV = "viewaswebdav";

    private static final String WEBDAVURL = "webdavUrl";

    private static final String WHATS_NEW = "whatsnew";

    private static final String WHO_HAS_ACCESS = "whohasaccess";

    private GwtMenuHelper() {
    }

    private static void buildFolderMenuItems(AllModulesInjected bs, HttpServletRequest request, Folder folder, List<ToolbarItem> tbiList) {
        tbiList.add(constructFolderItems(WebKeys.FOLDER_TOOLBAR, bs, request, folder, EntityType.folder));
        tbiList.add(constructFolderViewsItems(WebKeys.FOLDER_VIEWS_TOOLBAR, bs, request, folder));
        tbiList.add(constructCalendarImportItems(WebKeys.CALENDAR_IMPORT_TOOLBAR, bs, request, folder));
        tbiList.add(constructWhatsNewItems(WebKeys.WHATS_NEW_TOOLBAR, bs, request, folder, EntityType.folder));
        tbiList.add(constructEmailSubscriptionItems(WebKeys.EMAIL_SUBSCRIPTION_TOOLBAR, bs, request, folder));
    }

    private static void buildMiscMenuItems(AllModulesInjected bs, HttpServletRequest request, Binder binder, EntityType binderType, List<ToolbarItem> tbiList) {
        ToolbarItem miscTBI = new ToolbarItem(WebKeys.GWT_MISC_TOOLBAR);
        tbiList.add(miscTBI);
        miscTBI.addNestedItem(constructAboutItem());
        if ((null != binder) && (!(GwtServerHelper.getCurrentUser().isShared()))) {
            miscTBI.addNestedItem(constructEditBrandingItem(bs, binder, binderType));
            if (EntityIdentifier.EntityType.profiles != binder.getEntityType()) {
                miscTBI.addNestedItem(constructClipboardItem());
                miscTBI.addNestedItem(constructSendEmailToItem(request, binder));
                miscTBI.addNestedItem(constructShareBinderItem(request, binder));
                miscTBI.addNestedItem(constructMobileUiItem(request, binder));
                miscTBI.addNestedItems(constructTrackBinderItem(bs, binder));
                miscTBI.addNestedItem(constructTrashItem(request, binder));
            }
        }
    }

    private static void buildProfilesMenuItems(AllModulesInjected bs, HttpServletRequest request, ProfileBinder pb, List<ToolbarItem> tbiList) {
        tbiList.add(constructFolderItems(WebKeys.FOLDER_TOOLBAR, bs, request, pb, EntityType.profiles));
    }

    private static void buildWorkspaceMenuItems(AllModulesInjected bs, HttpServletRequest request, Workspace ws, List<ToolbarItem> tbiList) {
        tbiList.add(constructFolderItems(WebKeys.FOLDER_TOOLBAR, bs, request, ws, EntityType.workspace));
        tbiList.add(constructWhatsNewItems(WebKeys.WHATS_NEW_TOOLBAR, bs, request, ws, EntityType.workspace));
    }

    private static ToolbarItem constructAboutItem() {
        ToolbarItem aboutTBI = new ToolbarItem(ABOUT);
        markTBITitle(aboutTBI, "misc.about");
        markTBIEvent(aboutTBI, TeamingEvents.INVOKE_ABOUT);
        return aboutTBI;
    }

    private static ToolbarItem constructCalendarImportItems(String tbKey, AllModulesInjected bs, HttpServletRequest request, Folder folder) {
        ToolbarItem reply = new ToolbarItem(tbKey);
        boolean isCalendar = BinderHelper.isBinderCalendar(folder);
        boolean isTask = ((!isCalendar) && BinderHelper.isBinderTask(folder));
        if ((isCalendar || isTask) && bs.getFolderModule().testAccess(folder, FolderOperation.addEntry)) {
            ToolbarItem calTBI = new ToolbarItem(CALENDAR);
            ToolbarItem catTBI = new ToolbarItem(CATEGORIES);
            ToolbarItem calTBI2 = new ToolbarItem(CALENDAR);
            reply.addNestedItem(calTBI);
            calTBI.addNestedItem(catTBI);
            catTBI.addNestedItem(calTBI2);
            String importFromFile;
            String importByURL;
            String importType;
            if (isCalendar) {
                importFromFile = "toolbar.menu.calendarImport.fromFile";
                importByURL = "toolbar.menu.calendarImport.byURL";
                importType = "calendar";
            } else {
                importFromFile = "toolbar.menu.taskImport.fromFile";
                importByURL = "toolbar.menu.taskImport.byURL";
                importType = "task";
            }
            ToolbarItem importTBI = new ToolbarItem(importType + ".File");
            markTBITitle(importTBI, importFromFile);
            markTBIEvent(importTBI, TeamingEvents.INVOKE_IMPORT_ICAL_FILE);
            calTBI2.addNestedItem(importTBI);
            importTBI = new ToolbarItem(importType + ".Url");
            markTBITitle(importTBI, importByURL);
            markTBIEvent(importTBI, TeamingEvents.INVOKE_IMPORT_ICAL_URL);
            calTBI2.addNestedItem(importTBI);
        }
        return reply;
    }

    private static ToolbarItem constructClipboardItem() {
        ToolbarItem cbTBI = new ToolbarItem(CLIPBOARD);
        markTBITitle(cbTBI, "toolbar.menu.clipboard");
        markTBIEvent(cbTBI, TeamingEvents.INVOKE_CLIPBOARD);
        return cbTBI;
    }

    private static ToolbarItem constructEditBrandingItem(AllModulesInjected bs, Binder binder, EntityType binderType) {
        if (bs.getBinderModule().testAccess(binder, BinderOperation.modifyBinder)) {
            String menuKey = "toolbar.menu.brand.";
            if (EntityType.workspace == binderType) menuKey += "workspace"; else if (EntityType.folder == binderType) menuKey += "folder"; else if (EntityType.profiles == binderType) menuKey += "workspace"; else return null;
            ToolbarItem ebTBI = new ToolbarItem(BRANDING);
            markTBITitle(ebTBI, menuKey);
            markTBIEvent(ebTBI, TeamingEvents.EDIT_CURRENT_BINDER_BRANDING);
            return ebTBI;
        }
        return null;
    }

    private static ToolbarItem constructEmailSubscriptionItems(String tbKey, AllModulesInjected bs, HttpServletRequest request, Folder folder) {
        ToolbarItem reply = new ToolbarItem(tbKey);
        if (!(GwtServerHelper.getCurrentUser().isShared())) {
            ToolbarItem emailTBI = new ToolbarItem(EMAIL);
            markTBITitle(emailTBI, "toolbar.menu.subscribeToFolder");
            markTBIHint(emailTBI, "toolbar.menu.title.emailSubscriptions");
            markTBIEvent(emailTBI, TeamingEvents.INVOKE_EMAIL_NOTIFICATION);
            reply.addNestedItem(emailTBI);
        }
        return reply;
    }

    private static void constructEntryDropBoxItem(ToolbarItem entryToolbar, AllModulesInjected bs, HttpServletRequest request, String viewType, Folder folder) {
        ToolbarItem dropBoxTBI = new ToolbarItem("dropBox");
        markTBITitle(dropBoxTBI, "toolbar.menu.dropBox.dialog");
        markTBIEvent(dropBoxTBI, TeamingEvents.INVOKE_DROPBOX);
        entryToolbar.addNestedItem(dropBoxTBI);
    }

    @SuppressWarnings("unchecked")
    private static void constructEntryAddItems(ToolbarItem entryToolbar, AllModulesInjected bs, HttpServletRequest request, String viewType, Folder folder) {
        List defaultEntryDefinitions = folder.getEntryDefinitions();
        int defaultEntryDefs = ((null == defaultEntryDefinitions) ? 0 : defaultEntryDefinitions.size());
        boolean hasVT = MiscUtil.hasString(viewType);
        AdaptedPortletURL url;
        if ((!(folder.isMirrored())) || isFolderWritableMirrored(folder)) {
            if (1 < defaultEntryDefs) {
                ToolbarItem addTBI = new ToolbarItem("1_add");
                markTBITitle(addTBI, "toolbar.new");
                entryToolbar.addNestedItem(addTBI);
                int count = 1;
                int defaultEntryDefIndex = ListFolderHelper.getDefaultFolderEntryDefinitionIndex(RequestContextHolder.getRequestContext().getUser().getId(), bs.getProfileModule(), folder, defaultEntryDefinitions);
                Map<String, Boolean> usedTitles = new HashMap<String, Boolean>();
                for (int i = 0; i < defaultEntryDefinitions.size(); i += 1) {
                    Definition def = ((Definition) defaultEntryDefinitions.get(i));
                    url = createActionUrl(request);
                    url.setParameter(WebKeys.ACTION, WebKeys.ACTION_ADD_FOLDER_ENTRY);
                    url.setParameter(WebKeys.URL_BINDER_ID, folder.getId().toString());
                    url.setParameter(WebKeys.URL_ENTRY_TYPE, def.getId());
                    String title = NLT.getDef(def.getTitle());
                    if (null != usedTitles.get("title")) {
                        title = (title + " (" + String.valueOf(count++) + ")");
                    }
                    ToolbarItem entriesTBI = new ToolbarItem("entries");
                    markTBITitleRes(entriesTBI, title);
                    markTBIPopup(entriesTBI);
                    markTBIUrl(entriesTBI, url);
                    if (i == defaultEntryDefIndex) {
                        markTBIDefault(entriesTBI);
                    }
                    addTBI.addNestedItem(entriesTBI);
                }
            } else if ((1 == defaultEntryDefs) && ((!hasVT) || (!(viewType.equals(Definition.VIEW_STYLE_GUESTBOOK))))) {
                Definition def = (Definition) defaultEntryDefinitions.get(0);
                url = createActionUrl(request);
                url.setParameter(WebKeys.ACTION, WebKeys.ACTION_ADD_FOLDER_ENTRY);
                url.setParameter(WebKeys.URL_BINDER_ID, folder.getId().toString());
                url.setParameter(WebKeys.URL_ENTRY_TYPE, def.getId());
                ToolbarItem addTBI = new ToolbarItem("1_add");
                markTBITitle(addTBI, "toolbar.new");
                markTBIHighlight(addTBI);
                markTBIPopup(addTBI);
                markTBIUrl(addTBI, url);
                entryToolbar.addNestedItem(addTBI);
            }
        }
        if (hasVT) {
            BinderModule bm = bs.getBinderModule();
            TemplateModule tm = bs.getTemplateModule();
            if (viewType.equals(Definition.VIEW_STYLE_BLOG)) {
                if (bm.testAccess(folder, BinderOperation.addFolder)) {
                    TemplateBinder blogTemplate = tm.getTemplateByName(ObjectKeys.DEFAULT_TEMPLATE_NAME_BLOG);
                    if (blogTemplate != null) {
                        url = createActionUrl(request);
                        url.setParameter(WebKeys.ACTION, WebKeys.ACTION_ADD_BINDER);
                        url.setParameter(WebKeys.URL_BINDER_ID, getFolderSetFolderId(bs, folder, viewType).toString());
                        url.setParameter(WebKeys.URL_TEMPLATE_NAME, ObjectKeys.DEFAULT_TEMPLATE_NAME_BLOG);
                        ToolbarItem addTBI = new ToolbarItem("1_add_folder");
                        markTBITitle(addTBI, "toolbar.menu.add_blog_folder");
                        markTBIPopup(addTBI);
                        markTBIUrl(addTBI, url);
                        entryToolbar.addNestedItem(addTBI);
                    }
                }
            } else if (viewType.equals(Definition.VIEW_STYLE_PHOTO_ALBUM)) {
                if (bm.testAccess(folder, BinderOperation.addFolder)) {
                    TemplateBinder photoTemplate = tm.getTemplateByName(ObjectKeys.DEFAULT_TEMPLATE_NAME_PHOTO);
                    if (photoTemplate != null) {
                        url = createActionUrl(request);
                        url.setParameter(WebKeys.ACTION, WebKeys.ACTION_ADD_BINDER);
                        url.setParameter(WebKeys.URL_BINDER_ID, getFolderSetFolderId(bs, folder, viewType).toString());
                        url.setParameter(WebKeys.URL_TEMPLATE_NAME, ObjectKeys.DEFAULT_TEMPLATE_NAME_PHOTO);
                        ToolbarItem addTBI = new ToolbarItem("1_add_folder");
                        markTBITitle(addTBI, "toolbar.menu.add_photo_album_folder");
                        markTBIPopup(addTBI);
                        markTBIUrl(addTBI, url);
                        entryToolbar.addNestedItem(addTBI);
                    }
                }
            } else if (viewType.equals(Definition.VIEW_STYLE_WIKI)) {
                if (bm.testAccess(folder, BinderOperation.addFolder)) {
                    TemplateBinder wikiTemplate = tm.getTemplateByName(ObjectKeys.DEFAULT_TEMPLATE_NAME_WIKI);
                    if (wikiTemplate != null) {
                        url = createActionUrl(request);
                        url.setParameter(WebKeys.ACTION, WebKeys.ACTION_ADD_BINDER);
                        url.setParameter(WebKeys.URL_BINDER_ID, getFolderSetFolderId(bs, folder, viewType).toString());
                        url.setParameter(WebKeys.URL_TEMPLATE_NAME, ObjectKeys.DEFAULT_TEMPLATE_NAME_WIKI);
                        ToolbarItem addTBI = new ToolbarItem("1_add_folder");
                        markTBITitle(addTBI, "toolbar.menu.add_wiki_folder");
                        markTBIPopup(addTBI);
                        markTBIUrl(addTBI, url);
                        entryToolbar.addNestedItem(addTBI);
                    }
                }
            }
        }
    }

    private static ToolbarItem constructEntryConfigureColumsItem(Binder binder) {
        String viewType = DefinitionUtils.getViewType(binder);
        if (MiscUtil.hasString(viewType)) {
            if (viewType.equalsIgnoreCase("folder") || viewType.equalsIgnoreCase("table") || viewType.equalsIgnoreCase("file")) {
                ToolbarItem ccTBI = new ToolbarItem(CONFIGURE_COLUMNS);
                markTBITitle(ccTBI, "misc.configureColumns");
                markTBIEvent(ccTBI, TeamingEvents.INVOKE_CONFIGURE_COLUMNS);
                return ccTBI;
            }
        }
        return null;
    }

    private static void constructEntryDeleteItems(ToolbarItem entryToolbar, AllModulesInjected bs, HttpServletRequest request, String viewType, Folder folder) {
        if (MiscUtil.hasString(viewType)) {
            BinderModule bm = bs.getBinderModule();
            if (folderSupportsDeleteAndPurge(folder, viewType) && (!(folder.isMirrored()))) {
                if (bm.testAccess(folder, BinderOperation.deleteEntries)) {
                    ToolbarItem deleteTBI = new ToolbarItem("1_deleteSelected");
                    markTBITitle(deleteTBI, "toolbar.delete");
                    markTBIEvent(deleteTBI, TeamingEvents.DELETE_SELECTED_ENTRIES);
                    entryToolbar.addNestedItem(deleteTBI);
                }
            }
        }
    }

    private static void constructEntryMoreItems(ToolbarItem entryToolbar, AllModulesInjected bs, HttpServletRequest request, Long folderId, String viewType, Folder folder) {
        User user = GwtServerHelper.getCurrentUser();
        boolean isGuest = ObjectKeys.GUEST_USER_INTERNALID.equals(user.getInternalId());
        ToolbarItem moreTBI = new ToolbarItem("1_more");
        markTBITitle(moreTBI, "toolbar.more");
        ToolbarItem tbi = new ToolbarItem("1_copySelected");
        markTBITitle(tbi, "toolbar.copy");
        markTBIEvent(tbi, TeamingEvents.COPY_SELECTED_ENTRIES);
        moreTBI.addNestedItem(tbi);
        if (!isGuest) {
            tbi = new ToolbarItem("1_shareSelected");
            markTBITitle(tbi, "toolbar.shareSelected");
            markTBIEvent(tbi, TeamingEvents.SHARE_SELECTED_ENTRIES);
            moreTBI.addNestedItem(tbi);
        }
        tbi = new ToolbarItem("1_moveSelected");
        markTBITitle(tbi, "toolbar.move");
        markTBIEvent(tbi, TeamingEvents.MOVE_SELECTED_ENTRIES);
        moreTBI.addNestedItem(tbi);
        if (MiscUtil.hasString(viewType)) {
            BinderModule bm = bs.getBinderModule();
            if (folderSupportsDeleteAndPurge(folder, viewType)) {
                if (bm.testAccess(folder, BinderOperation.deleteEntries)) {
                    tbi = new ToolbarItem("1_purgeSelected");
                    markTBITitle(tbi, "toolbar.purge");
                    markTBIEvent(tbi, TeamingEvents.PURGE_SELECTED_ENTRIES);
                    moreTBI.addNestedItem(tbi);
                }
            }
        }
        tbi = new ToolbarItem("1_lockSelected");
        markTBITitle(tbi, "toolbar.lock");
        markTBIEvent(tbi, TeamingEvents.LOCK_SELECTED_ENTRIES);
        moreTBI.addNestedItem(tbi);
        tbi = new ToolbarItem("1_unlockSelected");
        markTBITitle(tbi, "toolbar.unlock");
        markTBIEvent(tbi, TeamingEvents.UNLOCK_SELECTED_ENTRIES);
        moreTBI.addNestedItem(tbi);
        if (!isGuest) {
            tbi = new ToolbarItem("1_markReadSelected");
            markTBITitle(tbi, "toolbar.markRead");
            markTBIEvent(tbi, TeamingEvents.MARK_READ_SELECTED_ENTRIES);
            moreTBI.addNestedItem(tbi);
        }
        moreTBI.addNestedItem(ToolbarItem.constructSeparatorTBI());
        tbi = new ToolbarItem("1_changeEntryTypeSelected");
        markTBITitle(tbi, "toolbar.changeEntryType");
        markTBIEvent(tbi, TeamingEvents.CHANGE_ENTRY_TYPE_SELECTED_ENTRIES);
        moreTBI.addNestedItem(tbi);
        if (!isGuest) {
            tbi = new ToolbarItem("1_subscribeSelected");
            markTBITitle(tbi, "toolbar.menu.subscribeToEntrySelected");
            markTBIEvent(tbi, TeamingEvents.SUBSCRIBE_SELECTED_ENTRIES);
            moreTBI.addNestedItem(tbi);
        }
        if (!(moreTBI.getNestedItemsList().isEmpty())) {
            entryToolbar.addNestedItem(moreTBI);
        }
    }

    @SuppressWarnings("unchecked")
    private static void constructEntryProfilesRootWSItems(ToolbarItem entryToolbar, AllModulesInjected bs, HttpServletRequest request, Workspace ws) {
        ProfileModule pm = bs.getProfileModule();
        if (pm.testAccess(((ProfileBinder) ws), ProfileOperation.addEntry)) {
            List defaultEntryDefinitions = ws.getEntryDefinitions();
            if ((null != defaultEntryDefinitions) && (!(defaultEntryDefinitions.isEmpty()))) {
                Definition def = ((Definition) defaultEntryDefinitions.get(0));
                AdaptedPortletURL url = createActionUrl(request);
                url.setParameter(WebKeys.ACTION, WebKeys.ACTION_ADD_PROFILE_ENTRY);
                url.setParameter(WebKeys.URL_BINDER_ID, ws.getId().toString());
                url.setParameter(WebKeys.URL_ENTRY_TYPE, def.getId());
                ToolbarItem addUserTBI = new ToolbarItem("1_add");
                markTBITitle(addUserTBI, "toolbar.new");
                markTBIPopup(addUserTBI);
                markTBIHighlight(addUserTBI);
                markTBIUrl(addUserTBI, url);
                entryToolbar.addNestedItem(addUserTBI);
            }
        }
        ToolbarItem tbi;
        ToolbarItem moreTBI = new ToolbarItem("1_more");
        markTBITitle(moreTBI, "toolbar.more");
        if (pm.testAccess(((ProfileBinder) ws), ProfileOperation.manageEntries)) {
            tbi = new ToolbarItem("1_disableSelected");
            markTBITitle(tbi, "toolbar.disable");
            markTBIEvent(tbi, TeamingEvents.DISABLE_SELECTED_USERS);
            moreTBI.addNestedItem(tbi);
            tbi = new ToolbarItem("1_enableSelected");
            markTBITitle(tbi, "toolbar.enable");
            markTBIEvent(tbi, TeamingEvents.ENABLE_SELECTED_USERS);
            moreTBI.addNestedItem(tbi);
        }
        BinderModule bm = bs.getBinderModule();
        boolean canTrash = bm.testAccess(ws, BinderOperation.preDeleteBinder);
        if (canTrash) {
            if (!(moreTBI.getNestedItemsList().isEmpty())) {
                moreTBI.addNestedItem(ToolbarItem.constructSeparatorTBI());
            }
            tbi = new ToolbarItem("1_deletedSelectedWS");
            markTBITitle(tbi, "toolbar.delete.workspaces");
            markTBIEvent(tbi, TeamingEvents.DELETE_SELECTED_USER_WORKSPACES);
            moreTBI.addNestedItem(tbi);
        }
        if (bm.testAccess(ws, BinderOperation.deleteBinder)) {
            if ((!canTrash) && (!(moreTBI.getNestedItemsList().isEmpty()))) {
                moreTBI.addNestedItem(ToolbarItem.constructSeparatorTBI());
            }
            tbi = new ToolbarItem("1_purgeSelectedWS");
            markTBITitle(tbi, "toolbar.purge.workspaces");
            markTBIEvent(tbi, TeamingEvents.PURGE_SELECTED_USER_WORKSPACES);
            moreTBI.addNestedItem(tbi);
            tbi = new ToolbarItem("1_purgeSelectedUsers");
            markTBITitle(tbi, "toolbar.purge.users");
            markTBIEvent(tbi, TeamingEvents.PURGE_SELECTED_USERS);
            moreTBI.addNestedItem(tbi);
        }
        if (!(moreTBI.getNestedItemsList().isEmpty())) {
            entryToolbar.addNestedItem(moreTBI);
        }
    }

    private static void constructEntrySignTheGuestbookItem(ToolbarItem entryToolbar, AllModulesInjected bs, HttpServletRequest request, Folder folder) {
        ToolbarItem signGuestbookTBI = new ToolbarItem("signGuestbool");
        markTBITitle(signGuestbookTBI, "guestbook.addEntry");
        markTBIEvent(signGuestbookTBI, TeamingEvents.INVOKE_SIGN_GUESTBOOK);
        entryToolbar.addNestedItem(signGuestbookTBI);
    }

    private static void constructEntrySortByItems(ToolbarItem entryToolbar, AllModulesInjected bs, HttpServletRequest request, String viewType, Folder folder) {
        Long folderId = folder.getId();
        UserProperties userFolderProperties = bs.getProfileModule().getUserProperties(GwtServerHelper.getCurrentUser().getId(), folderId);
        String searchSortBy = ((String) userFolderProperties.getProperty(ObjectKeys.SEARCH_SORT_BY));
        if (searchSortBy == null) searchSortBy = "";
        String[] sortOptions;
        boolean isPhotoAlbum = viewType.equals(Definition.VIEW_STYLE_PHOTO_ALBUM);
        if (isPhotoAlbum) sortOptions = new String[] { "number", "title", "activity" }; else sortOptions = new String[] { "number", "title", "state", "author", "activity" };
        Set<String> so = new HashSet<String>();
        for (String s : sortOptions) {
            so.add(s);
        }
        ToolbarItem displayStylesTBI = new ToolbarItem("2_display_styles");
        markTBITitle(displayStylesTBI, "toolbar.folder_sortBy");
        entryToolbar.addNestedItem(displayStylesTBI);
        ToolbarItem sortByTBI;
        AdaptedPortletURL url;
        String folderIdS = String.valueOf(folderId);
        if (so.contains("number")) {
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_VIEW_FOLDER_LISTING);
            url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_SAVE_FOLDER_SORT_INFO);
            url.setParameter(WebKeys.URL_BINDER_ID, folderIdS);
            url.setParameter(WebKeys.FOLDER_SORT_BY, Constants.DOCID_FIELD);
            url.setParameter(WebKeys.FOLDER_SORT_DESCEND, "true");
            String nltTag;
            if (isPhotoAlbum) nltTag = "folder.column.CreationDate"; else nltTag = "folder.column.Number";
            sortByTBI = new ToolbarItem("sortby");
            markTBITitle(sortByTBI, nltTag);
            markTBIUrl(sortByTBI, url);
            if (searchSortBy.equals(Constants.DOCID_FIELD)) {
                markTBIDefault(sortByTBI);
            }
            displayStylesTBI.addNestedItem(sortByTBI);
        }
        if (so.contains("title")) {
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_VIEW_FOLDER_LISTING);
            url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_SAVE_FOLDER_SORT_INFO);
            url.setParameter(WebKeys.URL_BINDER_ID, folderIdS);
            url.setParameter(WebKeys.FOLDER_SORT_BY, Constants.SORT_TITLE_FIELD);
            url.setParameter(WebKeys.FOLDER_SORT_DESCEND, "false");
            sortByTBI = new ToolbarItem("sortby");
            markTBITitle(sortByTBI, "folder.column.Title");
            markTBIUrl(sortByTBI, url);
            if (searchSortBy.equals(Constants.SORT_TITLE_FIELD)) {
                markTBIDefault(sortByTBI);
            }
            displayStylesTBI.addNestedItem(sortByTBI);
        }
        if (so.contains("state")) {
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_VIEW_FOLDER_LISTING);
            url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_SAVE_FOLDER_SORT_INFO);
            url.setParameter(WebKeys.URL_BINDER_ID, folderIdS);
            url.setParameter(WebKeys.FOLDER_SORT_BY, Constants.WORKFLOW_STATE_CAPTION_FIELD);
            url.setParameter(WebKeys.FOLDER_SORT_DESCEND, "false");
            sortByTBI = new ToolbarItem("sortby");
            markTBITitle(sortByTBI, "folder.column.State");
            markTBIUrl(sortByTBI, url);
            if (searchSortBy.equals(Constants.WORKFLOW_STATE_CAPTION_FIELD)) {
                markTBIDefault(sortByTBI);
            }
            displayStylesTBI.addNestedItem(sortByTBI);
        }
        if (so.contains("author")) {
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_VIEW_FOLDER_LISTING);
            url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_SAVE_FOLDER_SORT_INFO);
            url.setParameter(WebKeys.URL_BINDER_ID, folderIdS);
            url.setParameter(WebKeys.FOLDER_SORT_BY, Constants.CREATOR_TITLE_FIELD);
            url.setParameter(WebKeys.FOLDER_SORT_DESCEND, "false");
            sortByTBI = new ToolbarItem("sortby");
            markTBITitle(sortByTBI, "folder.column.Author");
            markTBIUrl(sortByTBI, url);
            if (searchSortBy.equals(Constants.CREATOR_TITLE_FIELD)) {
                markTBIDefault(sortByTBI);
            }
            displayStylesTBI.addNestedItem(sortByTBI);
        }
        if (so.contains("activity")) {
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_VIEW_FOLDER_LISTING);
            url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_SAVE_FOLDER_SORT_INFO);
            url.setParameter(WebKeys.URL_BINDER_ID, folderIdS);
            url.setParameter(WebKeys.FOLDER_SORT_BY, Constants.LASTACTIVITY_FIELD);
            url.setParameter(WebKeys.FOLDER_SORT_DESCEND, "true");
            sortByTBI = new ToolbarItem("sortby");
            markTBITitle(sortByTBI, "folder.column.LastActivity");
            markTBIUrl(sortByTBI, url);
            if (searchSortBy.equals(Constants.LASTACTIVITY_FIELD)) {
                markTBIDefault(sortByTBI);
            }
            displayStylesTBI.addNestedItem(sortByTBI);
        }
        if (so.contains("rating")) {
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_VIEW_FOLDER_LISTING);
            url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_SAVE_FOLDER_SORT_INFO);
            url.setParameter(WebKeys.URL_BINDER_ID, folderIdS);
            url.setParameter(WebKeys.FOLDER_SORT_BY, Constants.RATING_FIELD);
            url.setParameter(WebKeys.FOLDER_SORT_DESCEND, "true");
            sortByTBI = new ToolbarItem("sortby");
            markTBITitle(sortByTBI, "folder.column.Rating");
            markTBIUrl(sortByTBI, url);
            if (searchSortBy.equals(Constants.RATING_FIELD)) {
                markTBIDefault(sortByTBI);
            }
            displayStylesTBI.addNestedItem(sortByTBI);
        }
    }

    private static void constructEntryTrashItems(ToolbarItem entryToolbar, AllModulesInjected bs, HttpServletRequest request, Binder binder) {
        ToolbarItem trashTBI = new ToolbarItem("1_trashRestore");
        markTBITitle(trashTBI, "toolbar.menu.trash.restore");
        markTBIEvent(trashTBI, TeamingEvents.TRASH_RESTORE_SELECTED_ENTRIES);
        entryToolbar.addNestedItem(trashTBI);
        trashTBI = new ToolbarItem("2_trashPurge");
        markTBITitle(trashTBI, "toolbar.menu.trash.purge");
        markTBIEvent(trashTBI, TeamingEvents.TRASH_PURGE_SELECTED_ENTRIES);
        entryToolbar.addNestedItem(trashTBI);
        trashTBI = new ToolbarItem("3_trashRestoreAll");
        markTBITitle(trashTBI, "toolbar.menu.trash.restoreAll");
        markTBIEvent(trashTBI, TeamingEvents.TRASH_RESTORE_ALL);
        entryToolbar.addNestedItem(trashTBI);
        trashTBI = new ToolbarItem("4_trashPurgeAll");
        markTBITitle(trashTBI, "toolbar.menu.trash.purgeAll");
        markTBIEvent(trashTBI, TeamingEvents.TRASH_PURGE_ALL);
        entryToolbar.addNestedItem(trashTBI);
    }

    @SuppressWarnings("unused")
    private static ToolbarItem constructFolderItems(String tbKey, AllModulesInjected bs, HttpServletRequest request, Binder binder, EntityType binderType) {
        ToolbarItem reply = new ToolbarItem(tbKey);
        AdaptedPortletURL url;
        AdminModule am = bs.getAdminModule();
        BinderModule bm = bs.getBinderModule();
        boolean isFolder = (EntityType.folder == binderType);
        boolean isProfiles = (EntityType.profiles == binderType);
        boolean isWorkspace = (EntityType.workspace == binderType);
        boolean isWorkspaceReserved = (isWorkspace && binder.isReserved());
        boolean isWorkspaceRoot = (isWorkspace && binder.isRoot());
        Long binderId = binder.getId();
        String binderIdS = String.valueOf(binderId);
        ToolbarItem actionTBI;
        ToolbarItem addTBI = new ToolbarItem(ADD_BINDER);
        ToolbarItem adminTBI = new ToolbarItem(ADMINISTRATION);
        ToolbarItem catTBI = new ToolbarItem(CATEGORIES);
        ToolbarItem configTBI = new ToolbarItem(isFolder ? "" : CONFIGURATION);
        ToolbarItem reportsTBI = new ToolbarItem(REPORTS);
        adminTBI.addNestedItem(catTBI);
        boolean addMenuCreated = false;
        boolean adminMenuCreated = false;
        boolean configMenuCreated = false;
        boolean reportsMenuCreated = false;
        if ((isFolder || isWorkspace) && (!isWorkspaceRoot)) {
            if (bm.testAccess(binder, BinderOperation.addFolder)) {
                addMenuCreated = adminMenuCreated = true;
                url = createActionUrl(request);
                url.setParameter(WebKeys.ACTION, WebKeys.ACTION_ADD_BINDER);
                url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
                url.setParameter(WebKeys.URL_OPERATION, (isFolder ? WebKeys.OPERATION_ADD_SUB_FOLDER : WebKeys.OPERATION_ADD_FOLDER));
                actionTBI = new ToolbarItem(ADD_FOLDER);
                markTBIPopup(actionTBI);
                markTBITitle(actionTBI, "toolbar.menu.addFolder");
                markTBIUrl(actionTBI, url);
                addTBI.addNestedItem(actionTBI);
            }
            if (isWorkspace && bm.testAccess(binder, BinderOperation.addWorkspace)) {
                addMenuCreated = adminMenuCreated = true;
                url = createActionUrl(request);
                url.setParameter(WebKeys.ACTION, WebKeys.ACTION_ADD_BINDER);
                url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
                url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_ADD_WORKSPACE);
                actionTBI = new ToolbarItem(ADD_WORKSPACE);
                markTBIPopup(actionTBI);
                markTBITitle(actionTBI, "toolbar.menu.addWorkspace");
                markTBIUrl(actionTBI, url);
                addTBI.addNestedItem(actionTBI);
            }
        }
        if ((isFolder || isWorkspace) && (!isWorkspaceReserved)) {
            boolean allowCopyMove;
            if (isWorkspace) {
                Integer wsDefType = binder.getDefinitionType();
                allowCopyMove = ((null == wsDefType) || (Definition.USER_WORKSPACE_VIEW != wsDefType.intValue()));
            } else {
                allowCopyMove = true;
            }
            if (allowCopyMove) {
                if (bm.testAccess(binder, BinderOperation.moveBinder)) {
                    adminMenuCreated = configMenuCreated = true;
                    url = createActionUrl(request);
                    url.setParameter(WebKeys.ACTION, WebKeys.ACTION_MODIFY_BINDER);
                    url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
                    url.setParameter(WebKeys.URL_BINDER_TYPE, binderType.name());
                    url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_MOVE);
                    actionTBI = new ToolbarItem(MOVE);
                    markTBIPopup(actionTBI);
                    markTBITitle(actionTBI, (isFolder ? "toolbar.menu.move_folder" : "toolbar.menu.move_workspace"));
                    markTBIUrl(actionTBI, url);
                    configTBI.addNestedItem(actionTBI);
                }
                if (bm.testAccess(binder, BinderOperation.copyBinder)) {
                    adminMenuCreated = configMenuCreated = true;
                    url = createActionUrl(request);
                    url.setParameter(WebKeys.ACTION, WebKeys.ACTION_MODIFY_BINDER);
                    url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
                    url.setParameter(WebKeys.URL_BINDER_TYPE, binderType.name());
                    url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_COPY);
                    actionTBI = new ToolbarItem(COPY);
                    markTBIPopup(actionTBI);
                    markTBITitle(actionTBI, (isFolder ? "toolbar.menu.copy_folder" : "toolbar.menu.copy_workspace"));
                    markTBIUrl(actionTBI, url);
                    configTBI.addNestedItem(actionTBI);
                }
            }
        }
        if (bm.testAccess(binder, BinderOperation.modifyBinder)) {
            adminMenuCreated = configMenuCreated = true;
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_MODIFY_BINDER);
            url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
            url.setParameter(WebKeys.URL_BINDER_TYPE, binderType.name());
            url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_MODIFY);
            actionTBI = new ToolbarItem(MODIFY);
            markTBIPopup(actionTBI);
            markTBITitle(actionTBI, (isFolder ? "toolbar.menu.modify_folder" : "toolbar.menu.modify_workspace"));
            markTBIUrl(actionTBI, url);
            configTBI.addNestedItem(actionTBI);
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_CONFIGURE_DEFINITIONS);
            url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
            url.setParameter(WebKeys.URL_BINDER_TYPE, binderType.name());
            actionTBI = new ToolbarItem(CONFIGURE_DEFINITIONS);
            markTBIPopup(actionTBI);
            markTBITitle(actionTBI, "toolbar.menu.configuration");
            markTBIUrl(actionTBI, url);
            configTBI.addNestedItem(actionTBI);
        }
        if ((isFolder || isWorkspace) && bm.testAccess(binder, BinderOperation.report)) {
            adminMenuCreated = true;
            if (isFolder) configMenuCreated = true; else reportsMenuCreated = true;
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, (isFolder ? WebKeys.ACTION_BINDER_REPORTS : WebKeys.ACTION_ACTIVITY_REPORT));
            url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
            url.setParameter(WebKeys.URL_BINDER_TYPE, binderType.name());
            actionTBI = new ToolbarItem(REPORTS);
            markTBIPopup(actionTBI);
            markTBITitle(actionTBI, "toolbar.menu.reports");
            markTBIUrl(actionTBI, url);
            if (isFolder) configTBI.addNestedItem(actionTBI); else reportsTBI.addNestedItem(actionTBI);
        }
        if ((isFolder || isWorkspace) && bm.testAccess(binder, BinderOperation.manageConfiguration)) {
            adminMenuCreated = configMenuCreated = true;
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_MANAGE_DEFINITIONS);
            url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
            actionTBI = new ToolbarItem(MANAGE_DEFINITIONS);
            markTBIPopup(actionTBI);
            markTBITitle(actionTBI, "administration.definition_builder_designers");
            markTBIUrl(actionTBI, url);
            configTBI.addNestedItem(actionTBI);
        }
        if ((isFolder || isWorkspace) && bm.testAccess(binder, BinderOperation.manageConfiguration)) {
            adminMenuCreated = configMenuCreated = true;
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_MANAGE_TEMPLATES);
            url.setParameter(WebKeys.URL_BINDER_PARENT_ID, binderIdS);
            actionTBI = new ToolbarItem(MANAGE_TEMPLATES);
            markTBIPopup(actionTBI);
            markTBITitle(actionTBI, "administration.template_builder_local");
            markTBIUrl(actionTBI, url);
            configTBI.addNestedItem(actionTBI);
        }
        if ((isFolder || isWorkspace) && (!isWorkspaceReserved) && bm.testAccess(binder, BinderOperation.deleteBinder)) {
            adminMenuCreated = configMenuCreated = true;
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_MODIFY_BINDER);
            url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
            url.setParameter(WebKeys.URL_BINDER_TYPE, binderType.name());
            url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_DELETE);
            actionTBI = new ToolbarItem(DELETE);
            markTBIPopup(actionTBI);
            markTBITitle(actionTBI, (isFolder ? "toolbar.menu.delete_folder" : "toolbar.menu.delete_workspace"));
            markTBIUrl(actionTBI, url);
            configTBI.addNestedItem(actionTBI);
        }
        if (isFolder && LicenseChecker.isAuthorizedByLicense("com.novell.teaming.module.folder.MirroredFolder")) {
            Folder folder = ((Folder) binder);
            if (folder.isMirrored() && (null != folder.getResourceDriverName())) {
                FolderModule fm = bs.getFolderModule();
                if (fm.testAccess(folder, FolderOperation.synchronize)) {
                    adminMenuCreated = configMenuCreated = true;
                    url = createActionUrl(request);
                    url.setParameter(WebKeys.ACTION, WebKeys.ACTION_MODIFY_BINDER);
                    url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
                    url.setParameter(WebKeys.URL_BINDER_TYPE, binderType.name());
                    url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_SYNCHRONIZE_MIRRORED_FOLDER);
                    actionTBI = new ToolbarItem(MANUAL_SYNC);
                    markTBISpinner(actionTBI);
                    markTBITitle(actionTBI, "toolbar.menu.synchronize_mirrored_folder.manual");
                    markTBIUrl(actionTBI, url);
                    configTBI.addNestedItem(actionTBI);
                }
                if (fm.testAccess(folder, FolderOperation.scheduleSynchronization)) {
                    adminMenuCreated = configMenuCreated = true;
                    url = createActionUrl(request);
                    url.setParameter(WebKeys.ACTION, WebKeys.ACTION_SCHEDULE_SYNCHRONIZATION);
                    url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
                    actionTBI = new ToolbarItem(SCHEDULE_SYNC);
                    markTBIPopup(actionTBI);
                    markTBITitle(actionTBI, "toolbar.menu.synchronize_mirrored_folder.scheduled");
                    markTBIUrl(actionTBI, url);
                    configTBI.addNestedItem(actionTBI);
                }
            }
        }
        if ((isFolder || isWorkspace) && bm.testAccess(binder, BinderOperation.export)) {
            adminMenuCreated = configMenuCreated = true;
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_EXPORT_IMPORT);
            url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
            url.setParameter(WebKeys.URL_SHOW_MENU, "true");
            actionTBI = new ToolbarItem(IMPORT_EXPORT);
            markTBIPopup(actionTBI);
            markTBITitle(actionTBI, (isFolder ? "toolbar.menu.export_import_folder" : "toolbar.menu.export_import_workspace"));
            markTBIUrl(actionTBI, url);
            configTBI.addNestedItem(actionTBI);
        }
        if (isFolder && bm.testAccess(binder, BinderOperation.manageMail)) {
            Folder folder = ((Folder) binder);
            if (folder.isTop() || am.getMailConfig().isPostingEnabled()) {
                adminMenuCreated = configMenuCreated = true;
                url = createActionUrl(request);
                url.setParameter(WebKeys.ACTION, WebKeys.ACTION_CONFIG_EMAIL);
                url.setParameter(WebKeys.URL_BINDER_ID, binderIdS);
                actionTBI = new ToolbarItem(CONFIGURE_EMAIL);
                markTBIPopup(actionTBI);
                markTBITitle(actionTBI, "toolbar.menu.configure_folder_email");
                markTBIUrl(actionTBI, url);
                configTBI.addNestedItem(actionTBI);
            }
        }
        if (am.testAccess(binder, AdminOperation.manageFunctionMembership)) {
            adminMenuCreated = configMenuCreated = true;
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_ACCESS_CONTROL);
            url.setParameter(WebKeys.URL_WORKAREA_ID, String.valueOf(binder.getWorkAreaId()));
            url.setParameter(WebKeys.URL_WORKAREA_TYPE, binder.getWorkAreaType());
            actionTBI = new ToolbarItem(ACCESS_CONTROL);
            markTBIPopup(actionTBI);
            markTBITitle(actionTBI, "toolbar.menu.accessControl");
            markTBIUrl(actionTBI, url);
            configTBI.addNestedItem(actionTBI);
        }
        if (!(GwtServerHelper.getCurrentUser().isShared())) {
            url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_ACCESS_CONTROL);
            url.setParameter(WebKeys.URL_WORKAREA_ID, String.valueOf(binder.getWorkAreaId()));
            url.setParameter(WebKeys.URL_WORKAREA_TYPE, binder.getWorkAreaType());
            url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_VIEW_ACCESS);
            actionTBI = new ToolbarItem(WHO_HAS_ACCESS);
            markTBIPopup(actionTBI, "600", "700");
            markTBIHint(actionTBI, (isWorkspace ? "toolbar.menu.title.whoHasAccessWorkspace" : "toolbar.menu.title.whoHasAccessFolder"));
            markTBITitle(actionTBI, "toolbar.whoHasAccess");
            markTBIUrl(actionTBI, url);
            reply.addNestedItem(actionTBI);
        }
        if (addMenuCreated) catTBI.addNestedItem(addTBI);
        if (configMenuCreated) catTBI.addNestedItem(configTBI);
        if (reportsMenuCreated) catTBI.addNestedItem(reportsTBI);
        if (adminMenuCreated) reply.addNestedItem(adminTBI);
        return reply;
    }

    private static ToolbarItem constructFolderViewsItems(String tbKey, AllModulesInjected bs, HttpServletRequest request, Folder folder) {
        ToolbarItem reply = new ToolbarItem(tbKey);
        @SuppressWarnings("unchecked") List<Definition> folderViewDefs = folder.getViewDefinitions();
        if (!(folderViewDefs.isEmpty())) {
            ToolbarItem dispStylesTBI = new ToolbarItem(DISPLAY_STYLES);
            ToolbarItem catTBI = new ToolbarItem(CATEGORIES);
            ToolbarItem viewsTBI = new ToolbarItem(FOLDER_VIEWS);
            reply.addNestedItem(dispStylesTBI);
            dispStylesTBI.addNestedItem(catTBI);
            catTBI.addNestedItem(viewsTBI);
            Long folderId = folder.getId();
            UserProperties userFolderProperties = bs.getProfileModule().getUserProperties(GwtServerHelper.getCurrentUser().getId(), folderId);
            String userSelectedDefinition = ((String) userFolderProperties.getProperty(ObjectKeys.USER_PROPERTY_DISPLAY_DEFINITION));
            Definition currentDef = folderViewDefs.get(0);
            if (MiscUtil.hasString(userSelectedDefinition)) {
                for (Definition def : folderViewDefs) {
                    if (userSelectedDefinition.equals(def.getId())) {
                        currentDef = def;
                        break;
                    }
                }
            }
            String folderIdS = String.valueOf(folderId);
            for (Definition def : folderViewDefs) {
                AdaptedPortletURL url = createActionUrl(request);
                url.setParameter(WebKeys.ACTION, WebKeys.ACTION_VIEW_FOLDER_LISTING);
                url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_SET_DISPLAY_DEFINITION);
                url.setParameter(WebKeys.URL_BINDER_ID, folderIdS);
                url.setParameter(WebKeys.URL_VALUE, def.getId());
                ToolbarItem viewTBI = new ToolbarItem(def.getName());
                if (def.equals(currentDef)) {
                    viewTBI.addQualifier(WebKeys.TOOLBAR_MENU_SELECTED, "true");
                }
                markTBITitleGetDef(viewTBI, def.getTitle());
                markTBIUrl(viewTBI, url);
                viewsTBI.addNestedItem(viewTBI);
            }
        }
        return reply;
    }

    private static void constructFooterFolderItems(ToolbarItem footerToolbar, AllModulesInjected bs, HttpServletRequest request, Folder folder) {
        String permaLink = PermaLinkUtil.getPermalink(request, folder);
        ToolbarItem permalinkTBI = new ToolbarItem(PERMALINK);
        markTBITitle(permalinkTBI, "toolbar.menu.folderPermalink");
        markTBIUrl(permalinkTBI, permaLink);
        footerToolbar.addNestedItem(permalinkTBI);
        if (folder.isLibrary()) {
            String webdavUrl = SsfsUtil.getLibraryBinderUrl(request, folder);
            if (MiscUtil.hasString(webdavUrl)) {
                ToolbarItem webDavTBI = new ToolbarItem(VIEW_AS_WEBDAV);
                markTBITitle(webDavTBI, "toolbar.menu.viewASWebDav");
                markTBIUrl(webDavTBI, webdavUrl);
                footerToolbar.addNestedItem(webDavTBI);
                String webdavSuffix = SPropsUtil.getString("webdav.folder.url.suffix", "");
                if (MiscUtil.hasString(webdavSuffix)) {
                    webdavUrl = (webdavUrl + "/" + webdavSuffix);
                }
                webDavTBI = new ToolbarItem(WEBDAVURL);
                markTBITitle(webDavTBI, "toolbar.menu.webdavUrl");
                markTBIUrl(webDavTBI, webdavUrl);
                footerToolbar.addNestedItem(webDavTBI);
            }
        }
        String viewType = DefinitionUtils.getViewType(folder);
        if (viewType.equals(Definition.VIEW_STYLE_CALENDAR) || viewType.equals(Definition.VIEW_STYLE_TASK)) {
            String icalUrl = org.kablink.teaming.ical.util.UrlUtil.getICalURLHttp(request, String.valueOf(folder.getId()), null);
            ToolbarItem icalTBI = new ToolbarItem(ICALENDAR);
            markTBITitle(icalTBI, "toolbar.menu.iCalendar");
            markTBIUrl(icalTBI, icalUrl);
            footerToolbar.addNestedItem(icalTBI);
        }
        String topFolderId;
        if (folder.isTop()) topFolderId = folder.getId().toString(); else topFolderId = folder.getTopFolder().getId().toString();
        String rssUrl = org.kablink.teaming.module.rss.util.UrlUtil.getFeedURLHttp(request, topFolderId);
        if (MiscUtil.hasString(rssUrl)) {
            ToolbarItem rssTBI = new ToolbarItem(SUBSCRIBE_RSS);
            markTBITitle(rssTBI, "toolbar.menu.subscribeRSS");
            markTBIUrl(rssTBI, rssUrl);
            footerToolbar.addNestedItem(rssTBI);
        }
        String atomUrl = org.kablink.teaming.module.rss.util.UrlUtil.getAtomURLHttp(request, topFolderId);
        if (MiscUtil.hasString(atomUrl)) {
            ToolbarItem atomTBI = new ToolbarItem(SUBSCRIBE_ATOM);
            markTBITitle(atomTBI, "toolbar.menu.subscribeAtom");
            markTBIUrl(atomTBI, atomUrl);
            footerToolbar.addNestedItem(atomTBI);
        }
        constructFooterSimpleNameItems(footerToolbar, bs, request, folder.getId());
    }

    private static void constructFooterSimpleNameItems(ToolbarItem footerToolbar, AllModulesInjected bs, HttpServletRequest request, Long binderId) {
        List<SimpleName> simpleNames = bs.getBinderModule().getSimpleNames(binderId);
        int c = ((null == simpleNames) ? 0 : simpleNames.size());
        if (0 < c) {
            ToolbarItem simpleNamesTBI = new ToolbarItem(SIMPLE_NAMES);
            footerToolbar.addNestedItem(simpleNamesTBI);
            simpleNamesTBI.addQualifier("simple.host", getHostName(bs));
            simpleNamesTBI.addQualifier("simple.prefix", WebUrlUtil.getSimpleURLContextRootURL(request));
            simpleNamesTBI.addQualifier("simple.count", String.valueOf(c));
            for (int i = 0; i < c; i += 1) {
                SimpleName simpleName = simpleNames.get(i);
                SimpleNamePK simpleNameId = simpleName.getId();
                simpleNamesTBI.addQualifier(("simple." + i + ".email"), simpleName.getEmailAddress());
                simpleNamesTBI.addQualifier(("simple." + i + ".zone"), String.valueOf(simpleNameId.getZoneId()));
                simpleNamesTBI.addQualifier(("simple." + i + ".name"), simpleNameId.getName());
            }
        }
    }

    private static void constructFooterWorkspaceItems(ToolbarItem footerToolbar, AllModulesInjected bs, HttpServletRequest request, Workspace ws) {
        String permaLink = PermaLinkUtil.getPermalink(request, ws);
        ToolbarItem permalinkTBI = new ToolbarItem(PERMALINK);
        markTBITitle(permalinkTBI, "toolbar.menu.workspacePermalink");
        markTBIUrl(permalinkTBI, permaLink);
        footerToolbar.addNestedItem(permalinkTBI);
        constructFooterSimpleNameItems(footerToolbar, bs, request, ws.getId());
    }

    private static ToolbarItem constructSendEmailToItem(HttpServletRequest request, Binder binder) {
        User user = GwtServerHelper.getCurrentUser();
        if (MiscUtil.hasString(user.getEmailAddress()) && (!(user.isShared()))) {
            ToolbarItem sendEmailToTBI = new ToolbarItem(SEND_EMAIL);
            markTBITitle(sendEmailToTBI, "toolbar.menu.sendMail");
            markTBIEvent(sendEmailToTBI, TeamingEvents.INVOKE_SEND_EMAIL_TO_TEAM);
            AdaptedPortletURL url = createActionUrl(request);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_SEND_EMAIL);
            url.setParameter(WebKeys.URL_BINDER_ID, binder.getId().toString());
            url.setParameter(WebKeys.USER_IDS_TO_ADD, InvokeSendEmailToTeamEvent.CONTRIBUTOR_IDS_PLACEHOLER);
            markTBIUrl(sendEmailToTBI, url);
            return sendEmailToTBI;
        }
        return null;
    }

    private static ToolbarItem constructShareBinderItem(HttpServletRequest request, Binder binder) {
        ToolbarItem sbTBI = new ToolbarItem(SHARE);
        markTBIPopup(sbTBI, "550", "750");
        markTBITitle(sbTBI, GwtUIHelper.buildRelevanceKey(binder, "relevance.shareThis"));
        AdaptedPortletURL url = new AdaptedPortletURL(request, SS_FORUM, true);
        url.setParameter(WebKeys.ACTION, "__ajax_relevance");
        url.setParameter(WebKeys.URL_OPERATION, "share_this_binder");
        url.setParameter(WebKeys.URL_BINDER_ID, String.valueOf(binder.getId()));
        markTBIUrl(sbTBI, url);
        return sbTBI;
    }

    private static ToolbarItem constructMobileUiItem(HttpServletRequest request, Binder binder) {
        String userAgents = org.kablink.teaming.util.SPropsUtil.getString("mobile.userAgents", "");
        if (BrowserSniffer.is_mobile(request, userAgents)) {
            AdaptedPortletURL url = new AdaptedPortletURL(request, SS_FORUM, true, true);
            url.setParameter(WebKeys.ACTION, WebKeys.ACTION_MOBILE_AJAX);
            url.setParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_MOBILE_SHOW_MOBILE_UI);
            ToolbarItem mobileTBI = new ToolbarItem(MOBILE_UI);
            markTBITitle(mobileTBI, "toolbar.menu.mobileUI");
            markTBIEvent(mobileTBI, TeamingEvents.GOTO_PERMALINK_URL);
            markTBIUrl(mobileTBI, url);
            return mobileTBI;
        }
        return null;
    }

    private static List<ToolbarItem> constructTrackBinderItem(AllModulesInjected bs, Binder binder) {
        List<ToolbarItem> tbTBIs = new ArrayList<ToolbarItem>();
        List<TrackInfo> tiList = GwtUIHelper.getTrackInfoList(bs, binder);
        for (TrackInfo ti : tiList) {
            ToolbarItem tbTBI = new ToolbarItem(ti.m_tbName);
            markTBITitle(tbTBI, ti.m_resourceKey);
            markTBIEvent(tbTBI, TeamingEvents.valueOf(ti.m_event));
            tbTBIs.add(tbTBI);
        }
        return tbTBIs;
    }

    private static ToolbarItem constructTrashItem(HttpServletRequest request, Binder binder) {
        String binderPermalink = PermaLinkUtil.getPermalink(request, binder);
        String trashPermalink = GwtUIHelper.getTrashPermalink(binderPermalink);
        ToolbarItem trashTBI = new ToolbarItem(TRASH);
        markTBITitle(trashTBI, "toolbar.menu.trash");
        markTBIEvent(trashTBI, TeamingEvents.GOTO_PERMALINK_URL);
        markTBIUrl(trashTBI, trashPermalink);
        return trashTBI;
    }

    private static ToolbarItem constructWhatsNewItems(String tbKey, AllModulesInjected bs, HttpServletRequest request, Binder binder, EntityType binderType) {
        ToolbarItem reply = new ToolbarItem(tbKey);
        if (!(GwtServerHelper.getCurrentUser().isShared())) {
            boolean isFolder = (EntityType.folder == binderType);
            if ((!isFolder) || (!(BinderHelper.isBinderWiki(binder)))) {
                ToolbarItem itemTBI = new ToolbarItem(WHATS_NEW);
                markTBITitle(itemTBI, "toolbar.menu.whatsNew");
                markTBIHint(itemTBI, (isFolder ? "toolbar.menu.title.whatsNewInFolder" : "toolbar.menu.title.whatsNewInWorkspace"));
                markTBIEvent(itemTBI, TeamingEvents.VIEW_WHATS_NEW_IN_BINDER);
                reply.addNestedItem(itemTBI);
                itemTBI = new ToolbarItem(UNSEEN);
                markTBITitle(itemTBI, "toolbar.menu.whatsUnseen");
                markTBIHint(itemTBI, (isFolder ? "toolbar.menu.title.whatsUnreadInFolder" : "toolbar.menu.title.whatsUnreadInWorkspace"));
                markTBIEvent(itemTBI, TeamingEvents.VIEW_WHATS_UNSEEN_IN_BINDER);
                reply.addNestedItem(itemTBI);
            }
        }
        return reply;
    }

    private static AdaptedPortletURL createActionUrl(HttpServletRequest request) {
        return new AdaptedPortletURL(request, SS_FORUM, true);
    }

    private static void dumpString(String dumpThis) {
        m_logger.debug(dumpThis);
    }

    private static void dumpString(String dumpStart, String dumpThis) {
        if (MiscUtil.hasString(dumpThis)) {
            dumpString(dumpStart + dumpThis);
        }
    }

    private static void dumpToolbarItem(ToolbarItem tbi, String dumpStart) {
        dumpString(dumpStart + ":toolbar=" + tbi.getName());
        dumpString(dumpStart + "...:title=", tbi.getTitle());
        dumpString(dumpStart + "...:url=", tbi.getUrl());
        if (TeamingEvents.UNDEFINED != tbi.getTeamingEvent()) {
            dumpString(dumpStart + "...:event=", tbi.getTeamingEvent().name());
        }
        for (NameValuePair nvp : tbi.getQualifiersList()) {
            dumpString(dumpStart + "...:qualifier:name:value=", nvp.getName() + ":" + nvp.getValue());
        }
        dumpToolbarItems(tbi.getNestedItemsList(), (dumpStart + "..."));
    }

    private static void dumpToolbarItems(List<ToolbarItem> tbiList, String dumpStart) {
        if (m_logger.isDebugEnabled()) {
            for (ToolbarItem tbi : tbiList) {
                dumpToolbarItem(tbi, dumpStart);
            }
        }
    }

    private static boolean folderSupportsDeleteAndPurge(Folder folder, String viewType) {
        boolean reply = ((null != folder) && MiscUtil.hasString(viewType));
        if (reply) {
            reply = ((viewType.equals(Definition.VIEW_STYLE_DISCUSSION) || viewType.equals(Definition.VIEW_STYLE_TABLE) || viewType.equals(Definition.VIEW_STYLE_FILE) || viewType.equals(Definition.VIEW_STYLE_GUESTBOOK) || viewType.equals(Definition.VIEW_STYLE_MILESTONE) || viewType.equals(Definition.VIEW_STYLE_MINIBLOG) || viewType.equals(Definition.VIEW_STYLE_SURVEY) || viewType.equals(Definition.VIEW_STYLE_TASK)));
        }
        return reply;
    }

    private static Long getFolderSetFolderId(AllModulesInjected bs, Folder folder, String viewType) {
        Binder parentBinder = folder.getParentBinder();
        Binder topBinder = folder;
        while (null != parentBinder) {
            Integer pbDefType = parentBinder.getDefinitionType();
            if ((null != pbDefType) && (!(pbDefType.equals(Definition.FOLDER_VIEW))) || (!(viewType.equals(BinderHelper.getViewType(bs, parentBinder))))) {
                break;
            }
            topBinder = parentBinder;
            parentBinder = parentBinder.getParentBinder();
        }
        return topBinder.getId();
    }

    /**
	 * Returns a GetFolderToolbarItemsRpcResponseData containing the
	 * ToolbarItem's for a folder given the current user's rights to
	 * that folder.
	 *
	 * @param bs
	 * @param request
	 * @param folderInfo
	 * 
	 * @return
	 */
    public static GetFolderToolbarItemsRpcResponseData getFolderToolbarItems(AllModulesInjected bs, HttpServletRequest request, BinderInfo folderInfo) {
        SimpleProfiler.start("GwtMenuHelper.getFolderToolbarItems()");
        try {
            List<ToolbarItem> configureToolbarItems = new ArrayList<ToolbarItem>();
            List<ToolbarItem> toolbarItems = new ArrayList<ToolbarItem>();
            GetFolderToolbarItemsRpcResponseData reply = new GetFolderToolbarItemsRpcResponseData(toolbarItems, configureToolbarItems);
            ToolbarItem entryToolbar = new ToolbarItem(WebKeys.ENTRY_TOOLBAR);
            toolbarItems.add(entryToolbar);
            Long folderId = folderInfo.getBinderIdAsLong();
            Binder binder = bs.getBinderModule().getBinder(folderId);
            Folder folder = ((binder instanceof Folder) ? ((Folder) binder) : null);
            Workspace ws = ((binder instanceof Workspace) ? ((Workspace) binder) : null);
            if (folderInfo.isBinderTrash()) {
                constructEntryTrashItems(entryToolbar, bs, request, binder);
            } else if (folderInfo.isBinderProfilesRootWS()) {
                constructEntryProfilesRootWSItems(entryToolbar, bs, request, ws);
            } else {
                boolean isMirrored = ((null != folder) && folder.isMirrored());
                boolean isMirroredConfigured = isMirrored && MiscUtil.hasString(folder.getResourceDriverName());
                if ((!isMirrored) || isMirroredConfigured) {
                    FolderModule fm = bs.getFolderModule();
                    String viewType = DefinitionUtils.getViewType(folder);
                    boolean hasVT = MiscUtil.hasString(viewType);
                    boolean addAllowed = fm.testAccess(folder, FolderOperation.addEntry);
                    if (addAllowed) {
                        if (hasVT && viewType.equals(Definition.VIEW_STYLE_GUESTBOOK)) {
                            constructEntrySignTheGuestbookItem(entryToolbar, bs, request, folder);
                        }
                        constructEntryAddItems(entryToolbar, bs, request, viewType, folder);
                    }
                    if (hasVT) {
                        if (viewType.equals(Definition.VIEW_STYLE_BLOG) || viewType.equals(Definition.VIEW_STYLE_PHOTO_ALBUM)) {
                            constructEntrySortByItems(entryToolbar, bs, request, viewType, folder);
                        }
                        if (addAllowed && SsfsUtil.supportApplets(request)) {
                            if ((!(viewType.equals(Definition.VIEW_STYLE_MINIBLOG))) && ((!(folder.isMirrored())) || isFolderWritableMirrored(folder))) {
                                constructEntryDropBoxItem(entryToolbar, bs, request, viewType, folder);
                            }
                        }
                    }
                    constructEntryDeleteItems(entryToolbar, bs, request, viewType, folder);
                    constructEntryMoreItems(entryToolbar, bs, request, folderId, viewType, folder);
                }
            }
            ToolbarItem configureColumns = constructEntryConfigureColumsItem(binder);
            if (null != configureColumns) {
                configureToolbarItems.add(configureColumns);
            }
            m_logger.debug("GwtMenuHelper.getFolderToolbarItems():");
            dumpToolbarItems(configureToolbarItems, "...");
            dumpToolbarItems(toolbarItems, "...");
            return reply;
        } finally {
            SimpleProfiler.stop("GwtMenuHelper.getFolderToolbarItems()");
        }
    }

    /**
	 * Returns a List<ToolbarItem> of the ToolbarItem's for a binder's
	 * footer given the binder type, the current user's rights to that
	 * binder, ...
	 *
	 * @param bs
	 * @param request
	 * @param binderId
	 * 
	 * @return
	 */
    public static List<ToolbarItem> getFooterToolbarItems(AllModulesInjected bs, HttpServletRequest request, Long binderId) {
        SimpleProfiler.start("GwtMenuHelper.getFooterToolbarItems()");
        try {
            List<ToolbarItem> reply = new ArrayList<ToolbarItem>();
            ToolbarItem footerToolbar = new ToolbarItem(WebKeys.FOOTER_TOOLBAR);
            reply.add(footerToolbar);
            BinderModule bm = bs.getBinderModule();
            Binder binder = bm.getBinder(binderId);
            if (binder instanceof Folder) {
                Folder folder = ((Folder) binder);
                constructFooterFolderItems(footerToolbar, bs, request, folder);
            } else if (binder instanceof Workspace) {
                Workspace ws = ((Workspace) binder);
                constructFooterWorkspaceItems(footerToolbar, bs, request, ws);
            }
            m_logger.debug("GwtMenuHelper.getFooterToolbarItems():");
            dumpToolbarItems(reply, "...");
            return reply;
        } finally {
            SimpleProfiler.stop("GwtMenuHelper.getFooterToolbarItems()");
        }
    }

    private static String getHostName(AllModulesInjected bs) {
        String hostname = bs.getZoneModule().getVirtualHost(RequestContextHolder.getRequestContext().getZoneName());
        if (!(MiscUtil.hasString(hostname))) {
            try {
                InetAddress addr = InetAddress.getLocalHost();
                hostname = addr.getHostName();
            } catch (UnknownHostException e) {
                m_logger.debug("GwtMenuHelper.getHostName( UnknownHostException ):  Using localhost");
                hostname = "localhost";
            }
        }
        return hostname;
    }

    /**
	 * Returns information about the recent places the current user has
	 * visited.
	 *
	 * @param bs
	 * @param request
	 * @param binderId
	 * 
	 * @return
	 */
    public static List<RecentPlaceInfo> getRecentPlaces(AllModulesInjected bs, HttpServletRequest request, Long binderId) {
        List<RecentPlaceInfo> reply = new ArrayList<RecentPlaceInfo>();
        Tabs tabs = Tabs.getTabs(request);
        Binder binder = GwtUIHelper.getBinderSafely(bs.getBinderModule(), binderId);
        if (null != binder) {
            Tabs.TabEntry tab = tabs.findTab(binder, false);
            GwtServerHelper.fillRecentPlacesFromTabs(bs, request, tab.getTabs(), reply);
        }
        return reply;
    }

    /**
	 * Returns a StringRpcResponseData containing the URL to use to run
	 * the guest book signing UI.
	 * 
	 * @param bs
	 * @param request
	 * @param folderId
	 * 
	 * @return
	 * 
	 * @throws GwtTeamingException
	 */
    public static StringRpcResponseData getSignGuestbookUrl(AllModulesInjected bs, HttpServletRequest request, Long folderId) throws GwtTeamingException {
        try {
            StringRpcResponseData reply = new StringRpcResponseData();
            Definition def = DefinitionHelper.getDefinition(ObjectKeys.DEFAULT_ENTRY_GUESTBOOK_DEF);
            if (null != def) {
                AdaptedPortletURL url = createActionUrl(request);
                url.setParameter(WebKeys.ACTION, WebKeys.ACTION_ADD_FOLDER_ENTRY);
                url.setParameter(WebKeys.URL_BINDER_ID, String.valueOf(folderId));
                url.setParameter(WebKeys.URL_ENTRY_TYPE, def.getId());
                reply.setStringValue(url.toString());
            }
            return reply;
        } catch (Exception e) {
            if ((!(GwtServerHelper.m_logger.isDebugEnabled())) && m_logger.isDebugEnabled()) {
                m_logger.debug("GwtMenuHelper.getSignGuestbookUrl( SOURCE EXCEPTION ):  ", e);
            }
            throw GwtServerHelper.getGwtTeamingException(e);
        }
    }

    /**
	 * Returns a TeamManagementInfo object regarding the current user's
	 * team management capabilities.
	 * 
	 * @param bs
	 * @param request
	 * @param binderId
	 * 
	 * @return
	 */
    public static TeamManagementInfo getTeamManagementInfo(AllModulesInjected bs, HttpServletRequest request, String binderId) {
        SimpleProfiler.start("GwtMenuHelper.getTeamManagementInfo()");
        try {
            TeamManagementInfo reply = new TeamManagementInfo();
            User user = GwtServerHelper.getCurrentUser();
            if (!(user.isShared())) {
                BinderModule bm = bs.getBinderModule();
                Binder binder = GwtUIHelper.getBinderSafely(bm, binderId);
                if ((null != binder) && (EntityIdentifier.EntityType.profiles != binder.getEntityType())) {
                    reply.setViewAllowed(true);
                    AdaptedPortletURL adapterUrl;
                    if (bm.testAccess(binder, BinderOperation.manageTeamMembers)) {
                        adapterUrl = createActionUrl(request);
                        adapterUrl.setParameter(WebKeys.ACTION, WebKeys.ACTION_ADD_TEAM_MEMBER);
                        adapterUrl.setParameter(WebKeys.URL_BINDER_ID, binderId);
                        adapterUrl.setParameter(WebKeys.URL_BINDER_TYPE, binder.getEntityType().name());
                        reply.setManageUrl(adapterUrl.toString());
                    }
                    if (MiscUtil.hasString(user.getEmailAddress())) {
                        adapterUrl = createActionUrl(request);
                        adapterUrl.setParameter(WebKeys.ACTION, WebKeys.ACTION_SEND_EMAIL);
                        adapterUrl.setParameter(WebKeys.URL_BINDER_ID, binderId);
                        adapterUrl.setParameter(WebKeys.URL_APPEND_TEAM_MEMBERS, Boolean.TRUE.toString());
                        reply.setSendMailUrl(adapterUrl.toString());
                    }
                    if (bs.getConferencingModule().isEnabled()) {
                        CustomAttribute ca = user.getCustomAttribute("conferencingID");
                        if ((null != ca) && MiscUtil.hasString((String) ca.getValue())) {
                            try {
                                reply.setTeamMeetingUrl(GwtServerHelper.getAddMeetingUrl(bs, request, binderId));
                            } catch (GwtTeamingException e) {
                            }
                        }
                    }
                }
            }
            return reply;
        } finally {
            SimpleProfiler.stop("GwtMenuHelper.getTeamManagementInfo()");
        }
    }

    /**
	 * Returns a List<ToolbarItem> of the ToolbarItem's
	 * applicable for the given context.
	 *
	 * @param bs
	 * @param request
	 * @param binderIdS
	 * 
	 * @return
	 */
    public static List<ToolbarItem> getToolbarItems(AllModulesInjected bs, HttpServletRequest request, String binderIdS) {
        SimpleProfiler.start("GwtMenuHelper.getToolbarItems()");
        try {
            List<ToolbarItem> reply = new ArrayList<ToolbarItem>();
            Long binderId = Long.parseLong(binderIdS);
            Binder binder = bs.getBinderModule().getBinder(binderId);
            EntityType binderType = binder.getEntityType();
            switch(binderType) {
                case workspace:
                    buildWorkspaceMenuItems(bs, request, ((Workspace) binder), reply);
                    break;
                case folder:
                    buildFolderMenuItems(bs, request, ((Folder) binder), reply);
                    break;
                case profiles:
                    buildProfilesMenuItems(bs, request, ((ProfileBinder) binder), reply);
                    break;
            }
            buildMiscMenuItems(bs, request, binder, binderType, reply);
            m_logger.debug("GwtMenuHelper.getToolbarItems():");
            dumpToolbarItems(reply, "...");
            return reply;
        } finally {
            SimpleProfiler.stop("GwtMenuHelper.getToolbarItems()");
        }
    }

    private static boolean isFolderWritableMirrored(Folder folder) {
        return (folder.isMirrored() && (!(folder.isMirroredAndReadOnly())) && MiscUtil.hasString(folder.getResourceDriverName()));
    }

    private static void markTBIDefault(ToolbarItem tbi) {
        tbi.addQualifier("default", "true");
    }

    private static void markTBIEvent(ToolbarItem tbi, TeamingEvents event) {
        tbi.setTeamingEvent(event);
    }

    private static void markTBIHighlight(ToolbarItem tbi) {
        tbi.addQualifier("highlight", "true");
    }

    private static void markTBIHint(ToolbarItem tbi, String key) {
        tbi.addQualifier("title", NLT.get(key));
    }

    private static void markTBIPopup(ToolbarItem tbi, String width, String height) {
        tbi.addQualifier("popup", "true");
        if (MiscUtil.hasString(width)) tbi.addQualifier("popupWidth", width);
        if (MiscUtil.hasString(height)) tbi.addQualifier("popupHeight", height);
    }

    private static void markTBIPopup(ToolbarItem tbi) {
        markTBIPopup(tbi, null, null);
    }

    private static void markTBISpinner(ToolbarItem tbi) {
        tbi.addQualifier("showSpinner", "true");
    }

    private static void markTBITitleRes(ToolbarItem tbi, String title) {
        tbi.setTitle(title);
    }

    private static void markTBITitle(ToolbarItem tbi, String key) {
        markTBITitleRes(tbi, NLT.get(key));
    }

    private static void markTBITitleGetDef(ToolbarItem tbi, String key) {
        markTBITitleRes(tbi, NLT.getDef(key));
    }

    private static void markTBIUrl(ToolbarItem tbi, String url) {
        tbi.setUrl(url);
    }

    private static void markTBIUrl(ToolbarItem tbi, AdaptedPortletURL url) {
        markTBIUrl(tbi, url.toString());
    }
}
