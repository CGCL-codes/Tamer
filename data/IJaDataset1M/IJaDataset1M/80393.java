package com.caimao.netflow.udp.reader;

import com.caimao.netflow.util.SystemComponent;

/**
 * Sampling is done after rubbish has been cleared out
 * 
 * @author sicai
 * 
 */
public class ProtocolHandler_PacketBufferStorage extends SystemComponent {

    boolean DEBUG = false;

    IPandByteArrayBuffer pbuffer = IPandByteArrayBuffer.getProtocolHandleBuffer();

    int sample = 1;

    int currentSampleCnt = 1;

    @Override
    public void cleanup() {
    }

    public int getSample() {
        return sample;
    }

    public void setSample(int sample) {
        this.sample = sample;
    }

    @Override
    public void handle(Object data) {
        IPandByteArray vo = (IPandByteArray) data;
        if (currentSampleCnt++ % sample == 0) {
            super.handle(data);
        } else {
            pbuffer.setVo(vo);
        }
    }

    @Override
    public void initialize() {
    }
}
