package com.plarpebu.javakarplayer.plugins.AudioPlugins.taras;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import com.l2fprod.common.swing.JFontChooser;
import com.plarpebu.SkinMgr;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class FontDialogChoser extends JDialog {

    JPanel panel1 = new JPanel();

    BorderLayout borderLayout1 = new BorderLayout();

    JPanel jPanel1 = new JPanel();

    JButton jButton1 = new JButton();

    JFontChooser fc = new JFontChooser();

    /**
	 * Constructor
	 * 
	 * @param frame
	 * @param title
	 * @param modal
	 */
    public FontDialogChoser(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        try {
            SkinMgr.getInstance().addComponent(this);
            jbInit();
            panel1.add(fc, BorderLayout.CENTER);
            setSize(640, 400);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public FontDialogChoser() {
        this(null, "", false);
    }

    private void jbInit() throws Exception {
        panel1.setLayout(borderLayout1);
        this.setModal(true);
        this.setTitle("Please Select Font");
        jButton1.setText("Ok");
        jButton1.addActionListener(new FontDialogChoser_jButton1_actionAdapter(this));
        getContentPane().add(panel1);
        panel1.add(jPanel1, BorderLayout.SOUTH);
        jPanel1.add(jButton1, null);
    }

    public Font getSelectedFont() {
        return fc.getSelectedFont();
    }

    public void setSelectedFont(Font f) {
        fc.setSelectedFont(f);
    }

    void jButton1_actionPerformed(ActionEvent e) {
        setVisible(false);
    }
}

class FontDialogChoser_jButton1_actionAdapter implements java.awt.event.ActionListener {

    FontDialogChoser adaptee;

    FontDialogChoser_jButton1_actionAdapter(FontDialogChoser adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton1_actionPerformed(e);
    }
}
