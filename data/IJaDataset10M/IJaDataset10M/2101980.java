package org.apache.harmony.awt.datatransfer.linux;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DropTargetContextPeer;
import org.apache.harmony.awt.ContextStorage;
import org.apache.harmony.awt.internal.nls.Messages;
import org.apache.harmony.awt.wtk.linux.LinuxEventQueue;

public class LinuxDropTarget implements DropTargetContextPeer {

    private final LinuxDTK dtk;

    private final LinuxEventQueue nativeQueue;

    private final DropTargetContext context;

    private Transferable transferable;

    public LinuxDropTarget(LinuxDTK dtk, DropTargetContext context) {
        this.dtk = dtk;
        this.context = context;
        nativeQueue = (LinuxEventQueue) ContextStorage.getNativeEventQueue();
    }

    public void acceptDrag(int dragAction) {
    }

    public void acceptDrop(int dropAction) {
    }

    public void dropComplete(boolean success) {
    }

    public DropTarget getDropTarget() {
        return context.getDropTarget();
    }

    public int getTargetActions() {
        return context.getDropTarget().getDefaultActions();
    }

    public DataFlavor[] getTransferDataFlavors() {
        return (transferable != null) ? transferable.getTransferDataFlavors() : new DataFlavor[0];
    }

    public Transferable getTransferable() throws InvalidDnDOperationException {
        if (transferable == null) {
            throw new InvalidDnDOperationException(Messages.getString("awt.07"));
        }
        return transferable;
    }

    public boolean isTransferableJVMLocal() {
        return false;
    }

    public void rejectDrag() {
    }

    public void rejectDrop() {
    }

    public void setTargetActions(int actions) {
        context.getDropTarget().setDefaultActions(actions);
    }
}
