package com.gr.menuw.menu.item;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import com.gr.menuw.common.StringModel;

/**
 * 
 * @author Graham Rhodes 30 Dec 2010 15:59:48
 */
public abstract class AbstractMenuLink extends Panel implements IMenuItem {

    private static final long serialVersionUID = 1L;

    private final Label label;

    public AbstractMenuLink(String id, String label) {
        super(id);
        this.label = new Label("label", label);
    }

    @Override
    protected void onBeforeRender() {
        addOrReplace(getLink("link").add(label));
        super.onBeforeRender();
    }

    protected abstract AbstractLink getLink(String id);

    @Override
    public void updateLabel(String label) {
        this.label.setDefaultModel(new StringModel(label));
    }
}
