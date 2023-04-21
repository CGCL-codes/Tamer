package org.gvsig.hyperlink.config;

import java.util.ArrayList;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

public class LayerLinkConfig implements IPersistence {

    protected ArrayList<LinkConfig> linkList;

    protected boolean enabled;

    public LayerLinkConfig() {
        linkList = new ArrayList<LinkConfig>();
    }

    public void addLink(LinkConfig config) {
        linkList.add(config);
    }

    public void addLink(int position, LinkConfig config) {
        linkList.add(position, config);
    }

    public void addLink(String actionCode, String fieldName) {
        linkList.add(new LinkConfig(actionCode, fieldName));
    }

    public void addLink(String actionCode, String fieldName, String extension) {
        linkList.add(new LinkConfig(actionCode, fieldName, extension));
    }

    public LinkConfig removeLink(int linkIndex) {
        try {
            return linkList.remove(linkIndex);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public LinkConfig getLink(int index) {
        return linkList.get(index);
    }

    public int linkCount() {
        return linkList.size();
    }

    public String getClassName() {
        return this.getClass().getName();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public XMLEntity getXMLEntity() {
        XMLEntity xmlconfig = new XMLEntity();
        xmlconfig.putProperty("className", this.getClassName());
        xmlconfig.putProperty("enabled", isEnabled());
        for (int i = 0; i < linkList.size(); i++) {
            xmlconfig.addChild(linkList.get(i).getXMLEntity());
        }
        return xmlconfig;
    }

    public void setXMLEntity(XMLEntity xml) {
        if (xml.contains("enabled")) {
            setEnabled(xml.getBooleanProperty("enabled"));
        } else {
            setEnabled(false);
        }
        for (int i = 0; i < xml.getChildrenCount(); i++) {
            LinkConfig link = LinkConfig.createFromXMLEntity(xml.getChild(i));
            linkList.add(link);
        }
    }
}
