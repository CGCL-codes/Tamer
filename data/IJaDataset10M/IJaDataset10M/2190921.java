package visitpc.mtightvnc.example;

import java.awt.BorderLayout;
import java.net.URL;
import javax.swing.*;
import visitpc.VisitPCConstants;
import visitpc.lib.gui.StatusBar;
import visitpc.mtightvnc.OptionsPanel;
import visitpc.mtightvnc.VncViewer;
import visitpc.lib.gui.*;

/**
 * Example of how to embed a tabbed VNC panel in a frame.
 */
public class TabbedPanelExample {

    public TabbedPanelExample() {
        String args0[] = { "HOST", "192.168.0.5" };
        String args1[] = { "HOST", "192.168.0.18", "PORT", "5900", OptionsPanel.ParameterNames[0], OptionsPanel.ParameterValues[0][1] };
        URL image;
        ImageIcon imageIcon;
        VisitPCJFrame frame = new VisitPCJFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        image = this.getClass().getResource(VisitPCConstants.IMAGES_PATH + "computer.png");
        imageIcon = new ImageIcon(image);
        frame.getContentPane().add(tabbedPane);
        StatusBar statusBar = new StatusBar();
        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
        JPanel vncPanel0 = new JPanel(new BorderLayout());
        tabbedPane.addTab("", imageIcon, vncPanel0, "");
        JPanel vncPanel1 = new JPanel(new BorderLayout());
        tabbedPane.addTab("", imageIcon, vncPanel1, "");
        JPanel vncPanel2 = new JPanel(new BorderLayout());
        tabbedPane.addTab("", imageIcon, vncPanel2, "");
        VncViewer v0 = new VncViewer(args0);
        v0.setVncPanel(vncPanel0);
        v0.addStatusListener(statusBar);
        frame.addWindowListener(v0);
        v0.enableFrameResize(true);
        v0.init();
        v0.start();
        VncViewer v1 = new VncViewer(args1);
        v1.addStatusListener(statusBar);
        v1.setVncPanel(vncPanel1);
        frame.addWindowListener(v1);
        v1.enableFrameResize(true);
        v1.init();
        v1.start();
        VncViewer v2 = new VncViewer(args0);
        v2.addStatusListener(statusBar);
        v2.setVncPanel(vncPanel2);
        frame.addWindowListener(v2);
        v2.enableFrameResize(true);
        v2.init();
        v2.start();
        frame.setVisible(true);
    }

    public static void main(String[] argv) {
        new TabbedPanelExample();
    }
}
