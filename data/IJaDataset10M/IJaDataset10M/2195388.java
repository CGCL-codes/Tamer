package net.sourceforge.x360mediaserve.api.formats.playback.audio;

import net.sourceforge.x360mediaserve.api.formats.playback.BasePlaybackRequirements;

public interface BaseAudioPlaybackRequirements extends BasePlaybackRequirements {

    public Integer getMaxBitrate();

    public Integer getMaxChannels();
}
