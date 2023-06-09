package test;

import ch.randelshofer.quaqua.*;
import ch.randelshofer.quaqua.colorchooser.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * ColorWheelTest.
 *
 * @author  Werner Randelshofer
 * @version 1.0 August 27, 2005 Created.
 */
public class ColorWheelTest extends javax.swing.JPanel {

    /**
     * Creates a new instance.
     */
    public ColorWheelTest() {
        initComponents();
        JColorWheel cw = new JColorWheel();
        cw.getModel().setValue(2, 100);
        add(cw);
        add(new JLabel("Color Wheel Test"), BorderLayout.SOUTH);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(QuaquaManager.getLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame f = new JFrame("Color Wheel Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new ColorWheelTest());
        f.pack();
        f.setVisible(true);
    }
}
