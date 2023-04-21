package net.sipvip.SevCommon;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.json.JSONArray;

public class DomainCsvReader {

    private static final Logger log = Logger.getLogger(DomainCsvReader.class.getName());

    public static JSONArray DoaminCsvReader(String filestr, String domain) {
        String[] shot_domain_array = domain.split("\\.");
        String shot_domain = shot_domain_array[1] + "." + shot_domain_array[2];
        log.info("shot_domain " + shot_domain + " " + shot_domain_array.length);
        JSONArray outlist = new org.json.JSONArray();
        try {
            FileInputStream fis = new FileInputStream(filestr);
            BufferedReader bufRdr = new BufferedReader(new InputStreamReader(fis, "UTF8"));
            String line = null;
            while ((line = bufRdr.readLine()) != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                String[] values = line.split(",");
                String csvdomain = values[0];
                if (csvdomain.equals(shot_domain)) {
                    map.put("title", values[1]);
                    map.put("charset", values[2]);
                    map.put("locale", values[3]);
                    map.put("links", values[4]);
                    map.put("theme", values[5]);
                    map.put("facebookid", values[6]);
                    map.put("google", values[7]);
                    map.put("slot_r", values[8]);
                    map.put("slot_up", values[9]);
                    map.put("slot_s", values[10]);
                    map.put("slot_l", values[11]);
                    outlist.put(map);
                }
            }
        } catch (Exception ex) {
            log.severe(ex.getMessage());
        }
        return outlist;
    }
}
