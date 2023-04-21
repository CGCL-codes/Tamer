package info.jtrac.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * switches on css styling to highlight invalid form input
 */
public class ErrorHighlighter extends AbstractBehavior {

    private FormComponent fc;

    public ErrorHighlighter() {
    }

    public ErrorHighlighter(FormComponent fc) {
        this.fc = fc;
    }

    @Override
    public void bind(Component c) {
        if (c instanceof FormComponent) {
            fc = (FormComponent) c;
        }
    }

    @Override
    public void onComponentTag(Component c, ComponentTag tag) {
        if (!fc.isValid()) {
            tag.put("class", "error-input");
        }
    }
}
