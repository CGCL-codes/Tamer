package org.monet.modelling.ui.views.serversview.model;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Space {

    private static final String SPACE_ID = "id";

    private static final String SPACE_URL = "url";

    private String id;

    private String url;

    private Container parent;

    public Space() {
    }

    public Space(Container parent, Node node) {
        this.parent = parent;
        NamedNodeMap attributes = node.getAttributes();
        Node attr = attributes.getNamedItem(SPACE_ID);
        if (attr != null) this.id = attr.getNodeValue();
        attr = attributes.getNamedItem(SPACE_URL);
        if (attr != null) this.url = attr.getNodeValue();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Object getParent() {
        return parent;
    }

    public String getIp() {
        return parent.getIp();
    }
}
