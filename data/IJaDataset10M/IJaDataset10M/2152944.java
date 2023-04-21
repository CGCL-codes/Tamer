package net.sf.jnclib.tp.ssh2;

import net.sf.jnclib.tp.ssh2.crai.CraiDigest;

class KexGex256 extends KexGex {

    public KexGex256() {
        super();
    }

    public String getName() {
        return "diffie-hellman-group-exchange-sha256";
    }

    public CraiDigest getKDFDigest() {
        return mCrai.makeSHA256();
    }
}
