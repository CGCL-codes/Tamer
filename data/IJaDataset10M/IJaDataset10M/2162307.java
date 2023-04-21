package se.sics.tasim.aw.client;

import javax.swing.JComponent;
import se.sics.isl.transport.Transportable;

public abstract class InfoViewer {

    public abstract void init(String agentName);

    public abstract JComponent getComponent();

    public abstract void messageSent(String receiver, Transportable content);

    public abstract void messageReceived(String sender, Transportable content);
}
