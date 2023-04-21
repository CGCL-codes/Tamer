package org.openymsg.context.auth.challenge;

import java.security.*;

/**
 * @author Damian Minkov
 */
public class ChallengeResponseV16 extends ChallengeResponseUtility implements ChallengeResponseStrategy {

    public String getStrings(String challenge) throws NoSuchAlgorithmException {
        byte[] md5_digest = md5(challenge);
        String base64_string = yahoo64(md5_digest);
        return base64_string;
    }
}
