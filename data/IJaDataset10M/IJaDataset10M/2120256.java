package com.sin.client.ui;

import java.util.logging.Logger;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.sin.client.mvp.CetralActivityMapper;
import com.sin.client.mvp.EastActivityMapper;
import com.sin.client.mvp.NordActivityMapper;
import com.sin.client.mvp.SouthActivityMapper;
import com.sin.client.mvp.WestActivityMapper;

public class MainViewNotUiImpl extends Composite implements MainView {

    private static final Logger log = Logger.getLogger(MainViewNotUiImpl.class.getName());

    private SimplePanel centralDisplayPanel = new SimplePanel();

    private SimplePanel nordDisplayPanel = new SimplePanel();

    private SimplePanel westDisplayPanel = new SimplePanel();

    private SimplePanel eastDisplayPanel = new SimplePanel();

    private SimplePanel southDisplayPanel = new SimplePanel();

    @Inject
    public MainViewNotUiImpl(EventBus eventBus, CetralActivityMapper centralActivityMapper, WestActivityMapper westActivityMapper, EastActivityMapper eastActivityMapper, NordActivityMapper nordActivityMapper, SouthActivityMapper southActivityMapper) {
        ActivityManager centralActivityManager = new ActivityManager(centralActivityMapper, eventBus);
        centralActivityManager.setDisplay(centralDisplayPanel);
        ActivityManager nordActivityManager = new ActivityManager(nordActivityMapper, eventBus);
        nordActivityManager.setDisplay(nordDisplayPanel);
        ActivityManager westActivityManager = new ActivityManager(westActivityMapper, eventBus);
        westActivityManager.setDisplay(westDisplayPanel);
        ActivityManager eastActivityManager = new ActivityManager(eastActivityMapper, eventBus);
        eastActivityManager.setDisplay(eastDisplayPanel);
        ActivityManager southActivityManager = new ActivityManager(southActivityMapper, eventBus);
        southActivityManager.setDisplay(southDisplayPanel);
        DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
        dockLayoutPanel.addNorth(nordDisplayPanel, 50);
        dockLayoutPanel.addWest(westDisplayPanel, 200);
        dockLayoutPanel.addEast(eastDisplayPanel, 200);
        dockLayoutPanel.addEast(southDisplayPanel, 50);
        dockLayoutPanel.add(centralDisplayPanel);
        initWidget(dockLayoutPanel);
    }
}
