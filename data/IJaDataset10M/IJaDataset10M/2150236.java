package it.newinstance.spikes.server;

import it.newinstance.spikes.client.services.HelloObject;
import it.newinstance.spikes.client.services.HelloService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class HelloServiceImpl extends RemoteServiceServlet implements HelloService {

    private static final long serialVersionUID = -7370453858500433974L;

    public HelloObject sayHello() {
        return new HelloObject("uberto", "hello");
    }

    @Override
    protected void onAfterResponseSerialized(String serializedResponse) {
        System.out.println(serializedResponse);
    }
}
