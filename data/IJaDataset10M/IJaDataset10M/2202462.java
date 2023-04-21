package mudcartographer;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * MVC Controller that handles communication between the GUI, Map and Room objects
 */
public class MudController {

    private static MudController mudController;

    private static Set<Room.RoomProperty> REDRAW_PROPERTIES = new HashSet<Room.RoomProperty>(Arrays.asList(Room.RoomProperty.BACKGROUND_COLOR, Room.RoomProperty.TEXT_COLOR, Room.RoomProperty.SYMBOL, Room.RoomProperty.PAINT));

    private Set<RoomEventListener> listeners;

    private MudMap currentMap;

    private MapPainter painter;

    /**
     * Don't allow users to create new MudControllers
     */
    private MudController() {
        listeners = new HashSet<RoomEventListener>();
        currentMap = new MudMap();
        currentMap.setInitialRoom(new Room(' '));
    }

    /**
     * Only allow one MudController
     *
     * @return the only MudController
     */
    public static MudController getMudController() {
        if (mudController == null) {
            mudController = new MudController();
        }
        return mudController;
    }

    public void addListener(RoomEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RoomEventListener listener) {
        listeners.remove(listener);
    }

    public void setPainter(MapPainter painter) {
        this.painter = painter;
    }

    public MudMap getCurrentMap() {
        return currentMap;
    }

    /**
     * Allow classes to cause events through the controller
     * @param roomEvent the event to be caused
     */
    public void fireRoomEvent(RoomEvent roomEvent) {
        Set<Room.RoomProperty> matchingProperties;
        for (RoomEventListener listener : listeners) {
            matchingProperties = new HashSet<Room.RoomProperty>(roomEvent.getProperties());
            matchingProperties.retainAll(listener.getProperties());
            if (!listener.equals(roomEvent.getSource()) && !matchingProperties.isEmpty()) {
                listener.updateRoom(roomEvent.getRoom());
            }
        }
        matchingProperties = new HashSet<Room.RoomProperty>(roomEvent.getProperties());
        matchingProperties.retainAll(REDRAW_PROPERTIES);
        if (!matchingProperties.isEmpty()) {
            currentMap.paint();
            painter.setMaps(Arrays.asList(currentMap));
            painter.repaint();
            painter.revalidate();
            painter.requestFocus();
        }
    }
}
