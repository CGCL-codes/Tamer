package org.sulweb.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * <p>Title: Utilities awt</p>
 * <p>Description: Various commonly used functions</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Elaborazione Dati Pinerolo srl</p>
 * @author Lucio Crusca
 * @version 1.0
 */
public class MultiLineDlg extends JDialog {

    public static final int NONE = 0, OK = 1, CANCEL = 2, YES = 4, NO = 8, RETRY = 16, CUSTOM = 32;

    private JButton ok, cancel, yes, no, retry, custom;

    private JButton[] b_arr;

    private JPanel buttonsPanel;

    private JTextArea textArea;

    private int reply;

    public MultiLineDlg(Frame owner, String title, String text, int buttons) {
        this(owner, title);
        setText(text);
        setButtonsFlags(buttons);
    }

    public MultiLineDlg(Frame owner, String title) {
        super(owner);
        setTitle(title);
        Container cpane = getContentPane();
        cpane.setLayout(new BorderLayout());
        textArea = new JTextArea("", 7, 40);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel dummy = new JLabel();
        textArea.setBackground(dummy.getBackground());
        textArea.setForeground(dummy.getForeground());
        textArea.setFont(dummy.getFont());
        cpane.add(textArea, BorderLayout.CENTER);
        buttonsPanel = new JPanel();
        ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                clicked(OK);
            }
        });
        cancel = new JButton("Annulla");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                clicked(CANCEL);
            }
        });
        yes = new JButton("S�");
        yes.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                clicked(YES);
            }
        });
        no = new JButton("No");
        no.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                clicked(NO);
            }
        });
        retry = new JButton("Riprova");
        retry.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                clicked(RETRY);
            }
        });
        custom = new JButton();
        custom.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                clicked(CUSTOM);
            }
        });
        b_arr = new JButton[] { ok, cancel, yes, no, retry, custom };
        cpane.add(buttonsPanel, BorderLayout.SOUTH);
    }

    public void setButtonsFlags(int buttons) {
        int nButtons = countFlags(buttons);
        buttonsPanel.removeAll();
        if (nButtons > 0) {
            buttonsPanel.setLayout(new GridLayout(1, nButtons));
            for (int col = 0; col < nButtons; col++) {
                int buttonIndex = doFlagsMath(buttons, col);
                buttonsPanel.add(b_arr[buttonIndex]);
            }
        }
        buttonsPanel.revalidate();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public int getReply() {
        return reply;
    }

    private int doFlagsMath(int flags, int skip) {
        int result = 0, index = 0;
        while (flags > 0) {
            if ((flags & 1) > 0) if (skip < 0) result++; else {
                if (skip > 0) skip--; else {
                    result = index;
                    break;
                }
            }
            flags = flags >> 1;
            index++;
        }
        return result;
    }

    private int countFlags(int flags) {
        return doFlagsMath(flags, -1);
    }

    private void clicked(int buttonflag) {
        reply = buttonflag;
        dispose();
    }

    public JButton getFlagButton(int flag) {
        int index = doFlagsMath(flag, 0);
        return b_arr[index];
    }
}
