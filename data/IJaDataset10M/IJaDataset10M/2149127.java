package net.sourceforge.x360mediaserve.api.database.items.media.resources;

import net.sourceforge.x360mediaserve.api.database.items.media.formats.ImageInformation;

/** Interface designed to hold information about a specific image container
 * @author tom
 *
 */
public interface ImageResource extends MediaResource {

    public ImageInformation getImageInformation();

    public Integer getWidth();

    public Integer getHeight();
}
