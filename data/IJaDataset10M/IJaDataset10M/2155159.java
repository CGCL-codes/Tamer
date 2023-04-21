package org.freelords.network.server.room;

import java.util.List;
import java.util.UUID;

/**
 * Java interface representing a ROOM where clients interact.	
 */
public interface Room {

    public void addClient(UUID id);

    public List<UUID> getListOfClients();

    public void removeClient(UUID id);

    public boolean findClient(UUID id);

    public List<Room> getConnectedRooms();

    public String getName();
}
