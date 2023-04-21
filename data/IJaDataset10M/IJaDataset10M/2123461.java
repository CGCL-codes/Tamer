package com.mainatom.ui;

public class NbMainStart {

    public static class MainFrame extends AFrame {

        protected void onInit() throws Exception {
            super.onInit();
        }
    }

    public static void main(String[] args) {
        UITest_app app = new UITest_app();
        MainFrame f = new MainFrame();
        f.show("normal", "nbmain", "");
    }
}
