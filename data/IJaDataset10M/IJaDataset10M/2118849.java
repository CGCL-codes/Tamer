package org.openaion.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import org.openaion.gameserver.model.templates.GatherableTemplate;
import org.openaion.gameserver.model.templates.gather.Material;
import org.openaion.gameserver.network.aion.AionConnection;
import org.openaion.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer, orz
 *
 */
public class SM_GATHER_UPDATE extends AionServerPacket {

    private GatherableTemplate template;

    private int action;

    private int itemId;

    private int success;

    private int failure;

    private int nameId;

    public SM_GATHER_UPDATE(GatherableTemplate template, Material material, int success, int failure, int action) {
        this.action = action;
        this.template = template;
        this.itemId = material.getItemid();
        this.success = success;
        this.failure = failure;
        this.nameId = material.getNameid();
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeH(buf, template.getSkillLevel());
        writeC(buf, action);
        writeD(buf, itemId);
        switch(action) {
            case 0:
                {
                    writeD(buf, 100);
                    writeD(buf, 100);
                    writeD(buf, 0);
                    writeD(buf, 1200);
                    writeD(buf, 1330011);
                    writeH(buf, 0x24);
                    writeD(buf, nameId);
                    writeH(buf, 0);
                    break;
                }
            case 1:
                {
                    writeD(buf, success);
                    writeD(buf, failure);
                    writeD(buf, 700);
                    writeD(buf, 1200);
                    writeD(buf, 0);
                    writeH(buf, 0);
                    break;
                }
            case 2:
                {
                    writeD(buf, 100);
                    writeD(buf, failure);
                    writeD(buf, 700);
                    writeD(buf, 1200);
                    writeD(buf, 0);
                    writeH(buf, 0);
                    break;
                }
            case 5:
                {
                    writeD(buf, 0);
                    writeD(buf, 0);
                    writeD(buf, 700);
                    writeD(buf, 1200);
                    writeD(buf, 1330080);
                    writeH(buf, 0);
                    break;
                }
            case 6:
                {
                    writeD(buf, 100);
                    writeD(buf, failure);
                    writeD(buf, 700);
                    writeD(buf, 1200);
                    writeD(buf, 0);
                    writeH(buf, 0);
                    break;
                }
            case 7:
                {
                    writeD(buf, success);
                    writeD(buf, 100);
                    writeD(buf, 0);
                    writeD(buf, 1200);
                    writeD(buf, 1330079);
                    writeH(buf, 0x24);
                    writeD(buf, nameId);
                    writeH(buf, 0);
                    break;
                }
        }
    }
}
