package pl.edu.agh.ssm.monitor;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import pl.edu.agh.ssm.monitor.data.SessionConnection;
import pl.edu.agh.ssm.monitor.data.SessionNode;
import pl.edu.agh.ssm.monitor.data.Session;
import pl.edu.agh.ssm.monitor.data.SessionPair;
import pl.edu.agh.ssm.monitor.data.SessionType;

/**
 * Container for keep informations about multimedia sessions.
 * It allows to add, remove and find session.
 * @author Tomasz Jadczyk
 *
 */
public class SessionContainer {

    private HashMap<SessionPair, Session> sessions;

    public SessionContainer() {
        sessions = new HashMap<SessionPair, Session>();
    }

    public void addSession(Session session, SessionPair pair) {
        sessions.put(pair, session);
    }

    public List<Session> getSessions(SessionType type) {
        LinkedList<Session> matchSession = new LinkedList<Session>();
        for (Session s : sessions.values()) {
            if (s.getSessionType() == type) {
                matchSession.add(s);
            }
        }
        return matchSession;
    }

    public List<Session> getSessions(InetAddress address) {
        LinkedList<Session> matchSession = new LinkedList<Session>();
        for (SessionPair pair : sessions.keySet()) {
            if (pair.getAddress() == address) {
                matchSession.add(sessions.get(pair));
            }
        }
        return matchSession;
    }

    public List<Session> getSessions(int port) {
        LinkedList<Session> matchSession = new LinkedList<Session>();
        for (SessionPair pair : sessions.keySet()) {
            if (pair.getPort() == port) {
                matchSession.add(sessions.get(pair));
            }
        }
        return matchSession;
    }

    public Session getSession(SessionPair pair) {
        return sessions.get(pair);
    }

    public void printAll() {
        System.out.println("Print:");
        for (Session s : sessions.values()) {
            System.out.println(s.getSessionDesc());
            for (SessionNode n : s.getSessionParticipants()) {
                System.out.println("\t" + n.getAddress().getHostAddress() + " " + n.getNodeType() + ": " + n.getSsrc());
                List<SessionConnection> sourceConnections = s.getConnectionsFromSource(n);
                for (SessionConnection cn : sourceConnections) {
                    System.out.println("\t\t\tConnected to : " + cn.getReceiver().getAddress().getHostAddress() + " ssrc:" + cn.getReceiver().getSsrc());
                }
            }
        }
    }

    public void removeSession(Session session) {
        LinkedList<SessionPair> pairs = new LinkedList<SessionPair>();
        for (SessionPair sp : sessions.keySet()) {
            if (sessions.get(sp) == session) {
                pairs.add(sp);
            }
        }
        for (SessionPair sp : pairs) {
            sessions.remove(sp);
        }
    }

    public void removeSession(SessionPair pair) {
        sessions.remove(pair);
    }
}
