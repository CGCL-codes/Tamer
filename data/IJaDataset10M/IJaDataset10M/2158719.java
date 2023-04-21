package kg.manticore.site.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@Node(jcrType = "site:textdocument")
public class TextDocument extends BaseDocument {

    public HippoHtml getHtml() {
        return getHippoHtml("site:body");
    }
}
