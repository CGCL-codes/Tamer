package com.sts.webmeet.content.common.appshare;

import com.sts.webmeet.content.common.ConfigInfo;

public class ConfigSource implements com.sts.webmeet.content.common.ConfigSource {

    private static final ConfigInfo[] INFOS = { new ConfigInfo("webhuddle.property.appshare.kilobits.per.second", "5000.0"), new ConfigInfo("webhuddle.property.appshare.minimum.sleep.milliseconds", "250"), new ConfigInfo("webhuddle.property.appshare.key.frame.period.seconds", "30"), new ConfigInfo("webhuddle.property.appshare.default.compression.type", "jpeg"), new ConfigInfo("webhuddle.property.appshare.default.share.mode", "desktop") };

    public ConfigInfo[] getConfigInfos() {
        return INFOS;
    }
}
