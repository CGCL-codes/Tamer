package com.cbsgmbh.xi.af.edifact.as2.request;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import com.cbsgmbh.xi.af.edifact.as2.Util;
import com.cbsgmbh.xi.af.edifact.jca.configuration.ConfigurationSettings;
import com.cbsgmbh.xi.af.trace.helpers.BaseTracer;
import com.cbsgmbh.xi.af.trace.helpers.BaseTracerSapImpl;
import com.cbsgmbh.xi.af.trace.helpers.Tracer;
import com.cbsgmbh.xi.af.trace.helpers.TracerCategories;
import com.tssap.dtr.client.lib.protocol.Connection;
import com.tssap.dtr.client.lib.protocol.HTTPException;
import com.tssap.dtr.client.lib.protocol.IResponse;
import com.tssap.dtr.client.lib.protocol.entities.ByteArrayEntity;
import com.tssap.dtr.client.lib.protocol.requests.http.PostRequest;
import com.tssap.dtr.client.lib.protocol.session.BasicAuthenticator;
import com.tssap.dtr.client.lib.protocol.session.SessionContext;
import com.tssap.dtr.client.lib.protocol.util.Query;

public class As2RequestSapImpl implements As2Request {

    private static final String VERSION_ID = "$Id://OPI2_EDIFACT_Adapter_Http/com/cbsgmbh/opi2/xi/af/edifact/as2/request/As2RequestSapImpl.java#1 $";

    private static final BaseTracer baseTracer = new BaseTracerSapImpl(VERSION_ID, TracerCategories.APP_ADAPTER_HTTP);

    protected Connection httpConnection = null;

    protected SessionContext httpSessionContext = null;

    protected final String url;

    public As2RequestSapImpl(String url) throws MalformedURLException {
        this.url = url;
        this.httpConnection = new Connection(url);
        this.httpSessionContext = new SessionContext();
    }

    public void enableBasicAuthentication(String user, String password) throws NoSuchAlgorithmException {
        this.httpSessionContext.setUser(user);
        this.httpSessionContext.setPassword(password);
        this.httpSessionContext.setAuthenticator(BasicAuthenticator.AUTH_SCHEME);
        this.httpConnection.setSessionContext(this.httpSessionContext);
    }

    public void enableProxy(String proxyHost, int proxyPort, String proxyUser, String proxyPassword) {
        this.httpConnection.setProxyHost(proxyHost);
        this.httpConnection.setProxyPort(proxyPort);
        this.httpConnection.setUseProxy(true);
        this.httpSessionContext.setProxyUser(proxyUser);
        this.httpSessionContext.setProxyPassword(proxyPassword);
        this.httpConnection.setSessionContext(this.httpSessionContext);
    }

    public void setEngineCertificateStores(String store) {
        this.httpSessionContext.certificates().setEngineCertificateStores(store, null);
    }

    public void close() {
        if (this.httpSessionContext != null) {
            this.httpSessionContext.closeSession();
        }
        if (this.httpConnection != null) {
            this.httpConnection.close();
        }
    }

    public As2Response send(ConfigurationSettings configuration, String url, String path, String query, String fromAS2Party, String toAS2Party, String messageId, ByteArrayEntity byteArrayEntity) throws Exception {
        final Tracer tracer = baseTracer.entering("send(ConfigurationSettings configuration, String url, String path, String query, String fromAS2Party, String toAS2Party, String messageId, ByteArrayEntity byteArrayEntity)");
        PostRequest httpRequest = createHttpRequest(configuration, url, path, query, fromAS2Party, toAS2Party, messageId);
        tracer.info("PostRequest httpRequest created with query : {0} ", httpRequest.getQueryString());
        httpRequest.setRequestEntity(byteArrayEntity);
        String strContentLengthRequest = String.valueOf(httpRequest.getContentLength());
        httpRequest.setHeader("Content-Length", strContentLengthRequest);
        tracer.info("httpRequest.setHeader(\"Content-Length\", {0})", strContentLengthRequest);
        tracer.info("PostRequest ByteArrayEntity assigned to httpRequest - content length : {0}", new Long(httpRequest.getContentLength()));
        tracer.info("PostRequest ByteArrayEntity assigned to httpRequest - content type : {0}", httpRequest.getContentType());
        IResponse resp = send(httpRequest);
        return new As2ResponseSapImpl(resp);
    }

    protected IResponse send(PostRequest httpRequest) throws IOException, HTTPException {
        return this.httpConnection.send(httpRequest);
    }

    protected PostRequest createHttpRequest(ConfigurationSettings configuration, String url, String path, String query, String fromParty, String toParty, String messageId) throws Exception {
        final Tracer tracer = baseTracer.entering("createHttpRequest(ConfigurationSettings configuration, String url, String path, String query, String fromParty, String toParty, String messageId)");
        final String CONTENT_TRANSFER_ENCODING_BINARY = "binary";
        final String CONTENT_DISP_ATTACHMENT = "attachment; filename=smime.p7m";
        com.tssap.dtr.client.lib.protocol.requests.http.PostRequest httpRequest = null;
        Query queryParam = null;
        StringTokenizer queryParameters = null;
        int numberOfParameters = -1;
        String parameterPair = null;
        int separator = -1;
        String dispOtions = null;
        String hostName = null;
        String date = null;
        String subject = null;
        boolean useEncryption = configuration.getUseEncryption();
        boolean useSignature = configuration.getUseSignature();
        String micAlgMdn = configuration.getMicAlgMdn();
        boolean mdnRequired = configuration.getMdnRequired();
        httpRequest = new PostRequest(path);
        tracer.info("PostRequest httpRequest created with path : {0}", httpRequest.getPath());
        httpRequest.setHeader("Connection", "keep-alive");
        tracer.info("httpRequest.setHeader(\"Connection\", \"keep-alive\")");
        if (query != null) {
            queryParam = new Query();
            queryParameters = new StringTokenizer(query, "&");
            numberOfParameters = queryParameters.countTokens();
            tracer.info("Query number of parameter pairs : {0} ", new Integer(numberOfParameters));
            for (int i = 0; i < numberOfParameters; i++) {
                parameterPair = (String) queryParameters.nextElement();
                tracer.info("Query query parameter {0} : {1}", new Object[] { new Integer((i + 1)), parameterPair });
                separator = parameterPair.indexOf("=");
                queryParam.appendQueryParameter(parameterPair.substring(0, separator), parameterPair.substring((separator + 1), parameterPair.length()));
            }
            httpRequest.setQuery(queryParam);
        }
        httpRequest.setHeader("AS2-From", fromParty);
        tracer.info("httpRequest.setHeader(\"AS2-From\", {0})", fromParty);
        httpRequest.setHeader("AS2-To", toParty);
        tracer.info("httpRequest.setHeader(\"AS2-To\", {0})", toParty);
        httpRequest.setHeader("AS2-Version", Util.AS2_VERSION_10);
        tracer.info("httpRequest.setHeader(\"AS2-Version\", {0})", Util.AS2_VERSION_10);
        httpRequest.setHeader("Message-ID", messageId);
        tracer.info("httpRequest.setHeader(\"Message-ID\", {0})", messageId);
        hostName = Util.getHostName();
        httpRequest.setHeader("Host", hostName);
        tracer.info("httpRequest.setHeader(\"Host\", {0})", hostName);
        httpRequest.setHeader("From", hostName);
        tracer.info("httpRequest.setHeader(\"From\", {0})", hostName);
        date = Util.getCurrentDate();
        httpRequest.setHeader("Date", date);
        tracer.info("httpRequest.setHeader(\"Date\", {0})", date);
        subject = "AS2 Message Request";
        httpRequest.setHeader("Subject", subject);
        tracer.info("httpRequest.setHeader(\"Subject\", {0})", subject);
        httpRequest.setHeader("MIME-Version", Util.MIME_VERSION_10);
        tracer.info("httpRequest.setHeader(\"MIME-Version\", {0});", Util.MIME_VERSION_10);
        httpRequest.setHeader("Recipient-Address", url);
        tracer.info("httpRequest.setHeader(\"Recipient-Address\", {0});", url);
        if (useEncryption) {
            httpRequest.setHeader("Content-Transfer-Encoding", CONTENT_TRANSFER_ENCODING_BINARY);
            tracer.info("httpRequest.setHeader(\"Content-Transfer-Encoding\", {0});", CONTENT_TRANSFER_ENCODING_BINARY);
            httpRequest.setHeader("Content-Disposition", CONTENT_DISP_ATTACHMENT);
            tracer.info("httpRequest.setHeader(\"Content-Disposition\", {0});", CONTENT_DISP_ATTACHMENT);
        }
        if (mdnRequired) {
            httpRequest.setHeader("Disposition-Notification-To", hostName);
            tracer.info("httpRequest.setHeader(\"Disposition-Notification-To\", {0})", hostName);
            if (useSignature) {
                dispOtions = "signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, " + micAlgMdn;
                httpRequest.setHeader("Disposition-Notification-Options", dispOtions);
                tracer.info("httpRequest.setHeader(\"Disposition-Notification-Options\", {0})", dispOtions);
            } else {
                tracer.info("No signature for MDN required. Disposition-Notification-Options not set.");
            }
        } else {
            tracer.info("No MDN required. EDIFACTRequest message field Disposition-Notification-To and Disposition-Notification-Options not set.");
        }
        tracer.leaving();
        return httpRequest;
    }
}
