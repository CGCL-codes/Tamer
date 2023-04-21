package org.foment.gem.dialogs;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.foment.gem.Integrator;

/**
 *
 * @author ivan
 */
public class GemInternalDialog extends javax.swing.JInternalFrame {

    public GemInternalDialog() {
        attachToIntegrator();
    }

    public java.awt.Component getNavigatorComponent() {
        return null;
    }

    ;

    public static void setMainDesktopPane(javax.swing.JDesktopPane desktop) {
        _desktop = desktop;
    }

    public static javax.swing.JDesktopPane getMainDesktopPane() {
        return _desktop;
    }

    private static javax.swing.JDesktopPane _desktop = null;

    private void attachToIntegrator() {
        addInternalFrameListener(new InternalFrameAdapter() {

            public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {
                Integrator.getIntegrator().desktopFrameActivated(internalFrameEvent.getInternalFrame());
            }

            public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {
                Integrator.getIntegrator().desktopFrameDeactivated(internalFrameEvent.getInternalFrame());
            }
        });
    }
}
