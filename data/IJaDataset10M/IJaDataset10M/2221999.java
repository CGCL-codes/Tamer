package net.taylor.jrs;

import static org.jboss.resteasy.util.HttpHeaderNames.CONTENT_TYPE;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import net.taylor.inject.Locator;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse.BaseClientResponseStreamFactory;
import org.jboss.resteasy.client.core.executors.URLConnectionHeaderWrapper;
import org.jboss.resteasy.util.CaseInsensitiveMap;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.log.Log;

/**
 * JAX-RS client factory
 * 
 * @author jgilbert01
 */
@Scope(ScopeType.EVENT)
@Install(value = false, precedence = Install.BUILT_IN)
@AutoCreate
@BypassInterceptors
public class ClientFactory {

    @Logger
    protected Log log;

    protected Object client;

    protected String componentName;

    private Class<?> serviceEndpointInterface;

    private ValueExpression<String> endpointAddress;

    private Map<String, ValueExpression<String>> headers = new HashMap<String, ValueExpression<String>>();

    private ValueExpression<SSLSocketFactory> sslSocketFactory;

    private ValueExpression<Boolean> useMock;

    private ValueExpression<String> mockName;

    public Class<?> getServiceEndpointInterface() {
        return serviceEndpointInterface;
    }

    public void setServiceEndpointInterface(Class<?> serviceEndpointInterface) {
        this.serviceEndpointInterface = serviceEndpointInterface;
    }

    public ValueExpression<String> getEndpointAddress() {
        return endpointAddress;
    }

    public void setEndpointAddress(ValueExpression<String> endpointAddress) {
        this.endpointAddress = endpointAddress;
    }

    public Map<String, ValueExpression<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, ValueExpression<String>> headers) {
        this.headers = headers;
    }

    public ValueExpression<SSLSocketFactory> getSslSocketFactory() {
        return sslSocketFactory;
    }

    public void setSslSocketFactory(ValueExpression<SSLSocketFactory> sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public ValueExpression<String> getMockName() {
        return mockName;
    }

    public void setMockName(ValueExpression<String> mockName) {
        this.mockName = mockName;
    }

    public ValueExpression<Boolean> getUseMock() {
        return useMock;
    }

    public void setUseMock(ValueExpression<Boolean> useMock) {
        this.useMock = useMock;
    }

    protected boolean isMock() {
        return getUseMock() != null && getMockName() != null && getUseMock().getValue();
    }

    @Create
    public void create(Component component) throws Exception {
        componentName = component.getName();
        log.info("Creating #0", this);
        if (isMock()) {
            client = getMock();
        } else {
            client = getClient(endpointAddress.getValue());
        }
    }

    protected Object getMock() {
        log.debug("Using Mock: #0, for #1", getMockName().getValue(), componentName);
        return Locator.getInstance(getMockName().getValue());
    }

    protected Object getClient(String endpointAddress) {
        return ProxyFactory.create(serviceEndpointInterface, endpointAddress, new RestClientExecutor(headers, sslSocketFactory));
    }

    @Unwrap
    public Object getClient() {
        return client;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public static class RestClientExecutor implements ClientExecutor {

        private Map<String, ValueExpression<String>> requestHeaders;

        private ValueExpression<SSLSocketFactory> sslSocketFactory;

        public RestClientExecutor(Map<String, ValueExpression<String>> requestHeaders, ValueExpression<SSLSocketFactory> sslSocketFactory) {
            this.requestHeaders = requestHeaders;
            this.sslSocketFactory = sslSocketFactory;
        }

        public ClientResponse execute(ClientRequest request) throws Exception {
            String uri = request.getUri();
            String httpMethod = request.getHttpMethod();
            HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
            if (connection instanceof HttpsURLConnection) {
                if (sslSocketFactory != null) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory.getValue());
                }
            }
            if (requestHeaders != null) {
                for (Map.Entry<String, ValueExpression<String>> headerEntry : requestHeaders.entrySet()) {
                    connection.addRequestProperty(headerEntry.getKey(), headerEntry.getValue().getValue());
                }
            }
            connection.setRequestMethod(httpMethod);
            setupRequest(request, connection);
            return execute(request, connection);
        }

        protected void setupRequest(ClientRequest request, HttpURLConnection connection) throws ProtocolException {
            boolean isGet = "GET".equals(request.getHttpMethod());
            connection.setInstanceFollowRedirects(isGet && request.followRedirects());
            connection.setDoOutput(request.getBody() != null || !request.getFormParameters().isEmpty());
            setHeaders(request, connection);
            if (request.getBody() != null && !request.getFormParameters().isEmpty()) throw new RuntimeException("You cannot send both form parameters and an entity body");
            if (!request.getFormParameters().isEmpty()) {
                throw new RuntimeException("URLConnectionClientExecutor doesn't support form parameters yet");
            }
        }

        protected void setHeaders(ClientRequest request, HttpURLConnection connection) {
            for (Entry<String, List<String>> entry : request.getHeaders().entrySet()) {
                String value = null;
                if (entry.getValue().size() == 1) value = entry.getValue().get(0); else {
                    StringBuilder b = new StringBuilder();
                    String add = "";
                    for (String v : entry.getValue()) {
                        b.append(add).append(v);
                        add = ",";
                    }
                    value = b.toString();
                }
                connection.addRequestProperty(entry.getKey(), value);
            }
        }

        public ClientRequest createRequest(String uriTemplate) {
            return new ClientRequest(uriTemplate, this);
        }

        public ClientRequest createRequest(UriBuilder uriBuilder) {
            return new ClientRequest(uriBuilder, this);
        }

        protected ClientResponse execute(ClientRequest request, final HttpURLConnection connection) throws IOException {
            outputBody(request, connection);
            final int status = connection.getResponseCode();
            BaseClientResponse response = new BaseClientResponse(new BaseClientResponseStreamFactory() {

                public InputStream getInputStream() throws IOException {
                    return (status < 300) ? connection.getInputStream() : connection.getErrorStream();
                }

                public void performReleaseConnection() {
                    try {
                        getInputStream().close();
                    } catch (IOException e) {
                    }
                    connection.disconnect();
                }
            }, this);
            response.setProviderFactory(request.getProviderFactory());
            response.setStatus(status);
            response.setHeaders(getHeaders(connection));
            return response;
        }

        protected MultivaluedMap<String, String> getHeaders(final HttpURLConnection connection) {
            MultivaluedMap<String, String> headers = new CaseInsensitiveMap<String>();
            for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                if (header.getKey() != null) {
                    for (String value : header.getValue()) {
                        headers.add(header.getKey(), value);
                    }
                }
            }
            return headers;
        }

        protected MultivaluedMap<String, String> outputBody(ClientRequest request, HttpURLConnection connection) {
            if (request.getBody() != null) {
                if (connection.getRequestProperty(CONTENT_TYPE) == null) {
                    String type = request.getBodyContentType().toString();
                    connection.addRequestProperty(CONTENT_TYPE, type);
                }
                MultivaluedMap headers = new URLConnectionHeaderWrapper(connection, request.getProviderFactory());
                try {
                    OutputStream os = connection.getOutputStream();
                    request.writeRequestBody(headers, os);
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return headers;
            } else {
                return null;
            }
        }
    }
}
