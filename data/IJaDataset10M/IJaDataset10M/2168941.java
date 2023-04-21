package nakayo.gameserver.network.aion.serverpackets;

import nakayo.gameserver.model.gameobjects.player.ToyPet;
import nakayo.gameserver.network.aion.AionConnection;
import nakayo.gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * @author xitanium
 */
public class SM_PET_MOVE extends AionServerPacket {

    private int actionId;

    private ToyPet pet;

    public SM_PET_MOVE(int actionId, ToyPet pet) {
        this.actionId = actionId;
        this.pet = pet;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, pet.getUid());
        if (actionId != 0) writeC(buf, actionId);
        switch(actionId) {
            case 0:
                writeC(buf, 0);
                writeF(buf, pet.getX1());
                writeF(buf, pet.getY1());
                writeF(buf, pet.getZ1());
                writeC(buf, pet.getH());
            case 12:
                writeF(buf, pet.getX1());
                writeF(buf, pet.getY1());
                writeF(buf, pet.getZ1());
                writeC(buf, pet.getH());
                writeF(buf, pet.getX2());
                writeF(buf, pet.getY2());
                writeF(buf, pet.getZ2());
                break;
            default:
                break;
        }
    }
}
