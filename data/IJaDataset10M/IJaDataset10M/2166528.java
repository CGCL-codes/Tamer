package cx.ath.contribs.internal.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 * <a href="http://www.wapforum.org/DTD/wml_1.1.xml">
 * http://www.wapforum.org/DTD/wml_1.1.xml</a></p>
 *
 * @version $Id: WMLGoElement.java,v 1.1 2007/06/02 09:58:57 paul Exp $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */
public interface WMLGoElement extends WMLElement {

    public void setSendreferer(String newValue);

    public String getSendreferer();

    public void setAcceptCharset(String newValue);

    public String getAcceptCharset();

    public void setHref(String newValue);

    public String getHref();

    public void setMethod(String newValue);

    public String getMethod();
}
