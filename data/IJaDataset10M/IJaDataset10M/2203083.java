package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * This packet is displaying visible players.
 *
 * @author -Nemesiss-
 *
 */
public class SM_PLAYER_INFO extends AionServerPacket {

    /**
	 * Visible player
	 */
    private final Player player;

    private final float x, y, z;

    private final byte heading;

    private final boolean self;

    /**
	 * Constructs new <tt>SM_CI </tt> packet
	 *
	 * @param player
	 *            actual player.
	 */
    public SM_PLAYER_INFO(Player player, boolean self) {
        this.player = player;
        this.heading = player.getHeading();
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.self = self;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        PlayerCommonData pcd = player.getCommonData();
        final int raceId = pcd.getRace().getRaceId();
        final int genderId = pcd.getGender().getGenderId();
        final PlayerAppearance playerAppearance = player.getPlayerAppearance();
        writeF(buf, x);
        writeF(buf, y);
        writeF(buf, z);
        writeD(buf, player.getObjectId());
        int raceSex = 100000 + raceId * 2 + genderId;
        writeD(buf, raceSex);
        writeD(buf, raceSex);
        writeC(buf, 0x26);
        writeC(buf, raceId);
        writeC(buf, pcd.getPlayerClass().getClassId());
        writeC(buf, genderId);
        writeC(buf, 1);
        writeC(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0x00);
        writeC(buf, heading);
        writeS(buf, player.getName());
        writeD(buf, -1);
        writeD(buf, 0);
        byte[] unk = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
        writeB(buf, unk);
        writeC(buf, 66);
        writeC(buf, 0x00);
        writeC(buf, 0x00);
        writeC(buf, 0x00);
        writeH(buf, 0);
        writeD(buf, playerAppearance.getSkinRGB());
        writeD(buf, playerAppearance.getHairRGB());
        writeD(buf, playerAppearance.getLipRGB());
        writeD(buf, playerAppearance.getEyeRGB());
        writeC(buf, playerAppearance.getFace());
        writeC(buf, playerAppearance.getHair());
        writeC(buf, playerAppearance.getDeco());
        writeC(buf, playerAppearance.getTattoo());
        writeC(buf, 5);
        writeC(buf, playerAppearance.getFaceShape());
        writeC(buf, playerAppearance.getForehead());
        writeC(buf, playerAppearance.getEyeHeight());
        writeC(buf, playerAppearance.getEyeSpace());
        writeC(buf, playerAppearance.getEyeWidth());
        writeC(buf, playerAppearance.getEyeSize());
        writeC(buf, playerAppearance.getEyeShape());
        writeC(buf, playerAppearance.getEyeAngle());
        writeC(buf, playerAppearance.getBrowHeight());
        writeC(buf, playerAppearance.getBrowAngle());
        writeC(buf, playerAppearance.getBrowShape());
        writeC(buf, playerAppearance.getNose());
        writeC(buf, playerAppearance.getNoseBridge());
        writeC(buf, playerAppearance.getNoseWidth());
        writeC(buf, playerAppearance.getNoseTip());
        writeC(buf, playerAppearance.getCheek());
        writeC(buf, playerAppearance.getLipHeight());
        writeC(buf, playerAppearance.getMouthSize());
        writeC(buf, playerAppearance.getLipSize());
        writeC(buf, playerAppearance.getSmile());
        writeC(buf, playerAppearance.getLipShape());
        writeC(buf, playerAppearance.getJawHeigh());
        writeC(buf, playerAppearance.getChinJut());
        writeC(buf, playerAppearance.getEarShape());
        writeC(buf, playerAppearance.getHeadSize());
        writeC(buf, playerAppearance.getNeck());
        writeC(buf, playerAppearance.getNeckLength());
        writeC(buf, playerAppearance.getShoulders());
        writeC(buf, playerAppearance.getTorso());
        writeC(buf, playerAppearance.getChest());
        writeC(buf, playerAppearance.getWaist());
        writeC(buf, playerAppearance.getHips());
        writeC(buf, playerAppearance.getArmThickness());
        writeC(buf, playerAppearance.getHandSize());
        writeC(buf, playerAppearance.getLegThicnkess());
        writeC(buf, playerAppearance.getFootSize());
        writeC(buf, playerAppearance.getFacialRate());
        writeC(buf, 0x00);
        writeC(buf, -7);
        writeC(buf, playerAppearance.getVoice());
        writeD(buf, 0);
        writeF(buf, playerAppearance.getHeight());
        writeF(buf, 0.25f);
        writeF(buf, 2);
        writeF(buf, 6);
        writeD(buf, 0x08980898);
        writeC(buf, 2);
        writeS(buf, "");
        writeD(buf, 0);
        writeD(buf, 0);
        writeD(buf, 0);
        writeF(buf, 0);
        writeF(buf, 0);
        writeF(buf, 0);
        writeC(buf, 0);
        writeC(buf, self ? 0x40 : 0x00);
        writeS(buf, "aion-emu");
        writeD(buf, player.getLevel());
        writeC(buf, 0x00);
        writeD(buf, 0x01);
        writeB(buf, new byte[5]);
    }
}
