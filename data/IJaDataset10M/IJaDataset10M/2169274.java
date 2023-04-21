package com.dukesoftware.viewlon3.net.request.common;

import java.nio.channels.SocketChannel;
import com.dukesoftware.viewlon3.net.request.template.ComRequest;

public class RequestGetFrontBackSensor extends ComRequest {

    private final String id;

    public RequestGetFrontBackSensor(String name, SocketChannel socketChannel, String id) {
        super(name, socketChannel);
        this.id = id;
    }

    public void execute() {
        send(TYPE_GET_FRONT_BACK_SENSOR + TN + id);
    }
}
