package com.wuala.server.common;

import java.nio.ByteBuffer;
import com.wuala.server.common.rmi.mina.IMarshallableFactory;
import com.wuala.server.common.varia.ApplicationRuntimeException;

public abstract class PingServer implements IPingProcessor {

    public static final String DEFAULT_NAME = "NAMELESS";

    /**
     * Human readable name of this server
     */
    private String name;

    public PingServer() {
        this(DEFAULT_NAME);
    }

    protected PingServer(String name) {
        this.name = name;
    }

    public abstract IMarshallableFactory getFactory();

    public IPingProcessor getProcessor() {
        return this;
    }

    public Ping unmarshall(ByteBuffer buffer) {
        throw new ApplicationRuntimeException();
    }

    public abstract int getPort();

    public String getName() {
        return name;
    }
}
