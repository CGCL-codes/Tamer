package org.fao.fenix.web.client;

import java.util.HashMap;
import org.gwm.client.GFrame;
import org.gwm.client.impl.DefaultGFrame;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class FenixWorkspace {

    public static boolean visible = false;

    public static HashMap windowsMap = new HashMap();

    private GFrame window = new DefaultGFrame("Workspace Items");

    public static DockPanel dockPanel = new DockPanel();

    public static String counter = "0";

    public static HashMap panelMap = new HashMap();

    public static HashMap reversePanelMap = new HashMap();

    public void build() {
        visible = true;
        window.setSize(300, 50);
        window.setVisible(true);
        window.setLocation((int) (Window.getClientHeight() * 0.3), (int) (Window.getClientWidth() - 350));
        window.setClosable(false);
        window.setMaximizable(false);
        HorizontalPanel buttons = new HorizontalPanel();
        Button save = new Button("save", new ClickListener() {

            public void onClick(Widget sender) {
            }
        });
        Button saveAsProject = new Button("save as project", new ClickListener() {

            public void onClick(Widget sender) {
            }
        });
        Button trash = new Button("trash", new ClickListener() {

            public void onClick(Widget sender) {
            }
        });
        save.setWidth("100px");
        saveAsProject.setWidth("100px");
        trash.setWidth("100px");
        buttons.add(save);
        buttons.add(saveAsProject);
        buttons.add(trash);
        dockPanel.setSize("300px", "50px");
        dockPanel.add(buttons, DockPanel.SOUTH);
        window.setContent(dockPanel);
    }

    public void addResource(String name, String type) {
        final String windowNumber;
        HorizontalPanel resourcePanel = new HorizontalPanel();
        Image icon = new Image("chart.gif");
        Image showIconUp = new Image("arrow-up.gif");
        Image showIconDown = new Image("arrow-down.gif");
        icon.setPixelSize(16, 16);
        CheckBox resourceName = new CheckBox(" " + name);
        resourceName.setWidth("200px");
        windowNumber = Fenix.windowManager.windowsNumber;
        windowsMap.put(windowNumber, Fenix.windowManager.getWindow(windowNumber));
        panelMap.put(Fenix.windowManager.getWindow(windowNumber), counter);
        reversePanelMap.put(counter, Fenix.windowManager.getWindow(windowNumber));
        counter = String.valueOf((Integer.parseInt(counter) + 1));
        final ToggleButton show = new ToggleButton(showIconUp, showIconDown);
        show.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                if (show.isDown()) ((GFrame) windowsMap.get(windowNumber)).setVisible(false); else ((GFrame) windowsMap.get(windowNumber)).setVisible(true);
            }
        });
        resourcePanel.add(icon);
        resourcePanel.add(resourceName);
        resourcePanel.add(show);
        resourcePanel.setSize("300px", "20px");
        dockPanel.add(resourcePanel, DockPanel.NORTH);
        window.setContent(dockPanel);
    }

    public void updatePreviewMap(GFrame window) {
        counter = String.valueOf(Integer.parseInt(counter) - 1);
        int index = Integer.parseInt((String) panelMap.get(window));
        for (int i = index; i < panelMap.size(); i++) {
            GFrame tmp = (GFrame) reversePanelMap.get(String.valueOf(i));
            panelMap.remove(tmp);
            reversePanelMap.remove(String.valueOf(i));
            panelMap.put(tmp, String.valueOf(i - 1));
            reversePanelMap.put(String.valueOf(i - 1), tmp);
        }
    }
}
