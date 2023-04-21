package org.gvsig.rastertools.roi;

import javax.swing.JFrame;
import javax.swing.UIManager;
import org.gvsig.rastertools.roi.ui.ROIsManagerPanel;

/**
 * Test para el panel de gesti�n de ROIs.
 *  
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class TestROIManagerPanel {

    private JFrame jFrame = new JFrame();

    private ROIsManagerPanel roiManagerPanel = null;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            System.err.println("No se puede cambiar al LookAndFeel");
        }
        new TestROIManagerPanel();
    }

    public TestROIManagerPanel() {
        super();
        initialize();
    }

    private void initialize() {
        jFrame.setSize(new java.awt.Dimension(640, 480));
        roiManagerPanel = new ROIsManagerPanel(null);
        jFrame.setContentPane(roiManagerPanel);
        jFrame.setResizable(true);
        jFrame.setTitle("Gestor de ROIs");
        jFrame.setVisible(true);
        jFrame.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
