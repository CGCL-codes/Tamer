package hoplugins.youthclub.ctrl;

import static hoplugins.youthclub.Constants.*;
import hoplugins.YouthClub;
import hoplugins.commons.utils.Debug;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.w3c.dom.Document;

public class Retriever {

    private static final String pack1 = "https://www.hattrick-youthclub.org/_data_provider/?action=package1&application_id=" + YOUTHCLUB_APP_ID + "&application_code=" + YOUTHCLUB_APP_CODE + "&htID=";

    /**
	 * Get the URL for the main YC package.
	 * 
	 * @param htManagerId the users HT manager id
	 * @param ycCode the YC security code
	 */
    public static URL getPackage1Url(String htManagerId, String ycCode) throws MalformedURLException {
        return new URL(pack1 + htManagerId + "&password=" + getMD5(ycCode));
    }

    /**
	 * Load the 'package 1' XML data from the youth club page.
	 * @param ycCode the YC security code
	 */
    public static void loadPackage1(String ycCode) {
        InputStream input = null;
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new FakeTrustManager() };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            URL url = Retriever.getPackage1Url(String.valueOf(YouthClub.getMiniModel().getBasics().getTeamId()), ycCode);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
            uc.setHostnameVerifier(new FakeHostnameVerifier());
            uc.setConnectTimeout(CONNECTION_TIMEOUT);
            uc.setReadTimeout(CONNECTION_TIMEOUT);
            input = uc.getInputStream();
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = input.read()) != -1) {
                sb.append((char) c);
            }
            Document doc = YouthClub.getMiniModel().getXMLParser().parseString(sb.toString());
            String target = System.getProperty("user.home") + System.getProperty("file.separator") + "youthclub_" + new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + ".xml";
            YouthClub.getMiniModel().getXMLParser().writeXML(doc, target);
            Debug.log("YC XML saved to " + target);
        } catch (Exception e) {
            Debug.logException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
	 * Get the md5 hash for the given input string.
	 */
    public static String getMD5(String in) {
        if (in == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(in.getBytes());
            byte[] hash = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xFF & hash[i]);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            Debug.logException(e);
        }
        return null;
    }
}
