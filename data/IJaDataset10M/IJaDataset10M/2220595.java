package juploader.httpclient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import java.net.URISyntaxException;

/**
 * Żądanie HTTP pozwalające na pobranie zawartości strony (formalnie, dowolnego
 * zasobu) metodą GET. Uwaga: do pobierania większych zasobów należy skorzystać
 * z żądania {@link FileDownloadRequest}, które można przerwać oraz informuje o
 * postępach.
 *
 * @author Adam Pawelec
 */
public class GetPageRequest extends AbstractHttpRequest {

    GetPageRequest(DefaultHttpClient httpClient, HttpContext context) {
        super(httpClient, context);
    }

    public GetPageRequest() {
        super();
    }

    @Override
    protected HttpUriRequest createRequest() throws URISyntaxException {
        return new HttpGet(url.toURI());
    }
}
