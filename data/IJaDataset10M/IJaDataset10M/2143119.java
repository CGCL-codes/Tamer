package de.tudresden.inf.rn.mobilis.media.core.transfer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import de.tudresden.inf.rn.mobilis.media.core.ApplicationManager;
import de.tudresden.inf.rn.mobilis.mxa.ConstMXA;
import de.tudresden.inf.rn.mobilis.mxa.IXMPPService;
import de.tudresden.inf.rn.mobilis.mxa.services.parcelable.DiscoverItem;
import de.tudresden.inf.rn.mobilis.mxa.services.parcelable.FileTransfer;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPUtil;

public class OutgoingTransfer extends Transfer {

    private File localFile;

    public OutgoingTransfer(TransferManager manager, int id, FileTransfer xmppFile) {
        super(manager, id, xmppFile);
        this.xmppFile = xmppFile;
        this.localFile = new File(this.xmppFile.path);
    }

    @Override
    public long getTotalSize() {
        return this.localFile.length();
    }

    @Override
    public int getBlockSize() {
        return this.xmppFile.blockSize;
    }

    @Override
    protected boolean onReady() {
        final IXMPPService xmppService = this.manager.getService().getXmppService();
        final Messenger xmppMessenger = this.manager.getService().getXmppMessenger();
        try {
            if (!XMPPUtil.isFullyQualified(this.xmppFile.to)) xmppService.getServiceDiscoveryService().discoverItem(xmppMessenger, xmppMessenger, this.id, this.xmppFile.to, null); else this.onResourceDiscovered(null);
        } catch (RemoteException e) {
            this.notifyFailed(this, -2, "Could not initiate file transfer.");
        }
        return true;
    }

    protected void onResourceDiscovered(String fullyQualifiedJid) {
        final IXMPPService xmppService = this.manager.getService().getXmppService();
        final Messenger xmppMessenger = this.manager.getService().getXmppMessenger();
        try {
            if (fullyQualifiedJid != null) this.xmppFile.to = fullyQualifiedJid;
            xmppService.getFileTransferService().sendFile(xmppMessenger, this.id, this.xmppFile);
        } catch (RemoteException e) {
            this.notifyFailed(this, -2, "Could not initiate file transfer.");
        }
    }

    public void handleMessage(Message msg) {
        if (msg.what == ConstMXA.MSG_DISCOVER_ITEMS && msg.arg1 == ConstMXA.MSG_STATUS_SUCCESS) {
            msg.getData().setClassLoader(ApplicationManager.getInstance().getClassLoader());
            List<Parcelable> pp = (ArrayList<Parcelable>) msg.getData().getParcelableArrayList("DISCOVER_ITEMS");
            if (pp.size() > 0) this.onResourceDiscovered(((DiscoverItem) pp.get(0)).jid); else this.onError(-104, "Couldn't find out the roster items ressource.");
        } else super.handleMessage(msg);
    }

    @Override
    public boolean terminate() {
        return false;
    }
}
