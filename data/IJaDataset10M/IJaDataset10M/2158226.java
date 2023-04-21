package desktop;

import org.wings.*;
import org.wings.io.Device;
import org.wings.plaf.css.AbstractComponentCG;
import java.io.IOException;

public class DpCG extends AbstractComponentCG implements org.wings.plaf.DesktopPaneCG {

    private static final long serialVersionUID = 1L;

    public void installCG(SComponent component) {
        super.installCG(component);
        component.setPreferredSize(SDimension.FULLWIDTH);
    }

    @Override
    public void writeInternal(Device device, SComponent component) throws IOException {
        DesktopPane desktop = (DesktopPane) component;
        writeDivPrefix(device, desktop, null);
        boolean maximized = false;
        device.print("<div class=\"spacer\"></div>");
        int componentCount = desktop.getComponentCount();
        for (int i = 0; i < componentCount; i++) {
            if (desktop.getComponent(i) instanceof SInternalFrame) {
                SInternalFrame frame = (SInternalFrame) desktop.getComponent(i);
                if (!frame.isClosed() && frame.isMaximized()) {
                    frame.write(device);
                    maximized = true;
                }
            }
        }
        if (!maximized) {
            for (int i = 0; i < componentCount; i++) {
                if (desktop.getComponent(i) instanceof SInternalFrame) {
                    SInternalFrame frame = (SInternalFrame) desktop.getComponent(i);
                    if (!frame.isClosed()) {
                        frame.write(device);
                    }
                }
            }
        }
        for (int i = 0; i < componentCount; i++) {
            if (desktop.getComponent(i) instanceof DropLabel) {
                DropLabel label = (DropLabel) desktop.getComponent(i);
                label.write(device);
            }
        }
        device.print("<div class=\"spacer\"></div>");
        writeDivSuffix(device, desktop);
    }
}
