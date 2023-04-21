package no.sws.client;

import java.io.IOException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author Pål Orby, SendRegning AS @ Deprecated - SendRegning Web
 *         Services uses BASIC authentication and this was for the FORM
 *         authentication.
 */
public class SwsLogin {

    public static String LOGIN_URL;

    private final String FORM_POST_URL;

    private static final String LOGIN_ERROR_STRING = "SwsLoginErrorPage";

    public SwsLogin() {
        SwsLogin.LOGIN_URL = "https://www.sendregning.no/sws/";
        this.FORM_POST_URL = "https://www.sendregning.no/sws/j_security_check";
    }

    /**
	 * For internal testing
	 * 
	 * @param domainName
	 *            - the domain name of the url, e.g. "www.sendregning.no" or
	 *            "localhost:8443" for testing locally
	 */
    public SwsLogin(final String domainName) {
        if (domainName == null || domainName.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter domainName can't be null or an empty String");
        }
        SwsLogin.LOGIN_URL = "https://" + domainName + "/sws/";
        this.FORM_POST_URL = "https://" + domainName + "/sws/j_security_check";
    }

    public HttpClient login(final String username, final String password) throws HttpException, IOException {
        final HttpClient httpClient = new HttpClient();
        final GetMethod loggInnSide = new GetMethod(SwsLogin.LOGIN_URL);
        loggInnSide.setFollowRedirects(true);
        httpClient.executeMethod(loggInnSide);
        loggInnSide.releaseConnection();
        final PostMethod formPost = new PostMethod(this.FORM_POST_URL);
        formPost.addParameter("Referer", SwsLogin.LOGIN_URL);
        formPost.addParameter("j_username", username);
        formPost.addParameter("j_password", password);
        final int formResponseCode = httpClient.executeMethod(formPost);
        final String formResponse = formPost.getResponseBodyAsString();
        System.out.println("Response code=" + formResponseCode + "\n" + formResponse);
        final Header videreSend = formPost.getResponseHeader("Location");
        formPost.releaseConnection();
        if (videreSend == null || formResponse.contains(SwsLogin.LOGIN_ERROR_STRING)) {
            return null;
        }
        final GetMethod redirect = new GetMethod(videreSend.getValue());
        redirect.setFollowRedirects(true);
        final int redirectResponseCode = httpClient.executeMethod(redirect);
        final String redirectResponse = redirect.getResponseBodyAsString();
        System.out.println("Redirect response code=" + redirectResponseCode + "\n" + redirectResponse);
        return httpClient;
    }

    public static void main(final String[] args) throws Exception {
        final SwsLogin sws = new SwsLogin();
        sws.login(args[0], args[1]);
    }
}
