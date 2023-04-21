package com.mockturtlesolutions.snifflib.invprobs;

import javax.swing.ImageIcon;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import javax.swing.tree.*;
import java.io.*;

public class HierarchicalNode {

    private String name;

    private String repository;

    private String date;

    private TreeMap children;

    public HierarchicalNode() {
        this.name = null;
        this.date = null;
        this.repository = null;
        this.children = new TreeMap();
    }

    public HierarchicalNode(String repos, String name, String date) {
        this.name = name;
        this.date = date;
        this.repository = repos;
        this.children = new TreeMap();
    }

    public String toString() {
        return (this.name);
    }

    public void addChild(String key, HierarchicalNode child) {
        this.children.put(key, child);
    }

    public HierarchicalNode getChild(String key) {
        return ((HierarchicalNode) this.children.get(key));
    }

    public String getRepository() {
        return (this.repository);
    }

    public void setRepository(String n) {
        this.repository = n;
    }

    public TreeMap getChildren() {
        return (this.children);
    }

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return (this.date);
    }
}
