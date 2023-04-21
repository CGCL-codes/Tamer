package p2p.chat.gui.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import p2p.chat.action.ChatCoreJxta;
import p2p.chat.action.IChatCore;
import p2p.chat.action.listener.SendListener;
import p2p.chat.gui.component.ChatMenu;
import p2p.chat.gui.view.ChatView;
import p2p.chat.transfer.pipe.MulticastPipe;
import p2p.chat.util.Messages;
import p2p.chat.util.UserSettings;

/**
 * @author S.Timofiychuk
 *
 */
public class ChatController {

    private JFrame view;

    /**
	 * @param view
	 * @throws Exception 
	 */
    public ChatController(JFrame view) throws Exception {
        this.view = view;
        if (view instanceof ChatView) {
            ((ChatView) view).addClosingListener(new ClosingListener());
            ((ChatView) view).addSendListeners(new SendListener(view));
            ((ChatView) view).addLanguageListener(new ChangeLocaleListener());
            ((ChatView) view).addResetSettingsListener(new ResetSettingsListener());
            ((ChatView) view).addHelpMenuListeners(new HelpMenuListener());
            view.pack();
            view.setSize(getSize());
            view.setLocationRelativeTo(null);
            view.setVisible(true);
            ((ChatView) view).setMainSplitPaneSize(0.80);
            ((ChatView) view).setWriteSplitPaneSize(0.85);
        }
    }

    /**
	 * @return view
	 */
    public JFrame getView() {
        return view;
    }

    /**
	 * Create Dimension
	 * @return size
	 */
    private Dimension getSize() {
        int frameWidth = 630;
        int frameHeight = 400;
        return new Dimension(frameWidth, frameHeight);
    }

    /**
	 * @author S.Timofiychuk
	 *
	 */
    class ClosingListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent event) {
            try {
                IChatCore chat = ChatCoreJxta.getInstance(null);
                Message multicastMsg = new Message();
                multicastMsg.addMessageElement(new StringMessageElement(ChatCoreJxta.SENDER_NAME_TAG, ((ChatCoreJxta) chat).getUserName(), null));
                multicastMsg.addMessageElement(new StringMessageElement(MulticastPipe.MULTICAST_TYPE_TAG, MulticastPipe.LOGOUT_TYPE, null));
                chat.sendMessage(multicastMsg, true);
                chat.destroy();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * @author s.timofiychuk
	 *
	 * Aug 28, 2008
	 * 
	 */
    class ChangeLocaleListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            if (event.getSource() instanceof JMenuItem) {
                String language = (String) ((JMenuItem) event.getSource()).getClientProperty(UserSettings.LANG_PROP_NAME);
                Messages.setLocale(language);
                ((ChatView) view).updateMessages();
            }
        }
    }

    /**
	 * 29.08.2008
	 * 
	 * @author S.Timofiychuk
	 *
	 */
    class HelpMenuListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            if (event.getSource() instanceof JMenuItem) {
                String menu = (String) ((JMenuItem) event.getSource()).getClientProperty(ChatMenu.MENU);
                if (menu.equals(ChatMenu.ABOUT_MENU)) {
                    JOptionPane.showMessageDialog(getView(), Messages.getString("optionPane.about.message"), Messages.getString("application.title"), JOptionPane.INFORMATION_MESSAGE);
                } else if (menu.equals(ChatMenu.HELP_MENU)) {
                    JOptionPane.showMessageDialog(getView(), Messages.getString("optionPane.help.message"), Messages.getString("application.title"), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    /**
	 * 04.09.2008
	 * 
	 * @author S.Timofiychuk
	 *
	 */
    class ResetSettingsListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            UserSettings.deleteConfFile();
            JOptionPane.showMessageDialog(getView(), Messages.getString("optionPane.reset.information"), Messages.getString("optionPane.warning"), JOptionPane.WARNING_MESSAGE);
        }
    }
}
