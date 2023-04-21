package purej.web.servlet.ajax;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Helper class to build valid XML, for the AjaxTreeTag, typically returned in a
 * response to the client.
 * 
 * @author Musachy Barroso
 * @version $Revision: 1.2 $ $Date: 2006/09/03 18:55:17 $
 */
@SuppressWarnings("unchecked")
public class AjaxTreeXmlBuilder {

    private String encoding = "UTF-8";

    private List<TreeItem> items = new ArrayList<TreeItem>();

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Add item to XML.
     * 
     * @param name
     *                The name of the item
     * @param value
     *                The value of the item
     * @param collapsed
     *                The node is collapsed
     * @param url
     *                The url for the node
     * @return
     */
    public AjaxTreeXmlBuilder addItem(String name, String value, boolean collapsed, String url) {
        items.add(new TreeItem(name, value, collapsed, url, false));
        return this;
    }

    /**
     * Add item to XML. Collapsed by default.
     * 
     * @param name
     *                The name of the item
     * @param value
     *                The value of the item
     * @param collapsed
     *                The node is collapsed
     * @param url
     *                The url for the node
     * @return
     */
    public AjaxTreeXmlBuilder addItem(String name, String value, String url) {
        items.add(new TreeItem(name, value, true, url, false));
        return this;
    }

    /**
     * Add item wrapped with inside a CDATA element.
     * 
     * @param name
     *                The name of the item
     * @param value
     *                The value of the item
     * @param collapsed
     *                The node is collapsed
     * @param url
     *                The url for the node
     * @return
     */
    public AjaxTreeXmlBuilder addItemAsCData(String name, String value, boolean collapsed, String url) {
        items.add(new TreeItem(name, value, collapsed, url, true));
        return this;
    }

    /**
     * Add items from a collection.
     * 
     * @param collection
     * @param nameProperty
     * @param collapsedProperty
     * @param urlProperty
     * @param valueProperty
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public AjaxTreeXmlBuilder addItems(Collection collection, String nameProperty, String valueProperty, String collapsedProperty, String urlProperty) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return addItems(collection, nameProperty, valueProperty, collapsedProperty, urlProperty, false);
    }

    /**
     * Add items from a collection.
     * 
     * @param collection
     * @param nameProperty
     * @param valueProperty
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public AjaxTreeXmlBuilder addItems(Collection collection, String nameProperty, String valueProperty, String collapsedProperty, String urlProperty, boolean asCData) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            String name = BeanUtils.getProperty(element, nameProperty);
            String value = BeanUtils.getProperty(element, valueProperty);
            boolean collapsed = Boolean.getBoolean(BeanUtils.getProperty(element, collapsedProperty));
            String url = BeanUtils.getProperty(element, urlProperty);
            if (asCData) {
                items.add(new TreeItem(name, value, collapsed, url, false));
            } else {
                items.add(new TreeItem(name, value, collapsed, url, true));
            }
        }
        return this;
    }

    /**
     * Add items from a collection as CDATA element.
     * 
     * @param collection
     * @param nameProperty
     * @param valueProperty
     * @param collapsedProperty
     * @param urlProperty
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public AjaxTreeXmlBuilder addItemsAsCData(Collection collection, String nameProperty, String valueProperty, String collapsedProperty, String urlProperty) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return addItems(collection, nameProperty, valueProperty, collapsedProperty, urlProperty, true);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer xml = new StringBuffer().append("<?xml version=\"1.0\"");
        if (encoding != null) {
            xml.append(" encoding=\"");
            xml.append(encoding);
            xml.append("\"");
        }
        xml.append(" ?>");
        xml.append("<ajax-response>");
        xml.append("<response>");
        for (Iterator<TreeItem> iter = items.iterator(); iter.hasNext(); ) {
            TreeItem item = iter.next();
            xml.append("<item>");
            xml.append("<name>");
            if (item.isAsCData()) {
                xml.append("<![CDATA[");
            }
            xml.append(item.getName());
            if (item.isAsCData()) {
                xml.append("]]>");
            }
            xml.append("</name>");
            xml.append("<value>");
            if (item.isAsCData()) {
                xml.append("<![CDATA[");
            }
            xml.append(item.getValue());
            if (item.isAsCData()) {
                xml.append("]]>");
            }
            xml.append("</value>");
            xml.append("<collapsed>");
            xml.append(item.isCollapsed());
            xml.append("</collapsed>");
            xml.append("<url>");
            xml.append(item.getUrl());
            xml.append("</url>");
            xml.append("</item>");
        }
        xml.append("</response>");
        xml.append("</ajax-response>");
        return xml.toString();
    }
}
