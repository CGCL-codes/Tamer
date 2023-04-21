package org.middleheaven.ui.desktop.swing;

import java.util.List;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JWindow;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.models.UIWindowModel;
import org.middleheaven.util.collections.DelegatingList;

public class SRawWindow extends JWindow implements UIWindow {

    UIWindowModel model;

    private String family;

    private String id;

    private UIComponent parent;

    public SRawWindow() {
    }

    @Override
    public UIWindowModel getUIModel() {
        return model;
    }

    @Override
    public void gainFocus() {
        this.requestFocus();
    }

    @Override
    public void addComponent(UIComponent component) {
        component.setUIParent(this);
        this.getContentPane().add((JComponent) component);
    }

    @Override
    public void removeComponent(UIComponent component) {
        this.getContentPane().remove((JComponent) component);
    }

    @Override
    public List<UIComponent> getChildrenComponents() {
        return new DelegatingList<UIComponent>() {

            @Override
            public UIComponent get(int index) {
                return (UIComponent) getContentPane().getComponent(index);
            }

            @Override
            public int size() {
                return getComponentCount();
            }
        };
    }

    @Override
    public int getChildrenCount() {
        return getComponentCount();
    }

    @Override
    public String getFamily() {
        return family;
    }

    @Override
    public String getGID() {
        return id;
    }

    @Override
    public <T extends UIComponent> Class<T> getType() {
        return (Class<T>) UIWindow.class;
    }

    @Override
    public UIComponent getUIParent() {
        return parent;
    }

    @Override
    public boolean isRendered() {
        return true;
    }

    @Override
    public void setFamily(String family) {
        this.family = family;
    }

    @Override
    public void setGID(String id) {
        this.id = id;
    }

    @Override
    public void setUIModel(UIModel model) {
        this.model = (UIWindowModel) model;
    }

    @Override
    public void setUIParent(UIComponent parent) {
        this.parent = parent;
    }

    @Override
    public UIPosition getPosition() {
        return new UIPosition(this.getX(), this.getY());
    }

    @Override
    public void setSize(UIDimension size) {
        this.setSize(size.getWidth(), size.getHeight());
    }

    @Override
    public UIDimension getDimension() {
        return new UIDimension(this.getWidth(), this.getHeight());
    }
}
