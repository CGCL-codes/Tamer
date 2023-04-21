package net.sourceforge.x360mediaserve.api.formats.playback.images;

import net.sourceforge.x360mediaserve.api.database.items.media.formats.ImageInformation;
import net.sourceforge.x360mediaserve.api.formats.playback.PlaybackInformation;

public interface ImagePlaybackInformation extends PlaybackInformation {

    public ImageInformation getImageInformation();
}
