package net.simpleframework.web.page.component.ui.propeditor;

import java.util.ArrayList;
import java.util.Collection;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;
import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PropEditorBean extends AbstractContainerBean {

    private String title;

    private boolean titleToggle = true;

    private String jsLoadedCallback;

    private Collection<PropField> propFields;

    public PropEditorBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
        super(componentRegistry, pageDocument, element);
    }

    public Collection<PropField> getFormFields() {
        if (propFields == null) {
            propFields = new ArrayList<PropField>();
        }
        return propFields;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public boolean isTitleToggle() {
        return titleToggle;
    }

    public void setTitleToggle(final boolean titleToggle) {
        this.titleToggle = titleToggle;
    }

    public String getJsLoadedCallback() {
        return jsLoadedCallback;
    }

    public void setJsLoadedCallback(final String jsLoadedCallback) {
        this.jsLoadedCallback = jsLoadedCallback;
    }

    @Override
    protected String[] elementAttributes() {
        return new String[] { "jsLoadedCallback" };
    }
}
