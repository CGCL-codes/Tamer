package net.simpleframework.web.page.component.ui.list;

import java.util.ArrayList;
import java.util.Collection;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;
import org.dom4j.Element;

public class ListBean extends AbstractContainerBean {

    private boolean header, footer;

    private String jsLoadedCallback;

    private String jsClickCallback, jsDblclickCallback;

    public ListBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
        super(componentRegistry, pageDocument, element);
    }

    public String getJsLoadedCallback() {
        return jsLoadedCallback;
    }

    public void setJsLoadedCallback(final String jsLoadedCallback) {
        this.jsLoadedCallback = jsLoadedCallback;
    }

    public String getJsClickCallback() {
        return jsClickCallback;
    }

    public void setJsClickCallback(final String jsClickCallback) {
        this.jsClickCallback = jsClickCallback;
    }

    public String getJsDblclickCallback() {
        return jsDblclickCallback;
    }

    public void setJsDblclickCallback(final String jsDblclickCallback) {
        this.jsDblclickCallback = jsDblclickCallback;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public boolean isHeader() {
        return header;
    }

    public void setFooter(boolean footer) {
        this.footer = footer;
    }

    public boolean isFooter() {
        return footer;
    }

    private Collection<ListNode> treeNodes;

    public Collection<? extends ListNode> getListNodes() {
        if (treeNodes == null) {
            treeNodes = new ArrayList<ListNode>();
        }
        return treeNodes;
    }

    @Override
    protected String[] elementAttributes() {
        return new String[] { "jsLoadedCallback", "jsClickCallback", "jsDblclickCallback" };
    }
}
