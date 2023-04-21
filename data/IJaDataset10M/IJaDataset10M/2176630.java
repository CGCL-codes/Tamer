package ca.uwaterloo.crysp.otr;

import ca.uwaterloo.crysp.otr.crypt.DSAKeyPairGenerator;
import ca.uwaterloo.crysp.otr.crypt.Provider;
import ca.uwaterloo.crysp.otr.crypt.KeyPair;
import ca.uwaterloo.crysp.otr.crypt.SHA1;

/**
 * A PrivKey stores the private and public long-term authentication keys for an
 * individual account. Note that encryption keys are not stored anywhere, in
 * order to achieve forward secrecy.
 * 
 * @author Ian Goldberg <iang@cs.uwaterloo.ca>
 */
public class PrivKey {

    /** What type of key is this? */
    private int pubkey_type;

    /** A DSA key */
    public static final int TYPE_DSA = 0;

    /** The serialized public key */
    public byte[] pubkey_data;

    /** The keypair */
    private KeyPair kp;

    /** Create a new PrivKey for the given Account. */
    public PrivKey(Account acc, Provider crypt) throws OTRException {
        pubkey_type = TYPE_DSA;
        DSAKeyPairGenerator gen = crypt.getDSAKeyPairGenerator();
        kp = gen.generateKeyPair();
        pubkey_data = kp.getPublicKey().serialize();
    }

    public PrivKey(KeyPair kp) {
        this.kp = kp;
        pubkey_data = kp.getPublicKey().serialize();
    }

    public int pubkeySize() {
        return pubkey_data.length;
    }

    public int pubkeyType() {
        return pubkey_type;
    }

    public KeyPair getKeyPair() {
        return kp;
    }

    public static byte[] fingerprintRaw(UserState us, String accountname, String protocol, Provider prov) throws OTRException {
        PrivKey p = us.getPrivKey(new Account(accountname, protocol), false);
        if (p != null) {
            SHA1 sha = prov.getSHA1();
            byte[] hash = sha.hash(p.pubkey_data);
            return hash;
        } else {
            return null;
        }
    }
}
