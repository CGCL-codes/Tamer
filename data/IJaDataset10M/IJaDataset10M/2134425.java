package org.ezfusion.agent.manager;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import org.ezfusion.dataobject.Ticket;
import org.ezfusion.msgsrv.Message;
import org.ezfusion.msgsrv.MsgTicket;
import org.ezfusion.serviceint.Logger;
import org.ezfusion.serviceint.MsgClient;
import org.ezfusion.serviceint.MsgServer;
import org.ezfusion.serviceint.SrvMgr;

public class MessageMgr implements MsgClient, Runnable {

    private Hashtable<String, MsgTicket> msgTicket;

    private SrvMgr srvMgr;

    private Ticket loggerTck;

    private Logger logger;

    private String localIP;

    private MsgServer msgServer;

    private Vector<Message> messages;

    private Ticket agentTck, msgSrvTck;

    private boolean isRunning;

    private MsgTicket exeTopicTck;

    private String agentName, agentMsgTopic;

    public MessageMgr(SrvMgr sm, String ip, String aName) {
        srvMgr = sm;
        localIP = ip;
        agentName = aName;
        msgTicket = new Hashtable<String, MsgTicket>();
        isRunning = true;
        agentMsgTopic = "agent";
        messages = new Vector<Message>();
        loggerTck = new Ticket();
        logger = (Logger) srvMgr.getService("logger", "", loggerTck);
    }

    public MsgTicket getTopicTck(String t, String ip, Integer port) {
        return msgTicket.get(t + "@" + ip + ":" + port);
    }

    public void addTopicTck(String t, String ip, Integer port, MsgTicket tck) {
        msgTicket.put(t + "@" + ip + ":" + port, tck);
    }

    public void newMsg(String topic, String serverIP, Integer serverPort) {
        if ((serverIP == null) || (serverIP == "")) {
            logger.log(this, Level.WARNING, "server IP address is null or unset, " + localIP + " address will be used");
            serverIP = localIP;
        }
        Ticket t = new Ticket();
        msgServer = (MsgServer) srvMgr.getService("MsgServer", serverIP + ":" + serverPort, t);
        if (msgServer != null) {
            MsgTicket tTck = getTopicTck(topic, serverIP, serverPort);
            if (tTck != null) try {
                logger.log(this, Level.FINE, " try to read new messages");
                List<Message> rcvMsg = msgServer.getMsg(tTck);
                if (rcvMsg != null) {
                    messages.addAll(rcvMsg);
                } else {
                    logger.log(this, Level.WARNING, "emtpy list of messages received");
                }
            } catch (Exception e) {
                logger.log(this, Level.WARNING, "cannot read new messages on " + serverIP + ":" + serverPort + ", topic '" + topic + "':" + e.getMessage());
                e.printStackTrace();
            } else logger.log(this, Level.WARNING, "has receive a new message in an unregistered topic '" + topic + "'");
        } else logger.log(this, Level.WARNING, "cannot connect to message server");
        srvMgr.releaseService(t);
    }

    private void findLocalMsgServer() {
        msgServer = null;
        while ((msgServer == null) && isRunning) {
            logger.log(this, Level.INFO, "Agent searches local message server in the framwork");
            try {
                msgSrvTck = new Ticket();
                msgServer = (MsgServer) srvMgr.getService("MsgServer", localIP + ":8083", msgSrvTck);
            } catch (Exception e) {
                logger.log(this, Level.WARNING, "cannot find message server on " + localIP + ":8083 " + e.getMessage());
            }
        }
    }

    private void addAgentTopic() {
        findLocalMsgServer();
        try {
            logger.log(this, Level.INFO, "Agent adds new topic '" + agentMsgTopic + "'");
            exeTopicTck = msgServer.addTopic(agentMsgTopic, agentName);
        } catch (Exception e) {
            logger.log(this, Level.WARNING, "cannot add new topic '" + agentMsgTopic + "' :" + e.getMessage());
        }
    }

    private void registerAgentMsgClient() {
        findLocalMsgServer();
        try {
            logger.log(this, Level.INFO, "Agent registers to topic '" + agentMsgTopic + "'");
            addTopicTck(agentMsgTopic, localIP, new Integer(8083), msgServer.registerClient(agentMsgTopic, agentName));
        } catch (Exception e) {
            logger.log(this, Level.WARNING, "cannot register to topic '" + agentMsgTopic + "':" + e.getMessage());
        }
    }

    private void unregisterAgentMsgClient() {
        if (msgServer != null) try {
            msgServer.unregisterClient(getTopicTck(agentMsgTopic, localIP, new Integer(8083)));
        } catch (Exception e) {
            logger.log(this, Level.WARNING, "cannot unregister to topic 'agent' : " + e.getMessage());
        } else logger.log(this, Level.WARNING, "message server is null");
    }

    private void removeAgentTopic() {
        if (msgServer != null) try {
            msgServer.removeTopic(exeTopicTck);
        } catch (Exception e) {
            logger.log(this, Level.WARNING, "cannot remove topic 'agent' : " + e.getMessage());
        } else logger.log(this, Level.WARNING, "message server is null");
    }

    public void addMsg(Message msg) {
        try {
            msgServer.addMsg(getTopicTck(agentMsgTopic, localIP, new Integer(8083)), msg);
        } catch (Exception e2) {
            logger.log(this, Level.WARNING, "Agent " + agentName + " cannot add new message to message server : " + e2.getMessage());
            e2.printStackTrace();
        }
    }

    public void stopMgr() {
        isRunning = false;
        srvMgr.releaseService(msgSrvTck);
    }

    public String getMsgClientName() {
        return agentName;
    }

    public void topicRemoved(String topic, String serverIP, Integer serverPort) {
        logger.log(this, Level.WARNING, "Topic " + topic + " has been removed at " + serverIP);
    }

    private void registerAgentService() {
        logger.log(this, Level.INFO, "Register " + this.getClass().getName() + " agent service in framework");
        agentTck = srvMgr.registerService(MsgClient.class.getName(), this, agentName);
    }

    private void unregisterAgentService() {
        srvMgr.unregisterService(agentTck);
    }

    public void run() {
        registerAgentService();
        addAgentTopic();
        registerAgentMsgClient();
        logger.log(this, Level.INFO, "agent started");
        while (isRunning) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        unregisterAgentMsgClient();
        removeAgentTopic();
        unregisterAgentService();
    }

    public Message pop() {
        Message result = null;
        if (!messages.isEmpty()) {
            try {
                result = (Message) messages.firstElement();
            } catch (Exception e) {
                logger.log(this, Level.WARNING, "an error occured during pop : " + e.getMessage());
                e.printStackTrace();
            }
            messages.remove(0);
        }
        return result;
    }
}
