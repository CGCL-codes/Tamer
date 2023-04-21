package com.taobao.api.internal.stream;

/**
 * 
 * @author zhenzi
 * 2011-8-8 下午05:53:00
 */
public class TopCometStreamFactory {

    private Configuration configruation;

    public TopCometStreamFactory(Configuration configuration) {
        if (configuration == null) {
            throw new RuntimeException("configuration is must not null!");
        }
        this.configruation = configuration;
    }

    public TopCometStream getInstance() {
        return new TopCometStreamImpl(configruation);
    }
}
