package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import data.controls.CommunicationService;
import data.controls.CommunicationServiceImpl;
import gui.dialogs.MailEditor;
import ogv.OGV;
import util.Context;
import util.Message;
import util.SwingUtils;
import static gui.CommandsLocal.REPLY_MAIL;
import static ogv.OGVPreferences.MESSAGES_LIST;

/**
 * Date: Oct 7, 2009
 * Author: Alexander Okunevich (aokunevich@gmail.com)
 */
public class ViewMessages extends ViewList<Message> {

    private final JTextArea body = new JTextArea(20, 0);

    private final Message.MailType listType;

    private final CommunicationService service;

    public ViewMessages(Message.MailType listType) {
        service = new CommunicationServiceImpl();
        init(Message.class, OGV.getConfig().subnode(MESSAGES_LIST));
        this.listType = listType;
        loadMessages(listType);
        JScrollPane pane = new JScrollPane(body, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(pane, BorderLayout.SOUTH);
        body.setFont(new Font("Verdana", Font.PLAIN, 12));
        body.setLineWrap(true);
        body.setEditable(false);
    }

    private void loadMessages(Message.MailType listType) {
        switch(listType) {
            case BROADCAST:
                loadBroadcastMessages(service);
                break;
            case PRIVATE:
                loadPrivateMessages(service);
                break;
            case HYPERCAST:
                loadHypercastMessages(service);
                break;
        }
    }

    /**
	 * Load broadcasts.
	 *
	 * @param service Communication service.
	 * @return Context.
	 */
    protected Context loadBroadcastMessages(CommunicationService service) {
        OGV.showLoadingPopup();
        Context context = service.loadMessages(Message.MailType.BROADCAST);
        list = context.getBroadcasts();
        OGV.hideLoadingPopup();
        return context;
    }

    /**
	 * Load private messages.
	 *
	 * @param service Communication service.
	 * @return Context.
	 */
    protected Context loadPrivateMessages(CommunicationService service) {
        OGV.showLoadingPopup();
        Context context = service.loadMessages(Message.MailType.PRIVATE);
        list = context.getMessages();
        OGV.hideLoadingPopup();
        return context;
    }

    /**
	 * Load hypercast messages.
	 *
	 * @param service Communication service.
	 * @return Context.
	 */
    protected Context loadHypercastMessages(CommunicationService service) {
        OGV.showLoadingPopup();
        Context context = service.loadMessages(Message.MailType.HYPERCAST);
        list = context.getHypercasts();
        OGV.hideLoadingPopup();
        return context;
    }

    @Override
    public void selectionChanged() {
        Message message = table.getSelectedItem();
        if (message != null) {
            body.setText(message.getBody());
        }
    }

    @Override
    protected JPopupMenu getPopupMenu() {
        initActions();
        return SwingUtils.createPopup(actions.getAction(REPLY_MAIL));
    }

    @Override
    protected void initActions() {
        CommandsLocal[] commands = { REPLY_MAIL };
        actions.initActions(commands);
    }

    @Override
    public void actionPerformed(ActionEvent e, CommandsLocal c) {
        switch(c) {
            case REPLY_MAIL:
                OGV.setCurrentPanel(new MailEditor(table.getSelectedItem()));
                break;
        }
    }

    /**
	 * Reload messages and update ui.
	 *
	 * @param typeList message type - BROADCAST | HYPERCAST | PRIVATE.
	 */
    public void reloadMessages(Message.MailType typeList) {
        loadMessages(typeList);
        updateUI();
    }

    public Message.MailType getListType() {
        return listType;
    }

    @Override
    public String getTitle() {
        switch(listType) {
            case BROADCAST:
                return "Broadcasts Messages";
            case PRIVATE:
                return "Private Messages";
            case HYPERCAST:
                return "Hypercast Messages";
        }
        return "Messages";
    }
}
