package info.jtrac.wicket.yui;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * custom wicketized yahoo ui dialog widget
 */
public class YuiDialog extends Panel {

    public static final String CONTENT_ID = "content";

    private WebMarkupContainer dialog;

    private String heading;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public YuiDialog(String id) {
        super(id);
        add(HeaderContributor.forJavaScript("resources/yui/yahoo/yahoo-min.js"));
        add(HeaderContributor.forJavaScript("resources/yui/event/event-min.js"));
        add(HeaderContributor.forJavaScript("resources/yui/dom/dom-min.js"));
        add(HeaderContributor.forJavaScript("resources/yui/dragdrop/dragdrop-min.js"));
        add(HeaderContributor.forJavaScript("resources/yui/container/container-min.js"));
        add(HeaderContributor.forJavaScript("resources/yui/container/resize-dialog.js"));
        add(HeaderContributor.forCss("resources/yui/container/assets/container.css"));
        setOutputMarkupId(true);
        dialog = new WebMarkupContainer("dialog");
        dialog.setOutputMarkupId(true);
        dialog.setVisible(false);
        add(dialog);
        dialog.add(new Label("heading", new PropertyModel(this, "heading")));
        dialog.add(new WebMarkupContainer(CONTENT_ID));
    }

    public void show(AjaxRequestTarget target, String h, Component content) {
        this.heading = h;
        target.addComponent(this);
        dialog.setVisible(true);
        dialog.replace(content);
        final String markupId = dialog.getMarkupId();
        add(new HeaderContributor(new IHeaderContributor() {

            public void renderHead(IHeaderResponse response) {
                response.renderOnDomReadyJavascript("var " + markupId + " = new YAHOO.widget.ResizeDialog('" + markupId + "', " + " { constraintoviewport : true }); " + markupId + ".render(); " + markupId + ".show();");
            }
        }));
    }
}
