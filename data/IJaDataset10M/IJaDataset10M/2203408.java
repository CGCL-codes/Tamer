package dev.echoservice;

import dev.echoservice.TestTypedObject;
import javax.swing.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.util.Properties;

public class ObjectMessageClient extends JApplet implements ActionListener, MessageListener, WindowListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private TopicConnection connection = null;

    private TopicSession ssession = null;

    private TopicSession psession = null;

    private TopicSubscriber sc = null;

    private TopicPublisher pb = null;

    private Topic myTopic = null;

    private JTextArea inMessage;

    private JTextArea outMessage;

    private JButton send;

    public void init() {
        getContentPane().setLayout(new BorderLayout());
        inMessage = new JTextArea();
        inMessage.setEditable(false);
        getContentPane().add(inMessage, "Center");
        JPanel south = new JPanel();
        south.setSize(500, 100);
        south.setLayout(new GridLayout(1, 2));
        outMessage = new JTextArea();
        send = new JButton("Send");
        send.addActionListener(this);
        south.add(outMessage);
        south.add(send);
        getContentPane().add(south, "South");
        Properties env = new Properties();
        env.put("java.naming.factory.initial", "jrun.naming.JRunContextFactory");
        env.put("java.naming.provider.url", "localhost:2907");
        env.put("java.naming.factory.url.pkgs", "jrun.naming");
        env.put(Context.SECURITY_PRINCIPAL, "sampleuser");
        env.put(Context.SECURITY_CREDENTIALS, "samplepassword");
        try {
            Context ctx = new InitialContext(env);
            TopicConnectionFactory factory = (TopicConnectionFactory) ctx.lookup("jms/TopicConnectionFactory");
            connection = factory.createTopicConnection("sampleuser", "samplepassword");
            ssession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            psession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            myTopic = (Topic) ctx.lookup("jms/topic/flex/simpletopic");
            pb = psession.createPublisher(myTopic);
            sc = ssession.createSubscriber(myTopic);
            sc.setMessageListener(this);
            connection.start();
        } catch (NamingException ne) {
            ne.printStackTrace();
        } catch (JMSException je) {
            je.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        String om = outMessage.getText();
        outMessage.setText("");
        try {
            ObjectMessage ms = ssession.createObjectMessage();
            TestTypedObject mo = new TestTypedObject();
            mo.setProp2(om);
            ms.setObject(mo);
            pb.publish(ms);
        } catch (JMSException je) {
        }
    }

    public void onMessage(Message message) {
        String existingText = inMessage.getText();
        try {
            String type = message.getJMSType();
            existingText += "\nRecieved Messge Type:" + type;
            ObjectMessage oMsg = (ObjectMessage) message;
            Object tto = oMsg.getObject();
            existingText += "\n" + tto.getClass().getName();
            inMessage.setText(existingText);
        } catch (JMSException je) {
            outMessage.setText(je.toString());
        }
    }

    public static void main(String[] args) {
        ObjectMessageClient mc = new ObjectMessageClient();
        mc.init();
        JFrame window = new JFrame();
        window.getContentPane().add(mc);
        window.setSize(500, 500);
        window.setVisible(true);
        window.addWindowListener(mc);
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        System.exit(1);
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}
