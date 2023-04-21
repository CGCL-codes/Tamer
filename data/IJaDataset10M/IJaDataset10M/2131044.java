package jssh;

/**
 * This message is sent by the client in response to SSH_SMSG_EXITSTATUS.
 * It is the last message to be sent by the client.
 */
class CMSG_EXIT_CONFIRMATION extends Packet {

    CMSG_EXIT_CONFIRMATION() {
        super(SSH_CMSG_EXIT_CONFIRMATION);
    }
}
