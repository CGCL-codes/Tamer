package com.apelon.dts.db.admin;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.net.URL;
import java.lang.reflect.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import com.apelon.common.xml.*;
import com.apelon.common.util.db.dao.*;
import com.apelon.common.sql.*;
import com.apelon.common.log4j.Categories;
import com.apelon.common.log4j.LogConfigLoader;

public class DTSParseConfig extends com.apelon.common.util.db.ParseConfig {

    private static final String SYNONYM = "synonym";

    private static final String MAPPING = "mapping";

    public static final int SOURCE_INDEX = 1;

    public static final int TARGET_INDEX = 0;

    public static final int SYN_NAMESPACE_INDEX = 0;

    public static final int SYN_PROPERTY_INDEX = 1;

    public static final int SYN_PREFERFLAG_INDEX = 2;

    public static final int SYN_TERM_PROPERTY_NAME = 3;

    public static final int SYN_TERM_PROPERTY_VALUE = 4;

    public static final int MAP_SRC_NAMESPACE_INDEX = 0;

    public static final int MAP_TAR_PROPERTY_INDEX = 1;

    public static final int MAP_SOURCE_PROPERTY_INDEX = 2;

    public static final int MAP_TARGET_PROPERTY_INDEX = 3;

    public static final int MAP_DELETE_SOURCE_PROPERTY_INDEX = 4;

    public static final int MAP_DELETE_TARGET_PROPERTY_INDEX = 5;

    public static final int MAP_ASSOCIATION_NAMESPACE_NAME_INDEX = 6;

    public static final int MAP_NEW_ASSOCIATION_NAME_INDEX = 7;

    public static final int MAP_NEW_ASSOCIATION_CODE_INDEX = 8;

    public static final int MAP_NEW_ASSOCIATION_ID_INDEX = 9;

    public static final int MAP_NEW_ASSOCIATION_CONNECTS_INDEX = 10;

    public static final int MAP_NEW_ASSOCIATION_PURPOSE_INDEX = 11;

    public static final int MAP_NEW_ASSOCIATION_INVERSE_NAME_INDEX = 12;

    public static final int MAP_REUSE_ASSOCIATION_INDEX = 13;

    public static final String SOURCE_PROPERTY_NAME = "SOURCE_PROPERTY_NAME";

    public static final String TARGET_PROPERTY_NAME = "TARGET_PROPERTY_NAME";

    public static final String DELETE_SOURCE_PROP_AFTER_MAP = "DELETE_SOURCE_PROPERTY_POST_MAP";

    public static final String DELETE_TARGET_PROP_AFTER_MAP = "DELETE_TARGET_PROPERTY_POST_MAP";

    public static final String ASSOCIATION_NAMESPACE_NAME = "ASSOCIATION_NAMESPACE_NAME";

    public static final String NEW_ASSOCIATION_NAME = "NEW_ASSOCIATION_NAME";

    public static final String REUSE_ASSOCIATION = "REUSE_ASSOCIATION";

    public static final String NEW_ASSOCIATION_CODE = "NEW_ASSOCIATION_CODE";

    public static final String NEW_ASSOCIATION_ID = "NEW_ASSOCIATION_ID";

    public static final String NEW_ASSOCIATION_CONNECTS = "NEW_ASSOCIATION_CONNECTS";

    public static final String NEW_ASSOCIATION_PURPOSE = "NEW_ASSOCIATION_PURPOSE";

    public static final String NEW_ASSOCIATION_INVERSE_NAME = "NEW_ASSOCIATION_INVERSE_NAME";

    private List synonymList;

    private List mappingList;

    public DTSParseConfig(String dtdURL, Class resource, String dtdFileName) throws ParserConfigurationException {
        super(dtdURL, resource, dtdFileName);
        synonymList = new ArrayList();
        mappingList = new ArrayList();
    }

    public List getSynonyms() {
        return this.synonymList;
    }

    public List getMappings() {
        return this.mappingList;
    }

    public void setSynonym(Node node) {
        String nameSpace = getAttributeName(node, "nameSpace");
        NodeList synonymChildren = node.getChildNodes();
        List synonymPropsList = new ArrayList();
        synonymPropsList.add(SYN_NAMESPACE_INDEX, nameSpace);
        for (int m = 0; m < synonymChildren.getLength(); m++) {
            Node property = (Node) synonymChildren.item(m);
            if (property.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (property.getNodeType() == Node.COMMENT_NODE) {
                continue;
            } else {
                String name = getAttributeName(property, "name");
                String value = getAttributeName(property, "value");
                if ((value.equals("true") || value.equals("false"))) {
                    synonymPropsList.add(SYN_PROPERTY_INDEX, name);
                    synonymPropsList.add(SYN_PREFERFLAG_INDEX, value);
                } else {
                    synonymPropsList.add(name);
                    synonymPropsList.add(value);
                }
            }
            NodeList properytChildren = property.getChildNodes();
            for (int y = 0; y < properytChildren.getLength(); y++) {
                Node modifier = (Node) properytChildren.item(y);
                if (modifier.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                if (modifier.getNodeType() == Node.COMMENT_NODE) {
                    continue;
                } else {
                    String name = getAttributeName(modifier, "name");
                    String value = getAttributeName(modifier, "value");
                    synonymPropsList.add(name);
                    synonymPropsList.add(value);
                }
            }
        }
        synonymList.add(synonymPropsList);
    }

    public void setMapping(Node node) throws Exception {
        String sourceNameSpace = getAttributeName(node, "sourceNameSpace");
        String targetNameSpace = getAttributeName(node, "targetNameSpace");
        NodeList mappingChildren = node.getChildNodes();
        List mappingPropsList = new ArrayList();
        mappingPropsList.add(MAP_SRC_NAMESPACE_INDEX, sourceNameSpace);
        mappingPropsList.add(MAP_TAR_PROPERTY_INDEX, targetNameSpace);
        for (int m = 0; m < mappingChildren.getLength(); m++) {
            Node property = (Node) mappingChildren.item(m);
            if (property.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String name = getAttributeName(property, "name");
            if (name.equals(SOURCE_PROPERTY_NAME)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_SOURCE_PROPERTY_INDEX, value);
            }
            name = getAttributeName(property, "name");
            if (name.equals(TARGET_PROPERTY_NAME)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_TARGET_PROPERTY_INDEX, value);
            }
            name = getAttributeName(property, "name");
            if (name.equals(DELETE_SOURCE_PROP_AFTER_MAP)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_DELETE_SOURCE_PROPERTY_INDEX, value);
            }
            name = getAttributeName(property, "name");
            if (name.equals(DELETE_TARGET_PROP_AFTER_MAP)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_DELETE_TARGET_PROPERTY_INDEX, value);
            }
            name = getAttributeName(property, "name");
            if (name.equals(ASSOCIATION_NAMESPACE_NAME)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_ASSOCIATION_NAMESPACE_NAME_INDEX, value);
            }
            name = getAttributeName(property, "name");
            if (name.equals(NEW_ASSOCIATION_NAME)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_NEW_ASSOCIATION_NAME_INDEX, value);
            }
            name = getAttributeName(property, "name");
            if (name.equals(REUSE_ASSOCIATION)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_REUSE_ASSOCIATION_INDEX, value);
            }
            name = getAttributeName(property, "name");
            if (name.equals(NEW_ASSOCIATION_CODE)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_NEW_ASSOCIATION_CODE_INDEX, value);
            }
            name = getAttributeName(property, "name");
            if (name.equals(NEW_ASSOCIATION_ID)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_NEW_ASSOCIATION_ID_INDEX, value);
            }
            name = getAttributeName(property, "name");
            if (name.equals(NEW_ASSOCIATION_CONNECTS)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_NEW_ASSOCIATION_CONNECTS_INDEX, value);
            }
            name = getAttributeName(property, "name");
            if (name.equals(NEW_ASSOCIATION_PURPOSE)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_NEW_ASSOCIATION_PURPOSE_INDEX, value);
            }
            name = getAttributeName(property, "name");
            if (name.equals(NEW_ASSOCIATION_INVERSE_NAME)) {
                String value = getAttributeName(property, "value");
                mappingPropsList.add(MAP_NEW_ASSOCIATION_INVERSE_NAME_INDEX, value);
            }
        }
        mappingList.add(mappingPropsList);
    }
}
