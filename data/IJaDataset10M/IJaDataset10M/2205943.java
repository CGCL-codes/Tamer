package lv.odylab.evemanage.security;

import com.google.inject.Provider;
import lv.odylab.appengine.repackaged.Base64;
import lv.odylab.evemanage.application.exception.EveManageSecurityException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class PublicKeyProvider implements Provider<PublicKey> {

    private final PublicKey publicKey;

    public PublicKeyProvider() {
        try {
            publicKey = readPublicKeyFromFile("/public.key");
        } catch (IOException e) {
            throw new EveManageSecurityException(e);
        }
    }

    @Override
    public PublicKey get() {
        return publicKey;
    }

    private PublicKey readPublicKeyFromFile(String fileName) throws IOException {
        InputStream base64InputStream = new Base64.InputStream(getClass().getResourceAsStream(fileName));
        byte[] data = new byte[base64InputStream.available()];
        base64InputStream.read(data);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(data);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new EveManageSecurityException(e);
        } catch (InvalidKeySpecException e) {
            throw new EveManageSecurityException(e);
        }
    }
}
