package org.odlabs.wiquery.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryAjaxEventBehavior;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.plugin.ChilliPanel;
import org.odlabs.wiquery.ui.dialog.Dialog;

/**
 * @author lionel
 */
public class DialogPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private AjaxContent newContent = null;

    public DialogPanel(String id) {
        super(id);
        ChilliPanel chilliPlugin = new ChilliPanel("examples");
        this.add(chilliPlugin);
        Button button = new Button("openSimpleDialog");
        chilliPlugin.add(button);
        final Dialog dialog = new Dialog("simpleDialog");
        button.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {

            private static final long serialVersionUID = 1L;

            @Override
            public JsScope callback() {
                return JsScope.quickScope(dialog.open().render());
            }
        }));
        chilliPlugin.add(dialog);
        button = new Button("openAjaxDialog");
        chilliPlugin.add(button);
        final Dialog ajaxDialog = new Dialog("ajaxDialog");
        final WebMarkupContainer container = new WebMarkupContainer("ajaxContent");
        container.setOutputMarkupId(true);
        ajaxDialog.add(container);
        button.add(new WiQueryAjaxEventBehavior(MouseEvent.CLICK) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onEvent(AjaxRequestTarget target) {
                if (DialogPanel.this.newContent == null) {
                    DialogPanel.this.newContent = new AjaxContent("ajaxContent");
                    DialogPanel.this.newContent.setOutputMarkupId(true);
                    container.replaceWith(DialogPanel.this.newContent);
                    target.addComponent(DialogPanel.this.newContent, container.getMarkupId());
                }
                ajaxDialog.open(target);
            }
        });
        chilliPlugin.add(ajaxDialog);
    }
}
