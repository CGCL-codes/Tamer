package net.sourceforge.tile3d.dao;

import java.util.Collection;
import net.sourceforge.tile3d.model.GroupTile;

public interface IGroupTileDAO {

    public Collection findAllGroupTiles();

    public Boolean delete(Integer p_groupTile);

    public GroupTile search(Integer p_groupTileId);

    public Boolean create(GroupTile groupTile);

    public Boolean update(GroupTile groupTile);
}
