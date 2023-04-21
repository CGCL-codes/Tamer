package net.sf.jguard.core.authorization.permissions;

import org.apache.commons.lang.StringEscapeUtils;
import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * represents an url key and her associated value.
 *
 * @author <a href="mailto:diabolo512@users.sourceforge.net ">Charles Gay</a>
 * @author <a href="mailto:vberetti@users.sourceforge.net">Vincent Beretti</a>
 */
public class URLParameter implements Serializable, Cloneable {

    /**
     * serial version id.
     */
    private static final long serialVersionUID = 3835156176932384821L;

    private String key = null;

    /**
     * the HttpservlerRequest.getParameterValues() return a values Array.
     * when the URL contains copies of a parameter name,
     * this method return an array of all the values for the same parameter name.
     */
    private String[] value = null;

    private String permissionName = null;

    /**
     * constructor.
     */
    URLParameter() {
    }

    /**
     * override the java.lang.Object 's <i>clone</i> method.
     *
     * @return new URLParameter.
     */
    public Object clone() throws CloneNotSupportedException {
        URLParameter clone = (URLParameter) super.clone();
        clone.key = this.key;
        clone.value = this.value;
        clone.permissionName = this.permissionName;
        return clone;
    }

    /**
     * @return
     */
    String getKey() {
        return key;
    }

    /**
     * @return
     */
    String[] getValue() {
        String[] copy = new String[value.length];
        System.arraycopy(value, 0, copy, 0, value.length);
        return copy;
    }

    /**
     * concat the array String in a single string with a ";" separator.
     *
     * @return concat String
     */
    String getValuesAsString() {
        String[] valuesArray = this.value;
        StringBuffer concatValues = new StringBuffer();
        for (int i = 0; i < valuesArray.length; i++) {
            if (i != 0) {
                concatValues.append(";");
            }
            concatValues.append(valuesArray[i]);
        }
        return concatValues.toString();
    }

    /**
     * @param string
     */
    void setKey(String string) {
        key = string;
    }

    /**
     * @param string
     */
    void setValue(String[] string) {
        int length = string.length;
        String[] copy = new String[length];
        System.arraycopy(string, 0, copy, 0, length);
        value = copy;
    }

    /**
     * override java.lang.Object's <i>hashCode</i> method.
     *
     * @return int to compare quickly to URLParameters.
     */
    public int hashCode() {
        StringBuffer sb = new StringBuffer(this.key);
        sb.append(this.getValuesAsString());
        return sb.toString().hashCode();
    }

    /**
     * override Object equals method.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof URLParameter)) {
            return false;
        }
        URLParameter urlp = (URLParameter) obj;
        if (!urlp.getKey().equals(this.key)) {
            return false;
        }
        String[] targetArray = urlp.getValue();
        String[] myArray = this.value;
        if (myArray.length != targetArray.length) {
            return false;
        }
        Arrays.sort(targetArray);
        Arrays.sort(myArray);
        for (int i = 0; i < myArray.length; i++) {
            if (!(myArray[i]).equals(targetArray[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return
     */
    String getPermissionName() {
        return permissionName;
    }

    /**
     * @param string
     */
    void setPermissionName(String string) {
        permissionName = string;
    }

    /**
     * from an URI and a Permission associated
     *
     * @param uri
     * @return
     */
    protected static Set getURLParameters(URI uri) {
        Set parameters = new HashSet();
        String query = uri.getQuery();
        query = StringEscapeUtils.unescapeHtml(query);
        if (query != null && !"".equals(query)) {
            List tokens = Arrays.asList(query.split("&"));
            for (Object token1 : tokens) {
                String token = (String) token1;
                String[] parts = token.split("=");
                if (parts.length == 2) {
                    URLParameter param = new URLParameter();
                    param.setKey(parts[0]);
                    String[] values = parts[1].split(";");
                    param.setValue(values);
                    parameters.add(param);
                }
            }
        }
        return parameters;
    }

    public boolean impliesKey(String key) {
        String regexpKey = this.key.replaceAll("\\*", "\\.\\*");
        return !(!key.equals(this.key) && !Pattern.matches(regexpKey, key));
    }

    public boolean impliesValues(String[] value) {
        if (value.length != this.value.length) {
            return false;
        }
        Arrays.sort(value);
        Arrays.sort(this.value);
        for (int i = 0; i < value.length; i++) {
            String regexpValue = this.value[i].replaceAll("\\*", "\\.\\*");
            if (!(value[i]).equals(this.value[i]) && !Pattern.matches(regexpValue, value[i])) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("key=").append(key).append("  ");
        sb.append("value=").append(getValuesAsString());
        return sb.toString();
    }
}
