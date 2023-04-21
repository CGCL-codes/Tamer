package gameserver.network.aion.serverpackets;

import gameserver.model.alliance.PlayerAlliance;
import gameserver.model.group.LootDistribution;
import gameserver.model.group.LootGroupRules;
import gameserver.model.group.LootRuleType;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_ALLIANCE_INFO extends AionServerPacket {

    private PlayerAlliance alliance;

    public SM_ALLIANCE_INFO(PlayerAlliance alliance) {
        this.alliance = alliance;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeH(buf, 4);
        writeD(buf, alliance.getObjectId());
        writeD(buf, alliance.getCaptainObjectId());
        int i = 0;
        for (int group : alliance.getViceCaptainObjectIds()) {
            writeD(buf, group);
            i++;
        }
        for (; i < 4; i++) {
            writeD(buf, 0);
        }
        LootGroupRules lootRules = this.alliance.getLootAllianceRules();
        LootRuleType lootruletype = lootRules.getLootRule();
        LootDistribution autodistribution = lootRules.getAutodistribution();
        writeD(buf, lootruletype.getId());
        writeD(buf, autodistribution.getId());
        writeD(buf, lootRules.getCommon_item_above());
        writeD(buf, lootRules.getSuperior_item_above());
        writeD(buf, lootRules.getHeroic_item_above());
        writeD(buf, lootRules.getFabled_item_above());
        writeD(buf, lootRules.getEthernal_item_above());
        writeD(buf, lootRules.getOver_ethernal());
        writeD(buf, lootRules.getOver_over_ethernal());
        writeC(buf, 0);
        writeD(buf, 63);
        for (i = 0; i < 4; i++) {
            writeD(buf, i);
            writeD(buf, 1000 + i);
        }
        writeD(buf, 0);
        writeD(buf, 0);
    }
}
