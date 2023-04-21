package de.hdm.cefx.server.net;

import java.io.Serializable;
import de.hdm.cefx.client.CEFXClient;

public class ServerConnection_connect implements Serializable {

    public CEFXClient client;

    public String documentUri;
}
