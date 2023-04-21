package org.dolmen.core.container;

import org.dolmen.core.container.components.Component;
import org.dolmen.core.container.components.ServiceComponent;

/**
 * Helper to implements {@link Container Container} interface
 * 
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public final class ContainerHelper {

    public static void callCheck(Component aComponent) throws Exception {
    }

    public static void callConfigure(Component aComponent, Object aConfiguration) throws Exception {
    }

    public static void callLoad(Component aComponent) throws Exception {
    }

    public static void callStart(Component aComponent) throws Exception {
        if (aComponent instanceof ServiceComponent) {
            ((ServiceComponent) aComponent).start();
        }
    }

    public static void callStop(Component aComponent) throws Exception {
        if (aComponent instanceof ServiceComponent) {
            ((ServiceComponent) aComponent).stop();
        }
    }

    public static void callUnload(Component aComponent) throws Exception {
    }

    private ContainerHelper() {
    }
}
