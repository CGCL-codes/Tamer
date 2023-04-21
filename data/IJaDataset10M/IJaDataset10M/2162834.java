package com.qq.connect;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;

public class AccessTokenTest {

    @Test
    public void testGetAccessToken() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        final String html = new AccessToken().getAccessToken("13282187343341746703", "5pp3pzFaDWrqGBGB", "703608128");
        System.out.println(html);
    }
}
