package org.mobicents.media.server.mgcp.pkg.dtmf;

import org.mobicents.media.server.mgcp.controller.signal.Event;
import org.mobicents.media.server.utils.Text;

/**
 *
 * @author kulikov
 */
public class Dtmf7 extends AbstractDtmfEvent {

    private static final Event dtmf_7 = new Event(new Text("7"));

    public Dtmf7(String name) {
        super(name);
    }

    @Override
    public void onEvent(String tone) {
        if (tone.equals("7")) {
            dtmf_7.fire(this, null);
        }
    }

    @Override
    protected Event getTone() {
        return dtmf_7;
    }
}
