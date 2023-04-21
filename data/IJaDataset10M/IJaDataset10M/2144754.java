package org.jenia.faces.datatools.taglib;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import org.jenia.faces.datatools.component.html.HtmlCommandLinkSelector;

public class CommandLinkSelectorTag extends UIComponentELTag {

    private ValueExpression immediate;

    private MethodExpression action;

    private MethodExpression actionListener;

    private ValueExpression value;

    private ValueExpression target;

    private ValueExpression selection;

    private ValueExpression accesskey;

    private ValueExpression border;

    private ValueExpression dir;

    private ValueExpression lang;

    private ValueExpression onblur;

    private ValueExpression onclick;

    private ValueExpression ondblclick;

    private ValueExpression onfocus;

    private ValueExpression onkeydown;

    private ValueExpression onkeypress;

    private ValueExpression onkeyup;

    private ValueExpression onmousedown;

    private ValueExpression onmousemove;

    private ValueExpression onmouseout;

    private ValueExpression onmouseover;

    private ValueExpression onmouseup;

    private ValueExpression style;

    private ValueExpression styleClass;

    private ValueExpression styleSelected;

    private ValueExpression styleClassSelected;

    private ValueExpression tabindex;

    private ValueExpression title;

    private ValueExpression disabled;

    private ValueExpression disabledStyle;

    private ValueExpression disabledStyleClass;

    public void setAccesskey(ValueExpression accesskey) {
        this.accesskey = accesskey;
    }

    public void setDir(ValueExpression dir) {
        this.dir = dir;
    }

    public void setLang(ValueExpression lang) {
        this.lang = lang;
    }

    public void setOnblur(ValueExpression onblur) {
        this.onblur = onblur;
    }

    public void setOnclick(ValueExpression onclick) {
        this.onclick = onclick;
    }

    public void setOndblclick(ValueExpression ondblclick) {
        this.ondblclick = ondblclick;
    }

    public void setOnfocus(ValueExpression onfocus) {
        this.onfocus = onfocus;
    }

    public void setOnkeydown(ValueExpression onkeydown) {
        this.onkeydown = onkeydown;
    }

    public void setOnkeypress(ValueExpression onkeypress) {
        this.onkeypress = onkeypress;
    }

    public void setOnkeyup(ValueExpression onkeyup) {
        this.onkeyup = onkeyup;
    }

    public void setOnmousedown(ValueExpression onmousedown) {
        this.onmousedown = onmousedown;
    }

    public void setOnmousemove(ValueExpression onmousemove) {
        this.onmousemove = onmousemove;
    }

    public void setOnmouseout(ValueExpression onmouseout) {
        this.onmouseout = onmouseout;
    }

    public void setOnmouseover(ValueExpression onmouseover) {
        this.onmouseover = onmouseover;
    }

    public void setOnmouseup(ValueExpression onmouseup) {
        this.onmouseup = onmouseup;
    }

    public void setStyle(ValueExpression style) {
        this.style = style;
    }

    public void setStyleClass(ValueExpression styleClass) {
        this.styleClass = styleClass;
    }

    public void setStyleSelected(ValueExpression styleSelected) {
        this.styleSelected = styleSelected;
    }

    public void setStyleClassSelected(ValueExpression styleClassSelected) {
        this.styleClassSelected = styleClassSelected;
    }

    public void setTabindex(ValueExpression tabindex) {
        this.tabindex = tabindex;
    }

    public void setTitle(ValueExpression title) {
        this.title = title;
    }

    public String getRendererType() {
        return HtmlCommandLinkSelector.RENDERER_TYPE;
    }

    public void setBorder(ValueExpression border) {
        this.border = border;
    }

    public String getComponentType() {
        return HtmlCommandLinkSelector.COMPONENT_TYPE;
    }

    public void setSelection(ValueExpression selection) {
        this.selection = selection;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        HtmlCommandLinkSelector myComponent = null;
        try {
            myComponent = (HtmlCommandLinkSelector) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: HtmlSingleRowSelector.  Perhaps you're missing a tag?");
        }
        if (selection != null) {
            if (selection.isLiteralText()) {
                throw new RuntimeException("selection must be a value binding to a backing bean containing the selected object");
            } else {
                myComponent.setValueExpression("selection", selection);
            }
        }
        if (accesskey != null) {
            if (accesskey.isLiteralText()) {
                myComponent.setAccesskey(accesskey.getExpressionString());
            } else {
                myComponent.setValueExpression("accesskey", accesskey);
            }
        }
        if (dir != null) {
            if (dir.isLiteralText()) {
                myComponent.setDir(dir.getExpressionString());
            } else {
                myComponent.setValueExpression("dir", dir);
            }
        }
        if (lang != null) {
            if (lang.isLiteralText()) {
                myComponent.setLang(lang.getExpressionString());
            } else {
                myComponent.setValueExpression("lang", lang);
            }
        }
        if (onblur != null) {
            if (onblur.isLiteralText()) {
                myComponent.setOnblur(onblur.getExpressionString());
            } else {
                myComponent.setValueExpression("onblur", onblur);
            }
        }
        if (onclick != null) {
            if (onclick.isLiteralText()) {
                myComponent.setOnclick(onclick.getExpressionString());
            } else {
                myComponent.setValueExpression("onclick", onclick);
            }
        }
        if (ondblclick != null) {
            if (ondblclick.isLiteralText()) {
                myComponent.setOndblclick(ondblclick.getExpressionString());
            } else {
                myComponent.setValueExpression("ondblclick", ondblclick);
            }
        }
        if (onfocus != null) {
            if (onfocus.isLiteralText()) {
                myComponent.setOnfocus(onfocus.getExpressionString());
            } else {
                myComponent.setValueExpression("onfocus", onfocus);
            }
        }
        if (onkeydown != null) {
            if (onkeydown.isLiteralText()) {
                myComponent.setOnkeydown(onkeydown.getExpressionString());
            } else {
                myComponent.setValueExpression("onkeydown", onkeydown);
            }
        }
        if (onkeypress != null) {
            if (onkeypress.isLiteralText()) {
                myComponent.setOnkeypress(onkeypress.getExpressionString());
            } else {
                myComponent.setValueExpression("onkeypress", onkeypress);
            }
        }
        if (onkeyup != null) {
            if (onkeyup.isLiteralText()) {
                myComponent.setOnkeyup(onkeyup.getExpressionString());
            } else {
                myComponent.setValueExpression("onkeyup", onkeyup);
            }
        }
        if (onmousedown != null) {
            if (onmousedown.isLiteralText()) {
                myComponent.setOnmousedown(onmousedown.getExpressionString());
            } else {
                myComponent.setValueExpression("onmousedown", onmousedown);
            }
        }
        if (onmousemove != null) {
            if (onmousemove.isLiteralText()) {
                myComponent.setOnmousemove(onmousemove.getExpressionString());
            } else {
                myComponent.setValueExpression("onmousemove", onmousemove);
            }
        }
        if (onmouseout != null) {
            if (onmouseout.isLiteralText()) {
                myComponent.setOnmouseout(onmouseout.getExpressionString());
            } else {
                myComponent.setValueExpression("onmouseout", onmouseout);
            }
        }
        if (onmouseover != null) {
            if (onmouseover.isLiteralText()) {
                myComponent.setOnmouseover(onmouseover.getExpressionString());
            } else {
                myComponent.setValueExpression("onmouseover", onmouseover);
            }
        }
        if (onmouseup != null) {
            if (onmouseup.isLiteralText()) {
                myComponent.setOnmouseup(onmouseup.getExpressionString());
            } else {
                myComponent.setValueExpression("onmouseup", onmouseup);
            }
        }
        if (border != null) {
            if (border.isLiteralText()) {
                myComponent.setBorder(border.getExpressionString());
            } else {
                myComponent.setValueExpression("border", border);
            }
        }
        if (style != null) {
            if (style.isLiteralText()) {
                myComponent.setStyle(style.getExpressionString());
            } else {
                myComponent.setValueExpression("style", style);
            }
        }
        if (styleClass != null) {
            if (styleClass.isLiteralText()) {
                myComponent.setStyleClass(styleClass.getExpressionString());
            } else {
                myComponent.setValueExpression("styleClass", styleClass);
            }
        }
        if (styleSelected != null) {
            if (styleSelected.isLiteralText()) {
                myComponent.setStyleSelected(styleSelected.getExpressionString());
            } else {
                myComponent.setValueExpression("styleSelected", styleSelected);
            }
        }
        if (styleClassSelected != null) {
            if (styleClassSelected.isLiteralText()) {
                myComponent.setStyleClassSelected(styleClassSelected.getExpressionString());
            } else {
                myComponent.setValueExpression("styleClassSelected", styleClassSelected);
            }
        }
        if (tabindex != null) {
            if (tabindex.isLiteralText()) {
                myComponent.setTabindex(tabindex.getExpressionString());
            } else {
                myComponent.setValueExpression("tabindex", tabindex);
            }
        }
        if (title != null) {
            if (title.isLiteralText()) {
                myComponent.setTitle(title.getExpressionString());
            } else {
                myComponent.setValueExpression("title", title);
            }
        }
        if (immediate != null) {
            if (immediate.isLiteralText()) {
                myComponent.setImmediate(immediate.getExpressionString());
            } else {
                myComponent.setValueExpression("immediate", immediate);
            }
        }
        if (action != null) {
            myComponent.setAction(action);
        }
        if (actionListener != null) {
            myComponent.setActionListener(actionListener);
        }
        if (value != null) {
            if (value.isLiteralText()) {
                myComponent.setValue(value.getExpressionString());
            } else {
                myComponent.setValueExpression("value", value);
            }
        }
        if (target != null) {
            if (target.isLiteralText()) {
                myComponent.setTarget(target.getExpressionString());
            } else {
                myComponent.setValueExpression("target", target);
            }
        }
        if (disabled != null) {
            if (disabled.isLiteralText()) {
                myComponent.setDisabled(disabled.getExpressionString());
            } else {
                myComponent.setValueExpression("disabled", disabled);
            }
        }
        if (disabledStyle != null) {
            if (disabledStyle.isLiteralText()) {
                myComponent.setDisabledStyle(disabledStyle.getExpressionString());
            } else {
                myComponent.setValueExpression("disabledStyle", disabledStyle);
            }
        }
        if (disabledStyleClass != null) {
            if (disabledStyleClass.isLiteralText()) {
                myComponent.setDisabledStyleClass(disabledStyleClass.getExpressionString());
            } else {
                myComponent.setValueExpression("disabledStyleClass", disabledStyleClass);
            }
        }
    }

    public void setAction(MethodExpression action) {
        this.action = action;
    }

    public void setActionListener(MethodExpression actionListener) {
        this.actionListener = actionListener;
    }

    public void setImmediate(ValueExpression immediate) {
        this.immediate = immediate;
    }

    public void setTarget(ValueExpression target) {
        this.target = target;
    }

    public void setValue(ValueExpression value) {
        this.value = value;
    }

    public void setDisabled(ValueExpression disabled) {
        this.disabled = disabled;
    }

    public void setDisabledStyle(ValueExpression disabledStyle) {
        this.disabledStyle = disabledStyle;
    }

    public void setDisabledStyleClass(ValueExpression disabledStyleClass) {
        this.disabledStyleClass = disabledStyleClass;
    }
}
