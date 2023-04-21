package net.sourceforge.tile3d.service;

import java.util.Collection;
import net.sourceforge.tile3d.model.GroupTile;

public interface IGroupTileService {

    public Collection findAllGroups();

    public Boolean create(GroupTile p_groupTile);

    public Boolean delete(Integer p_groupId);

    public Boolean update(GroupTile p_groupTile);

    public GroupTile search(Integer p_groupTileId);
}
