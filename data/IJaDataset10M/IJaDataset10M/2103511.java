package it;

import it.beans.PreferenceBean;
import it.handler.HttpHandlerImpl;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class Other2 {

    public void bo(PreferenceBean pref) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new HttpHandlerImpl(pref));
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
