package com.directmodelling.demo.swing;

import com.directmodelling.demo.shared.DemoModel;
import com.directmodelling.swing.Init;

public class DemoWindow {

    public DemoWindow() {
    }

    public static void main(final String[] args) {
        Init.init();
        final DemoWindowVE ve = new DemoWindowVE();
        ve.bind(new DemoModel());
        ve.setVisible(true);
    }
}
