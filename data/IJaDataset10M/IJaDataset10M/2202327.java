package com.antilia.web.scriptaculous.drag;

import java.util.HashMap;
import java.util.Map;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.Model;
import com.antilia.web.effect.JavascriptHelper;

/**
 * adds draggable behavior to any component.
 *
 * Can use a {@link DraggableTarget} to perform work when a Draggable object
 * is dropped on a component.
 *
 * @see http://wiki.script.aculo.us/scriptaculous/show/Draggable
 * @see DraggableTarget
 */
public abstract class DraggableBehavior extends ScriptaculousAjaxBehavior {

    private static final long serialVersionUID = 1L;

    private Map<String, Object> options = new HashMap<String, Object>();

    private boolean revert = Boolean.TRUE;

    protected void onBind() {
        super.onBind();
        getComponent().setOutputMarkupId(true);
        getComponent().add(new AttributeAppender("class", new Model<String>(getDraggableClassName()), " "));
    }

    @Override
    protected void respond(AjaxRequestTarget target) {
    }

    /**
	 * define the css style used to define this component.
	 * used by the {@link DraggableTarget} to declare what
	 * classes it accepts.
	 *
	 * @see DraggableTarget#accepts(DraggableImage)
	 * @return
	 */
    public abstract String getDraggableClassName();

    protected void onComponentRendered() {
        super.onComponentRendered();
        Response response = RequestCycle.get().getResponse();
        options.put("revert", isRevert());
        JavascriptHelper builder = new JavascriptHelper();
        builder.addLine("new Draggable(\"" + getComponent().getMarkupId() + "\", ");
        builder.addOptions(options);
        builder.addLine(");");
        response.write(builder.buildScript());
    }

    public boolean isRevert() {
        return revert;
    }

    public void setRevert(boolean revert) {
        this.revert = revert;
    }
}
