package au.edu.diasb.chico.config.mapper;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * This mapper class is designed for the task of determining the
 * base URL of a dannotate request mapped into the URI space of
 * some other site. 
 * 
 * @author scrawley
 */
public class RequestBaseUrlMapper implements RequestURIMapper {

    private List<String> baseUrls = Collections.emptyList();

    private NormalizeURIMapper normalizer = new NormalizeURIMapper();

    public final void setBaseUrls(List<String> baseUrls) {
        this.baseUrls = new ArrayList<String>(baseUrls.size() * 2);
        for (String url : baseUrls) {
            try {
                url = normalizer.mapURI(url);
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException("Invalid base url '" + url + "'", ex);
            }
            this.baseUrls.add(url);
            if (url.startsWith("http:")) {
                this.baseUrls.add("https:" + url.substring("http:".length()));
            } else if (url.startsWith("https:")) {
                this.baseUrls.add("http:" + url.substring("https:".length()));
            }
        }
    }

    /**
     * If there is a 'baseUrl' query parameter, its
     * value is returned.  Otherwise, the normalized version of the
     * URL is compared with the "http" and "https" versions of the
     * configured base URLs, and the first one that is a prefix of
     * the URL is returned.
     */
    @Override
    public String mapURI(HttpServletRequest request, String uri) throws URISyntaxException {
        String requestURL = uri;
        if (requestURL == null || requestURL.isEmpty()) {
            requestURL = request.getHeader("X-Original-URI");
            if (requestURL == null || requestURL.isEmpty()) {
                requestURL = request.getRequestURL().toString();
            }
        }
        String normalizedUri = normalizer.mapURI(requestURL);
        int queryPos = normalizedUri.indexOf('?');
        if (queryPos >= 0) {
            normalizedUri = normalizedUri.substring(0, queryPos);
        }
        for (String baseUrl : baseUrls) {
            if (normalizedUri.startsWith(baseUrl)) {
                return baseUrl;
            }
        }
        return "";
    }
}
