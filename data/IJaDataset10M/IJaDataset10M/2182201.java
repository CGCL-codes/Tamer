package it.polito.appeal.traci.query;

import it.polito.appeal.traci.protocol.Command;
import it.polito.appeal.traci.protocol.Constants;
import it.polito.appeal.traci.protocol.ResponseContainer;
import java.io.IOException;
import java.net.Socket;

public class RetrieveEdgeStateQuery extends Query {

    private String edgeID;

    public RetrieveEdgeStateQuery(Socket sock, String edgeID) throws IOException {
        super(sock);
        this.edgeID = edgeID;
    }

    public double getGlobalTravelTime(int time) throws IOException {
        Command cmd = makeReadVarCommand(Constants.CMD_GET_EDGE_VARIABLE, Constants.VAR_EDGE_TRAVELTIME, edgeID);
        cmd.content().writeUnsignedByte(Constants.TYPE_INTEGER);
        cmd.content().writeInt(time);
        ResponseContainer respc = queryAndVerifySingle(cmd);
        Command resp = respc.getResponse();
        verifyGetVarResponse(resp, Constants.RESPONSE_GET_EDGE_VARIABLE, Constants.VAR_EDGE_TRAVELTIME, edgeID);
        verify("edge travel time data type", Constants.TYPE_DOUBLE, (int) resp.content().readUnsignedByte());
        return resp.content().readDouble();
    }
}
