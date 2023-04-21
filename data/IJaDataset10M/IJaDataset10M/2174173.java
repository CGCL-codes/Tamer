package com.semp.gu.codec;

import java.io.IOException;
import com.semp.gu.domain.local.LocalAlbumElement;

public class CodecManager {

    private ICodec imageCodec;

    private ICodec videoCodec;

    private ICodec defaultCodec;

    public void processElement(LocalAlbumElement element) throws IOException {
        switch(element.getType()) {
            case MOVIE:
                videoCodec.processElement(element);
                break;
            case PICTURE:
                imageCodec.processElement(element);
                break;
            case UNKNOWN:
                defaultCodec.processElement(element);
                break;
        }
    }

    public ICodec getImageCodec() {
        return imageCodec;
    }

    public void setImageCodec(ICodec imageCodec) {
        this.imageCodec = imageCodec;
    }

    public ICodec getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(ICodec videoCodec) {
        this.videoCodec = videoCodec;
    }

    public ICodec getDefaultCodec() {
        return defaultCodec;
    }

    public void setDefaultCodec(ICodec defaultCodec) {
        this.defaultCodec = defaultCodec;
    }
}
