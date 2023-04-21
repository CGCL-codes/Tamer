package net.sourceforge.jpcap.net;

import java.io.Serializable;

public class SignalLayerHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    protected byte[] _bytes = null;

    public SignalLayerHeader(byte[] bytes) {
        _bytes = bytes;
    }
}
