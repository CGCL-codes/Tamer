package org.iqual.chaplin;

import org.iqual.chaplin.msg.MessageScope;
import java.util.StringTokenizer;

/**
 * @author Zbynek Slajchrt
 * @since Jun 25, 2009 10:10:46 PM
 */
public class FromAnnotContent {

    String contextType;

    boolean dynaSign;

    long minOccurs;

    long maxOccurs;

    String name;

    MessageScope scope = MessageScope.local;

    String selectorType;

    private static final String NULL = "null";

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(contextType).append('\n');
        sb.append(dynaSign).append('\n');
        sb.append(minOccurs).append('\n');
        sb.append(maxOccurs).append('\n');
        sb.append(name).append('\n');
        sb.append(scope).append('\n');
        sb.append(selectorType).append('\n');
        return sb.toString();
    }

    public static FromAnnotContent fromString(String content) {
        String t;
        StringTokenizer tk = new StringTokenizer(content, "\n");
        FromAnnotContent fac = new FromAnnotContent();
        fac.contextType = tk.nextToken();
        fac.dynaSign = Boolean.parseBoolean(tk.nextToken());
        fac.minOccurs = Long.parseLong(tk.nextToken());
        fac.maxOccurs = Long.parseLong(tk.nextToken());
        t = tk.nextToken();
        fac.name = NULL.equals(t) ? null : t;
        fac.scope = MessageScope.valueOf(tk.nextToken());
        t = tk.nextToken();
        fac.selectorType = NULL.equals(t) ? null : t;
        return fac;
    }

    private FromAnnotContent() {
    }

    public FromAnnotContent(String contextType) {
        this(contextType, false, 1, 1);
    }

    public FromAnnotContent(FromContext fromCtxAnnot) {
        if (fromCtxAnnot != null) {
            contextType = fromCtxAnnot.value().toString();
            dynaSign = fromCtxAnnot.dynaSign();
            minOccurs = fromCtxAnnot.minOccurs();
            maxOccurs = fromCtxAnnot.maxOccurs();
            name = fromCtxAnnot.name();
            if ("".equals(name)) {
                name = null;
            }
            scope = fromCtxAnnot.scope();
            selectorType = fromCtxAnnot.selector().getName();
        }
    }

    public FromAnnotContent(String contextType, boolean dynaSign, long minOccurs, long maxOccurs) {
        this.contextType = contextType;
        this.dynaSign = dynaSign;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public boolean isDynaSign() {
        return dynaSign;
    }

    public void setDynaSign(boolean dynaSign) {
        this.dynaSign = dynaSign;
    }

    public long getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(long minOccurs) {
        this.minOccurs = minOccurs;
    }

    public long getMaxOccurs() {
        return maxOccurs;
    }

    public void setMaxOccurs(long maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MessageScope getScope() {
        return scope;
    }

    public void setScope(MessageScope scope) {
        this.scope = scope;
    }

    public String getSelectorType() {
        return selectorType;
    }

    public void setSelectorType(String selectorType) {
        this.selectorType = selectorType;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FromAnnotContent that = (FromAnnotContent) o;
        if (dynaSign != that.dynaSign) return false;
        if (maxOccurs != that.maxOccurs) return false;
        if (minOccurs != that.minOccurs) return false;
        if (!contextType.equals(that.contextType)) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (selectorType != null ? !selectorType.equals(that.selectorType) : that.selectorType != null) return false;
        if (scope != that.scope) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = contextType.hashCode();
        result = 31 * result + (dynaSign ? 1 : 0);
        result = 31 * result + (int) (minOccurs ^ (minOccurs >>> 32));
        result = 31 * result + (int) (maxOccurs ^ (maxOccurs >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (selectorType != null ? selectorType.hashCode() : 0);
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        return result;
    }
}
