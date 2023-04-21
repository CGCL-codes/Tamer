package org.hourglassstudios.tempuspre.server;

import com.sun.sgs.app.*;
import java.io.Serializable;
import java.util.*;

public class Main implements AppListener, Serializable {

    protected ManagedReference user = null;

    public void initialize(Properties properties) {
        System.out.println("TempusPre game server successfully initialized!");
    }

    public ClientSessionListener loggedIn(ClientSession session) {
        try {
            user = AppContext.getDataManager().createReference(AppContext.getDataManager().getBinding("user_" + session.getName(), UserInformation.class));
        } catch (NameNotBoundException ex) {
            AppContext.getDataManager().setBinding("user_" + session.getName(), new UserInformation(session));
            user = AppContext.getDataManager().createReference(AppContext.getDataManager().getBinding("user_" + session.getName(), UserInformation.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.getForUpdate(UserInformation.class).setOnlineStatus(true, session);
        return new ClientListener(session);
    }
}
