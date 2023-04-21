package foo.bar.forum.security.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * An implementation of {@link foo.bar.forum.security.encryption.HashedEncryptor} that
 * uses "MD2" encryption algorithm.
 *
 * @author tmjee
 * @version $Date$ $Id$
 */
public class Md2Encryptor extends HashedEncryptor {

    protected MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("MD2");
    }
}
