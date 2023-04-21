package org.openremote.web.console.unit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.dom.client.Style;
import org.openremote.web.console.controller.Controller;
import org.openremote.web.console.controller.ControllerCredentials;
import org.openremote.web.console.controller.EnumControllerResponseCode;
import org.openremote.web.console.event.ConsoleUnitEventManager;
import org.openremote.web.console.event.hold.*;
import org.openremote.web.console.event.rotate.*;
import org.openremote.web.console.event.sensor.SensorChangeEvent;
import org.openremote.web.console.event.swipe.*;
import org.openremote.web.console.event.swipe.SwipeEvent.SwipeDirection;
import org.openremote.web.console.event.ui.*;
import org.openremote.web.console.panel.Panel;
import org.openremote.web.console.panel.SystemPanel;
import org.openremote.web.console.panel.entity.DataValuePair;
import org.openremote.web.console.panel.entity.Gesture;
import org.openremote.web.console.panel.entity.Navigate;
import org.openremote.web.console.panel.entity.Screen;
import org.openremote.web.console.panel.entity.TabBar;
import org.openremote.web.console.service.*;
import org.openremote.web.console.util.BrowserUtils;
import org.openremote.web.console.util.PollingHelper;
import org.openremote.web.console.view.ScreenViewImpl;
import org.openremote.web.console.widget.ScreenIndicator;
import org.openremote.web.console.widget.TabBarComponent;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerManager;

public class ConsoleUnit extends VerticalPanel implements RotationHandler, WindowResizeHandler, SwipeHandler, HoldHandler, NavigateHandler, CommandSendHandler {

    public static final int MIN_WIDTH = 320;

    public static final int MIN_HEIGHT = 460;

    public static final int DEFAULT_DISPLAY_WIDTH = 320;

    public static final int DEFAULT_DISPLAY_HEIGHT = 460;

    public static final String DEFAULT_DISPLAY_COLOUR = "#000";

    public static final String CONSOLE_HTML_ELEMENT_ID = "consoleUnit";

    public static final int FRAME_WIDTH_TOP = 20;

    public static final int FRAME_WIDTH_BOTTOM = 50;

    public static final int FRAME_WIDTH_LEFT = 20;

    public static final int FRAME_WIDTH_RIGHT = 20;

    public static final int BOSS_WIDTH = 2;

    public static final String LOGO_TEXT_LEFT = "Open";

    public static final String LOGO_TEXT_RIGHT = "Remote";

    protected ConsoleDisplay consoleDisplay;

    private Boolean isFullscreen = true;

    protected int width;

    protected int height;

    private String orientation = "portrait";

    private ControllerService controllerService = JSONPControllerService.getInstance();

    private PanelService panelService = PanelServiceImpl.getInstance();

    private LocalDataService dataService = LocalDataServiceImpl.getInstance();

    private ScreenViewService screenViewService = ScreenViewService.getInstance();

    private ControllerCredentials currentControllerCredentials = null;

    private Panel systemPanel = null;

    private String currentPanelName = null;

    private Integer currentGroupId = 0;

    private Integer currentScreenId = 0;

    private Map<SwipeDirection, Gesture> gestureMap = new HashMap<SwipeDirection, Gesture>();

    private Map<Integer, PollingHelper> pollingHelperMap = new HashMap<Integer, PollingHelper>();

    private PopupPanel alertPopup;

    private HorizontalPanel logoPanel;

    AsyncControllerCallback<Map<Integer, String>> pollingCallback = new AsyncControllerCallback<Map<Integer, String>>() {

        @Override
        public void onSuccess(Map<Integer, String> result) {
            HandlerManager eventBus = ConsoleUnitEventManager.getInstance().getEventBus();
            for (Iterator<Integer> it = result.keySet().iterator(); it.hasNext(); ) {
                Integer id = it.next();
                String sensorValue = result.get(id);
                eventBus.fireEvent(new SensorChangeEvent(id, sensorValue));
            }
        }
    };

    public enum EnumSystemScreen {

        CONTROLLER_LIST(51, 3, "controllerlist"), ADD_EDIT_CONTROLLER(51, 3, "editcontroller"), CONSOLE_SETTINGS(51, 3, "settings"), LOGIN(51, 3, "login"), LOGOUT(51, 3, "logout"), PANEL_SELECTION(51, 3, "panelselection");

        private final int id;

        private final int groupId;

        private final String name;

        EnumSystemScreen(int id, int groupId, String name) {
            this.id = id;
            this.groupId = groupId;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public int getGroupId() {
            return groupId;
        }

        public String getName() {
            return name;
        }

        public static EnumSystemScreen getSystemScreen(int id) {
            EnumSystemScreen result = null;
            for (EnumSystemScreen screen : EnumSystemScreen.values()) {
                if (screen.getId() == id) {
                    result = screen;
                    break;
                }
            }
            return result;
        }

        public static EnumSystemScreen getSystemScreen(String name) {
            EnumSystemScreen result = null;
            for (EnumSystemScreen screen : EnumSystemScreen.values()) {
                if (screen.getName().equalsIgnoreCase(name)) {
                    result = screen;
                    break;
                }
            }
            return result;
        }
    }

    public ConsoleUnit() {
        this(DEFAULT_DISPLAY_WIDTH, DEFAULT_DISPLAY_HEIGHT);
    }

    public ConsoleUnit(int width, int height) {
        setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        if (width > height) {
            int tempWidth = height;
            height = width;
            width = tempWidth;
        }
        this.width = width;
        this.height = height;
        consoleDisplay = new ConsoleDisplay();
        add(consoleDisplay);
        consoleDisplay.getElement().getStyle().setBackgroundColor(DEFAULT_DISPLAY_COLOUR);
        DOM.setStyleAttribute(RootPanel.getBodyElement(), "backgroundColor", DEFAULT_DISPLAY_COLOUR);
        DOM.setStyleAttribute(RootPanel.getBodyElement(), "background", DEFAULT_DISPLAY_COLOUR);
        getElement().setId(CONSOLE_HTML_ELEMENT_ID);
        addStyleName("portraitConsole");
        addStyleName("consoleUnit");
        registerHandlers();
        alertPopup = new PopupPanel(true);
        alertPopup.setWidget(new Label());
    }

    public void setSize(int width, int height) {
        boolean setFullscreen = false;
        if (width > height) {
            int tempWidth = height;
            height = width;
            width = tempWidth;
        }
        if (BrowserUtils.isMobile) {
            setFullscreen = true;
        } else {
            int winWidth = BrowserUtils.getWindowWidth();
            int winHeight = BrowserUtils.getWindowHeight();
            int maxDim = width > height ? width : height;
            String winOrientation = BrowserUtils.getWindowOrientation();
            width = width < MIN_WIDTH ? MIN_WIDTH : width;
            height = height < MIN_HEIGHT ? MIN_HEIGHT : height;
            if (maxDim >= winWidth || maxDim >= winHeight) {
                if (winOrientation.equals("portrait")) {
                    width = winWidth;
                    height = winHeight;
                } else {
                    width = winHeight;
                    height = winWidth;
                }
                setFullscreen = true;
            }
        }
        if (isFullscreen != setFullscreen) {
            showFrame(!setFullscreen);
        }
        this.width = width;
        this.height = height;
        isFullscreen = setFullscreen;
        if (setFullscreen) {
            setOrientation(BrowserUtils.getWindowOrientation());
        }
        setPosition(BrowserUtils.getWindowWidth(), BrowserUtils.getWindowHeight());
        consoleDisplay.setSize(width, height);
    }

    private void showFrame(boolean showFrame) {
        if (showFrame) {
            removeStyleName("fullscreenConsole");
            createFrame();
        } else {
            removeFrame();
        }
    }

    private void createFrame() {
        Style style = consoleDisplay.getElement().getStyle();
        style.setMarginTop(FRAME_WIDTH_TOP - BOSS_WIDTH, Unit.PX);
        style.setMarginRight(FRAME_WIDTH_RIGHT - BOSS_WIDTH, Unit.PX);
        style.setMarginLeft(FRAME_WIDTH_LEFT - BOSS_WIDTH, Unit.PX);
        style.setMarginBottom(-BOSS_WIDTH, Unit.PX);
        addStyleName("consoleFrame");
        addStyleName("resizableConsole");
        removeStyleName("fullscreenConsole");
        DOM.setStyleAttribute(RootPanel.getBodyElement(), "backgroundColor", "");
        DOM.setStyleAttribute(RootPanel.getBodyElement(), "background", "");
        style.setBorderWidth(BOSS_WIDTH, Unit.PX);
        style.setBorderStyle(BorderStyle.SOLID);
        style.setBorderColor("#333");
        if (logoPanel == null) {
            logoPanel = new HorizontalPanel();
            logoPanel.setStylePrimaryName("consoleFrameLogo");
            logoPanel.setHeight(FRAME_WIDTH_BOTTOM + "px");
            DOM.setStyleAttribute(logoPanel.getElement(), "lineHeight", FRAME_WIDTH_BOTTOM + "px");
            Label logoLeft = new Label();
            logoLeft.setText(LOGO_TEXT_LEFT);
            logoLeft.setHeight(FRAME_WIDTH_BOTTOM + "px");
            logoLeft.getElement().setId("consoleFrameLogoLeft");
            logoPanel.add(logoLeft);
            Label logoRight = new Label();
            logoRight.setText(LOGO_TEXT_RIGHT);
            logoRight.setHeight(FRAME_WIDTH_BOTTOM + "px");
            logoRight.getElement().setId("consoleFrameLogoRight");
            logoPanel.add(logoRight);
        }
        add(logoPanel);
    }

    private void removeFrame() {
        Style style = consoleDisplay.getElement().getStyle();
        style.clearMargin();
        style.clearBorderColor();
        style.clearBorderStyle();
        style.clearBorderWidth();
        remove(logoPanel);
        removeStyleName("resizableConsole");
        addStyleName("fullscreenConsole");
        removeStyleName("consoleFrame");
        DOM.setStyleAttribute(RootPanel.getBodyElement(), "backgroundColor", DEFAULT_DISPLAY_COLOUR);
        DOM.setStyleAttribute(RootPanel.getBodyElement(), "background", DEFAULT_DISPLAY_COLOUR);
    }

    public ConsoleDisplay getConsoleDisplay() {
        return this.consoleDisplay;
    }

    public int getWidth() {
        if (isFullscreen) {
            return width;
        } else {
            return width + FRAME_WIDTH_LEFT + FRAME_WIDTH_RIGHT;
        }
    }

    public int getHeight() {
        if (isFullscreen) {
            return height;
        } else {
            return height + FRAME_WIDTH_TOP + FRAME_WIDTH_BOTTOM;
        }
    }

    /**
	 * Position the console unit in the centre of the window
	 */
    public void setPosition(int winWidth, int winHeight) {
        int xPos = 0;
        int yPos = 0;
        if (BrowserUtils.isIE && orientation.equalsIgnoreCase("landscape")) {
            xPos = (int) Math.round(((double) winWidth / 2) - (getHeight() / 2));
            yPos = (int) Math.round(((double) winHeight / 2) - (getWidth() / 2));
        } else {
            xPos = (int) Math.round(((double) winWidth / 2) - (getWidth() / 2));
            yPos = (int) Math.round(((double) winHeight / 2) - (getHeight() / 2));
        }
        BrowserUtils.getConsoleContainer().setWidgetPosition(this, xPos, yPos);
    }

    /**
	 * Adjusts the CSS class to either landscape or portrait
	 * @param orientation
	 */
    public void setOrientation(String orientation) {
        if ("portrait".equals(orientation)) {
            getElement().removeClassName("landscapeConsole");
            getElement().addClassName("portraitConsole");
        } else {
            getElement().removeClassName("portraitConsole");
            getElement().addClassName("landscapeConsole");
        }
        this.orientation = orientation;
        setPosition(BrowserUtils.getWindowWidth(), BrowserUtils.getWindowHeight());
    }

    public String getOrientation() {
        return orientation;
    }

    public void hide() {
        setVisible(false);
    }

    public void show() {
        setVisible(true);
    }

    public void alert(String msg) {
        if (msg != null && msg.length() > 0) {
            Label label = (Label) alertPopup.getWidget();
            label.setText(msg);
            alertPopup.show();
        }
    }

    private void loadControllerAndPanel(ControllerCredentials controllerCreds, String panelName) {
        if (controllerCreds == null) {
            loadSettings(EnumSystemScreen.CONTROLLER_LIST, null);
        } else {
            currentControllerCredentials = controllerCreds;
            currentPanelName = panelName;
            controllerService.setController(new Controller(controllerCreds));
            controllerService.isAlive(new AsyncControllerCallback<Boolean>() {

                @Override
                public void onSuccess(Boolean isAlive) {
                    if (isAlive) {
                        if (currentPanelName != null && !currentPanelName.equalsIgnoreCase("")) {
                            loadPanel(currentPanelName);
                        } else {
                            loadSettings(EnumSystemScreen.PANEL_SELECTION, null);
                        }
                    } else {
                        loadSettings(EnumSystemScreen.CONTROLLER_LIST, null);
                    }
                }
            });
        }
    }

    private void loadPanel(String panelName) {
        controllerService.getPanel(panelName, new AsyncControllerCallback<Panel>() {

            @Override
            public void onFailure(EnumControllerResponseCode response) {
                if (response == EnumControllerResponseCode.PANEL_NOT_FOUND) {
                    loadSettings(EnumSystemScreen.PANEL_SELECTION, null);
                }
            }

            @Override
            public void onSuccess(Panel result) {
                if (result != null) {
                    setPanel(result);
                    initialisePanel();
                }
            }
        });
    }

    private void setPanel(Panel result) {
        if (result != null) {
            unloadPanel();
            panelService.setCurrentPanel(result);
        }
    }

    private void initialisePanel() {
        if (panelService.isInitialized()) {
            Integer defaultGroupId = panelService.getDefaultGroupId();
            Screen defaultScreen = panelService.getDefaultScreen(defaultGroupId);
            if (defaultScreen != null) {
                loadDisplay(defaultGroupId, defaultScreen, null);
            } else {
                loadSettings(EnumSystemScreen.PANEL_SELECTION, null);
            }
        } else {
            loadSettings(EnumSystemScreen.PANEL_SELECTION, null);
        }
    }

    private void unloadPanel() {
        panelService.setCurrentPanel(null);
        screenViewService.reset();
        consoleDisplay.clearDisplay();
        clearGestureMap();
        for (Integer screenId : pollingHelperMap.keySet()) {
            PollingHelper helper = pollingHelperMap.get(screenId);
            if (helper != null) {
                helper.stopMonitoring();
            }
        }
        pollingHelperMap.clear();
        currentGroupId = 0;
        currentScreenId = 0;
    }

    private void unloadControllerAndPanel() {
        unloadPanel();
        unloadController();
    }

    private void unloadController() {
        controllerService.setController(null);
    }

    public void reloadControllerAndPanel() {
        unloadControllerAndPanel();
        Timer reloadTimer = new Timer() {

            @Override
            public void run() {
                loadControllerAndPanel(currentControllerCredentials, currentPanelName);
            }
        };
        reloadTimer.schedule(2000);
    }

    public void restart() {
        unloadControllerAndPanel();
        onAdd();
    }

    private void loadSettings(EnumSystemScreen systemScreen, List<DataValuePair> data) {
        if (systemPanel == null) {
            systemPanel = SystemPanel.get();
        }
        if (systemPanel == null) {
            Window.alert("Cannot load system panel definition");
            return;
        }
        if (panelService.getCurrentPanel() != systemPanel) {
            unloadPanel();
            setPanel(systemPanel);
        }
        Integer groupId = systemScreen.getGroupId();
        Screen screen = panelService.getScreenById(systemScreen.getId());
        if (screen != null) {
            loadDisplay(groupId, screen, null);
        }
    }

    private void loadDisplay(Screen screen, List<DataValuePair> data) {
        loadDisplay(currentGroupId, screen, false, data);
    }

    private void loadDisplay(Screen screen, boolean orientationChanged, List<DataValuePair> data) {
        loadDisplay(currentGroupId, screen, orientationChanged, data);
    }

    private void loadDisplay(Integer newGroupId, Screen screen, List<DataValuePair> data) {
        loadDisplay(newGroupId, screen, false, data);
    }

    private void loadDisplay(Integer newGroupId, Screen screen, boolean orientationChanged, List<DataValuePair> data) {
        boolean screenChanged = false;
        boolean groupChanged = false;
        boolean tabBarChanged = false;
        Integer oldScreenId = currentScreenId;
        if (screen == null || newGroupId == null) {
            return;
        }
        int newScreenId = screen.getId();
        if (currentScreenId != newScreenId) {
            screenChanged = true;
        }
        if (currentGroupId != newGroupId) {
            groupChanged = true;
        }
        if (!screenChanged && !groupChanged) {
            return;
        }
        if (screenChanged) {
            ScreenViewImpl screenView = screenViewService.getScreenView(screen);
            if (consoleDisplay.setScreenView(screenView, data)) {
                setGestureMap(screen.getGesture());
                if (pollingHelperMap.containsKey(oldScreenId)) {
                    PollingHelper pollingHelper = pollingHelperMap.get(oldScreenId);
                    if (pollingHelper != null) {
                        pollingHelper.stopMonitoring();
                    }
                }
                if (!pollingHelperMap.containsKey(newScreenId)) {
                    Set<Integer> sensorIds = screenView.getSensorIds();
                    if (sensorIds == null || sensorIds.size() == 0) {
                        pollingHelperMap.put(newScreenId, null);
                    } else {
                        pollingHelperMap.put(newScreenId, new PollingHelper(sensorIds, pollingCallback));
                    }
                }
                PollingHelper pollingHelper = pollingHelperMap.get(newScreenId);
                if (pollingHelper != null) {
                    pollingHelper.startSensorMonitoring();
                }
                currentScreenId = newScreenId;
            } else {
                loadSettings(EnumSystemScreen.CONTROLLER_LIST, null);
                return;
            }
        }
        if (groupChanged) {
            consoleDisplay.removeTabBar();
            TabBar tabBar = panelService.getTabBar(newGroupId);
            if (tabBar != null && tabBar.getItem() != null) {
                TabBarComponent tabBarComponent = new TabBarComponent(tabBar);
                tabBarChanged = consoleDisplay.setTabBar(tabBarComponent);
                tabBarComponent.onScreenViewChange(new ScreenViewChangeEvent(currentScreenId));
            }
            List<Integer> screenIds = panelService.getGroupScreenIds(newGroupId);
            if (screenIds != null && screenIds.size() > 1) {
                ScreenIndicator screenIndicator = new ScreenIndicator(screenIds);
                consoleDisplay.setScreenIndicator(screenIndicator);
                screenIndicator.onScreenViewChange(new ScreenViewChangeEvent(currentScreenId));
            } else {
                consoleDisplay.removeScreenIndicator();
            }
            currentGroupId = newGroupId;
        }
        if (screenChanged) {
            ConsoleUnitEventManager.getInstance().getEventBus().fireEvent(new ScreenViewChangeEvent(currentScreenId));
        }
    }

    private void setGestureMap(List<Gesture> gestures) {
        clearGestureMap();
        if (gestures != null) {
            for (Gesture gesture : gestures) {
                SwipeDirection direction = SwipeDirection.enumValueOf(gesture.getType());
                gestureMap.put(direction, gesture);
            }
        }
    }

    private void clearGestureMap() {
        gestureMap.put(SwipeDirection.LEFT, null);
        gestureMap.put(SwipeDirection.RIGHT, null);
        gestureMap.put(SwipeDirection.UP, null);
        gestureMap.put(SwipeDirection.DOWN, null);
    }

    private void getIsSecure() {
        controllerService.isSecure(new AsyncControllerCallback<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    Window.alert("I'm Secure!");
                } else {
                    Window.alert("I'm not Secure");
                }
            }
        });
    }

    public ControllerService getControllerService() {
        return this.controllerService;
    }

    public LocalDataService getLocalDataService() {
        return this.dataService;
    }

    private void registerHandlers() {
        HandlerManager eventBus = ConsoleUnitEventManager.getInstance().getEventBus();
        eventBus.addHandler(RotationEvent.getType(), this);
        eventBus.addHandler(WindowResizeEvent.getType(), this);
        eventBus.addHandler(SwipeEvent.getType(), this);
        eventBus.addHandler(HoldEvent.getType(), this);
        eventBus.addHandler(NavigateEvent.getType(), this);
        eventBus.addHandler(CommandSendEvent.getType(), this);
    }

    public void onAdd() {
        ControllerCredentials controllerCreds;
        String panelName = "";
        setSize(width, height);
        consoleDisplay.onAdd(width, height);
        show();
        controllerCreds = dataService.getDefaultControllerCredentials();
        if (controllerCreds != null) {
            panelName = controllerCreds.getDefaultPanel();
        } else {
            controllerCreds = AutoBeanService.getInstance().getFactory().create(ControllerCredentials.class).as();
            controllerCreds.setUrl("http://controller.openremote.org/iphone/controller/");
            controllerCreds.setDefaultPanel("My Home");
        }
        if (panelName != null && !panelName.equals("")) {
            loadControllerAndPanel(controllerCreds, panelName);
        } else {
            controllerCreds = dataService.getLastControllerCredentials();
            if (controllerCreds != null) {
                panelName = controllerCreds.getDefaultPanel();
            }
            if (panelName == null || panelName.equals("")) {
                panelName = dataService.getLastPanelName();
            }
            if (panelName != null && !panelName.equals("")) {
                loadControllerAndPanel(controllerCreds, panelName);
            } else {
                loadSettings(EnumSystemScreen.CONTROLLER_LIST, null);
            }
        }
    }

    @Override
    public void onRotate(RotationEvent event) {
        String orientation = event.getOrientation();
        if (BrowserUtils.isMobile || (!BrowserUtils.isMobile && !isFullscreen)) {
            setOrientation(orientation);
        }
        if (panelService.isInitialized()) {
            if (!orientation.equalsIgnoreCase(consoleDisplay.getOrientation())) {
                Screen inverseScreen = panelService.getInverseScreen(currentScreenId);
                if (inverseScreen != null) {
                    loadDisplay(inverseScreen, true, null);
                    consoleDisplay.updateTabBar();
                }
            }
        }
    }

    @Override
    public void onWindowResize(WindowResizeEvent event) {
        if (BrowserUtils.isMobile || isFullscreen) {
            if (getOrientation().equalsIgnoreCase("portrait")) {
                setSize(event.getWindowWidth(), event.getWindowHeight());
            } else {
                setSize(event.getWindowHeight(), event.getWindowWidth());
            }
        } else {
            setPosition(event.getWindowWidth(), event.getWindowHeight());
        }
    }

    @Override
    public void onHold(HoldEvent event) {
        if (event.getSource() == consoleDisplay) {
            loadSettings(EnumSystemScreen.CONTROLLER_LIST, null);
        }
    }

    @Override
    public void onSwipe(SwipeEvent event) {
        Gesture gesture = gestureMap.get(event.getDirection());
        boolean gestureHandled = false;
        Navigate navigate = null;
        Boolean hasControlCommand = null;
        Integer commandId = null;
        if (gesture != null) {
            navigate = gesture.getNavigate();
            hasControlCommand = gesture.getHasControlCommand();
            commandId = gesture.getId();
            HandlerManager eventBus = ConsoleUnitEventManager.getInstance().getEventBus();
            if (navigate != null) {
                if (navigate.getToGroup() != currentGroupId || navigate.getToScreen() != currentScreenId) {
                    gestureHandled = true;
                    eventBus.fireEvent(new NavigateEvent(navigate));
                }
            } else if (hasControlCommand) {
                gestureHandled = true;
                eventBus.fireEvent(new CommandSendEvent(commandId, "swipe", null));
            }
        }
        if (gestureHandled) {
            return;
        }
        switch(event.getDirection()) {
            case LEFT:
                Screen nextScreen = panelService.getNextScreen(currentGroupId, currentScreenId);
                loadDisplay(nextScreen, null);
                break;
            case RIGHT:
                Screen prevScreen = panelService.getPreviousScreen(currentGroupId, currentScreenId);
                loadDisplay(prevScreen, null);
                break;
        }
    }

    @Override
    public void onNavigate(NavigateEvent event) {
        Navigate navigate = event.getNavigate();
        if (navigate != null) {
            String to = navigate.getTo();
            Integer toGroupId = navigate.getToGroup();
            Integer toScreenId = navigate.getToScreen();
            List<DataValuePair> data = navigate.getData();
            if (to != null && !to.equals("")) {
                EnumSystemScreen screen = EnumSystemScreen.getSystemScreen(to);
                if (screen != null) {
                    loadSettings(screen, null);
                }
            } else if (toGroupId != null && toScreenId != null) {
                Screen screen = panelService.getScreenById(toScreenId);
                loadDisplay(toGroupId, screen, data);
            }
        }
    }

    @Override
    public void onCommandSend(CommandSendEvent event) {
        if (event != null) {
            controllerService.sendCommand(event.getCommandId() + "/" + event.getCommand(), new AsyncControllerCallback<Boolean>() {

                @Override
                public void onSuccess(Boolean result) {
                    if (!result) {
                        Window.alert("Command Send Failed!");
                    }
                }
            });
        }
    }

    public void onError(EnumControllerResponseCode errorCode) {
        switch(errorCode) {
            case XML_CHANGED:
                reloadControllerAndPanel();
                break;
            default:
                loadSettings(EnumSystemScreen.CONTROLLER_LIST, null);
                break;
        }
    }
}
