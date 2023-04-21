package security.crypto;

import java.security.InvalidKeyException;
import javax.crypto.SecretKey;
import data.model.Data;

public interface Cipher {

    public Data encrypt(Data data, SecretKey key);

    public Data decrypt(Data data, SecretKey key);
}
