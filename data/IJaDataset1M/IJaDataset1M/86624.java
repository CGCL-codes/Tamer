package code.messy;

import java.util.ArrayList;

public class Server implements Service {

    private static Server instance = new Server();

    private Server() {
    }

    public static Server getInstance() {
        return instance;
    }

    private static ArrayList<Service> services = new ArrayList<Service>();

    public static void add(Service service) {
        services.add(service);
    }

    public void clear() {
        services.clear();
    }

    @Override
    public void init() throws Exception {
        for (Service service : services) {
            service.init();
        }
    }

    @Override
    public void shutdown() throws Exception {
        for (Service service : services) {
            service.shutdown();
        }
    }

    @Override
    public void start() throws Exception {
        for (Service service : services) {
            service.start();
        }
    }

    @Override
    public void stop() throws Exception {
        for (Service service : services) {
            service.stop();
        }
    }
}
