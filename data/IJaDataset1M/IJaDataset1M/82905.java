package ubermunchkin.server;

/**
* Communique - encapsulates a protocol string placed in the incomming queue.
*/
class Communique {

    private short idNumber;

    private String protocol;

    public Communique(short idNumber, String protocol) {
        this.idNumber = idNumber;
        this.protocol = protocol;
    }

    public short getId() {
        return idNumber;
    }

    public String getProtocol() {
        return protocol;
    }
}
