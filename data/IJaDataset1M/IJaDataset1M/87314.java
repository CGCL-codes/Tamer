package org.stanwood.media.search;

import org.stanwood.media.MediaDirectory;
import org.stanwood.media.model.IVideo;

public class MediaSearchResult {

    private MediaDirectory mediaDirectory;

    private IVideo video;

    public MediaSearchResult(MediaDirectory mediaDirectory, IVideo video) {
        super();
        this.mediaDirectory = mediaDirectory;
        this.video = video;
    }

    public MediaDirectory getMediaDirectory() {
        return mediaDirectory;
    }

    public IVideo getVideo() {
        return video;
    }
}
