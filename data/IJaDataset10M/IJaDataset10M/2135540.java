package org.ditchnet.jsp.taglib.tabs.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.ditchnet.jsp.util.JspResponseWriter;
import org.ditchnet.xml.Xhtml;
import com.frameworkset.common.tag.BaseBodyTag;

/**
 *	@author Todd Ditchendorf
 *	@since 0.8
 *	
 *	JSP Tag that renders individual tab pane components and their tabs.
 */
public final class TabPaneTag extends BaseBodyTag {

    private String id, tabTitle;

    private TabContainerTag tabContainer;

    private TabPane tab = null;

    private boolean enablesecurity = false;

    private boolean lazeload = false;

    private List listIFrame = null;

    private String action = null;

    private String resourceType = null;

    public void setId(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTabTitle(final String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public String getTabTitle() {
        return tabTitle;
    }

    public int doStartTag() throws JspException {
        super.doStartTag();
        this.listIFrame = new ArrayList();
        addToContainer();
        if (this.getTabContainer().isEnablecookie()) {
            getTabContainer().consumeCookie();
        }
        getTabContainer().consumeQueryStringParam();
        getTabContainer().determineSelectedIndex();
        return EVAL_BODY_BUFFERED;
    }

    public int doAfterBody() throws JspException {
        renderComponent();
        bodyContent.clearBody();
        return SKIP_BODY;
    }

    public TabContainerTag getTabContainer() {
        if (null == tabContainer) {
            tabContainer = (TabContainerTag) findAncestorWithClass(this, TabContainerTag.class);
        }
        return tabContainer;
    }

    private void addToContainer() {
        TabPane tab = new TabPane();
        this.tab = tab;
        tab.setId(this.getId());
        tab.setTabTitle(this.tabTitle);
        getTabContainer().addChild(tab);
    }

    public int doEndTag() throws JspException {
        int ret = super.doEndTag();
        reset();
        return ret;
    }

    /**
	 * �������е�����
	 *
	 */
    void reset() {
        id = null;
        tabTitle = null;
        tabContainer = null;
        tab = null;
        enablesecurity = false;
        action = null;
        resourceType = null;
        this.lazeload = false;
        this.listIFrame = null;
    }

    private void renderComponent() throws JspException {
        JspResponseWriter out = new JspResponseWriter();
        out.lineBreak();
        out.startElement(Xhtml.Tag.DIV);
        out.attribute(Xhtml.Attr.ID, id);
        out.attribute(Xhtml.Attr.CLASS, TabConstants.TAB_PANE_CLASS_NAME);
        if (isSelectedTab()) {
            out.attribute(Xhtml.Attr.STYLE, "display:block;");
        } else {
            out.attribute(Xhtml.Attr.STYLE, "display:none;");
            if (this.lazeload && this.listIFrame != null && this.listIFrame.size() > 0) {
                String iframeStr = "";
                out.startElement(Xhtml.Tag.INPUT);
                out.attribute(Xhtml.Attr.ID, id + "-flag");
                out.attribute(Xhtml.Attr.TYPE, "hidden");
                out.endElement(Xhtml.Tag.INPUT);
                for (int i = 0; i < this.listIFrame.size(); i++) {
                    out.startElement(Xhtml.Tag.INPUT);
                    out.attribute(Xhtml.Attr.ID, id + "-hidden");
                    out.attribute(Xhtml.Attr.TYPE, "hidden");
                    String[] str = (String[]) this.listIFrame.get(i);
                    out.attribute(Xhtml.Attr.IFRAMEID, str[0]);
                    out.attribute(Xhtml.Attr.IFRAMESRC, str[1]);
                    out.endElement(Xhtml.Tag.INPUT);
                }
            }
        }
        out.text(" ");
        String result = this.getBodyContent().getString();
        out.text(result);
        out.lineBreak();
        out.endElement(Xhtml.Tag.DIV);
        try {
            String ret = out.getBuffer().toString();
            bodyContent.getEnclosingWriter().print(ret);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isSelectedTab() {
        return tab.getId().equals(getTabContainer().getSelectedIndex());
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isEnablesecurity() {
        return enablesecurity;
    }

    public void setEnablesecurity(boolean enablesecurity) {
        this.enablesecurity = enablesecurity;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public boolean isLazeload() {
        return lazeload;
    }

    public void setLazeload(boolean lazeload) {
        this.lazeload = lazeload;
    }

    public List getListIFrame() {
        return listIFrame;
    }
}
