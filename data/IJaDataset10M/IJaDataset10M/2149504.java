package org.mobicents.media.server.mgcp.pkg.dtmf;

import org.mobicents.media.server.mgcp.controller.signal.Event;
import org.mobicents.media.server.utils.Text;

/**
 *
 * @author kulikov
 */
public class DtmfC extends AbstractDtmfEvent {

    private static final Event dtmf_C = new Event(new Text("C"));

    public DtmfC(String name) {
        super(name);
    }

    @Override
    public void onEvent(String tone) {
        if (tone.equals("C")) {
            dtmf_C.fire(this, null);
        }
    }

    @Override
    protected Event getTone() {
        return dtmf_C;
    }
}
