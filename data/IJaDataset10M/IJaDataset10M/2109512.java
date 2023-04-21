package com.loribel.tools.sa.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import org.w3c.dom.Element;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_LabelIcon2Owner;
import com.loribel.commons.abstraction.GB_LabelIconOwner;
import com.loribel.commons.business.GB_BOMetaDataTools;
import com.loribel.commons.business.abstraction.GB_BOConfigurable;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.exception.GB_ConfigException;
import com.loribel.commons.exception.GB_XmlException;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.FTools;
import com.loribel.commons.util.GB_HtmlTidyTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.STools;
import com.loribel.commons.util.string.GB_SAAbstract;
import com.loribel.commons.xml.GB_ElementTools;
import com.loribel.commons.xml.GB_NodeList;
import com.loribel.tools.GB_ToolsInitializer;
import com.loribel.tools.office.GB_XmlWordTools;
import com.loribel.tools.sa.bo.GB_SAHtmlSiteMap2CsvBO;

/**
 * SA - HTML Tidy Cleaner.
 * 
 * @author Gr�gory Borelli
 */
public class GB_SAHtmlSiteMap2Csv extends GB_SAAbstract implements GB_LabelIconOwner, GB_LabelIcon2Owner, GB_BOConfigurable {

    static class Link {

        private String url;

        private String label;

        private List children;

        public Link() {
            super();
        }

        public Link(String a_label, String a_url) {
            super();
            setLabel(a_label);
            setUrl(a_url);
        }

        public void addChild(Link a_link) {
            getChildrenNotNull().add(a_link);
        }

        public List getChildren() {
            return children;
        }

        private List getChildrenNotNull() {
            if (children == null) {
                children = new ArrayList();
            }
            return children;
        }

        public String getLabel() {
            return label;
        }

        public String getUrl() {
            return url;
        }

        public void setLabel(String a_label) {
            label = a_label;
        }

        public void setUrl(String a_url) {
            url = a_url;
        }

        public void toDebug(int a_level) {
            String l_margin = STools.buildStringWithChar('\t', a_level);
            String l_margin2 = STools.buildStringWithChar('\t', 5 - a_level);
            System.out.println(l_margin + label + l_margin2 + url);
            int len = CTools.getSize(children);
            for (int i = 0; i < len; i++) {
                Link l_child = (Link) children.get(i);
                l_child.toDebug(a_level + 1);
            }
        }
    }

    public static void main(String[] args) {
        try {
            GB_ToolsInitializer.initAllForTest();
            String l_content = "<a href=A>titreA</a>" + "<ul>" + "<li><a href=1>line1</a></li>" + "<li><a href=2>line2</a>" + "<ul><li><a href=2.1>line2.1</a></li>" + "<li><a href=2.2>line2.2</a></li></ul>" + "<li>line3" + "<ul><li><a href=3.1>line3.1</a></li>" + "<li><a href=3.2>line3.2</a></li></ul>" + "<li><a href=4>line4</a></li></ul>" + "<a href=B>titreB</a>" + "<ul>" + "<li><a href=B1>lineB1</a></li></ul>";
            l_content = FTools.readFile(new File("C:/Temp/plan.txt"));
            GB_SAHtmlSiteMap2Csv l_sa = new GB_SAHtmlSiteMap2Csv();
            l_content = l_sa.doActionStr(l_content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private GB_SAHtmlSiteMap2CsvBO config;

    private int level;

    private Link linkRoot;

    public GB_SAHtmlSiteMap2Csv() {
        this(new GB_SAHtmlSiteMap2CsvBO());
    }

    public GB_SAHtmlSiteMap2Csv(GB_SAHtmlSiteMap2CsvBO a_config) {
        super();
        setConfig(a_config);
    }

    /**
     * Method used for ant task.
     */
    public void addConfiguredConfig(GB_SAHtmlSiteMap2CsvBO a_config) {
        setConfig(a_config);
    }

    /**
     * Ex�cute l'action. Effectue les remplacements.
     */
    public String doActionStr(String a_string) {
        if (STools.isNull(a_string)) {
            return a_string;
        }
        try {
            a_string = GB_XmlWordTools.epureNotStdCharacters(a_string);
            Element l_root = GB_HtmlTidyTools.parseHtml(a_string);
            linkRoot = new Link("ROOT", null);
            treatNode(linkRoot, l_root, true);
            linkRoot.toDebug(0);
            String retour = GB_HtmlTidyTools.toHtmlContent(l_root);
            return retour;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * getBOConfig
     * 
     * @return GB_SimpleBusinessObject
     */
    public GB_SimpleBusinessObject getBOConfig() {
        return config;
    }

    public GB_SAHtmlSiteMap2CsvBO getConfig() {
        return config;
    }

    /**
     * Returns labelIcon of this String action.
     */
    public GB_LabelIcon getLabelIcon() {
        return GB_BOMetaDataTools.getLabelIcon(GB_SAHtmlSiteMap2CsvBO.BO_NAME);
    }

    public GB_LabelIcon getLabelIcon2() {
        Icon l_icon = getLabelIcon().getIcon();
        String l_label = "";
        l_label = "HTML SiteMap to CSV";
        return GB_LabelIconTools.newLabelIcon(l_label, l_icon);
    }

    /**
     * setBOConfig
     * 
     * @param a_config GB_SimpleBusinessObject
     */
    public void setBOConfig(GB_SimpleBusinessObject a_config) throws GB_ConfigException {
        GB_SAHtmlSiteMap2CsvBO l_config = null;
        try {
            l_config = (GB_SAHtmlSiteMap2CsvBO) a_config;
        } catch (ClassCastException e) {
            throw new GB_ConfigException(e);
        }
        setConfig(l_config);
    }

    public void setConfig(GB_SAHtmlSiteMap2CsvBO a_config) {
        config = a_config;
    }

    private Link treatNode(Link a_link, Element a_node, boolean a_useSibling) throws GB_XmlException {
        if (a_node == null) {
            return null;
        }
        Link l_link = a_link;
        String l_name = a_node.getNodeName();
        if ("a".equals(l_name)) {
            Link l_link2 = treatNodeA(linkRoot, a_node);
            if (l_link2 != null) {
                return l_link2;
            } else {
                return l_link;
            }
        }
        if ("li".equals(l_name)) {
            l_link = treatNodeLi(a_link, a_node);
        }
        GB_NodeList l_nodes = GB_ElementTools.getChildElements(a_node);
        int len = CTools.getSize(l_nodes);
        for (int i = 0; i < len; i++) {
            Element l_node = (Element) l_nodes.get(i);
            l_link = treatNode(l_link, l_node, false);
        }
        return a_link;
    }

    private Link treatNodeA(Link a_parent, Element a_node) throws GB_XmlException {
        if ("true".equals(a_node.getAttribute("OK"))) {
            return null;
        }
        a_node.setAttribute("OK", "true");
        String l_label = GB_ElementTools.toText(a_node);
        String l_href = a_node.getAttribute("href");
        Link l_link = new Link(l_label, l_href);
        a_parent.addChild(l_link);
        return l_link;
    }

    private Link treatNodeLi(Link a_parent, Element a_node) throws GB_XmlException {
        Element l_nodeA = GB_ElementTools.getFirstChildElement(a_node, "a", -1);
        if (GB_ElementTools.getParentByName(l_nodeA, "li") != a_node) {
            Link l_link = new Link("???", "");
            a_parent.addChild(l_link);
            return l_link;
        } else {
            return treatNodeA(a_parent, l_nodeA);
        }
    }

    private void treatNodeUl(Element a_node) {
    }
}
