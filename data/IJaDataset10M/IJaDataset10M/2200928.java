package nakayo.gameserver.network.aion.serverpackets;

import nakayo.gameserver.network.aion.AionConnection;
import nakayo.gameserver.network.aion.AionServerPacket;
import nakayo.gameserver.skillengine.model.Effect;
import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * @author Avol, ATracer
 */
public class SM_ABNORMAL_STATE extends AionServerPacket {

    private Collection<Effect> effects;

    private int abnormals;

    public SM_ABNORMAL_STATE(Collection<Effect> effects, int abnormals) {
        this.effects = effects;
        this.abnormals = abnormals;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeD(buf, abnormals);
        writeD(buf, 0x00);
        writeH(buf, effects.size());
        for (Effect effect : effects) {
            writeD(buf, effect.getEffectorId());
            writeH(buf, effect.getSkillId());
            writeC(buf, effect.getSkillLevel());
            writeC(buf, effect.getTargetSlot());
            writeD(buf, effect.getElapsedTime());
        }
    }
}
