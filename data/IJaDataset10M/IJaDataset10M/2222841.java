package org.webdocwf.util.smime.cms;

import org.webdocwf.util.smime.exception.SMIMEException;
import org.webdocwf.util.smime.der.DERSetPr;
import java.security.cert.X509Certificate;

/**
 * RecipientInfos class is DER encoded container, represented in ASN.1 notation
 * according to RFC2630, used for storing individual information needed for all
 * recipients of encrypted message. This information, beside other information,
 * contain specifically encrypted symmetric key for all particular recipients.<BR>
 * <BR>
 * RecipientInfos ::= SET OF RecipientInfo<BR>
 * <BR>
 * <DL>
 * RecipientInfo ::= CHOICE { <BR>
 * <DD>       ktri KeyTransRecipientInfo,<BR>
 * <DD>       kari [1] KeyAgreeRecipientInfo,<BR>
 * <DD>       kekri [2] KEKRecipientInfo }<BR>
 * <BR>
 * </DL>
 * <DL>
 * KeyTransRecipientInfo ::= SEQUENCE { <BR>
 * <DD>       version CMSVersion,  -- always set to 0 or 2 <BR>
 * <DD>       rid RecipientIdentifier, <BR>
 * <DD>       keyEncryptionAlgorithm KeyEncryptionAlgorithmIdentifier, <BR>
 * <DD>       encryptedKey EncryptedKey }<BR>
 * </DL>
 */
public class RecipientInfos extends DERSetPr {

    /**
     * Container for symetric key initialized by constructor
     */
    private byte[] symmetricKey;

    /**
     * Input information for all recipients is the same: symmetric key. This
     * constructor is advisable to use.
     * @param symKey0 symmetric key represented as byte array
     * @exception SMIMEException thrown by super class constructor.
     */
    public RecipientInfos(byte[] symKey0) throws SMIMEException {
        symmetricKey = symKey0;
    }

    /**
     * Adds Recipient via Key Transport Recipient Infos. X509 certificate is used
     * to obtain all information needed for construction and organise information
     * about particular recipient of encrypted message.
     * @param cert0 X509 certificate of partial recipient
     * @exception SMIMEException thrown in super class addContent method.
     */
    public void addRecipient(X509Certificate cert0) throws SMIMEException {
        KeyTransRecipientInfo keyTrans = new KeyTransRecipientInfo(symmetricKey);
        keyTrans.addRecipient(cert0);
        super.addContent(keyTrans.getDEREncoded());
    }
}
