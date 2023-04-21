package tuwien.auto.eicldemo.ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * <p>
 * Title: MessageDialog
 * </p>
 * <p>
 * Description: Dialog um verschiedene Msg anzuzeigen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Geddi
 * @version 1.0
 */
public class MessageDialog extends JDialog {

    public static final byte ERROR_PIC = 0;

    public static final byte WARNING_PIC = 1;

    public static final byte INFO_PIC = 2;

    public static final byte NO_PIC = 3;

    public static final byte QUESTION_PIC = 4;

    public static final long serialVersionUID = 1;

    private boolean ok = false;

    private boolean buttonCancel = false;

    private JPanel pRoot = new JPanel();

    private JScrollPane jScrollPane1 = new JScrollPane();

    private JTextArea taMessage = new JTextArea();

    private JLabel lIcon = new JLabel();

    private JPanel pButton = new JPanel();

    private JButton bOk, bCancel;

    private FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 50, 0);

    private int typePic;

    private ImageIcon icon;

    private String message;

    private String title;

    public MessageDialog(String _message, int _typePic, boolean _buttonCancel) {
        try {
            buttonCancel = _buttonCancel;
            message = _message;
            typePic = _typePic;
            jbInit();
        } catch (Exception ex) {
            Main_Frame.showException(ex.getMessage());
        }
    }

    public MessageDialog(String _message, int _typePic) {
        this(_message, _typePic, false);
    }

    private void jbInit() throws Exception {
        Font font = new Font("Default", Font.PLAIN, 9);
        Dimension dim = new Dimension(280, 180);
        setSize(dim);
        switch(typePic) {
            case ERROR_PIC:
                icon = new ImageIcon("error_ic.gif");
                title = " - error";
                break;
            case WARNING_PIC:
                icon = new ImageIcon("warning_ic.gif");
                title = " - warning";
                break;
            case INFO_PIC:
                icon = new ImageIcon("info_ic.gif");
                title = " - info";
                break;
            case QUESTION_PIC:
                icon = new ImageIcon("question_ic.gif");
                title = " - question";
                break;
            default:
                icon = new ImageIcon();
        }
        setTitle(title);
        lIcon.setIcon(icon);
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pRoot.setLayout(null);
        taMessage.setFont(font);
        taMessage.setEditable(false);
        taMessage.setText(message);
        taMessage.setFocusable(false);
        taMessage.setEditable(false);
        taMessage.setLineWrap(true);
        jScrollPane1.setBorder(null);
        jScrollPane1.setBounds(60, 10, 200, 100);
        pButton.setLayout(flowLayout);
        getContentPane().add(pRoot);
        bOk = new JButton("OK");
        bOk.setPreferredSize(new Dimension(100, 20));
        bOk.addActionListener(new MessageDialog_bOk_actionAdapter(this));
        pRoot.add(jScrollPane1);
        pButton.setBounds(0, 120, 280, 100);
        pRoot.add(pButton);
        pButton.add(bOk);
        if (buttonCancel) {
            bCancel = new JButton("Cancel");
            bCancel.setPreferredSize(new Dimension(100, 25));
            bCancel.addActionListener(new MessageDialog_bCancel_actionAdapter(this));
            pButton.add(bCancel);
        }
        jScrollPane1.getViewport().add(taMessage, null);
        lIcon.setBounds(10, 10, 50, 50);
        pRoot.add(lIcon);
        setModal(true);
    }

    void bOk_actionPerformed(ActionEvent e) {
        ok = true;
        dispose();
    }

    void bCancel_actionPerformed(ActionEvent e) {
        ok = false;
        dispose();
    }

    public boolean getButtonOk() {
        return ok;
    }
}

class MessageDialog_bOk_actionAdapter implements java.awt.event.ActionListener {

    MessageDialog adaptee;

    MessageDialog_bOk_actionAdapter(MessageDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.bOk_actionPerformed(e);
    }
}

class MessageDialog_bCancel_actionAdapter implements java.awt.event.ActionListener {

    MessageDialog md;

    MessageDialog_bCancel_actionAdapter(MessageDialog md) {
        this.md = md;
    }

    public void actionPerformed(ActionEvent e) {
        md.bCancel_actionPerformed(e);
    }
}
