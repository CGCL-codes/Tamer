package netgest.bo.def.v2;

import java.util.Hashtable;
import netgest.bo.def.boDefAttribute;
import netgest.bo.def.boDefHandler;
import netgest.bo.def.boDefViewer;
import netgest.bo.def.boDefViewerCategory;
import netgest.utils.ngtXMLHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class boDefViewerImpl extends ngtXMLHandler implements boDefViewer {

    private boDefHandler p_bodef;

    private Node p_xmlnode;

    private Hashtable p_categories;

    public boDefViewerImpl(boDefHandler bodef, Node x) {
        super(x);
        p_xmlnode = x;
        p_bodef = bodef;
    }

    public String getViewerName() {
        return "general";
    }

    public ngtXMLHandler getForm(String xform) {
        ngtXMLHandler[] form = getForms();
        for (int i = 0; form != null && i < form.length; i++) {
            if (xform.equalsIgnoreCase(form[i].getAttribute("name"))) {
                return form[i];
            }
        }
        return null;
    }

    public boolean HasForm(String xform) {
        ngtXMLHandler[] form = getForms();
        for (int i = 0; form != null && i < form.length; i++) {
            if (xform.equalsIgnoreCase(form[i].getAttribute("name"))) {
                return true;
            }
        }
        return false;
    }

    public ngtXMLHandler[] getForms() {
        return super.getChildNode("forms").getChildNodes();
    }

    public boDefHandler getBoDefHandler() {
        return p_bodef;
    }

    public boDefViewerCategory getCategory(String category) {
        boDefViewerCategoryImpl ret = null;
        if (true || p_categories == null || (ret = (boDefViewerCategoryImpl) p_categories.get(category)) == null) {
            String cats[] = category.split("\\.");
            ngtXMLHandler node = super.getChildNode("categories");
            for (int i = 0; i < cats.length && node != null; i++) {
                if (i > 0) node = node.getChildNode("categories");
                if (node != null) node = GenericParseUtils.getChildOfName(node, cats[i]);
            }
            if (node != null) {
                ret = new boDefViewerCategoryImpl(node.getNode(), this);
            } else {
                boDefAttribute defAtt = p_bodef.getAttributeRef(category);
                if (defAtt != null) {
                    ret = new boDefViewerCategoryImpl(defAtt, this);
                }
            }
        }
        if (ret != null) {
            if (p_categories == null) p_categories = new Hashtable();
            p_categories.put(category, ret);
        }
        return ret;
    }

    public String getObjectViewerClass() {
        return super.getAttribute("viewerClass", "netgest.bo.runtime.ObjectViewerImpl");
    }
}
