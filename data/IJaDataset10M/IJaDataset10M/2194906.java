package com.dyuproject.demos.oauthconsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dyuproject.oauth.Constants;
import com.dyuproject.oauth.Consumer;
import com.dyuproject.oauth.ConsumerContext;
import com.dyuproject.oauth.Endpoint;
import com.dyuproject.oauth.HttpAuthTransport;
import com.dyuproject.oauth.Token;
import com.dyuproject.oauth.TokenExchange;
import com.dyuproject.oauth.Transport;
import com.dyuproject.util.http.HttpConnector;
import com.dyuproject.util.http.UrlEncodedParameterMap;
import com.dyuproject.util.http.HttpConnector.Parameter;
import com.dyuproject.util.http.HttpConnector.Response;

/**
 * @author David Yu
 * @created Jun 15, 2009
 */
@SuppressWarnings("serial")
public class LocalContactsServlet extends HttpServlet {

    static final String CONTACTS_SERVICE_URL = "http://localhost/services/contacts/";

    Consumer _consumer = Consumer.getInstance();

    Endpoint _localEndpoint = _consumer.getEndpoint("localhost");

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Token token = _consumer.getToken(_localEndpoint.getConsumerKey(), request);
        switch(token.getState()) {
            case Token.UNITIALIZED:
                UrlEncodedParameterMap params = new UrlEncodedParameterMap().add(Constants.OAUTH_CALLBACK, request.getRequestURL().toString());
                Response r = _consumer.fetchToken(_localEndpoint, params, TokenExchange.REQUEST_TOKEN, token);
                if (r.getStatus() == 200 && token.getState() == Token.UNAUTHORIZED) {
                    _consumer.saveToken(token, request, response);
                    StringBuilder urlBuffer = Transport.buildAuthUrl(_localEndpoint.getAuthorizationUrl(), token);
                    response.sendRedirect(urlBuffer.toString());
                }
                break;
            case Token.UNAUTHORIZED:
                if (token.authorize(request.getParameter(Constants.OAUTH_TOKEN), request.getParameter(Constants.OAUTH_VERIFIER))) {
                    if (fetchAccessToken(token, request, response)) queryLocalContacts(token, request, response); else _consumer.saveToken(token, request, response);
                }
                break;
            case Token.AUTHORIZED:
                if (fetchAccessToken(token, request, response)) queryLocalContacts(token, request, response);
                break;
            case Token.ACCESS_TOKEN:
                queryLocalContacts(token, request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }

    public boolean fetchAccessToken(Token token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UrlEncodedParameterMap params = new UrlEncodedParameterMap();
        Response r = _consumer.fetchToken(_localEndpoint, params, TokenExchange.ACCESS_TOKEN, token);
        if (r.getStatus() == 200 && token.getState() == Token.ACCESS_TOKEN) {
            _consumer.saveToken(token, request, response);
            return true;
        }
        return false;
    }

    protected void queryLocalContacts(Token token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Response r = serviceGET(CONTACTS_SERVICE_URL, _consumer.getConsumerContext(), _localEndpoint, token, request, response);
        BufferedReader br = new BufferedReader(new InputStreamReader(r.getInputStream(), "UTF-8"));
        response.setContentType("text/xml");
        PrintWriter pw = response.getWriter();
        for (String line = null; (line = br.readLine()) != null; ) pw.append(line);
    }

    public static Response serviceGET(String serviceUrl, ConsumerContext context, Endpoint ep, Token token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpConnector connector = context.getHttpConnector();
        UrlEncodedParameterMap params = new UrlEncodedParameterMap(serviceUrl);
        context.getNonceAndTimestamp().put(params, token.getCk());
        Parameter authorization = new Parameter("Authorization", HttpAuthTransport.getAuthHeaderValue(params, ep, token, context.getNonceAndTimestamp(), ep.getSignature()));
        return connector.doGET(params.getUrl(), authorization);
    }
}
