package net.sf.jguard.core.authorization.permissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.Guard;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It represents the right to execute one or more actions (or methods for HTTP)
 * on one or more URLs via one or more protocols identified by their schemes.
 * this permission, <strong>only</strong> implies URLPermission.
 *
 * @author <a href="mailto:diabolo512@users.sourceforge.net">Charles Gay</a>
 * @see java.lang.IllegalArgumentException which wrap the
 * @see java.net.URISyntaxException thrown if the URI is not correct.
 */
public final class URLPermission extends java.security.BasicPermission implements Serializable, Cloneable, Comparable {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(URLPermission.class.getName());

    private static final String DELETE = "DELETE";

    private static final String GET = "GET";

    private static final String HEAD = "HEAD";

    private static final String OPTIONS = "OPTIONS";

    private static final String POST = "POST";

    private static final String PUT = "PUT";

    private static final String TRACE = "TRACE";

    private static final String ANY = "ANY";

    private static final String HTTP = "http";

    private static final String HTTPS = "https";

    /**
     * serial version id.
     */
    private static final long serialVersionUID = 3257283643243574067L;

    private Pattern pattern;

    /**
     * regexp to display (include /* instead of /.*)
     */
    private String prettyPattern;

    private URI uri;

    /**
     * unique permission's name.
     */
    private String name;

    /**
     * explain permission
     */
    private String description = "";

    private URLParameterCollection parameters;

    private Collection methods = new ArrayList();

    /**
     * protocol (http, https...). default value is 'ANY'
     */
    private String scheme = URLPermission.ANY;

    /**
     * actions
     */
    private String actions = "";

    public static final String FORWARD = "forward";

    public static final String REDIRECT = "redirect";

    private String dispatch = FORWARD;

    /**
     * Creates a new instance of URLPermission.
     *
     * @param n permission's name
     */
    public URLPermission(String n) {
        super(n);
        this.name = n;
        try {
            uri = new URI("");
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        parameters = new URLParameterCollection();
    }

    /**
     * Creates a new instance of URLPermission.
     *
     * @param name    permission name
     * @param actions permission's actions splitted by a "," :
     *                regexp,scheme(optional),description(optional),http methods(optional)
     *                http methods and schemes (Http or https) are automatically recognized, after the regexp.
     * @throws IllegalArgumentException which wraps a
     * @see URISyntaxException
     */
    public URLPermission(String name, String actions) {
        super(name);
        this.name = name;
        String[] actionsArray = actions.split(",");
        if (actionsArray.length > 5) {
            throw new IllegalArgumentException(" 'actions' argument can contain a maximum of five elements separated by ',' ");
        }
        try {
            setURI(actionsArray[0]);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        for (int i = 1; i < actionsArray.length; i++) {
            if (URLPermission.HTTPS.equalsIgnoreCase(actionsArray[i]) || URLPermission.HTTP.equalsIgnoreCase(actionsArray[i])) {
                this.scheme = actionsArray[i];
            } else if (URLPermission.DELETE.equalsIgnoreCase(actionsArray[i]) || URLPermission.GET.equalsIgnoreCase(actionsArray[i]) || URLPermission.HEAD.equalsIgnoreCase(actionsArray[i]) || URLPermission.OPTIONS.equalsIgnoreCase(actionsArray[i]) || URLPermission.POST.equalsIgnoreCase(actionsArray[i]) || URLPermission.PUT.equalsIgnoreCase(actionsArray[i]) || URLPermission.TRACE.equalsIgnoreCase(actionsArray[i])) {
                methods.add(actionsArray[i]);
            } else if (URLPermission.FORWARD.equalsIgnoreCase(actionsArray[i]) || URLPermission.REDIRECT.equalsIgnoreCase(actionsArray[i])) {
                dispatch = actionsArray[i];
            } else if (description == null) {
                this.description = actionsArray[i];
            }
        }
        if (scheme == null) {
            scheme = URLPermission.ANY;
        }
        if (methods.size() == 0) {
            methods.add(URLPermission.ANY);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.prettyPattern);
        sb.append(',');
        sb.append(this.scheme);
        if (this.description.length() > 0) {
            sb.append(',');
            sb.append(this.description);
        }
        this.actions = sb.toString();
    }

    /**
     * 'prettyPattern' is the regexp included in the URI : uri with a 'star'(*) operator, which can be evaluated.
     *
     * @param pPattern
     * @throws URISyntaxException
     */
    private void setURI(String pPattern) throws URISyntaxException {
        String regexp = pPattern;
        regexp = URLParameterCollection.buildRegexpFromString(getPathFromURIString(regexp));
        pattern = Pattern.compile(regexp);
        if (logger.isDebugEnabled()) {
            logger.debug("regexp=" + regexp);
        }
        String uriWithoutRegexp = removeRegexpFromURI(pPattern);
        this.uri = new URI(uriWithoutRegexp);
        if (logger.isDebugEnabled()) {
            logger.debug("uri=" + uri);
        }
        prettyPattern = pPattern;
        parameters = URLParameterCollection.getURLParameters(getQueryFromURIString(pPattern));
    }

    /**
     * replace '*'character (not followed by '*' character, or if it's the last '*') by '' and we replace '**' by '*'.
     *
     * @param uriPath
     * @return URI escaped
     */
    public static String removeRegexpFromURI(String uriPath) {
        uriPath = uriPath.replaceAll("\\*(?!\\*)", "");
        uriPath = uriPath.replaceAll("\\*{2}", "\\*");
        uriPath = uriPath.replaceAll("\\$\\{", "%24%7B");
        uriPath = uriPath.replaceAll("\\}", "%7D");
        uriPath = uriPath.replaceAll("\\|", "%7C");
        return uriPath;
    }

    private static String getPathFromURIString(String uriString) {
        String uriPath = uriString;
        int position = uriString.indexOf('?');
        if (position != -1) {
            uriPath = uriString.substring(0, position);
        }
        return uriPath;
    }

    private static String getQueryFromURIString(String uriString) {
        String uriQuery = "";
        int position = uriString.indexOf('?');
        if (position != -1) {
            uriQuery = uriString.substring(position + 1, uriString.length());
        }
        return uriQuery;
    }

    /**
     * Determines whether or not to allow access to the guarded object object.
     * this method comes from the {@link Guard} interface.
     *
     * @param perm Permission to check
     */
    @Override
    public void checkGuard(Object perm) {
        Permission p = (Permission) perm;
        AccessController.checkPermission(p);
    }

    /**
     * override the java.lang.Object 's <i>clone</i> method.
     *
     * @return new URLPermission with the <strong>same Domain</strong>.
     */
    public Object clone() throws CloneNotSupportedException {
        URLPermission permission = (URLPermission) super.clone();
        permission.name = this.name;
        permission.actions = this.getActions();
        return permission;
    }

    /**
     * @param obj
     * @return true if equals, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof URLPermission) && ((URLPermission) obj).getName().equals(this.getName())) {
            URLPermission tempPerm = (URLPermission) obj;
            String[] tempActions = tempPerm.getActions().split(",");
            URI tempUri = null;
            try {
                tempUri = new URI(removeRegexpFromURI(tempActions[0]));
            } catch (URISyntaxException e) {
                logger.error(" URI syntax error: " + removeRegexpFromURI(tempActions[0]), e);
            }
            if (!tempPerm.getScheme().equals(this.scheme)) {
                return false;
            }
            if (!tempPerm.getMethods().equals(this.methods)) {
                return false;
            }
            if (uri.getPath().equals(tempUri.getPath())) {
                if (uri.getQuery() == null && tempUri.getQuery() == null) {
                    return true;
                } else if (uri.getQuery() == null || tempUri.getQuery() == null) {
                    return false;
                } else if (uri.getQuery().equals(tempUri.getQuery())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * return actions in a String splitted by comma.
     *
     * @return permitted actions.
     */
    public String getActions() {
        return actions;
    }

    /**
     * methode used to accelerate the comparation doFilter: useful when hashcode return different int.
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * verify if this permission implies another URLPermission.
     *
     * @param permission
     * @return true if implies, false otherwise
     */
    @Override
    public boolean implies(java.security.Permission permission) {
        URLPermission urlpTemp;
        if (!(permission instanceof URLPermission)) {
            if (logger.isDebugEnabled()) {
                logger.debug(" permission is not an URLPermission. type = " + permission.getClass().getName());
            }
            return false;
        }
        urlpTemp = (URLPermission) permission;
        if (this.equals(permission)) {
            return true;
        }
        String urlpTempActions = urlpTemp.getActions();
        if (urlpTempActions == null || "".equals(urlpTempActions)) {
            return actions == null || "".equals(actions);
        }
        if (!this.scheme.equals(URLPermission.ANY) && !this.scheme.equals(urlpTemp.getScheme())) {
            return false;
        }
        if (!this.methods.contains(URLPermission.ANY)) {
            Collection httpMethods = new ArrayList(urlpTemp.getMethods());
            httpMethods.retainAll(this.methods);
            if (httpMethods.size() == 0) {
                return false;
            }
        }
        boolean b = impliesURI(urlpTemp.getURI());
        if (!b) {
            return false;
        }
        b = impliesParameters(getQueryFromURIString(urlpTemp.getURI()));
        if (logger.isDebugEnabled()) {
            logger.debug("access decision =" + b);
        }
        return b;
    }

    private boolean impliesURI(String uri) {
        String regexp = getPathFromURIString(uri);
        Matcher m = pattern.matcher(regexp);
        if (logger.isDebugEnabled()) {
            logger.debug("pattern used to check access =" + pattern.pattern());
            logger.debug(" path to be checked =" + regexp);
        }
        boolean b = m.matches();
        if (logger.isDebugEnabled()) {
            logger.debug("access decision =" + b);
        }
        m.reset();
        return b;
    }

    /**
     * look at the provided parameters by the user permission.
     *
     * @param queryFromUserPermission
     * @return
     */
    private boolean impliesParameters(String queryFromUserPermission) {
        if ("".equals(queryFromUserPermission)) {
            queryFromUserPermission = null;
        }
        if (queryFromUserPermission != null && !parameters.isEmpty()) {
            String[] params = queryFromUserPermission.split("&");
            for (String param : params) {
                String[] keyAndValue = param.split("=");
                URLParameter urlparam = new URLParameter();
                urlparam.setKey(keyAndValue[0]);
                String[] values = new String[1];
                if (keyAndValue.length != 1) {
                    values[0] = keyAndValue[1];
                } else {
                    values[0] = "";
                }
                urlparam.setValue(values);
                if (!parameters.implies(urlparam)) {
                    return false;
                }
            }
        } else if (parameters.isEmpty() && queryFromUserPermission != null) {
            return true;
        } else if (parameters.isEmpty() && queryFromUserPermission == null) {
            return true;
        } else if (!parameters.isEmpty() && queryFromUserPermission == null) {
            return false;
        }
        return true;
    }

    /**
     * return an enmpy JGPermissionCollection.
     *
     * @return empty JGPermissionCollection
     */
    @Override
    public java.security.PermissionCollection newPermissionCollection() {
        return new JGPositivePermissionCollection();
    }

    /**
     * return a String representation of the permission.
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name: ");
        sb.append(this.name);
        sb.append(",scheme: ");
        sb.append(this.scheme);
        sb.append(",parameters: ");
        sb.append(this.parameters.toString());
        sb.append(",pattern: ");
        sb.append(this.pattern);
        sb.append(",uri: ");
        sb.append(this.uri);
        sb.append(",description: ");
        sb.append(this.description);
        sb.append("\n");
        sb.append(",dispatch: ");
        sb.append(this.dispatch);
        sb.append("\n");
        return sb.toString();
    }

    /**
     * method used to compare two objects. this method is used in Collection to <strong>order</strong> items, and MUST be
     * consistent with the <i>equals</i> method (eache method should return 0/true in the same cases).
     *
     * @param o object to compare
     * @return 0 if both objects are equals, &lt;0 if 0 is lesser than the URLPermission, &gt;0 if 0 is greater than the
     *         URLPermission
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        URLPermission perm = (URLPermission) o;
        if (this.equals(perm)) {
            return 0;
        }
        return this.name.compareTo(perm.getName());
    }

    public String getURI() {
        if (uri == null) {
            throw new IllegalStateException("no uri has been filled ");
        }
        return uri.toString();
    }

    Collection getMethods() {
        return methods;
    }

    String getScheme() {
        return scheme;
    }

    public String getDispatch() {
        return dispatch;
    }
}
