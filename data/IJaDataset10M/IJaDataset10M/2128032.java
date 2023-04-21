package osmtaglister;

import java.net.URLEncoder;
import java.net.URLDecoder;

/**
 *
 * @author plowater
 */
public class AreaStore {

    private String gName;

    private String gURL;

    public AreaStore(String Name, String URL, boolean Encoded) {
        if (Encoded) {
            StoreEncodedURL(URL);
            StoreEncodedName(Name);
        } else {
            gName = Name;
            gURL = URL;
        }
    }

    @Override
    public String toString() {
        return gName;
    }

    public String getName() {
        return gName;
    }

    public String getURL() {
        return gURL;
    }

    public String EncodeName() {
        try {
            return URLEncoder.encode(gName, "UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

    public String EncodeURL() {
        try {
            return URLEncoder.encode(gURL, "UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

    public void StoreEncodedURL(String URL) {
        try {
            gURL = URLDecoder.decode(URL, "UTF-8");
        } catch (Exception ex) {
        }
    }

    public void StoreEncodedName(String Name) {
        try {
            gName = URLDecoder.decode(Name, "UTF-8");
        } catch (Exception ex) {
        }
    }
}
