package edu.harvard.iq.safe.saasystem.util;

import java.util.logging.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import com.ning.http.client.*;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Realm.AuthScheme;
import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Akio Sone
 */
public class AsyncHttpClientZeroBytesCopy {

    static final Logger logger = Logger.getLogger(AsyncHttpClientZeroBytesCopy.class.getName());

    static String SAAS_BIRT_VIEWER_ACCESS_ACCOUNT = "";

    static String SAAS_BIRT_VIEWER_ACCESS_PASSWORD = "";

    static final String CONFIG_JNDI_NAME = "java:global/safearchiveauditsystem-ear/safearchiveauditsystem-ejb-1.1/ConfigFile";

    InitialContext ic = null;

    ConfigFile configFile = null;

    String saasBirtviewerAccessAccount = null;

    String saasBirtviewerAccessPassword = null;

    {
        try {
            ic = new InitialContext();
            configFile = (ConfigFile) ic.lookup(CONFIG_JNDI_NAME);
            logger.log(Level.INFO, "AsyncHttpClientZeroBytesCopy: JNDI-look up seems OK");
        } catch (NamingException ex) {
            logger.severe("Class AsyncHttpClientZeroBytesCopy: JNDI lookup failed: ConfigFile");
        }
    }

    public AsyncHttpClientZeroBytesCopy() {
        init();
    }

    private void init() {
        saasBirtviewerAccessAccount = configFile.getBirtViewerAccessAccount();
        saasBirtviewerAccessPassword = configFile.getBirtViewerAccessPassword();
        if (StringUtils.isBlank(saasBirtviewerAccessAccount)) {
            logger.log(Level.INFO, "AsyncHttpClientZeroBytesCopy: ini: configFile does not have saasBirtviewerAccessAccount data");
            saasBirtviewerAccessAccount = SAAS_BIRT_VIEWER_ACCESS_ACCOUNT;
        } else {
            logger.log(Level.INFO, "AsyncHttpClientZeroBytesCopy: ini: configFile: saasBirtviewerAccessAccount={0}", saasBirtviewerAccessAccount);
        }
        logger.log(Level.INFO, "AsyncHttpClientZeroBytesCopy: ini: saasBirtviewerAccessAccount={0}", saasBirtviewerAccessAccount);
        if (StringUtils.isBlank(saasBirtviewerAccessPassword)) {
            logger.log(Level.INFO, "AsyncHttpClientZeroBytesCopy: ini: configFile does not have saasBirtviewerAccessPassword data");
            saasBirtviewerAccessPassword = SAAS_BIRT_VIEWER_ACCESS_PASSWORD;
        } else {
            logger.log(Level.INFO, "AsyncHttpClientZeroBytesCopy: ini: configFile: saasBirtviewerAccessPassword={0}", saasBirtviewerAccessPassword);
        }
        logger.log(Level.INFO, "AsyncHttpClientZeroBytesCopy: ini: saasBirtviewerAccessPassword={0}", saasBirtviewerAccessPassword);
    }

    /**
     * 
     * To use this method, the caller must specify realm like this:
     *     Realm realm = new Realm.RealmBuilder().setPrincipal(user)
     *     .setPassword(admin).setUsePreemptiveAuth(true)
     *     .setScheme(AuthScheme.BASIC).build();
     * 
     * @param url
     * @param filename
     * @param realm
     */
    public void zeroCopyDownload(String url, String filename, Realm realm) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
            Future<String> future = httpClient.prepareGet(url).setRealm(realm).execute(new SaasAsyncHandlerImpl(bos));
            while (future.isDone()) {
                logger.log(Level.INFO, "please wait while getting the requeste file downloaded ... ");
            }
            logger.log(Level.INFO, future.get());
            bos.close();
            httpClient.close();
        } catch (ExecutionException ex) {
            logger.severe("zeroCopyDownload: realm : execution error occurred");
        } catch (InterruptedException ex) {
            logger.severe("zeroCopyDownload: realm : the request was interrupted");
        } catch (IOException ex) {
            logger.severe("zeroCopyDownload: realm : IOException was thrown");
        }
    }

    /**
     * 
     * @param url
     * @param filename
     */
    public void zeroCopyDownload(String url, String filename) {
        Builder builder = new AsyncHttpClientConfig.Builder();
        Realm realm = new Realm.RealmBuilder().setPrincipal(saasBirtviewerAccessAccount).setPassword(saasBirtviewerAccessPassword).setUsePreemptiveAuth(true).setScheme(AuthScheme.BASIC).build();
        builder.setRealm(realm).build();
        AsyncHttpClient httpClient = new AsyncHttpClient(builder.build());
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
            Future<String> future = httpClient.prepareGet(url).execute(new SaasAsyncHandlerImpl(bos));
            while (future.isDone()) {
                logger.log(Level.INFO, "please wait while getting the requeste file downloaded ... ");
            }
            logger.log(Level.INFO, "future={0}", future.get());
            bos.close();
            httpClient.close();
        } catch (ExecutionException ex) {
            logger.severe("zeroCopyDownload: execution error occurred");
        } catch (InterruptedException ex) {
            logger.severe("zeroCopyDownload: the request was interrupted");
        } catch (IOException ex) {
            logger.severe("zeroCopyDownload: IOException was thrown");
        }
    }

    private class SaasAsyncHandlerImpl implements AsyncHandler<String> {

        OutputStream output;

        public SaasAsyncHandlerImpl(OutputStream outputStream) {
            output = outputStream;
        }

        @Override
        public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) {
            try {
                bodyPart.writeTo(output);
            } catch (IOException ex) {
                logger.severe("IOException was thrown");
            }
            return STATE.CONTINUE;
        }

        @Override
        public STATE onStatusReceived(HttpResponseStatus hrs) throws Exception {
            logger.log(Level.INFO, hrs.getStatusText());
            return STATE.CONTINUE;
        }

        @Override
        public STATE onHeadersReceived(HttpResponseHeaders hrh) throws Exception {
            FluentCaseInsensitiveStringsMap headers = hrh.getHeaders();
            Set<String> keySet = headers.keySet();
            if (logger.isLoggable(Level.INFO)) {
                for (String key : keySet) {
                    logger.log(Level.INFO, "key:{0}=value:{1}", new Object[] { key, headers.get(key) });
                }
            }
            return STATE.CONTINUE;
        }

        @Override
        public String onCompleted() throws Exception {
            return "COMPLETED";
        }

        @Override
        public void onThrowable(Throwable t) {
            logger.severe("exception was thrown");
        }
    }
}
