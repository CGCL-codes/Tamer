package base;

import network.client.RemoteClientConnection;
import org.apache.log4j.PropertyConfigurator;

public class ClientInitTest {

    public static void main(String[] args) {
        PropertyConfigurator.configure("./data/config/logger/log4j.properties");
        System.out.println("Starting client connection test!");
        RemoteClientConnection RCC = new RemoteClientConnection("localhost", 22600);
        RCC.Connect();
        RCC.start();
        RCC.Login("mirage", "pw");
    }
}
