package com.thegreatchina.im.msn.backend.cmd.client;

import com.thegreatchina.im.msn.Contact.Status;
import com.thegreatchina.im.msn.backend.cmd.ClientCommand;
import com.thegreatchina.im.msn.backend.cmd.ServerCommand;

/**
 * Setting Your Presence
 * To manually set or change your status, send the CHG command with a TrID, 
 * the three letter status code for your status, and your client ID number. 
 * If successful, the server will echo your command back to you. Otherwise, 
 * it will most likely disconnect you immediately. Because status codes are 
 * case sensitive, sending nln will result in the server closing the connection.
 * 
 * If you attempt to change your status too rapidly, you may receive error 800 
 * in response to some CHGs.
 * 
 * @author patrick_jiang
 *
 */
public class CHG extends ClientCommand {

    public CHG(int trId, Status status, long clientIdNumber) {
        super("CHG", trId, new String[] { status.toString(), "" + clientIdNumber });
    }

    public ServerCommand getResponse() {
        return new com.thegreatchina.im.msn.backend.cmd.server.CHG(this.getTrId());
    }
}
