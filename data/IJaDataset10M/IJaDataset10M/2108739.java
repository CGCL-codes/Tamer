package at.htlwien10.roomproxy;

import java.security.*;

public final class Master {

    private static final long SLEEP_ON_DENY = 500;

    private static final char[] HEXTABLE = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private String name;

    private String passwd;

    public Master(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    public boolean checkAuth(String toCheck) {
        if (toCheck != null && passwd.equalsIgnoreCase(hashPassword(toCheck))) return true;
        while (true) {
            try {
                Thread.currentThread().sleep(SLEEP_ON_DENY);
                break;
            } catch (InterruptedException e) {
            }
        }
        return false;
    }

    public String toString() {
        return "[Master: " + name + ": " + passwd + "]";
    }

    public String getName() {
        return name;
    }

    public static String hashPassword(String clear) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hash = md.digest(clear.getBytes());
            StringBuffer sb = new StringBuffer();
            int i;
            for (i = 0; i < hash.length; i++) {
                sb.append(HEXTABLE[(hash[i] >> 4) & 0xf]);
                sb.append(HEXTABLE[hash[i] & 0xf]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.severe("Got: " + e);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("<Master user=\"" + args[0] + "\" passwd=\"" + hashPassword(args[1]) + "\"/>");
    }
}
