package jmemento.api.domain.album;

import java.util.List;
import jmemento.api.domain.photo.IPhoto;

/**
 * @author rusty
 * 
 */
public interface IReadonlyPhotoCollection {

    /**
     * @return the photos
     */
    List<IPhoto> getPhotos();

    /**
     * @param index
     * @return
     */
    IPhoto getPhoto(final int index);

    /**
     * get the size of the album.
     * 
     * @return the size of the album.
     */
    int size();

    /**
     * @return the name
     */
    String getDisplayName();

    /**
     * @return the id
     */
    String getDisplayId();
}
