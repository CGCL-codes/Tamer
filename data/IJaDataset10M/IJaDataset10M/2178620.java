package com.softaspects.jsf.tag.panel;

import com.softaspects.jsf.tag.base.WGFComponentBaseTag;
import javax.el.ValueExpression;

/**
 * PanelHeader Component tag
 */
public class BasePanelHeaderTag extends WGFComponentBaseTag {

    public String getComponentType() {
        return "com.softaspects.jsf.component.panel.PanelHeader";
    }

    public String getRendererType() {
        return "com.softaspects.jsf.renderer.panel.PanelHeaderRenderer";
    }

    /**
     * Setter for property 'immediate'
     *
     * @param immediate 'immediate' property value
     */
    public void setImmediate(ValueExpression immediate) {
        setAttribute("immediate", immediate);
    }

    /**
     * Setter for property 'model'
     *
     * @param model 'model' property value
     */
    public void setModel(ValueExpression model) {
        setAttribute("model", model);
    }

    /**
     * Setter for property 'style'
     *
     * @param style 'style' property value
     */
    public void setStyle(ValueExpression style) {
        setAttribute("style", style);
    }

    /**
     * Setter for property 'styleClass'
     *
     * @param styleClass 'styleClass' property value
     */
    public void setStyleClass(ValueExpression styleClass) {
        setAttribute("styleClass", styleClass);
    }

    /**
     * Setter for property 'title'
     *
     * @param title 'title' property value
     */
    public void setTitle(ValueExpression title) {
        setAttribute("title", title);
    }

    /**
     * Setter for property 'valueExpression'
     *
     * @param valueExpression 'valueExpression' property value
     */
    public void setValueExpression(ValueExpression valueExpression) {
        setAttribute("valueExpression", valueExpression);
    }
}
