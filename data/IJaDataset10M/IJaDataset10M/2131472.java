package zuul.core;

import java.awt.Window;
import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import zuul.game.Room;

/**
 * Die Klasse wird dazu benutzt zu entscheiden ob Raeume auf dem Bildschirm sind
 * und gezeichnet werden muessen oder nicht. Quasi ein RoomObserver.
 * 
 * @author swe_0802
 */
public class RoomDrawManager {

    private List<Room> visibleRooms;

    private Window window;

    Point mapPosition;

    Point cTopLeft, cTopRight, cDownRight, cDownLeft;

    int i;

    /**
     * Konstruktor des RoomDrawManagers
     * 
     * @param Window window
     */
    public RoomDrawManager(Window window) {
        this.window = window;
        setVisibleRooms(Collections.synchronizedList(new LinkedList<Room>()));
    }

    /**
     * Update-Methode des RoomDrawManagers
     */
    public void update() {
        int size = getVisibleRooms().size();
        synchronized (getVisibleRooms()) {
            for (i = 0; i < size; i++) {
                Room room = getVisibleRooms().get(i);
                if (room != null && isOnScreen(room)) {
                    areAdjRoomVisible(room);
                }
                size = getVisibleRooms().size();
            }
        }
    }

    /**
     * Ueberprueft ob das uebergebene Raum auf dem Bildschirm sichtbar ist.
     * 
     * @param room
     *            Der zu ueberpruefende Raum
     * @return boolean Raum sichtbar ?
     */
    private boolean isOnScreen(Room room) {
        calcWBorders();
        mapPosition = room.getMapPosition();
        int critXLeft = cTopLeft.x - room.getMapSize().width;
        int critXRight = cTopRight.x;
        int critYTop = cTopLeft.y - room.getMapSize().height;
        int critYDown = cDownLeft.y + room.getMapSize().height;
        int x = mapPosition.x;
        int y = mapPosition.y;
        if (x < critXLeft || x > critXRight || y > critYDown || y < critYTop) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Berechnet die Grenzen des Fensters
     */
    private void calcWBorders() {
        int w = window.getWidth();
        int h = window.getHeight();
        cTopLeft = new Point(0, 0);
        cTopRight = new Point(w, 0);
        cDownRight = new Point(w, h);
        cDownLeft = new Point(0, h);
    }

    /**
     * ueberprueft ob Nebenraeume sichtbar sind
     * 
     * @param Room room
     */
    private void areAdjRoomVisible(Room room) {
        mapPosition = room.getMapPosition();
        int x = mapPosition.x;
        int y = mapPosition.y;
        Room east = room.getEastRoom();
        Room west = room.getWestRoom();
        Room north = room.getNorthRoom();
        Room south = room.getSouthRoom();
        synchronized (getVisibleRooms()) {
            if (east != null) {
                east.setMapPosition(x + room.getMapSize().width, y);
                if (isOnScreen(east)) {
                    if (!getVisibleRooms().contains(east)) {
                        getVisibleRooms().add(east);
                        i = 0;
                    }
                }
            }
            if (west != null) {
                west.setMapPosition(x - room.getMapSize().width, y);
                if (isOnScreen(west)) {
                    if (!getVisibleRooms().contains(west)) {
                        getVisibleRooms().add(west);
                        i = 0;
                    }
                }
            }
            if (north != null) {
                north.setMapPosition(y - room.getMapSize().height, x);
                if (isOnScreen(north)) {
                    if (!getVisibleRooms().contains(north)) {
                        getVisibleRooms().add(north);
                        i = 0;
                    }
                }
            }
            if (south != null) {
                south.setMapPosition(y + room.getMapSize().height, x);
                if (isOnScreen(south)) {
                    if (!getVisibleRooms().contains(south)) {
                        getVisibleRooms().add(south);
                        i = 0;
                    }
                }
            }
        }
    }

    /**
     * Fuegt ein Raum zu der Raumliste hinzu
     * 
     * @param room
     */
    public void addRoom(Room room) {
        synchronized (getVisibleRooms()) {
            getVisibleRooms().add(room);
        }
    }

    /**
     * Setzt sichtbare Raeume
     * 
     * @param visibleRooms
     *            the visibleRooms to set
     */
    public void setVisibleRooms(List<Room> visibleRooms) {
        this.visibleRooms = visibleRooms;
    }

    /**
     * Get-Methode der sichtbaren Raeume
     * 
     * @return the visibleRooms
     */
    public List<Room> getVisibleRooms() {
        return visibleRooms;
    }
}
