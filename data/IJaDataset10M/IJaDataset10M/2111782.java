package net.mlw.vlh.web.tag.support;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.PageContext;

/** 
 * If you have any problem with decoding URI, please set in Tomcat connector property 
 * this param  <b>URIEncoding="UTF-8"</b> as well:
 * <h4>Example</h4>
 * <p>
 * &lt;!-- Define a non-SSL Coyote HTTP/1.1 Connector on port 8080 --&gt; 
 * <BR>&lt;Connector port="8080" 
 * <BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
 * <b>URIEncoding="UTF-8"</b>&nbsp; ...... /&gt;
 * </p> 
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.6 $ $Date: 2005/01/26 17:36:57 $
 */
public class DefaultLinkEncoder implements LinkEncoder {

    private String encoding = "UTF-8";

    /**
	 * @see net.mlw.vlh.web.tag.support.LinkEncoder#encode(java.util.Map, java.util.Collection, java.util.Collection)
	 */
    public String encode(PageContext pageContext, Map parameters) {
        StringBuffer sb = new StringBuffer();
        try {
            for (Iterator iter = parameters.keySet().iterator(); iter.hasNext(); ) {
                String key = (String) iter.next();
                Object value = parameters.get(key);
                if (value instanceof String[]) {
                    String[] values = (String[]) value;
                    for (int i = 0; i < values.length; i++) {
                        sb.append(key).append("=").append(URLEncoder.encode(values[i], encoding)).append("&amp;");
                    }
                } else if (value instanceof String) {
                    sb.append(key).append("=").append(URLEncoder.encode((String) value, encoding)).append("&amp;");
                } else if (value != null) {
                    sb.append(key).append("=").append(URLEncoder.encode(value.toString(), encoding)).append("&amp;");
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
	 * @return Returns the encoding.
	 */
    public String getEncoding() {
        return encoding;
    }

    /**
	 * @param encoding The encoding to set.
	 */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
