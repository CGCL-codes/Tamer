package netgest.bo.xwc.framework.def;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XUIViewerDefinitionNode {

    public XUIViewerDefinitionNode parent;

    public XUIViewerDefinition root;

    public ArrayList<XUIViewerDefinitionNode> children = new ArrayList<XUIViewerDefinitionNode>();

    public String name;

    public String id;

    public String textContent;

    public LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();

    public XUIViewerDefinitionNode() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties.putAll(properties);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperty(String name, String value) {
        properties.put(name, value);
    }

    public String getProperty(String name) {
        return (String) properties.get(name);
    }

    protected void setParent(XUIViewerDefinitionNode parent) {
        this.parent = parent;
    }

    public XUIViewerDefinitionNode getParent() {
        return parent;
    }

    protected void setRoot(XUIViewerDefinition root) {
        this.root = root;
    }

    public XUIViewerDefinition getRoot() {
        return root;
    }

    public void addChild(XUIViewerDefinitionNode component) {
        children.add(component);
    }

    public List<XUIViewerDefinitionNode> getChildren() {
        return children;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getTextContent() {
        return this.textContent;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
