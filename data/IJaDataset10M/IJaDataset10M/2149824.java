package md.gui;

import md.core.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.swing.*;

public class ChatDialog extends JFrame implements ActionListener, MessageListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JList list = null;

    private JTextField tfield = null;

    private JButton cmdSenden = null;

    private String clientName = "";

    private String gameName = "";

    private InitialContext context = null;

    private TopicConnectionFactory factory = null;

    private TopicConnection connection = null;

    private Topic topic = null;

    private TopicSession session = null;

    private TopicSubscriber subscriber = null;

    private TopicPublisher publisher = null;

    private Vector<String> chat = new Vector<String>();

    private JScrollPane scrollPane = null;

    public ChatDialog(String clientName, String gameName) {
        super("Chat <" + clientName + ">");
        this.clientName = clientName;
        this.gameName = gameName;
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.initializeControls();
        this.addControls();
        this.initializeCommunication();
    }

    private void addControls() {
        GridBagLayout gbl = null;
        gbl = new GridBagLayout();
        this.setLayout(gbl);
        this.addComponent(this, gbl, this.scrollPane, 0, 0, 1, 1, 0, 1);
        this.addComponent(this, gbl, this.tfield, 0, 1, 2, 1, 0.0, 0.0);
        this.addComponent(this, gbl, this.cmdSenden, 2, 1, 2, 1, 0.0, 0.0);
    }

    private void initializeCommunication() {
        try {
            context = new TopicInitialContext();
            factory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
            connection = factory.createTopicConnection("dynsub", "dynsub");
            connection.start();
            topic = (Topic) context.lookup("topic/testTopic");
            session = connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
            subscriber = session.createDurableSubscriber(topic, this.clientName, "channel='" + this.gameName + "'", false);
            publisher = session.createPublisher(topic);
            subscriber.setMessageListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeControls() {
        this.list = new JList();
        this.scrollPane = new JScrollPane(list);
        this.list.setListData(this.chat);
        this.tfield = new JTextField();
        this.cmdSenden = new JButton();
        this.cmdSenden.setText("Senden");
        this.cmdSenden.addActionListener(this);
        this.cmdSenden.setActionCommand("Senden");
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand() == "Senden") {
            this.sendMessage();
        }
    }

    private void sendMessage() {
        String message = "";
        ObjectMessage om = null;
        try {
            message = this.tfield.getText();
            System.out.println("Gamename: " + this.gameName);
            om = session.createObjectMessage(new AlcatrazMessage(this.clientName, this.gameName, message));
            om.setStringProperty("channel", this.gameName);
            publisher.publish(om);
            this.tfield.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message m) {
        ObjectMessage om = null;
        AlcatrazMessage am = null;
        String message = "";
        try {
            om = (ObjectMessage) m;
            am = (AlcatrazMessage) om.getObject();
            message = am.getUser() + " says: " + am.getMessage();
            this.chat.add(message);
            this.list.setListData(this.chat);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void addComponent(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height, double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbl.setConstraints(c, gbc);
        cont.add(c);
    }
}
