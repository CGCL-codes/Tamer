package org.jimcat.persistence.xstream;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.jimcat.model.Image;
import org.jimcat.persistence.ImageRepository;

/**
 * 
 * Image repository for XStream backend.
 * 
 * 
 * $Id$
 * 
 * @author Christoph
 */
public class XStreamImageRepository implements ImageRepository {

    /**
	 * Load all images from the persistence layer.
	 * 
	 * @return a set of all images
	 */
    public Set<Image> getAll() {
        return new HashSet<Image>(XStreamBackup.getInstance().images);
    }

    /**
	 * Remove a collection of images
	 * 
	 * @param images
	 *            the images to be removed
	 */
    public void remove(Collection<Image> images) {
        Set<Image> library = XStreamBackup.getInstance().images;
        library.removeAll(images);
    }

    /**
	 * Save a collection of images
	 * 
	 * @param images
	 *            the images to be saved
	 */
    public void save(Collection<Image> images) {
        Set<Image> library = XStreamBackup.getInstance().images;
        library.addAll(images);
    }
}
