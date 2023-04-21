package tstoa;

import javax.swing.*;

public class TSToAMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    TSToAGUI view = new TSToAGUI();
                    view.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
