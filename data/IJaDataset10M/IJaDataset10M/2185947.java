package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author ATracer
 *
 */
public abstract class AbyssRankDAO implements DAO {

    @Override
    public final String getClassName() {
        return AbyssRankDAO.class.getName();
    }

    public abstract void loadAbyssRank(Player player);

    public abstract boolean storeAbyssRank(Player player);
}
