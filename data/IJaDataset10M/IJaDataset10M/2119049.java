package com.mrroman.linksender.gui;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import com.mrroman.linksender.gui.actions.SendMessageDialogAction;
import com.mrroman.linksender.ioc.In;
import com.mrroman.linksender.ioc.Init;
import com.mrroman.linksender.ioc.Locales;
import com.mrroman.linksender.ioc.Name;
import com.mrroman.linksender.ioc.Prototype;
import com.mrroman.linksender.sender.Message;

/**
 *
 * @author mrozekon
 */
@Name("gui.MessageWindow")
@Prototype
public class MessageWindow extends JFrame implements ActionListener {

    private static int cascade = 30;

    @Locales
    ResourceBundle messages;

    @In
    private PopupLabel popupLabel;

    @In
    private ClipboardUtils clipboardUtils;

    @In
    private SendMessageDialogAction sendMessageDialogAction;

    public MessageWindow() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle max = graphicsEnvironment.getMaximumWindowBounds();
        setBounds(cascade, cascade, (int) max.getWidth() / 3, (int) max.getHeight() / 4);
        cascade = 30 + ((cascade + 16) & 0xff);
    }

    @Init
    public void init() {
        popupLabel.addActionListener(this);
        popupLabel.setScrollable(true);
        popupLabel.setFocusableLabel(true);
        popupLabel.setParseable(false);
        add(popupLabel);
    }

    public void setMessage(Message message) {
        popupLabel.setMessage(message);
        this.setTitle(messages.getString("message_window") + " " + message.getSender());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("COPY".equals(e.getActionCommand())) {
            clipboardUtils.copyText(popupLabel.getMessage().getMessage());
        }
        if ("REMOVE".equals(e.getActionCommand())) {
            setVisible(false);
        }
        if ("REPLY".equals(e.getActionCommand())) {
            sendMessageDialogAction.actionPerformed(e);
        }
    }
}
