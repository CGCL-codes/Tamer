package com.icesoft.faces.component.menubar;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.ActionEvent;
import java.util.List;
import javax.faces.context.FacesContext;
import com.icesoft.util.pooling.CSSNamePool;

/**
 * MenuItem is a JSF component class that represent an ICEfaces menuItem.
 * <p>MenuItem components are the menu items contained by a menuBar. The action
 * and actionListener attributes operate in the same way as the standard
 * component attributes of the same name. The MenuItem component is only used in
 * the static approach to defining the heirarchy of menu items.
 * <p/>
 * This component extends the ICEfaces MenuItemBase component.
 * <p/>
 * By default the MenuItem is rendered by the "com.icesoft.faces.View" renderer
 * type.
 *
 * @author Chris Brown
 * @author gmccleary
 * @version 1.1
 */
public class MenuItem extends MenuItemBase {

    private String icon;

    private Object value;

    private String link;

    private String target;

    private MethodBinding action = null;

    private MethodBinding actionListener = null;

    private Boolean disabled = null;

    private String enabledOnUserRole = null;

    private String renderedOnUserRole = null;

    private String title;

    private String alt;

    private String onclick;

    /**
     * String constant defining default menu icon img
     */
    public static final String DEFAULT_ICON = "/xmlhttp/css/xp/css-images/menu_blank_icon.gif";

    private static final String DEFAULT_VALUE = "menu item default";

    private static final String DEFAULT_LINK = "javascript:;";

    public MenuItem() {
    }

    public String getRendererType() {
        return "com.icesoft.faces.View";
    }

    public String getComponentType() {
        return "com.icesoft.faces.MenuNode";
    }

    /**
     * <p>Return the value of the <code>COMPONENT_FAMILY</code> of this
     * component.</p>
     */
    public String getFamily() {
        return "com.icesoft.faces.MenuNode";
    }

    /**
     * <p>Return the value of the <code>icon</code> property.</p>
     */
    public String getIcon() {
        String ret = getSpecifiedIcon();
        if (ret != null) return ret;
        return DEFAULT_ICON;
    }

    public String getSpecifiedIcon() {
        if (icon != null) {
            return icon;
        }
        ValueBinding vb = getValueBinding("icon");
        if (vb != null) {
            return (String) vb.getValue(getFacesContext());
        }
        return null;
    }

    /**
     * <p>Set the value of the <code>icon</code> property.</p>
     */
    public void setIcon(String iconValue) {
        icon = iconValue;
    }

    /**
     * <p>Return the value of the <code>value</code> property.</p>
     */
    public Object getValue() {
        if (value != null) {
            return value;
        }
        ValueBinding vb = getValueBinding("value");
        if (vb != null) {
            return (String) vb.getValue(getFacesContext());
        }
        return DEFAULT_VALUE;
    }

    /**
     * <p>Set the value of the <code>value</code> property.</p>
     */
    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isLinkSpecified() {
        if (link != null) return true;
        ValueBinding vb = getValueBinding("link");
        if (vb != null) return true;
        return false;
    }

    /**
     * <p>Return the value of the <code>link</code> property.</p>
     */
    public String getLink() {
        if (link != null) {
            return link;
        }
        ValueBinding vb = getValueBinding("link");
        if (vb != null) {
            return (String) vb.getValue(getFacesContext());
        }
        return DEFAULT_LINK;
    }

    /**
     * <p>Set the value of the <code>link</code> property.</p>
     */
    public void setLink(String linkValue) {
        this.link = linkValue;
    }

    /**
     * <p>Return the value of the <code>target</code> property.</p>
     */
    public String getTarget() {
        if (target != null) {
            return target;
        }
        ValueBinding vb = getValueBinding("target");
        if (vb != null) {
            return (String) vb.getValue(getFacesContext());
        }
        return null;
    }

    /**
     * <p>Set the value of the <code>target</code> property.</p>
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * <p>Return the value of the <code>action</code> property.</p>
     */
    public MethodBinding getAction() {
        MethodBinding actionMB = super.getAction();
        if (actionMB != null) return actionMB;
        return action;
    }

    /**
     * <p>Set the value of the <code>action</code> property.</p>
     */
    public void setAction(MethodBinding action) {
        this.action = action;
    }

    /**
     * <p>Return the value of the <code>actionListener</code> property.</p>
     */
    public MethodBinding getActionListener() {
        return actionListener;
    }

    /**
     * <p>Set the value of the <code>actionListener</code> property.</p>
     */
    public void setActionListener(MethodBinding actionListener) {
        this.actionListener = actionListener;
    }

    public boolean hasActionOrActionListener() {
        if (getAction() != null) return true;
        if (getActionListener() != null) return true;
        ActionListener[] actionListeners = getActionListeners();
        if (actionListeners != null && actionListeners.length > 0) return true;
        return false;
    }

    public void queueEvent(FacesEvent e) {
        if ((e instanceof ActionEvent) && !this.equals(e.getComponent()) && getParent() != null) {
            getParent().queueEvent(e);
        } else {
            super.queueEvent(e);
        }
    }

    /**
     * <p>Set the value of the <code>disabled</code> property.</p>
     */
    public void setDisabled(boolean disabled) {
        this.disabled = new Boolean(disabled);
        ValueBinding vb = getValueBinding("disabled");
        if (vb != null) {
            vb.setValue(getFacesContext(), this.disabled);
            this.disabled = null;
        }
    }

    public boolean getDisabled() {
        return isDisabled();
    }

    /**
     * <p>Return the value of the <code>disabled</code> property.</p>
     */
    public boolean isDisabled() {
        if (!Util.isEnabledOnUserRole(this)) {
            return true;
        }
        if (disabled != null) {
            return disabled.booleanValue();
        }
        ValueBinding vb = getValueBinding("disabled");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }

    void addParameter(UIComponent link) {
        if (getChildCount() == 0) return;
        List children = getChildren();
        for (int i = 0; i < children.size(); i++) {
            UIComponent nextChild = (UIComponent) children.get(i);
            if (nextChild instanceof UIParameter) {
                UIParameter templateParam = (UIParameter) nextChild;
                UIParameter param = new UIParameter();
                param.setName(templateParam.getName());
                param.setValue(templateParam.getValue());
                link.getChildren().add(param);
            }
        }
    }

    /**
     * <p>Set the value of the <code>enabledOnUserRole</code> property.</p>
     *
     * @param enabledOnUserRole
     */
    public void setEnabledOnUserRole(String enabledOnUserRole) {
        this.enabledOnUserRole = enabledOnUserRole;
    }

    /**
     * <p>Return the value of the <code>enabledOnUserRole</code> property.</p>
     *
     * @return String enabledOnUserRole
     */
    public String getEnabledOnUserRole() {
        if (enabledOnUserRole != null) {
            return enabledOnUserRole;
        }
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>renderedOnUserRole</code> property.</p>
     *
     * @param renderedOnUserRole
     */
    public void setRenderedOnUserRole(String renderedOnUserRole) {
        this.renderedOnUserRole = renderedOnUserRole;
    }

    /**
     * <p>Return the value of the <code>renderedOnUserRole</code> property.</p>
     *
     * @return String renderedOnUserRole
     */
    public String getRenderedOnUserRole() {
        if (renderedOnUserRole != null) {
            return renderedOnUserRole;
        }
        ValueBinding vb = getValueBinding("renderedOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>title</code> property.</p>
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * <p>Return the value of the <code>renderedOnUserRole</code> property.</p>
     *
     * @return String renderedOnUserRole
     */
    public String getTitle() {
        if (title != null) {
            return title;
        }
        ValueBinding vb = getValueBinding("title");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Set the value of the <code>alt</code> property.</p>
     *
     * @param alt
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }

    /**
     * <p>Return the value of the <code>alt</code> property.</p>
     *
     * @return String alt
     */
    public String getAlt() {
        if (alt != null) {
            return alt;
        }
        ValueBinding vb = getValueBinding("alt");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Return the value of the <code>rendered</code> property.</p>
     *
     * @return boolean rendered
     */
    public boolean isRendered() {
        if (!Util.isRenderedOnUserRole(this)) {
            return false;
        }
        return super.isRendered();
    }

    private String styleClass;

    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     *
     * @param styleClass
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     *
     * @return String styleClass
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this, styleClass, CSS_DEFAULT.MENU_ITEM_STYLE, "styleClass", isDisabled());
    }

    public String getLabelStyleClass() {
        return Util.getQualifiedStyleClass(this, CSS_DEFAULT.MENU_ITEM_LABEL_STYLE, isDisabled());
    }

    public String getImageStyleClass() {
        return Util.getQualifiedStyleClass(this, CSS_DEFAULT.MENU_ITEM_IMAGE_STYLE);
    }

    String getUserDefinedStyleClass(String parentClass, String subClass) {
        String disSuffix = isDisabled() ? "-dis" : "";
        if (styleClass != null) {
            return CSSNamePool.get(parentClass + disSuffix + " " + styleClass + subClass + disSuffix);
        }
        ValueBinding vb = getValueBinding("styleClass");
        return vb != null ? parentClass + disSuffix + " " + (String) vb.getValue(getFacesContext()) + subClass + disSuffix : CSSNamePool.get(parentClass + disSuffix);
    }

    /**
     * <p>Set the value of the <code>onclick</code> property.</p>
     *
     * @param onclick
     */
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    /**
     * <p>Return the value of the <code>onclick</code> property.</p>
     *
     * @return String onclick
     */
    public String getOnclick() {
        if (onclick != null) {
            return onclick;
        }
        ValueBinding vb = getValueBinding("onclick");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    private transient Object states[];

    public Object saveState(FacesContext context) {
        if (states == null) {
            states = new Object[14];
        }
        states[0] = super.saveState(context);
        states[1] = saveAttachedState(context, action);
        states[2] = saveAttachedState(context, actionListener);
        states[3] = alt;
        states[4] = disabled;
        states[5] = enabledOnUserRole;
        states[6] = icon;
        states[7] = link;
        states[8] = onclick;
        states[9] = renderedOnUserRole;
        states[10] = styleClass;
        states[11] = target;
        states[12] = title;
        states[13] = saveAttachedState(context, value);
        return states;
    }

    public void restoreState(FacesContext context, Object state) {
        states = (Object[]) state;
        super.restoreState(context, states[0]);
        action = (MethodBinding) restoreAttachedState(context, states[1]);
        actionListener = (MethodBinding) restoreAttachedState(context, states[2]);
        alt = (String) states[3];
        disabled = (Boolean) states[4];
        enabledOnUserRole = (String) states[5];
        icon = (String) states[6];
        link = (String) states[7];
        onclick = (String) states[8];
        renderedOnUserRole = (String) states[9];
        styleClass = (String) states[10];
        target = (String) states[11];
        title = (String) states[12];
        value = restoreAttachedState(context, states[13]);
    }
}
