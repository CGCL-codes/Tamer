package org.nargila.treexml;

import org.nargila.util.*;
import java.awt.event.*;
import java.awt.Component;
import org.xml.sax.*;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTProcessor;
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTResultTarget;
import org.apache.xalan.xslt.XSLTProcessor;
import org.w3c.dom.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;

/**
    builds the tree and its components using stylesheet. Configuration of the components
    can be done inside the stylesheet.
*/
public class ComponentBuilder implements TreeXmlConstants {

    /**
       map of resources.
       Any arbitrary resource may be set from program or stylesheet. Example:
       setResource("actionListener", listener);
       Inside the stylesheet:
       <xsl:param name="listener" select="java:getResource($builder, 'actionListener')"/>
       @see #setResource
       @see #getResource
    */
    Map resourceMap = new HashMap();

    XSLTInputSource xsl = null;

    TreeData treeData;

    public ComponentBuilder() {
        treeData = new TreeData();
    }

    /**
       constructor with target tree argument.
       @param tree target tree
    */
    public ComponentBuilder(TreeData tree_data) {
        treeData = tree_data;
    }

    /**
       set the orientation member.
       @param axis orientation of target tree - VERTICAL or HORISONTAL
    */
    public void setOrientation(int axis) {
        treeData.setOrientation(axis);
    }

    /**
       set the root member. This method is called from inside the stylesheet, Therefore
       it takes a NodeList as argument because in the XSLT processor '.' translates to
       a NodeList object with one Node item.
       @param nodes the NodeList containing root node
    */
    public void setRoot(NodeList nodes) {
        treeData.setRoot(nodes.item(0));
    }

    /**
       set resource in resourceMap.
       Any arbitrary resource may be set from program or stylesheet. Example:
       setResource("actionListener", listener);
       Inside the stylesheet:
       <xsl:param name="listener" select="java:getResource($builder, 'actionListener')"/>
       @param name resource name
       @param obj resource value object
       @see #getResource
    */
    public void setResource(String name, Object obj) {
        resourceMap.put(name, obj);
    }

    /** get resource from resourceMap.
       @see #setResource
    */
    public Object getResource(String name) {
        return resourceMap.get(name);
    }

    /**
       set the stylesheet input
       @param xsl_input XSLT stylesheet input
    */
    public void setXslt(InputSource xsl_input) {
        xsl = new XSLTInputSource(xsl_input);
    }

    /**
       build tree from XML input source. The stylesheet set via setXslt() above will be used.
       Default stylesheet will be created if stylesheet was not set.
       @param xml XML input
    */
    public void buildData(InputSource xml) throws IOException, SAXException {
        Document doc = XMLUtils.loadXML(xml);
        buildData(doc);
    }

    /**
       builds the component map for target tree using stylesheet. Default stylesheet will be created if
       stylesheet was not set.
       @param node DOM Node to build componant map for
    */
    public void buildData(Node node) throws SAXException, IOException {
        treeData.newMap();
        treeData.setRoot(node);
        if (null == xsl) {
            xsl = new XSLTInputSource(JarUtils.resourceReader(this, "default.xsl"));
        }
        XSLTProcessor processor = XSLTProcessorFactory.getProcessor();
        processor.setStylesheetParam("builder", processor.createXObject(this));
        setResource("data", treeData);
        processor.process(new XSLTInputSource(node), xsl, new XSLTResultTarget(new StringWriter()));
        treeData.setupMap();
    }

    public TreeData getData() {
        return treeData;
    }

    /**
       add a component to component map for first node. Really param nodes should have been a single
       Node, but NodeList is used because the scope of '.' in the stylesheet's XSLT processor is a NodeList
       containing the current node.
       @param nodes the first node of which to put in map as key
       @param c the component to put as value in map
    */
    public Component addComponent(NodeList nodes, Component c) {
        if (0 != nodes.getLength()) {
            treeData.componentMap.put(nodes.item(0), c);
            return c;
        }
        return null;
    }

    /**
       add a component to component map for first node. Really param nodes should have been a single
       Node, but NodeList is used because the scope of '.' in the stylesheet's XSLT processor is a NodeList
       containing the current node. The component will be created by retrieving the class and creating a new
       instance using the reflection APIs. The component class must have a constructor which accepts a single Node
       as its parameter.
       @param nodes the first node of which to put in map as key
       @param class_name the class name of component to create and put as value in map
    */
    public Component addComponent(NodeList nodes, String class_name) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, InstantiationException, IllegalAccessException {
        Class component_class = Class.forName(class_name);
        Class[] cargs = new Class[1];
        Object[] oargs = new Object[1];
        cargs[0] = Node.class;
        Component res = null;
        for (int i = 0; i < nodes.getLength(); ++i) {
            Node n = nodes.item(i);
            oargs[0] = n;
            Constructor constructor = component_class.getConstructor(cargs);
            Component component = (Component) constructor.newInstance(oargs);
            treeData.componentMap.put(n, component);
            if (null == res) {
                res = component;
            }
        }
        return res;
    }
}
