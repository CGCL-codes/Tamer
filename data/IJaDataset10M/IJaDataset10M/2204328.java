package org.pokenet.server.battle.impl;

import org.pokenet.server.backend.entity.NonPlayerChar;
import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.backend.entity.Positionable.Direction;
import org.pokenet.server.battle.DataService;

/**
 * When the player is challenged by an NPC, this class moves the player to the NPC and launches a battle
 * @author shadowkanji
 *
 */
public class NpcBattleLauncher implements Runnable {

    private NonPlayerChar m_npc;

    private PlayerChar m_player;

    /**
	 * Constructor
	 * @param n
	 * @param p
	 */
    public NpcBattleLauncher(NonPlayerChar n, PlayerChar p) {
        m_npc = n;
        m_npc.setLastBattleTime(System.currentTimeMillis());
        m_player = p;
    }

    /**
	 * Moves the player to the npc and starts the battle
	 */
    public void run() {
        try {
            m_player.setBattling(true);
            m_npc.challengePlayer(m_player);
            Thread.sleep(1000);
            switch(m_npc.getFacing()) {
                case Up:
                    m_player.setFacing(Direction.Down);
                    break;
                case Down:
                    m_player.setFacing(Direction.Up);
                    break;
                case Left:
                    m_player.setFacing(Direction.Right);
                    break;
                case Right:
                    m_player.setFacing(Direction.Left);
                    break;
            }
            m_player.ensureHealthyPokemon();
            m_player.setBattleField(new NpcBattleField(DataService.getBattleMechanics(), m_player, m_npc));
        } catch (Exception e) {
            m_player.setBattling(false);
            e.printStackTrace();
        }
    }

    /**
	 * Starts the battle launcher
	 */
    public void start() {
        new Thread(this).start();
    }
}
