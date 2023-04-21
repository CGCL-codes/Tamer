package org.garret.ptl.template.tags;

import org.garret.ptl.template.*;

/**
 * Blah blah
 *
 * @author Andrey Subbotin
 */
public class Radio implements ITag {

    private final TemplateComponent parent;

    private final String id;

    private final String group;

    private boolean checked = false;

    public Radio(TemplateComponent parent, String id, String group) {
        assert parent != null && id != null && id.length() > 0 && group != null && group.length() > 0;
        this.parent = parent;
        this.id = id;
        this.group = group;
        parent.addTag(this);
    }

    public final String id() {
        return id;
    }

    public boolean checked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void onAjax(IPageContext context, String action) {
    }

    public void attach(IPageContext context) {
        this.checked = id.equals(context.parameter(group));
    }

    public void renderHtml(IPageContext context, ITagAttributes attributes) {
        attributes.setAttribute("name", group);
        attributes.setAttribute("value", id);
        if (checked) attributes.setAttribute("checked", "checked");
    }
}
