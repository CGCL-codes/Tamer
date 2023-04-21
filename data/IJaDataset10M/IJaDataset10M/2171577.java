package org.ztemplates.web.ui.form.state;

import org.ztemplates.jquery.JQueryLoaderAction;
import org.ztemplates.render.ZExpose;
import org.ztemplates.render.ZJavaScript;
import org.ztemplates.render.ZRenderer;
import org.ztemplates.render.ZScript;
import org.ztemplates.render.velocity.ZVelocityRenderer;
import org.ztemplates.web.ui.form.script.assets.ZFormScriptLoaderAction;

@ZRenderer(ZVelocityRenderer.class)
@ZScript(javaScript = { @ZJavaScript(value = JQueryLoaderAction.JQUERY_MIN_JS, standalone = JQueryLoaderAction.STANDALONE, merge = JQueryLoaderAction.MERGE), @ZJavaScript(ZFormScriptLoaderAction.FORM_SCRIPT) })
public final class ZFormState {

    private final String formId;

    private final String displayId;

    public ZFormState(String formId, String displayId) {
        this.formId = formId;
        this.displayId = displayId;
    }

    @ZExpose
    public String getFormId() {
        return formId;
    }

    @ZExpose
    public String getDisplayId() {
        return displayId;
    }
}
