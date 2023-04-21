package net.sourceforge.epoint.pgp;

import java.io.InputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

/**
 * OpenPGP keyring without uids, sigs and subs
 *
 * @author <a href="nagydani@sourceforge.net">Daniel A. Nagy</a>
 */
public class KeyRing implements java.util.Iterator {

    private InputStream is;

    private KEYPacket next;

    private KEYPacket last;

    public Object next() {
        try {
            return nextKEYPacket().getPublicKey();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasNext() {
        return next != null;
    }

    public KEYPacket nextKEYPacket() {
        last = next;
        try {
            Packet p = new Packet(is, false);
            while (p.getTag() != Packet.PUBKEY && p.getTag() != Packet.SECKEY) {
                if (p.getTag() == Packet.SIGNATURE) {
                    SIGNATUREPacket sig = new SIGNATUREPacket(p);
                    PublicKey pk;
                    if (sig.getType() == SIGNATUREPacket.KEYREVOCATION && sig.match(pk = last.getPublicKey())) {
                        Signature s = Signature.getInstance(sig.getAlgorithm());
                        s.initVerify(pk);
                        s.update(last.toByteArrayPUBKEY());
                        if (sig.verify(s)) {
                            p = new Packet(is, false);
                            while (p.getTag() != Packet.PUBKEY && p.getTag() != Packet.SECKEY) {
                                is.skip(p.getTail());
                                p = new Packet(is, false);
                            }
                            last = new KEYPacket(p);
                        }
                    }
                } else is.skip(p.getTail());
                p = new Packet(is, false);
            }
            next = new KEYPacket(p);
        } catch (Exception e) {
            next = null;
        }
        return last;
    }

    public KEYPacket getKEYPacket() {
        return last;
    }

    public void remove() {
    }

    /**
     * Load key ring
     * @param source
     */
    public KeyRing(InputStream source) throws IOException, NoSuchAlgorithmException {
        is = source;
        next = new KEYPacket(is);
    }
}
