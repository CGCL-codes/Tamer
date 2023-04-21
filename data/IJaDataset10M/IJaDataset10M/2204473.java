package com.narirelays.ems.jms.sample.ch04.p2p;

import java.io.*;
import java.util.Properties;
import javax.jms.*;
import javax.naming.*;

public class QLender implements MessageListener {

    private QueueConnection qConnect = null;

    private QueueSession qSession = null;

    private Queue requestQ = null;

    public QLender(String queuecf, String requestQueue) {
        try {
            Properties env = new Properties();
            env.put(Context.SECURITY_PRINCIPAL, "system");
            env.put(Context.SECURITY_CREDENTIALS, "manager");
            env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            env.put(Context.PROVIDER_URL, "tcp://198.87.91.180:61616");
            env.put("connectionFactoryNames", "QueueCF");
            env.put("queue.queue1", "jms.queue1");
            env.put("queue.request", "jms.request");
            env.put("queue.response", "jms.response");
            Context ctx = new InitialContext(env);
            QueueConnectionFactory qFactory = (QueueConnectionFactory) ctx.lookup(queuecf);
            qConnect = qFactory.createQueueConnection();
            qSession = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            requestQ = (Queue) ctx.lookup(requestQueue);
            qConnect.start();
            QueueReceiver qReceiver = qSession.createReceiver(requestQ);
            qReceiver.setMessageListener(this);
            System.out.println("Waiting for loan requests...");
        } catch (JMSException jmse) {
            jmse.printStackTrace();
            System.exit(1);
        } catch (NamingException jne) {
            jne.printStackTrace();
            System.exit(1);
        }
    }

    public void onMessage(Message message) {
        try {
            boolean accepted = false;
            MapMessage msg = (MapMessage) message;
            double salary = msg.getDouble("Salary");
            double loanAmt = msg.getDouble("LoanAmount");
            if (loanAmt < 200000) {
                accepted = (salary / loanAmt) > .25;
            } else {
                accepted = (salary / loanAmt) > .33;
            }
            System.out.println("" + "Percent = " + (salary / loanAmt) + ", loan is " + (accepted ? "Accepted!" : "Declined"));
            TextMessage tmsg = qSession.createTextMessage();
            tmsg.setText(accepted ? "Accepted!" : "Declined");
            tmsg.setJMSCorrelationID(message.getJMSMessageID());
            QueueSender qSender = qSession.createSender((Queue) message.getJMSReplyTo());
            qSender.send(tmsg);
            System.out.println("\nWaiting for loan requests...");
        } catch (JMSException jmse) {
            jmse.printStackTrace();
            System.exit(1);
        } catch (Exception jmse) {
            jmse.printStackTrace();
            System.exit(1);
        }
    }

    private void exit() {
        try {
            qConnect.close();
        } catch (JMSException jmse) {
            jmse.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String argv[]) {
        argv = new String[2];
        String queuecf = null;
        String requestq = null;
        if (argv.length == 2) {
            queuecf = argv[0];
            requestq = argv[1];
        } else {
            System.out.println("Invalid arguments. Should be: ");
            System.out.println("java QLender factory request_queue");
            System.exit(0);
        }
        queuecf = "QueueCF";
        requestq = "request";
        QLender lender = new QLender(queuecf, requestq);
        try {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("QLender application started");
            System.out.println("Press enter to quit application");
            stdin.readLine();
            lender.exit();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
