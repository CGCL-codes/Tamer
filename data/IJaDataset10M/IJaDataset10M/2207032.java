package memento.dao.iface;

import java.util.List;
import memento.domain.Album;

public interface IAlbumDao {

    Album getAlbumById(String id);

    List<Album> getAlbumsByUserId(String id);

    void deleteAlbumById(String id);
}
