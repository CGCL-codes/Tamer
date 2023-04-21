package de.iritgo.openmetix.system.infocenter.guinetworkdisplay;

import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.action.ActionTools;
import de.iritgo.openmetix.framework.user.User;
import de.iritgo.openmetix.system.infocenter.guinetworkdisplay.action.InfoCenterAction;
import de.iritgo.openmetix.system.infocenter.infocenter.InfoCenterDisplay;
import java.io.File;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NetworkDisplay implements InfoCenterDisplay {

    private String infoStoreFile;

    private File file;

    private LineNumberReader reader;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("D");

    private String month = simpleDateFormat.format(new Date());

    public NetworkDisplay() {
    }

    public void setInfoStoreFile(String infoStoreFile) {
        this.infoStoreFile = infoStoreFile;
    }

    public String getId() {
        return "network.display";
    }

    public void init(String category, int context, User user) {
    }

    public void release() {
    }

    public void info(User user, int context, String category, String icon, String message, String guiPaneId, long uniqueId, int level) {
        ClientTransceiver clientTransceiver = new ClientTransceiver(user.getNetworkChannel());
        clientTransceiver.addReceiver(user.getNetworkChannel());
        InfoCenterAction action = new InfoCenterAction(context, category, icon, message, guiPaneId, uniqueId, level);
        action.setTransceiver(clientTransceiver);
        ActionTools.sendToClient(action);
    }
}
