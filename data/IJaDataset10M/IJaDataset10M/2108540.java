package org.eaiframework.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eaiframework.Component;
import org.eaiframework.ComponentController;
import org.eaiframework.ComponentLifecycleListener;

public abstract class AbstractComponentController implements ComponentController {

    private static Log log = LogFactory.getLog(AbstractComponentController.class);

    protected ComponentLifecycleListener listener;

    public void setComponentLifecycleListener(ComponentLifecycleListener listener) {
        this.listener = listener;
    }

    protected void notifyOnInit(Component component) {
        if (listener != null) {
            try {
                listener.onInit(component);
            } catch (Exception e) {
                log.error(getLogHead(component) + "Exception notifying onInit: " + e.getMessage(), e);
            }
        }
    }

    protected void notifyOnStart(Component component) {
        if (listener != null) {
            try {
                listener.onStart(component);
            } catch (Exception e) {
                log.error(getLogHead(component) + "Exception notifying onStart: " + e.getMessage(), e);
            }
        }
    }

    protected void notifyOnStop(Component component) {
        if (listener != null) {
            try {
                listener.onStop(component);
            } catch (Exception e) {
                log.error(getLogHead(component) + "Exception notifying onStop: " + e.getMessage(), e);
            }
        }
    }

    protected void notifyOnDestroy(Component component) {
        if (listener != null) {
            try {
                listener.onDestroy(component);
            } catch (Exception e) {
                log.error(getLogHead(component) + "Exception notifying onDestroy: " + e.getMessage(), e);
            }
        }
    }

    protected String getLogHead(String id) {
        return "[component=" + id + "] ";
    }

    protected String getLogHead(Component component) {
        return "[component=" + component.getComponentContext().getId() + "] ";
    }
}
