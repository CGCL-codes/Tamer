package jdc.apps.ojdcClient;

import java.awt.*;
import javax.swing.*;
import jdc.lib.*;
import java.awt.event.*;
import javax.swing.event.*;
import org.apache.log4j.Logger;

/**
 * Displays a conversation with a certain user.
 */
public class PrivateMsgInternalFrame extends JInternalFrame {

    /** get the logger for this package */
    protected static Logger ojdcLogger = LoggerContainer.getLogger(PrivateMsgInternalFrame.class);

    private String _sender;

    private HubConnection _hc;

    private String _window_title;

    JScrollPane _chat_scroll_pane = new JScrollPane();

    JTextField _msg_field = new JTextField();

    JTextArea _chat_text_area = new JTextArea();

    public PrivateMsgInternalFrame(String sender, Hub hub) {
        super(sender + " @ " + hub.getName(), true, true, true, true);
        _sender = sender;
        _hc = ConnectionHandler.instance().getHubConnection(hub);
        _window_title = sender + " @ " + hub.getName();
        try {
            jbInit();
            addInternalFrameListener(new InternalFrameListener() {

                public void internalFrameClosing(InternalFrameEvent e) {
                    DesktopFrame.instance().windowClosed(e.getInternalFrame());
                }

                public void internalFrameClosed(InternalFrameEvent e) {
                }

                public void internalFrameOpened(InternalFrameEvent e) {
                }

                public void internalFrameDeactivated(InternalFrameEvent e) {
                }

                public void internalFrameActivated(InternalFrameEvent e) {
                }

                public void internalFrameDeiconified(InternalFrameEvent e) {
                }

                public void internalFrameIconified(InternalFrameEvent e) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendMessage(String msg) {
        _chat_text_area.append(msg + "\n");
    }

    private void jbInit() throws Exception {
        setFrameIcon(((Icon) jdc.images.images.PrivateMsgInternalFrameIcon));
        this.setMinimumSize(new Dimension(300, 200));
        this.setPreferredSize(new Dimension(500, 300));
        this.setSize(new Dimension(500, 300));
        _msg_field.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _msg_field_actionPerformed(e);
            }
        });
        _chat_text_area.setWrapStyleWord(true);
        _chat_text_area.setLineWrap(true);
        _chat_text_area.setEditable(false);
        this.getContentPane().add(_chat_scroll_pane, BorderLayout.CENTER);
        _chat_scroll_pane.getViewport().add(_chat_text_area, null);
        this.getContentPane().add(_msg_field, BorderLayout.SOUTH);
    }

    void _msg_field_actionPerformed(ActionEvent e) {
        String msg = _msg_field.getText();
        if (msg.length() > 0) {
            _hc.privateMessage(_sender, msg);
            appendMessage("<" + Configuration.instance().getUser().getNick() + "> " + msg);
            _msg_field.setText("");
            _chat_text_area.setCaretPosition(_chat_text_area.getDocument().getLength());
            _chat_scroll_pane.doLayout();
        }
    }
}
