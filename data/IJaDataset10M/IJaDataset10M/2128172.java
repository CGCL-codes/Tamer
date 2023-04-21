package net.sourceforge.x360mediaserve.devices.impl.instances.xml.playback.image;

import java.util.Collection;
import java.util.HashMap;
import net.sourceforge.x360mediaserve.api.formats.playback.images.ImageCodecPlaybackRequirements;
import net.sourceforge.x360mediaserve.api.formats.playback.images.ImageContainerPlaybackRequirements;
import net.sourceforge.x360mediaserve.util.formats.playback.requirements.image.ImageContainerPlaybackRequirementsImpl;

public class BuildableContainerImagePlaybackRequirements extends ImageContainerPlaybackRequirementsImpl implements ImageContainerPlaybackRequirements {

    HashMap<String, ImageCodecPlaybackRequirements> codecSpecs;

    public boolean allowsCodec(String codecName) {
        return codecSpecs.containsKey(codecName);
    }

    public Collection<? extends ImageCodecPlaybackRequirements> getAllowedCodecs() {
        return codecSpecs.values();
    }

    public ImageCodecPlaybackRequirements getCodecDetails(String codecName) {
        return codecSpecs.get(codecName);
    }

    private static String getStr(Object o) {
        if (o != null) return o.toString();
        return "null";
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ContainerImagePlaybackRequirements:");
        buffer.append("Mimetype:");
        buffer.append(getStr(getMimeType()));
        buffer.append("Max height:");
        buffer.append(getStr(getMaxHeight()));
        buffer.append("Max width:");
        buffer.append(getStr(getMaxWidth()));
        return buffer.toString();
    }
}
