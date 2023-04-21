package com.aionemu.gameserver.network.aion.clientpackets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.FriendList.Status;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_SEARCH;
import com.aionemu.gameserver.world.World;
import com.google.inject.Inject;

/**
 * Received when a player searches using the social search panel
 * @author Ben
 *
 */
public class CM_PLAYER_SEARCH extends AionClientPacket {

    /**
	 * The max number of players to return as results
	 */
    public static final int MAX_RESULTS = 128;

    @Inject
    private World world;

    private String name;

    private int region;

    private int classMask;

    private int minLevel;

    private int maxLevel;

    private boolean lfgOnly;

    public CM_PLAYER_SEARCH(int opcode) {
        super(opcode);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void readImpl() {
        name = readS();
        readB(44 - (name.length() * 2 + 2));
        region = readD();
        classMask = readD();
        minLevel = readC();
        maxLevel = readC();
        lfgOnly = readC() == 1 ? true : false;
        readC();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void runImpl() {
        Iterator<Player> it = world.getPlayersIterator();
        List<Player> matches = new ArrayList<Player>(MAX_RESULTS);
        while (it.hasNext() && matches.size() < MAX_RESULTS) {
            Player player = it.next();
            if (player.getFriendList().getStatus() == Status.OFFLINE) continue; else if (lfgOnly && !player.isLookingForGroup()) continue; else if (!name.isEmpty() && !player.getName().toLowerCase().contains(name.toLowerCase())) continue; else if (minLevel != 0xFF && player.getLevel() < minLevel) continue; else if (maxLevel != 0xFF && player.getLevel() > maxLevel) continue; else if (classMask > 0 && (player.getPlayerClass().getMask() & classMask) == 0) continue; else if (region > 0 && player.getActiveRegion().getMapId() != region) continue; else {
                matches.add(player);
            }
        }
        sendPacket(new SM_PLAYER_SEARCH(matches));
    }
}
