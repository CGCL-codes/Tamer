package com.persistent.appfabric.acs;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import com.persistent.appfabric.common.AppFabricEnvironment;
import com.persistent.appfabric.common.AppFabricException;

/**
 * This class provides the functions used for getting credentials in SharedSecretTokenProvider 
 * */
public class SharedSecretCredential {

    public static String ComputeSimpleWebToken(String issuerName, String issuerSecret) throws UnsupportedEncodingException, AppFabricException {
        String token_ = TokenConstants.getTokenIssuer() + "=" + URLDecoder.decode(issuerName, AppFabricEnvironment.ENCODING);
        String ComputedSimpleWebTokenString = token_ + "&" + TokenConstants.getTokenDigest256() + "=" + URLEncoder.encode(SharedSecretCredential.getTokenHMcKey(URLDecoder.decode(token_, AppFabricEnvironment.ENCODING), issuerSecret), AppFabricEnvironment.ENCODING);
        return ComputedSimpleWebTokenString;
    }

    /**
     * Function that gets the HMac signature 
     * @param token Issuer name
     * @param issuerSecretKey Issuer secret key
     * */
    public static String getTokenHMcKey(String token, String issuerSecretKey) throws AppFabricException {
        String algorithm = TokenConstants.getTokenDigest256();
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(issuerSecretKey.getBytes());
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, algorithm);
        Mac mac = null;
        try {
            mac = Mac.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        try {
            mac.init(secretKeySpec);
        } catch (InvalidKeyException e) {
            throw new AppFabricException("InvalidKeyException in getTokenHMcKey");
        }
        byte[] macBytes = mac.doFinal(token.getBytes());
        Base64 encoder = new Base64();
        String signature = null;
        signature = new String(encoder.encode(macBytes));
        signature = signature.replaceAll("\r\n", "");
        return signature;
    }
}
