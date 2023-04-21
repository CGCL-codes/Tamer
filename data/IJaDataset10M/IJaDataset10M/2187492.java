package Netzwerk;

public class Fileserver extends ServerNode {

    public Fileserver(String name) {
        super(name);
    }

    void specialProcedure(Packet packet) {
        packet.store();
    }
}
