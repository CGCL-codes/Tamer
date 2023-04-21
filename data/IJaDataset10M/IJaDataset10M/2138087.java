package com.makotan.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.seasar.framework.util.Base64Util;
import com.makotan.exception.NoSuchAlgorithmRuntimeException;
import com.makotan.util.file.InputStreamUtil;

public class MessageDigestUtil {

    public static String stringMessageDigest(String content) {
        return stringMessageDigest(content, "SHA-256");
    }

    public static String stringMessageDigest(String content, String algorithm) {
        if (content == null) {
            return "";
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmRuntimeException(e);
        }
        byte[] digestBytes = digest.digest(content.getBytes());
        return Base64Util.encode(digestBytes);
    }

    public static String fileMessageDigest(String fileName) {
        return fileMessageDigest(fileName, "SHA-256");
    }

    public static String fileMessageDigest(String fileName, String algorithm) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmRuntimeException(e);
        }
        byte[] digestBytes = digest.digest(InputStreamUtil.getContent(fileName));
        return Base64Util.encode(digestBytes);
    }
}
