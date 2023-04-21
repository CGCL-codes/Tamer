package info.reflectionsofmind.connexion.platform.gui.common;

import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.transport.IClientToServerTransport;
import info.reflectionsofmind.connexion.platform.core.transport.IServerToClientTransport;
import info.reflectionsofmind.connexion.platform.core.transport.IServerTransportFactory;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.AbstractDocument.AbstractElement;
import javax.swing.text.html.HTMLDocument;
import net.miginfocom.swing.MigLayout;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class ChatPane extends JPanel {

    private final List<IListener> listeners = new ArrayList<IListener>();

    private final JEditorPane chatPane;

    private final JButton sendButton;

    private final JTextField sendField;

    public ChatPane() {
        this.chatPane = new JEditorPane("text/html", null);
        this.chatPane.setEditable(false);
        this.chatPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        this.sendButton = new JButton(new SendAction());
        this.sendField = new JTextField();
        this.sendField.setAction(new SendAction());
        setLayout(new MigLayout("ins 0", "[grow][]", "[grow][]"));
        add(this.chatPane, "grow, span");
        add(this.sendField, "grow");
        add(this.sendButton, "grow");
        putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.sendButton.setEnabled(enabled);
        this.sendField.setEnabled(enabled);
    }

    public void addListener(IListener listener) {
        this.listeners.add(listener);
    }

    public void writeRaw(final String text) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    final HTMLDocument document = (HTMLDocument) ChatPane.this.chatPane.getDocument();
                    final AbstractElement element = (AbstractElement) document.getRootElements()[0].getElement(0).getElement(0);
                    final String timestampedText = new SimpleDateFormat("[HH:mm:ss] ").format(new Date()) + text;
                    document.insertBeforeEnd(element, timestampedText);
                } catch (final IOException exception) {
                    throw new RuntimeException(exception);
                } catch (final BadLocationException exception) {
                    throw new RuntimeException(exception);
                }
            }
        });
    }

    public void writeMessage(final Participant sender, final String message) {
        writeRaw(format(sender) + ": " + message + "<br>");
    }

    public void writeSystem(final String text) {
        writeRaw("<font color=green>" + text + "</font><br>");
    }

    public interface IListener {

        void onChatPaneMessageSent(String message);
    }

    private final class SendAction extends AbstractAction {

        public SendAction() {
            super("Send");
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            for (IListener listener : listeners) {
                listener.onChatPaneMessageSent(sendField.getText());
            }
            sendField.setText("");
            sendField.requestFocus();
        }
    }

    public static String format(Participant client) {
        return "[<font color=red>" + (client == null ? "Server" : client.getName()) + "</font>]";
    }

    public static String format(IClientToServerTransport transport) {
        return "[<font color=blue>" + transport.getName() + "</font>]";
    }

    public static String format(IServerToClientTransport transport) {
        return "[<font color=blue>" + transport.getName() + "</font>]";
    }

    public static String format(IServerTransportFactory transportFactory) {
        return "[<font color=blue>" + transportFactory.getName() + "</font>]";
    }

    public static List<String> format(List<Participant> participants) {
        return Lists.transform(participants, new Function<Participant, String>() {

            @Override
            public String apply(final Participant client) {
                return ChatPane.format(client);
            }
        });
    }

    public static String format(DisconnectReason reason) {
        return "[<font color=red>" + reason + "</font>]";
    }
}
