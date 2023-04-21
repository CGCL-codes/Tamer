package com.indigen.victor.actions;

import java.util.*;
import com.indigen.victor.util.*;
import org.w3c.dom.*;

public class GetInstanceAction extends GetInstanceInfoAction {

    static final String VALID_NEUTRAL_SRC = "images/validation-neutral.gif";

    static final String VALID_OK_SRC = "images/validation-ok.gif";

    static final String VALID_KO_SRC = "images/validation-ko.gif";

    public Map process() throws Exception {
        String id = getParameter("id");
        Node instanceNode = getNodeFromXPath("/project/instances/instance[@id='" + id + "']");
        if (instanceNode == null) {
            getLogger().error("instance id '" + id + "' not found");
            return null;
        }
        String classId = ((Element) instanceNode).getAttribute("class");
        Node classNode = getNodeFromXPath("/project/ontology/class[id='" + classId + "']");
        if (classNode == null) {
            getLogger().error("class id '" + classId + "' not found");
            return null;
        }
        setFlowData("instanceid", id);
        setFlowData("instancename", getInstanceName(instanceNode));
        setFlowData("classname", getStringFromXPath(classNode, "name"));
        setFlowData("classid", classId);
        List props = new Vector();
        setFlowData("props", props);
        List propertyNodes = getNodesFromXPath(classNode, "property");
        Iterator i = propertyNodes.iterator();
        while (i.hasNext()) {
            Node propertyNode = (Node) i.next();
            String propId = getStringFromXPath(propertyNode, "id");
            Map prop = new Hashtable();
            props.add(prop);
            prop.put("id", propId);
            String propName = getStringFromXPath(propertyNode, "name");
            prop.put("name", propName);
            List rules = new Vector();
            prop.put("rules", rules);
            List ruleNodes = getNodesFromXPath(propertyNode, "rule");
            Iterator j = ruleNodes.iterator();
            while (j.hasNext()) {
                Node ruleNode = (Node) j.next();
                Map rule = new Hashtable();
                rules.add(rule);
                String ruleId = ((Element) ruleNode).getAttribute("id");
                rule.put("id", ruleId);
                rule.put("value", objectToJs(ruleNode));
            }
            List values = new Vector();
            prop.put("values", values);
            List valueNodes = getNodesFromXPath(instanceNode, "inst-value[@prop-id='" + propId + "']");
            j = valueNodes.iterator();
            while (j.hasNext()) {
                Node valueNode = (Node) j.next();
                String temp = XmlUtils.getStringFromXPath(valueNode, "temp");
                if (temp == null || temp.length() == 0 || temp.equals(getSession().getId())) {
                    Map value = new Hashtable();
                    values.add(value);
                    if (temp != null && temp.length() > 0) {
                        value.put("type", "ontomatch");
                    } else {
                        value.put("type", "ontovalue");
                    }
                    String valueId = ((Element) valueNode).getAttribute("id");
                    value.put("id", valueId);
                    String dataType = ((Element) valueNode).getAttribute("data-type");
                    value.put("datatype", dataType);
                    String valueContent = getStringFromXPath(valueNode, "value");
                    value.put("value", valueContent);
                    String dataId = ((Element) valueNode).getAttribute("data-id");
                    if (dataId != null && dataId.length() > 0) {
                        value.put("dataid", dataId);
                        value.put("name", getInstanceName(valueContent));
                    } else {
                        value.put("name", fixStringForDisplay(valueContent));
                    }
                    String pageId = getStringFromXPath(valueNode, "page-id");
                    if (pageId != null && pageId.length() > 0) value.put("pageid", pageId);
                    Node location = getNodeFromXPath(valueNode, "location");
                    if (location != null) value.put("location", objectToJs(location));
                    String validationState = XmlUtils.getStringFromXPath(valueNode, "validation-state");
                    if ("ok".equals(validationState)) value.put("validationstate", VALID_OK_SRC); else if ("ko".equals(validationState)) value.put("validationstate", VALID_KO_SRC); else value.put("validationstate", VALID_NEUTRAL_SRC);
                    String lang = ((Element) valueNode).getAttribute("lang");
                    if (lang != null && lang.length() > 0) value.put("lang", lang);
                }
            }
        }
        Map map = new Hashtable();
        return map;
    }
}
