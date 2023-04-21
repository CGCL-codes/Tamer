package javasape;

import javax.servlet.http.Cookie;

public class Sape {

    private final String sapeUser;

    private final SapeConnection sapePageLinkConnection;

    public Sape(String sapeUser, String host, int socketTimeout, int cacheLifeTime) {
        this.sapeUser = sapeUser;
        this.sapePageLinkConnection = new SapeConnection("/code.php?user=" + sapeUser + "&host=" + host, "SAPE_Client PHP", socketTimeout, cacheLifeTime);
    }

    public boolean debug = false;

    public SapePageLinks getPageLinks(String requestUri, Cookie[] cookies) {
        return new SapePageLinks(sapePageLinkConnection, sapeUser, requestUri, cookies, debug);
    }
}
