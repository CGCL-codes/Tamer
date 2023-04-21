package jpar2.packets;

import jpar2.utility.Checksum;

class PacketHeader {

    private long packetLength;

    private Checksum packetChecksum;

    private Checksum recoverySetId;

    private PacketType packetType;

    PacketHeader() {
    }

    long getPacketLength() {
        return packetLength;
    }

    void setPacketLength(long packetLength) {
        this.packetLength = packetLength;
    }

    Checksum getPacketChecksum() {
        return packetChecksum;
    }

    void setPacketChecksum(Checksum packetChecksum) {
        this.packetChecksum = packetChecksum;
    }

    Checksum getRecoverySetId() {
        return recoverySetId;
    }

    void setRecoverySetId(Checksum recoverySetId) {
        this.recoverySetId = recoverySetId;
    }

    PacketType getPacketType() {
        return packetType;
    }

    void setPacketType(PacketType packetType) {
        this.packetType = packetType;
    }
}
