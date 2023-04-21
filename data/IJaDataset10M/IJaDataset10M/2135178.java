package de.iritgo.openmetix.framework.user.action;

import de.iritgo.openmetix.core.Engine;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.IritgoEngine;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.base.action.NetworkFrameworkServerAction;
import de.iritgo.openmetix.framework.base.action.WrongVersionAction;
import de.iritgo.openmetix.framework.server.Server;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.framework.user.UserEvent;
import de.iritgo.openmetix.framework.user.UserRegistry;
import java.io.IOException;
import java.util.Iterator;

/**
 * UserLoginServerAction.
 *
 * @version $Id: UserLoginServerAction.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class UserLoginServerAction extends NetworkFrameworkServerAction {

    private String userName;

    private String password;

    private String appBuildNumber;

    private String iritgoBuildNumber;

    public UserLoginServerAction() {
    }

    public UserLoginServerAction(String userName, String password) {
        super(-1);
        this.userName = userName;
        this.password = password;
        this.iritgoBuildNumber = Engine.instance().getBuildNumber();
        this.appBuildNumber = Engine.instance().getApplicationBuildNumber();
    }

    public String getTypeId() {
        return "server.action.userlogin";
    }

    public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException {
        userName = stream.readUTF();
        password = stream.readUTF();
        iritgoBuildNumber = stream.readUTF();
        appBuildNumber = stream.readUTF();
    }

    public void writeObject(FrameworkOutputStream stream) throws IOException {
        stream.writeUTF(userName);
        stream.writeUTF(password);
        stream.writeUTF(iritgoBuildNumber);
        stream.writeUTF(appBuildNumber);
    }

    public FrameworkAction getAction(ClientTransceiver clientTransceiver) {
        clientTransceiver.addReceiver(clientTransceiver.getSender());
        FrameworkAction action = rightVersions();
        if (action == null) {
            action = checkUserLogin(clientTransceiver);
        }
        return action;
    }

    private FrameworkAction rightVersions() {
        if (IritgoEngine.instance().getCommandLine().hasOption("n")) {
            return null;
        }
        String iritgoBuildNumberCurrent = Engine.instance().getBuildNumber();
        String appBuildNumberCurrent = Engine.instance().getApplicationBuildNumber();
        if (iritgoBuildNumberCurrent.equals(this.iritgoBuildNumber)) {
            if (appBuildNumberCurrent.equals(this.appBuildNumber)) {
                return null;
            }
        }
        return (FrameworkAction) new WrongVersionAction();
    }

    private FrameworkAction checkUserLogin(ClientTransceiver clientTransceiver) {
        UserRegistry userRegistry = Server.instance().getUserRegistry();
        Iterator iter = userRegistry.nameIterator();
        User user = null;
        while (iter.hasNext()) {
            Long userKey = (Long) iter.next();
            User userSearch = (User) userRegistry.getUser(userKey);
            if ((userSearch.getPassword().equals(password)) && (userSearch.getName().equals(userName))) {
                user = userSearch;
                break;
            }
        }
        if ((user == null) || (user.isOnline())) {
            if (user == null) {
                return (FrameworkAction) new UserLoginFailureAction(UserLoginFailureAction.BAD_USERNAME_OR_PASSWORD);
            }
            if ((user != null) && (user.isOnline())) {
                return (FrameworkAction) new UserLoginFailureAction(UserLoginFailureAction.USER_ALREADY_ONLINE);
            }
        }
        user.setNetworkChannel(clientTransceiver.getSender());
        user.setOnline(true);
        clientTransceiver.getConnectedChannel().setCustomContextObject(user);
        Engine.instance().getEventRegistry().fire("UserLogin", new UserEvent(user));
        return (FrameworkAction) new UserLoginAction(user);
    }
}
