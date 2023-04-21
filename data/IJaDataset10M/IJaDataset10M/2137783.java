package org.ctor.dev.llrps2.mediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ctor.dev.llrps2.message.CloseMessage;
import org.ctor.dev.llrps2.message.RoundMediationStatusMessage;
import org.ctor.dev.llrps2.message.RoundMessage;
import org.ctor.dev.llrps2.message.StartMessage;
import org.springframework.jms.core.JmsTemplate;

public class RoundMediationManager implements MessageListener {

    private static final Log LOG = LogFactory.getLog(RoundMediationManager.class);

    private static final List<RoundMessage> EMPTY_ROUNDS = new ArrayList<RoundMessage>();

    private final JmsTemplate jmsTemplate;

    private Mediator mediator = null;

    private boolean started = false;

    private String roundResultNotificationDestination = null;

    private final List<RoundMessage> rounds = new CopyOnWriteArrayList<RoundMessage>();

    private final RoundMediationStatusMessage status = RoundMediationStatusMessage.create();

    RoundMediationManager(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void onMessage(Message message) {
        if (!(message instanceof ObjectMessage)) {
            throw new IllegalArgumentException("Message must be of type ObjectMessage: " + message.getClass());
        }
        try {
            final Object obj = ((ObjectMessage) message).getObject();
            if (obj instanceof RoundMessage) {
                final RoundMessage round = (RoundMessage) obj;
                LOG.info("received: round mediation request: " + round);
                rounds.add(round);
                status.setWaitingRounds(rounds.size());
                mediator.notifyRoundMediationRequest();
            } else if (obj instanceof StartMessage) {
                final StartMessage start = (StartMessage) obj;
                Collections.shuffle(rounds);
                LOG.info("received start message: " + start.getMessage());
                started = true;
            } else if (obj instanceof CloseMessage) {
                final CloseMessage close = (CloseMessage) obj;
                mediator.notifyCloseRequest(close);
                LOG.info("received close message: " + close.getMessage());
            }
        } catch (JMSException e) {
            LOG.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    void notifyRoundResult(RoundMessage round) {
        if (!rounds.contains(round)) {
            LOG.warn("unknown round message: " + round);
            throw new IllegalStateException();
        }
        if (!round.isCompleted()) {
            throw new IllegalStateException("round not finished");
        }
        jmsTemplate.convertAndSend(roundResultNotificationDestination, round);
        LOG.info("sent round result notification: " + round);
        rounds.remove(round);
        LOG.info(String.format("%d rounds left", rounds.size()));
        status.incrementMediatedRounds();
        status.setWaitingRounds(rounds.size());
        mediator.notifyScan();
        notifyRoundMediationStatus();
    }

    void notifyRoundMediationStatus() {
        jmsTemplate.convertAndSend(roundResultNotificationDestination, status);
        LOG.info("sent round mediation status: " + status);
    }

    public void setMediator(Mediator sessionManager) {
        this.mediator = sessionManager;
    }

    public Mediator getMediator() {
        return mediator;
    }

    public void setRoundResultNotificationDestination(String roundResultNotificationDestination) {
        this.roundResultNotificationDestination = roundResultNotificationDestination;
    }

    public String getRoundResultNotificationDestination() {
        return roundResultNotificationDestination;
    }

    public List<RoundMessage> getRounds() {
        if (!started) {
            return EMPTY_ROUNDS;
        }
        return rounds;
    }
}
