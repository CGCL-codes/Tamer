package org.middleheaven.ui.web.html.tags;

import javax.servlet.jsp.JspException;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIFieldInput;
import org.middleheaven.ui.models.UIFieldInputModel;
import org.middleheaven.ui.web.HtmlFieldInputModel;
import org.middleheaven.ui.web.tags.TagContext;

public class UIFieldInputTag extends AbstractUIComponentBodyTagSupport {

    private Object value;

    private String type;

    private boolean required = false;

    private boolean readOnly = false;

    private UIFieldInputModel model;

    private int maxLength;

    private int minLength;

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public void setModel(UIFieldInputModel model) {
        this.model = model;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public Class<? extends UIComponent> getComponentType() {
        return UIFieldInput.class;
    }

    public UIComponent getUIComponent() {
        this.setFamilly("field:" + type);
        UIFieldInput uic = (UIFieldInput) super.getUIComponent();
        uic.setReadState(UIReadState.computeFrom(this.getVisible(), this.getEnabled(), readOnly));
        return uic;
    }

    public int doStartTag() throws JspException {
        if (this.model == null) {
            this.model = new HtmlFieldInputModel();
        }
        return EVAL_BODY_BUFFERED;
    }

    protected void prepareRender(TagContext attributeContext) {
        super.prepareRender(attributeContext);
        model.setRequired(this.required);
        model.setEnabled(this.getEnabled());
        model.setValue(value);
        model.setMaxLength(maxLength);
    }

    @Override
    public void releaseState() {
    }

    @Override
    public UIModel getModel() {
        return model;
    }
}
