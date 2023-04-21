package org.clico.swing.impl;

import java.io.File;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.clico.ClientContainer.Mode;
import org.clico.impl.MVCImpl;
import org.clico.services.config.LocalisationConfig;
import org.clico.swing.SwingMVC;
import org.clico.swing.ViewInitializer;
import org.clico.swing.impl.view.EasyFormLayoutImpl;
import org.clico.swing.impl.view.LocalisationStage;
import org.clico.swing.impl.view.ValidationStage;
import org.clico.swing.view.VisualFeedback;
import org.picocontainer.PicoContainer;

public class SwingMVCImpl<M, V, C> extends MVCImpl<M, V, C> implements SwingMVC<M, V, C> {

    private static final long serialVersionUID = 3258133569926018870L;

    /**
     * The JComponent client property key for the MVC property 
     * that links the view to its container.
     * @see #getView()
     */
    public static final String CONTAINER_KEY = "clico.container";

    public static JComponent getParentView(JComponent component) {
        JComponent c = component;
        while (c.getClientProperty(CONTAINER_KEY) == null) {
            assert (c.getParent() != null) : "Component " + c + " has no parent";
            c = (JComponent) c.getParent();
        }
        return c;
    }

    public static Object getModel(JComponent component) {
        JComponent parent = getParentView(component);
        SwingMVCImpl<?, ?, ?> mvc = (SwingMVCImpl) parent.getClientProperty(CONTAINER_KEY);
        return mvc.getModel();
    }

    public static boolean isView(JComponent component) {
        return component.getClientProperty(CONTAINER_KEY) != null;
    }

    static void setMVC(JComponent view, SwingMVCImpl container) {
        view.putClientProperty(CONTAINER_KEY, container);
    }

    public SwingMVCImpl(Class<M> modelClass, Class<V> viewClass, Class<C> controllerClass, PicoContainer parent) {
        super(modelClass, viewClass, controllerClass, parent);
        init(viewClass);
    }

    public SwingMVCImpl(Class<M> modelClass, Class<V> viewClass, Class<C> controllerClass, PicoContainer parent, Mode mode, File srcRoot) {
        super(modelClass, viewClass, controllerClass, parent, mode, srcRoot);
        init(viewClass);
    }

    private void init(Class<V> viewClass) {
        registerComponentImplementation(EasyFormLayoutImpl.class);
        registerComponentInstance(new ViewInitializerImpl(this));
    }

    @Override
    public void configureLocalisation(LocalisationConfig config) {
        super.configureLocalisation(config);
        registerComponentImplementation(LocalisationStage.class);
    }

    @Override
    protected void configureValidation() {
        registerComponentImplementation(ValidationStage.class);
    }

    @Override
    protected void initView(Object view) {
        super.initView(view);
        if (view instanceof JPanel) {
            registerComponentInstance(JPanel.class, view);
        }
        if (view.getClass().isAnnotationPresent(VisualFeedback.class)) {
            configureValidation();
        }
        ViewInitializer initializer = (ViewInitializer) getComponentInstanceOfType(ViewInitializer.class);
        JComponent component = getComponent(view);
        initializer.init(component);
    }

    private JComponent getComponent(Object view) {
        if (view instanceof JComponent) {
            return (JComponent) view;
        }
        throw new UnsupportedOperationException("Doesn't work with Views that are not JComponent yet");
    }
}
