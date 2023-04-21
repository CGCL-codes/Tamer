package com.cosmos.data.magic;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author Miro
 */
public class FileNode {

    private String name;

    private String description;

    private TreeMap<String, FieldNode> fieldNodesMap = new TreeMap<String, FieldNode>();

    private List<String> fieldNodeNames = new ArrayList<String>();

    public FileNode(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFieldNode(FieldNode fieldNode) {
        String fieldNodeName = fieldNode.getDescription();
        fieldNodeNames.add(fieldNodeName);
        fieldNodesMap.put(fieldNodeName, fieldNode);
    }

    public TreeMap<String, FieldNode> getFieldNodesMap() {
        return fieldNodesMap;
    }

    public List<String> getFieldNodeNames() {
        return fieldNodeNames;
    }

    public FieldNode getFieldNode(String fieldNodeName) {
        return fieldNodesMap.get(fieldNodeName);
    }
}
