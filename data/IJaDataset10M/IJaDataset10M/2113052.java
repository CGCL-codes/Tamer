package at.ac.wu.ec.projbib.persist;

import java.util.List;
import org.springframework.security.annotation.Secured;
import at.ac.wu.ec.projbib.domain.Room;

/**
 * Persists rooms
 * 
 * @author stani
 * 
 */
public interface RoomDAO {

    /**
	 * 
	 * @return list of all rooms
	 */
    List<Room> getRoomList();

    /**
	 * 
	 * @return list of distinct names 
	 */
    List<String> getNames();

    /**
	 * 
	 * @param id
	 *            primary key
	 * @return room corresponding to id, or new room if no such room exists
	 */
    Room getRoomById(int id);

    /**
	 * 
	 * @param r
	 * @return true if room exists
	 */
    boolean doesRoomExist(Room r);

    /**
	 * 
	 * @param r
	 *            room object to save or update
	 */
    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    void saveOrUpdateRoom(Room r);

    /**
	 * Checks whether a given Room object's contents match the database record
	 * 
	 * @param r
	 * @return
	 */
    boolean isRoomCurrent(Room r);
}
