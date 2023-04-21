package com.ecyrd.jspwiki.htmltowiki;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.xpath.XPath;

/**
 * Converting XHtml to Wiki Markup.  This is the class which does all of the heavy loading.
 *
 * @author Sebastian Baltes (sbaltes@gmx.com)
 */
public class XHtmlElementToWikiTranslator {

    private static final String UTF8 = "UTF-8";

    private XHtmlToWikiConfig m_config;

    private WhitespaceTrimWriter m_outTimmer;

    private PrintWriter m_out;

    private LiStack m_liStack = new LiStack();

    private PreStack m_preStack = new PreStack();

    /**
     *  Create a new translator using the default config.
     *  
     *  @param base The base element from which to start translating.
     *  @throws IOException If reading of the DOM tree fails.
     *  @throws JDOMException If the DOM tree is faulty.
     */
    public XHtmlElementToWikiTranslator(Element base) throws IOException, JDOMException {
        this(base, new XHtmlToWikiConfig());
    }

    /**
     *  Create a new translator using the specified config.
     *  
     *  @param base The base element from which to start translating.
     *  @param config The config to use.
     *  @throws IOException If reading of the DOM tree fails.
     *  @throws JDOMException If the DOM tree is faulty.
     */
    public XHtmlElementToWikiTranslator(Element base, XHtmlToWikiConfig config) throws IOException, JDOMException {
        this.m_config = config;
        m_outTimmer = new WhitespaceTrimWriter();
        m_out = new PrintWriter(m_outTimmer);
        print(base);
    }

    /**
     *  FIXME: I have no idea what this does...
     * 
     *  @return Something.
     */
    public String getWikiString() {
        return m_outTimmer.toString();
    }

    private void print(String s) {
        s = StringEscapeUtils.unescapeHtml(s);
        m_out.print(s);
    }

    private void print(Object element) throws IOException, JDOMException {
        if (element instanceof Text) {
            Text t = (Text) element;
            String s = t.getText();
            if (m_preStack.isPreMode()) {
                m_out.print(s);
            } else {
                s = s.replaceAll("[\\r\\n\\f\\u0085\\u2028\\u2029]", "");
                m_out.print(s);
            }
        } else if (element instanceof Element) {
            Element base = (Element) element;
            String n = base.getName().toLowerCase();
            if ("imageplugin".equals(base.getAttributeValue("class"))) {
                printImage(base);
            } else if ("wikiform".equals(base.getAttributeValue("class"))) {
                printChildren(base);
            } else {
                boolean bold = false;
                boolean italic = false;
                boolean monospace = false;
                String cssSpecial = null;
                String cssClass = base.getAttributeValue("class");
                boolean ignoredCssClass = cssClass != null && cssClass.matches("wikipage|createpage|external|interwiki|attachment");
                Map styleProps = null;
                if (!n.equals("a")) {
                    styleProps = getStylePropertiesLowerCase(base);
                }
                if (styleProps != null) {
                    String fontFamily = (String) styleProps.get("font-family");
                    String whiteSpace = (String) styleProps.get("white-space");
                    if (fontFamily != null && (fontFamily.indexOf("monospace") >= 0 && whiteSpace != null && whiteSpace.indexOf("pre") >= 0)) {
                        styleProps.remove("font-family");
                        styleProps.remove("white-space");
                        monospace = true;
                    }
                    String weight = (String) styleProps.remove("font-weight");
                    String style = (String) styleProps.remove("font-style");
                    if (n.equals("p")) {
                        n = "div";
                    }
                    italic = "oblique".equals(style) || "italic".equals(style);
                    bold = "bold".equals(weight) || "bolder".equals(weight);
                    if (!styleProps.isEmpty()) {
                        cssSpecial = propsToStyleString(styleProps);
                    }
                }
                if (cssClass != null && !ignoredCssClass) {
                    if (n.equals("div")) {
                        m_out.print("\n%%" + cssClass + " \n");
                    } else if (n.equals("span")) {
                        m_out.print("%%" + cssClass + " ");
                    }
                }
                if (bold) {
                    m_out.print("__");
                }
                if (italic) {
                    m_out.print("''");
                }
                if (monospace) {
                    m_out.print("{{{");
                    m_preStack.push();
                }
                if (cssSpecial != null) {
                    if (n.equals("div")) {
                        m_out.print("\n%%(" + cssSpecial + " )\n");
                    } else {
                        m_out.print("%%(" + cssSpecial + " )");
                    }
                }
                printChildren(base);
                if (cssSpecial != null) {
                    if (n.equals("div")) {
                        m_out.print("\n%%\n");
                    } else {
                        m_out.print("%%");
                    }
                }
                if (monospace) {
                    m_preStack.pop();
                    m_out.print("}}}");
                }
                if (italic) {
                    m_out.print("''");
                }
                if (bold) {
                    m_out.print("__");
                }
                if (cssClass != null && !ignoredCssClass) {
                    if (n.equals("div")) {
                        m_out.print("\n%%\n");
                    } else if (n.equals("span")) {
                        m_out.print("%%");
                    }
                }
            }
        }
    }

    private void printChildren(Element base) throws IOException, JDOMException {
        for (Iterator i = base.getContent().iterator(); i.hasNext(); ) {
            Object c = i.next();
            if (c instanceof Element) {
                Element e = (Element) c;
                String n = e.getName().toLowerCase();
                if (n.equals("h1")) {
                    m_out.print("\n!!! ");
                    print(e);
                    m_out.println();
                } else if (n.equals("h2")) {
                    m_out.print("\n!!! ");
                    print(e);
                    m_out.println();
                } else if (n.equals("h3")) {
                    m_out.print("\n!! ");
                    print(e);
                    m_out.println();
                } else if (n.equals("h4")) {
                    m_out.print("\n! ");
                    print(e);
                    m_out.println();
                } else if (n.equals("p")) {
                    if (e.getContentSize() != 0) {
                        m_out.println();
                        print(e);
                        m_out.println();
                    }
                } else if (n.equals("br")) {
                    if (m_preStack.isPreMode()) {
                        m_out.println();
                    } else {
                        String parentElementName = base.getName().toLowerCase();
                        if (parentElementName.matches("p|div") && !base.getText().matches("(?s).*\\[\\{.*\\}\\].*")) {
                            m_out.print(" \\\\\n");
                        } else {
                            m_out.print(" \\\\");
                        }
                    }
                    print(e);
                } else if (n.equals("hr")) {
                    m_out.println();
                    print("----");
                    print(e);
                    m_out.println();
                } else if (n.equals("table")) {
                    if (!m_outTimmer.isCurrentlyOnLineBegin()) {
                        m_out.println();
                    }
                    print(e);
                } else if (n.equals("tr")) {
                    print(e);
                    m_out.println();
                } else if (n.equals("td")) {
                    m_out.print("| ");
                    print(e);
                    if (!m_preStack.isPreMode()) {
                        print(" ");
                    }
                } else if (n.equals("th")) {
                    m_out.print("|| ");
                    print(e);
                    if (!m_preStack.isPreMode()) {
                        print(" ");
                    }
                } else if (n.equals("a")) {
                    if (!isIgnorableWikiMarkupLink(e)) {
                        if (e.getChild("IMG") != null) {
                            printImage(e);
                        } else {
                            String ref = e.getAttributeValue("href");
                            if (ref == null) {
                                if (isUndefinedPageLink(e)) {
                                    m_out.print("[");
                                    print(e);
                                    m_out.print("]");
                                } else {
                                    print(e);
                                }
                            } else {
                                ref = trimLink(ref);
                                if (ref != null) {
                                    if (ref.startsWith("#")) {
                                        String href = ref.replaceFirst("#ref-.+-(\\d+)", "$1");
                                        String textValue = e.getValue().substring(1, (e.getValue().length() - 1));
                                        if (href.equals(textValue)) {
                                            print(e);
                                        } else {
                                            m_out.print("[" + textValue + "|" + href + "]");
                                        }
                                    } else {
                                        Map augmentedWikiLinkAttributes = getAugmentedWikiLinkAttributes(e);
                                        m_out.print("[");
                                        print(e);
                                        if (!e.getTextTrim().equalsIgnoreCase(ref)) {
                                            m_out.print("|");
                                            print(ref);
                                            if (!augmentedWikiLinkAttributes.isEmpty()) {
                                                m_out.print("|");
                                                String augmentedWikiLink = augmentedWikiLinkMapToString(augmentedWikiLinkAttributes);
                                                m_out.print(augmentedWikiLink);
                                            }
                                        } else if (!augmentedWikiLinkAttributes.isEmpty()) {
                                            m_out.print("|" + ref + "|");
                                            String augmentedWikiLink = augmentedWikiLinkMapToString(augmentedWikiLinkAttributes);
                                            m_out.print(augmentedWikiLink);
                                        }
                                        m_out.print("]");
                                    }
                                }
                            }
                        }
                    }
                } else if (n.equals("b") || n.equals("strong")) {
                    m_out.print("__");
                    print(e);
                    m_out.print("__");
                } else if (n.equals("i") || n.equals("em") || n.equals("address")) {
                    m_out.print("''");
                    print(e);
                    m_out.print("''");
                } else if (n.equals("u")) {
                    m_out.print("%%( text-decoration:underline; )");
                    print(e);
                    m_out.print("%%");
                } else if (n.equals("strike")) {
                    m_out.print("%%strike ");
                    print(e);
                    m_out.print("%%");
                } else if (n.equals("sup")) {
                    m_out.print("%%sup ");
                    print(e);
                    m_out.print("%%");
                } else if (n.equals("sub")) {
                    m_out.print("%%sub ");
                    print(e);
                    m_out.print("%%");
                } else if (n.equals("dl")) {
                    m_out.print("\n");
                    print(e);
                    m_out.print("\n");
                } else if (n.equals("dt")) {
                    m_out.print(";");
                    print(e);
                } else if (n.equals("dd")) {
                    m_out.print(":");
                    print(e);
                } else if (n.equals("ul")) {
                    m_out.println();
                    m_liStack.push("*");
                    print(e);
                    m_liStack.pop();
                } else if (n.equals("ol")) {
                    m_out.println();
                    m_liStack.push("#");
                    print(e);
                    m_liStack.pop();
                } else if (n.equals("li")) {
                    m_out.print(m_liStack + " ");
                    print(e);
                    boolean lastListItem = base.indexOf(e) == (base.getContentSize() - 2);
                    boolean sublistItem = m_liStack.toString().length() > 1;
                    if (!sublistItem || !lastListItem) {
                        m_out.println();
                    }
                } else if (n.equals("pre")) {
                    m_out.print("\n{{{");
                    m_preStack.push();
                    print(e);
                    m_preStack.pop();
                    m_out.print("}}}\n");
                } else if (n.equals("code") || n.equals("tt")) {
                    m_out.print("{{");
                    m_preStack.push();
                    print(e);
                    m_preStack.pop();
                    m_out.print("}}");
                } else if (n.equals("img")) {
                    if (!isIgnorableWikiMarkupLink(e)) {
                        m_out.print("[");
                        print(trimLink(e.getAttributeValue("src")));
                        m_out.print("]");
                    }
                } else if (n.equals("form")) {
                    Element formName = (Element) XPath.selectSingleNode(e, "INPUT[@name='formname']");
                    if (formName != null) {
                        formName.detach();
                    }
                    String name = e.getAttributeValue("name");
                    m_out.print("\n[{FormOpen");
                    if (name != null) {
                        m_out.print(" form='" + name + "'");
                    }
                    m_out.print("}]\n");
                    print(e);
                    m_out.print("\n[{FormClose}]\n");
                } else if (n.equals("input")) {
                    String type = e.getAttributeValue("type");
                    String name = e.getAttributeValue("name");
                    String value = e.getAttributeValue("value");
                    String checked = e.getAttributeValue("checked");
                    m_out.print("[{FormInput");
                    if (type != null) {
                        m_out.print(" type='" + type + "'");
                    }
                    if (name != null) {
                        if (name.startsWith("nbf_")) {
                            name = name.substring(4, name.length());
                        }
                        m_out.print(" name='" + name + "'");
                    }
                    if (value != null && !value.equals("")) {
                        m_out.print(" value='" + value + "'");
                    }
                    if (checked != null) {
                        m_out.print(" checked='" + checked + "'");
                    }
                    m_out.print("}]");
                    print(e);
                } else if (n.equals("textarea")) {
                    String name = e.getAttributeValue("name");
                    String rows = e.getAttributeValue("rows");
                    String cols = e.getAttributeValue("cols");
                    m_out.print("[{FormTextarea");
                    if (name != null) {
                        if (name.startsWith("nbf_")) {
                            name = name.substring(4, name.length());
                        }
                        m_out.print(" name='" + name + "'");
                    }
                    if (rows != null) {
                        m_out.print(" rows='" + rows + "'");
                    }
                    if (cols != null) {
                        m_out.print(" cols='" + cols + "'");
                    }
                    m_out.print("}]");
                    print(e);
                } else if (n.equals("select")) {
                    String name = e.getAttributeValue("name");
                    m_out.print("[{FormSelect");
                    if (name != null) {
                        if (name.startsWith("nbf_")) {
                            name = name.substring(4, name.length());
                        }
                        m_out.print(" name='" + name + "'");
                    }
                    m_out.print(" value='");
                    print(e);
                    m_out.print("'}]");
                } else if (n.equals("option")) {
                    if (base.indexOf(e) != 1) {
                        m_out.print(";");
                    }
                    Attribute selected = e.getAttribute("selected");
                    if (selected != null) {
                        m_out.print("*");
                    }
                    String value = e.getAttributeValue("value");
                    if (value != null) {
                        m_out.print(value);
                    } else {
                        print(e);
                    }
                } else {
                    print(e);
                }
            } else {
                print(c);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void printImage(Element base) throws JDOMException {
        Element child = (Element) XPath.selectSingleNode(base, "TBODY/TR/TD/*");
        if (child == null) {
            child = base;
        }
        Element img;
        String href;
        Map<Object, Object> map = new ForgetNullValuesLinkedHashMap();
        if (child.getName().equals("A")) {
            img = child.getChild("IMG");
            href = child.getAttributeValue("href");
        } else {
            img = child;
            href = null;
        }
        if (img == null) {
            return;
        }
        String src = trimLink(img.getAttributeValue("src"));
        if (src == null) {
            return;
        }
        map.put("align", base.getAttributeValue("align"));
        map.put("height", img.getAttributeValue("height"));
        map.put("width", img.getAttributeValue("width"));
        map.put("alt", img.getAttributeValue("alt"));
        map.put("caption", emptyToNull(XPath.newInstance("CAPTION").valueOf(base)));
        map.put("link", href);
        map.put("border", img.getAttributeValue("border"));
        map.put("style", base.getAttributeValue("style"));
        if (map.size() > 0) {
            m_out.print("[{Image src='" + src + "'");
            for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry entry = (Map.Entry) i.next();
                if (!entry.getValue().equals("")) {
                    m_out.print(" " + entry.getKey() + "='" + entry.getValue() + "'");
                }
            }
            m_out.print("}]");
        } else {
            m_out.print("[" + src + "]");
        }
    }

    private String emptyToNull(String s) {
        return s == null ? null : (s.replaceAll("\\s", "").length() == 0 ? null : s);
    }

    private String propsToStyleString(Map styleProps) {
        StringBuffer style = new StringBuffer();
        for (Iterator i = styleProps.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            style.append(" ").append(entry.getKey()).append(": ").append(entry.getValue()).append(";");
        }
        return style.toString();
    }

    private boolean isIgnorableWikiMarkupLink(Element a) {
        String ref = a.getAttributeValue("href");
        String clazz = a.getAttributeValue("class");
        return (ref != null && ref.startsWith(m_config.getPageInfoJsp())) || (clazz != null && clazz.trim().equalsIgnoreCase(m_config.getOutlink()));
    }

    /**
     * Checks if the link points to an undefined page.
     */
    private boolean isUndefinedPageLink(Element a) {
        String classVal = a.getAttributeValue("class");
        return classVal != null && classVal.equals("createpage");
    }

    /**
     *  Returns a Map containing the valid augmented wiki link attributes.
     */
    private Map getAugmentedWikiLinkAttributes(Element a) {
        Map<String, String> attributesMap = new HashMap<String, String>();
        String id = a.getAttributeValue("id");
        if (id != null && !id.equals("")) {
            attributesMap.put("id", id.replaceAll("'", "\""));
        }
        String cssClass = a.getAttributeValue("class");
        if (cssClass != null && !cssClass.equals("") && !cssClass.matches("wikipage|createpage|external|interwiki|attachment")) {
            attributesMap.put("class", cssClass.replaceAll("'", "\""));
        }
        String style = a.getAttributeValue("style");
        if (style != null && !style.equals("")) {
            attributesMap.put("style", style.replaceAll("'", "\""));
        }
        String title = a.getAttributeValue("title");
        if (title != null && !title.equals("")) {
            attributesMap.put("title", title.replaceAll("'", "\""));
        }
        String lang = a.getAttributeValue("lang");
        if (lang != null && !lang.equals("")) {
            attributesMap.put("lang", lang.replaceAll("'", "\""));
        }
        String dir = a.getAttributeValue("dir");
        if (dir != null && !dir.equals("")) {
            attributesMap.put("dir", dir.replaceAll("'", "\""));
        }
        String charset = a.getAttributeValue("charset");
        if (charset != null && !charset.equals("")) {
            attributesMap.put("charset", charset.replaceAll("'", "\""));
        }
        String type = a.getAttributeValue("type");
        if (type != null && !type.equals("")) {
            attributesMap.put("type", type.replaceAll("'", "\""));
        }
        String hreflang = a.getAttributeValue("hreflang");
        if (hreflang != null && !hreflang.equals("")) {
            attributesMap.put("hreflang", hreflang.replaceAll("'", "\""));
        }
        String rel = a.getAttributeValue("rel");
        if (rel != null && !rel.equals("")) {
            attributesMap.put("rel", rel.replaceAll("'", "\""));
        }
        String rev = a.getAttributeValue("rev");
        if (rev != null && !rev.equals("")) {
            attributesMap.put("rev", rev.replaceAll("'", "\""));
        }
        String accesskey = a.getAttributeValue("accesskey");
        if (accesskey != null && !accesskey.equals("")) {
            attributesMap.put("accesskey", accesskey.replaceAll("'", "\""));
        }
        String tabindex = a.getAttributeValue("tabindex");
        if (tabindex != null && !tabindex.equals("")) {
            attributesMap.put("tabindex", tabindex.replaceAll("'", "\""));
        }
        String target = a.getAttributeValue("target");
        if (target != null && !target.equals("")) {
            attributesMap.put("target", target.replaceAll("'", "\""));
        }
        return attributesMap;
    }

    /**
     * Converts the entries in the map to a string for use in a wiki link.
     */
    private String augmentedWikiLinkMapToString(Map attributesMap) {
        StringBuffer sb = new StringBuffer();
        for (Iterator itr = attributesMap.entrySet().iterator(); itr.hasNext(); ) {
            Map.Entry entry = (Map.Entry) itr.next();
            String attributeName = (String) entry.getKey();
            String attributeValue = (String) entry.getValue();
            sb.append(" " + attributeName + "='" + attributeValue + "'");
        }
        return sb.toString().trim();
    }

    private Map getStylePropertiesLowerCase(Element base) throws IOException {
        String n = base.getName().toLowerCase();
        String style = base.getAttributeValue("style");
        if (style == null) {
            style = "";
        }
        if (n.equals("p") || n.equals("div")) {
            String align = base.getAttributeValue("align");
            if (align != null) {
                if (style.indexOf("text-align") == -1) {
                    style = style + ";text-align:" + align + ";";
                }
            }
        }
        if (n.equals("font")) {
            String color = base.getAttributeValue("color");
            String face = base.getAttributeValue("face");
            String size = base.getAttributeValue("size");
            if (color != null) {
                style = style + "color:" + color + ";";
            }
            if (face != null) {
                style = style + "font-family:" + face + ";";
            }
            if (size != null) {
                if (size.equals("1")) {
                    style = style + "font-size:xx-small;";
                } else if (size.equals("2")) {
                    style = style + "font-size:x-small;";
                } else if (size.equals("3")) {
                    style = style + "font-size:small;";
                } else if (size.equals("4")) {
                    style = style + "font-size:medium;";
                } else if (size.equals("5")) {
                    style = style + "font-size:large;";
                } else if (size.equals("6")) {
                    style = style + "font-size:x-large;";
                } else if (size.equals("7")) {
                    style = style + "font-size:xx-large;";
                }
            }
        }
        if (style.equals("")) {
            return null;
        }
        style = style.replace(';', '\n').toLowerCase();
        LinkedHashMap m = new LinkedHashMap();
        new PersistentMapDecorator(m).load(new ByteArrayInputStream(style.getBytes()));
        return m;
    }

    private String trimLink(String ref) {
        if (ref == null) {
            return null;
        }
        try {
            ref = URLDecoder.decode(ref, UTF8);
            ref = ref.trim();
            if (ref.startsWith(m_config.getAttachPage())) {
                ref = ref.substring(m_config.getAttachPage().length());
            }
            if (ref.startsWith(m_config.getWikiJspPage())) {
                ref = ref.substring(m_config.getWikiJspPage().length());
                ref = ref.replaceFirst(".+#section-(.+)-(.+)", "$1#$2");
            }
            if (ref.startsWith(m_config.getEditJspPage())) {
                ref = ref.substring(m_config.getEditJspPage().length());
            }
            if (m_config.getPageName() != null) {
                if (ref.startsWith(m_config.getPageName())) {
                    ref = ref.substring(m_config.getPageName().length());
                }
            }
        } catch (UnsupportedEncodingException e) {
        }
        return ref;
    }

    private static class LiStack {

        private StringBuffer m_li = new StringBuffer();

        public void push(String c) {
            m_li.append(c);
        }

        public void pop() {
            m_li = m_li.deleteCharAt(m_li.length() - 1);
        }

        public String toString() {
            return m_li.toString();
        }
    }

    private class PreStack {

        private int m_pre = 0;

        public boolean isPreMode() {
            return m_pre > 0;
        }

        public void push() {
            m_pre++;
            m_outTimmer.setWhitespaceTrimMode(!isPreMode());
        }

        public void pop() {
            m_pre--;
            m_outTimmer.setWhitespaceTrimMode(!isPreMode());
        }
    }
}
