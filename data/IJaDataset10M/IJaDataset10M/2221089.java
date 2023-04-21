package org.tcpfile.net.packets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcpfile.main.Misc;
import org.tcpfile.net.ByteArray;
import org.tcpfile.net.Connection;

/**
 * The ChallengeAnswerPacket is used when authentificating the other person.
 * The other person has to be able to decrypt the byte[], that was encrypted with 
 * the RSA Authentification Key. If he is able to do so, he must be the one having
 * the appropriate Private Key.
 * @author Stivo
 *
 */
public class ChallengeAnswerPacket extends Packet {

    private static Logger log = LoggerFactory.getLogger(ChallengeAnswerPacket.class);

    private static final long serialVersionUID = 1L;

    byte[] challenge;

    public void handle(Connection c) {
        log.trace("Entering");
        challenge = Misc.authKey.decrypt(challenge);
        if (ByteArray.equal(challenge, c.challenge)) {
            log.debug("Verification successful");
            c.setReceiveStatus(Connection.RECEIVE_PROTOCOLINITIALIZE);
            c.putNextByteArrayOnCryptServer();
        } else {
            c.hangup();
            log.warn("Verification failed");
        }
    }

    public ChallengeAnswerPacket(Connection c, byte[] b) {
        super();
        challenge = Misc.authKey.decrypt(b);
        challenge = c.contact.getPublickey().encrypt(challenge);
    }
}
