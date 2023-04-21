package com.vitria.test.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

public class JmsWorkerImpl extends AbstractWorkerBase {

    protected ConnectionFactory jmsFactory_ = null;

    protected Connection jmsConnection_ = null;

    protected Session jmsSession_ = null;

    protected MessageConsumer jmsConsumer_ = null;

    @Override
    protected void doRun() throws Exception {
        try {
            doStart();
            Message msg = jmsConsumer_.receive(3 * 1000);
            if (msg != null) {
                Logger.info("the worker is: " + info_);
            }
        } catch (Exception e) {
            Logger.error("", e);
            doStop();
        }
    }

    @Override
    protected void doStart() throws Exception {
        if (jmsConnection_ == null) {
            connect();
            jmsConnection_.start();
        }
    }

    @Override
    protected void doStop() {
        try {
            if (jmsConnection_ != null) {
                jmsConnection_.close();
            }
        } catch (JMSException igore) {
        } finally {
            jmsConnection_ = null;
            jmsSession_ = null;
            jmsConsumer_ = null;
        }
    }

    protected void connect() throws Exception {
        jmsFactory_ = JmsLib.getConnectionFactory(info_);
        jmsConnection_ = JmsLib.createConnection(jmsFactory_, info_);
        jmsSession_ = JmsLib.createSession(jmsConnection_);
        jmsConsumer_ = JmsLib.createMessageConsumer(jmsSession_, info_);
    }
}
