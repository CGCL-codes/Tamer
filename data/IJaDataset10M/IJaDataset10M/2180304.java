package net.sf.warpcore.cms.webfrontend.gui.window;

import net.sf.wedgetarian.*;
import net.sf.wedgetarian.event.*;
import net.sf.wedgetarian.ui.UIException;
import net.sf.wedgetarian.ui.UIRenderContext;
import java.util.Vector;

public class WindowManagerGridDisplay extends WindowManagerDisplay {

    private static final String resourcePath = "classpath:///net/sf/warpcore/cms/webfrontend/gui/resources/window/";

    private static final Image closeImage = new Image(resourcePath + "close.gif");

    private static final Image closeDisabledImage = new Image(resourcePath + "close_disabled.gif");

    private static final Image blankImage = (Application.getCurrentApplication()).getImage("blankImage");

    private static final Image defaultActivateImage = new Image(resourcePath + "activate.gif");

    private static final Image defaultActivateDisabledImage = new Image(resourcePath + "activate_disabled.gif");

    private WindowDisplay display;

    private Panel panel;

    private Vector panels;

    private Window activeWindow;

    private String gridNameActive;

    private String gridNameInactive;

    private boolean isActiveClickable;

    public WindowManagerGridDisplay(WindowManager manager) {
        this(manager, "WindowManagerGridDisplayGrid-active", "WindowManagerGridDisplayGrid-inactive", false);
    }

    public WindowManagerGridDisplay(WindowManager manager, String gridNameActive, String gridNameInactive, boolean isActiveClickable) {
        super(manager);
        this.gridNameActive = gridNameActive;
        this.gridNameInactive = gridNameInactive;
        this.isActiveClickable = isActiveClickable;
        panel = new Panel();
        add(panel);
        refresh(manager.getWindows());
    }

    protected void refreshSelection(Window window) {
        activeWindow = window;
        refresh(panels);
    }

    protected void refresh(Vector v) {
        panels = v;
        int size = panels.size();
        panel.removeAllWedgets();
        for (int i = 0; i < size; i++) {
            Window w = (Window) panels.get(i);
            panel.add(createGrid(w));
        }
    }

    private Grid createGrid(Window window) {
        Label title = new Label(window.getTitle());
        Image activateImage = window.getTitleImage();
        Image activateDisabledImage = window.getTitleImage();
        WindowButton closeButton = new WindowButton(window, closeImage, closeDisabledImage);
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                WindowButton closeButton = (WindowButton) event.getSource();
                Window window = closeButton.getWindow();
                window.close();
            }
        });
        if (!window.isClosable()) {
            closeButton.setEnabled(false);
        }
        if (activateImage == null) {
            activateImage = defaultActivateImage;
            activateDisabledImage = defaultActivateDisabledImage;
        }
        WindowButton activateButton = new WindowButton(window, activateImage, activateDisabledImage);
        activateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                WindowButton activateButton = (WindowButton) event.getSource();
                Window window = activateButton.getWindow();
                WindowManagerGridDisplay.this.manager.setActiveWindow(window);
            }
        });
        Grid wmgdGrid;
        if (window == activeWindow) {
            wmgdGrid = new Grid(gridNameActive);
            activateButton.setEnabled(isActiveClickable);
        } else {
            wmgdGrid = new Grid(gridNameInactive);
        }
        wmgdGrid.add(title, "title");
        wmgdGrid.add(activateButton, "activateButton");
        wmgdGrid.add(closeButton, "closeButton");
        return wmgdGrid;
    }

    public void render(UIRenderContext context) throws UIException {
        panel.render(context);
    }

    private class WindowButton extends Button {

        private Window window;

        public WindowButton(Window window, Image image, Image disabledImage) {
            super();
            this.window = window;
            this.setIcon(image);
            this.setDisabledIcon(disabledImage);
            setTooltip(window.getLongTitle());
        }

        public Window getWindow() {
            return window;
        }
    }
}
