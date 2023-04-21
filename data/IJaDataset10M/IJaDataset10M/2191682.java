package sp.randres.garmin.packet;

import sp.randres.garmin.ByteReader;

public class RunPacket extends GarminUSBPacket {

    int trackIndex;

    int firstLapIndex;

    int lastLapIndex;

    int sportType;

    int programType;

    int multisport;

    int unused1;

    int unused2;

    long time;

    double distance;

    public RunPacket(int packetType, int reserved1, int packetId, int reserved2, int dataLength, byte[] dataPacket) throws Exception {
        super(packetType, reserved1, packetId, reserved2, dataLength, dataPacket);
        ByteReader br = new ByteReader(dataPacket);
        trackIndex = br.readInt16LE();
        firstLapIndex = br.readInt16LE();
        lastLapIndex = br.readInt16LE();
        sportType = br.readInt8();
        programType = br.readInt8();
        multisport = br.readInt8();
        unused1 = br.readInt8();
        unused2 = br.readInt32LE();
        time = br.readUInt32LE();
        distance = br.readFloat32();
    }

    @Override
    public String toString() {
        return "RunPacket [distance=" + distance + ", firstLapIndex=" + firstLapIndex + ", lastLapIndex=" + lastLapIndex + ", multisport=" + multisport + ", programType=" + programType + ", sportType=" + sportType + ", time=" + time + ", trackIndex=" + trackIndex + ", unused1=" + unused1 + ", unused2=" + unused2 + "]";
    }
}
