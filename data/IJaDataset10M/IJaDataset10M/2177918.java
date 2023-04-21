package gui;

import datamodels.DailyGraphModel;
import javax.swing.*;
import java.awt.*;
import java.util.Observer;

public class DailyGraphFrame extends JFrame {

    private DailyGraphModel dGM;

    private DailyGraphView dGV;

    private static DailyGraphFrame singleton = null;

    public DailyGraphFrame() {
        super("DailyGraphFrame");
        setBounds(300, 300, 300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBackground(Color.white);
        dGM = new DailyGraphModel();
        dGV = new DailyGraphView();
        dGM.addObserver((Observer) dGV);
        getContentPane().add(dGV, BorderLayout.CENTER);
        getContentPane().setBackground(Color.white);
        setVisible(true);
    }

    public static void showMe() {
        if (singleton == null) singleton = new DailyGraphFrame();
        singleton.show();
    }

    public static void closeMe() {
        if (singleton != null) {
            singleton.dispose();
            singleton = null;
        }
    }

    public static void redraw() {
        if (singleton != null) singleton.repaint();
    }
}
