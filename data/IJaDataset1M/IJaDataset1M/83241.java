package alice.c4jadex.roomsworld.navigator;

import java.util.ArrayList;
import alice.cartago.examples.roomsworld.location;

/**
 *    capability to navigate rooms
 * @author Michele Piunti
 */
public interface IRoomsWorldNavigator {

    public ArrayList<location> getPath(int fromRoom, int toRoom);

    public ArrayList<location> getChPath(int fromRoom, int toRoom);

    public location goToChList(int myRoom);

    public int getRoom(location loc);
}
