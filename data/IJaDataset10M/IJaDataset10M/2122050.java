package sp.randres.garmin.packet;

import java.util.Date;
import sp.randres.garmin.ByteReader;
import sp.randres.garmin.datatypes.PositionType;

public class WaypointDataPacket extends GarminUSBPacket {

    int dtyp;

    int wpt_class;

    int dspl_color;

    int attr;

    int smbl;

    PositionType posn;

    double alt;

    double dpth;

    double dist;

    String state;

    String cc;

    int ete;

    double temp;

    Date time;

    int wpt_cat;

    public WaypointDataPacket(int packetType, int reserved1, int packetId, int reserved2, int dataLength, byte[] dataPacket) throws Exception {
        super(packetType, reserved1, packetId, reserved2, dataLength, dataPacket);
        ByteReader br = new ByteReader(dataPacket);
        dtyp = br.readInt8();
        wpt_class = br.readInt8();
        dspl_color = br.readInt8();
        attr = br.readInt8();
        smbl = br.readInt16LE();
        br.skipNBytes(18);
        int latSemi = br.readInt32LE();
        int lonSemi = br.readInt32LE();
        posn = new PositionType(latSemi, lonSemi);
        alt = br.readFloat32();
    }

    public PositionType getPosition() {
        return posn;
    }

    @Override
    public String toString() {
        return String.format("(Pos %s) (Alt %.2f m)  ", posn.toString(), alt);
    }
}
