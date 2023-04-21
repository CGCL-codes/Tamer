package com.wuala.loader2.crypto;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import javax.crypto.BadPaddingException;
import com.wuala.loader2.copied.Util;

public class AsymmetricPublicKey {

    public static final String PUBLIC_FILENAME = "PublicKey.dat";

    private static AsymmetricPublicKey publicKey;

    private static final byte VERSION = 1;

    private static final byte INITIAL_STAMP = 1;

    private PublicKey key;

    private byte[] keyData;

    private byte versionStamp;

    private byte internalKeys;

    public AsymmetricPublicKey(PublicKey key) {
        this(key, INITIAL_STAMP);
    }

    public AsymmetricPublicKey(PublicKey key, byte versionStamp) {
        this.key = key;
        this.versionStamp = versionStamp;
        this.internalKeys = 1;
    }

    public AsymmetricPublicKey(ByteBuffer buffer) {
        byte v = buffer.get();
        assert v == VERSION;
        this.versionStamp = buffer.get();
        this.internalKeys = buffer.get();
        int len = buffer.getShort();
        this.keyData = new byte[len];
        buffer.get(keyData);
    }

    public byte getCurrentVersion() {
        return versionStamp;
    }

    private PublicKey getKey() {
        if (this.key == null) {
            this.key = AsymmetricCrypto.getInstance().loadPublicKey(keyData);
        }
        return this.key;
    }

    public boolean check(byte[] letter, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature dsa = Signature.getInstance("SHA256withRSA");
        dsa.initVerify(getKey());
        dsa.update(letter);
        boolean verifies = dsa.verify(signature);
        return verifies;
    }

    public ByteBuffer decrypt(ByteBuffer source) throws BadPaddingException {
        AsymmetricCrypto crypto = AsymmetricCrypto.getInstance();
        return crypto.decrypt(source, getKey());
    }

    public ByteBuffer encrypt(ByteBuffer source) {
        AsymmetricCrypto encrypter = AsymmetricCrypto.getInstance();
        return encrypter.encrypt(source, getKey());
    }

    public void toBytes(ByteBuffer buffer) {
        byte[] encoded = keyData == null ? AsymmetricCrypto.getInstance().serializeKey(key) : keyData;
        internalToBytes(buffer, encoded);
    }

    public byte[] getBytes() {
        byte[] encoded = keyData == null ? AsymmetricCrypto.getInstance().serializeKey(key) : keyData;
        ByteBuffer buffer = ByteBuffer.allocate(encoded.length + 5);
        internalToBytes(buffer, encoded);
        return buffer.array();
    }

    private void internalToBytes(ByteBuffer buffer, byte[] encoded) {
        buffer.put(VERSION);
        buffer.put(versionStamp);
        buffer.put(internalKeys);
        buffer.putShort((short) encoded.length);
        buffer.put(encoded);
    }

    public static AsymmetricPublicKey createFromData(ByteBuffer buffer) {
        return new AsymmetricPublicKey(buffer);
    }

    public static AsymmetricPublicKey getServerPublicKey() {
        if (publicKey == null) {
            String hexString = "010101012630820122300D06092A864886F70D01010105000382010F003082010A02820101008A103D56A7106F16F98139D93207AE124F2E3670D8597DC36D4D6A22D7BD6CF70187BF7E9C8C00F51D9A995A0A8289B809A1D1AE25C83EA255C8A35C56E61351A05B44ACB378CFFC92933A6524BA2AFECCE91146514AF353E604E8443B347A8F477BA87526C786EA25D506FFA235908D88778DF8E1EE139608DE40CB36307C90FDC91AF16BD97EC15108D006E1559B0A8ACB7ADB311954379A10079F3CCA5ED43C03CA9F4EA3C91C07B6DE8083B9993F8011BC29A066070229D940ED5F394BF3A9DDA91C93D460BFFFD19F64D1582DB1B4B0CDEECC9D49152FA017C09A69C54ECB5796347F4447A95691378B3297E564B98C3D643F015BA4CE8AAFEAE53D29FB0203010001";
            publicKey = AsymmetricPublicKey.createFromData(ByteBuffer.wrap(Util.fromHexString(hexString)));
        }
        return publicKey;
    }
}
