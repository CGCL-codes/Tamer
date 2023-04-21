package org.ufacekit.ui.core.internal;

import java.util.Collection;
import org.ufacekit.ui.core.UIApplicationContext;
import org.ufacekit.ui.core.UIFactory;
import org.ufacekit.ui.core.UIWidget;
import org.ufacekit.ui.core.UIWidgetContainer;
import org.ufacekit.ui.core.controls.UIWidgetControl;
import org.ufacekit.ui.core.controls.UIWindow;
import org.ufacekit.ui.core.controls.UIWidgetControl.UIWidgetControlInfo;
import org.ufacekit.ui.core.layouts.UILayoutData;
import org.ufacekit.ui.core.styles.StylableElement;

/**
 * Base class for {@link UIWidgetControl}.
 * 
 * @param <NativeWidget>
 *            the native widget
 * @since 1.0
 */
public abstract class UIWidgetBean<NativeWidget> extends UIElementBean<UIWidgetControlInfo> implements UIWidgetControl {

    private static final UIWidgetControl[] EMPTY_WIDGET = new UIWidgetControl[0];

    private UIWidgetContainer<?> parent;

    private NativeWidget nativeWidget;

    private UIWindow window;

    /**
	 * A new widget bean
	 * 
	 * @param factory
	 *            the factory that created the widget
	 * @param parent
	 *            the parent of the widget
	 * @param nativeWidget
	 *            the native widget
	 * @param uiInfo
	 *            the uiInfo
	 */
    public UIWidgetBean(UIFactory<?> factory, UIWidgetContainer<?> parent, NativeWidget nativeWidget, UIWidgetControlInfo uiInfo) {
        super(factory, uiInfo);
        this.parent = parent;
        this.nativeWidget = nativeWidget;
    }

    public UILayoutData getLayoutData() {
        return getUIInfo().getLayoutData();
    }

    public UIWidgetContainer<?> getParent() {
        return parent;
    }

    public UIWidgetControl getPreviousSibling() {
        UIWidgetControl[] widgets = getWidgets();
        if (widgets == null) return null;
        UIWidgetControl last = null;
        for (int i = 0; i < widgets.length; i++) {
            UIWidgetControl w = widgets[i];
            if (this.equals(w)) {
                return last;
            }
            last = w;
        }
        return null;
    }

    public UIWidgetControl getNextSibling() {
        UIWidgetControl[] widgets = getWidgets();
        if (widgets == null) return null;
        boolean founded = false;
        for (int i = 0; i < widgets.length; i++) {
            UIWidgetControl w = widgets[i];
            if (founded) return w;
            founded = (this.equals(w));
        }
        return null;
    }

    private UIWidgetControl[] getWidgets() {
        UIWidgetContainer<?> parent = getParent();
        if (parent != null) {
            Collection<?> children = parent.getChildren();
            if (children != null) {
                return children.toArray(EMPTY_WIDGET);
            }
        }
        return null;
    }

    public UIWindow getWindow() {
        if (window == null) {
            window = getWindow(this);
        }
        return window;
    }

    private UIWindow getWindow(UIWidget widget) {
        UIWidgetContainer<?> parent = widget.getParent();
        while (parent != null) {
            if (parent instanceof UIWindow) return (UIWindow) parent;
            parent = parent.getParent();
        }
        return null;
    }

    /**
	 * @return the native widget
	 */
    public NativeWidget getNativeWidget() {
        return nativeWidget;
    }

    public String getName() {
        return getUIInfo().getName();
    }

    public StylableElement getStylableElement() {
        return getUIInfo().getStylableElement();
    }

    public UIApplicationContext getApplicationContext() {
        return parent.getApplicationContext();
    }

    /**
	 * Handle  exception
	 * @param e
	 */
    protected void handleExceptions(Throwable e) {
        UIApplicationContext context = getApplicationContext();
        if (context != null) {
            context.handleExceptions(e);
        }
    }
}
