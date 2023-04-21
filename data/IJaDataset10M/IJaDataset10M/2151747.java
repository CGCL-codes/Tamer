package sf2.service.legacy.msg;

import java.io.IOException;
import java.io.Serializable;
import sf2.io.StreamFuture;
import sf2.vm.VMNetwork;

public class CommitedState implements Serializable {

    private static final long serialVersionUID = 1437862962429528352L;

    protected StreamFuture fullSnap;

    protected VMNetwork vnet;

    public CommitedState(String fullSnapPath, VMNetwork vnet) throws IOException {
        this.fullSnap = new StreamFuture(fullSnapPath);
        this.vnet = vnet;
    }

    public StreamFuture getFullSnap() {
        return fullSnap;
    }

    public VMNetwork getVMNetwork() {
        return vnet;
    }
}
