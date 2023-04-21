package fitService.fitnesse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import fitService.ServerCommunicator;
import fitService.util.Base64;

public class FitnesseServerCommunicator implements ServerCommunicator {

    private String serverURL;

    private String textareaContent;

    private String user;

    private String password;

    private HashMap formDataMap;

    private Set<Map.Entry> dataSet;

    public FitnesseServerCommunicator() {
        formDataMap = new HashMap();
    }

    public boolean login(String serverpath, String username, String password) {
        serverURL = serverpath;
        URL url;
        try {
            url = new URL(serverURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String loadWikiPage(String pageName) {
        URL url;
        HttpURLConnection connectHttp;
        StringBuffer sb = new StringBuffer();
        String inputLine;
        BufferedReader br;
        try {
            formDataMap.clear();
            url = new URL(getPagepath(pageName) + "?edit");
            connectHttp = (HttpURLConnection) url.openConnection();
            initConnection(connectHttp);
            connectHttp.setRequestMethod("GET");
            connectHttp.setRequestProperty("Content-Type", "text/html");
            connectHttp.connect();
            br = GetInputStream(connectHttp);
            do {
                inputLine = br.readLine();
                sb.append(inputLine).append("\r\n");
            } while (inputLine != null);
            br.close();
            connectHttp.disconnect();
            if (sb.length() != 0) {
                getTextAreaContent(sb);
                getFormData(sb.toString());
                dataSet = formDataMap.entrySet();
                return textareaContent;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveWikiPage(String pageName, String pageContent, String author) {
        URL url;
        HttpURLConnection connectHttp;
        StringBuffer sb = new StringBuffer();
        String inputLine;
        BufferedReader br;
        String encodedStr;
        PrintStream ps;
        try {
            for (Map.Entry child : dataSet) sb.append(child.getKey() + "=" + child.getValue() + "&");
            url = new URL(getPagepath(pageName));
            connectHttp = (HttpURLConnection) url.openConnection();
            initConnection(connectHttp);
            connectHttp.setRequestMethod("POST");
            connectHttp.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connectHttp.setRequestProperty("Authorization", "Basic " + Base64.encode(user + ":" + password));
            sb.append("pageContent=" + pageContent);
            encodedStr = URLEncoder.encode(sb.toString(), "UTF-8");
            encodedStr = encodedStr.replace("%3D", "=");
            encodedStr = encodedStr.replace("%26", "&");
            ps = GetPrintStream(connectHttp);
            ps.println(encodedStr);
            ps.close();
            br = GetInputStream(connectHttp);
            br.close();
            if (connectHttp.getResponseMessage().equals("OK")) return true;
            connectHttp.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void initConnection(HttpURLConnection connect) {
        connect.setDoOutput(true);
        connect.setDoInput(true);
        connect.setDefaultUseCaches(false);
    }

    private BufferedReader GetInputStream(URLConnection cnt) throws IOException {
        InputStream is = cnt.getInputStream();
        InputStreamReader inStream = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(inStream);
        return br;
    }

    private PrintStream GetPrintStream(URLConnection cnt) throws IOException {
        OutputStream os = cnt.getOutputStream();
        PrintStream ps = new PrintStream(os);
        return ps;
    }

    private void getTextAreaContent(StringBuffer htmlString) {
        Pattern pattern = Pattern.compile("(.*)<textarea(.*)>(.*)</textarea>(.*)", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher match = pattern.matcher(htmlString.toString());
        if (!match.find()) return;
        textareaContent = match.group(3);
    }

    private void getFormData(String strToParse) {
        String str;
        if (strToParse != null) {
            Pattern pattern = Pattern.compile("(.*)<input(.*)name=\"(.*)\"(.*)value=\"([^\\s]+)\"(.*)/>(.*)", Pattern.MULTILINE | Pattern.DOTALL);
            Matcher match = pattern.matcher(strToParse);
            if (!match.find()) return;
            formDataMap.put(match.group(3), match.group(5));
            str = match.group(1);
            getFormData(str);
        }
    }

    private String getPagepath(String pageName) {
        String pageUrl = serverURL;
        if (serverURL.charAt(serverURL.length() - 1) != '/') pageUrl += '/';
        pageUrl += pageName;
        return pageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
