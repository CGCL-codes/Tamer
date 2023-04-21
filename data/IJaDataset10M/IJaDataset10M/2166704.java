package org.viewaframework.view.delegator;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import org.viewaframework.controller.DefaultWindowController;
import org.viewaframework.controller.ViewController;
import org.viewaframework.view.ViewContainer;
import org.viewaframework.view.ViewException;

/**
 * @author mario
 *
 */
public class DialogViewClosingWindowDelegator implements Delegator {

    private static final String POINT = ".";

    private String componentName;

    /**
	 * @param componentName
	 */
    public DialogViewClosingWindowDelegator(String componentName) {
        this.componentName = componentName;
    }

    public void clean(ViewContainer arg0) throws ViewException {
        arg0.getViewControllerMap().get(arg0.getId() + POINT + getComponentName()).clear();
    }

    /**
	 * @return
	 */
    public String getComponentName() {
        return componentName;
    }

    public void inject(ViewContainer arg0) throws ViewException {
        Object list = arg0.getViewControllerMap().get(arg0.getId() + POINT + getComponentName());
        if (list == null) {
            List<ViewController<? extends EventListener, ? extends EventObject>> listilla = new ArrayList<ViewController<? extends EventListener, ? extends EventObject>>();
            listilla.add(new DefaultWindowController());
            arg0.getViewControllerMap().put(arg0.getId() + POINT + getComponentName(), listilla);
        }
    }
}
