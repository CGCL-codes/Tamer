package com.google.code.facebookapi;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import com.Ostermiller.util.Browser;

public class FacebookSessionTestUtils {

    protected static Log log = LogFactory.getLog(FacebookSessionTestUtils.class);

    public static final String LOGIN_BASE_URL = "https://www.facebook.com/login.php";

    public static final String PERM_BASE_URL = "http://www.facebook.com/connect/prompt_permissions.php";

    public static JUnitProperties junitProperties;

    public static JUnitProperties getJunitProperties() {
        if (junitProperties == null) {
            junitProperties = new JUnitProperties();
        }
        return junitProperties;
    }

    public static void clearSessions() throws IOException {
        getJunitProperties().clearSessions();
    }

    public static JSONObject attainSession() throws Exception {
        JSONObject out = getJunitProperties().loadSession();
        if (out == null) {
            out = attainSessionRaw2();
            junitProperties.storeSession(out);
        }
        return out;
    }

    public static <T extends IFacebookRestClient> T getSessionlessValidClient(Class<T> clientReturnType) {
        return getSessionlessIFacebookRestClient(clientReturnType);
    }

    public static <T extends IFacebookRestClient> T getValidClient(Class<T> clientReturnType) {
        try {
            JSONObject session_info = attainSession();
            return createRestClient(clientReturnType, session_info);
        } catch (Exception ex) {
            throw BasicClientHelper.runtimeException(ex);
        }
    }

    private static <T extends IFacebookRestClient> T createRestClient(Class<T> clientReturnType, JSONObject session_info) throws JSONException {
        String apikey = session_info.getString("api_key");
        String secret = session_info.getString("secret");
        String sessionkey = session_info.getString("session_key");
        return createRestClient(clientReturnType, apikey, secret, sessionkey);
    }

    private static <T extends IFacebookRestClient> T createRestClient(Class<T> clientReturnType, String apikey, String secret, String sessionkey) {
        try {
            Constructor<T> clientConstructor = clientReturnType.getConstructor(String.class, String.class, String.class);
            return clientConstructor.newInstance(apikey, secret, sessionkey);
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't create relevant IFacebookRestClient using reflection", ex);
        }
    }

    private static <T extends IFacebookRestClient> T getSessionlessIFacebookRestClient(Class<T> clientReturnType) {
        try {
            Constructor<T> clientConstructor = clientReturnType.getConstructor(String.class, String.class);
            return clientConstructor.newInstance(getJunitProperties().getAPIKEY(), getJunitProperties().getSECRET());
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't create relevant IFacebookRestClient using reflection", ex);
        }
    }

    public static <T extends IFacebookRestClient> void requirePerm(Permission perm, T client) throws FacebookException {
        if (!client.users_hasAppPermission(perm)) {
            HttpClient http = new HttpClient();
            http.setParams(new HttpClientParams());
            http.setState(new HttpState());
            Map<String, String> params = new HashMap<String, String>();
            params.put("api_key", client.getApiKey());
            params.put("v", "1.0");
            params.put("fbconnect", "true");
            params.put("extern", "1");
            params.put("ext_perm", perm.getName());
            params.put("next", "http://www.facebook.com/connect/login_success.html?xxRESULTTOKENxx");
            String queryString = BasicClientHelper.delimit(params.entrySet(), "&", "=", true).toString();
            String url = PERM_BASE_URL + "?" + queryString;
            throw new IllegalStateException("Require extended permission " + perm.getName() + "; please visit: " + url);
        }
    }

    public static JSONObject attainSessionRaw2() throws Exception {
        return attainSessionRaw2(getJunitProperties().getAPIKEY(), getJunitProperties().getSECRET(), 8080);
    }

    /**
	 * http://wiki.developers.facebook.com/index.php/Authorization_and_Authentication_for_Desktop_Applications
	 */
    public static JSONObject attainSessionRaw2(String apikey, String secret, int port) throws Exception {
        Server server = null;
        Semaphore semaphore = new Semaphore(1);
        semaphore.drainPermits();
        CaptureSessionHandler handler = new CaptureSessionHandler(apikey, secret, semaphore);
        try {
            {
                server = new Server(port);
                server.setHandler(handler);
                log.info("Starting Jetty");
                server.start();
            }
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", apikey);
                params.put("fbconnect", "true");
                params.put("v", "1.0");
                params.put("return_session", "true");
                params.put("req_perms", "publish_stream,read_stream,offline_access");
                params.put("next", "http://localhost:" + port + "/next");
                params.put("cancel_url", "http://localhost:" + port + "/cancel");
                String query = BasicClientHelper.delimit(params.entrySet(), "&", "=", true).toString();
                String url = LOGIN_BASE_URL + "?" + query;
                log.info("Sending user to attain session:\n" + url);
                Browser.init();
                Browser.displayURL(url);
            }
            {
                for (int i = 1; i <= 12; i++) {
                    log.debug("Waiting for 5 sec (" + i + ")");
                    if (semaphore.tryAcquire(5, TimeUnit.SECONDS)) {
                        Thread.sleep(1000);
                        break;
                    }
                }
            }
        } finally {
            if (server != null && !(server.isStopped() || server.isStopping())) {
                log.info("Stopping Jetty");
                server.stop();
            }
        }
        return handler.getOut();
    }

    public static class CaptureSessionHandler extends AbstractHandler {

        private JSONObject out;

        private String apikey;

        private String secret;

        private Semaphore semaphore;

        public CaptureSessionHandler(String apikey, String secret, Semaphore semaphore) {
            this.apikey = apikey;
            this.secret = secret;
            this.semaphore = semaphore;
        }

        public JSONObject getOut() {
            return out;
        }

        @SuppressWarnings("unchecked")
        public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
            log.debug("handle(): " + request.getRequestURL() + printMap(request.getParameterMap()));
            try {
                if (target.equals("/next")) {
                    out = captureSession(request, apikey, secret);
                }
                if (target.equals("/cancel")) {
                    log.warn("User cancelled");
                    semaphore.release();
                }
                if (target.equals("/done")) {
                    log.warn("User is done");
                    semaphore.release();
                }
            } catch (JSONException ex) {
                throw new ServletException(ex);
            }
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<html><body><a href='/done'>done?</a></body></html>");
            ((Request) request).setHandled(true);
        }

        public static JSONObject captureSession(HttpServletRequest request, String apikey, String secret) throws JSONException {
            String str = request.getParameter("session");
            JSONObject out = new JSONObject(str);
            out.remove("sig");
            out.put("ss", out.getString("secret"));
            out.put("secret", secret);
            out.put("api_key", apikey);
            log.debug("session: " + out);
            return out;
        }

        public static String printMap(Map<String, String[]> map) {
            StringBuilder sb = new StringBuilder();
            for (Entry<String, String[]> entry : map.entrySet()) {
                sb.append("\n");
                sb.append(entry.getKey() + " : " + Arrays.asList(entry.getValue()));
            }
            return sb.toString();
        }
    }

    public static void pauseForStreamRate() {
        log.debug("pauseForStreamRate()");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            throw BasicClientHelper.runtimeException(ex);
        }
    }
}
