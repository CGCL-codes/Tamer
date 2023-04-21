package j_waste.network;

/**
 * This class represents an entry in the PriorityQueue used for incoming packets.
 * @author Mattias Ek (matekd@users.sourceforge.net)
 */
public class QueueItem {

    private Connection connection;

    private HeadedPacket packet;

    public QueueItem(Connection connection, HeadedPacket packet) {
        this.connection = connection;
        this.packet = packet;
    }

    public Connection getConnection() {
        return connection;
    }

    public HeadedPacket getPacket() {
        return packet;
    }
}
