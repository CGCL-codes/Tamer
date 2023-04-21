package org.armedbear.j;

import java.io.InputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.StringTokenizer;

public final class Netrc {

    private static Vector logins;

    private static long lastModified;

    public static Login getLogin(String host) {
        if (host == null) return null;
        parseNetrc();
        if (logins == null) return null;
        final int limit = logins.size();
        for (int i = 0; i < limit; i++) {
            Login login = (Login) logins.get(i);
            if (host.equals(login.host)) return login;
        }
        return null;
    }

    public static String getPassword(String host, String user) {
        if (host == null) return null;
        parseNetrc();
        if (logins == null) return null;
        final int limit = logins.size();
        for (int i = 0; i < limit; i++) {
            Login login = (Login) logins.get(i);
            if (host.equals(login.host)) {
                if (user == null || user.equals(login.user)) return login.password;
            }
        }
        return null;
    }

    private static void parseNetrc() {
        File file = File.getInstance(Directories.getUserHomeDirectory(), ".netrc");
        if (!file.isFile() || !file.canRead()) {
            logins = null;
            return;
        }
        if (logins != null) {
            if (file.lastModified() == lastModified) return;
            logins = null;
        }
        try {
            lastModified = file.lastModified();
            int length = (int) file.length();
            byte bytes[] = new byte[length];
            InputStream in = file.getInputStream();
            if (in.read(bytes) != length) return;
            in.close();
            String s = new String(bytes);
            StringTokenizer st = new StringTokenizer(s);
            String host = null;
            String user = null;
            String password = null;
            logins = new Vector();
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.equals("machine")) {
                    if (host != null && user != null && password != null) logins.add(new Login(host, user, password));
                    host = st.nextToken();
                    user = null;
                    password = null;
                } else if (token.equals("login")) {
                    user = st.nextToken();
                } else if (token.equals("password")) password = st.nextToken();
            }
            if (host != null && user != null && password != null) logins.add(new Login(host, user, password));
        } catch (IOException e) {
            Log.error(e);
        }
        if (logins.size() == 0) logins = null;
    }
}
