package corner.util.crypto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 加密算法的key产生器.
 * @author <a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version $Revision:3677 $
 */
public class CryptoKeyGenerator {

    /**
	 * 产生加密的key.
	 * @param algorithm 算法名称.
	 * @param fileName 加密产生文件的名称.
	 */
    public static void generateKey(String algorithm, String fileName) {
        SecretKey key = null;
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
            keygen.init(56);
            key = keygen.generateKey();
            ObjectOutputStream keyFile = new ObjectOutputStream(new FileOutputStream(fileName));
            keyFile.writeObject(key);
            keyFile.close();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
