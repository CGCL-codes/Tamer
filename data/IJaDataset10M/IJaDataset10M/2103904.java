package org.escapek.core.dto.registry;

import org.escapek.core.commons.RegistryNodeType;
import org.escapek.core.dto.NodeDTO;

public class RegistryNodeDTO extends NodeDTO {

    private static final long serialVersionUID = -7871116958325206103L;

    public static final String FOLDER_NODE = "FOLDER";

    public static final String VALUE_NODE = "VALUE";

    private String path;

    private String description;

    private Object value;

    private RegistryNodeDTO parentNode;

    private RegistryNodeType nodeType;

    public RegistryNodeDTO(RegistryNodeType nodeType) {
        setNodeType(nodeType);
    }

    public RegistryNodeDTO(RegistryNodeType nodeType, String path, String desc) {
        this(nodeType);
        setPath(path);
        setDescription(desc);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        firePropertyChange("description", this.description, description);
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        firePropertyChange("path", this.path, path);
        this.path = path;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        firePropertyChange("value", this.value, value);
        this.value = value;
    }

    public String getBasePath() {
        int posSlash = getPath().lastIndexOf("/");
        if (posSlash < getPath().length()) return getPath().substring(posSlash + 1); else return "";
    }

    public String getDirPath() {
        int posSlash = getPath().lastIndexOf("/", getPath().length() - 2);
        if (posSlash < getPath().length()) return getPath().substring(0, posSlash); else return "";
    }

    public RegistryNodeDTO getParentNode() {
        return parentNode;
    }

    public void setParentNode(RegistryNodeDTO parentNode) {
        firePropertyChange("parentNode", this.parentNode, parentNode);
        this.parentNode = parentNode;
    }

    public RegistryNodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(RegistryNodeType nodeType) {
        firePropertyChange("nodeType", this.nodeType, nodeType);
        this.nodeType = nodeType;
    }
}
