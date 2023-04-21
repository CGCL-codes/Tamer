package com.peterhi.media.voice.impl;

import com.peterhi.media.voice.Encoder;
import com.peterhi.natives.Pointer;
import com.peterhi.natives.Speex;

public class SpeexEncoderImpl implements Encoder {

    private Pointer struct_pointer;

    public SpeexEncoderImpl() {
        Speex.tryInit();
        struct_pointer = new Pointer(Speex.create(Speex.MODE_ENCODER));
        Speex.addRef(hashCode());
        Speex.setQuality(hashCode(), 10);
    }

    public int encode(byte[] data) {
        if (Speex.isInited()) {
            return Speex.encode(hashCode(), data);
        } else {
            return -1;
        }
    }

    public int hashCode() {
        return struct_pointer.hashCode();
    }
}
