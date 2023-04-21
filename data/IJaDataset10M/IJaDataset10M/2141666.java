package protocol.handlers;

import java.util.HashSet;
import java.util.Set;

public class PacketDispatcherFactory {

    private static PacketDispatcherFactory instance = new PacketDispatcherFactory();

    public static PacketDispatcherFactory getInstance() {
        return instance;
    }

    public static void registerHandler(Class<? extends PacketHandler> p) {
        getInstance().classList.add(p);
    }

    private PacketDispatcherFactory() {
    }

    public PacketDispatcher newPacketDispatcher() {
        PacketDispatcher pd = new PacketDispatcher();
        for (Class<? extends PacketHandler> c : classList) {
            try {
                pd.addHandler(c.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Handler creation error.");
            }
        }
        return pd;
    }

    private Set<Class<? extends PacketHandler>> classList = new HashSet<Class<? extends PacketHandler>>();
}
