package net.sf.yari.ui.util.internal.search;

import java.util.List;
import net.sf.yari.ui.IInspectorNode;
import net.sf.yari.ui.util.IPropertyBean;

public class ResultSet {

    IInspectorNode node = null;

    List<IPropertyBean> properties = null;

    public ResultSet(IInspectorNode node) {
        this.node = node;
    }

    public IInspectorNode getNode() {
        return node;
    }

    public void setNode(IInspectorNode node) {
        this.node = node;
    }

    public List<IPropertyBean> getProperties() {
        return properties;
    }

    public void setProperties(List<IPropertyBean> properties) {
        this.properties = properties;
    }
}
