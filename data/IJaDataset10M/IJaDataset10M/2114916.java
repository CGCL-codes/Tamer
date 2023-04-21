package com.loanapp.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.loanapp.domain.CreditHistoryBean;

public class MessageReceiverImpl implements MessageReceiver {

    private static Log log = LogFactory.getLog(MessageReceiverImpl.class);

    private JmsReceiver jmsReceiver;

    public JmsReceiver getJmsReceiver() {
        return this.jmsReceiver;
    }

    public void setJmsReceiver(JmsReceiver jmsReceiver) {
        this.jmsReceiver = jmsReceiver;
    }

    public CreditHistoryBean receive() {
        CreditHistoryBean creditHistory = new CreditHistoryBean();
        try {
            log.debug("MessageReceiverImpl started");
            creditHistory = jmsReceiver.receiveMessage();
            log.debug("MessageReceiverImpl end");
        } catch (Exception e) {
            log.error(e, e);
        }
        return creditHistory;
    }
}
