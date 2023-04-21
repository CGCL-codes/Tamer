package orxatas.travelme.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

public class SyncInternetConnection {

    /**
	 * URL de la petición.
	 * */
    private final String url;

    private String urlFormated;

    private List<NameValuePair> GETparamList = null;

    private List<NameValuePair> POSTparamList = null;

    public SyncInternetConnection(String url) {
        this.url = url;
    }

    /**
	 * Establece los parámetros GET de la petición.
	 * */
    public void addGETParams(List<NameValuePair> paramList) {
        GETparamList = paramList;
    }

    /**
	 * Establece los parámetros POST de la petición.
	 * */
    public void addPOSTParams(List<NameValuePair> paramList) {
        POSTparamList = paramList;
    }

    /**
	 * Formatea la petición y su URL.
	 * */
    private void formatCall() {
        if (GETparamList != null && GETparamList.size() > 0) {
            if (!url.endsWith("?")) urlFormated = url + "?";
            urlFormated += URLEncodedUtils.format(GETparamList, "utf-8");
        }
        Log.d("URLWS", urlFormated);
    }

    private String processAnswer(HttpResponse response) {
        if (response != null) {
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream stream;
                    stream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    stream.close();
                    String responseString = sb.toString();
                    return responseString;
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String send() {
        formatCall();
        HttpUriRequest request;
        if (POSTparamList != null && POSTparamList.size() > 0) {
            HttpPost r = new HttpPost(urlFormated);
            try {
                r.setEntity(new UrlEncodedFormEntity(POSTparamList));
            } catch (UnsupportedEncodingException e) {
                Log.e("HTTPPOST", "Error en la codificación de los parámetros.");
                e.printStackTrace();
            }
            request = r;
        } else {
            HttpGet r = new HttpGet(urlFormated);
            request = r;
        }
        try {
            HttpResponse response = new DefaultHttpClient().execute(request);
            return processAnswer(response);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
