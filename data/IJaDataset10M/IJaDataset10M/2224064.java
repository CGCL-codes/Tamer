package net.sourceforge.tile3d.dao;

import java.util.Collection;
import net.sourceforge.tile3d.model.Room;

public interface IRoomDAO {

    public Collection findAllRooms();

    public Boolean delete(Long p_roomId);

    public Room search(Long p_roomId);

    public Boolean create(Room room);

    public Boolean update(Room room);
}
