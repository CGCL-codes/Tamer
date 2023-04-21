package net.comensus.gh.service.ejb;

import java.util.List;
import net.comensus.gh.core.entity.Hotel;
import net.comensus.gh.core.entity.Room;
import net.comensus.gh.core.entity.RoomType;
import net.comensus.gh.core.entity.type.RoomStatusEnum;

/**
 *
 * @author fab
 */
public interface RoomService {

    Room create(Hotel hotel, RoomType rt, int maxOccupancy, String desc, boolean enabled, RoomStatusEnum status);

    Room create(Hotel hotel, RoomType rt, int maxOccupancy, String desc);

    Room Update(Room room);

    void Remove(long id);

    Room GetData(long id);

    List<Room> GetListByHotel(Hotel hotel);

    List<Room> GetListByHotel(Hotel hotel, RoomType rt, RoomStatusEnum status);

    List<Room> GetListByTypeStatusAndCapacity(Hotel hotel, RoomType rt, RoomStatusEnum status, Integer capacity);
}
