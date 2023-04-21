package net.sf.andhsli.hotspotlogin;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class HotspotUtilities {

    private static final String TAG = "HSLI";

    public static enum HotspotStatus {

        WIFIDISABLED, SSIDNOTFOUND, NETCONFIGNOTFOUND, DISCONNECTED, CONNECTING, SWITCHTOWIFI, HOTSPOTCONNECTED, INTERNET, UNKNOWNHOTSPOT, ERROR
    }

    ;

    public static HotspotStatus getHotspotStatus(Params params, WifiManager wifiMan, ConnectivityManager conyMan) {
        try {
            if (!wifiMan.isWifiEnabled()) return HotspotStatus.WIFIDISABLED;
            boolean foundSSID = false;
            List<ScanResult> scanResults = wifiMan.getScanResults();
            if (scanResults == null) {
                Log.e(TAG, "ScanResults=<null>");
                return HotspotStatus.SSIDNOTFOUND;
            }
            for (ScanResult scanResult : scanResults) {
                if (params.getSSID().equals(scanResult.SSID)) {
                    foundSSID = true;
                    break;
                }
            }
            if (!foundSSID) return HotspotStatus.SSIDNOTFOUND;
            int netConfig = -1;
            List<WifiConfiguration> confNets = wifiMan.getConfiguredNetworks();
            if (confNets == null) {
                Log.e(TAG, "WifiConfiguration=<null>");
                return HotspotStatus.NETCONFIGNOTFOUND;
            }
            for (WifiConfiguration confNet : confNets) {
                if (("\"" + params.getSSID() + "\"").equals(confNet.SSID)) {
                    netConfig = confNet.networkId;
                    break;
                }
            }
            if (netConfig == -1) return HotspotStatus.NETCONFIGNOTFOUND;
            WifiInfo conInfo = wifiMan.getConnectionInfo();
            if (conInfo == null) return HotspotStatus.DISCONNECTED;
            if ((conInfo.getSupplicantState() == SupplicantState.DISCONNECTED) || (conInfo.getSupplicantState() == SupplicantState.DORMANT) || (conInfo.getSupplicantState() == SupplicantState.INACTIVE)) return HotspotStatus.DISCONNECTED;
            if (!params.getSSID().equals(conInfo.getSSID())) return HotspotStatus.DISCONNECTED;
            if (conInfo.getSupplicantState() != SupplicantState.COMPLETED) return HotspotStatus.CONNECTING;
            NetworkInfo netInfo = conyMan.getActiveNetworkInfo();
            if (netInfo == null) {
                Log.e(TAG, "NetworkInfo=<null>");
                return HotspotStatus.SWITCHTOWIFI;
            }
            if (netInfo.getType() != ConnectivityManager.TYPE_WIFI) return HotspotStatus.SWITCHTOWIFI;
            String statusUrl = params.getStatusCheckUrl();
            String response = httpGet(statusUrl, params.getUserAgent());
            if (response.indexOf(params.getStatusCheckSucceededResponse()) != -1) return HotspotStatus.INTERNET;
            if (response.indexOf(params.getStatusCheckFailedResponse()) != -1) return HotspotStatus.HOTSPOTCONNECTED;
            return HotspotStatus.UNKNOWNHOTSPOT;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return HotspotStatus.ERROR;
        }
    }

    public static String connect(Params params, WifiManager wifiMan) {
        try {
            List<ScanResult> srList = wifiMan.getScanResults();
            boolean start = wifiMan.startScan();
            if (!start) return "startScan failed";
            if (srList == null) return "no visible nets";
            boolean found = false;
            for (ScanResult sr : srList) {
                if (params.getSSID().equals(sr.SSID)) {
                    found = true;
                    break;
                }
            }
            if (!found) return "SSID not found";
            List<WifiConfiguration> confNets = wifiMan.getConfiguredNetworks();
            for (WifiConfiguration confNet : confNets) {
                if (("\"" + params.getSSID() + "\"").equals(confNet.SSID)) {
                    boolean resultOK = wifiMan.enableNetwork(confNet.networkId, true);
                    if (resultOK) return "ok";
                    return "enableNetwork #" + confNet.networkId + " failed";
                }
            }
            return "network config not found";
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return "Exception '" + e.getMessage() + "'";
        }
    }

    ;

    public static boolean disconnect(Params params, WifiManager wifiMan) {
        try {
            WifiInfo conInfo = wifiMan.getConnectionInfo();
            if (conInfo == null) return true;
            if (!params.getSSID().equals(conInfo.getSSID())) return true;
            boolean result = wifiMan.disconnect();
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    public static String login(Params params, String login, String pw) {
        try {
            String logInURL = params.getLogInURL();
            String logInParams = params.getLogInParams();
            String[] paramList = logInParams.split("[|]");
            String[][] logInAttribs = new String[paramList.length][2];
            for (int i = 0; i < paramList.length; i++) {
                logInAttribs[i][0] = paramList[i].replaceFirst("^([^=]*)[=](.*)$", "$1");
                logInAttribs[i][1] = paramList[i].replaceFirst("^([^=]*)[=](.*)$", "$2");
                logInAttribs[i][1] = HotspotUtilities.replaceAllFix(logInAttribs[i][1], "$LOGIN", login);
                logInAttribs[i][1] = HotspotUtilities.replaceAllFix(logInAttribs[i][1], "$PW", pw);
            }
            String response = HotspotUtilities.httpPost(logInURL, logInAttribs, params.getUserAgent());
            if (response.indexOf(params.getLoggedInText()) != -1) return "OK";
            if (response.indexOf(params.getLoggedOutText()) != -1) return "WRONG";
            return "UNKNOWN";
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return "Exception: " + e.getMessage();
        }
    }

    public static boolean logout(Params params) {
        try {
            String response = HotspotUtilities.httpGet(params.getLogOutURL(), params.getUserAgent());
            return response.startsWith("OK:");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    public static String httpGet(String urlReq, String userAgent) throws Exception {
        StringBuffer result = new StringBuffer();
        DefaultHttpClient client = new DefaultHttpClient();
        client.setRedirectHandler(new RedirectHandler() {

            @Override
            public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
                return false;
            }

            @Override
            public URI getLocationURI(HttpResponse response, HttpContext context) throws ProtocolException {
                return null;
            }
        });
        HttpGet method = new HttpGet(new URI(urlReq));
        method.setHeader("user-agent", userAgent);
        HttpResponse res = client.execute(method);
        StatusLine status = res.getStatusLine();
        result.append(status.getStatusCode() + " " + status.getReasonPhrase());
        Header[] header = res.getAllHeaders();
        for (int i = 0; i < header.length; i++) {
            result.append(header[i].getName() + "='" + header[i].getValue() + "'\n");
        }
        int length = Math.min(30000, (int) res.getEntity().getContentLength());
        if (length <= 0) length = 30000;
        InputStream is = res.getEntity().getContent();
        byte[] buf = new byte[length];
        int cnt = is.read(buf);
        while ((cnt > 0) && (length > 0)) {
            result.append(new String(buf, 0, cnt));
            length -= cnt;
            cnt = is.read(buf);
        }
        return result.toString();
    }

    public static String httpPost(String urlReq, String[][] attribs, String userAgent) throws Exception {
        StringBuffer result = new StringBuffer();
        DefaultHttpClient client = new DefaultHttpClient();
        ArrayList<BasicNameValuePair> attributeList = new ArrayList<BasicNameValuePair>();
        for (int i = 0; i < attribs.length; i++) {
            attributeList.add(new BasicNameValuePair(attribs[i][0], attribs[i][1]));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(attributeList);
        HttpPost method = new HttpPost(new URI(urlReq));
        method.setHeader("user-agent", userAgent);
        method.setEntity(entity);
        result.append("REQUEST=").append(method.getRequestLine()).append("\n");
        Header[] header = method.getAllHeaders();
        for (int i = 0; i < header.length; i++) result.append(header[i].getName() + "='" + header[i].getValue() + "'\n");
        HttpResponse res = client.execute(method);
        StatusLine status = res.getStatusLine();
        result.append("RESPONSE: ").append(status.getStatusCode() + " " + status.getReasonPhrase());
        header = res.getAllHeaders();
        for (int i = 0; i < header.length; i++) result.append(header[i].getName() + "='" + header[i].getValue() + "'\n");
        int length = Math.min(30000, (int) res.getEntity().getContentLength());
        if (length <= 0) length = 30000;
        InputStream is = res.getEntity().getContent();
        byte[] buf = new byte[length];
        int cnt = is.read(buf);
        while ((cnt > 0) && (length > 0)) {
            result.append(new String(buf, 0, cnt));
            length -= cnt;
            cnt = is.read(buf);
        }
        return result.toString();
    }

    public static String replaceAllFix(String text, String search, String replace) {
        int n = text.indexOf(search);
        if (n == -1) return text;
        return text.substring(0, n) + replace + text.substring(n + search.length());
    }

    public static void playSound() {
        ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
    }
}
