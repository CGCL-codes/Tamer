package lanlab;

public class EncryptedPacket extends Packet {

    /**
	 * simply upcalls Packet's constructor
	 * @param n the node, that created the packet
	 * @param id the node's identifier
	 */
    public EncryptedPacket(Node n, int id) {
        super(n, id);
    }

    /**
	 * Overrides the inherited method to encrypt the message being saved.
	 */
    @Override
    public void setMessage(String msg) {
        super.setMessage(Utils.encrypt(msg));
    }
}
