package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.SRawTextComponent;
import org.wings.SConstants;
import org.wings.plaf.ComponentCG;
import org.wings.io.Device;
import java.io.IOException;
import java.io.Serializable;

public final class RawTextComponentCG implements ComponentCG, SConstants, Serializable {

    private static final long serialVersionUID = 1L;

    public void write(Device device, SComponent component) throws IOException {
        component.fireRenderEvent(SComponent.START_RENDERING);
        SRawTextComponent _c = (SRawTextComponent) component;
        device.print(_c.getText());
        component.fireRenderEvent(SComponent.DONE_RENDERING);
    }

    public void installCG(SComponent c) {
    }

    public void uninstallCG(SComponent c) {
    }

    public void componentChanged(SComponent c) {
    }
}
