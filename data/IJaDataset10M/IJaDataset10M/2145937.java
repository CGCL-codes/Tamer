package com.indigen.victor.export.staticxp;

import java.util.Hashtable;
import java.util.Map;

public class NodeDescr {

    String nodeId;

    Template template;

    Template subTemplate;

    Map nodeNames = new Hashtable();

    Map menus = new Hashtable();

    boolean used = false;

    String nodeLink;

    NodeDescr(String nodeId) {
        this.nodeId = nodeId;
    }
}
