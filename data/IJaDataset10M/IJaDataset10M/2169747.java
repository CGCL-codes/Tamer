package rmi.digest;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class RMIDigestClient {

    private static final String USERNAME = "user";

    private static final String PASSWORD = "1234";

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            final MD5Digest digest = new MD5Digest();
            System.out.println("Making lookup ...");
            RMIDigestValidator val = (RMIDigestValidator) Naming.lookup("//160.98.31.52/RMIDigestServer");
            System.out.println("Invoking challange ...");
            final String challange = val.getChallenge2(USERNAME + "CL");
            if (val.challengeResponse(USERNAME, digest.doHash(digest.doHash(PASSWORD), challange.getBytes()))) {
                System.out.println("Challange successful");
            } else {
                System.out.println("Challange failed");
            }
        } catch (Exception e) {
            System.out.println("RMIDigestClient exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
