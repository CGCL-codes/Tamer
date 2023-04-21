package org.deft.tuba.integrator.dragdrop;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.navigator.CommonDragAdapterAssistant;
import org.eclipse.ui.part.ResourceTransfer;

public class SVNDragAdapterAssistant extends CommonDragAdapterAssistant {

    public SVNDragAdapterAssistant() {
        System.out.println("ddddddddddddddddddddd");
    }

    @Override
    public Transfer[] getSupportedTransferTypes() {
        System.out.println("ddddddddddddddddddddd");
        return new Transfer[] { TextTransfer.getInstance(), FileTransfer.getInstance(), LocalSelectionTransfer.getTransfer(), ResourceTransfer.getInstance() };
    }

    @Override
    public boolean setDragData(DragSourceEvent anEvent, IStructuredSelection aSelection) {
        System.out.println("ddddddddddddddddddddd");
        return false;
    }
}
