package net.sourceforge.myvd.protocol.ldap.mina.ldap.codec.search.controls;

import java.nio.ByteBuffer;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.tlv.TLV;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.tlv.UniversalTag;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.ber.tlv.Value;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.AbstractAsn1Object;
import net.sourceforge.myvd.protocol.ldap.mina.asn1.codec.EncoderException;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.name.LdapDN;
import net.sourceforge.myvd.protocol.ldap.mina.ldap.util.StringTools;

/**
 * A response control that may be returned by Persistent Search entry responses.
 * It contains addition change information to descrive the exact change that
 * occured to an entry. The exact details of this control are covered in section
 * 5 of this (yes) expired draft: <a
 * href="http://www3.ietf.org/proceedings/01aug/I-D/draft-ietf-ldapext-psearch-03.txt">
 * Persistent Search Draft v03</a> which is printed out below for convenience:
 * 
 * <pre>
 *    5.  Entry Change Notification Control
 *    
 *    This control provides additional information about the change the caused
 *    a particular entry to be returned as the result of a persistent search.
 *    The controlType is &quot;2.16.840.1.113730.3.4.7&quot;.  If the client set the
 *    returnECs boolean to TRUE in the PersistentSearch control, servers MUST
 *    include an EntryChangeNotification control in the Controls portion of
 *    each SearchResultEntry that is returned due to an entry being added,
 *    deleted, or modified.
 *    
 *               EntryChangeNotification ::= SEQUENCE 
 *               {
 *                         changeType ENUMERATED 
 *                         {
 *                                 add             (1),
 *                                 delete          (2),
 *                                 modify          (4),
 *                                 modDN           (8)
 *                         },
 *                         previousDN   LDAPDN OPTIONAL,     -- modifyDN ops. only
 *                         changeNumber INTEGER OPTIONAL     -- if supported
 *               }
 *    
 *    changeType indicates what LDAP operation caused the entry to be
 *    returned.
 *    
 *    previousDN is present only for modifyDN operations and gives the DN of
 *    the entry before it was renamed and/or moved.  Servers MUST include this
 *    optional field only when returning change notifications as a result of
 *    modifyDN operations.
 * 
 *    changeNumber is the change number [CHANGELOG] assigned by a server for
 *    the change.  If a server supports an LDAP Change Log it SHOULD include
 *    this field.
 * </pre>
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class EntryChangeControl extends AbstractAsn1Object {

    public static final int UNDEFINED_CHANGE_NUMBER = -1;

    private ChangeType changeType = ChangeType.ADD;

    private int changeNumber = UNDEFINED_CHANGE_NUMBER;

    /** The previous DN */
    private LdapDN previousDn = null;

    /** A temporary storage for the previous DN */
    private byte[] previousDnBytes = null;

    /** The entry change global length */
    private int eccSeqLength;

    /**
     * @see Asn1Object#Asn1Object
     */
    public EntryChangeControl() {
        super();
    }

    /**
     * Compute the EntryChangeControl length 
     * 
     * 0x30 L1 
     *   | 
     *   +--> 0x0A 0x0(1-4) [1|2|4|8] (changeType) 
     *  [+--> 0x04 L2 previousDN] 
     *  [+--> 0x02 0x0(1-4) [0..2^31-1] (changeNumber)]
     */
    public int computeLength() {
        int changeTypesLength = 1 + 1 + 1;
        int previousDnLength = 0;
        int changeNumberLength = 0;
        if (previousDn != null) {
            previousDnBytes = StringTools.getBytesUtf8(previousDn.getUpName());
            previousDnLength = 1 + TLV.getNbBytes(previousDnBytes.length) + previousDnBytes.length;
        }
        if (changeNumber != UNDEFINED_CHANGE_NUMBER) {
            changeNumberLength = 1 + 1 + Value.getNbBytes(changeNumber);
        }
        eccSeqLength = changeTypesLength + previousDnLength + changeNumberLength;
        return 1 + TLV.getNbBytes(eccSeqLength) + eccSeqLength;
    }

    /**
     * Encodes the entry change control.
     * 
     * @param buffer The encoded sink
     * @return A ByteBuffer that contains the encoded PDU
     * @throws EncoderException If anything goes wrong.
     */
    public ByteBuffer encode(ByteBuffer buffer) throws EncoderException {
        ByteBuffer bb = ByteBuffer.allocate(computeLength());
        bb.put(UniversalTag.SEQUENCE_TAG);
        bb.put(TLV.getBytes(eccSeqLength));
        bb.put(UniversalTag.ENUMERATED_TAG);
        bb.put((byte) 1);
        bb.put(Value.getBytes(changeType.getValue()));
        if (previousDn != null) {
            Value.encode(bb, previousDnBytes);
        }
        if (changeNumber != UNDEFINED_CHANGE_NUMBER) {
            Value.encode(bb, changeNumber);
        }
        return bb;
    }

    /**
     * Return a String representing this EntryChangeControl.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("    Entry Change Control\n");
        sb.append("        changeType   : '").append(changeType).append("'\n");
        sb.append("        previousDN   : '").append(previousDn).append("'\n");
        if (changeNumber == UNDEFINED_CHANGE_NUMBER) {
            sb.append("        changeNumber : '").append("UNDEFINED").append("'\n");
        } else {
            sb.append("        changeNumber : '").append(changeNumber).append("'\n");
        }
        return sb.toString();
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public LdapDN getPreviousDn() {
        return previousDn;
    }

    public void setPreviousDn(LdapDN previousDn) {
        this.previousDn = previousDn;
    }

    public int getChangeNumber() {
        return changeNumber;
    }

    public void setChangeNumber(int changeNumber) {
        this.changeNumber = changeNumber;
    }
}
