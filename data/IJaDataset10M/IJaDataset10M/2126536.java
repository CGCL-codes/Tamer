package com.usoog.creepattack.tile;

import com.usoog.creepattack.CAGameGrid;
import com.usoog.creepattack.CAGameManager;
import com.usoog.creepattack.CAGameState;
import com.usoog.creepattack.CAPlayer;
import com.usoog.creepattack.creep.CACreep;
import com.usoog.creepattack.creep.CreepData;
import com.usoog.creepattack.tower.base.CATower;
import com.usoog.tdcore.tile.AbstractTDTile;
import java.util.Set;

/**
 * The base class for grounds
 *
 * @author jimmy
 */
public abstract class CATile extends AbstractTDTile<CACreep, CreepData, CATower, CATile, CAPlayer, CAGameGrid, CAGameManager, CAGameState> {

    private int id;

    public CATile(CAGameGrid gameGrid) {
        super(gameGrid);
    }

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMode(String mode, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isBuildable(CAPlayer p) {
        return true;
    }

    @Override
    public void getNeighbours(int radius, Set<CATile> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
