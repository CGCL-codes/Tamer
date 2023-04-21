package net.firefly.client.gui.context.events;

import java.util.EventObject;
import net.firefly.client.model.data.list.AlbumList;

public class FilteredAlbumListChangedEvent extends EventObject {

    private static final long serialVersionUID = -4411275266298884152L;

    public FilteredAlbumListChangedEvent(AlbumList newFilteredAlbumList) {
        super(newFilteredAlbumList);
    }

    public AlbumList getNewFilteredAlbumList() {
        return (AlbumList) source;
    }
}
