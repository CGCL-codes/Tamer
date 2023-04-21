package org.strophe.sn;

import org.strophe.sn.*;

public final class SNMessageView extends org.strophe.sn.SNChildView implements java.awt.event.MouseListener {

    ScriptNavigator navigator;

    java.awt.TextArea messageArea;

    java.util.Vector messages = new java.util.Vector();

    public SNMessageView() {
        messageArea = new java.awt.TextArea("", 0, 0, java.awt.TextArea.SCROLLBARS_VERTICAL_ONLY);
        messageArea.setEditable(false);
        messageArea.setBounds(0, 276, 612, 60);
        messageArea.setBackground(new java.awt.Color(16777215));
        messageArea.addMouseListener(this);
    }

    public java.awt.Component getComponent() {
        return messageArea;
    }

    void clearMessageArea() {
        messageArea.setText("");
        messages.removeAllElements();
    }

    void addSourceMessage(String msg) {
        messageArea.append(msg + "\n");
        messages.addElement(msg);
        messageArea.setCaretPosition(0);
    }

    public void mousePressed(java.awt.event.MouseEvent e) {
    }

    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    public void mouseEntered(java.awt.event.MouseEvent e) {
    }

    public void mouseExited(java.awt.event.MouseEvent e) {
    }

    public void mouseClicked(java.awt.event.MouseEvent e) {
        int lintCaret = messageArea.getCaretPosition();
        if (e.getClickCount() == 3) {
            int lintMsgPosition = 0;
            int lintMsgNubmer = -1;
            for (int i = 0; i < messages.size(); i++) {
                String s = (String) messages.elementAt(i);
                lintMsgPosition += s.length();
                if (lintMsgPosition >= lintCaret) {
                    lintMsgNubmer = i;
                    break;
                }
                lintMsgPosition++;
                lintMsgPosition++;
            }
            navigator.performMessageAction(lintMsgNubmer);
        }
        messageArea.setCaretPosition(lintCaret);
    }
}
