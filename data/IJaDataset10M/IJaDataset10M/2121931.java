package ie.lawlor.amvc.swing;

import ie.lawlor.amvc.IDialogEnabledView;
import ie.lawlor.amvc.IView;
import ie.lawlor.amvc.controller.Controller;
import ie.lawlor.amvc.event.Event;
import ie.lawlor.amvc.event.EventName;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import org.apache.log4j.Logger;

/**
 * @author blawlor
 *
 */
public abstract class JFrameView extends JFrame implements SwingView {

    protected SwingViewProxy viewProxy;

    protected Logger logger;

    /**
   * 
   */
    public JFrameView(String name) {
        super();
        initFrame(name);
    }

    public JFrameView() {
        super();
        initFrame("noName");
    }

    /**
   * @param arg0
   */
    public JFrameView(String name, GraphicsConfiguration arg0) {
        super(arg0);
        initFrame(name);
    }

    /**
   * @param arg0
   */
    public JFrameView(String name, String arg0) {
        super(arg0);
        initFrame(name);
    }

    /**
   * @param arg0
   * @param arg1
   */
    public JFrameView(String name, String arg0, GraphicsConfiguration arg1) {
        super(arg0, arg1);
        initFrame(name);
    }

    private void initFrame(String name) {
        viewProxy = new SwingViewProxy(name, this);
        logger = viewProxy.getLogger();
        setJMenuBar(new JMenuBar());
    }

    public void setViewVisible(boolean visible) {
        viewProxy.setViewVisible(visible);
    }

    public void eventIn(Event e) {
        viewProxy.eventIn(e);
    }

    public void fire(Event e) {
        viewProxy.fire(e);
    }

    public void fire(EventName name, Object payload) {
        viewProxy.fire(name, payload);
    }

    public void fire(EventName name) {
        viewProxy.fire(name);
    }

    public void addChild(IView childView, String viewName) {
        viewProxy.addChild(childView, viewName);
    }

    public void addChild(IView childView) {
        viewProxy.addChild(childView);
    }

    public void addDialogChild(IDialogEnabledView childView) {
        viewProxy.addDialogChild(childView);
    }

    public void setParent(IView view) {
        viewProxy.setParent(view);
    }

    public void setParent(IView view, String viewName) {
        viewProxy.setParent(view, viewName);
    }

    public IView getParentView() {
        return viewProxy.getParentView();
    }

    public Container getContainer(String viewName) {
        return getContentPane();
    }

    public void setController(Controller controller) {
        viewProxy.setController(controller);
    }

    public Controller getController() {
        return viewProxy.getController();
    }

    public Class getTriadClass() {
        return null;
    }

    public void setTriadClass(Object c) {
    }

    public void doHideView(Event e) {
        this.hide();
        System.exit(0);
    }

    public void doShowView(Event e) {
        this.show();
    }
}
