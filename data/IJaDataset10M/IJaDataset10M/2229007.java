package com.wwg.market.ui.dashboard.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.mobileares.midp.widgets.client.anchor.Split;
import com.mobileares.midp.widgets.client.anchor.TextAnchor;
import com.mobileares.midp.widgets.client.frame.Frame;
import com.mobileares.midp.widgets.client.frame.Header;
import com.mobileares.midp.widgets.client.frame.MainPanel;
import com.mobileares.midp.widgets.client.panel.MaxPanel;
import com.mobileares.midp.widgets.client.tab.FrameTabItem;
import com.mobileares.midp.widgets.client.tab.FrameTabPanel;
import com.wwg.market.ui.dashboard.client.utils.AppModelCooker;
import com.wwg.market.ui.dashboard.client.utils.CacheCode;
import com.wwg.market.ui.dashboard.client.widgets.MyPage;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 2011-11-3
 * Time: 14:34:29
 * To change this template use File | Settings | File Templates.
 */
public class Dashboard implements EntryPoint {

    static Frame frame;

    static FrameTabPanel tabPanel;

    private FocusPanel bodyWrapper;

    static FrameTabItem.FishTableItemEvent event = new FrameTabItem.FishTableItemEvent() {

        public void onSelect() {
        }

        public void onRefresh() {
            ((MyPage) tabPanel.getSelectItem().getShowWidget()).ddOutModel(null, null);
        }

        public void onClose() {
        }

        public void onHidden() {
        }
    };

    public void onModuleLoad() {
        frame = new Frame();
        initHead("");
        AppModelCooker.initAppModel();
        CacheCode.initAppCodes();
        initMenu();
        initMain();
        initDefaultTab();
        addListener();
        RootPanel.get("frame").add(frame);
    }

    private void initDefaultTab() {
        tabPanel.addItem("欢迎使用", new Image("fishimages/darling.jpg"));
        tabPanel.getItem("欢迎使用").setClose(false);
        tabPanel.setSelectItem("欢迎使用");
    }

    private void initMenu() {
        MainPanel main = frame.getMain();
        main.addMenuWidget(DashMenuBuilder.getMenuWidget());
    }

    private void initMain() {
        final MainPanel main = frame.getMain();
        tabPanel = new FrameTabPanel();
        main.setMainWidget(tabPanel);
    }

    private void addListener() {
        frame.addListener(new Frame.LayoutListener() {

            public void layout(int width, int height) {
                if (tabPanel.getSelectItem().getShowWidget() instanceof MyPage) {
                    tabPanel.getSelectItem().getShowWidget().setHeight(height - 30 + "px");
                }
            }
        });
    }

    private void initHead(String text) {
        Header header = frame.getHeader();
        header.setWelcomeText(text);
        FlowPanel fp = new FlowPanel();
        TextAnchor exit = new TextAnchor("退出");
        exit.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent clickEvent) {
            }
        });
        TextAnchor reLogin = new TextAnchor("重登录");
        reLogin.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent clickEvent) {
            }
        });
        TextAnchor max = new TextAnchor("最大化");
        max.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent clickEvent) {
                Widget widget = tabPanel.getSelectItem().getShowWidget();
                MaxPanel mp = new MaxPanel();
                mp.setMaxWidget(widget);
                mp.addResumeListener(new MaxPanel.IResume() {

                    public void process(Widget widget) {
                        tabPanel.getSelectItem().setShowWidget(widget);
                        tabPanel.setSelectItem(tabPanel.getSelectItem().getUserObject());
                    }
                });
                mp.show();
            }
        });
        fp.add(max);
        fp.add(new Split());
        fp.add(reLogin);
        fp.add(new Split());
        fp.add(exit);
        header.setFunctionBarWidget(fp);
    }
}
