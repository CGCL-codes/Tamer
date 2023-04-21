package nzdis.agent.amc;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.agent.JasConstants;
import javax.agent.service.directory.AgentDescription;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import nzdis.agent.Platform;
import nzdis.agent.message.Message;

/**
 * Represents a simle Message gui window.
 * 
 *<br><br>
 * MessageForm.java<br>
 * Created: Thu Jun 20 16:36:07 2002<br>
 *
 * @author  <a href="mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @version $Revision: 1.2 $ $Date: 2003/05/02 06:53:51 $
 */
public class MessageForm extends JInternalFrame {

    Platform platform;

    AgentDescription[] agents;

    String[] agentNames;

    JTextField conversationID;

    JTextField replyWith;

    JTextField language;

    JTextField ontology;

    JTextField protocol;

    JTextArea content;

    JTextArea preview;

    JComboBox act;

    public JComboBox sender;

    public JComboBox receiver;

    /**
   * Creates a message gui. */
    public MessageForm(final Platform p) {
        super("FIPA Message", true, true, true, true);
        this.platform = p;
        updateAgentTable();
        initGUI();
    }

    private void initGUI() {
        final JPanel main = new JPanel();
        final GridBagLayout l = new GridBagLayout();
        main.setLayout(l);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 1.0;
        prepareLabel(main, l, "Communicative ACT  ");
        this.act = prepareComboBox(main, l, Message.COMM_ACTS);
        this.act.setSelectedIndex(7);
        prepareLabel(main, l, "Sender");
        this.sender = prepareComboBox(main, l, this.agentNames);
        prepareLabel(main, l, "Receiver");
        this.receiver = prepareComboBox(main, l, this.agentNames);
        prepareLabel(main, l, "Language ");
        this.language = prepareTextField(main, l);
        prepareLabel(main, l, "Ontology");
        this.ontology = prepareTextField(main, l);
        prepareLabel(main, l, "Protocol");
        this.protocol = prepareTextField(main, l);
        prepareLabel(main, l, "Reply-with");
        this.replyWith = prepareTextField(main, l);
        prepareLabel(main, l, "Conversation-id");
        this.conversationID = prepareTextField(main, l);
        prepareLabel(main, l, "Content");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        this.content = new JTextArea(4, 40);
        JScrollPane scontent = new JScrollPane(this.content);
        l.setConstraints(scontent, c);
        main.add(scontent);
        JSeparator sep = new JSeparator();
        l.setConstraints(sep, c);
        main.add(sep);
        prepareLabel(main, l, "Message preview");
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        this.preview = new JTextArea(7, 40);
        scontent = new JScrollPane(this.preview);
        l.setConstraints(scontent, c);
        main.add(scontent);
        sep = new JSeparator();
        l.setConstraints(sep, c);
        main.add(sep);
        c.fill = GridBagConstraints.HORIZONTAL;
        final JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        final JButton update = new JButton("Update preview");
        update.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final Message m = prepareMessage();
                preview.setText(m.toString());
            }
        });
        final JButton send = new JButton("Send Message");
        send.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final Message m = prepareMessage();
                platform.send(m);
            }
        });
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(cancel);
        buttons.add(update);
        buttons.add(send);
        l.setConstraints(buttons, c);
        main.add(buttons);
        getContentPane().add(main);
        pack();
    }

    private Message prepareMessage() {
        final Message m = new Message((String) act.getSelectedItem());
        m.setSender(agents[sender.getSelectedIndex()].getAgentName());
        m.setReceiver(agents[receiver.getSelectedIndex()].getAgentName());
        addToMessage(m, Message.LANGUAGE, language.getText());
        addToMessage(m, Message.ONTOLOGY, ontology.getText());
        addToMessage(m, Message.PROTOCOL, protocol.getText());
        addToMessage(m, Message.REPLY_WITH, replyWith.getText());
        addToMessage(m, Message.CONVERSATION_ID, conversationID.getText());
        addToMessage(m, Message.CONTENT, content.getText());
        return m;
    }

    private void addToMessage(Message m, String key, String value) {
        if (!value.trim().equals("")) {
            m.set(key, value.trim());
        }
    }

    private void updateAgentTable() {
        final AgentDescription desc = this.platform.createAgentDescription();
        this.agents = this.platform.search(desc);
        List l = new ArrayList();
        for (int i = 0; i < this.agents.length; i++) {
            l.add(this.agents[i].get(JasConstants.AGENT_DISPLAY_NAME));
        }
        this.agentNames = (String[]) l.toArray(new String[l.size()]);
    }

    private void prepareLabel(JPanel main, GridBagLayout l, String text) {
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.RELATIVE;
        final JLabel label = new JLabel(text);
        l.setConstraints(label, c);
        main.add(label);
    }

    private JTextField prepareTextField(JPanel main, GridBagLayout l) {
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        final JTextField tf = new JTextField();
        l.setConstraints(tf, c);
        main.add(tf);
        return tf;
    }

    private JComboBox prepareComboBox(JPanel main, GridBagLayout l, Object[] data) {
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        final JComboBox cb = new JComboBox(data);
        l.setConstraints(cb, c);
        main.add(cb);
        return cb;
    }
}
