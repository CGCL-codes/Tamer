package galao.net.peers;

import galao.core.logging.Logging;
import galao.core.util.EncodeHelper;
import galao.crypto.hashing.HashUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class User extends Peer {

    public User(BigInteger id) {
        super(id);
    }

    private byte[] privateKey;

    public byte[] getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public void setKey(KeyPair k) {
        this.setPrivateKey(k.getPrivate().getEncoded());
        this.setPublicKey(k.getPublic().getEncoded());
    }

    public Element toXmlExtended() {
        Element peer = super.toXml();
        Element key = peer.getChild("key");
        Element privatekey = new Element("private");
        privatekey.setText(EncodeHelper.getInstance().encode(this.privateKey));
        key.addContent(privatekey);
        return peer;
    }

    public static User wrap(Element e) throws NumberFormatException, JDOMException, IOException {
        User u = null;
        Peer p = Peer.wrap(e);
        u = new User(p.getId());
        u.setPrivateKey(EncodeHelper.getInstance().decode(e.getChild("key").getChildText("private")));
        u.setPublicKey(p.getPublicKey());
        return u;
    }

    public static User createRandom() {
        BigInteger id = HashUtil.getInstance().randomHash();
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            Logging.exception("[User.createRandom] Could not find RSA algo! ", e);
            System.exit(1);
        }
        KeyPair keys = kpg.generateKeyPair();
        User u = new User(id);
        u.setPrivateKey(keys.getPrivate().getEncoded());
        u.setPublicKey(keys.getPublic().getEncoded());
        return u;
    }

    public void persist(File f) throws IOException {
        f.createNewFile();
        Element root = this.toXmlExtended();
        new XMLOutputter(Format.getPrettyFormat()).output(root, new FileOutputStream(f));
    }
}
