package net.sourceforge.x360mediaserve.util.formats.playback;

import net.sourceforge.x360mediaserve.api.database.items.media.formats.ImageInformation;
import net.sourceforge.x360mediaserve.api.formats.playback.images.ImagePlaybackInformation;

public class ImagePlaybackInfoImpl extends PlaybackInformationImpl implements ImagePlaybackInformation {

    ImageInformation imageInformation;

    public ImagePlaybackInfoImpl() {
        super();
    }

    public ImagePlaybackInfoImpl(ImagePlaybackInformation audioPlaybackInfo) {
        super(audioPlaybackInfo);
        imageInformation = audioPlaybackInfo.getImageInformation();
    }

    public ImageInformation getImageInformation() {
        return imageInformation;
    }
}
