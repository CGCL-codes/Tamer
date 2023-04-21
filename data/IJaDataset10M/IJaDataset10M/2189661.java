package sti.installer.setup;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class SetupDialog extends JFrame {

    private JPanel jPanel1 = new JPanel();

    private XYLayout xYLayout1 = new XYLayout();

    private JLabel jLabel1 = new JLabel();

    private JLabel jLabel3 = new JLabel();

    private JLabel jLabel2 = new JLabel();

    private JButton jButton1 = new JButton();

    private JButton jButton2 = new JButton();

    public SetupDialog() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(640, 450));
        this.setContentPane(jPanel1);
        jPanel1.setLayout(xYLayout1);
        jLabel1.setSize(new Dimension(635, 90));
        jLabel3.setText("http://sti.kicks-ass.net");
        jLabel2.setText("Sti JInstaller");
        jButton1.setText("Next");
        jButton2.setText("Quit");
        jPanel1.add(jButton2, new XYConstraints(20, 355, 80, 25));
        jPanel1.add(jButton1, new XYConstraints(535, 350, 80, 25));
        jPanel1.add(jLabel2, new XYConstraints(485, 390, 110, 15));
        jPanel1.add(jLabel3, new XYConstraints(485, 405, 135, 15));
        jPanel1.add(jLabel1, new XYConstraints(0, 0, 635, 90));
        this.getContentPane().add(jPanel1, null);
    }
}
