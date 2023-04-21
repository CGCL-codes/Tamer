package org.snsmeet.zxing.client.android.result.supplement;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import org.snsmeet.R;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.snsmeet.zxing.client.android.AndroidHttpClient;
import org.snsmeet.zxing.client.result.URIParsedResult;
import java.io.IOException;

final class URIResultInfoRetriever extends SupplementalInfoRetriever {

    private static final String[] REDIRECTOR_HOSTS = { "http://bit.ly/", "http://tinyurl.com/", "http://tr.im/", "http://goo.gl/", "http://ow.ly/" };

    private final URIParsedResult result;

    private final String redirectString;

    URIResultInfoRetriever(TextView textView, URIParsedResult result, Handler handler, Context context) {
        super(textView, handler, context);
        redirectString = context.getString(R.string.msg_redirect);
        this.result = result;
    }

    @Override
    void retrieveSupplementalInfo() throws IOException, InterruptedException {
        String oldURI = result.getURI();
        String newURI = unredirect(oldURI);
        int count = 0;
        while (count < 3 && !oldURI.equals(newURI)) {
            append(redirectString + ": " + newURI);
            count++;
            oldURI = newURI;
            newURI = unredirect(newURI);
        }
        setLink(newURI);
    }

    private static String unredirect(String uri) throws IOException {
        if (!isRedirector(uri)) {
            return uri;
        }
        HttpUriRequest head = new HttpHead(uri);
        AndroidHttpClient client = AndroidHttpClient.newInstance(null);
        HttpResponse response = client.execute(head);
        int status = response.getStatusLine().getStatusCode();
        if (status == 301 || status == 302) {
            Header redirect = response.getFirstHeader("Location");
            if (redirect != null) {
                String location = redirect.getValue();
                if (location != null) {
                    return location;
                }
            }
        }
        return uri;
    }

    private static boolean isRedirector(String uri) {
        for (String redirectorHost : REDIRECTOR_HOSTS) {
            if (uri.startsWith(redirectorHost)) {
                return true;
            }
        }
        return false;
    }
}
