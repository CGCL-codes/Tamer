package uk.co.pointofcare.echobase.terminology.atc.webpage;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Logger;
import uk.co.pointofcare.echobase.webcache.WebPage;

/**
 * @author RCHALLEN
 *
 */
public class AtcConceptDetailsPage extends WebPage {

    static Logger log = Logger.getLogger(AtcConceptDetailsPage.class);

    private static String baseURL = "http://www.whocc.no/atc_ddd_index/?";

    private static String getCodeOption = "code=";

    private static String showDescriptionOption = "showdescription=no";

    private String code;

    public static AtcConceptDetailsPage create(String atcCode) {
        AtcConceptDetailsPage out = new AtcConceptDetailsPage();
        out.code = atcCode;
        return WebPage.init(out);
    }

    protected Epoch getExpiry() {
        return Epoch.MONTH;
    }

    public URL getUrl() {
        try {
            return new URL(baseURL + getCodeOption + code + "&" + showDescriptionOption);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCode() {
        return code;
    }
}
