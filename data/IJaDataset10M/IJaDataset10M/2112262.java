package com.xsm.gwt.widgets.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * The HTMLIncludePanel allows you to specify a {@link Source} for HTML (just
 *  like {@link HTMLInclude} but in-addition it allows you to replace HTML 
 *  elements or their attributes.
 *  
 * @author Sony Mathew
 */
public class HTMLIncludePanel extends SimplePanel implements Source.Consumer {

    private final Source htmlSrc;

    private final String defaultHTML;

    private HTMLPanel htmlPanel = null;

    private Map<String, Widget> replaceWidgetsMap = new HashMap<String, Widget>();

    private Map<String, String[]> rewriteAttributesMap = new HashMap<String, String[]>();

    private List<ContentLoadedHandler> contentLoadedHandlers = new ArrayList<ContentLoadedHandler>();

    /**
     * Convenience method for {@link #HTMLIncludePanel(String, String)}
     *  Uses "Not-Available" as the default HTML.
     *  
     * author Sony Mathew
     */
    public HTMLIncludePanel(String url) {
        this(url, "<b>Not-Available</b>");
    }

    /**
     * Convenience method to obtain HTML from an {@link HttpSource}
     * 
     * @see #HTMLIncludePanel(Source, String)
     * 
     * @param url: provide the url whose content is HTML from which this 
     *  component will be built - and {@link HttpSource} will be created from it.  
     *      
     * author Sony Mathew
     */
    public HTMLIncludePanel(String url, String defaultHTML) {
        this(new HttpSource(url), defaultHTML);
    }

    /**
     * The HTML content from the Source is read only on an explicit refresh().
     *  Invoke refresh() to re-load or setHTML() explictly if you want to 
     *      override HTML content.
     *  
     * @param htmlSrc: Provide a {@link Source} for the HTML content. 
     *      
     * @param defaultHTML: The default HTML to display if the URL content
     *  could not be read.
     *
     * author Sony Mathew
     */
    public HTMLIncludePanel(Source htmlSrc, String defaultHTML) {
        this.htmlSrc = htmlSrc;
        this.htmlSrc.addConsumer(this);
        this.defaultHTML = defaultHTML;
    }

    /**
     * Set the HTML for this HTMLIncludePanel.
     * Don't need to explicity call this method, content will be read
     *  from the Source and set automatically.
     * 
     * author Sony Mathew
     */
    public void setHTML(String html) {
        this.htmlPanel = new HTMLPanel(html);
        this.setWidget(this.htmlPanel);
        apply();
    }

    /**
     * Return the HTML panel.
     */
    public HTMLPanel getHTMLPanel() {
        return this.htmlPanel;
    }

    /**
     * See if element exists.
     */
    public boolean containsElement(String id) {
        return (this.htmlPanel != null && this.htmlPanel.getElementById(id) != null);
    }

    /**
     * Return an HTML element by id.
     */
    public Element getElementById(String id) {
        if (this.htmlPanel != null) return this.htmlPanel.getElementById(id); else return null;
    }

    /**
     * Applies any changes to widgets followed by attributes.
     * 
     * author Sony Mathew
     */
    public HTMLIncludePanel apply() {
        redoReplaceWidgets();
        redoRewriteAttributes();
        return this;
    }

    /**
     * Replace the HTML element (identified by htmlElementId) 
     *  with the given widget, this widget's id is updated to replaced id.
     * 
     * author Sony Mathew
     */
    public void replaceElement(String htmlElementId, Widget w) {
        replaceWidgetsMap.put(htmlElementId, w);
        w.getElement().setAttribute("id", htmlElementId);
    }

    /**
     * Rewite an attribute of the an HTML element (identified by htmlElementId) 
     *      with the given attributeValue.
     * 
     * author Sony Mathew
     */
    public void rewriteAttribute(String htmlElementId, String attributeName, String attributeValue) {
        rewriteAttributesMap.put(htmlElementId, new String[] { attributeName, attributeValue });
    }

    /**
     * Convenience method for replacing an <A id="myLink" class="myLinkClass" href="dummy/url">
     *  elements with Hyperlink Widgets.  This method copies over the id and class
     *  attributes of the orignal <A> element to the replaced Hyperlink.  The Href attribute
     *  is ignored since the Hyperlink widget is expected to implement the linking functionality.
     *  
     * @param hyperLink : will be normalized using {@link GWTUtil#normalizeURL(String)}
     * 
     * author Sony Mathew
     */
    public HTMLIncludePanel rewriteLink(String htmlElementId, Hyperlink hyperLink) {
        throw new UnsupportedOperationException();
    }

    /**
     * Convenience method for 
     *  rewriteAttribute(htmlElementId, "href", GWTUtil.normalizeURL(linkUrl))}
     *  For <A> link elements only.
     *  
     * @param linkUrl : will be normalized using {@link GWTUtil#normalizeURL(String)}
     * 
     * @see #rewriteAttribute(String, String, String)
     * 
     * author Sony Mathew
     */
    public HTMLIncludePanel rewriteLink(String htmlElementId, String linkUrl) {
        rewriteAttribute(htmlElementId, "href", GWTUtil.normalizeURL(linkUrl));
        return this;
    }

    /**
     * Convenience method for 
     *  rewriteAttribute(htmlElementId, "class", cssClass)}
     * 
     * @see #rewriteAttribute(String, String, String)
     * 
     * author Sony Mathew
     */
    public HTMLIncludePanel rewriteCssClass(String htmlElementId, String cssClass) {
        rewriteAttribute(htmlElementId, "class", cssClass);
        return this;
    }

    /**
     * Convenience method for 
     *  rewriteAttribute(htmlElementId, "style", cssStyle)}
     * 
     * @see #rewriteAttribute(String, String, String)
     * 
     * author Sony Mathew
     */
    public HTMLIncludePanel rewriteCssStyle(String htmlElementId, String cssStyle) {
        rewriteAttribute(htmlElementId, "style", cssStyle);
        return this;
    }

    /**
     * the HTML has been updated, redo the widgets.
     * 
     * author Sony Mathew
     */
    private void redoReplaceWidgets() {
        if (htmlPanel == null) {
            return;
        }
        for (String id : replaceWidgetsMap.keySet()) {
            try {
                Element origElement = htmlPanel.getElementById(id);
                Widget replaceWidget = replaceWidgetsMap.get(id);
                copyEssentialsToEquiv(origElement, replaceWidget.getElement());
                this.htmlPanel.addAndReplaceElement(replaceWidget, id);
            } catch (java.util.NoSuchElementException x) {
                log("Could not place widget at id=[" + id + "], element with id not found", x);
            }
        }
    }

    /**
     * Copies over the style class from the given element to an appropriate element in the toElement.
     * Only checks top level and first child recursivley for equivalent element tags.
     *    
     * This is useful when replacing e.g. an <A> element with a decorated version e.g. <DIV><A></DIV>
     *     or even <DIV><DIV><A></DIV></DIV> and we want to copy over the exisiting style class of
     *     the original <A> element.
     *    
     * author Sony Mathew
     */
    private void copyEssentialsToEquiv(com.google.gwt.dom.client.Element fromElement, com.google.gwt.dom.client.Element toElement) {
        if (fromElement.getTagName().equals(toElement.getTagName())) {
            copyEssentials(fromElement, toElement);
        } else {
            com.google.gwt.dom.client.Element toElementChild = toElement.getFirstChildElement();
            if (toElementChild != null) {
                copyEssentialsToEquiv(fromElement, toElementChild);
            }
        }
    }

    /**
     * Copies over the essentials from one element to the other.
     * These include: class, style, text content.
     * 
     * author Sony Mathew
     */
    private void copyEssentials(com.google.gwt.dom.client.Element fromElement, com.google.gwt.dom.client.Element toElement) {
        String className = UIObject.getStyleName(fromElement);
        if (className != null && (className = className.trim()).length() > 0) {
            UIObject.setStyleName(toElement, className);
        }
        String nodetext = fromElement.getInnerText();
        if (nodetext != null && nodetext.length() > 0) {
            toElement.setInnerText(nodetext);
        }
    }

    /**
     * the HTML has been updated, redo the widgets.
     * 
     * author Sony Mathew
     */
    private void redoRewriteAttributes() {
        if (htmlPanel == null) {
            return;
        }
        for (String id : rewriteAttributesMap.keySet()) {
            Element element = htmlPanel.getElementById(id);
            if (element == null) {
                log("Could not place widget at id=[" + id + "], element with id not found", null);
            } else {
                String[] attrib = rewriteAttributesMap.get(id);
                element.setAttribute(attrib[0], attrib[1]);
            }
        }
    }

    /**
\     * @see com.xsm.gwt.widgets.client.Source.Consumer#setContent(com.xsm.gwt.widgets.client.Source, java.lang.String)
     */
    public void setContent(Source src, String content) {
        setHTML(content);
        for (ContentLoadedHandler handler : this.contentLoadedHandlers) {
            handler.onContentLoaded();
        }
    }

    /**
     * @see com.xsm.gwt.widgets.client.Source.Consumer#failedContentLoad(com.xsm.gwt.widgets.client.Source, java.lang.Throwable)
     */
    public void failedContentLoad(Source src, Throwable x) {
        log("Failed to retreive HTML content from [" + htmlSrc + "]", x);
        setHTML(this.defaultHTML);
    }

    /**
     * Refreshes the HTML content from the Source and substitutes any rewrites of attributes or replacements of eleemnts.
     * 
     * @see #setHTML(String)
     * 
     * author Sony Mathew
     */
    public HTMLIncludePanel refresh() {
        htmlSrc.refresh();
        return this;
    }

    private void log(String msg, Throwable x) {
        GWT.log(getClass().getName() + ">>" + msg, x);
    }

    /**
     * Use this to call an event when the content
     * has been loaded.
     *
     */
    public interface ContentLoadedHandler {

        public void onContentLoaded();
    }

    public void addContentLoadedHandler(ContentLoadedHandler handler) {
        this.contentLoadedHandlers.add(handler);
    }

    public void clearContentLoadedHandlers() {
        this.contentLoadedHandlers.clear();
    }
}
