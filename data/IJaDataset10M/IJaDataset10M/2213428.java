package jssh;

/**
 * Sent by the server in response to a SSH_CMSG_AUTH_RSA message from
 * the client. This message contains 32 8-bit random bytes, encrypted
 * with the client's public key.
 */
public class SMSG_AUTH_RSA_CHALLENGE extends Packet {

    /** Use this constructor when receiving a SMSG_AUTH_RSA_CHALLENGE packet
     * on the network.
     */
    SMSG_AUTH_RSA_CHALLENGE(byte[] data_) {
        super(data_);
    }

    public byte[] getChallenge() {
        int offset = 1;
        return SSHInputStream.getMpInt(offset, super._data);
    }
}
