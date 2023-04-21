package shu.cms.applet.measure.tool;

import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class CharacterizeFrame extends JInternalFrame {

    protected BorderLayout borderLayout1 = new BorderLayout();

    protected JList jList1 = new JList();

    protected JButton jButton_Characterize = new JButton();

    protected JPanel jPanel1 = new JPanel();

    protected FlowLayout flowLayout1 = new FlowLayout();

    public CharacterizeFrame() {
        super("Characterize", false, true, false, false);
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        getContentPane().setLayout(borderLayout1);
        this.getContentPane().add(jList1, java.awt.BorderLayout.CENTER);
        jButton_Characterize.setText("Characterize");
        jPanel1.setLayout(flowLayout1);
        flowLayout1.setAlignment(FlowLayout.RIGHT);
        jPanel1.add(jButton_Characterize);
        this.getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        CharacterizeFrame characterizeframe = new CharacterizeFrame();
    }
}
