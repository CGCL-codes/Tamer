package utils;

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.util.*;
import java.security.Security;
import java.security.Provider;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.*;
import com.thoughtworks.selenium.Selenium;
import org.testng.*;

public class mySeleniumUtils {

    public static void captureEntirePageMultipleBrowsers(Selenium driverSelenium, ITestContext contextTNG, String bpath, String filename, String kwargs) {
        String JSlibrarySnapsie = "";
        if (bpath.contains("iexplore") || bpath.contains("iehta")) {
            String file = contextTNG.getOutputDirectory() + "\\..\\..\\static\\js\\snapsie.js";
            FileReader fichero = null;
            StringBuffer str = new StringBuffer();
            try {
                fichero = new FileReader(file);
                int c;
                while ((c = fichero.read()) != -1) {
                    str.append((char) c);
                }
                JSlibrarySnapsie = str.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fichero) fichero.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            filename = filename.replace("\\", "\\\\");
            String grabScreen = JSlibrarySnapsie + ";" + "    Snapsie.saveSnapshot('" + filename + "');";
            driverSelenium.runScript(grabScreen);
        } else {
            driverSelenium.captureEntirePageScreenshot(filename, kwargs);
        }
    }

    public static InputStream obtainDocumentURL(Selenium selenium, String URL, boolean URLRelativa, ITestContext contextTNG) throws IOException, Exception {
        String cookie = selenium.getCookie();
        if (URLRelativa) {
            String dominio = selenium.getEval("window.document.domain");
            String location = selenium.getLocation();
            int posDominio = location.indexOf(dominio);
            URL = location.substring(0, posDominio) + dominio + URL;
        }
        try {
            HostnameVerifier hv = new HostnameVerifier() {

                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            URL url = new URL(URL);
            if (URL.indexOf("https") == 0) {
                if (((String) contextTNG.getAttribute("proxyPort")).compareTo("") != 0) {
                    System.setProperty("https.proxyHost", "muscat");
                    System.setProperty("https.proxyPort", "8080");
                }
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestProperty("Cookie", cookie);
                return con.getInputStream();
            } else {
                if (((String) contextTNG.getAttribute("proxyPort")).compareTo("") != 0) {
                    System.setProperty("http.proxyHost", "muscat");
                    System.setProperty("http.proxyPort", "8080");
                }
                URLConnection con = url.openConnection();
                con.setRequestProperty("Cookie", cookie);
                return con.getInputStream();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void acceptAllCertificates() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }

    public static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
            return;
        }
    }

    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}
