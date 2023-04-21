package cx.ath.contribs.internal.wml.dom;

import cx.ath.contribs.internal.wml.WMLRefreshElement;

/**
 * @xerces.internal
 * @version $Id: WMLRefreshElementImpl.java,v 1.2 2007/07/13 07:23:27 paul Exp $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */
public class WMLRefreshElementImpl extends WMLElementImpl implements WMLRefreshElement {

    private static final long serialVersionUID = 8781837880806459398L;

    public WMLRefreshElementImpl(WMLDocumentImpl owner, String tagName) {
        super(owner, tagName);
    }

    public void setClassName(String newValue) {
        setAttribute("class", newValue);
    }

    public String getClassName() {
        return getAttribute("class");
    }

    public void setId(String newValue) {
        setAttribute("id", newValue);
    }

    public String getId() {
        return getAttribute("id");
    }
}
