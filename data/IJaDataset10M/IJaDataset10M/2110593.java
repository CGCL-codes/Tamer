package net.sf.wgfa.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.struts.taglib.TagUtils;

/**
 *
 * @author tobias
 */
public class CollectionNavigatorTag extends BodyTagSupport {

    public int doStartTag() throws JspException {
        CollectionTag ct = (CollectionTag) findAncestorWithClass(this, CollectionTag.class);
        int pages = ct.getSize() / ct.getMaxItems();
        if (pages * ct.getMaxItems() < ct.getSize()) {
            pages++;
        }
        if (pages > 1) {
            for (int i = 0; i < pages; i++) {
                try {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("collectionId", ct.getCollectionId());
                    params.put("offset", Integer.toString(ct.getMaxItems() * i));
                    pageContext.getOut().write("<a href=\"");
                    String url = TagUtils.getInstance().computeURL(pageContext, null, null, null, "browseCollection", null, params, null, false);
                    pageContext.getOut().write(url);
                    pageContext.getOut().write("\">" + (i + 1) + "</a> ");
                } catch (IOException ioe) {
                    throw new JspException(ioe);
                }
            }
        }
        return SKIP_BODY;
    }
}
