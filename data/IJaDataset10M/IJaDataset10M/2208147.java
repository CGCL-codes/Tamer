package us.wthr.jdem846.ui.base;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JFrame;
import us.wthr.jdem846.exception.ComponentException;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;
import us.wthr.jdem846.ui.Disposable;

@SuppressWarnings("serial")
public class Frame extends JFrame {

    private static Log log = Logging.getLog(Frame.class);

    public void close() {
        try {
            disposeComponents();
        } catch (ComponentException e) {
            log.error("Error when disposing child components: " + e.getMessage(), e);
            e.printStackTrace();
        }
        dispose();
    }

    public static void dispose(Component component) throws ComponentException {
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                Frame.dispose(child);
            }
        }
        if (component instanceof Disposable) {
            Disposable disposableComponent = (Disposable) component;
            disposableComponent.dispose();
        }
    }

    public void disposeComponents() throws ComponentException {
        log.info("Frame dispose initiated");
        Frame.dispose(this);
    }
}
