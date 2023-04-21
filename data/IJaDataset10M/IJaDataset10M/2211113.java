package vidis.modules.byzantineGenerals;

public class AttackPacket extends APacket {

    public AttackPacket(int id) {
        super(id);
    }

    @Override
    protected PacketType getPacketType() {
        return PacketType.ATTACK;
    }
}
