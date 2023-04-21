package interfaces;

import java.util.LinkedList;
import java.util.UUID;
import common.Result;
import common.Session;
import common.UserConnectionInfo;

public interface IClientToRegional {

    public abstract boolean wasInGame(UUID sid);

    public abstract boolean connect(UUID sessionId, String userName, String ipAddress);

    public abstract LinkedList<Session> getAvailableUsers();

    public abstract void setAsAvailable(UUID sessionId);

    public abstract UserConnectionInfo requestConnectToUser(UUID sid2);

    public abstract void notifyUsersConnected(UUID sid1, String otherPlayerIP);

    public abstract String notifyUsersDisconnected(UUID sid, Result myresult);

    public void logOut(UUID sid);
}
