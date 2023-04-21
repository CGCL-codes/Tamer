package org.broadleafcommerce.openadmin.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.BkgndRepeat;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.IMenuButton;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import org.broadleafcommerce.openadmin.client.BLCMain;
import org.broadleafcommerce.openadmin.client.Module;
import org.broadleafcommerce.openadmin.client.security.SecurityManager;
import org.broadleafcommerce.openadmin.client.setup.AppController;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * 
 * @author jfischer
 *
 */
public class MasterView extends VLayout {

    protected Canvas canvas;

    protected ToolStrip bottomBar;

    protected Label status;

    protected Label selectedPrimaryMenuOption;

    protected Label selectedSecondaryMenuOption;

    protected HLayout secondaryMenu = new HLayout();

    public static String moduleKey;

    protected String pageKey;

    protected LinkedHashMap<String, Module> modules;

    public MasterView(String moduleKey, String pageKey, LinkedHashMap<String, Module> modules) {
        MasterView.moduleKey = moduleKey;
        this.pageKey = pageKey;
        this.modules = modules;
        setWidth100();
        setHeight100();
        addMember(buildHeader());
        addMember(buildPrimaryMenu());
        addMember(buildSecondaryMenu(pageKey));
        canvas = new HLayout();
        canvas.setWidth100();
        canvas.setHeight100();
        addMember(canvas);
        buildFooter();
    }

    private Layout buildHeader() {
        HLayout header = new HLayout();
        header.setWidth100();
        header.setLayoutMargin(10);
        header.setBackgroundImage(GWT.getModuleBaseURL() + "admin/images/header_bg.png");
        LayoutSpacer sp = new LayoutSpacer();
        sp.setWidth(20);
        header.addMember(sp);
        header.addMember(buildLogo());
        header.addMember(new LayoutSpacer());
        VLayout userAndLocale = new VLayout();
        userAndLocale.setHeight100();
        userAndLocale.addMember(buildUserInfo());
        header.addMember(userAndLocale);
        return header;
    }

    private void addAuthorizedModulesToMenu(Layout menuHolder) {
        Collection<Module> allowedModules = modules.values();
        for (Iterator<Module> iterator = allowedModules.iterator(); iterator.hasNext(); ) {
            Module testModule = iterator.next();
            if (!SecurityManager.getInstance().isUserAuthorizedToViewModule(testModule.getModuleKey())) {
                iterator.remove();
                if (moduleKey != null && moduleKey.equals(testModule.getModuleKey())) {
                    moduleKey = null;
                }
            }
        }
        if (moduleKey == null && allowedModules.size() > 0) {
            moduleKey = allowedModules.iterator().next().getModuleKey();
        }
        for (Module module : allowedModules) {
            boolean selected = module.getModuleKey().equals(moduleKey);
            menuHolder.addMember(buildPrimaryMenuOption(module, selected));
            menuHolder.addMember(buildMenuSpacer());
        }
    }

    private Layout buildPrimaryMenu() {
        HLayout moduleLayout = new HLayout();
        moduleLayout.setWidth100();
        moduleLayout.setHeight(38);
        moduleLayout.setBackgroundImage(GWT.getModuleBaseURL() + "admin/images/nav_bg.png");
        moduleLayout.setBackgroundRepeat(BkgndRepeat.REPEAT_X);
        moduleLayout.addMember(new LayoutSpacer());
        HLayout primaryMenuOptionsHolder = new HLayout();
        primaryMenuOptionsHolder.setLayoutAlign(VerticalAlignment.BOTTOM);
        primaryMenuOptionsHolder.setMembersMargin(5);
        primaryMenuOptionsHolder.setWidth100();
        primaryMenuOptionsHolder.setHeight(30);
        primaryMenuOptionsHolder.setAlign(Alignment.LEFT);
        LayoutSpacer sp = new LayoutSpacer();
        sp.setWidth(20);
        primaryMenuOptionsHolder.addMember(sp);
        addAuthorizedModulesToMenu(primaryMenuOptionsHolder);
        moduleLayout.addMember(primaryMenuOptionsHolder);
        return moduleLayout;
    }

    private Layout buildSecondaryMenu(String selectedPage) {
        Module currentModule = modules.get(moduleKey);
        secondaryMenu.removeMembers(secondaryMenu.getMembers());
        LayoutSpacer sp2 = new LayoutSpacer();
        sp2.setWidth(10);
        secondaryMenu.addMember(sp2);
        secondaryMenu.setHeight(40);
        secondaryMenu.setBackgroundImage(GWT.getModuleBaseURL() + "admin/images/nav_sec_bg.png");
        secondaryMenu.addMember(sp2);
        LinkedHashMap<String, String[]> pages = modules.get(moduleKey).getPages();
        Collection<String> allowedPages = pages.keySet();
        for (Iterator<String> iterator = allowedPages.iterator(); iterator.hasNext(); ) {
            String testPage = (String) iterator.next();
            if (!SecurityManager.getInstance().isUserAuthorizedToViewSection(pages.get(testPage)[0])) {
                iterator.remove();
                if (selectedPage != null && selectedPage.equals(testPage)) {
                    selectedPage = null;
                }
            }
        }
        if (selectedPage == null && allowedPages.size() > 0) {
            selectedPage = (String) allowedPages.iterator().next();
        }
        for (String page : allowedPages) {
            boolean selected = (page.equals(selectedPage));
            secondaryMenu.addMember(buildSecondaryMenuOption(page, selected));
            if (selectedPage == null) {
                selectedPage = page;
            }
            selected = false;
        }
        return secondaryMenu;
    }

    private Canvas buildLogo() {
        ImgButton logo = new ImgButton();
        logo.setSrc(GWT.getModuleBaseURL() + "admin/images/blc_logo_white.png");
        logo.setWidth(149);
        logo.setHeight(71);
        logo.setShowRollOver(false);
        logo.setShowDownIcon(false);
        logo.setShowDown(false);
        return logo;
    }

    private Canvas buildMenuSpacer() {
        ImgButton spacer = new ImgButton();
        spacer.setSrc(GWT.getModuleBaseURL() + "admin/images/nav_spacer_36.png");
        spacer.setWidth(2);
        spacer.setHeight(30);
        spacer.setShowRollOver(false);
        spacer.setShowDownIcon(false);
        spacer.setShowDown(false);
        return spacer;
    }

    private Label buildPrimaryMenuOption(final Module module, boolean selected) {
        Label tmp = new Label(module.getModuleTitle());
        tmp.setValign(VerticalAlignment.CENTER);
        tmp.setHeight(30);
        tmp.setAlign(Alignment.CENTER);
        tmp.setWrap(false);
        tmp.setPadding(0);
        tmp.setShowRollOver(true);
        tmp.setCursor(Cursor.POINTER);
        final String style;
        if (selected) {
            style = "primaryMenuText-selected";
            selectedPrimaryMenuOption = tmp;
        } else {
            style = "primaryMenuText";
        }
        tmp.setBaseStyle(style);
        tmp.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                Object o = event.getSource();
                if (o instanceof Label) {
                    final Label lbl = (Label) o;
                    if (!lbl.getTitle().equals(selectedPrimaryMenuOption.getTitle())) {
                        selectedPrimaryMenuOption.setBaseStyle("primaryMenuText");
                        lbl.setBaseStyle("primaryMenuText-selected");
                        selectedPrimaryMenuOption = lbl;
                        moduleKey = module.getModuleKey();
                        BLCMain.setCurrentModuleKey(moduleKey);
                        buildSecondaryMenu(null);
                        AppController.getInstance().go(canvas, module.getPages(), null, false);
                    }
                }
            }
        });
        return tmp;
    }

    private Label buildSecondaryMenuOption(final String title, boolean selected) {
        Label tmp = new Label(title);
        tmp.setTitle(title);
        tmp.setWrap(false);
        tmp.setValign(VerticalAlignment.BOTTOM);
        tmp.setAlign(Alignment.CENTER);
        tmp.setPadding(10);
        tmp.setShowRollOver(true);
        tmp.setCursor(Cursor.POINTER);
        String style;
        if (selected) {
            style = "secondaryMenuText-selected";
            selectedSecondaryMenuOption = tmp;
        } else {
            style = "secondaryMenuText";
        }
        tmp.setBaseStyle(style);
        tmp.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Object o = event.getSource();
                if (o instanceof Label) {
                    Label lbl = (Label) o;
                    if (!lbl.getTitle().equals(selectedSecondaryMenuOption.getTitle())) {
                        selectedSecondaryMenuOption.setBaseStyle("secondaryMenuText");
                        lbl.setBaseStyle("secondaryMenuText-selected");
                        selectedSecondaryMenuOption = lbl;
                        BLCMain.setCurrentPageKey(lbl.getTitle());
                        History.newItem("moduleKey=" + moduleKey + "&pageKey=" + lbl.getTitle());
                    }
                }
            }
        });
        return tmp;
    }

    private Canvas buildLocaleSelection() {
        String[] languages = { "English", "Spanish" };
        String[] languageCodes = { "en", "sp" };
        Menu localeMenu = new Menu();
        MenuItem[] menuItems = new MenuItem[languages.length];
        for (int i = 0; i < languages.length; i++) {
            MenuItem tempMenuItem = new MenuItem(languages[i]);
            menuItems[i] = tempMenuItem;
        }
        localeMenu.setData(menuItems);
        localeMenu.setShowIcons(false);
        String currentLanguage = languages[0];
        IMenuButton moduleSelectionButton = new IMenuButton(currentLanguage, localeMenu);
        moduleSelectionButton.setOverflow(Overflow.VISIBLE);
        return moduleSelectionButton;
    }

    private Canvas buildUserImage() {
        ImgButton logo = new ImgButton();
        logo.setSrc(GWT.getModuleBaseURL() + "admin/images/user.png");
        logo.setSize(16);
        logo.setShowRollOver(false);
        logo.setShowDownIcon(false);
        logo.setShowDown(false);
        return logo;
    }

    private Canvas buildLogoutImage() {
        ImgButton logo = new ImgButton();
        logo.setSrc(GWT.getModuleBaseURL() + "admin/images/logout_arrow.png");
        logo.setSize(11);
        logo.setValign(VerticalAlignment.CENTER);
        logo.setShowRollOver(false);
        logo.setShowDownIcon(false);
        logo.setShowDown(false);
        return logo;
    }

    private Canvas buildUserInfo() {
        HLayout userFields = new HLayout();
        userFields.setAlign(Alignment.RIGHT);
        userFields.addMember(buildUserImage());
        Label userLabel = new Label(SecurityManager.USER.getUserName());
        userLabel.setBaseStyle("userText");
        userLabel.setWidth(1);
        userLabel.setOverflow(Overflow.VISIBLE);
        userFields.addMember(userLabel);
        userFields.addMember(buildLogoutImage());
        Label logoutLink = new Label("LOGOUT");
        logoutLink.setCursor(Cursor.POINTER);
        logoutLink.setShowRollOver(true);
        logoutLink.setBaseStyle("userLogout");
        logoutLink.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                UrlBuilder builder = Window.Location.createUrlBuilder();
                builder.setPath(BLCMain.webAppContext + "/adminLogout.htm");
                builder.setParameter("time", String.valueOf(System.currentTimeMillis()));
                Window.open(builder.buildString(), "_self", null);
            }
        });
        userFields.addMember(logoutLink);
        return userFields;
    }

    private void buildFooter() {
        bottomBar = new ToolStrip();
        bottomBar.setBackgroundImage(GWT.getModuleBaseURL() + "admin/images/header_bg.png");
        bottomBar.setHeight(30);
        bottomBar.setWidth100();
        status = new Label();
        status.setWrap(false);
        bottomBar.addSpacer(6);
        bottomBar.addMember(status);
        bottomBar.addFill();
        bottomBar.addMember(BLCMain.NON_MODAL_PROGRESS);
        bottomBar.addSpacer(5);
        addMember(bottomBar);
    }

    public Canvas getContainer() {
        return canvas;
    }

    public ToolStrip getBottomBar() {
        return bottomBar;
    }

    public Label getStatus() {
        return status;
    }

    public void clearStatus() {
        status.setContents("");
    }
}
