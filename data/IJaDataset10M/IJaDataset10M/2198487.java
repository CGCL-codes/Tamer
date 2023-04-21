package org.blindsideproject.asterisk;

import java.io.IOException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.blindsideproject.asterisk.meetme.MeetMeRoomAdapter;

public class AsteriskVoiceService implements IVoiceService {

    protected static Logger logger = LoggerFactory.getLogger(AsteriskVoiceService.class);

    private ManagerConnection managerConnection;

    private AsteriskServer asteriskServer = new DefaultAsteriskServer();

    public void setManagerConnection(ManagerConnection connection) {
        this.managerConnection = connection;
    }

    public void start() {
        try {
            logger.info("Logging at " + managerConnection.getHostname() + ":" + managerConnection.getPort());
            managerConnection.login();
            ((DefaultAsteriskServer) asteriskServer).setManagerConnection(managerConnection);
            ((DefaultAsteriskServer) asteriskServer).initialize();
        } catch (IOException e) {
            logger.error("IOException while connecting to Asterisk server.");
        } catch (TimeoutException e) {
            logger.error("TimeoutException while connecting to Asterisk server.");
        } catch (AuthenticationFailedException e) {
            logger.error("AuthenticationFailedException while connecting to Asterisk server.");
        } catch (ManagerCommunicationException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            managerConnection.logoff();
        } catch (IllegalStateException e) {
            logger.error("Logging off when Asterisk Server is not connected.");
        }
    }

    public IConference getConference(String id) {
        IConference bridge = null;
        try {
            MeetMeRoom room = asteriskServer.getMeetMeRoom(id);
            bridge = new MeetMeRoomAdapter(room);
            bridge.getParticipants();
        } catch (ManagerCommunicationException e) {
            e.printStackTrace();
        }
        return bridge;
    }

    public Collection<MeetMeUser> getUsers(String roomId) {
        MeetMeRoom room;
        try {
            room = asteriskServer.getMeetMeRoom(roomId);
            return room.getUsers();
        } catch (ManagerCommunicationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addAsteriskServerListener(AsteriskServerListener listener) throws ManagerCommunicationException {
        asteriskServer.addAsteriskServerListener(listener);
    }
}
