package org.aha.mf4j;

import static org.aha.mf4j.FlickrUrls.AUTH_URL;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import org.aha.mf4j.reflection.FlickrMethod;
import org.aha.mf4j.reflection.Reflection;
import org.aha.mf4j.response.FlickrObject;
import org.aha.mf4j.util.CommandLineParser;
import org.aha.mf4j.util.MD5;

/**
 * <p>
 *  Represents a Flickr session.
 * </p>
 * @author Arne Halvorsen (aha42)
 */
public final class FlickrSession {

    private String m_apiKey;

    private String m_secret;

    private ReflectionImpl m_reflection;

    private String m_frob = null;

    private String m_token = null;

    private String m_perms = "none";

    private FlickrObject m_user = null;

    private Authenticator m_authenticator = SimpleAuthenticator.INSTANCE;

    private boolean m_authenticated = false;

    /**
   * <p>
   *   Constructor.
   * </p>
   * <p>
   *   This constructor should only be used when authentication and signed
   *   request are not required.
   * </p>
   * @param apiKey Application key.      
   * @throws IOException If fails.
   */
    public FlickrSession(String apiKey) throws FlickrException {
        init(apiKey, null);
    }

    /**
   * <p>
   *   Constructor.
   * </p>
   * @param apiKey Application key.
   * @param secret Shared secret for signing requests.
   * @throws IOException If fails.
   */
    public FlickrSession(String apiKey, String secret) throws FlickrException {
        if (secret == null) {
            throw new NullPointerException("secret");
        }
        init(apiKey, secret);
    }

    /**
   * <p>
   *   Creates from parsed command line arguments.
   * </p>
   * <p>
   *   Accepting {@code api_key} and {@code secret} from command line is 
   *   exactly not what a real application should <b>not</b> do so 
   *   this constructor is strictly a convenient method for 
   *   test/demo/development purposes.
   * </p>
   * <table border='1'>
   *   
   *   <tr><th>Option</th><th>Description</th><th>Mandatory</th></tr>
   *   <tr>
   *     <td>-key <api_key></td>
   *     <td>
   *       Application key.
   *     </td>
   *     <td>Yes</td>
   *   </tr>
   *   
   *   <tr>
   *     <td>-key <secret></td>
   *     <td>Secret needed for authentication.</td>
   *     <td>
   *       Only if authentication is required, if missing construct not 
   *       authenticated session.
   *     </td>
   *   
   *   </tr>
   * </table>
   * @param clp Parsed command line arguments, see table above for accepted
   *            options.
   * @throws IOException If fails.
   */
    public FlickrSession(CommandLineParser clp) throws FlickrException {
        if (clp == null) {
            throw new NullPointerException("clp");
        }
        String apiKey = clp.value("key");
        if (apiKey == null) {
            throw new IllegalArgumentException("Missing api key (no key option)");
        }
        init(apiKey, clp.value("-secret"));
    }

    private void init(String apiKey, String secret) throws FlickrException {
        if (apiKey == null) {
            throw new NullPointerException("apiKey");
        }
        m_apiKey = apiKey;
        m_secret = secret;
        if (m_secret != null) {
            FlickrObject fo = new Request(m_secret, null).setApiKey(apiKey).setMethod("flickr.auth.getFrob").perform();
            FlickrObject frobFo = fo.getMandatoryChild("frob");
            m_frob = frobFo.getValue();
        }
        m_reflection = new ReflectionImpl(this);
    }

    /**
   * <p>
   *   Gets the Flickr API key used for this communication.
   * </p>
   * @return Key.
   */
    String getApiKey() {
        return m_apiKey;
    }

    /**
   * <p>
   *   Gets URL specification to where to go for end user to authorize access.
   * </p>
   * @param perms Permissions requested.
   * @return URL specification.
   * @throws IllegalArgumentException If {@code perms} not represents 
   *         permissions.
   * @throws IllegalStateException If {@code this} been created with no secret
   *         which is needed if to authentication. 
   */
    public String getLoginUrlSpec(String perms) {
        if (perms == null) {
            throw new NullPointerException("permission");
        }
        if (m_secret == null) {
            throw new IllegalStateException("can not authenticate: has no secret");
        }
        Permissions.checkPermission(perms);
        StringBuilder sb = new StringBuilder();
        String signatureString = sb.append(m_secret).append("api_key").append(m_apiKey).append("frob").append(m_frob).append("perms").append(perms).toString();
        String signature = MD5.compute(signatureString);
        sb.setLength(0);
        return sb.append(AUTH_URL).append('?').append("api_key").append('=').append(m_apiKey).append('&').append("perms").append('=').append(perms.toString()).append('&').append("frob").append('=').append(m_frob).append('&').append("api_sig").append('=').append(signature).toString();
    }

    /**
   * <p>
   *   Sets 
   *   {@link Authenticator} to use.
   * </p>
   * @param authenticator {@code Authenticator}.
   */
    public void setAuthenticator(Authenticator authenticator) {
        if (authenticator == null) {
            throw new NullPointerException("authenticator");
        }
        m_authenticator = authenticator;
    }

    /**
   * <p>
   *   Performs authentication using the registered
   *   {@link Authenticator}.
   * </p>
   * <p>
   *   Request {@code read} permission only.
   * </p>
   * @return {@code true} if end user signals has authenticated or {@code false}
   *         if did not.
   * @throws IOException If fails.
   * @see #setAuthenticator(Authenticator).
   */
    public boolean authenticate() throws FlickrException {
        return authenticate("read");
    }

    /**
   * <p>
   *   Authenticate.
   * </p>
   * <p>
   *   How this is done is a function of the 
   *   {@link #setAuthenticator(Authenticator) registered}
   *   {@link Authenticator}.
   *   
   *   If no 
   *   {@code Authenticator} has been registered the default 
   *   {@link SimpleAuthenticator} is used.
   * </p>
   * @param perms Permissions requested.
   * @return {@code true} if end user signals has authenticated or {@code false}
   *         if did not.
   * @throws IllegalArgumentException If {@code perms} not represents 
   *         permissions or is {@code none}.         
   * @throws IllegalStateException If already authenticated.
   * @throws IllegalStateException If {@code this} been created with no secret
   *         which is needed if to authentication.   
   * @throws IOException If fails.
   */
    public boolean authenticate(String perms) throws FlickrException {
        if (perms == null) {
            throw new NullPointerException("perms");
        }
        if (m_authenticated) {
            throw new IllegalStateException("is already authenticated");
        }
        if (m_secret == null) {
            throw new IllegalStateException("can not authenticate: has no secret");
        }
        if (perms.equals("none")) {
            throw new IllegalArgumentException("perms are none");
        }
        Permissions.checkPermission(perms);
        if (m_authenticator.authenticate(this, perms)) {
            m_authenticated = true;
            getToken();
        }
        return m_authenticated;
    }

    /**
   * <p>
   *   Tells if {@code this} session is authenticated.
   * </p>
   * @return {@code true} if is, {@code false} if is not.
   */
    public boolean isAuthenticated() {
        return m_authenticated;
    }

    /**
   * <p>
   *   Gets session's token.
   * </p>
   * @return Token.
   * @throws IOException If fails.
   * @throws IllegalStateException If is not authenticated.
   */
    public String getToken() throws FlickrException {
        if (m_token == null) {
            if (!isAuthenticated()) {
                throw new IllegalStateException("is not authenticated");
            }
            if (m_secret == null) {
                throw new Error();
            }
            Request req = new Request(m_secret, null).setApiKey(m_apiKey);
            FlickrObject fo = req.setMethod("flickr.auth.getToken").setArg("frob", m_frob).perform();
            FlickrObject authFo = fo.getMandatoryChild("auth");
            FlickrObject tokenFo = authFo.getMandatoryChild("token");
            m_token = tokenFo.getValue();
            FlickrObject permsFo = authFo.getMandatoryChild("perms");
            m_perms = permsFo.getValue();
            m_user = authFo.getMandatoryChild("user");
        }
        return m_token;
    }

    /**
   * <p>
   *   Gets session's permissions.
   * </p>
   * @return Permissions.
   * @throws IOException If fails.
   */
    public String getPermissions() {
        return m_perms;
    }

    /**
   * <p>
   *   Gets user logged in.
   * </p>
   * @return {@link FlickrObject} representing parsed user element in XML 
   *         returned by the {@code flickr.auth.getToken}.
   * @throws IOException If fails.
   * @throws IllegalStateException If is not authenticated. 
   */
    public FlickrObject getUser() throws FlickrException {
        getToken();
        return m_user;
    }

    /**
   * <p>
   *   Gets the {@code nsid} for logged in user.
   * </p>
   * @return nsid.
   * @throws IOException If fails.
   * @throws IllegalStateException If is not authenticated. 
   */
    public String getNsid() throws FlickrException {
        return getUser().getAttribute("nsid");
    }

    /**
   * <p>
   *   Tells if is logged in.
   * </p>
   * @return {@code true} if is logged in, {@code false} if is not
   */
    public boolean isLoggedIn() {
        return m_token != null;
    }

    /**
   * <p>
   *   Tells if session has sufficient permissions to execute given method.
   * </p>
   * @param method Name of method.
   * @return {@code true} if has, {@code false} if has not.
   * @throws IllegalArgumentException If {@code name} is not a method.
   * @throws IOException If fails.
   */
    public boolean hasPermission(String method) throws FlickrException {
        if (method == null) {
            throw new NullPointerException("method");
        }
        if (!m_reflection.methodExists(method)) {
            throw new IllegalArgumentException("method : " + method + " does not exists");
        }
        FlickrMethod meta = m_reflection.getMethod(method);
        return meta.isRequiredPermission(m_perms);
    }

    /**
   * <p>
   *   Creates 
   *   {@link Request}.
   * </p>
   * @param method Name of method to create 
   *               {@link Request}.
   * @return Created.
   * @throws IOException If fails.
   */
    public Request createRequest(String method) throws FlickrException {
        if (method == null) {
            throw new NullPointerException("method");
        }
        if (m_reflection != null && !m_reflection.methodExists(method)) {
            throw new IllegalArgumentException("method : " + method + " does not exists");
        }
        FlickrMethod meta = m_reflection.getMethod(method);
        if (meta.getNeedsLogin() && m_token == null) {
            throw new IllegalArgumentException("method : " + method + " needs login and is not logged in");
        }
        if (!meta.isRequiredPermission(m_perms)) {
            throw new IllegalArgumentException("not sufficient permissions to execute method " + method + ", had : " + m_perms + ", needs : " + Permissions.toString(meta.getRequiredPermission()));
        }
        Request retVal = meta.getNeedsSigning() ? new Request(m_secret, m_token) : new Request();
        retVal.setMethod(method);
        retVal.setApiKey(m_apiKey);
        retVal.setMeta(meta);
        return retVal;
    }

    /**
   * <p>
   *   Gets object that provides Flickr API meta data.
   * </p>
   * @return {@link Reflection}.
   */
    public Reflection getReflection() {
        return m_reflection;
    }

    @Override
    public String toString() {
        CharArrayWriter caw = new CharArrayWriter();
        PrintWriter pw = new PrintWriter(caw);
        pw.println("permission : " + m_perms);
        pw.println("user : " + m_user);
        pw.println("authenticated : " + isAuthenticated());
        pw.flush();
        return caw.toString();
    }
}
