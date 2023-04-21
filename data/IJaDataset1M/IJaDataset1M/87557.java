package de.objectinc.samsha.ui.information;

import de.objectinc.samsha.information.ConnectionInformation;

public interface IConnectionUIListener {

    public void onConnected(ConnectionInformation info);

    public void onDisconnected(ConnectionInformation info);

    public void onConnecting(ConnectionInformation info);

    public void onFailure(ConnectionInformation info);
}
