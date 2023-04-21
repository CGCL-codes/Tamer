package au.edu.archer.metadata.mde;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Session bean that captures and validates configuration parameters used by the the metadata editor.
 * The index.jsp entry point can be used to import CGI parameters into this bean before forwarding to
 * mde.jsp that launches the metadata editor window.
 *
 * TODO: Internet Security: Wrapper all calls to setParam() in setter methods with validation logic
 * that filters hostile/illegal CGI parameter values.
 *
 * @author tohagan
 */
public class EditorSession implements Serializable {

    private static final Logger logger = Logger.getLogger(EditorSession.class);

    private static final long serialVersionUID = 4957313567528748841L;

    Map<String, Object> params;

    String baseUrl;

    public EditorSession() {
        clear();
    }

    /**
     * Reset bean to default property values.
     * Used in JSP prior to importing CGI parameters.
     */
    public void clear() {
        this.params = new HashMap<String, Object>();
        this.setUiStyle(1);
        this.setDebug(0);
        this.setNext("close");
        this.setCancel("close");
        this.setReadOnly(false);
        this.setSchemaCachingDisabled(false);
    }

    /**
     * Used by mde.jsp to convert bean state into a JSON object used to configure the EditorController Javascript class.
     * @return Returns a JSON encoding of bean properties.      Unset or null parameters are not included in the property list.
     */
    public String toJSON() {
        if (this.params.isEmpty()) {
            return "{ }";
        } else {
            StringBuffer sb = new StringBuffer();
            boolean first = true;
            for (Map.Entry<String, Object> e : this.params.entrySet()) {
                if (first) {
                    first = false;
                    sb.append("\"");
                } else {
                    sb.append(", \"");
                }
                sb.append(e.getKey());
                sb.append("\": ");
                Object val = e.getValue();
                if (val instanceof String) {
                    String strVal = (String) val;
                    strVal.replace("\\", "\\\\");
                    strVal.replace("\"", "\\\"");
                    sb.append("\"");
                    sb.append(strVal);
                    sb.append("\"");
                } else {
                    sb.append(val.toString());
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("EditorSession is '" + sb.toString() + "'");
            }
            return "{ " + sb.toString() + " }";
        }
    }

    private String getStringParam(String key) {
        return (String) this.params.get(key);
    }

    private int getIntegerParam(String key) {
        return (Integer) this.params.get(key);
    }

    private boolean getBoolParam(String key) {
        return (Boolean) this.params.get(key);
    }

    private void setParam(String key, Object value) {
        if (value == null) {
            this.params.remove(key);
        } else {
            this.params.put(key, value);
        }
    }

    /**
     * @return Base URL of editor.  Same as request.getContextPath().
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * @param baseUrl the baseUrl to set
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        if (getRecRepos() == null) {
            setRecRepos(baseUrl + "/server/repos/");
        }
        if (getSchemaRepos() == null) {
            setSchemaRepos(baseUrl + "/server/repos/");
        }
    }

    /**
     * Used as a base URL for relative record and template URL paths.
     * @return The record and template repository
     */
    public String getRecRepos() {
        return getStringParam("recordRepositoryUrl");
    }

    /**
     * @param repository The record repository to set
     */
    public void setRecRepos(String repository) {
        setParam("recordRepositoryUrl", repository);
    }

    /**
     * Used as a base URL by relative schema URLs.
     * These schema URLS are typically embedded inside XML records and templates as target locations.
     * @return the Schema repository URL
     */
    public String getSchemaRepos() {
        return getStringParam("schemaRepositoryUrl");
    }

    /**
     * @param repository The schema repository URL to set
     */
    public void setSchemaRepos(String repository) {
        setParam("schemaRepositoryUrl", repository);
    }

    /**
     * @return True if loaded schema are to be cached client side.
     */
    public boolean isSchemaCachingDisabled() {
        return getBoolParam("isSchemaCachingDisabled");
    }

    /**
     * @param isSchemaCachingDisabled Enable/disable caching.
     */
    public void setSchemaCachingDisabled(boolean isSchemaCachingDisabled) {
        setParam("isSchemaCachingDisabled", isSchemaCachingDisabled);
    }

    /**
     * @return Debug level
     */
    public int getDebug() {
        return getIntegerParam("debug");
    }

    /**
     * @param debug Set the debug level
     */
    public void setDebug(int debug) {
        setParam("debug", debug);
    }

    /**
     * @return User interface style
     */
    public int getUiStyle() {
        return getIntegerParam("uiStyle");
    }

    /**
     * @param uiStyle User interface style
     */
    public void setUiStyle(int uiStyle) {
        setParam("uiStyle", uiStyle);
    }

    /**
     * Edited record is saved to this location.
     * It is also loaded from this location unless a template property is set.
     * @return Id of record to be edited (typically a URL).
     */
    public String getRecord() {
        return getStringParam("recordId");
    }

    /**
     * @param record Id of record to be edited (typically a URL).
     */
    public void setRecord(String record) {
        setParam("recordId", record);
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return getStringParam("templateId");
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String template) {
        setParam("templateId", template);
    }

    /**
     * @return Editor window page title.  Defaults to 'record' field if 'title' is not set.
     */
    public String getTitle() {
        return this.params.containsKey("title") ? getStringParam("title") : getStringParam("recordId");
    }

    /**
     * @param title Editor window page title.
     */
    public void setTitle(String title) {
        setParam("title", title);
    }

    /**
     * No forwarding is performed if null.
     * @return Editor forward browser to this URL when user closes the editor.
     */
    public String getNext() {
        return getStringParam("nextUrl");
    }

    /**
     * No forwarding is performed if null
     * @param next Editor forwards browser to this URL when user closes the editor.
     */
    public void setNext(String next) {
        setParam("nextUrl", next);
    }

    /**
     * No forwarding is performed if null
     * @return Editor forward browser to this URL on user initiated cancel.
     */
    public String getCancel() {
        return getStringParam("cancelUrl");
    }

    /**
     * No forwarding is performed if null.
     * @param cancel Editor forwards browser to this URL on user initiated cancel.
     */
    public void setCancel(String cancel) {
        setParam("cancelUrl", cancel);
    }

    /**
     * @return Returns true if Editor will disabled saving of records.
     */
    public boolean isReadOnly() {
        return getBoolParam("readOnly");
    }

    /**
     * @param isReadOnly If true, Editor will disabled saving of records.
     */
    public void setReadOnly(boolean isReadOnly) {
        setParam("readOnly", isReadOnly);
    }
}
