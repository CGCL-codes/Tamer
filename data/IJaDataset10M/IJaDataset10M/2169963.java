package net.sourceforge.jwapi.http;

import net.sourceforge.jwapi.http.exceptions.ReturnCodeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

public class Connection {

    private static ArrayList<Connection> instances = new ArrayList<Connection>();

    private static Logger logger = Logger.getLogger("JWAPI");

    public static Connection getConnection(URI apipath) {
        for (int i = 0; i < instances.size(); i++) {
            if (instances.get(i).getApiPath().toString().equals(apipath.toString())) {
                logger.log(Level.INFO, "Returned an EXISTING connection to '" + apipath.toString() + "'.");
                return instances.get(i);
            }
        }
        Connection con = new Connection(apipath);
        instances.add(con);
        logger.log(Level.INFO, "Returned a NEW connection to '" + apipath.toString() + "'.");
        return con;
    }

    private URI apiPath;

    private int timeBetweenReads = 0;

    private int timeBetweenWrites = 0;

    private String userAgent = "";

    private long beginReadTime = 0;

    private long beginWriteTime = 0;

    private Cookies cookies = new Cookies();

    public Connection(URI apiPath) {
        this.apiPath = apiPath;
    }

    public synchronized String postRequest(Parameters parameters) throws ReturnCodeException, IOException {
        HttpURLConnection con = (HttpURLConnection) apiPath.toURL().openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-encoding", "gzip");
        con.setRequestProperty("User-Agent", userAgent);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if (!cookies.isEmpty()) {
            for (String cookieLine : cookies.toCookieCollection()) {
                con.addRequestProperty("Cookie", cookieLine);
            }
        }
        con.setUseCaches(false);
        con.setAllowUserInteraction(false);
        con.setDoOutput(true);
        con.setDoInput(true);
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(parameters.toString());
        writer.flush();
        con.connect();
        if (con.getResponseCode() != 200) {
            throw new ReturnCodeException(con.getResponseCode() + ": " + con.getResponseMessage());
        }
        Map<String, List<String>> headers = con.getHeaderFields();
        List<String> values = headers.get("Set-Cookie");
        cookies.empty();
        if (values != null) {
            for (String cookieLine : values) {
                cookies.addCookie(cookieLine);
            }
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream()), "UTF-8"));
        String line;
        StringBuilder text = new StringBuilder(100000);
        while ((line = in.readLine()) != null) {
            text.append(line);
            text.append("\n");
        }
        in.close();
        con.disconnect();
        return text.toString();
    }

    public URI getApiPath() {
        return apiPath;
    }

    public int getTimeBetweenReads() {
        return timeBetweenReads;
    }

    public void setTimeBetweenReads(int timeBetweenReads) {
        this.timeBetweenReads = timeBetweenReads;
    }

    public int getTimeBetweenWrites() {
        return timeBetweenWrites;
    }

    public void setTimeBetweenWrites(int timeBetweenWrites) {
        this.timeBetweenWrites = timeBetweenWrites;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
