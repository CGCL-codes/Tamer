package org.pokenet.server.backend.entity;

import org.pokenet.server.battle.BattleField;
import org.pokenet.server.battle.Pokemon;
import org.simpleframework.xml.Root;

/**
 * Provides an interface for all game objects that can be battled
 * @author shadowkanji
 *
 */
@Root
public interface Battleable {

    public boolean isBattling();

    public String getName();

    public Pokemon[] getParty();

    public int getBattleId();

    public Battleable getOpponent();

    public BattleField getBattleField();

    public void setBattleField(BattleField b);

    public void setParty(Pokemon[] team);

    public void setBattleId(int battleID);
}
