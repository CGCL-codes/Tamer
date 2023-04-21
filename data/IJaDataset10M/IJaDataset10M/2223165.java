package org.apache.zookeeper.server.jersey.cfg;

import java.util.HashMap;

public class Credentials extends HashMap<String, String> {

    public static Credentials join(Credentials a, Credentials b) {
        Credentials result = new Credentials();
        result.putAll(a);
        result.putAll(b);
        return result;
    }

    public Credentials() {
        super();
    }

    public Credentials(String credentials) {
        super();
        if (!credentials.trim().equals("")) {
            String[] parts = credentials.split(",");
            for (String p : parts) {
                String[] userPass = p.split(":");
                put(userPass[0], userPass[1]);
            }
        }
    }
}
