package com.thoughtworks.blipit.panicblip.utils;

import android.util.Log;
import com.thoughtworks.blipit.types.Blip;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.APP_TAG;

public class PanicBlipHttpHelper {

    private static PanicBlipHttpHelper instance = new PanicBlipHttpHelper();

    private static String USER_AGENT = "PanicBlip/1.0";

    private static String JSON_CONTENT_TYPE = "application/json";

    private static final int BUFFER_SIZE = 8 * 1024;

    private final HttpClient httpClient;

    public static PanicBlipHttpHelper getInstance() {
        return instance;
    }

    private PanicBlipHttpHelper() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUserAgent(params, USER_AGENT);
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpClientParams.setRedirecting(params, false);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);
        httpClient = new DefaultHttpClient(manager, params);
    }

    public String getAllChannelsAsJson(String blipItSvcHost) {
        HttpEntity httpEntity = null;
        String channelsJson = null;
        try {
            String blipItSvcUrl = String.format("http://%s/blipit/panic/channel", blipItSvcHost);
            HttpGet httpGet = new HttpGet(blipItSvcUrl);
            httpGet.addHeader("Content-Type", JSON_CONTENT_TYPE);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            InputStream content = httpEntity.getContent();
            channelsJson = convertStreamToString(content);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return channelsJson;
    }

    public Blip savePanic(String blipItSvcHost, Blip panic) {
        Blip savedPanic = null;
        HttpEntity httpEntity = null;
        try {
            String panicJson = PanicBlipUtils.toPanicJson(panic);
            String blipItSvcUrl = String.format("http://%s/blipit/panic/blip", blipItSvcHost);
            StringEntity stringEntity = new StringEntity(panicJson, "UTF-8");
            stringEntity.setContentType(JSON_CONTENT_TYPE);
            HttpPost httpPost = new HttpPost(blipItSvcUrl);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            InputStream content = httpEntity.getContent();
            savedPanic = PanicBlipUtils.toPanic(convertStreamToString(content));
        } catch (UnsupportedEncodingException e) {
            logError(e);
        } catch (ClientProtocolException e) {
            logError(e);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return savedPanic;
    }

    public boolean deletePanic(String blipItSvcHost, Blip panic) {
        boolean result = false;
        HttpEntity httpEntity = null;
        try {
            String panicId = panic.getKey().getId();
            String blipItSvcUrl = String.format("http://%s/blipit/panic/blip/%s", blipItSvcHost, panicId);
            HttpDelete httpDelete = new HttpDelete(blipItSvcUrl);
            HttpResponse httpResponse = httpClient.execute(httpDelete);
            httpEntity = httpResponse.getEntity();
            String response = convertStreamToString(httpEntity.getContent());
            result = response.toLowerCase().contains("success");
        } catch (ClientProtocolException e) {
            logError(e);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return result;
    }

    private void closeEntity(HttpEntity httpEntity) {
        if (httpEntity != null) {
            try {
                httpEntity.consumeContent();
            } catch (IOException e) {
                logError(e);
            }
        }
    }

    private String convertStreamToString(InputStream strStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(strStream), BUFFER_SIZE);
        StringBuilder buffer = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
        } catch (IOException e) {
            logError(e);
        } finally {
            try {
                strStream.close();
            } catch (IOException e) {
                logError(e);
            }
        }
        String response = buffer.toString();
        Log.i(APP_TAG, response);
        return response;
    }

    private void logError(Exception e) {
        Log.e(APP_TAG, e.getMessage(), e);
    }
}
