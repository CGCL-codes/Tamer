package com.wuala.loader2.copied.client.messages;

import java.nio.ByteBuffer;

public abstract class Loader2Ping extends Ping {

    public Loader2Ping() {
    }

    public Loader2Ping(ByteBuffer buffer) {
        super(buffer);
    }

    public void toBytes(ByteBuffer buffer) {
        super.toBytes(buffer);
    }
}
