package com.sts.webmeet.content.common.audio;

import com.sts.webmeet.content.common.ConfigInfo;

public class ConfigSource implements com.sts.webmeet.content.common.ConfigSource {

    private static final ConfigInfo[] INFOS = { new ConfigInfo("webhuddle.property.audio.encoder", "com.sts.webmeet.content.common.audio.GSMReferenceEncoder"), new ConfigInfo("webhuddle.property.audio.decoder", "com.sts.webmeet.content.common.audio.GSMReferenceDecoder"), new ConfigInfo("webhuddle.property.audio.encoder.quality", "5"), new ConfigInfo("webhuddle.property.audio.encoder.complexity", "3"), new ConfigInfo("webhuddle.property.audio.encoder.vad", "false"), new ConfigInfo("webhuddle.property.audio.encoder.vbr", "false"), new ConfigInfo("webhuddle.property.audio.encoder.dtx", "false"), new ConfigInfo("webhuddle.property.audio.vox.threshold", "700"), new ConfigInfo("webhuddle.property.audio.mp3.decoder.channel.count", "1"), new ConfigInfo("webhuddle.property.audio.mp3.decoder.sample.rate", "44100") };

    public ConfigInfo[] getConfigInfos() {
        return INFOS;
    }
}
