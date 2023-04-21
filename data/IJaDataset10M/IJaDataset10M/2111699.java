package it.polito.appeal.traci.query;

import it.polito.appeal.traci.protocol.Command;
import it.polito.appeal.traci.protocol.Constants;
import java.io.IOException;
import java.net.Socket;

public class CloseQuery extends Query {

    public CloseQuery(Socket sock) throws IOException {
        super(sock);
    }

    public void doCommand() throws IOException {
        Command req = new Command(Constants.CMD_CLOSE);
        queryAndVerifySingle(req);
    }
}
