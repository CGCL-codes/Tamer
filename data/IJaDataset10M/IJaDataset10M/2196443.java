package net.sf.nodeInsecure.processes;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: janmejay.singh
 * Date: Sep 6, 2007
 * Time: 1:43:17 PM
 */
public class RequestorCredentials {

    private Map<String, String> credentials;

    RequestorCredentials(String... credentials) {
        this.credentials = new HashMap<String, String>();
        for (int i = 0; i < credentials.length; i++) {
            if (i % 2 == 1) {
                this.credentials.put(credentials[i - 1], credentials[i]);
            }
        }
    }

    public String getParam(String param) {
        return credentials.get(param);
    }
}
