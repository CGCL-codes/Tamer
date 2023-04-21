package com.unitt.servicemanager.service;

import com.unitt.servicemanager.websocket.MessageResponse;
import com.unitt.servicemanager.websocket.MessageRoutingInfo;
import com.unitt.servicemanager.websocket.SerializedMessageBody;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MockServiceDelegate extends ServiceDelegate {

    public static final int SERVICE_ARG_COUNT = 101;

    public static final String SERVICE_ARG_VALUE = "TestValue";

    protected BlockingQueue<MessageRoutingInfo> requestQueue;

    protected BlockingQueue<MessageResponse> destQueue;

    protected ConcurrentMap<String, SerializedMessageBody> bodyMap;

    public MockServiceDelegate(Object aService) {
        super(aService, 10000, null, 1);
        requestQueue = new ArrayBlockingQueue<MessageRoutingInfo>(10);
        destQueue = new ArrayBlockingQueue<MessageResponse>(10);
        bodyMap = new ConcurrentHashMap<String, SerializedMessageBody>();
    }

    @Override
    public Object[] getArguments(MessageResponse aResponse, Method aMethod) {
        return new Object[] { SERVICE_ARG_COUNT, SERVICE_ARG_VALUE };
    }

    @Override
    public ConcurrentMap<String, SerializedMessageBody> getBodyMap(MessageRoutingInfo aInfo) {
        return bodyMap;
    }

    @Override
    public BlockingQueue<MessageResponse> getDestinationQueue(MessageRoutingInfo aInfo) {
        return destQueue;
    }

    @Override
    public BlockingQueue<MessageRoutingInfo> getRequestQueue() {
        return requestQueue;
    }
}
