package net.sf.copernicus.server.m2;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import net.sf.copernicus.server.m2.discovery.Agent;
import net.sf.copernicus.server.m2.http.HttpMessageSyntaxException;
import net.sf.copernicus.server.m2.http.HttpRequest;
import net.sf.copernicus.server.m2.http.HttpResponse;
import net.sf.copernicus.server.m2.transport.Client;
import net.sf.copernicus.server.m2.transport.ClientConnectionListener;
import net.sf.copernicus.server.m2.transport.ConnectionHandler;
import org.apache.log4j.Logger;

public class Session implements RequestLogicHandler, ClientConnectionListener {

    private static Logger log = Logger.getLogger(Session.class);

    private class AgentSession {

        private Client connection;

        private HttpResponse response = new HttpResponse();

        public AgentSession(Client connection) {
            this.connection = connection;
        }

        public Client getConnection() {
            return connection;
        }

        public HttpResponse getResponce() {
            return response;
        }
    }

    private Dispatcher dispatcher;

    private ConcurrentHashMap<String, AgentSession> agentSessions = new ConcurrentHashMap<String, AgentSession>();

    private ConnectionHandler clientHandler;

    private HttpRequest clientRequest = new HttpRequest();

    private RequestLogic logicEngine;

    private ConcurrentHashMap<String, String> clientRequestsToSend = new ConcurrentHashMap<String, String>();

    public Session(Dispatcher dispatcher, ConnectionHandler clientHandler) {
        this.dispatcher = dispatcher;
        this.clientHandler = clientHandler;
    }

    public String getClientId() {
        return peerId(clientHandler);
    }

    public void handleDataFromClient(String s) {
        log.debug("Got data: " + s + "\nFrom client " + getClientId());
        try {
            if (!clientRequest.isComplete()) {
                clientRequest.appendData(s);
                if (clientRequest.isComplete()) {
                    log.debug("Client request complete, connecting to Agent.");
                    if (logicEngine == null) logicEngine = new RequestLogic(this);
                    logicEngine.requestFromClient(clientRequest);
                    clientRequest.reset();
                }
            }
        } catch (HttpMessageSyntaxException e) {
            log.warn("Client " + getClientId() + ": Request syntax error: " + e.getMessage(), e);
            sendErrToClient("400 Bad Request");
            kill();
        } catch (Exception e) {
            log.error("Client " + getClientId() + ": Internal error: " + e.getMessage(), e);
            sendErrToClient("500 Internal Server Error");
            kill();
        }
    }

    @Override
    public void replyErrorToClient(String errorText) {
        log.error("Client " + getClientId() + ": " + errorText);
        sendErrToClient("400 Bad Request : " + errorText);
    }

    @Override
    public void sendToAgent(String request, String host) {
        if (agentSessions.containsKey(host)) {
            agentSessions.get(host).getConnection().send(request.toString());
        } else {
            Client agent_cn = new Client(host, 5988, this);
            agentSessions.put(host, new AgentSession(agent_cn));
            clientRequestsToSend.put(host, request);
            agent_cn.connect();
        }
    }

    @Override
    public void sendToAllAgents(String request) {
        Iterator<Agent> agents = dispatcher.getAgents();
        while (agents.hasNext()) {
            sendToAgent(request, agents.next().hostname);
        }
    }

    @Override
    public void sendToClient(String response) {
        if (log.isDebugEnabled()) log.debug("Sending response to Client " + peerId(clientHandler) + ":\r\n" + response);
        clientHandler.send(response);
    }

    @Override
    public Iterator<Agent> getAgentList() {
        return dispatcher.getAgents();
    }

    @Override
    public void onConnectedToServer(ConnectionHandler handler) {
        log.info("Connected to Agent: " + peerId(handler));
        if (clientRequestsToSend.containsKey(handler.getHostname())) {
            handler.send(clientRequestsToSend.get(handler.getHostname()));
            clientRequestsToSend.remove(handler.getHostname());
        }
    }

    @Override
    public void onConnectionBroken(ConnectionHandler handler) {
        log.info("Agent broke connection: " + peerId(handler));
        agentSessions.remove(handler.getHostname());
    }

    @Override
    public void onError(ConnectionHandler handler, Exception e) {
        log.fatal("Connection error: " + e.getMessage() + ", Agent: " + peerId(handler), e);
    }

    @Override
    public void onReadString(ConnectionHandler handler, String s) {
        log.debug("Got data: " + s + "\n from Agent " + peerId(handler));
        AgentSession agentSession = agentSessions.get(handler.getHostname());
        HttpResponse agentResponce = agentSession.getResponce();
        try {
            if (!agentResponce.isComplete()) {
                agentResponce.appendData(s);
                if (agentResponce.isComplete()) {
                    log.debug("Agent response complete.");
                    logicEngine.responseFromAgent(agentResponce, handler.getHostname());
                    agentResponce.reset();
                }
            }
        } catch (HttpMessageSyntaxException e) {
            log.error("Error in Agent HTTP response.", e);
            agentResponce.reset();
        }
    }

    public static String peerId(ConnectionHandler handler) {
        return handler.getHostname() + ":" + handler.getPortNumber();
    }

    private void sendErrToClient(String status) {
        clientHandler.send("HTTP/1.1 " + status + "\r\n" + "Server: Copernicus Server M2 v 0.0.1\r\n" + "Content-Length: 0\r\n\r\n");
    }

    public void kill() {
        for (AgentSession session : agentSessions.values()) session.getConnection().disconnect();
        agentSessions.clear();
        clientHandler.disconnect();
        dispatcher.removeSession(getClientId());
    }

    @Override
    public void kickAgent(String agent) {
        if (!agentSessions.containsKey(agent)) return;
        log.info("Disconnecting Agent " + agent);
        AgentSession agentSession = agentSessions.get(agent);
        agentSession.getConnection().disconnect();
        agentSessions.remove(agent);
    }

    @Override
    public String getPrimaryAgent() {
        return dispatcher.getPrimaryAgent().hostname;
    }
}
