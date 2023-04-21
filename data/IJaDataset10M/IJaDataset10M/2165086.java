package com.tenline.game.simulation.moneytree.client;

import java.util.Date;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tenline.game.simulation.moneytree.shared.PlatformConfig;

public class MenuContainer extends LayoutContainer {

    private MainContainer mainContainer;

    public MenuContainer(MainContainer cont) {
        this.mainContainer = cont;
        setSize("650", "40");
        this.setStyleAttribute("background", "transparent");
        HBoxLayout boxLayout = new HBoxLayout();
        boxLayout.setPack(BoxLayoutPack.CENTER);
        boxLayout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
        setLayout(boxLayout);
        Button btnD = new Button("购买种子");
        add(btnD, new HBoxLayoutData(0, 5, 0, 0));
        btnD.setSize("71", "25");
        btnD.addListener(Events.Select, new Listener<ButtonEvent>() {

            public void handleEvent(ButtonEvent e) {
                final long orderId = (new Date()).getTime();
                EnvConfig.getRPCService().getRenrenOrderToken(EnvConfig.getSessionKey(), EnvConfig.getRenrenUserId(), PlatformConfig.DOU_PER_SEED, orderId, new AsyncCallback<String>() {

                    public void onFailure(Throwable caught) {
                        caught.printStackTrace();
                        MessageBox.info("错误", "调用后台服务生成订单失败", null);
                    }

                    public void onSuccess(String result) {
                        if (result != null) {
                            OrderWindow w = new OrderWindow(orderId, result);
                            w.show();
                            w.center();
                        } else {
                            MessageBox.info("错误", "无法从人人网支付平台获取订单的Token", null);
                        }
                    }
                });
            }
        });
        com.extjs.gxt.ui.client.widget.button.Button plantButton = new com.extjs.gxt.ui.client.widget.button.Button("种植");
        plantButton.addListener(Events.Select, new Listener<ButtonEvent>() {

            public void handleEvent(ButtonEvent e) {
                mainContainer.plant();
            }
        });
        plantButton.setSize("71", "25");
        add(plantButton, new HBoxLayoutData(0, 5, 0, 5));
        com.extjs.gxt.ui.client.widget.button.Button harvestButton = new com.extjs.gxt.ui.client.widget.button.Button("收获");
        harvestButton.setSize("71", "25");
        add(harvestButton, new HBoxLayoutData(0, 0, 0, 5));
        harvestButton.addListener(Events.Select, new Listener<ButtonEvent>() {

            public void handleEvent(ButtonEvent e) {
                mainContainer.harvest();
            }
        });
        com.extjs.gxt.ui.client.widget.button.Button bargainButton = new com.extjs.gxt.ui.client.widget.button.Button("交易");
        bargainButton.setSize("71", "25");
        add(bargainButton, new HBoxLayoutData(0, 0, 0, 5));
        bargainButton.addListener(Events.Select, new Listener<ButtonEvent>() {

            public void handleEvent(ButtonEvent e) {
                BarginWindow w = new BarginWindow();
                w.show();
                w.center();
            }
        });
    }
}
