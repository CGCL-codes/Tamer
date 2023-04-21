package org.jscep.message;

import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.jscep.transaction.MessageType;
import org.jscep.transaction.Nonce;
import org.jscep.transaction.TransactionId;

/**
 * This class represents a GetCRL PKIMessage, which wraps an IssuerAndSerialNumber
 * object.
 */
public class GetCRL extends PkiRequest<IssuerAndSerialNumber> {

    public GetCRL(TransactionId transId, Nonce senderNonce, IssuerAndSerialNumber messageData) {
        super(transId, MessageType.GetCRL, senderNonce, messageData);
    }
}
