package net.unto.twitter.methods;

import java.util.ArrayList;
import java.util.List;
import net.unto.twitter.Api;
import net.unto.twitter.HttpManager;
import net.unto.twitter.UrlUtil;
import net.unto.twitter.UtilProtos.Url;
import net.unto.twitter.UtilProtos.Url.Part;

public abstract class AbstractRequest implements Request {

    public abstract static class Builder<BuilderType extends Builder<?>> implements Request.Builder {

        boolean authorizationRequired = false;

        String host = Api.DEFAULT_HOST;

        HttpManager httpManager;

        List<Url.Parameter> parameters = new ArrayList<Url.Parameter>();

        List<Url.Part> parts = new ArrayList<Url.Part>();

        String path = null;

        int port = Api.DEFAULT_PORT;

        Url.Scheme scheme = Api.DEFAULT_SCHEME;

        @SuppressWarnings("unchecked")
        BuilderType authorizationRequired(boolean authorizationRequired) {
            this.authorizationRequired = authorizationRequired;
            return (BuilderType) this;
        }

        /**
     * Sets the host (e.g., "twitter.com") for this Twitter API call.
     * 
     * @param host the host (e.g., "twitter.com") for Twitter API call.
     * @return This {@link Builder} instance.
     */
        @SuppressWarnings("unchecked")
        public BuilderType host(String host) {
            this.host = host;
            return (BuilderType) this;
        }

        /**
     * Sets the HttpManager for this Twitter API call.
     * 
     * @param httpManager the HttpManager for Twitter API call.
     * @return This {@link Builder} instance.
     */
        @SuppressWarnings("unchecked")
        public BuilderType httpManager(HttpManager httpManager) {
            this.httpManager = httpManager;
            return (BuilderType) this;
        }

        @SuppressWarnings("unchecked")
        BuilderType parameter(String name, String value) {
            assert (name != null);
            assert (name.length() > 0);
            assert (value != null);
            Url.Parameter parameter = Url.Parameter.newBuilder().setName(name).setValue(value).build();
            parameters.add(parameter);
            return (BuilderType) this;
        }

        @SuppressWarnings("unchecked")
        BuilderType part(Part part) {
            assert (part != null);
            parts.add(part);
            return (BuilderType) this;
        }

        @SuppressWarnings("unchecked")
        BuilderType path(String path) {
            this.path = path;
            return (BuilderType) this;
        }

        /**
     * Sets the port (e.g., 80) for this Twitter API call.
     * 
     * @param port the port (e.g., 80) for this Twitter API call.
     * @return This {@link Builder} instance.
     */
        @SuppressWarnings("unchecked")
        public BuilderType port(int port) {
            this.port = port;
            return (BuilderType) this;
        }

        /**
     * Sets the http scheme (e.g., Url.Scheme.HTTP) for this Twitter API call.
     * 
     * @param scheme the http scheme (e.g., Url.Scheme.HTTP) for this Twitter
     *        API call.
     * @return This {@link Builder} instance.
     */
        @SuppressWarnings("unchecked")
        public BuilderType scheme(Url.Scheme scheme) {
            this.scheme = scheme;
            return (BuilderType) this;
        }
    }

    HttpManager httpManager;

    Url url;

    AbstractRequest(Builder<?> builder) {
        assert (builder.path != null);
        assert (builder.host != null);
        assert (builder.port > 0);
        assert (builder.scheme != null);
        assert (builder.parameters != null);
        assert (builder.parts != null);
        httpManager = builder.httpManager == null ? Api.DEFAULT_HTTP_MANAGER : builder.httpManager;
        if (builder.authorizationRequired && !httpManager.hasCredentials()) {
            throw new IllegalStateException("Authorization required.");
        }
        url = Url.newBuilder().setScheme(builder.scheme).setHost(builder.host).setPort(builder.port).setPath(builder.path).addAllParameters(builder.parameters).addAllParts(builder.parts).build();
    }

    String getJson() {
        return httpManager.get(url);
    }

    String postJson() {
        return httpManager.post(url);
    }

    @Override
    public final String toString() {
        return UrlUtil.assemble(url);
    }

    public final Url toUrl() {
        return url;
    }
}
