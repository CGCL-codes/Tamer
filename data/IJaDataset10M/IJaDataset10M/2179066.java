package com.softaspects.jsf.component.container;

import com.softaspects.jsf.component.base.BaseComponentConsts;
import com.softaspects.jsf.component.base.ComponentConsts;
import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * AbsoluteLayoutManager Component
 */
public class AbsoluteLayoutManager extends BaseAbsoluteLayoutManager implements LayoutManager {

    private String fWidth = null;

    private String fHeight = null;

    private String fTop = null;

    private String fLeft = null;

    private List<UIComponent> fComponentsList = new ArrayList<UIComponent>();

    public Collection<UIComponent> getComponents() {
        return fComponentsList;
    }

    public void removeAllLayoutComponents() {
        getComponents().clear();
    }

    public void addLayoutComponent(UIComponent aComponent) {
        if (aComponent != null) {
            getComponents().add(aComponent);
        }
    }

    public void doLayout(ContainerInterface container) {
        removeAllLayoutComponents();
        for (UIComponent uiComponent : container.getChildren()) {
            if (uiComponent instanceof LayoutManager) continue;
            getComponents().add(uiComponent);
        }
    }

    public void setWidth(String aWidth) {
        validateValue(aWidth, BaseComponentConsts.WIDTH_PROPERTY);
        fWidth = aWidth;
    }

    public String getWidth() {
        return fWidth;
    }

    public void setHeight(String aHeight) {
        validateValue(aHeight, BaseComponentConsts.HEIGHT_PROPERTY);
        fHeight = aHeight;
    }

    public String getHeight() {
        return fHeight;
    }

    public void setTop(String aTop) {
        validateValue(aTop, BaseComponentConsts.TOP_PROPERTY);
        fTop = aTop;
    }

    public String getTop() {
        return fTop;
    }

    public void setLeft(String aLeft) {
        validateValue(aLeft, BaseComponentConsts.LEFT_PROPERTY);
        fLeft = aLeft;
    }

    public String getLeft() {
        return fLeft;
    }

    protected String getInnerRendererType() {
        return ComponentConsts.CONTAINER_RENDERER_TYPE_ABSOLUTE;
    }

    private void validateValue(String aValue, String aName) {
        if (aValue == null || aValue.length() == 0) {
            throw new IllegalArgumentException("AbsoluteLayoutManager: " + aName + " value is invalid.");
        }
    }

    public Renderer getRenderer(FacesContext context) {
        return super.getRenderer(context);
    }

    public boolean ignoreParentStyle() {
        return true;
    }
}
