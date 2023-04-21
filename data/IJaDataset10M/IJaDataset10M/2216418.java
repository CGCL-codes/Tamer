package org.openremote.controller.protocol.knx.ip.message;

import java.io.IOException;
import java.io.InputStream;

public class IpDiscoverResp extends IpMessage {

    public static final int STI = 0x202;

    private Hpai controlEndpoint;

    public IpDiscoverResp(InputStream is, int vl) throws IOException {
        super(STI, vl);
        this.controlEndpoint = new Hpai(is);
        int l = is.read();
        is.skip(l - 1);
        l = is.read();
        is.skip(l - 1);
    }

    @Override
    public Primitive getPrimitive() {
        return Primitive.RESP;
    }

    public Hpai getControlEndpoint() {
        return this.controlEndpoint;
    }
}
