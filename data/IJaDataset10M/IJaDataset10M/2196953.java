package org.photovault.imginfo;

import java.util.List;
import java.util.UUID;
import org.photovault.persistence.GenericDAO;

/**
 *
 * @author harri
 */
public interface PhotoInfoDAO extends GenericDAO<PhotoInfo, UUID> {

    /**
     Find photo with given UUID
     @param uuid UUID to look for
     @return The photo with given UUID or <code>null</code> if not found
     */
    PhotoInfo findByUUID(UUID uuid);

    /**
     Find photos whose original instance file has a given MD5 hash
     @deprecated Use {@link ImageFileDAO} instead
     @param hash The hash code to search for
     @param List of matching photos
     */
    List findPhotosWithOriginalHash(byte[] hash);

    /**
     Find photos that have any instance matching a given hash code
     @param hash The hash code to search for
     @param List of matching photos     
     */
    List findPhotosWithHash(byte[] hash);

    /**
     Creates a new persistent photo with random UUID
     @return
     */
    PhotoInfo create();
}
