package fr.loria.ecoo.pbcast;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class PbCast {

    public static final int LOG_OBJECT = 0;

    public static final int LOG_AND_GOSSIP_OBJECT = 1;

    public static final int ANTI_ENTROPY = 2;

    private String workingDirPath;

    private Neighbors neighbors;

    private Receiver site;

    private Log log;

    private int round;

    public PbCast(String workingDirPath, int messagesRound, int logDelay, int maxNeighbors, Receiver site) throws Exception {
        this.workingDirPath = workingDirPath;
        this.log = new Log(this.workingDirPath + File.separator + "log");
        this.neighbors = new Neighbors(this.workingDirPath + File.separator + "neighbors", maxNeighbors);
        this.site = site;
    }

    public synchronized void receiveMessage(Collection receivedMessages) throws Exception {
        for (Iterator iter = receivedMessages.iterator(); iter.hasNext(); ) {
            Message receivedMessage = (Message) iter.next();
            if (this.log.existInLog(receivedMessage.getId())) {
                return;
            }
            switch(receivedMessage.getAction()) {
                case LOG_OBJECT:
                    this.site.deliver(receivedMessage.getContent());
                    this.log.addMessage(receivedMessage);
                    break;
                case LOG_AND_GOSSIP_OBJECT:
                    this.site.deliver(receivedMessage.getContent());
                    this.log.addMessage(receivedMessage);
                    if (!(receivedMessage.getOriginalPeerId().equals(site.getPbCastId()))) {
                        this.neighbors.addNeighbor(receivedMessage.getOriginalPeerId());
                    }
                    int messageRound = receivedMessage.getRound();
                    messageRound--;
                    if (!(messageRound == 0)) {
                        receivedMessage.setRound(messageRound);
                        Vector<Message> toSend = new Vector<Message>();
                        toSend.add(receivedMessage);
                        this.neighbors.notifyNeighbors(toSend);
                    }
                    break;
                case ANTI_ENTROPY:
                    Object[] diff = log.getDiff((Object[]) receivedMessage.getContent());
                    Vector<Message> toSend = new Vector<Message>();
                    for (int i = 0; i < diff.length; i++) {
                        Object id = diff[i];
                        Message missingMessage = log.getMessage(id);
                        missingMessage.setAction(LOG_OBJECT);
                        toSend.add(missingMessage);
                    }
                    neighbors.notifyNeighbor(receivedMessage.getOriginalPeerId(), toSend);
                    break;
                default:
                    break;
            }
        }
    }

    public void antiEntropy(Object neighbor) throws Exception {
        Message message = new Message();
        message.setAction(ANTI_ENTROPY);
        message.setOriginalPeerId(this.site.getPbCastId());
        message.setContent(this.log.getMessagesId());
        Vector<Message> toSend = new Vector<Message>();
        toSend.add(message);
        neighbors.notifyNeighbor(neighbor, toSend);
    }

    public Log getLog() {
        return log;
    }

    public Neighbors getNeighbors() {
        return neighbors;
    }

    public int getRound() {
        return round;
    }

    public Receiver getSite() {
        return site;
    }

    public void setSite(Receiver site) {
        this.site = site;
    }
}
