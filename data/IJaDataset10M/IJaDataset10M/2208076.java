package com.thimbleware.jmemcached;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

public class MemcachedProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    public MemcachedProtocolCodecFactory() {
        super.register(CommandDecoder.class);
        super.register(ResponseEncoder.class);
    }
}
