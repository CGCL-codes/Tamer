package net.sourceforge.birmi.example.hello2;

import net.sourceforge.birmi.server.ServiceServer;
import org.apache.log4j.BasicConfigurator;

public class HelloServer {

    public static final int SERVER_PORT = 8090;

    public static final String SERVICE_NAME = "helloService";

    public HelloServer() {
        ServiceServer server = new ServiceServer(SERVER_PORT);
        server.registerService(SERVICE_NAME, new HelloService());
        server.listen();
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        new HelloServer();
    }
}
