package icescrum2.presentation.scrumos;

import icescrum2.dao.model.IRole;
import icescrum2.presentation.app.chat.ChatSession;
import icescrum2.presentation.app.productbacklog.ProductBacklogUI;
import icescrum2.presentation.app.productconnexion.ProductConnexionUI;
import icescrum2.presentation.app.tests.TestsUI;
import icescrum2.presentation.broadcast.BroadcastManager;
import icescrum2.presentation.broadcast.RenderableSession;
import icescrum2.presentation.model.ProductImpl;
import icescrum2.presentation.model.UserImpl;
import icescrum2.presentation.scrumos.effects.ResizeTo;
import icescrum2.presentation.scrumos.model.BroadcastListener;
import icescrum2.presentation.scrumos.model.CallBack;
import icescrum2.presentation.scrumos.model.Widgetable;
import icescrum2.presentation.scrumos.model.Windowable;
import icescrum2.service.ConfigurationService;
import icescrum2.service.ProductService;
import icescrum2.service.SpringApplicationContext;
import icescrum2.service.beans.Action;
import icescrum2.service.beans.ProgressObject;
import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.Cookie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.icesoft.faces.component.ext.HtmlCommandButton;
import com.icesoft.faces.component.panelpositioned.PanelPositionedEvent;
import com.icesoft.faces.context.BridgeExternalContext;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;

public class ScrumOScontroller implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int DEFAULT_PAGINATION_SETTING = 15;

    private static Log log = LogFactory.getLog(ScrumOScontroller.class);

    private UserImpl user;

    private ProductImpl product;

    private boolean rememberMe = false;

    private boolean openedProduct;

    private List<Widgetable> widgetList = new LinkedList<Widgetable>();

    private Widgetable lastWidgetClosed;

    private Windowable lastWindow;

    private Windowable mainWindow = null;

    private Boolean openPanelChat = false;

    private Windowable popupWindow = null;

    private Effect windowEffect = null;

    private Effect fisheyeEffect = null;

    private Effect titleEffect = null;

    private ResizeTo wcEffect = null;

    private Boolean popupRendered = Boolean.FALSE;

    private Boolean popupRenderedModal = Boolean.FALSE;

    private Boolean fisheyeRendered = Boolean.FALSE;

    private Boolean windowRendered = Boolean.FALSE;

    private HtmlCommandButton btn_disableRendered = new HtmlCommandButton();

    private BroadcastManager broadcastManager;

    private boolean connectionLost = false;

    private Map<String, CallBack> registeredCallbacks = new HashMap<String, CallBack>();

    private String currentCallBack = "";

    private String toWinWrapper = "";

    private boolean offlineMode = false;

    private ChatSession chatSession;

    private File requestedFile;

    private AppLauncher appLauncher = new AppLauncher(this);

    private boolean animationActivated = true;

    private ProgressObject progressBar;

    private Integer paginationSetting = DEFAULT_PAGINATION_SETTING;

    private Integer paginationSettingMin = 15;

    private String lastVersion;

    private Boolean is2UpdateAvailable = null;

    private Date lastCheck;

    public Integer getPaginationSetting() {
        return paginationSetting;
    }

    public void setPaginationSetting(Integer paginationSetting) {
        this.paginationSetting = paginationSetting;
    }

    public Integer getPaginationSettingMin() {
        return paginationSettingMin;
    }

    public void setPaginationSettingMin(Integer paginationSetting) {
        this.paginationSettingMin = paginationSetting;
    }

    public ScrumOScontroller() {
    }

    public UserImpl getUser() {
        return user;
    }

    public void setUser(UserImpl user) {
        this.user = user;
    }

    public List<Widgetable> getWidgetList() {
        return widgetList;
    }

    public void setWidgetList(List<Widgetable> widgetList) {
        this.widgetList = widgetList;
    }

    public Windowable getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(Windowable mainWindow) {
        this.mainWindow = mainWindow;
    }

    public Boolean getPopupRendered() {
        return popupRendered;
    }

    public void setPopupRendered(Boolean popupRendered) {
        this.popupRendered = popupRendered;
    }

    public Boolean getFisheyeRendered() {
        return fisheyeRendered;
    }

    public void setFisheyeRendered(Boolean fisheyeRenrered) {
        this.fisheyeRendered = fisheyeRenrered;
    }

    public void newProductWizard(ActionEvent evt) {
        this.popupRendered = Boolean.TRUE;
    }

    public Windowable getPopupWindow() {
        return popupWindow;
    }

    public void setPopupWindow(Windowable popupWindow) {
        this.popupWindow = popupWindow;
    }

    public Boolean getPopupRenderedModal() {
        return popupRenderedModal;
    }

    public ProductImpl getProduct() {
        return product;
    }

    public void setProduct(ProductImpl product) {
        this.product = product;
    }

    public Effect getWindowEffect() {
        return windowEffect;
    }

    public Effect getFisheyeEffect() {
        return fisheyeEffect;
    }

    public void setFisheyeEffect(Effect fisheyeEffect) {
        this.fisheyeEffect = fisheyeEffect;
    }

    public void setWindowEffect(Effect windowEffect) {
        this.windowEffect = windowEffect;
    }

    public AppLauncher getAppLauncher() {
        return appLauncher;
    }

    public Boolean getWindowRendered() {
        return windowRendered;
    }

    public void setWindowRendered(Boolean windowRendered) {
        this.windowRendered = windowRendered;
    }

    public Windowable getLastWindow() {
        return this.lastWindow;
    }

    private void toWidget(boolean closeWin) {
        if (this.mainWindow != null && this.mainWindow.getWidgetable() && !this.widgetList.contains(this.mainWindow)) {
            this.widgetList.add((Widgetable) this.mainWindow);
            if (closeWin) {
                if (this.mainWindow != null) {
                    ((ScrumOSdnd) this.getManagedValue("#{scrumOSdnd}")).closeTooltip(null);
                    this.mainWindow = null;
                }
            }
        }
    }

    public String toWidget() {
        this.toWidget(true);
        return "widgetAdded";
    }

    public void closeWidget(ActionEvent ae) {
        String idUI = (String) ((HtmlCommandButton) ae.getComponent()).getValue();
        Widgetable widget = null;
        for (Widgetable fetchWidget : this.widgetList) {
            if (fetchWidget.getWindowId().equals(idUI)) {
                widget = fetchWidget;
                break;
            }
        }
        if (widget != null) {
            this.closeWidget(widget, this.animationActivated);
        }
    }

    public void upWidgets(ActionEvent ae) {
        if (this.widgetList.size() > 1) {
            this.widgetList.add(this.widgetList.remove(0));
        }
    }

    public void downWidgets(ActionEvent ae) {
        if (this.widgetList.size() > 1) {
            this.widgetList.add(0, this.widgetList.remove(this.widgetList.size() - 1));
        }
    }

    public void widgetToWindow(ActionEvent ae) {
        String idUI = (String) ((HtmlCommandButton) ae.getComponent()).getValue();
        Widgetable widget = null;
        for (Widgetable fetchWidget : this.widgetList) {
            if (fetchWidget.getWindowId().equals(idUI)) {
                widget = fetchWidget;
                break;
            }
        }
        if (this.mainWindow != null) {
            this.toWidget(false);
        }
        if (widget != null) {
            this.mainWindow = widget;
            this.openWindow(widget, Boolean.TRUE);
        }
    }

    public String logout() {
        if (this.product != null) {
            this.closeDesktop();
        }
        try {
            getRSession().dispose();
        } catch (Exception e) {
        }
        clearCookies();
        return "is2_logout";
    }

    private void clearCookies() {
        BridgeExternalContext bec = ((BridgeExternalContext) (FacesContext.getCurrentInstance().getExternalContext()));
        Cookie btuser = new Cookie("btuser", user.getLogin());
        Cookie btpasswd = new Cookie("btpasswd", null);
        Cookie btremember = new Cookie("btremember", "false");
        Cookie btproduct = new Cookie("btproduct", null);
        btuser.setMaxAge(0);
        btproduct.setMaxAge(0);
        btpasswd.setMaxAge(0);
        btremember.setMaxAge(0);
        bec.addCookie(btproduct);
        bec.addCookie(btuser);
        bec.addCookie(btpasswd);
        bec.addCookie(btremember);
        this.rememberMe = false;
    }

    public RenderableSession getRSession() {
        return (RenderableSession) getManagedValue("#{rSession}");
    }

    public void openWindow(Windowable mainWindow, Boolean verifyWidget) {
        if (this.mainWindow != null) {
            this.toWidget(false);
        }
        this.lastWindow = this.mainWindow;
        this.setMainWindow(mainWindow);
        this.mainWindow.openWindow();
        int i = 0;
        Boolean found = Boolean.FALSE;
        while (verifyWidget && i < this.widgetList.size() && !found) {
            if (mainWindow.getWindowId().equalsIgnoreCase(this.widgetList.get(i).getWindowId())) {
                found = Boolean.TRUE;
            }
            i++;
        }
        if (found) {
            this.closeWidget(this.widgetList.get(--i), this.animationActivated);
        }
        ScrumOSdnd sdnd = (ScrumOSdnd) this.getManagedValue("#{scrumOSdnd}");
        sdnd.setTooltipObject(null);
    }

    public void closeWidget(Widgetable widget, boolean effect) {
        widget.closeWidget();
        this.lastWidgetClosed = widget;
        this.disableRendered();
    }

    public void closePopup(ActionEvent ae) {
        if (this.popupWindow.processContent()) {
            closePopupWithoutProcess(ae);
        }
    }

    public String disableRendered() {
        if (this.mainWindow == null) {
            this.windowRendered = Boolean.FALSE;
        } else {
            this.windowRendered = Boolean.TRUE;
        }
        if (this.lastWidgetClosed != null) {
            this.widgetList.remove(this.lastWidgetClosed);
            this.lastWidgetClosed = null;
        }
        if (this.product != null) {
            this.fisheyeRendered = Boolean.TRUE;
        }
        if (FacesContext.getCurrentInstance() != null) {
            this.btn_disableRendered.requestFocus();
        }
        return "renderersDisabled";
    }

    public String closeApplication() {
        if (this.mainWindow != null) {
            this.mainWindow.processContent();
        }
        return this.closeWindow();
    }

    public String closeWindow() {
        if (this.mainWindow != null) {
            ((ScrumOSdnd) this.getManagedValue("#{scrumOSdnd}")).closeTooltip(null);
            this.mainWindow.closeWindow();
            this.mainWindow = null;
        }
        return "windowClosed";
    }

    public String closeDesktop() {
        try {
            this.btn_disableRendered.requestFocus();
            this.openedProduct = false;
            this.broadcastManager.closeProduct(this.product, this);
            this.closeWindow();
            this.closeAllWidgets();
            this.user.setActualRoleId(-1);
            chatSession.dispose();
            this.product = null;
            return "desktopClosed";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeAllWidgets() {
        this.widgetList.clear();
        if (this.getOpenPanelChat()) {
            openPanelChat = false;
        }
    }

    public void closePopupWithoutProcess(ActionEvent ae) {
        this.windowRendered = Boolean.FALSE;
        this.popupRenderedModal = Boolean.FALSE;
        this.popupRendered = Boolean.FALSE;
        this.popupWindow = null;
    }

    public void openPopup(Windowable popup) {
        if (popup.prepareWindow()) {
            this.popupWindow = popup;
            popup.openWindow();
            this.popupRenderedModal = Boolean.TRUE;
        }
    }

    public void menuBarProductAction(ProductImpl p) {
        ProductConnexionUI pcUI = (ProductConnexionUI) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{productConnexionUI}").getValue(FacesContext.getCurrentInstance());
        pcUI.readProduct(p);
        this.openPopup(pcUI);
    }

    public void openProduct(String idProduct) {
        ProductImpl p = (ProductImpl) ((ProductService) (SpringApplicationContext.getBean("ProductService"))).getProduct(idProduct);
        this.openProduct(p);
    }

    public void openProduct(ProductImpl p) {
        if (p == null) return;
        if (this.product != null) {
            this.closeDesktop();
        }
        this.setProduct(p);
        if (!this.user.isAdmin()) {
            this.user.setActualRoleId(this.product.getRoleMap().get(user.getIdUser()).getId_role());
        } else {
            this.user.setActualRoleId(IRole.ADMIN_ROLE);
        }
        this.openedProduct = true;
        broadcastManager.openProduct(p);
        if (this.rememberMe) {
            Cookie btproduct = new Cookie("btproduct", p.getIdProduct());
            btproduct.setMaxAge(3153600);
            ((BridgeExternalContext) (FacesContext.getCurrentInstance().getExternalContext())).addCookie(btproduct);
        }
        this.appLauncher.goActiveSprint();
    }

    public String invokeFisheye() {
        this.fisheyeRendered = Boolean.TRUE;
        return null;
    }

    public void sendCallToUser(BroadcastListener evt, ScrumOScontroller session) {
        this.getBroadcastManager().launchUserUpdate(session, evt);
    }

    public void sendCallToGroup(BroadcastListener evt) {
        this.getBroadcastManager().launchGroupUpdate(this, evt);
    }

    public void sendCallToGroup(String javascript) {
        if (this.product != null) {
            this.getBroadcastManager().sendJScallToGroup(javascript, broadcastManager.otherUsers(this.getProduct().getIdProduct(), this));
        }
    }

    public void sendCallToApp(String javascript) {
        Set<ScrumOScontroller> sessions = null;
        for (List<ScrumOScontroller> entry : broadcastManager.getSessionsByProduct().values()) {
            sessions = new HashSet<ScrumOScontroller>();
            for (ScrumOScontroller r : entry) {
                sessions.add(r);
            }
            this.getBroadcastManager().sendJScallToGlobal(javascript, sessions);
        }
    }

    public void sendCallToApp(BroadcastListener evt) {
        RenderableSession session = ((RenderableSession) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{rSession}").getValue(FacesContext.getCurrentInstance()));
        this.getBroadcastManager().launchAppUpdate(session, evt);
    }

    public HtmlCommandButton getBtn_disableRendered() {
        return btn_disableRendered;
    }

    public void setBtn_disableRendered(HtmlCommandButton btn_disableRendered) {
        this.btn_disableRendered = btn_disableRendered;
    }

    public Effect getTitleEffect() {
        return titleEffect;
    }

    public void onWidgetPositionChange(PanelPositionedEvent evt) {
    }

    public void addRegisteredCallbackAfterJSAnim(String id, Effect e, CallBack cb) {
        String lastCall = "scrumOScallback('" + id + "');";
        e.toString("document.getElementById('" + id + "')", lastCall);
        this.registeredCallbacks.put(id, cb);
    }

    public String serverSideCallBacks() {
        CallBack cb = this.registeredCallbacks.get(this.currentCallBack);
        FacesContext fc = FacesContext.getCurrentInstance();
        if (cb != null) {
            fc.getApplication().createMethodBinding(cb.getEl(), cb.getValuesClass()).invoke(fc, cb.getValues());
            this.registeredCallbacks.remove(this.currentCallBack);
        }
        return null;
    }

    public Effect getWcEffect() {
        return wcEffect;
    }

    public String getCurrentCallBack() {
        return currentCallBack;
    }

    public void setCurrentCallBack(String currentCallBack) {
        this.currentCallBack = currentCallBack;
    }

    public static Object getManagedValue(String el) {
        if (FacesContext.getCurrentInstance() != null) {
            return FacesContext.getCurrentInstance().getApplication().createValueBinding(el).getValue(FacesContext.getCurrentInstance());
        }
        return null;
    }

    public boolean isAnimationActivated() {
        return animationActivated;
    }

    public void setAnimationActivated(boolean animationActivated) {
        this.animationActivated = animationActivated;
    }

    public boolean isOpenedProduct() {
        return openedProduct;
    }

    public void setOpenedProduct(boolean openedProduct) {
        this.openedProduct = openedProduct;
    }

    public String getHeadTitle() {
        if (popupWindow != null) {
            return popupWindow.getWindowTitle();
        }
        if (mainWindow != null) {
            return mainWindow.getWindowTitle();
        }
        return "";
    }

    public String getCurrentPopupPath() {
        if (popupWindow != null) {
            return popupWindow.getWindowPath();
        }
        return "../errors/404.jspx";
    }

    public String getCurrentWindowPath() {
        if (mainWindow != null) {
            return mainWindow.getWindowPath();
        }
        return "../errors/404.jspx";
    }

    public String getToWinWrapper() {
        return toWinWrapper;
    }

    public void setToWinWrapper(String toWinWrapper) {
        this.toWinWrapper = toWinWrapper;
    }

    public BroadcastManager getBroadcastManager() {
        return broadcastManager;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScrumOScontroller other = (ScrumOScontroller) obj;
        if (user == null) {
            if (other.user != null) {
                return false;
            }
        } else if (!user.equals(other.user)) {
            return false;
        }
        return true;
    }

    public void setBroadcastManager(BroadcastManager broadcastManager) {
        this.broadcastManager = broadcastManager;
    }

    public Boolean getOpenPanelChat() {
        return openPanelChat;
    }

    public void switchPanelChat(ActionEvent ae) {
        this.openPanelChat = !this.openPanelChat;
    }

    public boolean isConnectionLost() {
        return connectionLost;
    }

    public void setConnectionLost(boolean connectionLost) {
        this.connectionLost = connectionLost;
    }

    public int getActualUserRole() {
        return this.user.getActualRoleId();
    }

    public File getRequestedFile() {
        return requestedFile;
    }

    public void setRequestedFile(File requestedFile) {
        this.requestedFile = requestedFile;
    }

    public void goProgressBar(Action action) {
        this.progressBar = new ProgressObject(this.getRSession(), action);
    }

    public ProgressObject getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressObject progressBar) {
        this.progressBar = progressBar;
    }

    public ChatSession getChatSession() {
        return chatSession;
    }

    public void setChatSession(ChatSession chatSession) {
        this.chatSession = chatSession;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    public boolean containsWidget(String id) {
        Boolean found = Boolean.FALSE;
        int i = 0;
        while (i < this.widgetList.size() && !found) {
            if (mainWindow.getWindowId().equalsIgnoreCase(this.widgetList.get(i).getWindowId())) {
                found = Boolean.TRUE;
            }
            i++;
        }
        return found;
    }

    public Widgetable getWidget(String id) {
        int i = 0;
        while (i < this.widgetList.size()) {
            if (mainWindow.getWindowId().equalsIgnoreCase(this.widgetList.get(i).getWindowId())) {
                return this.widgetList.get(i);
            }
            i++;
        }
        return null;
    }

    public boolean isOpenedUI(Class<?> view) {
        Boolean found = Boolean.FALSE;
        int i = 0;
        if (mainWindow != null && mainWindow.getClass().equals(view)) {
            found = Boolean.TRUE;
        }
        while (found && i < this.widgetList.size() && !found) {
            if (this.widgetList.get(i).getClass().equals(view)) {
                found = Boolean.TRUE;
            }
            i++;
        }
        return found;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public boolean checkVersion() {
        if (this.is2UpdateAvailable == null && this.lastCheck == null) {
            realCheck();
        } else if ((lastCheck.getTime() + (3600 * 24 * 1000)) < new Date().getTime()) {
            realCheck();
        }
        return this.is2UpdateAvailable;
    }

    public Widgetable getLastWidgetClosed() {
        return lastWidgetClosed;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public boolean isIs2UpdateAvailable() {
        return checkVersion();
    }

    public void setIs2UpdateAvailable(boolean is2UpdateAvailable) {
        this.is2UpdateAvailable = is2UpdateAvailable;
    }

    private void realCheck() {
        ConfigurationService conf = ((ConfigurationService) SpringApplicationContext.getBean("ConfigurationServiceNoAOP"));
        String currentVersion = LocaleBean.get("is2_version");
        String lastVersion = conf.getLastVersion();
        if (lastVersion == null || lastVersion.equals(currentVersion)) {
            this.lastVersion = currentVersion;
            this.is2UpdateAvailable = false;
        } else {
            this.lastVersion = lastVersion;
            this.is2UpdateAvailable = true;
        }
        this.lastCheck = new Date();
    }
}
