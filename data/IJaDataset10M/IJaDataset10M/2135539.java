package de.schlund.marwein;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import org.xml.sax.SAXException;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;

/**
 * 
 http://httpunit.sourceforge.net/doc/sslfaq.html
 http://httpunit.sourceforge.net/doc/cookbook.html
 * 
 */
public class Test {

    public static final int ERR_NOFORM = 1;

    public static final String KEY_REQUEST_URL = "baseurl";

    public static final String KEY_LOGIN = "login";

    public static final String KEY_PASSWORD = "password";

    public static final String KEY_LINKS = "links";

    public static final String KEY_FORMNUMBER = "loginformnumber";

    private static final int NUMBER_OF_TIMES = 10;

    long[] mTimesRelative = new long[NUMBER_OF_TIMES];

    long mTimeStart = -1;

    int mTimeCount = -1;

    Properties mProperties = null;

    public void times_reset() {
        mTimeStart = System.currentTimeMillis();
        for (int a = 0; a < NUMBER_OF_TIMES; a++) mTimesRelative[a] = -1;
        mTimeCount = 0;
    }

    public void times_measure() {
        mTimesRelative[mTimeCount++] = System.currentTimeMillis() - mTimeStart;
    }

    public void times_show() {
        System.out.print(" milliseconds:");
        for (int a = 0; (a < NUMBER_OF_TIMES) && (mTimesRelative[a] > 0); a++) {
            System.out.print(mTimesRelative[a] + "\t");
        }
        System.out.println();
    }

    public int test1(Properties props) throws MalformedURLException, IOException, SAXException {
        WebConversation wc = new WebConversation();
        String baseurl = props.getProperty(KEY_REQUEST_URL);
        System.out.println("baseurl=" + baseurl);
        times_reset();
        WebRequest req = new GetMethodWebRequest(baseurl);
        wc.getResponse(req);
        WebForm[] allforms = wc.getCurrentPage().getForms();
        if (allforms == null) return ERR_NOFORM;
        if (allforms.length < 1) return ERR_NOFORM;
        String sFormnumber = props.getProperty(KEY_FORMNUMBER);
        Integer i = Integer.valueOf(sFormnumber);
        int fn = 0;
        if (i != null) fn = i.intValue();
        allforms[fn].setParameter("login.User", props.getProperty(KEY_LOGIN));
        allforms[fn].setParameter("login.Pass", props.getProperty(KEY_PASSWORD));
        allforms[fn].submit();
        times_measure();
        int linkCount = 1;
        String linkname = props.getProperty(KEY_LINKS + "." + linkCount);
        while (linkname != null) {
            WebLink link = wc.getCurrentPage().getLinkWith(linkname);
            if (link == null) link = wc.getCurrentPage().getLinkWithImageText(linkname);
            if (link != null) {
                System.out.println(" now following: " + linkname);
                link.click();
                times_measure();
                linkCount++;
                linkname = props.getProperty(KEY_LINKS + "." + linkCount);
            } else linkname = null;
        }
        times_show();
        return 0;
    }

    public Properties loadProperties(String propertyFileName) {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(propertyFileName));
        } catch (IOException e) {
            System.out.println(" Error while reading the property file: " + propertyFileName + ":\n" + e);
            p = null;
        }
        return p;
    }

    public static void main(String[] args) throws Exception {
        HttpUnitOptions.setLoggingHttpHeaders(false);
        HttpUnitOptions.setScriptingEnabled(false);
        if (args.length < 1) {
            System.out.println(" need a setup file number, for example 1 ");
        }
        String setupfilename = "conf/setup." + args[0] + ".conf";
        Test t = new Test();
        Properties p = t.loadProperties(setupfilename);
        t.test1(p);
    }
}
