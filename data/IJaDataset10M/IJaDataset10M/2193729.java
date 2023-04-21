package com.technoetic.dof.transport;

public interface Transport {

    public void sendAsynchronousRequest(Request request, ResponseCallback callback);

    public Response sendSynchronousRequest(Request request) throws Throwable;

    public Object getEndPoint();
}
