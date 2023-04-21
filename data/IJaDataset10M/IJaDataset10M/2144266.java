package org.jcyclone.ext.atls;

import org.jcyclone.core.queue.IElement;
import org.jcyclone.ext.atls.protocol.ATLSCipherSpecRecord;

/**
 * An aTLSChangeCipherSpecPacket is passed to the handshake stage from the
 * record stage to indicate that a change cipher spec handshake message was
 * just received. This type of packet is necessary to differentiate
 * from a normal handshake packet (aTLSHandshakePacket) because a
 * change cipher spec message must be dealt with differently.
 */
class ATLSCipherSpecPacket implements IElement {

    private ATLSConnection atlsconn;

    private ATLSCipherSpecRecord record;

    ATLSCipherSpecPacket(ATLSConnection atlsconn, ATLSCipherSpecRecord record) {
        this.atlsconn = atlsconn;
        this.record = record;
    }

    /**
	 * Returns the aTLSConnection that this packet was received on.
	 */
    ATLSConnection getConnection() {
        return atlsconn;
    }

    /**
	 * Returns the record that was actually received.
	 */
    ATLSCipherSpecRecord getRecord() {
        return record;
    }
}
