package com.technoetic.xplanner.security.module.ntlm;

import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;
import org.apache.log4j.Priority;
import com.technoetic.xplanner.security.util.Base64;

public class NtlmLoginHelperImpl implements NtlmLoginHelper {

    private final SecureRandom secureRandom = new SecureRandom();

    public void setLoggingPriority(Priority loggingPriority) {
    }

    public void authenticate(String userId, String password, String domainController, String domain) throws UnknownHostException, SmbException {
        UniAddress dc = UniAddress.getByName(domainController, true);
        NtlmPasswordAuthentication ntlm = new NtlmPasswordAuthentication(domain, userId, password);
        SmbSession.logon(dc, ntlm);
    }

    public String encodePassword(String password, byte[] salt) throws Exception {
        if (salt == null) {
            salt = new byte[12];
            secureRandom.nextBytes(salt);
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(password.getBytes("UTF8"));
        byte[] digest = md.digest();
        byte[] storedPassword = new byte[digest.length + 12];
        System.arraycopy(salt, 0, storedPassword, 0, 12);
        System.arraycopy(digest, 0, storedPassword, 12, digest.length);
        return new String(Base64.encode(storedPassword));
    }
}
