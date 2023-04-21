package com.cafe.serve.view.main;

import com.cafe.serve.controller.main.MainController;
import com.cafe.serve.view.AbstractGenericView;

public class MainView extends AbstractGenericView<MainController, MainPanel> {

    private MainPanel mainPanel;

    public MainView(MainController controller) {
        super(controller);
        controller.setView(this);
    }

    protected void initialize() {
        mainPanel = new MainPanel();
        setComponent(mainPanel);
    }
}
