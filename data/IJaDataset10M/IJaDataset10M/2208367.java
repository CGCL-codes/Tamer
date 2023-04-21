package ipblockui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

    private String conffile;

    private Map<String, String> option;

    private String[] keys = { "AUTOSTART", "IPTABLES_CHAIN_BLOCK", "IPTABLES_CHAIN_ALLOW", "LESS_MEMORY", "BLOCK_LIST", "BLOCK_LIST_INPUT", "BLOCK_LIST_OUTPUT", "BLOCK_LIST_FORWARD", "ALLOW_LIST", "ALLOW_LIST_INPUT", "ALLOW_LIST_OUTPUT", "ALLOW_LIST_FORWARD", "IGN_TCP_INPUT", "IGN_UDP_INPUT", "IGN_TCP_OUTPUT", "IGN_UDP_OUTPUT", "IGN_TCP_FORWARD", "IGN_UDP_FORWARD", "IGN_PROTO_INPUT", "IGN_PROTO_OUTPUT", "IGN_PROTO_FORWARD", "IPLIST_LISTDIR", "LOG_FILE", "LOG_LEVEL", "LOG_IPTABLES", "VERBOSE", "URL_FILE", "UPDATE_STAMP", "UPDATE_INTERVAL", "http_proxy", "GUI_START_HIDDEN", "GUI_LAST_LOG_LINES", "GUI_AUTOSCROLL", "GUI_THEME", "GUI_WHITELIST_PERM", "GUI_WHITELIST_TEMP" };

    public Config() {
        this.conffile = "/etc/ipblock.conf";
        option = new HashMap<String, String>();
    }

    public void read() {
        try {
            BufferedReader file = new BufferedReader(new FileReader(conffile));
            Pattern p = Pattern.compile("^([a-zA-Z_]+)[=](.+)$");
            String line;
            while ((line = file.readLine()) != null) {
                Matcher m = p.matcher(line.replaceAll("\"", ""));
                if (m.find()) {
                    option.put(m.group(1), m.group(2));
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write() {
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter(conffile));
            for (int i = 0; i < keys.length; i++) {
                file.write(keys[i] + "=\"" + getValue(keys[i]) + "\"\n");
            }
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key) {
        String val = option.get(key);
        return (val != null) ? val : "";
    }

    public String setOption(String key, String value) {
        return option.put(key, value);
    }

    public String[] readUrl(String urlfile) {
        Vector<String> urls = new Vector<String>();
        try {
            BufferedReader file = new BufferedReader(new FileReader(urlfile));
            Pattern p = Pattern.compile("^[^#].*$");
            String line;
            while ((line = file.readLine()) != null) {
                Matcher m = p.matcher(line);
                if (m.find()) {
                    String val[] = line.split("[ \t]+");
                    if (val.length > 0) {
                        urls.add(val[0]);
                    }
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls.toArray(new String[1]);
    }

    public void writeUrl(String list, String url) {
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter(getValue("URL_FILE"), true));
            file.write(list + "\t\t" + url + "\n");
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
