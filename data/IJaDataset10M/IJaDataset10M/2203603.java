package net.sourceforge.x360mediaserve.util.formats.playback.requirements.image;

import net.sourceforge.x360mediaserve.api.formats.playback.images.ImageCodecPlaybackRequirements;

public class ImageCodecPlaybackRequirementsImpl extends BaseImagePlaybackRequirementsImpl implements ImageCodecPlaybackRequirements {

    String codecName;

    public String getCodecName() {
        return codecName;
    }

    public void setCodecName(String codecName) {
        this.codecName = codecName;
    }
}
