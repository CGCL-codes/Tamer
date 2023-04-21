package br.com.pbsoft.yacgs.views;

import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.pbsoft.yacgs.controller.Controller;

public abstract class BaseView extends JFrame implements InitializingBean {

    private static final long serialVersionUID = 1L;

    @Autowired
    private Controller controller;

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void registerComponent(JComponent arg0) {
        Method addActionListenerMethod;
        try {
            addActionListenerMethod = arg0.getClass().getMethod("addActionListener", ActionListener.class);
            addActionListenerMethod.invoke(arg0, controller);
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
    }

    @Override
    public final void afterPropertiesSet() throws Exception {
        if (controller == null) throw new BeanInitializationException("'controller' not set.");
        registerAllComponents();
        afterInitialization();
    }

    protected void afterInitialization() {
    }

    protected abstract JComponent[] getRegisterableComponents();

    private void registerAllComponents() {
        JComponent[] registerableComponents = getRegisterableComponents();
        if (registerableComponents != null) for (JComponent component : registerableComponents) registerComponent(component);
    }
}
