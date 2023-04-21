package omschaub.clientstuff.clientid;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.gudy.azureus2.plugins.clientid.ClientIDException;
import org.gudy.azureus2.plugins.clientid.ClientIDGenerator;
import org.gudy.azureus2.plugins.torrent.Torrent;

/**
 * @author marc
 *
 */
public class Mainline_3_4_2 implements ClientIDGenerator {

    private Map key_map = new HashMap();

    public byte[] generatePeerID(Torrent torrent, boolean for_tracker) {
        return (createPeerID());
    }

    public void generateHTTPProperties(Properties properties) {
    }

    public String[] filterHTTP(String[] lines_in) throws ClientIDException {
        View.addElement("-----------------Start------------------");
        for (int i = 0; i < lines_in.length; i++) {
            View.addElement("From Azureus:  " + lines_in[i]);
        }
        String get_in = lines_in[0];
        if (get_in.indexOf("scrape?") != -1) {
            throw (new ClientIDException("Scrape not supported by emulated client"));
        }
        int last_space_pos = get_in.lastIndexOf(' ');
        get_in = get_in.substring(0, last_space_pos).trim();
        int prefix_pos = get_in.indexOf("info_hash=") - 1;
        String prefix = get_in.substring(0, prefix_pos + 1);
        Map args = new HashMap();
        int pos = prefix_pos + 1;
        while (true) {
            int p1 = get_in.indexOf('&', pos);
            String arg;
            if (p1 == -1) {
                arg = get_in.substring(pos);
            } else {
                arg = get_in.substring(pos, p1);
            }
            int e_pos = arg.indexOf('=');
            args.put(arg.substring(0, e_pos), arg.substring(e_pos + 1));
            if (p1 == -1) {
                break;
            } else {
                pos = p1 + 1;
            }
        }
        String[] lines_out = new String[4];
        String get_out = prefix;
        String[] arg_list = { "info_hash", "peer_id", "port", "key", "uploaded", "downloaded", "left", "compact", "event" };
        for (int i = 0; i < arg_list.length; i++) {
            String key = arg_list[i];
            String arg = (String) args.get(key);
            if (arg != null) {
                if (key.equals("key")) {
                    arg = mapKeyValue(arg);
                }
                if (key.equals("peer_id")) {
                    View.setClient(arg.substring(0, 6) + " -- Mainline Bittorrent Version 3.4.2");
                }
                get_out += (get_out == prefix ? "" : "&") + key + "=" + arg;
            }
        }
        lines_out[0] = get_out + " HTTP/1.0";
        for (int i = 1; i < lines_in.length; i++) {
            if (lines_in[i].toLowerCase().indexOf("host:") != -1) {
                lines_out[1] = lines_in[i];
                break;
            }
        }
        lines_out[2] = "Accept-encoding: gzip";
        lines_out[3] = "User-agent: BitTorrent/3.4.2";
        for (int i = 0; i < lines_out.length; i++) {
            View.addElement("Sent to Tracker: " + lines_out[i]);
        }
        View.addElement("------------------End-------------------");
        return (lines_out);
    }

    protected String mapKeyValue(String in) {
        String out = (String) key_map.get(in);
        if (out == null) {
            out = "";
            for (int i = 0; i < 8; i++) {
                int pos = (int) (Math.random() * chars.length());
                out += chars.charAt(pos);
            }
            key_map.put(in, out);
        }
        return (out);
    }

    static final String chars = "abcdef0123456789";

    public static byte[] createPeerID() {
        byte[] peerId = new byte[20];
        byte[] version = "M3-4-2--".getBytes();
        for (int i = 0; i < 8; i++) {
            peerId[i] = version[i];
        }
        for (int i = 8; i < 20; i++) {
            int pos = (int) (Math.random() * chars.length());
            peerId[i] = (byte) chars.charAt(pos);
        }
        return (peerId);
    }
}
