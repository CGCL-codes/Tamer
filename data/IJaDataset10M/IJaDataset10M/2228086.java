package sf2.service.legacy;

import java.io.File;
import java.io.IOException;
import sf2.core.Sustainable;
import sf2.io.StreamFuture;
import sf2.log.Logging;
import sf2.service.legacy.msg.CommitedState;
import sf2.service.legacy.msg.LauchVM;
import sf2.service.legacy.msg.LauchVMResponse;
import sf2.service.legacy.msg.NullOp;
import sf2.service.legacy.msg.NullResponse;
import sf2.service.legacy.msg.TakeSnapShot;
import sf2.view.impl.paxos.PaxosChooser;
import sf2.view.impl.paxos.PaxosCommitter;
import sf2.view.impl.paxos.PaxosExecutor;
import sf2.vm.VMException;
import sf2.vm.VMNetwork;
import sf2.vm.VMSnapShot;
import sf2.vm.VirtualMachine;
import sf2.vm.VirtualMachineFactory;

public class LegacyExecutor implements PaxosExecutor, PaxosChooser, PaxosCommitter {

    protected static final String LOG_NAME = "Executor";

    protected static final boolean ENABLE_STAT = true;

    protected Logging logging = Logging.getInstance();

    protected VirtualMachineFactory factory = VirtualMachineFactory.getInstance();

    protected VirtualMachine virtualMachine;

    protected VMSnapShot fullSnap;

    protected VMNetwork vnet;

    protected LegacyService service;

    public LegacyExecutor(VirtualMachine vm, LegacyService service, Sustainable core) {
        this.virtualMachine = vm;
        this.service = service;
    }

    public VMSnapShot getFullSnap() {
        return fullSnap;
    }

    public Object choose(Object req) {
        if (req instanceof LauchVM) return chooseLauchVM((LauchVM) req); else if (req instanceof TakeSnapShot) return chooseTakeSnapShot((TakeSnapShot) req); else if (req instanceof NullOp) ;
        return null;
    }

    protected Object chooseLauchVM(LauchVM req) {
        try {
            logging.debug(LOG_NAME, "lauchVM (choose)");
            logging.debug(LOG_NAME, "VM=" + virtualMachine);
            logging.debug(LOG_NAME, "RUNNING=" + virtualMachine.isRunning());
            fullSnap = virtualMachine.takeFullSnap();
            return new StreamFuture(fullSnap.getPath());
        } catch (VMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Object chooseTakeSnapShot(TakeSnapShot req) {
        try {
            logging.debug(LOG_NAME, "incrementalSnap (choose)");
            long start = System.currentTimeMillis();
            VMSnapShot delta = virtualMachine.takeIncrementalSnap();
            long end = System.currentTimeMillis();
            if (ENABLE_STAT) logging.info(LOG_NAME, "snap time: " + (end - start) + " [ms]");
            return new StreamFuture(delta.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (VMException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object execute(Object req, Object extra) {
        if (req instanceof LauchVM) return launchVM((LauchVM) req, (StreamFuture) extra); else if (req instanceof TakeSnapShot) return takeSnapShot((TakeSnapShot) req, (StreamFuture) extra); else if (req instanceof NullOp) return new NullResponse();
        return null;
    }

    protected Object launchVM(LauchVM init, StreamFuture snap) {
        try {
            if (fullSnap == null) fullSnap = factory.createVMSnapShot();
            fullSnap.load(snap.getPath());
            vnet = init.getVMNetwork();
            service.setVMNetwork(vnet);
        } catch (VMException e) {
            e.printStackTrace();
        }
        return new LauchVMResponse();
    }

    protected Object takeSnapShot(TakeSnapShot req, StreamFuture snap) {
        try {
            VMSnapShot delta = factory.createVMSnapShot();
            delta.load(snap.getPath());
            if (!service.isPrimary()) {
                fullSnap.merge(service.getBase(), delta);
            }
            delta.remove();
        } catch (VMException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getCommited() {
        try {
            logging.debug(LOG_NAME, "transfer state=" + fullSnap.getPath());
            return new CommitedState(fullSnap.getPath(), vnet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void commit(Object state) {
        CommitedState commited = (CommitedState) state;
        try {
            if (fullSnap == null) fullSnap = factory.createVMSnapShot();
            fullSnap.load(commited.getFullSnap().getPath());
            vnet = commited.getVMNetwork();
            service.setVMNetwork(vnet);
            logging.debug(LOG_NAME, "recovery from=" + fullSnap.getPath());
        } catch (VMException e) {
            e.printStackTrace();
        }
    }
}
