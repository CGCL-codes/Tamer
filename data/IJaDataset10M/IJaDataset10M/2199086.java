package net.sf.ldaptemplate.support.filter;

import org.apache.commons.lang.Validate;

/**
 * Decorator filter for NOT. (!<i>filter</i> )
 * 
 * <pre>
 *   Filter filter = new NotFilter(new EqualsFilter(&quot;cn&quot;, &quot;foo&quot;);
 *   System.out.println(filter.ecode());
 * </pre>
 * 
 * would resut in: <code>(!(cn=foo))</code>
 * 
 * @author Adam Skogman
 */
public class NotFilter extends AbstractFilter {

    private final Filter filter;

    private static final int HASH = "!".hashCode();

    /**
     * 
     */
    public NotFilter(Filter filter) {
        Validate.notNull(filter);
        this.filter = filter;
    }

    /**
     * @see net.sf.ldaptemplate.support.filter.Filter#encode(java.lang.StringBuffer)
     */
    public StringBuffer encode(StringBuffer buff) {
        buff.append("(!");
        filter.encode(buff);
        buff.append(')');
        return buff;
    }

    /**
     * Compares key and value before encoding
     * 
     * @see net.sf.ldaptemplate.support.filter.Filter#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (o instanceof NotFilter && o.getClass() == this.getClass()) {
            NotFilter f = (NotFilter) o;
            return this.filter.equals(f.filter);
        }
        return false;
    }

    /**
     * hash attribute and value
     * 
     * @see net.sf.ldaptemplate.support.filter.Filter#hashCode()
     */
    public int hashCode() {
        return HASH ^ filter.hashCode();
    }
}
