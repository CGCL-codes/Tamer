package org.jawara.modules;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.StringWriter;
import java.util.Collection;
import org.jawara.lang.IO;
import org.jawara.lang.Lisp;
import org.piax.trans.IdResolver;
import org.piax.trans.LocatorTransport;
import org.piax.trans.common.*;
import org.piax.trans.msgframe.CallerHandle;
import org.piax.trans.msgframe.MessageSendReceiver;
import org.piax.trans.msgframe.NoSuchPeerException;

class JawaraIdResolver extends IdResolver {

    private boolean isActive;

    LocatorTransport locTrans;

    PeerId id;

    Lisp lisp;

    static final byte[] ID_RES_MAGIC = { (byte) 0x155 };

    public JawaraIdResolver(PeerId id, LocatorTransport locTrans, Lisp lisp) {
        super(ID_RES_MAGIC, locTrans);
        this.id = id;
        this.locTrans = locTrans;
        this.lisp = lisp;
        isActive = false;
    }

    public void acceptChange(PeerLocator newLoc) {
    }

    public boolean activate() throws IllegalStateException, IOException {
        isActive = true;
        return true;
    }

    public boolean activate(Collection<PeerLocator> seeds) throws IllegalStateException, IOException {
        isActive = true;
        return true;
    }

    public boolean inactivate() throws IllegalStateException, IOException {
        isActive = false;
        return true;
    }

    public boolean isActive() {
        return isActive;
    }

    protected PeerLocator lookupRemoteLocator(PeerId peerId) {
        return null;
    }

    public void receive(byte[] data, CallerHandle replyHandle) {
        Object res = null;
        String sid = null;
        try {
            sid = new String(data);
        } catch (Throwable e) {
        }
        try {
            if (sid != null) {
                PeerLocator s = (PeerLocator) replyHandle;
                put(new PeerId(sid), s);
            }
            locTrans.reply(replyHandle, id.toString().getBytes());
        } catch (Exception e) {
        }
    }

    public PeerId getPeerId(PeerLocator locator) throws InterruptedIOException, NoSuchPeerException, IOException {
        byte[] iddata = sendSync(locator, ((String) id.toString()).getBytes(), 10000);
        String id = new String(iddata);
        return new PeerId(id);
    }
}
