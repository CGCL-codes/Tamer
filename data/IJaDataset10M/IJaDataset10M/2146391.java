package archive.playground.david.vis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.matsim.utils.vis.snapshots.writers.PositionInfo;
import org.matsim.utils.vis.snapshots.writers.PositionInfo.VehicleState;
import archive.playground.david.vis.OTFVisNet;
import playground.david.vis.interfaces.OTFAgentHandler;

public class DefaultAgentHandler extends OTFParamProviderA implements OTFAgentHandler<PositionInfo> {

    private static OTFVisNet visnet = null;

    protected float x, y, color;

    protected int state;

    protected String id = "not def.";

    public void readAgent(DataInputStream in) throws IOException {
        id = in.readUTF();
        x = in.readFloat();
        y = in.readFloat();
        state = in.readInt();
        color = in.readFloat() * 3.6f;
    }

    public void writeAgent(PositionInfo pos, DataOutputStream out) throws IOException {
        out.writeUTF(pos.getAgentId().toString());
        out.writeFloat((float) ((pos.getEasting() - visnet.minEasting()) * OTFVisNet.zoomFactorX));
        out.writeFloat((float) ((pos.getNorthing() - visnet.minNorthing()) * OTFVisNet.zoomFactorY));
        out.writeInt(pos.getVehicleState() == VehicleState.Parking ? 1 : 0);
        out.writeFloat((float) pos.getSpeed());
    }

    @Override
    public int getIntParam(int index) throws UnsupportedOperationException {
        switch(index) {
            case 3:
                return state;
        }
        ;
        return 0;
    }

    @Override
    public float getFloatParam(int index) throws UnsupportedOperationException {
        switch(index) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return color;
        }
        ;
        return 0;
    }

    @Override
    public String getStringParam(int index) throws UnsupportedOperationException {
        return id;
    }

    public int getParamCount() {
        return 5;
    }

    public final String getLongName(int index) {
        switch(index) {
            case 0:
                return "PosX";
            case 1:
                return "PosY";
            case 2:
                return "Color";
            case 3:
                return "State";
            case 4:
                return "AgentId";
        }
        ;
        return null;
    }

    public void setOTFNet(OTFVisNet net) {
        visnet = net;
    }
}
