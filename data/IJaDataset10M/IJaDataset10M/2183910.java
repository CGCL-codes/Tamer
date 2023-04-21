package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.LFGApplyGroup;
import gameserver.model.gameobjects.LFGRecruitGroup;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.LGFService;
import java.nio.ByteBuffer;
import java.util.Collection;

public class SM_FIND_GROUP extends AionServerPacket {

    private int type;

    private Player player;

    public SM_FIND_GROUP(int type, Player player) {
        this.type = type;
        this.player = player;
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        writeC(buf, type);
        switch(type) {
            case 0:
                Collection<LFGRecruitGroup> playerRecruitGroups = LGFService.getInstance().geRecruitGroup(player.getCommonData().getRace());
                writeH(buf, playerRecruitGroups.size());
                writeH(buf, playerRecruitGroups.size());
                writeD(buf, (int) System.currentTimeMillis());
                for (LFGRecruitGroup playerRecruitGroup : playerRecruitGroups) {
                    Player pl = playerRecruitGroup.getPlayer();
                    writeD(buf, pl.getObjectId());
                    if (pl.isInGroup()) writeD(buf, pl.getPlayerGroup().getGroupId()); else writeD(buf, 0);
                    writeC(buf, playerRecruitGroup.getGroupType());
                    writeS(buf, playerRecruitGroup.getApplyString());
                    writeS(buf, pl.getName());
                    writeC(buf, pl.getPlayerGroup().getMembers().size());
                    writeC(buf, pl.getLevel());
                    writeC(buf, playerRecruitGroup.getMaxLevel());
                    writeD(buf, (int) playerRecruitGroup.getCreationTime());
                    if ((System.currentTimeMillis() - playerRecruitGroup.getCreationTime()) > 3600000) LGFService.getInstance().removeApplyGroup(pl.getObjectId());
                }
                break;
            case 1:
                writeD(buf, player.getObjectId());
                if (player.isInGroup()) writeD(buf, player.getPlayerGroup().getGroupId()); else writeD(buf, 0);
                writeC(buf, 0);
                writeH(buf, 1);
                break;
            case 4:
                Collection<LFGApplyGroup> playerApplyGroups = LGFService.getInstance().geApplyGroup(player.getCommonData().getRace());
                writeH(buf, playerApplyGroups.size());
                writeH(buf, playerApplyGroups.size());
                writeD(buf, (int) System.currentTimeMillis());
                for (LFGApplyGroup playerApplyGroup : playerApplyGroups) {
                    Player pl = playerApplyGroup.getPlayer();
                    writeD(buf, pl.getObjectId());
                    writeC(buf, playerApplyGroup.getGroupType());
                    writeS(buf, playerApplyGroup.getApplyString());
                    writeS(buf, pl.getName());
                    writeC(buf, pl.getPlayerClass().getClassId());
                    writeC(buf, pl.getLevel());
                    writeD(buf, (int) playerApplyGroup.getCreationTime());
                    if ((System.currentTimeMillis() - playerApplyGroup.getCreationTime()) > 3600000) LGFService.getInstance().removeApplyGroup(pl.getObjectId());
                }
                break;
            case 5:
                writeD(buf, player.getObjectId());
                writeH(buf, 722);
                writeC(buf, 0);
                break;
        }
    }
}
