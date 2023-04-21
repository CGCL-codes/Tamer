package rtm.core;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UrlBuilder {

    private static final String SERVER_URL = "http://www.rememberthemilk.com/services/rest/?";

    private static final String API_KEY = "17d807e330ca388a15be2aadf06a84b5";

    private static final String SHARED_SECRET = "51bb6a79bba375e5";

    private static final String LOGIN_URL = "http://www.rememberthemilk.com/services/auth/?";

    private static String getUnsigned(String method) {
        StringBuilder b = new StringBuilder(1000);
        b.append("api_key=").append(API_KEY);
        b.append("&method=").append(method);
        b.append("&perms=delete");
        return b.toString();
    }

    private static String getUnsigned(String method, String frob) {
        StringBuilder b = new StringBuilder(1000);
        b.append("api_key=").append(API_KEY);
        b.append("&frob=").append(frob);
        b.append("&method=").append(method);
        System.out.println(b.toString());
        return b.toString();
    }

    public String getSigned(String method, String frob) {
        final String unsigned = getUnsigned(method, frob);
        return getUrl(unsigned);
    }

    public String getSigned(String method) {
        String unsigned = getUnsigned(method);
        return getUrl(unsigned);
    }

    public String getAuthenticated(String method, String token) {
        StringBuilder b = new StringBuilder(1000);
        b.append("api_key=").append(API_KEY);
        b.append("&auth_token=").append(token);
        b.append("&method=").append(method);
        return getUrl(b.toString());
    }

    private static String getUrl(String unsigned) {
        StringBuilder b = new StringBuilder(300);
        b.append(SERVER_URL).append(unsigned).append("&api_sig=").append(getSignature(unsigned));
        return b.toString();
    }

    public String getLoginURL(String frob) {
        String unsigned = "api_key=" + API_KEY + "&frob=" + frob + "&perms=delete";
        StringBuilder b = new StringBuilder(300);
        b.append(LOGIN_URL).append(unsigned).append("&api_sig=").append(getSignature(unsigned));
        return b.toString();
    }

    private static String getSignature(String url) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not available", e);
        }
        StringBuilder msg = new StringBuilder(400);
        msg.append(SHARED_SECRET).append(url.replace("&", "").replace("=", ""));
        BigInteger hash = new BigInteger(1, md.digest(msg.toString().getBytes()));
        String s = hash.toString(16);
        return (s.length() % 2 != 0) ? "0" + s : s;
    }
}
