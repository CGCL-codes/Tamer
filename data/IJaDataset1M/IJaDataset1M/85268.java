package org.mahjong.matoso.rules;

import org.mahjong.matoso.bean.Player;

public interface IGameProps {

    /**
	 * Update the victories, defeats, given, sustain selfpick and selfpick
	 * figures of one player
	 * 
	 * @param player
	 * @param scorePlayer
	 * @param scoreOtherPlayer1
	 * @param scoreOtherPlayer2
	 * @param scoreOtherPlayer3
	 */
    public void updateGamePropsForOnePlayer(Player player, Integer scorePlayer, Integer scoreOtherPlayer1, Integer scoreOtherPlayer2, Integer scoreOtherPlayer3, String rules);
}
