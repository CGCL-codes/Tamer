package org.encuestame.oauth1.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Request factory that signs RestTemplate requests with an OAuth 1 Authorization header.
 * Internally used for Spring 3.0 compatibility only.
 * @author Picado, Juan juanATencuestame.org
 * @since
 */
class OAuth1RequestFactory implements ClientHttpRequestFactory {

    private final ClientHttpRequestFactory delegate;

    private final String consumerKey;

    private final String consumerSecret;

    private final String accessToken;

    private final String accessTokenSecret;

    public OAuth1RequestFactory(ClientHttpRequestFactory delegate, String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        this.delegate = delegate;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
    }

    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        return new OAuth1SigningRequest(delegate.createRequest(uri, httpMethod), consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

    private static class OAuth1SigningRequest implements ClientHttpRequest {

        private final ClientHttpRequest delegate;

        private ByteArrayOutputStream bodyOutputStream;

        private final String consumerKey;

        private final String consumerSecret;

        private final String accessToken;

        private final String accessTokenSecret;

        public OAuth1SigningRequest(ClientHttpRequest delegate, String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
            this.delegate = delegate;
            this.consumerKey = consumerKey;
            this.consumerSecret = consumerSecret;
            this.accessToken = accessToken;
            this.accessTokenSecret = accessTokenSecret;
            this.bodyOutputStream = new ByteArrayOutputStream();
        }

        public ClientHttpResponse execute() throws IOException {
            byte[] bufferedOutput = bodyOutputStream.toByteArray();
            String authorizationHeader = OAuth1Utils.spring30buildAuthorizationHeaderValue(this, bufferedOutput, consumerKey, consumerSecret, accessToken, accessTokenSecret);
            delegate.getBody().write(bufferedOutput);
            delegate.getHeaders().set("Authorization", authorizationHeader);
            return delegate.execute();
        }

        public URI getURI() {
            return delegate.getURI();
        }

        public HttpMethod getMethod() {
            return delegate.getMethod();
        }

        public HttpHeaders getHeaders() {
            return delegate.getHeaders();
        }

        public OutputStream getBody() throws IOException {
            return bodyOutputStream;
        }
    }
}
