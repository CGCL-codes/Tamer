package com.tiani.prnscp.client.ddl2odgui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.tiani.prnscp.print.CalibrationException;

public class ODCurveGUIFrame extends JFrame {

    private final int DEF_WIDTH = 800, DEF_HEIGHT = 600;

    private ODCurveGUIPanel curvePanel;

    private ButtonLegendPanel legendPanel;

    private File lastFile = null;

    private JFileChooser chooser = new JFileChooser();

    private ODCurveGUIFrame() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        curvePanel = new ODCurveGUIPanel(this);
        legendPanel = new ButtonLegendPanel(curvePanel);
        legendPanel.setMinimumSize(new Dimension(DEF_WIDTH / 4, DEF_HEIGHT));
        JLabel lbl = new JLabel("Loaded Curves");
        curvePanel.setLegend(legendPanel);
        contentPane.add(curvePanel, BorderLayout.CENTER);
        contentPane.add(legendPanel, BorderLayout.SOUTH);
        JMenuBar mnubar = new JMenuBar();
        JMenu mnuCurve = new JMenu("Curve");
        Action actLoadCurve = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (promptUserForFile() != null) {
                    try {
                        curvePanel.loadScannedImageCurve(lastFile = chooser.getSelectedFile());
                    } catch (CalibrationException ce) {
                        showMsgDialog("There is a problem analyzing the selected image (" + lastFile + "):\n" + ce.getMessage(), "Calibration Error");
                    } catch (IOException ioe) {
                        showMsgDialog("There is a problem reading the selected image (" + lastFile + "):\n" + ioe.getMessage(), "File Error");
                    }
                    ODCurveGUIFrame.this.validate();
                    ODCurveGUIFrame.this.repaint();
                }
            }
        };
        actLoadCurve.putValue(Action.NAME, "Load...");
        JMenuItem mnuLoadCurve = new JMenuItem(actLoadCurve);
        mnuCurve.add(mnuLoadCurve);
        Action actReset = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                curvePanel.reset();
                validate();
            }
        };
        actReset.putValue(Action.NAME, "Reset");
        JMenuItem mnuReset = new JMenuItem(actReset);
        mnuCurve.add(mnuReset);
        Action actExit = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        actExit.putValue(Action.NAME, "Exit");
        JMenuItem mnuExit = new JMenuItem(actExit);
        mnuCurve.add(mnuExit);
        mnubar.add(mnuCurve);
        setJMenuBar(mnubar);
        setSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
        validate();
    }

    private ODCurveGUIFrame(String title) {
        this();
        setTitle(title);
    }

    File promptUserForFile() {
        chooser.setCurrentDirectory(lastFile);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) return (lastFile = chooser.getSelectedFile()); else return null;
    }

    void showMsgDialog(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.WARN);
        ODCurveGUIFrame fr = new ODCurveGUIFrame("OD Curve Viewer");
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.show();
    }
}
