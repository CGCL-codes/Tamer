package gnu.java.awt.peer.qt;

import java.awt.Component;
import java.awt.peer.WindowPeer;
import gnu.java.awt.peer.EmbeddedWindowPeer;

/** 
 * Embedded window peer for applets.
 * FIXME: EmbeddedWindowPeer and this class should extend Window, NOT Frame.
 */
public class QtEmbeddedWindowPeer extends QtFramePeer implements EmbeddedWindowPeer {

    public QtEmbeddedWindowPeer(QtToolkit kit, Component owner) {
        super(kit, owner);
    }

    protected native void init();

    protected void setup() {
        super.setup();
    }

    public native void embed(long handle);
}
