package blueprint4j.comm;

import java.util.Vector;

public class VectorPacketListener implements PacketListener {

    private Vector store = new Vector();

    public PacketListener getNewInstance() {
        return new VectorPacketListener();
    }

    public PacketListener get(int pos) {
        return (PacketListener) store.get(pos);
    }

    public void add(PacketListener item) {
        store.add(item);
    }

    public boolean remove(PacketListener item) {
        return store.remove(item);
    }

    public PacketListener remove(int pos) {
        return (PacketListener) store.remove(pos);
    }

    public void removeAllElements() {
        store.removeAllElements();
    }

    public int size() {
        return store.size();
    }

    public void packetArrived(Packet packet) throws Throwable {
        for (int i = 0; i < size(); i++) {
            get(i).packetArrived(packet);
        }
    }
}
