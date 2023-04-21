package org.openjrpg.gameplay.maps;

import java.io.Serializable;
import org.openjrpg.gameplay.entities.EntityI;

public interface LevelI extends EntityI, Serializable {

    public void setTiles(TileI[][] tiles);

    public TileI getTile(int x, int y);
}
