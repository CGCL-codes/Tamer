package com.habitton.quartz;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class LanzadorCola {

    public void doExecute(String mensaje) {
        QueueConnection queueConnection = null;
        QueueSession queueSession = null;
        Queue colaPeticion = null;
        QueueReceiver queueReceiver = null;
        QueueSender queueSender = null;
        try {
            InitialContext jndiContext = new InitialContext();
            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("jms/pruebaQCF");
            colaPeticion = (Queue) jndiContext.lookup("jms/estadistica");
            queueConnection = queueConnectionFactory.createQueueConnection();
            queueConnection.start();
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            queueSender = queueSession.createSender(colaPeticion);
            TextMessage messageSended = queueSession.createTextMessage();
            messageSended.setText(mensaje);
            queueSender.send(messageSended);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (queueConnection != null) {
                    queueConnection.close();
                    queueConnection = null;
                }
                if (queueSession != null) {
                    queueSession.close();
                    queueSession = null;
                }
                if (queueSender != null) {
                    queueSender.close();
                    queueSender = null;
                }
                if (queueReceiver != null) {
                    queueReceiver.close();
                    queueReceiver = null;
                }
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }
    }
}
