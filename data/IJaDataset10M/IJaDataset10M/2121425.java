package org.broadleafcommerce.extensibility.context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StandardConfigLocations {

    private static final Log LOG = LogFactory.getLog(StandardConfigLocations.class);

    public static String[] retrieveAll() throws IOException {
        String[] response;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(StandardConfigLocations.class.getResourceAsStream("StandardConfigLocations.txt")));
            ArrayList<String> items = new ArrayList<String>();
            boolean eof = false;
            while (!eof) {
                String temp = reader.readLine();
                if (temp == null) {
                    eof = true;
                } else {
                    if (!temp.startsWith("#") && temp.trim().length() > 0) {
                        items.add(temp.trim());
                    }
                }
            }
            response = new String[] {};
            response = items.toArray(response);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable e) {
                    LOG.error("Unable to merge source and patch locations", e);
                }
            }
        }
        return response;
    }
}
