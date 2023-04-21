package eowl.view;

import javax.swing.*;
import eowl.model.SensorData;
import eowl.model.ControllerConfig;
import java.awt.*;
import lib.gui.*;
import java.util.*;

public class PlotFrame extends JFrame {

    public static final String PREFIX_TEXT = "SENSOR_DATA=";

    private Vector<SensorPanel> sensorPanelList;

    private JTabbedPane tabbedPane = new JTabbedPane();

    private ControllerConfig controllerConfig;

    private ElectricOWLFrame electricOWLFrame;

    /**
   * Constructor
   * 
   * @param title The window title
   * @param width The initial width of the window
   * @param height The initial height of the window
   */
    public PlotFrame(ElectricOWLFrame electricOWLFrame, ControllerConfig controllerConfig, String title, int width, int height) {
        super(title);
        this.electricOWLFrame = electricOWLFrame;
        this.controllerConfig = controllerConfig;
        setSize(width, height);
        UI.CenterOnScreen(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sensorPanelList = new Vector<SensorPanel>();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void plotData(SensorData sensorData, String sensorName) {
        SensorPanel sensorPanel;
        int tabIndex = -1;
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(sensorName)) {
                tabIndex = i;
                break;
            }
        }
        if (tabIndex == -1) {
            tabIndex = tabbedPane.getTabCount();
            sensorPanel = new SensorPanel(electricOWLFrame);
            sensorPanelList.add(sensorPanel);
            JScrollPane scrollPane = new JScrollPane(sensorPanel);
            tabbedPane.addTab(sensorName, scrollPane);
        }
        sensorPanelList.get(tabIndex).plotData(sensorName, sensorData, controllerConfig, 3600 * 24 * 365, false);
    }

    public void setNotify(boolean notify) {
        for (SensorPanel sensorPanel : sensorPanelList) {
            sensorPanel.setNotify(notify);
        }
    }
}
